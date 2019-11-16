package thermos.thermite;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class ThermiteTeleporter extends Teleporter
{


        public ThermiteTeleporter(WorldServer world)
        {
            super(world);
        }

        @Override
        public boolean placeInExistingPortal(Entity e, double x, double y, double z, float rY)
        {
            e.setLocationAndAngles(x, y, z, rY, e.rotationPitch);
            return true;
        }

        @Override
        public void removeStalePortalLocations(long totalWorldTime) { }

        @Override
        public void placeInPortal(Entity e, double x, double y, double z, float rY)
        {
            placeInExistingPortal(e, x, y, z, rY);
        }

}