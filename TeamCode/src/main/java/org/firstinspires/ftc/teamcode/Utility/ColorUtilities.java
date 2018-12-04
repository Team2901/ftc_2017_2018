package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcontroller.internal.JewelFinder;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class ColorUtilities {
    //Converts RGB channels from the bitmap to HSV counts to get a the of the hue of the pixels
    public static double[] RGBtoHSV(double r, double g, double b) {

        double h, s, v;

        double min, max, delta;

        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);

        // V
        v = max;

        delta = max - min;


        /*
        // S
        if( max != 0 )
            s = delta / max;
        else {
            s = 0;
            h = -1;
            return new double[]{h,s,v};
        }*/

        // H
        if (r == max)
            h = (g - b) / delta; // between yellow & magenta - reds
        else if (g == max)
            h = 2 + (b - r) / delta; // between cyan & yellow - greens
        else
            h = 4 + (r - g) / delta; // between magenta & cyan - blues

        h *= 60;    // degrees

        if (h < 0)
            h += 360;

        return new double[]{h,/*s,*/v};
    }

    public static int getColorCount(Bitmap bitmap,
                                    int minHue, int maxHue,
                                    int sampleLeftXPct, int sampleTopYPct,
                                    int sampleRightXPct, int sampleBotYPct) {
        int colorCount = 0;

        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;

        for (int x = sampleLeftXPct; x < sampleRightXPct; x++) { // replace 200 with x pixel size value
            for (int y = sampleTopYPct; y < sampleBotYPct; y++) {
                int color = bitmap.getPixel((int) (x * xPercent), (int) (y * yPercent));

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                double[] HBV = RGBtoHSV(red, green, blue);
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

    public static int[] getColorCounts (Bitmap bitmap){
        int[] colorCounts = new int [360];

        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;

        for (int x = 0; x < bitmap.getWidth(); x = x+1) { // replace 200 with x pixel size value
            for (int y = 0; y < bitmap.getHeight(); y = y+1) {
                int color = bitmap.getPixel( x, y );

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                double[] HBV = RGBtoHSV(red, green, blue);
                int hue = (int) HBV[0];

                int hueCount = colorCounts[hue];
                hueCount++;

                colorCounts[hue] = hueCount;
            }
        }

        return colorCounts;
    }



    public static int determineColor (Bitmap bitmap, int minHue, int maxHue)
    {

        int total = 0;


        for (int x = 0; x < bitmap.getWidth(); x = x+5) { // replace 200 with x pixel size value
            for (int y = 0; y < bitmap.getHeight(); y = y+5) {
                int color = bitmap.getPixel( x, y );

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                double[] HBV = RGBtoHSV(red, green, blue);
                int hue = (int) HBV[0];
                if (minHue<= hue && hue <= maxHue)
                {
                    total ++;
                }
            }
        }

        return total;

    }

}
