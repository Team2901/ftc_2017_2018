package org.firstinspires.ftc.teamcode.Autonomous;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.MotoLinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;
import org.firstinspires.ftc.teamcode.Utility.BitmapUtilities;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.GoldPosition.MIDDLE;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;
import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.RED_DEPOT;

@SuppressLint("DefaultLocale")
public class BaseRoverRuckusAuto extends MotoLinearOpMode {

    public enum StartCorner {
        RED_CRATER, RED_DEPOT, BLUE_CRATER, BLUE_DEPOT
    }

    public enum GoldPosition {

        LEFT, MIDDLE, RIGHT
    }

    public static final int GO_TO_ANGLE_BUFFER = 3;
    public static final int GO_TO_POSITION_BUFFER = 2;
    public static final int TARGET_LIFT_TICKS = 5000;
    public static final double P_DRIVE_COEFF = 0.05;

    public final RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    public VuforiaLocalizer vuforia;
    public WebcamName webcam;

    public boolean writeFiles = false;

    // Default goldPosition to use if determineGoldPosition = false
    public GoldPosition goldPosition = MIDDLE;
    public boolean determineGoldPosition = true;

    public boolean dropSupported = true;

    public boolean isVuforiaActive = true;
    public boolean vuNav = false;

    public final StartCorner startCorner;
    public final PolarCoord dropPosition;
    public final PolarCoord startPosition;

    public BaseRoverRuckusAuto(StartCorner startCorner) {
        this.startCorner = startCorner;
        dropPosition = getDropPosition();
        startPosition = getStartPosition();
    }
    public BaseRoverRuckusAuto(StartCorner startCorner, GoldPosition goldPosition) {
        this.startCorner = startCorner;
        dropPosition = getDropPosition();
        startPosition = getStartPosition();
        this.goldPosition = goldPosition;
    }

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("startCorner", startCorner);
        telemetry.update();

        //step -2: initialize hardware
        robot.init(hardwareMap);

        robot.offset = dropPosition.theta;

        telemetry.addData("begin setup vuforia", "");
        telemetry.update();

        // step -1: initialize vuforia
        VuforiaTrackables roverRuckus = null;

        try {
            if (isVuforiaActive) {
                webcam = hardwareMap.get(WebcamName.class, "webcam");
                if (webcam != null) {
                    VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebCameraParameters(hardwareMap, webcam);
                    vuforia = VuforiaUtilities.getVuforia(parameters);
                    if (vuNav) {
                        roverRuckus = VuforiaUtilities.setUpTrackables(vuforia, parameters);
                    }
                }
            }
        } catch (Exception e) {

        }

        telemetry.addData("I setup vuforia", vuforia != null);
        telemetry.update();
        waitForStart();

        telemetry.addData("starting determine goldPosition", "");
        telemetry.update();

        //step 0: locate cheddar
        if (determineGoldPosition) {
            goldPosition = determineGoldPosition();
        }

        telemetry.addData("goldPosition", goldPosition);
        telemetry.update();

        //step 1: drop down from lander
        if (dropSupported) {
            dropFromLander();
        }

        //step 1.5 move 2 inches away from lander
        PolarCoord currentPosition = goToPosition(dropPosition, startPosition, true);

        //step 2: do vuforia to determine position
        if (vuNav && roverRuckus != null) {
            roverRuckus.activate();

            OpenGLMatrix location = VuforiaUtilities.getLocation(roverRuckus);
            if (location != null) {
                currentPosition = new PolarCoord(location);
            }
        }

        if (startCorner == BLUE_DEPOT || startCorner == RED_DEPOT) {
            currentPosition = runOpModeDepotCorner(currentPosition);
        } else {
            currentPosition = runOpModeCraterCorner(currentPosition);
        }

        while (

                opModeIsActive())

