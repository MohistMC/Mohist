package red.mohist.test;

import red.mohist.api.CooldownAPI;

public class CooldownTest {

    public static void main(String[] args) {

        CooldownAPI cooldown = new CooldownAPI("mohist", "cd", 15);
        if (!CooldownAPI.isInCooldown("mohist", "cd")) {
            cooldown.start();
        } else {
            System.out.println("Remaining cooldown: " + CooldownAPI.getTimeLeft("mohist", "cd") + "s");
        }
    }
}
