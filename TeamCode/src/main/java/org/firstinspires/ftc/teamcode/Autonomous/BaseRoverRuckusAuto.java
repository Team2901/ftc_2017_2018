package org.firstinspires.ftc.teamcode.Autonomous;

import android.annotation.SuppressLint;
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
import org.firstinspires.ftc.teamcode.Hardware.BaseRRHardware;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Utility.BitmapUtilities;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.GoldPosition.LEFT;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.RED_DEPOT;

@SuppressLint("DefaultLocale")
public class BaseRoverRuckusAuto extends LinearOpMode {

    public static final String jewelConfigLeft = "jewelConfigLeft.txt";
    public static final String jewelConfigMiddle = "jewelConfigMiddle.txt";
    public static final String jewelConfigRight = "jewelConfigRight.txt";
    public static final String jewelBitmap = "jewelBitmap.png";
    public static final String jewelBitmapLeft = "jewelBitmapLeft.png";
    public static final String jewelBitmapMiddle = "jewelBitmapMiddle.png";
    public static final String jewelBitmapRight = "jewelBitmapRight.png";

    public static final int TARGET_LIFT_TICKS = 5000;

    public enum StartCorner {
        RED_CRATER, RED_DEPOT, BLUE_CRATER, BLUE_DEPOT;
    }

    public enum GoldPosition {

        LEFT, MIDDLE, RIGHT
    }

    public final BaseRRHardware robot = new RoverRuckusBotHardware();
    public VuforiaLocalizer vuforia;


    public VuforiaTrackable blue;
    public VuforiaTrackable red;
    public VuforiaTrackable front;
    public VuforiaTrackable back;

    public WebcamName webcam;

    public StartCorner startCorner;
    public float angleStart;
    public double xStart;
    public double yStart;
    public double dropX;
    public double dropY;

    public boolean isVuforiaAcvtive = false;
    public boolean dropSupported = true;
    public boolean vuNav = false;

    @Override
    public void runOpMode() throws InterruptedException {

        //step -2: initialize hardware
        robot.init(hardwareMap);

        robot.offset = angleStart;

        // step -1: initialize vuforia
        VuforiaTrackables roverRuckus = null;
        if (isVuforiaAcvtive) {
            VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebCameraParameters(hardwareMap, webcam);
            vuforia = VuforiaUtilities.getVuforia(parameters);

            roverRuckus = VuforiaUtilities.setUpTrackables(vuforia, parameters);
            VuforiaTrackable blue = roverRuckus.get(0);
            VuforiaTrackable red = roverRuckus.get(1);
            VuforiaTrackable front = roverRuckus.get(2);
            VuforiaTrackable back = roverRuckus.get(3);
        }

        telemetry.addData("startCorner", startCorner);
        telemetry.update();

        waitForStart();

        //step 0: locate cheddar
        BaseRoverRuckusAuto.GoldPosition goldPosition = LEFT;//determineGoldPosition();

        //step 1: drop down from lander
        dropSupported = false;
        if (dropSupported) {
            dropFromLander();
        }
        //step 1.5 move 2 inches away from lander
        goToPosition(dropX, dropY, xStart, yStart, true);

        //step 2: do vuforia to determine position
        if (isVuforiaAcvtive && roverRuckus != null) {
            roverRuckus.activate();
        }

        OpenGLMatrix location = null;
        if (vuNav) {
            location = VuforiaUtilities.getLocation(blue, red, front, back);
        }
        //
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

        double x = (translation.get(0) * VuforiaUtilities.MM_TO_INCHES);
        double y = (translation.get(1) * VuforiaUtilities.MM_TO_INCHES);
        double z = ((translation.get(2) * VuforiaUtilities.MM_TO_INCHES));
        float angleVu = orientation.thirdAngle;

        double angleImu = robot.getAngle();


        //step 3: go to the cheddar pivot point
        PolarCoord preJewelPosition = getPreJewelPosition(goldPosition, startCorner);

        goToPosition(x, y, preJewelPosition.x, preJewelPosition.y);

        if (startCorner == BLUE_DEPOT || startCorner == RED_DEPOT) {
            runOpModeDepotCorner(goldPosition);
        } else {
            runOpModeCraterCorner(goldPosition);

        }


        while (opModeIsActive()) {
            idle();
        }
    }

