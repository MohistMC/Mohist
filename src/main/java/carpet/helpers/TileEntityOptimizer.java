package carpet.helpers;
//AUTHOR: PallaPalla
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockHopper;

// This class contains the code responsible for optimizing tile entities by making them sleep until they receive an update.
// It contains an interface that all optimized tile entities must implement, and the code responsible for propagating the updates.
public class TileEntityOptimizer
{

    // All optimized tile entities must implement this interface so that the world object knows it should wake them up.
    // A tile entity that implements this interface should set a sleeping flag if it becomes unused.
    // This sleeping flag should causes it to skip all or part of its update() method and be reset by wakeUp().
    public interface ILazyTileEntity extends ITickable
    {
        /**
         * CARPET-optimizedTileEntities: Wakes up the tile entity so it updates again. Called upon receiving a comparator update in
         * {@linkplain net.minecraft.world.World#updateComparatorOutputLevel(net.minecraft.util.math.BlockPos, net.minecraft.block.Block)}
         */
        public void wakeUp();
    }

    // The method called by the world object when a comparator update happens. Wakes up the tile entity causing it, and nearby hoppers.
    // Some code here is copied from world.updateComparatorOutputLevel() to perform the vanilla comparator updates.
    public static void updateComparatorsAndLazyTileEntities(World worldIn, BlockPos pos, Block blockIn)
    {
        // Wake up the tile entity that caused the comparator update
        if (blockIn.hasTileEntity())
        {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof ILazyTileEntity)
            {
                ((ILazyTileEntity) tileEntity).wakeUp();
            }
        }

        // Perform the usual comparator updates horizontally
        // Additionally iterate over the up and down directions, since hoppers can also be vertical
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            BlockPos blockpos = pos.offset(enumfacing);
            boolean horizontal = enumfacing.getAxis() != Axis.Y;

            if (worldIn.isBlockLoaded(blockpos))
            {
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                // Check for comparators like in vanilla. This check is only performed horizontally, as comparators are only horizontal
                if (horizontal && Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate))
                {
                    iblockstate.neighborChanged(worldIn, blockpos, blockIn, pos);
                }
                else if (horizontal && iblockstate.isNormalCube())
                {
                    blockpos = blockpos.offset(enumfacing);
                    iblockstate = worldIn.getBlockState(blockpos);

                    if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate))
                    {
                        iblockstate.neighborChanged(worldIn, blockpos, blockIn, pos);
                    }
                }

                // Wake up nearby hoppers. Only hoppers under the block (pulling) or pointing into it (pushing) should be woken up
                else if (iblockstate.getBlock() == Blocks.HOPPER)
                {
                    TileEntity tileEntity = worldIn.getTileEntity(blockpos);
                    if((enumfacing == EnumFacing.DOWN || enumfacing == BlockHopper.getFacing(tileEntity.getBlockMetadata()).getOpposite())
                            && tileEntity instanceof ILazyTileEntity)
                    {
                        ((ILazyTileEntity) tileEntity).wakeUp();
                    }
                }
            }
        }
    }
}