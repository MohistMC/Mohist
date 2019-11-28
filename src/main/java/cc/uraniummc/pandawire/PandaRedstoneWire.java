package cc.uraniummc.pandawire;

import net.minecraft.block.BlockRedstoneWire;

/**
 * Created by xjboss on 2017/9/10.
 * @author Panda4994,jiongjionger
 */
public class PandaRedstoneWire  extends BlockRedstoneWire {
    /*
    // facing方向的枚举
    private static final EnumFacing[] facingsHorizontal = { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH };
    // facing垂直的枚举
    private static final EnumFacing[] facingsVertical = { EnumFacing.DOWN, EnumFacing.UP };
    // 上述两者之和
    private static final EnumFacing[] facings = ArrayUtils.addAll(facingsVertical, facingsHorizontal);
    // facing的X、Y、Z的偏移量位置数组
    private static final Vec3i[] surroundingBlocksOffset;
    // 初始化surroundingBlocksOffset
    static {
        Set<Vec3i> set = Sets.newLinkedHashSet();
        for (EnumFacing facing : facings) {
            set.add(PandaWireReflectUtil.getOfT(facing, Vec3i.class));
        }
        for (EnumFacing facing1 : facings) {
            Vec3i v1 = PandaWireReflectUtil.getOfT(facing1, Vec3i.class);
            for (EnumFacing facing2 : facings) {
                Vec3i v2 = PandaWireReflectUtil.getOfT(facing2, Vec3i.class);
                set.add(new BlockPos(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ()));
            }
        }
        set.remove(BlockPos.ZERO);
        surroundingBlocksOffset = set.toArray(new Vec3i[set.size()]);
    }
    */
    /*
     * 作者说用LinkedHashSet替代arraylist没有明显的性能提示，红石设备不多的时候的确如此
     * 但是实测在红石设备数量很大的时候，有2~5%的性能提升（基于PaperSpigot1.10.2测试），所以还是改用LinkedHashSet来实现
     */
    /*
    // 需要被断路的红石位置
    private Set<BlockPos> turnOff = Sets.newLinkedHashSet();
    // 需要被激活的红石位置
    private Set<BlockPos> turnOn = Sets.newLinkedHashSet();

    // 已经更新的红石线路
    private final Set<BlockPos> updatedRedstoneWire = Sets.newLinkedHashSet();

    private boolean g = true;

    public PandaRedstoneWire() {
        c(0.0F);
        a(Block.e);
        c("redstoneDust");
        K();
    }

    @Override
    public int a(final IBlockAccess iblockaccess, final BlockPos BlockPos, final IBlockData iblockdata, final EnumFacing EnumFacing) {
        if (!this.g) {
            return 0;
        }
        int i = iblockdata.get(BlockRedstoneWire.POWER);
        if (i == 0) {
            return 0;
        }
        if (EnumFacing == EnumFacing.UP) {
            return i;
        }
        if (getSidesToPower((World) iblockaccess, BlockPos).contains(EnumFacing)) {
            return i;
        }
        return 0;
    }

    private void addAllSurroundingBlocks(final BlockPos pos, final Set<BlockPos> set) {
        for (Vec3i vect : surroundingBlocksOffset) {
            set.add(pos.a(vect));
        }
    }

    private void addBlocksNeedingUpdate(final World worldIn, final BlockPos pos, final Set<BlockPos> set) {
        List<EnumFacing> connectedSides = getSidesToPower(worldIn, pos);
        for (EnumFacing facing : facings) {
            BlockPos offsetPos = pos.shift(facing);
            if (((connectedSides.contains(facing.opposite())) || (facing == EnumFacing.DOWN) || ((facing.k().c()) && (a(worldIn.getType(offsetPos), facing)))) &&
                    (canBlockBePoweredFromSide(worldIn.getType(offsetPos), facing, true))) {
                set.add(offsetPos);
            }
        }
        for (EnumFacing facing : facings) {
            BlockPos offsetPos = pos.shift(facing);
            if (((connectedSides.contains(facing.opposite())) || (facing == EnumFacing.DOWN)) &&
                    (worldIn.getType(offsetPos).getBlock().isOccluding())) {
                for (EnumFacing facing1 : facings) {
                    if (canBlockBePoweredFromSide(worldIn.getType(offsetPos.shift(facing1)), facing1, false)) {
                        set.add(offsetPos.shift(facing1));
                    }
                }
            }
        }
    }

    // 添加红石线路到需要被断路/激活的List
    private void addWireToList(final World worldIn, final BlockPos pos, final int otherPower) {
        final IBlockData state = worldIn.getType(pos);
        if (state.getBlock() == this) {
            int power = state.get(POWER);
            if ((power < otherPower - 1) && (!this.turnOn.contains(pos))) {
                this.turnOn.add(pos);
            }
            if ((power > otherPower) && (!this.turnOff.contains(pos))) {
                this.turnOff.add(pos);
            }
        }
    }

    private void calculateCurrentChanges(final World world, final BlockPos BlockPos) {
        if (world.getType(BlockPos).getBlock() == this) {
            this.turnOff.add(BlockPos);
        } else {
            checkSurroundingWires(world, BlockPos);
        }
        // 遍历递减充能等级
        while (!this.turnOff.isEmpty()) {
            Iterator<BlockPos> iter = this.turnOff.iterator();
            final BlockPos pos = iter.next();
            iter.remove();
            IBlockData state = world.getType(pos);
            int oldPower = state.get(POWER);
            this.g = false;
            int blockPower = world.A(pos);
            this.g = true;
            int wirePower = getSurroundingWirePower(world, pos);

            wirePower--;
            int newPower = Math.max(blockPower, wirePower);
            if (newPower < oldPower) {
                if ((blockPower > 0) && (!this.turnOn.contains(pos))) {
                    this.turnOn.add(pos);
                }
                state = setWireState(world, pos, state, 0);
            } else if (newPower > oldPower) {
                state = setWireState(world, pos, state, newPower);
            }
            checkSurroundingWires(world, pos);
        }
        while (!this.turnOn.isEmpty()) {
            Iterator<BlockPos> iter = this.turnOn.iterator();
            final BlockPos pos = iter.next();
            iter.remove();
            IBlockData state = world.getType(pos);
            int oldPower = state.get(POWER);
            this.g = false;
            int blockPower = world.A(pos);
            this.g = true;
            int wirePower = getSurroundingWirePower(world, pos);

            wirePower--;
            int newPower = Math.max(blockPower, wirePower);
            if (oldPower != newPower) {
                // 充能等级发现变化，触发BlockRedstoneEvent事件
                BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(BlockPos.getX(), BlockPos.getY(), BlockPos.getZ()), oldPower, newPower);
                world.getServer().getPluginManager().callEvent(event);
                newPower = event.getNewCurrent();
            }
            if (newPower > oldPower) {
                state = setWireState(world, pos, state, newPower);
            }
            checkSurroundingWires(world, pos);
        }
        this.turnOff.clear();
        this.turnOn.clear();
    }

    private boolean canBlockBePoweredFromSide(final IBlockData state, final EnumFacing side, final boolean isWire) {
        if (((state.getBlock() instanceof BlockPiston)) && (state.get(BlockPiston.FACING) == side.opposite())) {
            return false;
        }
        if (((state.getBlock() instanceof BlockDiodeAbstract)) && (state.get(BlockDirectional.FACING) != side.opposite())) {
            if ((isWire) && ((state.getBlock() instanceof BlockRedstoneComparator)) && (state.get(BlockDirectional.FACING).k() != side.k()) && (side.k().c())) {
                return true;
            }
            return false;
        }
        if (((state.getBlock() instanceof BlockRedstoneTorch)) && ((isWire) || (state.get(BlockTorch.FACING) != side))) {
            return false;
        }
        return true;
    }

    // 遍历facing枚举检测附近的红石线路
    private void checkSurroundingWires(final World worldIn, final BlockPos pos) {
        final IBlockData state = worldIn.getType(pos);
        int ownPower = 0;
        if (state.getBlock() == this) {
            ownPower = state.get(POWER);
        }
        for (EnumFacing facing : facingsHorizontal) {
            BlockPos offsetPos = pos.shift(facing);
            if (facing.k().c()) {
                addWireToList(worldIn, offsetPos, ownPower);
            }
        }
        for (EnumFacing facingVertical : facingsVertical) {
            BlockPos offsetPos = pos.shift(facingVertical);
            boolean solidBlock = worldIn.getType(offsetPos).getBlock().u();
            for (EnumFacing facingHorizontal : facingsHorizontal) {
                if (((facingVertical == EnumFacing.UP) && (!solidBlock))
                        || ((facingVertical == EnumFacing.DOWN) && (solidBlock) && (!worldIn.getType(offsetPos.shift(facingHorizontal)).getBlock().isOccluding()))) {
                    addWireToList(worldIn, offsetPos.shift(facingHorizontal), ownPower);
                }
            }
        }
    }

    private boolean d(final IBlockAccess iblockaccess, final BlockPos BlockPos, final EnumFacing EnumFacing) {
        BlockPos BlockPos1 = BlockPos.shift(EnumFacing);
        IBlockData iblockdata = iblockaccess.getType(BlockPos1);
        Block block = iblockdata.getBlock();
        boolean blockisOccluding = block.isOccluding();
        boolean upBlockisOccluding = iblockaccess.getType(BlockPos.up()).getBlock().isOccluding();
        return (!upBlockisOccluding) && (blockisOccluding) && (e(iblockaccess, BlockPos1.up()));
    }

    @Override
    public void doPhysics(final World world, final BlockPos BlockPos, final IBlockData iblockdata, final Block block) {
        if (!world.isClientSide) {
            if (canPlace(world, BlockPos)) {
                e(world, BlockPos, iblockdata);
            } else {
                b(world, BlockPos, iblockdata, 0);
                world.setAir(BlockPos);
            }
        }
    }

    private void e(final World world, final BlockPos BlockPos, final IBlockData iblockdata) {
        calculateCurrentChanges(world, BlockPos);
        Set<BlockPos> blocksNeedingUpdate = Sets.newLinkedHashSet();
        Iterator<BlockPos> iterator = this.updatedRedstoneWire.iterator();
        while (iterator.hasNext()) {
            addBlocksNeedingUpdate(world, iterator.next(), blocksNeedingUpdate);
        }
        Iterator<BlockPos> BlockPosIterator = Lists.newLinkedList(this.updatedRedstoneWire).descendingIterator();
        while (BlockPosIterator.hasNext()) {
            addAllSurroundingBlocks(BlockPosIterator.next(), blocksNeedingUpdate);
        }
        blocksNeedingUpdate.removeAll(this.updatedRedstoneWire);
        this.updatedRedstoneWire.clear();
        for (BlockPos pos : blocksNeedingUpdate) {
            world.d(pos, this);
        }
    }

    private List<EnumFacing> getSidesToPower(final World worldIn, final BlockPos pos) {
        List<EnumFacing> retval = Lists.newArrayList();
        for (EnumFacing facing : facingsHorizontal) {
            if (d(worldIn, pos, facing)) {
                retval.add(facing);
            }
        }
        if (retval.isEmpty()) {
            return Lists.newArrayList(facingsHorizontal);
        }
        boolean northsouth = (retval.contains(EnumFacing.NORTH)) || (retval.contains(EnumFacing.SOUTH));
        boolean eastwest = (retval.contains(EnumFacing.EAST)) || (retval.contains(EnumFacing.WEST));
        if (northsouth) {
            retval.remove(EnumFacing.EAST);
            retval.remove(EnumFacing.WEST);
        }
        if (eastwest) {
            retval.remove(EnumFacing.NORTH);
            retval.remove(EnumFacing.SOUTH);
        }
        return retval;
    }

    // 计算周围的红石线路来统计当前位置的红石充能等级
    private int getSurroundingWirePower(final World worldIn, final BlockPos pos) {
        int wirePower = 0;
        for (EnumFacing enumfacing : EnumFacing.EnumFacingLimit.HORIZONTAL) {
            BlockPos offsetPos = pos.shift(enumfacing);
            wirePower = getPower(worldIn, offsetPos, wirePower);
            if ((worldIn.getType(offsetPos).getBlock().isOccluding()) && (!worldIn.getType(pos.up()).getBlock().isOccluding())) {
                wirePower = getPower(worldIn, offsetPos.up(), wirePower);
            } else if (!worldIn.getType(offsetPos).getBlock().isOccluding()) {
                wirePower = getPower(worldIn, offsetPos.down(), wirePower);
            }
        }
        return wirePower;
    }

    @Override
    public void onPlace(final World world, final BlockPos BlockPos, final IBlockData iblockdata) {
        if (!world.isClientSide) {
            e(world, BlockPos, iblockdata);
            for (EnumFacing EnumFacing : EnumFacing.EnumFacingLimit.VERTICAL) {
                world.applyPhysics(BlockPos.shift(EnumFacing), this);
            }
            for (EnumFacing EnumFacing : EnumFacing.EnumFacingLimit.HORIZONTAL) {
                b(world, BlockPos.shift(EnumFacing));
            }
            for (EnumFacing EnumFacing : EnumFacing.EnumFacingLimit.HORIZONTAL) {
                BlockPos BlockPos1 = BlockPos.shift(EnumFacing);
                if (world.getType(BlockPos1).getBlock().isOccluding()) {
                    b(world, BlockPos1.up());
                } else {
                    b(world, BlockPos1.down());
                }
            }
        }
    }

    @Override
    public void remove(final World world, final BlockPos BlockPos, final IBlockData iblockdata) {
        super.remove(world, BlockPos, iblockdata);
        if (!world.isClientSide) {
            EnumFacing[] aEnumFacing = EnumFacing.values();
            int i = aEnumFacing.length;
            for (int j = 0; j < i; j++) {
                EnumFacing EnumFacing = aEnumFacing[j];
                world.applyPhysics(BlockPos.shift(EnumFacing), this);
            }
            e(world, BlockPos, iblockdata);
            for (EnumFacing EnumFacing : EnumFacing.EnumFacingLimit.HORIZONTAL) {
                b(world, BlockPos.shift(EnumFacing));
            }
            for (EnumFacing EnumFacing : EnumFacing.EnumFacingLimit.HORIZONTAL) {
                BlockPos BlockPos1 = BlockPos.shift(EnumFacing);
                if (world.getType(BlockPos1).getBlock().isOccluding()) {
                    b(world, BlockPos1.up());
                } else {
                    b(world, BlockPos1.down());
                }
            }
        }
    }

    private IBlockData setWireState(final World worldIn, final BlockPos pos, IBlockData state, final int power) {
        state = state.set(POWER, power);
        worldIn.setTypeAndData(pos, state, 2);
        this.updatedRedstoneWire.add(pos);
        return state;
    }
*/
}