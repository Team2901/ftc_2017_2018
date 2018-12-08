package org.firstinspires.ftc.teamcode.Autonomous;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Utility.BitmapUtilities;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.GoldPosition.*;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartPosition.BLUE_CRATER;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartPosition.BLUE_DEPOT;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartPosition.RED_DEPOT;


public class BaseRoverRuckusAuto extends LinearOpMode {

    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();
    VuforiaLocalizer vuforia;
    float angleStart;
    double xStart;
    double yStart;
    WebcamName webcam;
    VuforiaTrackables trackables;
    VuforiaTrackable blue;
    VuforiaTrackable red;
    VuforiaTrackable front;
    VuforiaTrackable back;
    public static final int TARGET_POSITION = -1120;
    public StartPosition initialPosition;
    String jewelConfigLeft = "jewelConfigLeft.txt";
    String jewelConfigMiddle = "jewelConfigMiddle.txt";
    String jewelConfigRight = "jewelConfigRight.txt";
    String jewelBitmap = "jewelBitmap.png";
    String jewelBitmapLeft = "jewelBitmapLeft.png";
    String jewelBitmapMiddle = "jewelBitmapMiddle.png";
    String jewelBitmapRight = "jewelBitmapRight.png";
    double x;
    double y;
    double z;
    float angleVu;

    public enum StartPosition {
        RED_CRATER, RED_DEPOT, BLUE_CRATER, BLUE_DEPOT;
    }

    public enum GoldPosition {

        LEFT, MIDDLE, RIGHT
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //step -2: initialize hardware
        robot.init(hardwareMap);
        // step -1: initialize vuforia
        /*
        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getBackCameraParameters(hardwareMap);
        vuforia = VuforiaUtilities.getVuforia(parameters);


        VuforiaTrackables roverRuckus = VuforiaUtilities.setUpTrackables(vuforia, parameters);
        blue = roverRuckus.get(0);
        red = roverRuckus.get(1);
        front = roverRuckus.get(2);
        back = roverRuckus.get(3);
        */
        //step 0: locate cheddar
        telemetry.addData("startPosition", initialPosition);
        telemetry.update();

        waitForStart();
        BaseRoverRuckusAuto.GoldPosition goldPosition = LEFT;//determineGoldPosition();
        //step 1: drop down from lander
        //dropFromLander();
        //step 2: do vuforia to determine position
        // roverRuckus.activate();
        OpenGLMatrix location = null; //  VuforiaUtilities.getLocation(blue, red, front, back);
        if (location == null) {
            location = VuforiaUtilities.getMatrix(0, 0, angleStart,
                    (float) (xStart / VuforiaUtilities.MM_TO_INCHES),
                    (float) (yStart / VuforiaUtilities.MM_TO_INCHES), 0);
        }
        VectorF translation = location.getTranslation();

        Orientation orientation = Orientation.getOrientation(location,
                AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        telemetry.addData("goldPosition", goldPosition);
        telemetry.update();

        x = (translation.get(0) * VuforiaUtilities.MM_TO_INCHES);
        y = (translation.get(1) * VuforiaUtilities.MM_TO_INCHES);
        z = ((translation.get(2) * VuforiaUtilities.MM_TO_INCHES));
        angleVu = orientation.thirdAngle;


        //step 3: go to the cheddar pivot point
        PolarCoord goal = getGoldenPostition(goldPosition, initialPosition);
        goToPosition(x, y, goal.x, goal.y, angleVu);

        telemetry.addData("goalx", goal.x);
        telemetry.addData("goaly", goal.y);
        telemetry.update();


        //step 4:turn to face depot point
        double angleImu = robot.getAngle();

        robot.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        while (Math.abs(goal.theta - angleImu) > 1) {
            angleImu = robot.getAngle();

            robot.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.left.setPower(-getPower(angleImu, goal.theta));
            robot.right.setPower(getPower(angleImu, goal.theta));

            telemetry.addData("goldPosition", goldPosition);
            telemetry.addData("Goal Angle", goal.theta);
            telemetry.addData("Robot Angle ", angleImu);
            telemetry.addData("offset Angle", robot.offset);
            telemetry.addData("angleGoal-angle ", goal.theta - angleImu);
            telemetry.addData("Power", getPower(angleImu, goal.theta));
            telemetry.update();
            idle();
        }


        robot.left.setPower(0);
        robot.right.setPower(0);
        //step 5: gooooo

        double distance = getDistance(initialPosition, goal);

        robot.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.right.setTargetPosition((int) (distance * VuforiaUtilities.INCHES_TO_ENCODERCOUNTS));
        robot.left.setTargetPosition((int) (distance * VuforiaUtilities.INCHES_TO_ENCODERCOUNTS));


        while (robot.left.isBusy()) {
            telemetry.addData("distance to goal", distance);
            telemetry.addData("encoders to goal", distance * VuforiaUtilities.INCHES_TO_ENCODERCOUNTS);
            telemetry.addData("encoders", robot.left.getCurrentPosition());
            telemetry.update();


            robot.left.setPower(1);
            robot.right.setPower(1);
        }
        //step 7: do corner action(if depot: drop team marker/if crater: park)
        doCornerAction();

        while (opModeIsActive()) {
            idle();
        }


    }


