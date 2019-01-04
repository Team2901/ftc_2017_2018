package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Presentation.PresentationBotHardware;
import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import java.util.ArrayList;


@Disabled
@Autonomous (name = "OneCornerAuto")
public class OneCornerAuto extends LinearOpMode {
    enum StartPosition {
        CRATER_BLUE, DEPOT_BLUE;
    }

    enum GoldPosition {

        LEFT, MIDDLE, RIGHT;
    }

    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    WebcamName webcam;
    VuforiaLocalizer vuforia;

    VuforiaTrackable blue;
    VuforiaTrackable red;
    VuforiaTrackable front;
    VuforiaTrackable back;

    StartPosition startPos = StartPosition.CRATER_BLUE;
    GoldPosition goldPos = GoldPosition.RIGHT;


    double x;
    double y;
    double z;
    float angleVu;

    //for never rest 40s
    public static final double ENCODER_COUNTS_PER_REV = 1120;
    //measured may not be exact
    public static final double INCHES_PER_ROTATION = 7.75;
    static final double INCHES_TO_ENCODERCOUNTS = ((1 / INCHES_PER_ROTATION) * ENCODER_COUNTS_PER_REV);

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getBackCameraParameters(hardwareMap);
        vuforia = VuforiaUtilities.getVuforia(parameters);


        VuforiaTrackables roverRuckus = VuforiaUtilities.setUpTrackables(vuforia, parameters);
        blue = roverRuckus.get(0);
        red = roverRuckus.get(1);
        front = roverRuckus.get(2);
        back = roverRuckus.get(3);

        waitForStart();

        roverRuckus.activate();

        OpenGLMatrix location = null;
        ElapsedTime time = new ElapsedTime();
        while (location == null && time.seconds() < 5) {
            location = VuforiaUtilities.getLocation(blue, red, front, back);
        }

        VectorF translation = location.getTranslation();

        Orientation orientation = Orientation.getOrientation(location,
                AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        PolarCoord goal = getGoalPosition();

        x = 24;//(translation.get(0) * VuforiaUtilities.MM_TO_INCHES);
        y = 24; //(translation.get(1) * VuforiaUtilities.MM_TO_INCHES);
        z = 0;//(translation.get(2) * VuforiaUtilities.MM_TO_INCHES);
        angleVu = 0; //orientation.thirdAngle;


        telemetry.addData("X", "%.2f", x);
        telemetry.addData("Y", "%.2f", y);
        telemetry.addData("Z", "%.2f", z);
        telemetry.addData("Angle", "%.2f", angleVu);
        telemetry.update();


        goToPosition(x, y, goal.x, goal.y, angleVu);


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

        double distance = Math.sqrt(Math.pow(54 - goal.x, 2) + Math.pow(54 - goal.y, 2));

        robot.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.right.setTargetPosition((int) (distance * INCHES_TO_ENCODERCOUNTS));
        robot.left.setTargetPosition((int) (distance * INCHES_TO_ENCODERCOUNTS));

        telemetry.addData("distance to goal", distance);
        telemetry.addData("encoders to goal", distance * INCHES_TO_ENCODERCOUNTS);
        telemetry.update();

        robot.left.setPower(1);
        robot.right.setPower(1);

        while (robot.left.isBusy()) {
            idle();
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

        int encodersToGoal = (int) (INCHES_TO_ENCODERCOUNTS * distanceToGoal);

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

    public PolarCoord getGoalPosition() {
        if (startPos == StartPosition.CRATER_BLUE) {

            switch (goldPos) {
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
        } else {
            switch (goldPos) {
                case LEFT:
                    return new PolarCoord(42.70655476, -8.019544399

                            , -76.2005146);
                case MIDDLE:
                    return new PolarCoord(23.52207794, -23.52207794

                            , -45);
                case RIGHT:
                    return new PolarCoord(8.019544399, -42.70655476

                            , -13.7994854);
            }
        }
        return new PolarCoord(0, 0, 0);
    }
}