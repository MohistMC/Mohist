package cc.uraniummc.capture;

import cc.uraniummc.capture.type.ACapture;
import cc.uraniummc.capture.type.CaptureBlock;
import cc.uraniummc.capture.type.CaptureTree;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class WorldCapture{

    public final World mWorld;
    /** Capture任务Id生成器 */
    private AtomicInteger mIdCreate=new AtomicInteger();
    /** 方块捕获任务 */
    public Stack<CaptureBlock> mBlockCaptures=new Stack<CaptureBlock>();
    /** 树捕获任务 */
    public Stack<CaptureTree> mTreeCaptures=new Stack<CaptureTree>();

    public WorldCapture(World pWorld){
        this.mWorld=pWorld;
    }

    public CaptureBlock getLastBlockCapture(){
        return getEleOrLast(this.mBlockCaptures,-1);
    }

    public CaptureTree getLastTreeGenCapture(){
        return getEleOrLast(this.mTreeCaptures,-1);
    }

    public static <T extends ACapture> T getEleOrLast(Stack<T> pStack,int pId){
        if(pId!=-1){
            Iterator<T> tIt=pStack.iterator();
            while(tIt.hasNext()){
                T tCapture=tIt.next();
                if(tCapture.mHandled){
                    tIt.remove();
                }else if(tCapture.mId==pId){
                    return tCapture;
                }
            }
        }

        while(!pStack.isEmpty()){
            T tCapture=pStack.lastElement();
            if(tCapture.mHandled){
                pStack.pop();
                continue;
            }
            return tCapture;
        }

        return null;
    }

    public void addCaptureEntity(World pWorld,Entity pEntity,SpawnReason pReason){
        CaptureBlock tCapture=this.getLastBlockCapture();
        if(tCapture==null||!tCapture.mEnable) return;

        tCapture.addCaptureEntity(pWorld,pEntity,pReason);
    }

    public void addCaptureItem(EntityPlayer pPlayer,ItemStack pItem){
        CaptureBlock tCapture=this.getLastBlockCapture();
        if(tCapture==null||!tCapture.mEnable) return;

        tCapture.addCaptureItem(pPlayer,pItem);
    }

    public void addCheckedBlock(World pWorld,int pPosX,int pPosY,int pPosZ){
        CaptureBlock tCapture=this.getLastBlockCapture();
        if(tCapture==null||!tCapture.mEnable) return;

        tCapture.addCheckedBlock(pWorld,pPosX,pPosY,pPosZ);
    }

    public boolean isCapture(){
        CaptureBlock tCapture=this.getLastBlockCapture();
        return tCapture!=null&&tCapture.mEnable;
    }

    public boolean isTreeGenCapture(){
        CaptureTree tCapture=this.getLastTreeGenCapture();
        return tCapture!=null&&tCapture.mEnable;
    }

    public CaptureBlock startCapture(EntityPlayer pPlayer){
        ItemStack tItem=null;
        int tSlot=-1;
        if(pPlayer!=null){
            tSlot=pPlayer.inventory.currentItem;
            tItem=pPlayer.getCurrentEquippedItem();
        }
        return this.startCapture(pPlayer,tItem,tSlot);

    }

    public CaptureBlock startCapture(EntityPlayer pPlayer,ItemStack pCurrent){
        return this.startCapture(pPlayer,pCurrent,-1);
    }

    public CaptureBlock startCapture(EntityPlayer pPlayer,ItemStack pCurrent,int pSlot){
        CaptureBlock tCapture=this.mBlockCaptures.push(new CaptureBlock(this,this.mIdCreate.incrementAndGet(),pPlayer));
        tCapture.mUseItem=pCurrent;
        if(pCurrent!=null){
            tCapture.mOldItemMeta=pCurrent.getItemDamage();
            tCapture.mOldItemSize=pCurrent.stackSize;
            tCapture.mOldItemNBT=pCurrent.stackTagCompound;
            tCapture.mSlot=pSlot;
        }
        return tCapture;
    }

    public void disableCapture(){
        CaptureBlock tCapture=this.getLastBlockCapture();
        if(tCapture==null) return;

        tCapture.mEnable=false;
    }

    public boolean endCapture(int pId){
        CaptureBlock tCapture=getEleOrLast(this.mBlockCaptures,pId);
        if(tCapture==null) return true;

        this.mBlockCaptures.remove(tCapture);
        return tCapture.endCapture();

    }

    public CaptureTree startTreeGenCapture(EntityPlayer pPlayer,int pPosX,int pPosY,int pPosZ){
        CaptureTree tCapture=this.mTreeCaptures.push(new CaptureTree(this,this.mIdCreate.incrementAndGet(),pPlayer));
        tCapture.setAgaistPostionAndSide(-1,pPosX,pPosY,pPosZ);
        return tCapture;
    }

    public CaptureTree startTreeGenCapture(EntityPlayer pPlayer,org.bukkit.Location pTreeLoc){
        return this.startTreeGenCapture(pPlayer,pTreeLoc.getBlockX(),pTreeLoc.getBlockY(),pTreeLoc.getBlockZ());
    }

    public CaptureTree disableTreeGenCapture(int pId){
        CaptureTree tCapture=getEleOrLast(this.mTreeCaptures,pId);
        if(tCapture==null) return null;

        tCapture.mEnable=false;
        return tCapture;
    }

    public boolean endTreeGenCapture(int pId){
        CaptureTree tCapture=getEleOrLast(this.mTreeCaptures,pId);
        if(tCapture==null) return true;

        this.mTreeCaptures.remove(tCapture);
        return tCapture.endCapture();
    }

    /**
     * 清理捕获的数据
     */
    public void clearAllData(){
        this.mBlockCaptures.clear();
        this.mTreeCaptures.clear();
    }

}
