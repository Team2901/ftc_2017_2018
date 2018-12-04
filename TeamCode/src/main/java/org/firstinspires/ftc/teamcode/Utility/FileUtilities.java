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
    public static void writeConfigFile(String filename, List<? extends Object> config) throws IOException {
        final File teamDir = new File(Environment.getExternalStorageDirectory(), "Team");

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
        final File teamDir = new File(Environment.getExternalStorageDirectory(), "Team");

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

    /*
    This function takes the modified bitmap that we created and saves it to a file on the phone
    so we can confirm the code works correctly
    */

    public static void saveBitmap(String filename, Bitmap bitmap) throws IOException {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + filename)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

    public static Bitmap readBitmap(String filename) {

        final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";

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

    public static void writeWinnerFile(String winnerLocation, int leftHueTotal,
                                       int middleHueTotal, int rightHueTotal) throws IOException {
        final File teamDir = new File(Environment.getExternalStorageDirectory(), "Team");
        boolean newDir = teamDir.mkdirs();
        final File file = new File(teamDir, "writeWinnerFile.txt");

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

    public static void writeWinnerFile(String winnerLocation, int middleHueTotal,
                                       int rightHueTotal) throws IOException {
        final File teamDir = new File(Environment.getExternalStorageDirectory(), "Team");
        boolean newDir = teamDir.mkdirs();
        final File file = new File(teamDir, "writeWinnerFile.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Middle Jewel Yellow count = " + middleHueTotal);
            writer.newLine();
            writer.write("Right Jewel Yellow count = " + rightHueTotal);
            writer.newLine();
            writer.write("winner is " + winnerLocation);
        }

    }

    public static void saveHueFile(String jewelHues, Bitmap bitmap) throws IOException {
        int[] colorCounts = ColorUtilities.getColorCounts(bitmap);


        final File teamDir = new File(Environment.getExternalStorageDirectory(), "Team");

        boolean newDir = teamDir.mkdirs();

        final File file = new File(teamDir, jewelHues);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {


            for (int i = 0; i < colorCounts.length; i++) {
                int colorCount = colorCounts[i];
                writer.write(String.valueOf(colorCount));
                writer.newLine();
            }

        }


        //Bitmap readBitmap()

        // RGBtoHSV();


    }
}
