package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Presentation.PresentationBotHardware;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import java.util.ArrayList;


@Autonomous (name = "OneCornerAuto")
public class OneCornerAuto extends LinearOpMode {

    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    WebcamName webcam;
    VuforiaLocalizer vuforia;

    VuforiaTrackable blue;
    VuforiaTrackable red;
    VuforiaTrackable front;
    VuforiaTrackable back;

    String startPos = "blueCrater";
    String goldPos = "right";

    //for never rest 40s
    public static final double ENCODER_COUNTS_PER_REV = 1120;
    //measured may not be exact
    public static final double INCHES_PER_ROTATION = 7.75;
    static final double INCHES_TO_ENCODERCOUNTS = ((1 / INCHES_PER_ROTATION) * ENCODER_COUNTS_PER_REV);

    @Override
    public void runOpMode() {


        webcam = hardwareMap.get(WebcamName.class, "webcam");
        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebcamParameters(hardwareMap, webcam);
        vuforia = VuforiaUtilities.getVuforia(parameters);



        VuforiaTrackables roverRuckus =  VuforiaUtilities.setUpTrackables( vuforia , parameters);
        blue = roverRuckus.get(0);
        red = roverRuckus.get(1);
        front = roverRuckus.get(2);
        back = roverRuckus.get(3);

        waitForStart();

        roverRuckus.activate();

        OpenGLMatrix location = VuforiaUtilities.getLocation(blue,red,front,back);


    }


    double getPower(double currentPosition, double goal) {
       /*
        If under halfway to the goal, have the robot speed up by .01 for every angle until it is
        over halfway there
         */

        if (currentPosition < goal / 2) {

            return (.01 * currentPosition + (Math.signum(currentPosition) * .075));
        } else {
// Starts to slow down by .01 per angle closer to the goal.
            return (.01 * (goal - currentPosition + (Math.signum(currentPosition) * .075)));
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
}