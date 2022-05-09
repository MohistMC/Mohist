/*
 * MohistMC
 * Copyright (C) 2019-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.api;

import java.util.HashMap;
import java.util.Map;

public class CooldownAPI {

    private static final Map<String, CooldownAPI> cooldowns = new HashMap<>();
    private final int timeInSeconds;
    private final String id;
    private final String cooldownName;
    private long start;

    public CooldownAPI(String id, String cooldownName, int timeInSeconds) {
        this.id = id;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }

    public static boolean isInCooldown(String id, String cooldownName) {
        if (getTimeLeft(id, cooldownName) >= 1) {
            return true;
        }
        stop(id, cooldownName);
        return false;
    }

    private static void stop(String id, String cooldownName) {
        CooldownAPI.cooldowns.remove(id + cooldownName);
    }

    private static CooldownAPI getCooldown(String id, String cooldownName) {
        return CooldownAPI.cooldowns.get(id + cooldownName);
    }

    public static int getTimeLeft(String id, String cooldownName) {
        CooldownAPI cooldown = getCooldown(id, cooldownName);
        int f = -1;
        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int totalTime = cooldown.timeInSeconds;
            int r = (int) (now - cooldownTime) / 1000;
            f = (r - totalTime) * -1;
        }
        return f;
    }

    public static double getTimeLeftDouble(String id, String cooldownName) {
        CooldownAPI cooldown = getCooldown(id, cooldownName);
        double f = -1.0;
        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int totalTime = cooldown.timeInSeconds;
            double r = (now - cooldownTime) / 1000;
            f = (double) (r - totalTime) * -1;
        }
        return f;
    }

    public void start() {
        this.start = System.currentTimeMillis();
        CooldownAPI.cooldowns.put(this.id + this.cooldownName, this);
    }
}
