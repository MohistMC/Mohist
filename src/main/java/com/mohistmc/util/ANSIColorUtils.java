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

package com.mohistmc.util;

public class ANSIColorUtils {

    public static String getColor(String text, String d) {
        switch (text) {
            case "1":
                text = "\u001B[34;22m";
                break;
            case "2":
                text = "\u001B[32;22m";
                break;
            case "3":
                text = "\u001B[36;22m";
                break;
            case "4":
                text = "\u001B[31;22m";
                break;
            case "5":
                text = "\u001B[35;22m";
                break;
            case "6":
                text = "\u001B[33;22m";
                break;
            case "7":
                text = "\u001B[37;22m";
                break;
            case "8":
                text = "\u001B[30;1m";
                break;
            case "9":
                text = "\u001B[34;1m";
                break;
            case "a":
                text = "\u001B[32;1m";
                break;
            case "b":
                text = "\u001B[36;1m";
                break;
            case "c":
                text = "\u001B[31;1m";
                break;
            case "d":
                text = "\u001B[35;1m";
                break;
            case "e":
                text = "\u001B[33;1m";
                break;
            case "f":
                text = "\u001B[37;1m";
                break;
            case "r":
                text = "\u001B[39;0m";
                break;
            default:
                text = d;
        }
        return text;
    }
}
