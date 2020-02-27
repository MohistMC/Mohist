package red.mohist.util;

public class ANSIColorUtils {

    public static String getColor(String text, String d) {
        switch (text) {
            case "1":
                text = "\u001b[0;34;22m";
                break;
            case "2":
                text = "\u001b[0;32;22m";
                break;
            case "3":
                text = "\u001b[0;36;22m";
                break;
            case "4":
                text = "\u001b[0;31;22m";
                break;
            case "5":
                text = "\u001b[0;35;22m";
                break;
            case "6":
                text = "\u001b[0;33;22m";
                break;
            case "7":
                text = "\u001b[0;37;22m";
                break;
            case "8":
                text = "\u001b[0;30;1m";
                break;
            case "9":
                text = "\u001b[0;34;1m";
                break;
            case "a":
                text = "\u001b[0;32;1m";
                break;
            case "b":
                text = "\u001b[0;36;1m";
                break;
            case "c":
                text = "\u001b[0;31;1m";
                break;
            case "d":
                text = "\u001b[0;35;1m";
                break;
            case "e":
                text = "\u001b[0;33;1m";
                break;
            case "f":
                text = "\u001b[0;37;1m";
                break;
            default:
                text = d;
        }
        return text;
    }
}
