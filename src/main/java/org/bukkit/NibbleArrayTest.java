package org.bukkit;

import java.util.Random;
import net.minecraft.world.chunk.NibbleArray;
import org.junit.Assert;
import org.junit.Test;

public class NibbleArrayTest {

    private static final int NIBBLE_SIZE = 4096;

    @Test
    public void testNibble() {
        Random random = new Random();
        byte[] classic = new byte[NIBBLE_SIZE];
        NibbleArray nibble = new NibbleArray();

        for (int i = 0; i < classic.length; i++) {
            byte number = (byte) (random.nextInt() & 0xF);

            classic[i] = number;
            nibble.setIndex(i, number);
        }

        for (int i = 0; i < classic.length; i++) {
            Assert.assertEquals("Nibble array mismatch", classic[i], nibble.getFromIndex(i));
        }
    }
}
