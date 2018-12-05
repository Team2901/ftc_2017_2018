package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtilities {
    public final static String PICTURES_FOLDER_NAME = "Pictures";
    public final static String TEAM_FOLDER_NAME = "Team";
    public final static String WINNER_FILE_NAME_3 = "writeWinnerFile3Jewels.txt";
    public final static String WINNER_FILE_NAME_2 = "writeWinnerFile2Jewels.txt";

    public static void writeConfigFile(String filename,
                                       List<? extends Object> config) throws IOException {

        final File teamDir = new File(Environment.getExternalStorageDirectory(), TEAM_FOLDER_NAME);
        boolean newDir = teamDir.mkdirs();
        final File file = new File(teamDir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Object o : config) {
                writer.write(o.toString());
                writer.newLine();
            }
        }
    }

    public static List<String> readConfigFile(String filename) throws IOException {

        final File teamDir = new File(Environment.getExternalStorageDirectory(), TEAM_FOLDER_NAME);
        boolean newDir = teamDir.mkdirs();
        final File file = new File(teamDir, filename);

        final List<String> config = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                config.add(line);
                line = reader.readLine();
            }
        }

        return config;
    }

    public static List<Integer> readIntegerConfigFile(String filename) throws IOException {

        final List<String> stringConfig = readConfigFile(filename);

        final List<Integer> config = new ArrayList<>();
        for (String string : stringConfig) {
            config.add(Integer.valueOf(string));
        }

        return config;
    }

    public static void writeBitmapFile(String filename, Bitmap bitmap) throws IOException {
        final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PICTURES_FOLDER_NAME;

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + filename)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
        }
    }

    public static Bitmap readBitmapFile(String filename) {

        final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PICTURES_FOLDER_NAME;
        final File image = new File(filePath, filename);

        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap;

        try {
            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        } catch (Exception e) {
            bitmap = null;
        }

        return bitmap;
    }

    public static void writeWinnerFile(String winnerLocation,
                                       int leftHueTotal,
                                       int middleHueTotal,
                                       int rightHueTotal) throws IOException {

        writeWinnerFile(WINNER_FILE_NAME_3, winnerLocation, leftHueTotal, middleHueTotal, rightHueTotal);
    }

    public static void writeWinnerFile(String fileName,
                                       String winnerLocation,
                                       int leftHueTotal,
                                       int middleHueTotal,
                                       int rightHueTotal) throws IOException {
        final File teamDir = new File(Environment.getExternalStorageDirectory(), TEAM_FOLDER_NAME);
        boolean newDir = teamDir.mkdirs();
        final File file = new File(teamDir, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Left Jewel Yellow count = " + leftHueTotal);
            writer.newLine();
            writer.write("Middle Jewel Yellow count = " + middleHueTotal);
            writer.newLine();
            writer.write("Right Jewel Yellow count = " + rightHueTotal);
            writer.newLine();
            writer.write("winner is " + winnerLocation);
        }
    }

    public static void writeWinnerFile(
            String winnerLocation,
            int middleHueTotal,
            int rightHueTotal) throws IOException  {
        writeWinnerFile(WINNER_FILE_NAME_2,winnerLocation, middleHueTotal, rightHueTotal);
    }

    public static void writeWinnerFile(String fileName,
                                       String winnerLocation,
                                       int middleHueTotal,
                                       int rightHueTotal) throws IOException {
        final File teamDir = new File(Environment.getExternalStorageDirectory(), TEAM_FOLDER_NAME);
        boolean newDir = teamDir.mkdirs();
        final File file = new File(teamDir, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Middle Jewel Yellow count = " + middleHueTotal);
            writer.newLine();
            writer.write("Right Jewel Yellow count = " + rightHueTotal);
            writer.newLine();
            writer.write("winner is " + winnerLocation);
        }
    }

    public static void writeHueFile(String filename,
                                    Bitmap bitmap) throws IOException {
        int[] colorCounts = ColorUtilities.getColorCounts(bitmap);

        final File teamDir = new File(Environment.getExternalStorageDirectory(), TEAM_FOLDER_NAME);
        boolean newDir = teamDir.mkdirs();
        final File file = new File(teamDir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < colorCounts.length; i++) {
                int colorCount = colorCounts[i];
                writer.write(String.valueOf(colorCount));
                writer.newLine();
            }
        }
    }
}
