package cc.uraniummc.capture.type;

import cc.uraniummc.capture.WorldCapture;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class ACapture{

    public final WorldCapture mWorldCapture;
    public final World mWorld;
    public final int mId;
    /** 是否启用捕获 */
    public boolean mEnable=true;
    /** 是否已经处理完毕 */
    public boolean mHandled=false;
    /** 当前捕获作用的玩家,可能为null,用作普通捕获 */
    public final EntityPlayer mCapturePlayer;
    /** 方块放置时顶着的面 */
    protected ForgeDirection mSide=ForgeDirection.UNKNOWN;
    protected Block mAgaistBlock=Blocks.air;
    /** 捕获的数据 */
    public ArrayList<BlockSnapshot> mCapturedBlocks=new ArrayList<BlockSnapshot>();

    public ACapture(WorldCapture pWorldCapture,int pId,EntityPlayer pPlayer){
        this.mWorldCapture=pWorldCapture;
        this.mWorld=pWorldCapture.mWorld;
        this.mId=pId;
        this.mCapturePlayer=pPlayer;
    }

    public void setAgaistPostionAndSide(int pSide,int pPosX,int pPosY,int pPosZ){
        this.mSide=ForgeDirection.getOrientation(pSide);
        this.mAgaistBlock=this.mWorldCapture.mWorld.getBlock(pPosX,pPosY,pPosZ);
    }

    protected BlockSnapshot addCapturedBlock(World pWorld,int pPosX,int pPosY,int pPosZ){
        return this.addCapturedBlock(pWorld,pPosX,pPosY,pPosZ,3);
    }

    protected BlockSnapshot addCapturedBlock(World pWorld,int pPosX,int pPosY,int pPosZ,int pFlag){
        return this.addCapturedBlock(BlockSnapshot.getBlockSnapshot(pWorld,pPosX,pPosY,pPosZ,pFlag));
    }

    protected BlockSnapshot addCapturedBlock(BlockSnapshot pSnapshot){
        if(!this.mEnable) return pSnapshot;

        this.mCapturedBlocks.add(pSnapshot);
        return pSnapshot;
    }

    public void disableCapture(){
        this.mEnable=false;
    }

    /**
     * 标记此捕获为已经处理
     * <p>
     * 只设置捕获中的数据,并从世界捕获中移除,不会清理捕获数据
     * </p>
     */
    public void markHandled(){
        this.mEnable=false;
        this.mHandled=true;
    }

}
