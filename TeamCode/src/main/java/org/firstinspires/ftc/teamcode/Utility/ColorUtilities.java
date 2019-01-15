package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

                float[] HSV = new float[3];
                Color.RGBToHSV(red, green, blue, HSV);

                double hue = HSV[0];

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

    public static int[] getColorCount(Bitmap bitmap, int minHue, int maxHue, LinearOpMode opMode) throws InterruptedException{
        int[]counts = {0,0};
        for (int x = 0; x < bitmap.getWidth(); x = x + HUE_SAMPLE_RATE) { // replace 200 with x pixel size value
            for (int y = 0; y < bitmap.getHeight(); y = y + HUE_SAMPLE_RATE) {
                if (opMode != null && !opMode.opModeIsActive()) {
                    throw new InterruptedException("");
                };
                int color = bitmap.getPixel(x, y);

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                float[] HSV = new float[3];
                Color.RGBToHSV(red, green, blue, HSV);

                int hue = (int) HSV[0];
                // if (HSV[1] <= 0.5 && HSV[2] >= 0.9){
                // counts[1]++;
                // }
                if (HSV[1]<0.7)//in gray scale protion of color scale.
                {
                    if (HSV[2]>0.8)//white
                    {
                        counts[1]++;
                    }
                }
                else if (minHue <= hue && hue <= maxHue) {
                    counts[0]++;
                }
            }
        }

        return counts;
    }
    public static int[] getColorCount(Bitmap bitmap, int minHue, int maxHue) throws InterruptedException{
     return getColorCount(bitmap, minHue, maxHue, null);
    };

    public static int[] getColorCounts(Bitmap bitmap, LinearOpMode opMode) throws InterruptedException{
        int[] colorCounts = new int[361];

        for (int x = 0; x < bitmap.getWidth(); x = x + HUE_SAMPLE_RATE) { // replace 200 with x pixel size value
            for (int y = 0; y < bitmap.getHeight(); y = y + HUE_SAMPLE_RATE) {
                if (opMode != null && !opMode.opModeIsActive()) {
                    throw new InterruptedException("");
                };

                int color = bitmap.getPixel(x, y);

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                float[] HSV = new float[3];
                Color.RGBToHSV(red, green, blue, HSV);

                int hue = (int) HSV[0];

                int hueCount = colorCounts[hue];
                hueCount++;
                if (HSV[1]<0.7)//in gray scale protion of color scale.
                {
                    if (HSV[2]>0.8)//white
                    {
                        colorCounts[360]++;
                    }
                }
                else {
                    colorCounts[hue] = hueCount;
                }
            }
        }

        return colorCounts;
    }
    public static int[] getColorCounts(Bitmap bitmap) throws InterruptedException{
        return getColorCounts(bitmap, null);
    }

    public static Bitmap blackWhiteColorDecider (Bitmap bitmap, int minHue, int maxHue, LinearOpMode opMode) throws InterruptedException
    {
        Bitmap babyBitmapBW = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),bitmap.getConfig());
        int newColor =0;
        for (int x = 0; x < bitmap.getWidth(); x++) { // replace 200 with x pixel size value
            for (int y = 0; y < bitmap.getHeight(); y++) {
                if (opMode != null && !opMode.opModeIsActive()) {
                    throw new InterruptedException("");
                };
                int color = bitmap.getPixel(x, y);

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                float[] HSV = new float[3];
                Color.RGBToHSV(red, green, blue, HSV);

                int hue = (int) HSV[0];
               /* HSV[1] =(float) 1.0;
                HSV[2]=(float) 1.0;
                newColor = Color.HSVToColor(HSV); */


                // if (HSV[1] <= 0.5 && HSV[2] >= 0.9){
                // counts[1]++;
                // }
                if (HSV[1]<0.7)//in gray scale protion of color scale.
                {
                   if (HSV[2]>0.8)//white
                   {
                     newColor = Color.WHITE;
                   }
                   else if (HSV[2]<0.2 ) //black
                   {
                       newColor= Color.BLACK;
                   }
                   else{
                       newColor = Color.GRAY;

                   }
                    babyBitmapBW.setPixel(x, y, newColor);
                }
                else if (minHue <= hue && hue <= maxHue) {
                    HSV[1] =(float) 1.0;
                    HSV[2]=(float) 1.0;
                    newColor = Color.HSVToColor(HSV);
                    babyBitmapBW.setPixel(x, y, Color.CYAN);
                }
                else{
                    HSV[1] =(float) 1.0;
                    HSV[2]=(float) 1.0;
                    newColor = Color.HSVToColor(HSV);
                    babyBitmapBW.setPixel(x, y, newColor);
                }
            }
        }

        return babyBitmapBW;
    }
    public static Bitmap blackWhiteColorDecider (Bitmap bitmap, int minHue, int maxHue) throws InterruptedException{
        return blackWhiteColorDecider(bitmap, minHue, maxHue, null);
    }

}
