package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;


public class Shell {

    private static final String CMD_EXIT = "exit";
    private static final String INVALID_COMMAND_MSG = "Invalid Command";
    private static final String RES_ERROR_MSG = "Cannot change resolution any more";
    private static final String RES_CHANGE_MSG = "Width set to ";
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String INITIAL_CHARS_RANGE = "0-9";

    private final int minCharsInRow;
    private final int maxCharsInRow;
    private final BrightnessImgCharMatcher charMatcher;
    private final HtmlAsciiOutput output;
    private int charsInRow;
    private Image img;
    private Set<Character> charSet = new HashSet<>();


    /**
     * constructor
     *
     * @param img the image to run the shell on
     */
    public Shell(Image img) {
        this.img = img;
        this.minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        addChars(INITIAL_CHARS_RANGE);
    }

    /**
     * as instructed
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String cmd = scanner.nextLine().trim();
        String[] words = cmd.split("\\s+");
        while (!words[0].toLowerCase().equals(CMD_EXIT)) {
            if (!words[0].equals("")) {
                String param = "";
                if (words.length > 1) {
                    param = words[1];
                }
                switch (words.length) {
                    case 1:
                        switch (words[0]) {
                            case "chars":
                                showChars();
                                break;
                            case "console":
                                console();
                                break;
                            case "render":
                                render();
                                break;
                            default:
                                System.out.println(INVALID_COMMAND_MSG);
                        }
                        break;
                    case 2:
                        switch (words[0]) {
                            case "add":
                                addChars(param);
                                break;
                            case "remove":
                                removeChars(param);
                                break;
                            case "res":
                                resChange(param);
                                break;
                            default:
                                System.out.println(INVALID_COMMAND_MSG);
                        }
                        break;
                    default:
                        System.out.println(INVALID_COMMAND_MSG);
                }
            }
            System.out.print(">>> ");
            cmd = scanner.nextLine().trim();
            words = cmd.split("\\s+");
        }
    }

    /**
     * prints ascii art to html file
     */
    private void render() {
        Character[] charArray = setToArray(charSet); // for api
        char[][] asciiArt = charMatcher.chooseChars(charsInRow, charArray);
        output.output(asciiArt);
    }

    /**
     * converts set to array (for api)
     * @param charSet the set
     * @return the array
     */
    private Character[] setToArray(Set<Character> charSet){
        Character[] charArray = new Character[charSet.size()];
        int i = 0;
        for(char c : charSet){
            charArray[i] = c;
            i++;
        }
        return charArray;
    }

    /**
     * prints ascii art to console
     */
    private void console() {
        Character[] charArray = setToArray(charSet); // for api
        char[][] asciiArt = charMatcher.chooseChars(charsInRow, charArray);
        for (int j = 0; j < asciiArt.length; j++) {
            for (int k = 0; k < asciiArt[j].length; k++) {
                System.out.print(asciiArt[j][k]);
            }
            System.out.println();
        }
    }

    /**
     * prints the chars in charSet
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c -> System.out.print(c + " "));
        System.out.println();
    }

    /**
     * parses the parameter of the command given to shell to the range of chars to add/remove
     * @param param the paramater
     * @return array with first and last char in range
     */
    private static char[] parseCharRange(String param) {
        if (param.equals("all")) {
            return new char[]{' ', '~'};
        }
        if (param.equals("space")) {
            return new char[]{' ', ' '};
        }
        if (param.length() == 1)
            return new char[]{param.charAt(0), param.charAt(0)};
        if (param.length() == 3) {
            if (param.charAt(1) == '-') {
                // order array
                return new char[]{(char) Math.min(param.charAt(0), param.charAt(2)),
                        (char) Math.max(param.charAt(0), param.charAt(2))};
            }
        } else {
            System.out.println(INVALID_COMMAND_MSG);
        }
        return null;
    }


    /**
     * as in campus site
     * @param s
     */
    private void addChars(String s) {
        char[] range = parseCharRange(s);
        if (range != null) {
            Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::add);
        }
    }

    /**
     * as in campus site
     * @param s
     */
    private void removeChars(String s) {
        char[] range = parseCharRange(s);
        if (range != null) {
            Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::remove);
        }
    }

    /**
     * changes resolution
     * @param s the command to change the resolution to
     */
    private void resChange(String s){
        switch(s){
            case "up":
                if(charsInRow * 2 <= maxCharsInRow){
                    charsInRow = charsInRow * 2;
                    System.out.println(RES_CHANGE_MSG + charsInRow);
                }
                else{
                    System.out.println(RES_ERROR_MSG);
                }
                break;
            case "down":
                if(charsInRow / 2 >= minCharsInRow){
                    charsInRow = charsInRow / 2;
                    System.out.println(RES_CHANGE_MSG + charsInRow);
                }
                else{
                    System.out.println(RES_ERROR_MSG);
                }
                break;
            default:
                System.out.println(INVALID_COMMAND_MSG);
        }
    }


}