        {
            idle();
        }

    }

    public PolarCoord runOpModeDepotCorner(PolarCoord currentPosition) {

        final PolarCoord startPosition = currentPosition;
        final PolarCoord preJewelPosition = getPreJewelPosition();
        final PolarCoord depotPosition = getDepotPosition();
        final PolarCoord postDepotPosition = getPostDepotPosition();
        final PolarCoord craterPosition = getCraterPosition();

        currentPosition = goToPosition(currentPosition, preJewelPosition);
        currentPosition = goToPosition(currentPosition, depotPosition);

        dropMarker();

        currentPosition = goToPosition(currentPosition, postDepotPosition);
        currentPosition = goToPosition(currentPosition, craterPosition);

        telemetry.addData("Start     ", formatMovement(dropPosition, startPosition));
        telemetry.addData("PreJewel  ", formatMovement(startPosition, preJewelPosition));
        telemetry.addData("Depot     ", formatMovement(preJewelPosition, depotPosition));
        telemetry.addData("PostDepot ", formatMovement(depotPosition, postDepotPosition));
        telemetry.addData("Crater    ", formatMovement(postDepotPosition, craterPosition));
        telemetry.addData("Angle     ", String.format("%.1f", robot.getAngle()));
        telemetry.addData("JewelPos  ", goldPosition);
        telemetry.update();

        return currentPosition;
    }

    public PolarCoord runOpModeCraterCorner(PolarCoord currentPosition) {

        final PolarCoord startPosition = currentPosition;
        final PolarCoord preJewelPosition = getPreJewelPosition();
        final PolarCoord jewelPosition = getJewelPosition();
        final PolarCoord safePosition = getSafePosition();
        final PolarCoord preDepot = getPreDepotPosition();
        final PolarCoord depotPosition = getDepotPosition();
        final PolarCoord craterPosition = getCraterPosition();

        currentPosition = goToPosition(currentPosition, preJewelPosition);

        goToPosition(preJewelPosition, jewelPosition);
        goToDistance(-PolarCoord.getDistanceBetween(jewelPosition, preJewelPosition), .75);

        currentPosition = goToPosition(currentPosition, safePosition);
        currentPosition = goToPosition(currentPosition, preDepot);

        goToPosition(preDepot, depotPosition);
        dropMarker();
        goToDistance(-PolarCoord.getDistanceBetween(depotPosition, preDepot), .75);

        currentPosition = goToPosition(currentPosition, craterPosition);

        telemetry.addData("Start   ", formatMovement(dropPosition, startPosition));
        telemetry.addData("PreJewel", formatMovement(startPosition, preJewelPosition));
        telemetry.addData("Jewel   ", formatMovement(preJewelPosition, jewelPosition));
        telemetry.addData("Safe    ", formatMovement(preJewelPosition, safePosition));
        telemetry.addData("preDepot", formatMovement(safePosition, preDepot));
        telemetry.addData("Depot   ", formatMovement(preDepot, depotPosition));
        telemetry.addData("Crater  ", formatMovement(preDepot, craterPosition));
        telemetry.addData("Angle   ", String.format("%.1f", robot.getAngle()));
        telemetry.addData("JewelPos  ", goldPosition);
        telemetry.update();

        return currentPosition;
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

        try {
            Bitmap bitmap = BitmapUtilities.getVuforiaImage(vuforia);
            telemetry.addData("bitmap found", bitmap != null);
            telemetry.update();
            if (bitmap != null) {
                try {
                    if (writeFiles) {
                        FileUtilities.writeBitmapFile("jewelBitmap.png", bitmap);
                        FileUtilities.writeHueFile("jewelHuesBig.txt", bitmap);
                    }

                    int[] leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap,
                            "jewelConfigLeft.txt", "jewelBitmapLeft.png",
                            "jewelHuesLeft.txt", this, writeFiles);
                    int[] middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap,
                            "jewelConfigMiddle.txt", "jewelBitmapMiddle.png",
                            "jewelHuesMiddle.txt", this, writeFiles);
                    int[] rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap,
                            "jewelConfigRight.txt", "jewelBitmapRight.png",
                            "jewelHuesRight.txt", this, writeFiles);
                    telemetry.addData("getJewelHueCount complete", "");
                    telemetry.update();
                    GoldPosition winner = BitmapUtilities.findWinnerLocation(middleHueTotal, rightHueTotal);

                    if (writeFiles) {
                        FileUtilities.writeWinnerFile(winner, leftHueTotal, middleHueTotal, rightHueTotal);
                    }
                    return winner;
                } catch (Exception e) {
                    telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
                    telemetry.update();
                    return GoldPosition.MIDDLE;
                }
            } else {
                telemetry.addData("ERROR reading bitmap", "");
                telemetry.update();
                return GoldPosition.MIDDLE;
            }
        } catch (Exception e) {
            return GoldPosition.MIDDLE;
        }
    }

    public void dropMarker() {
        robot.marker.setPosition(robot.markerDropPosition);

        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() < 1) {
            idle();
        }
    }

    public PolarCoord goToPosition(PolarCoord startPosition, PolarCoord goalPosition) {
        return goToPosition(startPosition, goalPosition, false);
    }

    public PolarCoord goToPosition(PolarCoord startPosition, PolarCoord goalPosition, boolean override) {
        return goToPosition(startPosition.x, startPosition.y, goalPosition.x, goalPosition.y, override);
    }

    public PolarCoord goToPosition(double startX, double startY,
                                   double goalX, double goalY,
                                   boolean override) {

        double xDiff = goalX - startX;
        double yDiff = goalY - startY;

        double angleGoal = Math.atan2(yDiff, xDiff) * (180 / Math.PI);
        double angleStart = robot.getAngle();

        double goalDistance = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

        if (goalDistance > GO_TO_POSITION_BUFFER || override) {
            goToAngle(angleStart, angleGoal);
            goToDistance(goalDistance, .75);
        } else {
            telemetry.addData("Too close. Skipped turning and moving", "");
            telemetry.update();
        }

        return new PolarCoord(goalX, goalY, robot.getAngle());
    }

    public void goToAngle(double angleStart, double angleGoal) {
        double angleCurrent = angleStart;

        while (Math.abs(angleGoal - angleCurrent) > GO_TO_ANGLE_BUFFER) {
            angleCurrent = robot.getAngle();
            double power = Math.abs(getPower(angleCurrent, angleGoal, angleStart));
            robot.turn(-power);

            telemetry.addData("Start Angle ", "%.1f", angleStart);
            telemetry.addData("Goal Angle  ", "%.1f", angleGoal);
            telemetry.addData("Cur Angle   ", "%.1f", angleCurrent);
            telemetry.addData("Remain Angle", "%.1f", AngleUnit.normalizeDegrees(angleGoal - angleCurrent));
            telemetry.addData("Power       ", "%.2f", power);
            telemetry.update();
            idle();
        }

        robot.goStraight(0);
    }

    public void goToDistance(double goalDistance) {
        final int ticksToGoal = (int) (robot.getInchesToEncoderCounts() * goalDistance);

        robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.setTargetPosition(ticksToGoal);

        robot.goStraight(0.75);

        while (robot.isLeftBusy()) {
            double currentTicks = robot.getLeftCurrentPosition();
            telemetry.addData("Goal Dist    ", "%.2f", goalDistance);
            telemetry.addData("Current Dist ", "%.2f", currentTicks / robot.getInchesToEncoderCounts());
            telemetry.addData("Goal Ticks   ", ticksToGoal);
            telemetry.addData("Current Ticks", currentTicks);
            telemetry.update();
            idle();
        }

        robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.goStraight(0);
    }

    public void goToDistance(double distance,
                             double speed) {
        // Ensure that the opmode is still active
        if (!opModeIsActive()) {
            return;
        }

        double angle = robot.getAngle();
        speed = Range.clip(Math.abs(speed), 0.0, 1.0);

        robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Determine new target position, and pass to motor controller
        int targetTicks = (int) (distance * robot.getInchesToEncoderCounts());
        int leftTargetTicks = robot.left.getCurrentPosition() + targetTicks;
        int rightTargetTicks = robot.right.getCurrentPosition() + targetTicks;

        // Set Target and Turn On RUN_TO_POSITION
        robot.left.setTargetPosition(leftTargetTicks);
        robot.right.setTargetPosition(rightTargetTicks);

        // start motion.
        robot.left.setPower(speed);
        robot.right.setPower(speed);

        // keep looping while we are still active, and BOTH motors are running.
        while (opModeIsActive() &&
                (robot.left.isBusy() && robot.right.isBusy())) {

            // adjust relative speed based on heading error.
            double error = AngleUtilities.getNormalizedAngle(robot.getAngle() - angle);
            double steer = Range.clip(error * P_DRIVE_COEFF, -1, 1);

            // if driving in reverse, the motor correction also needs to be reversed
            if (distance < 0)
                steer *= -1.0;

            double leftSpeed = speed - steer;
            double rightSpeed = speed + steer;

            // Normalize speeds if either one exceeds +/- 1.0;
            double max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
            if (max > 1.0) {
                leftSpeed /= max;
                rightSpeed /= max;
            }

            robot.left.setPower(leftSpeed);
            robot.right.setPower(rightSpeed);

            // Display drive status for the driver.
            telemetry.addData("Error/Steer  ", "%5.1f/%5.1f", error, steer);
            telemetry.addData("Goal Distance", "%5.2f", distance);
            telemetry.addData("Goal    Ticks", "%7d:%7d", targetTicks);
            telemetry.addData("Current Ticks", "%7d:%7d", robot.left.getCurrentPosition(),
                    robot.right.getCurrentPosition());
            telemetry.addData("Speed", "%5.2f:%5.2f", leftSpeed, rightSpeed);
            telemetry.update();
        }

        // Stop all motion;
        robot.left.setPower(0);
        robot.right.setPower(0);

        robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getPower(double absCurrent, double absGoal, double absStart) {
        double relCurrent = AngleUtilities.getNormalizedAngle(absCurrent - absStart);
        double relGoal = AngleUtilities.getNormalizedAngle(absGoal - absStart);
        return getPower(relCurrent, relGoal);
    }

    public double getPower(double currentPosition, double goal) {
        double remainingDistance = AngleUtilities.getNormalizedAngle(goal - currentPosition);
        double basePower = .01 * remainingDistance;
        double stallPower = .1 * Math.signum(remainingDistance);
        return basePower + stallPower;
    }

    public PolarCoord getDropPosition() {
        switch (startCorner) {
            case BLUE_DEPOT:
                return new PolarCoord(13, 13, 45);
            case BLUE_CRATER:
                return new PolarCoord(13, -13, -45);
            case RED_DEPOT:
                return new PolarCoord(-13, -13, -135);
            case RED_CRATER:
                return new PolarCoord(-13, 13, 135);
        }

        return new PolarCoord(0, 0);
    }

    public PolarCoord getStartPosition() {
        switch (startCorner) {
            case BLUE_DEPOT:
                return new PolarCoord(18, 18);
            case BLUE_CRATER:
                return new PolarCoord(18, -18);
            case RED_DEPOT:
                return new PolarCoord(-18, -18);
            case RED_CRATER:
                return new PolarCoord(-18, 18);
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getPreJewelPosition() {
        switch (this.startCorner) {
            case BLUE_DEPOT:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(8.019544399, 42.70655476, 13.7994854);
                    case MIDDLE:
                        return new PolarCoord(23.52207794, 23.52207794, 45);
                    case RIGHT:
                        return new PolarCoord(42.70655476, 8.019544399, 76.2005146);
                }
            case BLUE_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(42.70655476, -8.019544399, -76.2005146);
                    case MIDDLE:
                        return new PolarCoord(23.52207794, -23.52207794, -45);
                    case RIGHT:
                        return new PolarCoord(8.019544399, -42.70655476, -13.7994854);
                }
            case RED_DEPOT:

                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-8.019544399, -42.70655476, -166.2005146);
                    case MIDDLE:
                        return new PolarCoord(-23.52207794, -23.52207794, -135);
                    case RIGHT:
                        return new PolarCoord(-42.70655476, -8.019544399, -103.7994854);
                }
            case RED_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-42.70655476, 8.019544399, 103.7994854);
                    case MIDDLE:
                        return new PolarCoord(-23.52207794, 23.52207794, 135);
                    case RIGHT:
                        return new PolarCoord(-8.019544399, 42.70655476, 166.2005146);
                }
        }
        return new PolarCoord(0, 0, 0);
    }

    public PolarCoord getJewelPosition() {
        switch (this.startCorner) {
            case BLUE_DEPOT:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(48.5, 27.0);
                    case MIDDLE:
                        return new PolarCoord(37.75, 37.75);
                    case RIGHT:
                        return new PolarCoord(27.0, 48.5);
                }
            case BLUE_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(48.5, -27.0);
                    case MIDDLE:
                        return new PolarCoord(37.75, -37.75);
                    case RIGHT:
                        return new PolarCoord(27.0, -48.5);
                }
            case RED_DEPOT:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-48.5, -27.0);
                    case MIDDLE:
                        return new PolarCoord(-37.75, -37.75);
                    case RIGHT:
                        return new PolarCoord(-27.0, -48.5);
                }
            case RED_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-48.5, 27.0);
                    case MIDDLE:
                        return new PolarCoord(-37.75, 37.75);
                    case RIGHT:
                        return new PolarCoord(-27.0, 48.5);
                }
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getDepotPosition() {
        switch (this.startCorner) {
            case BLUE_DEPOT:
                return new PolarCoord(50, 54);
            case BLUE_CRATER:
                switch (goldPosition) {
                    case RIGHT:
                        return new PolarCoord(62, 54);
                    case MIDDLE:
                        return new PolarCoord(58, 54);
                    case LEFT:
                        return new PolarCoord(58, 54);
                }
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-54, -54);
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getCraterPosition() {
        switch (this.startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                return new PolarCoord(60, -28);
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-60, 28);
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getSafePosition() {
        switch (this.startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                switch (goldPosition) {
                    case RIGHT:
                        return new PolarCoord(62, 0);
                    case MIDDLE:
                    case LEFT:
                        return new PolarCoord(58, 0);
                }
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-60, 24);
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getPreDepotPosition() {
        switch (this.startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                switch (goldPosition) {
                    case RIGHT:
                        return new PolarCoord(62, 36);
                    case MIDDLE:
                    case LEFT:
                        return new PolarCoord(58, 36);
                }
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-60, 36);
        }
        return new PolarCoord(0, 0);
    }

    public PolarCoord getPostDepotPosition() {
        switch (startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                return new PolarCoord(61, 36);
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-61, -36);
        }
        return new PolarCoord(0, 0);
    }

    public String formatMovement(PolarCoord startPosition, PolarCoord endPosition) {
        double xDiff = endPosition.x - startPosition.x;
        double yDiff = endPosition.y - startPosition.y;

        double angleGoal = Math.atan2(yDiff, xDiff) * (180 / Math.PI);
        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

        return String.format("(%.1f, %.1f)   %.0f     %.1f", endPosition.x, endPosition.y, angleGoal, distanceToGoal);
    }
}