package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ColorUtilities {
    public static final int HUE_SAMPLE_RATE = 1;

    public static int getColorCount(Bitmap bitmap,
                                    int minHue, int maxHue,
                                    int sampleLeftXPct, int sampleTopYPct,
                                    int sampleRightXPct, int sampleBotYPct) {
        int colorCount = 0;

        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;

        for (int x = sampleLeftXPct; x < sampleRightXPct; x++) {
            for (int y = sampleTopYPct; y < sampleBotYPct; y++) {
                int color = bitmap.getPixel((int) (x * xPercent), (int) (y * yPercent));

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                float[] HBV = RGBtoHSV(red, green, blue);
                double hue = HBV[0];

                if (minHue < maxHue) {
                    if (minHue < hue && hue < maxHue) {
                        colorCount++;
                    }
                } else {
                    if (minHue < hue || hue < maxHue) {
                        colorCount++;
                    }
                }
            }
        }

        return colorCount;
    }

    public static int getColorCount(Bitmap bitmap, int minHue, int maxHue) {
        int total = 0;

        for (int x = 0; x < bitmap.getWidth(); x = x + HUE_SAMPLE_RATE) { // replace 200 with x pixel size value
            for (int y = 0; y < bitmap.getHeight(); y = y + HUE_SAMPLE_RATE) {
                int color = bitmap.getPixel(x, y);

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                float[] HBV = RGBtoHSV(red, green, blue);
                int hue = (int) HBV[0];
                if (minHue <= hue && hue <= maxHue) {
                    total++;
                }
            }
        }

        return total;
    }

    public static int getColorCount(int[] colorCounts, int minHue, int maxHue) {
        // TODO
        return 0;
    }

    public static int[] getColorCounts(Bitmap bitmap) {
        int[] colorCounts = new int[360];

        for (int x = 0; x < bitmap.getWidth(); x = x + HUE_SAMPLE_RATE) { // replace 200 with x pixel size value
            for (int y = 0; y < bitmap.getHeight(); y = y + HUE_SAMPLE_RATE) {
                int color = bitmap.getPixel(x, y);

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                float[] HBV = RGBtoHSV(red, green, blue);
                int hue = (int) HBV[0];

                int hueCount = colorCounts[hue];
                hueCount++;

                colorCounts[hue] = hueCount;
            }
        }

        return colorCounts;
    }

    public static float[] RGBtoHSV(int r, int g, int b) {
        //Converts RGB channels from the bitmap to HSV counts to get a the of the hue of the pixels

        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        return hsv;
    }
}