    public void runOpModeDepotCorner(BaseRoverRuckusAuto.GoldPosition goldPosition) {

        PolarCoord preJewelPosition = getPreJewelPosition(goldPosition, startCorner);
        PolarCoord depotPosition = getDepotPosition(startCorner);
        PolarCoord craterPosition = getCraterPosition(startCorner);

        goToPosition(preJewelPosition, depotPosition);
        dropMarker();
        goToPosition(depotPosition, craterPosition);
    }

    public void runOpModeCraterCorner(BaseRoverRuckusAuto.GoldPosition goldPosition) {

        PolarCoord preJewelPosition = getPreJewelPosition(goldPosition, startCorner);

        PolarCoord jewelPosition = getJewelPosition(goldPosition, startCorner);

        goToPosition(preJewelPosition, jewelPosition);

        double distance = PolarCoord.getDistanceBetween(jewelPosition, preJewelPosition);
        //back up to prejewel position

        robot.resetEncoderCounts();

        robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.setTargetPosition((int) (distance * robot.getInchesToEncoderCounts()));

        while (robot.isLeftBusy()) {
            telemetry.addData("distance to goal", distance);
            telemetry.addData("encoders to goal", distance * robot.getInchesToEncoderCounts());
            telemetry.addData("encoders", robot.getLeftCurrentPosition());
            telemetry.update();

            robot.goStraight(1);
        }

    }

