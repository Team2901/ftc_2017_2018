package org.firstinspires.ftc.teamcode.Autonomous;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeCamera;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Robot.Team2901RobotHardware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gallagherb20503 on 11/7/2017.
 */


public class BaseCode extends LinearOpModeCamera {
    VuforiaLocalizer vuforia;
    RelicRecoveryVuMark vuMark = null;
    int sampleBox_x1= 0;
    int sampleBox_x2= 40;
    int sampleBox_y1= 40;
    int sampleBox_y2= 100;
    String teamColor;
    Bitmap bitmap;
    File sd = Environment.getExternalStorageDirectory();
    File sampleBox = new File(sd + "/team", "sampleBox.txt");



    Team2901RobotHardware robot = new Team2901RobotHardware();
    HardwareMap hwMap = null;

    public void runOpMode() {

        robot.initAutonomous(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AW/DxXD/////AAAAGYJtW/yP3kG0pVGawWtQZngsJNFQ8kp1Md8CaP2NP72Q0on4mGKPLt/lsSnMnUkCFNymrXXOjs0eHMDTvijWRIixEe/sJ4KHEVf1fhf0kqUB29+dZEvh4qeI7tlTU6pIy/MLW0a/t9cpqMksBRFqXIrhtR/vw7ZnErMTZrJNNXqmbecBnRhDfLncklzgH2wAkGmQDn0JSP7scEczgrggcmerXy3v6flLDh1/Tt2QZ8l/bTcEJtthE82i8/8p0NuDDhUyatFK1sZSSebykRz5A4PDUkw+jMTV28iUytrr1QLiQBwaTX7ikl71a1XkBHacnxrqyY07x9QfabtJf/PYNFiU17m/l9DB6Io7DPnnIaFP";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        vuforia.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565,true);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary


        telemetry.addData("start","");
        telemetry.update();

        waitForStart();
        relicTrackables.activate();

        /*robot.closeClaw();
        robot.raiseLift(100);*/


       /*while(opModeIsActive()) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            bitmap = getImage();
            saveBitmap(bitmap);
            telemetry.addData("VuMark", "%s visible", vuMark);
            //    break;
        } else telemetry.addLine("vuMark not visable");
        telemetry.update();
    } */

            // lower arm/ jewel knocker
            robot.lowerJewelKnocker();

    bitmap = getImage();
        saveBitmap(bitmap);

        int position= 0;

    if (isOurJewelOnLeft(bitmap)){
        // if your jewel is on the left turn right
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        position= robot.leftMotor.getCurrentPosition() + 1140;
        telemetry.addData("current Position", "%d", position);
        robot.leftMotor.setTargetPosition(position);
        robot.leftMotor.setPower(.75);
    }
    else {
        // if your jewel is on the right turn left.
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        position= robot.rightMotor.getCurrentPosition() + 1140;
        telemetry.addData("current Position", "%d", position);
        robot.rightMotor.setTargetPosition(position);
        robot.rightMotor.setPower(.75);
        }

        while (opModeIsActive()) {
            //steps to knock the jewel off
            //step 1: take picture
            //step 2:analis picture
            //step 3:drive backwords
            //step 4: turn right or left depending on the jewel needed


            //steps for block placement
            //turn 90 left
            //drive forward to left, right, or center position
            // turn right 90
            //drive forward
            //drop block
            telemetry.addData("current Position", "%d", position);

        }
        robot.armServo.setPosition(1);
    }
    Bitmap getImage (){
        try{
            VuforiaLocalizer.CloseableFrame closeableFrame = vuforia.getFrameQueue().take ();
            for (int i=0; i < closeableFrame.getNumImages(); i++){
                Image image= closeableFrame.getImage (i);
                if (image.getFormat()== PIXEL_FORMAT.RGB565){
                    Bitmap bitmap= Bitmap.createBitmap(image.getWidth (),
                            image.getHeight(),
                            Bitmap.Config.RGB_565);
                    bitmap.copyPixelsFromBuffer (image.getPixels());
                    return bitmap;
                }
            }
        } catch (Exception e){
            return null;
        }

        return null;
    }
    public void saveBitmap(Bitmap bitmap)  {
        String filename =   Environment.getExternalStorageDirectory(). getAbsolutePath()
                +    "/Pictures?vuforia.png";
        FileOutputStream out = null;
        try{
            out= new FileOutputStream(filename);
            bitmap.compress (Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null){
                    out.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean isOurJewelOnLeft(Bitmap bitmap) {
        int leftRed = 0;
        int leftBlue = 0;
        int count = 0;

        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;

        telemetry.addData("Start For loop", "");
        for (int x = sampleBox_x1; x < sampleBox_x2; x++) { // replace 200 with x pixel size value
            for (int y = sampleBox_y1; y < sampleBox_y2; y++) {
                int color = bitmap.getPixel((int) (x * xPercent), (int) (y * yPercent));
                //telemetry.addData("Color", "%d", color);
                count++;
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                double[] HBV = RGBtoHSV(red, green, blue);
                double hue = HBV[0];

                if (((300 < hue) || (hue < 20)))
                    leftRed = leftRed + 1;

                if (((180 < hue) && (hue <= 300)))
                    leftBlue = leftBlue + 1;
            }
        }


        String leftJewelColor = "unknown";
        if (leftRed > leftBlue)
            leftJewelColor = "red";
        else
            leftJewelColor = "blue";

        telemetry.addData("Red count", "%d", leftRed);
        telemetry.addData("Blue count", "%d", leftBlue);
        telemetry.addData("Count", "%d", count);
        telemetry.addData("JewelColor", leftJewelColor);


        telemetry.update();


        if (leftJewelColor.equals(teamColor))
            return true;
        else
            return false;
    } public static double[] RGBtoHSV(double r, double g, double b) {

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
    public void readConfigFile (){
        try (BufferedReader reader = new BufferedReader(new FileReader(sampleBox))){
            sampleBox_x1 = Integer.parseInt(reader.readLine());
            sampleBox_y1 = Integer.parseInt(reader.readLine());
            sampleBox_x2 = Integer.parseInt(reader.readLine());
            sampleBox_y2 = Integer.parseInt(reader.readLine());
        } catch (Exception e){
            telemetry.addData ("ERROR READING THE FILE", e.getMessage());
        }
    }


}




