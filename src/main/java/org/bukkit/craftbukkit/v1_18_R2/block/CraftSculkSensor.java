package org.bukkit.craftbukkit.v1_18_R2.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import org.bukkit.World;
import org.bukkit.block.SculkSensor;

public class CraftSculkSensor extends CraftBlockEntityState<SculkSensorBlockEntity> implements SculkSensor {

    public CraftSculkSensor(World world, final SculkSensorBlockEntity te) {
        super(world, te);
    }

    @Override
    public int getLastVibrationFrequency() {
        return getSnapshot().getLastVibrationFrequency();
    }

    @Override
    public void setLastVibrationFrequency(int lastVibrationFrequency) {
        Preconditions.checkArgument(0 <= lastVibrationFrequency && lastVibrationFrequency <= 15, "Vibration frequency must be between 0-15");
        getSnapshot().lastVibrationFrequency = lastVibrationFrequency;
    }
}
