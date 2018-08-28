package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeCamera;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.HardwareDxm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class RelicRecoveryUtilities extends LinearOpModeCamera {


    public String teamColor;
    public static final int PINCH_LOOP = 12500;
    public static long TIME_PAST = 5000;

    HardwareDxm robot = new HardwareDxm();
    HardwareMap hwMap = null;
    BNO055IMU imu;
    public int startingPosition = 1;

    Orientation angles;
    Acceleration gravity;

    File sd = Environment.getExternalStorageDirectory();
    File sampleBox = new File(sd + "/team", "sampleBox.txt");

    VuforiaLocalizer vuforia;


    String filePath = "Pictures";
    String imageName = "TestImage.JPEG";
    private ElapsedTime runtime = new ElapsedTime();
    private int sampleBox_x1;
    private int sampleBox_y1;
    private int sampleBox_x2;
    private int sampleBox_y2;

    //Full Init process that leads up to the taking of the picture, put at the start of every autonomous
    public void myinit() {

        robot.init(hardwareMap);


        //Imu setup
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();


        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);


        if (!isCameraAvailable()) {
            return;
        }
        startCamera();

        while (yuvImage == null) ;

        readConfigFile();

        this.jewel.moveBox(sampleBox_x1, sampleBox_y1);
        this.jewel.sampleLeftXPct = sampleBox_x1;
        this.jewel.sampleTopYPct = sampleBox_y1;
        this.jewel.sampleRightXPct = sampleBox_x2;
        this.jewel.sampleBotYPct = sampleBox_y2;

        telemetry.addData("x1", "%d", sampleBox_x1);
        telemetry.addData("y1", "%d", sampleBox_y1);
        telemetry.addData("x2", "%d", sampleBox_x2);
        telemetry.addData("y2", "%d", sampleBox_y2);
        telemetry.addData(String.valueOf(width), height);
        telemetry.update();

        robot.open();

    }

    //Inits the vuMarks because the camera and the vuForia cannot operate at the same time, has a loop that breaks only when vuMark is seen
    public int readVuImage() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parametersv = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parametersv.vuforiaLicenseKey = "AW/DxXD/////AAAAGYJtW/yP3kG0pVGawWtQZngsJNFQ8kp1Md8CaP2NP72Q0on4mGKPLt/lsSnMnUkCFNymrXXOjs0eHMDTvijWRIixEe/sJ4KHEVf1fhf0kqUB29+dZEvh4qeI7tlTU6pIy/MLW0a/t9cpqMksBRFqXIrhtR/vw7ZnErMTZrJNNXqmbecBnRhDfLncklzgH2wAkGmQDn0JSP7scEczgrggcmerXy3v6flLDh1/Tt2QZ8l/bTcEJtthE82i8/8p0NuDDhUyatFK1sZSSebykRz5A4PDUkw+jMTV28iUytrr1QLiQBwaTX7ikl71a1XkBHacnxrqyY07x9QfabtJf/PYNFiU17m/l9DB6Io7DPnnIaFP";


        parametersv.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parametersv);


        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        // **********************************************************************************************

        relicTrackables.activate();


        int mark = 0;
        /*
        Right - 1
        Center - 2
        Left - 3
*/
// This can be used to identify the pictograph and this loop will run until it is found and it'll store the mark

        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        Long startTime = d.getTime();
        do {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                if (vuMark == RelicRecoveryVuMark.RIGHT) {
                    mark = 1;
                    telemetry.addData("RIGHT", "");
                } else if (vuMark == RelicRecoveryVuMark.CENTER) {
                    telemetry.addData("CENTER", "");
                    mark = 2;
                } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                    telemetry.addData("LEFT", "");
                    mark = 3;
                }

                telemetry.update();
            }
        }
        while (mark == 0 || Calendar.getInstance().getTime().getTime() - startTime > 5000);
        relicTrackables.deactivate();

        return mark;
    }

    //Converts RGB channels from the bitmap to HSV counts to get a the of the hue of the pixels
    @NonNull
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
    This function takes the modified bitmap that we created and saves it to a file on the phone
    so we can confirm the code works correctly
    */

    public static void saveBitmap(String filename, Bitmap bitmap) {

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(filePath + "/" + filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /*
    This program draws a box on top of the picture that we analyze then saves the picture to a file
    so that we can confirm where the box is located
    */
    public void drawSamplingBox(Bitmap bitmap) {
        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutableBitmap);
        Paint p = new Paint();
        p.setARGB(100, 0, 200, 0);
        c.drawRect((int) (jewel.sampleLeftXPct * xPercent),
                (int) (jewel.sampleTopYPct * yPercent),
                (int) (jewel.sampleRightXPct * xPercent),
                (int) (jewel.sampleBotYPct * yPercent), p);
        saveBitmap("previewImage.png", mutableBitmap);
    }

    /*
    This is the boolean value that allows us to know if Our Jewel is on left, we have been using
    this in the sampling ox we use
     */
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

                if (((300 < hue) || (hue < 60)))
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
        telemetry.update();

        if (leftJewelColor.equals(teamColor))
            return true;
        else
            return false;
    }

    /*
    In our Robot setUp we right to a file on the phone the coordinates for the sampling box. This function reads that file so we
    can allow the robot to take the correct picture during the autonomous
    */
    public void readConfigFile() {
        File sd = Environment.getExternalStorageDirectory();
        File sampleBox = new File(sd + "/team", "sampleBox.txt");

        String text = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(sampleBox));
            text = reader.readLine();
            teamColor = text;
            text = reader.readLine();
            startingPosition = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_x1 = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_y1 = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_x2 = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_y2 = Integer.parseInt(text);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            telemetry.addData("", "couldn't read");

            teamColor = "red";
            startingPosition = 1;
            sampleBox_x1 = 30;
            sampleBox_x2 = 50;
            sampleBox_y1 = 30;
            sampleBox_y2 = 50;
        }
    }

    /*
    A problem we ran into using the IMU sensor was the fact that the angles were not as though they were on the unit circle
    This function normalizes the angle so the robot starts at zero when the IMU is initialized and right is 360 and left is 0.
    This allows only one problem spot to remain
    */
    public int normalizeAngle(int angle) {

        return (angle + 360) % 360;

    }

    //    This returns a normalized current angle
    public int getCurrentAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return normalizeAngle((int) angles.firstAngle);
    }

    /*This function allows you to input a goal angle between 1-360 and a direction that allows you
    to turn in that direction and angle

    DELETE THIS ONCE BETTER TURN CODE HAS BEEN CREATED
    */
    public void turn(int goal, String direction) {
        robot.bLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.bRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.fLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.fRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        int start = getCurrentAngle();
        getRelativePosition(goal, direction, start);

        int current = getRelativePosition(goal, direction, start);
/*        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        Long startTime = d.getTime();
    */
        if (direction == "left") {

            // start goal relative
            while (getRelativePosition(goal, direction, start) < goal - 4) {
                Log.v("FTC_LEFt_TURN", String.format("start=%d goal=%d Relative=%d", start, goal, getRelativePosition(goal, direction, start)));
                if (current < (.27 * goal)) {

                    robot.fLeft.setPower(-.5);
                    robot.bLeft.setPower(-.5);
                    robot.fRight.setPower(.5);
                    robot.bRight.setPower(.5);
                    telemetry.addData("state 1", "");

                } else if (current < (goal * .56)) {
                    robot.fLeft.setPower(-.35);
                    robot.bLeft.setPower(-.35);
                    robot.fRight.setPower(.35);
                    robot.bRight.setPower(.35);
                    telemetry.addData("state 2", "");

                } else if (current < goal * .78) {
                    robot.fLeft.setPower(-.2);
                    robot.bLeft.setPower(-.2);
                    robot.fRight.setPower(.2);
                    robot.bRight.setPower(.2);
                    telemetry.addData("state 3", "");
                } else if (current < goal - 10) {
                    robot.fLeft.setPower(-.2);
                    robot.bLeft.setPower(-.2);
                    robot.fRight.setPower(.2);
                    robot.bRight.setPower(.2);
                    telemetry.addData("state 4", "");
                } else {
                    telemetry.addData("state 5", "");
                }

                current = getRelativePosition(goal, direction, start);
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                telemetry.addData("goal", goal);
                telemetry.addData("current", current);
                telemetry.addData("get current", getCurrentAngle());
                telemetry.addData("imu", angles.firstAngle);
                telemetry.addData("Relative position", getRelativePosition(goal, direction, start));
                telemetry.update();
            }

            robot.fLeft.setPower(0);
            robot.bLeft.setPower(0);
            robot.fRight.setPower(0);
            robot.bRight.setPower(0);

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("goal", goal);
            telemetry.addData("current", current);
            telemetry.addData("get current", getCurrentAngle());
            telemetry.addData("imu", angles.firstAngle);
            telemetry.addData("Relative position", getRelativePosition(goal, direction, start));
            telemetry.update();
        } else if (direction == "right") {
            while (current < goal - 4) {
               /*c = Calendar.getInstance();
                d = c.getTime();
                Long now = d.getTime();
                if (now - startTime > TIME_PAST) {
                    break;
                }
*/
                if (current < (.27 * goal)) {
                    robot.fLeft.setPower(.5);
                    robot.bLeft.setPower(.5);
                    robot.fRight.setPower(-.5);
                    robot.bRight.setPower(-.5);
                    telemetry.addData("state 1", "");
                } else if (current < (goal * .56)) {
                    robot.fLeft.setPower(.35);
                    robot.bLeft.setPower(.35);
                    robot.fRight.setPower(-.35);
                    robot.bRight.setPower(-.35);
                    telemetry.addData("state 2", "");
                } else if (current < goal * .78) {
                    robot.fLeft.setPower(.2);
                    robot.bLeft.setPower(.2);
                    robot.fRight.setPower(-.2);
                    robot.bRight.setPower(-.2);
                    telemetry.addData("state 3", "");
                } else if (current < goal - 10) {
                    robot.fLeft.setPower(.2);
                    robot.bLeft.setPower(.2);
                    robot.fRight.setPower(-.2);
                    robot.bRight.setPower(-.2);
                    telemetry.addData("state 4", "");
                } else {
                    telemetry.addData("state 5", "");
                }
                current = getRelativePosition(goal, direction, start);
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                telemetry.addData("goal", goal);
                telemetry.addData("current", current);
                telemetry.addData("get current", getCurrentAngle());
                telemetry.addData("imu", angles.firstAngle);
                telemetry.addData("Relative position", getRelativePosition(goal, direction, start));
                telemetry.update();

            }


            robot.fLeft.setPower(0);
            robot.bLeft.setPower(0);
            robot.fRight.setPower(0);
            robot.bRight.setPower(0);

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("goal", goal);
            telemetry.addData("current", current);
            telemetry.addData("get current", getCurrentAngle());
            telemetry.addData("imu", angles.firstAngle);
            telemetry.addData("Relative position", getRelativePosition(goal, direction, start));
            telemetry.update();
        }
    }

    //In order to allow the robot to move across the line that divides 360 &
    public int getRelativePosition(int goal, String direction, int start) {
        int current = getCurrentAngle();
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

    //This function saves the image that we took from the camera as a bitmap
    public boolean isOurJewelOnLeft() {


        Bitmap rgbImage = convertYuvImageToRgb(yuvImage, width, height, 0);
        stopCamera();

        saveBitmap(imageName, rgbImage);

        if (sd == null) {
            telemetry.addLine("Open External Storage Failed");
        }

        File image = new File(sd + "/" + filePath, imageName);

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
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        drawSamplingBox(bitmap);
        return isOurJewelOnLeft(bitmap);

    }


    }
