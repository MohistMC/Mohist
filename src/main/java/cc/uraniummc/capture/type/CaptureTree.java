package cc.uraniummc.capture.type;

import cc.uraniummc.capture.WorldCapture;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.BlockSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;

public class CaptureTree extends ACapture{

    /** 捕获数模式下的位置 */
    public org.bukkit.Location mTreeLoc=null;

    public CaptureTree(WorldCapture pWorldCapture, int pId, EntityPlayer pPlayer){
        super(pWorldCapture,pId,pPlayer);
    }

    @Override
    public void setAgaistPostionAndSide(int pSide,int pPosX,int pPosY,int pPosZ){
        super.setAgaistPostionAndSide(pSide,pPosX,pPosY,pPosZ);
        this.mTreeLoc=new org.bukkit.Location(this.mWorld.getWorld(),(double)pPosX,(double)pPosY,(double)pPosZ);
    }

    @Override
    public void markHandled(){
        super.markHandled();
        this.mWorldCapture.mTreeCaptures.remove(this);
    }

    public boolean endCapture(){
        if(this.mHandled) return true;
        this.markHandled();

        boolean tResult=true;
        if(this.mCapturedBlocks.isEmpty())
            return true;

        TreeType tType=BlockSapling.treeType;
        BlockSapling.treeType=null;
        List<BlockState> tStates=new ArrayList();

        for(BlockSnapshot sSnapshot : (List<BlockSnapshot>)this.mCapturedBlocks.clone()){
            tStates.add(new CraftBlockState(sSnapshot));
        }

        StructureGrowEvent tEvent=null;
        if(tType!=null){
            tEvent=new StructureGrowEvent(this.mTreeLoc,tType,false,
                    this.mCapturePlayer==null?null:(Player)this.mCapturePlayer.getBukkitEntity(),tStates);
            Bukkit.getPluginManager().callEvent(tEvent);
        }

        if(tEvent==null||!tEvent.isCancelled()){
            for(BlockState sState : tStates){
                sState.update(true);
            }
            tResult=true;
        }

        return tResult;
    }

}
