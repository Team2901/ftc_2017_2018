package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelicRecoveryUtilities {

    public static void writeConfigFile(String filename, List<? extends Object> config) throws IOException {
        final File teamDir = new File(Environment.getExternalStorageDirectory(), "team");

        boolean newDir = teamDir.mkdirs();

        final File file = new File(teamDir, filename);

        try (BufferedWriter writer = new BufferedWriter (new FileWriter(file))){
            for (Object o : config) {
                writer.write(o.toString());
                writer.newLine();
            }
        }
    }

    public static List<String> readConfigFile (String filename) throws IOException {
        final File teamDir = new File(Environment.getExternalStorageDirectory(), "team");

        boolean newDir = teamDir.mkdirs();

        final File file = new File(teamDir, filename);

        final List<String> config = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            while (reader.ready()) {
                String line = reader.readLine();
                config.add(line);
            }
        }
        return config;
    }

    /*
    This function takes the modified bitmap that we created and saves it to a file on the phone
    so we can confirm the code works correctly
    */
    public static void saveBitmap(String filename, Bitmap bitmap) throws IOException{
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath+"/"+filename)) {
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

    /*
This program draws a box on top of the picture that we analyze then saves the picture to a file
so that we can confirm where the box is located
*/
    public static void drawSamplingBox(Bitmap bitmap, String filename,
                                int sampleLeftXPct, int sampleTopYPct,
                                int sampleRightXPct, int sampleBotYPct) throws IOException{
        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutableBitmap);
        Paint p = new Paint();
        p.setARGB(100, 0, 200, 0);
        c.drawRect((int) (sampleLeftXPct * xPercent),
                (int) (sampleTopYPct * yPercent),
                (int) (sampleRightXPct * xPercent),
                (int) (sampleBotYPct * yPercent), p);
        saveBitmap(filename, mutableBitmap);
    }

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

    /*
A problem we ran into using the IMU sensor was the fact that the angles were not as though they were on the unit circle
This function normalizes the angle so the robot starts at zero when the IMU is initialized and right is 360 and left is 0.
This allows only one problem spot to remain
*/
    public static int normalizeAngle(int angle) {
        return (angle + 360) % 360;
    }

    /*
     * This returns a normalized current angle
     */
    public static int getCurrentAngle(BNO055IMU imu) {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return normalizeAngle((int) angles.firstAngle);
    }

    //In order to allow the robot to move across the line that divides 360 &
    public static int getRelativePosition(int current, int goal, String direction, int start) {
        if (direction == "right" && goal > start && current > goal) {
            return Math.abs(360 - (current - start));
        } else if (direction == "right") {
            return Math.abs(current - start);
        } else if (direction == "left" && start > goal && current < goal) {
            return Math.abs(360 - (start - current));
        } else {
            return Math.abs(start - current);
        }
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

                double[] HBV = RelicRecoveryUtilities.RGBtoHSV(red, green, blue);
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

    public static Bitmap getVuforiaImage(VuforiaLocalizer vuforia) {
        try{
            VuforiaLocalizer.CloseableFrame closeableFrame = vuforia.getFrameQueue().take ();
            for (int i=0; i < closeableFrame.getNumImages(); i++){
                Image image= closeableFrame.getImage (i);
                if (image.getFormat()== PIXEL_FORMAT.RGB565){
                    Bitmap bitmap= Bitmap.createBitmap(image.getWidth (),
                            image.getHeight(),
                            Bitmap.Config.RGB_565);
                    bitmap.copyPixelsFromBuffer (image.getPixels());
                    Matrix matrix = new Matrix ();
                    matrix.postRotate(90);

                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (Exception e){
            return null;
        }

        return null;
    }

    // returns ROTATED image, to match preview window
    public static Bitmap convertYuvImageToRgb(YuvImage yuvImage, int width, int height, int downSample) {
        Bitmap rgbImage;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, out);
        byte[] imageBytes = out.toByteArray();

        BitmapFactory.Options opt;
        opt = new BitmapFactory.Options();
        opt.inSampleSize = downSample;

        // get image and rotate it so (0,0) is in the bottom left
        Bitmap tmpImage;
        Matrix matrix = new Matrix();
        matrix.postRotate(90); // to rotate the camera images so (0,0) is in the bottom left
        tmpImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, opt);
        rgbImage=Bitmap.createBitmap(tmpImage , 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), matrix, true);

        return rgbImage;
    }
}
