package helpers;

import java.util.Map;

import CostumExceptions.NotExpectedParameter;

import java.util.HashMap;

public class viewHelper {

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    public static String colorText(String Text, String Color) {

        Map<String, String> colors = new HashMap<>();
        colors.put("red", "\u001b[31m");
        colors.put("green", "\u001B[32m");
        colors.put("yellow", "\u001b[33m");
        colors.put("blue", "\u001b[34m");
        colors.put("magenta", "\u001b[35m");
        colors.put("cyan", "\u001b[36m");
        colors.put("white", "\u001b[37m");

        String endColor = "\u001B[0m";

        String AnsiCode = colors.get(Color);

        if (AnsiCode == null) {
            throw new NotExpectedParameter(
                    "Not Expected Parameter (Choose only : red , green , yellow , blue, magenta , cyan , white)");
        }

        System.out.println(AnsiCode + Text + endColor);

        return null;
    }

}
