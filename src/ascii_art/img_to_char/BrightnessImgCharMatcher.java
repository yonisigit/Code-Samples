package ascii_art.img_to_char;


import image.Image;

import java.awt.*;
import java.util.HashMap;

public class BrightnessImgCharMatcher {

    private static final int CHAR_RESOLUTION = 16;

    private Image image;
    private String font;
    private final HashMap<Image, Double> cache = new HashMap<>();


    /**
     * constructor
     *
     * @param image the image to convert
     * @param font  the font to use
     */
    public BrightnessImgCharMatcher(Image image, String font) {
        this.image = image;
        this.font = font;
    }

    /**
     * converts the image into a 2 dimensional array of ascii
     *
     * @param numCharsInRow the resolution of the ascii conversion
     * @param charSet       the chars used in conversion
     * @return the ascii array
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        double[] brightnessLevels = getCharBrightness(charSet);
        stretch(brightnessLevels);
        char[][] asciiImage = convertImageToAscii(image, numCharsInRow, charSet, brightnessLevels);
        return asciiImage;
    }

    /**
     * calculates brightess values of array of chars
     *
     * @param charSet the chars.
     * @return array of the brightness values
     */
    private double[] getCharBrightness(Character[] charSet) {
        double[] brightnessLevels = new double[charSet.length];
        for (int i = 0; i < charSet.length; i++) {
            boolean[][] booleanImg = CharRenderer.getImg(charSet[i], CHAR_RESOLUTION, font);
            brightnessLevels[i] = getRatio(booleanImg);
        }
        return brightnessLevels;
    }

    /**
     * calculates the true/false ration of a boolean 2 dimensional array
     * @param booleanImg the array
     * @return the ratio
     */
    private double getRatio(boolean[][] booleanImg) {
        int totalSum = 0;
        int trueSum = 0;
        for (int j = 0; j < booleanImg.length; j++) {
            for (int k = 0; k < booleanImg[j].length; k++) {
                if (booleanImg[j][k]) {
                    trueSum++;
                }
                totalSum++;
            }
        }
        return (double) trueSum / totalSum;
    }

    /**
     * linearly stretches the values of an array
     *
     * @param brightnessLevels the array
     */
    private void stretch(double[] brightnessLevels) {
        double[] maxMin = getMaxMin(brightnessLevels);
        double denominator = maxMin[0] - maxMin[1];
        if (denominator > 0) {
            for (int i = 0; i < brightnessLevels.length; i++) {
                brightnessLevels[i] = (brightnessLevels[i] - maxMin[1]) / denominator;
            }
        }
    }

    /**
     * finds max value in array
     *
     * @param brightnessLevels the array
     * @return max value
     */
    private double[] getMaxMin(double[] brightnessLevels) {
        double max = brightnessLevels[0];
        double min = brightnessLevels[0];
        for (int i = 0; i < brightnessLevels.length; i++) {
            if (brightnessLevels[i] > max) {
                max = brightnessLevels[i];
            }
            if (brightnessLevels[i] < min) {
                min = brightnessLevels[i];
            }
        }
        return new double[]{max, min};
    }

    /**
     * calculates the brightess of an image by converting to greyscale or gets brightness from cache
     * @param image the adress of the image
     * @return the brightness
     */
    private double getImageBrightness(Image image) {
        if (cache.containsKey(image)){  // get from cache
            return cache.get(image);
        }
        int totalSum = 0;
        double brightnessSum = 0;
        for (Color pixel : image.pixels()) {
            double grey = pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152 + pixel.getBlue() * 0.0722;
            brightnessSum += grey;
            totalSum++;
        }
        double brightness = brightnessSum / totalSum / 255;
        cache.put(image, brightness);  // save to cache
        return brightness;
    }

    /**
     * as in instructions
     *
     * @param numCharsInRow    number of characters in row of ascii image
     * @param image            the image to convert
     * @param charSet          characters to be used
     * @param brightnessLevels brightness levels of the characters
     * @return 2 dimensional array of characters representing the ascii image
     */
    private char[][] convertImageToAscii(Image image, int numCharsInRow, Character[] charSet,
                                         double[] brightnessLevels) {
        int subImageSize = image.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[image.getHeight() / subImageSize][image.getWidth() / subImageSize];
        int i = 0;
        int j = 0;
        // converts each subImage to ascii by brightness
        for (Image subImage : image.squareSubImagesOfSize(subImageSize)) {
            double subImageBrightness = getImageBrightness(subImage);
            asciiArt[i][j] = getMatch(subImageBrightness, charSet, brightnessLevels);
            j++;
            if (j >= asciiArt[i].length) {
                j = 0;
                i++;
            }
        }
        return asciiArt;
    }

    /**
     * finds best char match to brightness level of image
     *
     * @param imageBrightness  brightness of image
     * @param charSet          array of characters
     * @param brightnessLevels array of brightness levels matching the characters
     * @return the character with best match
     */
    private char getMatch(double imageBrightness, Character[] charSet, double[] brightnessLevels) {
        int matchIndex = 0;
        double minDifference = 255;
        for (int i = 0; i < brightnessLevels.length; i++) {
            double curDifference = Math.abs(brightnessLevels[i] - imageBrightness);
            if (curDifference < minDifference) {
                minDifference = curDifference;
                matchIndex = i;
            }
        }
        return charSet[matchIndex];
    }

}
