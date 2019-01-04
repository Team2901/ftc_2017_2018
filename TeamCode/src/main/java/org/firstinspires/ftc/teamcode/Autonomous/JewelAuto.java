package org.firstinspires.ftc.teamcode.Autonomous;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.JewelFinder;
import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeJewelCamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gallagherb20503 on 10/21/2017.
 *
 *This is bridgett's code to take a picture and save it to the phone
 */

@Disabled
public class JewelAuto extends LinearOpModeJewelCamera {
    String filePath = "Pictures";
    String imageName = "TestImage.JPEG"; //TODO come back//
    String imageName2 = "TestImage2.JPEG"; //TODO come back//

    @Override
    public void runOpMode() throws InterruptedException {
        if (isCameraAvailable()) {
            startCamera();
            telemetry.addData(String.valueOf(width), height);
            telemetry.update();
            waitForStart();

            stopCamera();
            File sd = Environment.getExternalStorageDirectory();
            File image;
            try {
                image = new File(sd + "/" + filePath, imageName);
                saveYuvImage(image);

                image = new File(sd + "/" + filePath, imageName2);
                saveBitmapImage(image);
            } catch (Exception e) {
                telemetry.addData("NEED TO FIX", e.getMessage());
            }

            String filePath = "Pictures";
            String imageName = "TestImage.JPEG";
            ElapsedTime runtime = new ElapsedTime();

            waitForStart();
            telemetry.addLine("Hi");

            if (sd == null) {
                telemetry.addLine("Open External Storage Failed");
            }
            image = new File(sd + "/" + filePath, imageName);
            if (image == null) {
                telemetry.addLine("Open Image File Failed");
            } else {
                telemetry.addLine("Open Image Successful");

            }
            telemetry.addData("Image Name", "%s", image.getAbsolutePath());


            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

            if (bitmap == null) {
                telemetry.addLine("Could not read bitmap");

            }
            int LeftRed = 0;
            int RightRed = 0;
            int LeftBlue = 0;
            int RightBlue = 0;
            int count = 0;

            double xPercent = (bitmap.getWidth()) / 100.0;
            double yPercent = (bitmap.getHeight()) / 100.0;

            telemetry.addData("Start For loop", "");
            for (int x = 20; x < 30; x++) { // replace 200 with x pixel size value
                for (int y = 60; y < 80; y++) {
                    int color = bitmap.getPixel((int) (x * xPercent), (int) (y * yPercent));
                    //telemetry.addData("Color", "%d", color);
                    count++;
                    int red = Color.red(color);
                    int green = Color.green(color);
                    int blue = Color.blue(color);

                    double[] HBV = RGBtoHSV(red, green, blue);
                    double hue = HBV[0];

                    if (((300 < hue) || (hue < 60)))
                        LeftRed = LeftRed + 1;

                    if (((180 < hue) && (hue <= 300)))
                        LeftBlue = LeftBlue + 1;

                }

                Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas c = new Canvas(mutableBitmap);
                Paint p = new Paint();
                p.setColor(Color.BLACK);

                JewelFinder jewel = getJewel();
                c.drawRect((int) (jewel.getBoxLeftXPct() * xPercent),
                        (int) (jewel.getBoxTopYPct() * yPercent),
                        (int) (jewel.getBoxRightXPct() * xPercent),
                        (int) (jewel.getBoxBotYPct() * yPercent), p);

                telemetry.addData("Red count", "%d", LeftRed);
                telemetry.addData("Blue count", "%d", LeftBlue);
                telemetry.addData("Count", "%d", count);
                telemetry.update();

                saveBitmap("previewImage.png", mutableBitmap);


            }
            {    //runtime.reset();
                while (opModeIsActive()) {
                }
            }

        }
        else{
        telemetry.addData(" ", "Camera is not available");
    }
}


public static double[] RGBtoHSV(double r, double g, double b){

        double h, s, v;

        double min, max, delta;

        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);

        // V
        v = max;

        delta = max - min;

        // H
        if( r == max )
            h = ( g - b ) / delta; // between yellow & magenta - reds
        else if( g == max )
            h = 2 + ( b - r ) / delta; // between cyan & yellow - greens
        else
            h = 4 + ( r - g ) / delta; // between magenta & cyan - blues

        h *= 60;    // degrees

        if( h < 0 )
            h += 360;

        return new double[]{h,/*s,*/v};
    }

    public static void saveBitmap(String filename, Bitmap bitmap) {
        String filePath =
                Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath + "/" + filename);
            bitmap.compress( Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

}}