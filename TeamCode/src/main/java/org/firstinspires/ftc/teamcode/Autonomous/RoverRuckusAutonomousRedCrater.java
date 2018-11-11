package org.firstinspires.ftc.teamcode.Autonomous;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
import org.firstinspires.ftc.teamcode.Utility.RelicRecoveryUtilities;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;


@Autonomous(name = "RoverRuckusAutonomous Red Crater")
public class RoverRuckusAutonomousRedCrater extends LinearOpMode {

    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();
    VuforiaLocalizer vuforia;
    WebcamName webcam;
    VuforiaTrackables trackables;
    VuforiaTrackable blue;
    VuforiaTrackable red;
    VuforiaTrackable front;
    VuforiaTrackable back;
    public static final int TARGET_POSITION = 1120;
    public String initialPosition = "crater";
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


    enum GoldPosition {

        LEFT, MIDDLE, RIGHT
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //step -2: initialize hardware
        robot.init(hardwareMap);
        // step -1: initialize vuforia
        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getBackCameraParameters(hardwareMap);
        vuforia = VuforiaUtilities.getVuforia(parameters);


        VuforiaTrackables roverRuckus = VuforiaUtilities.setUpTrackables(vuforia, parameters);
        blue = roverRuckus.get(0);
        red = roverRuckus.get(1);
        front = roverRuckus.get(2);
        back = roverRuckus.get(3);
        //step 0: locate cheddar
        GoldPosition goldPosition = determineGoldPosition();
        waitForStart();
        //step 1: drop down from lander
        dropFromLander();
        //step 2: do vuforia to determine position
        OpenGLMatrix location = VuforiaUtilities.getLocation(blue, red, front, back);

        VectorF translation = location.getTranslation();

        Orientation orientation = Orientation.getOrientation(location,
                AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);


        x =(translation.get(0) * VuforiaUtilities.MM_TO_INCHES);
        y = (translation.get(1) * VuforiaUtilities.MM_TO_INCHES);
        z = ((translation.get(2) * VuforiaUtilities.MM_TO_INCHES));
        angleVu = orientation.thirdAngle;


        //step 3: go to the cheddar pivot point
        PolarCoord goal = getGoalPosition(goldPosition);
        goToPosition(x,y, goal.x , goal.y ,angleVu);

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

            telemetry.addData("Goal Angle", goal.theta);
            telemetry.addData("angleGoal-angle ", goal.theta - angleImu);
            telemetry.addData("Power", getPower(angleImu, goal.theta));
            telemetry.update();
            idle();
        }
        //step 5: gooooo
        double distance = Math.sqrt(Math.pow(-54 - goal.x, 2) + Math.pow(-54 - goal.y, 2));

        robot.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.right.setTargetPosition((int) (distance * VuforiaUtilities.INCHES_TO_ENCODERCOUNTS));
        robot.left.setTargetPosition((int) (distance * VuforiaUtilities.INCHES_TO_ENCODERCOUNTS));

        telemetry.addData("distance to goal", distance);
        telemetry.addData("encoders to goal", distance * VuforiaUtilities.INCHES_TO_ENCODERCOUNTS);
        telemetry.update();

        robot.left.setPower(1);
        robot.right.setPower(1);
        //step 7: do corner action(if depot: drop team marker/if crater: park)
        doCornerAction();
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

        Bitmap bitmap = RelicRecoveryUtilities.getVuforiaImage(vuforia);
        try {
            RelicRecoveryUtilities.saveBitmap(jewelBitmap, bitmap);

            RelicRecoveryUtilities.saveHueFile("jewelHuesBig.txt", bitmap);

            int leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft, jewelBitmapLeft, "jewelHuesLeft.txt");
            int middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle, jewelBitmapMiddle, "jewelHuesMiddle.txt");
            int rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight, jewelBitmapRight, "jewelHuesRight.txt");

            RelicRecoveryUtilities.totalYellowHues(leftHueTotal, middleHueTotal, rightHueTotal);
            if (leftHueTotal > middleHueTotal && middleHueTotal > rightHueTotal) {
                return GoldPosition.LEFT;
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
        if (initialPosition.equals("depot")) {
            robot.marker.setPosition(1);
        } else if (initialPosition.equals("crater")) {
            ;
        } else {
            robot.left.setPower(1);
            final double currentTime = time;
            double targetTime = currentTime + 5;
            while (targetTime < time) {
                idle();
            }
            robot.left.setPower(0);
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

        while (Math.abs(angleGoal - angleImu) > 1) {
            angleImu = robot.getAngle();

            robot.left.setPower(-getPower(angleImu, angleGoal));
            robot.right.setPower(getPower(angleImu, angleGoal));

            telemetry.addData("Goal Angle", angleGoal);
            telemetry.addData("angleGoal-angle ", angleGoal - angleImu);
            telemetry.addData("angleImu", angleImu);
            telemetry.addData("Power", getPower(angleImu, angleGoal));
            telemetry.update();
            idle();
        }


        robot.left.setPower(0);
        robot.right.setPower(0);

        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

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
            telemetry.addData("left Counts", robot.left.getCurrentPosition());
            telemetry.addData("right Counts", robot.right.getCurrentPosition());
            telemetry.addData("distance to goal", distanceToGoal);
            telemetry.addData("encoders to goal", encodersToGoal);
            telemetry.update();

            idle();
        }

        robot.left.setPower(0);
        robot.right.setPower(0);

    }

    public PolarCoord getGoalPosition(GoldPosition goldPosition) {


        switch (goldPosition) {
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


        return new PolarCoord(0, 0, 0);
    }
}
