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
