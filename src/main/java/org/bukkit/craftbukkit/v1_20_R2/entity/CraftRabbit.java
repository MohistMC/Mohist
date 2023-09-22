package org.bukkit.craftbukkit.v1_20_R2.entity;

import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Rabbit;

public class CraftRabbit extends CraftAnimals implements Rabbit {

    public CraftRabbit(CraftServer server, net.minecraft.world.entity.animal.Rabbit entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Rabbit getHandle() {
        return (net.minecraft.world.entity.animal.Rabbit) entity;
    }

    @Override
    public String toString() {
        return "CraftRabbit{RabbitType=" + getRabbitType() + "}";
    }

    @Override
    public Type getRabbitType() {
        return Type.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setRabbitType(Type type) {
        getHandle().setVariant(net.minecraft.world.entity.animal.Rabbit.Variant.values()[type.ordinal()]);
    }

    private static class CraftMagicMapping {

        private static final int[] types = new int[Type.values().length];
        private static final Type[] reverse = new Type[Type.values().length];

        static {
            set(Type.BROWN, 0);
            set(Type.WHITE, 1);
            set(Type.BLACK, 2);
            set(Type.BLACK_AND_WHITE, 3);
            set(Type.GOLD, 4);
            set(Type.SALT_AND_PEPPER, 5);
            set(Type.THE_KILLER_BUNNY, 99);
        }

        private static void set(Type type, int value) {
            types[type.ordinal()] = value;
            if (value < reverse.length) {
                reverse[value] = type;
            }
        }

        public static Type fromMagic(int magic) {
            if (magic >= 0 && magic < reverse.length) {
                return reverse[magic];
            } else if (magic == 99) {
                return Type.THE_KILLER_BUNNY;
            } else {
                return null;
            }
        }

        public static int toMagic(Type type) {
            return types[type.ordinal()];
        }
    }
}
