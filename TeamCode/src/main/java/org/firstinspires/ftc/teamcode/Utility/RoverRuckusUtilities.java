package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.IOException;
import java.util.List;

public class RoverRuckusUtilities {

    public static final String JEWEL_CONFIG_FILE_FORMAT = "jewelConfig%s.txt";
    public static final String JEWEL_BITMAP_FILE_FORMAT = "jewelBitmap%s.png";
    public static final String JEWEL_HUE_FILE_FORMAT = "jewelHues%s.txt";

    public static int[] getJewelHueCount(Bitmap bitmap,
                                         String name,
                                         LinearOpMode opMode) throws RuntimeException, InterruptedException {
        return getJewelHueCount(bitmap, name, opMode, true);
    }

    public static int[] getJewelHueCount(Bitmap bitmap,
                                         String name,
                                         LinearOpMode opMode,
                                         boolean writeFiles) throws RuntimeException, InterruptedException {
        String configFilename = String.format(JEWEL_CONFIG_FILE_FORMAT, name);
        String bitmapFilename = String.format(JEWEL_BITMAP_FILE_FORMAT, name);
        String hueFilename = String.format(JEWEL_HUE_FILE_FORMAT, name);
        return getJewelHueCount(bitmap, configFilename, bitmapFilename, hueFilename, opMode, writeFiles);
    }

    public static int[] getJewelHueCount(Bitmap bitmap,
                                         String configFilename,
                                         String bitmapFilename,
                                         String hueFilename, LinearOpMode opMode) throws RuntimeException, InterruptedException {
        return getJewelHueCount(bitmap, configFilename, bitmapFilename, hueFilename, opMode, true);
    }

    public static int[] getJewelHueCount(Bitmap bitmap,
                                         String configFilename,
                                         String bitmapFilename,
                                         String hueFilename, LinearOpMode opMode,
                                         boolean writeFiles) throws RuntimeException, InterruptedException {
        try {
            List<Integer> config = FileUtilities.readIntegerConfigFile(configFilename);
            int sampleLeftXPct = config.get(0);
            int sampleTopYPct = config.get(1);
            int sampleRightXPct = config.get(2);
            int sampleBotYPct = config.get(3);

            Bitmap babyBitmap = BitmapUtilities.getBabyBitmap(bitmap, sampleLeftXPct, sampleTopYPct, sampleRightXPct, sampleBotYPct);

            if (writeFiles) {
                FileUtilities.writeBitmapFile(bitmapFilename, babyBitmap);
                FileUtilities.writeHueFile(hueFilename, babyBitmap);
                Bitmap babyBitmapBW = ColorUtilities.blackWhiteColorDecider(babyBitmap, 25, 40, opMode);
                String[] fileNameSplit = bitmapFilename.split(".png");
                String bwFileName = fileNameSplit[0] + "BW.png";
                FileUtilities.writeBitmapFile(bwFileName, babyBitmapBW);
            }

            int[] hueTotal = ColorUtilities.getColorCount(babyBitmap, 25, 40, opMode);
            return hueTotal;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