    public void dropFromLander() {

        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(TARGET_LIFT_TICKS);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) {
            idle();
        }

    }

    public GoldPosition determineGoldPosition() {

        Bitmap bitmap = BitmapUtilities.getVuforiaImage(vuforia);
        try {
            FileUtilities.writeBitmapFile(jewelBitmap, bitmap);

            FileUtilities.writeHueFile("jewelHuesBig.txt", bitmap);

            int[] leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft, jewelBitmapLeft,
                    "jewelHuesLeft.txt", this);
            int[] middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle, jewelBitmapMiddle,
                    "jewelHuesMiddle.txt", this);
            int[] rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight, jewelBitmapRight,
                    "jewelHuesRight.txt", this);

            GoldPosition winner = BitmapUtilities.findWinnerLocation(leftHueTotal, middleHueTotal, rightHueTotal);
            FileUtilities.writeWinnerFile(winner, leftHueTotal, middleHueTotal, rightHueTotal);
            return winner;
        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
            telemetry.update();
            return GoldPosition.MIDDLE;
        }

    }

    public void dropMarker() {
        robot.marker.setPosition(1);
        robot.marker.setPosition(0);
    }

    double getPower(double absCurrent, double absGoal, double absStart) {

        double relCurrent = AngleUnit.normalizeDegrees(absCurrent - absStart);
        double relGoal = AngleUnit.normalizeDegrees(absGoal - absStart);
        telemetry.addData("relCurrent", relCurrent);
        telemetry.addData("relGoal", relGoal);
        return getPower(relCurrent, relGoal);

    }

    double getPower(double currentPosition, double goal) {
        double remainingDistance = AngleUnit.normalizeDegrees(goal - currentPosition);
        double basePower = .01 * remainingDistance;
        double stallPower = .1 * Math.signum(remainingDistance);
        return basePower + stallPower;
        /*
         * If under halfway to the goal, have the robot speed up by .01 for every angle until it is
         * over halfway there
         */
   /*     if (goal > 0) {
            if (currentPosition < goal / 2) {

                return (.01 * currentPosition + (Math.signum((currentPosition == 0) ? goal : currentPosition) * .1));
            } else {
                // Starts to slow down by .01 per angle closer to the goal.
                return (.01 * (goal - currentPosition) + (Math.signum((currentPosition == 0) ? goal : currentPosition) * .1));
            }
        } else {
            if (currentPosition > goal / 2) {
                return (0.01 * currentPosition + (Math.signum((currentPosition == 0) ? goal : currentPosition) * .1));
            } else {
                return (0.01 * (goal - currentPosition) + (Math.signum((currentPosition == 0) ? goal : currentPosition) * .1));
            }
        }   */
    }

    public void goToPosition(PolarCoord startPolarCoord, PolarCoord goalPolarCoord) {
        goToPosition(startPolarCoord.x, startPolarCoord.y, goalPolarCoord.x, goalPolarCoord.y);
    }

    public void goToPosition(double startX, double startY, double goalX, double goalY) {
        goToPosition(startX, startY, goalX, goalY, false);
    }

    public void goToPosition(double startX, double startY, double goalX, double goalY, boolean overide) {

        double xDiff = goalX - startX;
        double yDiff = goalY - startY;

        double angleGoal = Math.atan2(yDiff, xDiff) * (180 / Math.PI);
        double angleStart = robot.getAngle();
        double angleCurrent = angleStart;

        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

        if (distanceToGoal > 2 || overide) {
            while (Math.abs(angleGoal - angleCurrent) > 1) {
                angleCurrent = robot.getAngle();
                double power = getPower(angleCurrent, angleGoal, angleStart);
                robot.turn(-power);

                telemetry.addData("Start Angle", angleStart);
                telemetry.addData("Goal Angle", angleGoal);
                telemetry.addData("Current Angle", angleCurrent);
                telemetry.addData("Remaining Angle", AngleUnit.normalizeDegrees(angleGoal - angleCurrent));
                telemetry.addData("offset", robot.offset);
                telemetry.addData("Power", power);
                telemetry.update();
                idle();
            }

            robot.goStraight(0);

            int ticksToGoal = (int) (robot.getInchesToEncoderCounts() * distanceToGoal);

            telemetry.addData("Distance to goal", distanceToGoal);
            telemetry.addData("Encoders to goal", ticksToGoal);
            telemetry.update();

            robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.setTargetPosition(ticksToGoal);

            robot.goStraight(0.75);

            while (robot.isLeftBusy()) {

                telemetry.addData("Goal", String.format("%f %f", goalX, goalY));
                telemetry.addData("Start", String.format("%f %f", startX, startY));
                telemetry.addData("Left Ticks", robot.getLeftCurrentPosition());
                telemetry.addData("Distance to goal", distanceToGoal);
                telemetry.addData("Ticks to goal", ticksToGoal);
                telemetry.update();

                idle();
            }
            robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.goStraight(0);
        } else {
            telemetry.addData("Too close. Skipped moving or turning", "");
            telemetry.update();
        }
    }

    public PolarCoord getPreJewelPosition(GoldPosition goldPosition, StartCorner startCorner) {
        if (startCorner == BLUE_DEPOT) {
            switch (goldPosition) {
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
        } else if (startCorner == BLUE_CRATER) {

            switch (goldPosition) {
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
            }
        } else if (startCorner == RED_DEPOT) {

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
        } else {
            switch (goldPosition) {
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

    public PolarCoord getJewelPosition(GoldPosition goldPosition, StartCorner startCorner) {
        if (startCorner == BLUE_DEPOT) {
            switch (goldPosition) {
                case LEFT:
                    return new PolarCoord(47.5, 26.0);
                case MIDDLE:
                    return new PolarCoord(36.75, 36.75);
                case RIGHT:
                    return new PolarCoord(26.0, 47.5);
            }
        } else if (startCorner == BLUE_CRATER) {

            switch (goldPosition) {
                case LEFT:
                    return new PolarCoord(47.5, -26.0);
                case MIDDLE:
                    return new PolarCoord(36.75, -36.75);
                case RIGHT:
                    return new PolarCoord(26.0, -47.5);
            }
        } else if (startCorner == RED_DEPOT) {

            switch (goldPosition) {
                case LEFT:
                    return new PolarCoord(-47.5, -26.0);
                case MIDDLE:
                    return new PolarCoord(-36.75, -36.75);
                case RIGHT:
                    return new PolarCoord(-26.0, -47.5);
            }
        } else {
            switch (goldPosition) {
                case LEFT:
                    return new PolarCoord(-47.5, 26.0);
                case MIDDLE:
                    return new PolarCoord(-36.75, 36.75);
                case RIGHT:
                    return new PolarCoord(-26.0, 47.5);
            }
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getDepotPosition(StartCorner startCorner) {
        switch (startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                return new PolarCoord(54, 54);
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-54, -54);
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getCraterPosition(StartCorner startCorner) {
        switch (startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                return new PolarCoord(60, -60);
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-60, 60);
        }
        return new PolarCoord(0, 0);
    }
}