    public void dropFromLander() {
        DcMotor.RunMode originalValue = robot.lift.getMode();
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(TARGET_POSITION);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) {
            idle();
        }
        robot.lift.setMode(originalValue);
    }

    public GoldPosition determineGoldPosition() {

        Bitmap bitmap = BitmapUtilities.getVuforiaImage(vuforia);
        try {
            FileUtilities.writeBitmapFile(jewelBitmap, bitmap);

            FileUtilities.writeHueFile("jewelHuesBig.txt", bitmap);

            int leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft, jewelBitmapLeft, "jewelHuesLeft.txt");
            int middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle, jewelBitmapMiddle, "jewelHuesMiddle.txt");
            int rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight, jewelBitmapRight, "jewelHuesRight.txt");

            String winnerLocation = BitmapUtilities.findWinnerLocation(leftHueTotal, middleHueTotal, rightHueTotal);
            FileUtilities.writeWinnerFile(winnerLocation, leftHueTotal, middleHueTotal, rightHueTotal);
            if (leftHueTotal > middleHueTotal && middleHueTotal > rightHueTotal) {
                return LEFT;
            } else if (rightHueTotal > middleHueTotal && rightHueTotal > leftHueTotal) {
                return GoldPosition.RIGHT;
            } else {
                return GoldPosition.MIDDLE;
            }
        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
            telemetry.update();
            return GoldPosition.MIDDLE;
        }

    }

    public void doCornerAction() {
        if (initialPosition == BLUE_DEPOT || initialPosition == RED_DEPOT) {
            robot.marker.setPosition(1);
            robot.marker.setPosition(0);
        } else {

        }
    }

    //Using The IMU to turn 90^
    double getPower(double absCurrent, double absGoal, double absStart) {

        double relCurrent = AngleUnit.normalizeDegrees(absCurrent - absStart);
        double relGoal = AngleUnit.normalizeDegrees(absGoal - absStart);
        if (relCurrent < relGoal / 2) {

            return (.01 * relCurrent + (Math.signum(relCurrent) * .075));
        } else {
            return (.01 * (relGoal - relCurrent) + (Math.signum(relGoal - relCurrent) * .025));
        }
    }

    double getPower(double currentPosition, double goal) {
       /*
        If under halfway to the goal, have the robot speed up by .01 for every angle until it is
        over halfway there
         */

        if (currentPosition < goal / 2) {

            return (.01 * currentPosition + (Math.signum(currentPosition) * .1));
        } else {
// Starts to slow down by .01 per angle closer to the goal.
            return (.01 * (goal - currentPosition + (Math.signum(currentPosition) * .1)));
        }
    }

    //Only use once per class and ALWAYS FOR VUFORIA
    public void goToPosition(double startX, double startY, double goalX, double goalY, double angleVu) {
        double angleImu = robot.getAngle();

        robot.offset = angleVu - angleImu;

        double xDiff = goalX - startX;
        double yDiff = goalY - startY;

        double angleGoal = Math.atan2(yDiff, xDiff) * (180 / Math.PI);

        angleImu = robot.getAngle();
        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

        if (distanceToGoal > 2) {
            while (Math.abs(angleGoal - angleImu) > 1) {
                angleImu = robot.getAngle();

                robot.left.setPower(-getPower(angleImu, angleGoal , angleVu));
                robot.right.setPower(getPower(angleImu, angleGoal , angleVu));
              //  telemetry.addData("Goal", String.format("%f %f", goalX, goalY));
             //   telemetry.addData("Start", String.format("%f %f", startX, startY));

                telemetry.addData("Goal Angle", angleGoal);
                telemetry.addData("angleGoal-angle ", AngleUnit.normalizeDegrees(angleGoal - angleImu));
                telemetry.addData("Robot Angle ", angleImu);
                //telemetry.addData("offset Angle", robot.offset);
                telemetry.addData("Power", getPower(angleImu, angleGoal));
                telemetry.update();
                idle();
            }


            robot.left.setPower(0);
            robot.right.setPower(0);


            int encodersToGoal = (int) (VuforiaUtilities.INCHES_TO_ENCODERCOUNTS * distanceToGoal);

            telemetry.addData("distance to goal", distanceToGoal);
            telemetry.addData("encoders to goal", encodersToGoal);
            telemetry.update();

            robot.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.left.setTargetPosition(encodersToGoal);
            robot.right.setTargetPosition(encodersToGoal);

            robot.left.setPower(.75);
            robot.right.setPower(.75);

            while (robot.left.isBusy()) {

                telemetry.addData("Goal", String.format("%f %f", goalX, goalY));
                telemetry.addData("Start", String.format("%f %f", startX, startY));
                telemetry.addData("left Counts", robot.left.getCurrentPosition());
                telemetry.addData("right Counts", robot.right.getCurrentPosition());
                telemetry.addData("distance to goal", distanceToGoal);
                telemetry.addData("encoders to goal", encodersToGoal);
                telemetry.update();

                idle();
            }

            robot.left.setPower(0);
            robot.right.setPower(0);
        } else {
            telemetry.addData("too close not moving or turning", "");
            telemetry.update();
        }

    }

    public PolarCoord getGoldenPostition(GoldPosition goldPostition, StartPosition startPostition) {
        if (startPostition == BLUE_DEPOT) {
            switch (goldPostition) {
                case LEFT:
                    return new PolarCoord(8.019544399, 42.70655476
                            , 13.7994854);
                case MIDDLE:
                    return new PolarCoord(23.52207794, 23.52207794
                            , 45);
                case RIGHT:
                    return new PolarCoord(42.70655476, 8.019544399
                            , 76.2005146);
            }
        } else if (startPostition == BLUE_CRATER) {

            switch (goldPostition) {
                case LEFT:
                    return new PolarCoord(42.70655476, -8.019544399
                            , -76.2005146);
                case MIDDLE:
                    return new PolarCoord(23.52207794, -23.52207794
                            , -45);
                case RIGHT:
                    return new PolarCoord(8.019544399, -42.70655476
                            , -13.7994854
                    );
                //zero is when robot id looking at blue cripto graph
            }
        } else if (startPostition == RED_DEPOT) {

            switch (goldPostition) {
                case LEFT:
                    return new PolarCoord(-8.019544399, -42.70655476
                            , -166.2005146
                    );
                case MIDDLE:
                    return new PolarCoord(-23.52207794, -23.52207794
                            , -135);
                case RIGHT:
                    return new PolarCoord(-42.70655476, -8.019544399
                            , -103.7994854
                    );
            }
        } else {
            switch (goldPostition) {
                case LEFT:
                    return new PolarCoord(-42.70655476, 8.019544399
                            , 103.7994854);
                case MIDDLE:
                    return new PolarCoord(-23.52207794, 23.52207794
                            , 135);
                case RIGHT:
                    return new PolarCoord(-8.019544399, 42.70655476
                            , 166.2005146);
            }
        }
        return new PolarCoord(0, 0, 0);
    }

    public double getDistance(StartPosition startPosition, PolarCoord goal) {
        double distance = 0.0;
        if (startPosition == BLUE_DEPOT) {
            distance = Math.sqrt(Math.pow(54 - goal.x, 2) + Math.pow(54 - goal.y, 2));

        } else if (startPosition == BLUE_CRATER) {
            distance = Math.sqrt(Math.pow(54 - goal.x, 2) + Math.pow(-54 - goal.y, 2));

        } else if (startPosition == RED_DEPOT) {
            distance = Math.sqrt(Math.pow(-54 - goal.x, 2) + Math.pow(-54 - goal.y, 2));

        } else {
            distance = Math.sqrt(Math.pow(-54 - goal.x, 2) + Math.pow(54 - goal.y, 2));

        }
        return distance;
    }
}