package rtl.extract.display;

public class Display {

    private static String lastDisplay;

    public static void display(String s) {
        System.out.println(s);
    }

    public static void displayProgression(String s) {
        if (lastDisplay == null) {
            lastDisplay = s;
        }
        for (int j = 0; j < lastDisplay.length(); j++)
            System.out.print("\b");

        lastDisplay = s;
        System.out.print(s);
    }
}
