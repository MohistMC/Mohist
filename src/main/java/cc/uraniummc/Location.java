package cc.uraniummc;

import net.minecraft.world.World;

public class Location{

    public World mWorld;

    public int mPosX=0;
    public int mPosY=0;
    public int mPosZ=0;

    private String mToString=null;

    public Location(World pWorld,int pPosX,int pPosY,int pPosZ){
        this.mWorld=pWorld;
        this.mPosX=pPosX;
        this.mPosY=pPosY;
        this.mPosZ=pPosZ;
    }

    @Override
    public String toString(){
        if(this.mToString==null){
            this.mToString=this.mWorld.worldInfo.getWorldName()+"("+this.mPosX+","+this.mPosY+","+this.mPosZ+")";
        }
        return this.mToString;
    }

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object pObj){
        return pObj instanceof Location&&((Location)pObj).hashCode()==this.hashCode();
    }

}
