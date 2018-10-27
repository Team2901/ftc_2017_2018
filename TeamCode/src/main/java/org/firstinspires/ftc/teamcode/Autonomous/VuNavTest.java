package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.CoachBotHardware;

@Autonomous (name = "VuNavTest")
public class VuNavTest extends LinearOpMode {

    static final double COUNTS_PER_MOTOR_REV = 1120;
    static final double MOTORREV_PER_WHEELREV = 2;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double INCHES_TO_ENCODERCOUNTS = (1 / (WHEEL_DIAMETER_INCHES * 3.1415)) *
            MOTORREV_PER_WHEELREV * COUNTS_PER_MOTOR_REV;


    CoachBotHardware robot = new CoachBotHardware();

    double xStart = 24;
    double yStart = 24;
    double angleStart = 45;

    double xGoal = 48;
    double yGoal = 24;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();



        goToPosition(xStart , yStart, xGoal , yGoal , angleStart);

        while (opModeIsActive()) {
            idle();
        }
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

    public void goToPosition(double startX, double startY, double goalX, double goalY, double angleVu) {
        double angleImu = robot.getAngle();

        robot.offset = angleVu - angleImu;

        double xDiff = goalX - startX;
        double yDiff = goalY - startY;

        double angleGoal = Math.atan2(yDiff, xDiff);

        angleImu = robot.getAngle();

        while (Math.abs(angleGoal - angleImu) > 1) {
            angleImu = robot.getAngle();

            robot.leftMotor.setPower(-getPower(angleImu, angleGoal));
            robot.rightMotor.setPower(getPower(angleImu, angleGoal));

            telemetry.addData("Goal Angle", angleGoal);
            telemetry.addData("angleGoal-angle ", angleGoal - angleImu);
            telemetry.addData("Power", getPower(angleImu, angleGoal));
            telemetry.update();
            idle();
        }

        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

        int encodersToGoal = (int) (INCHES_TO_ENCODERCOUNTS * distanceToGoal);

        telemetry.addData("distance to goal", distanceToGoal);
        telemetry.addData("encoders to goal", encodersToGoal);
        telemetry.update();

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setTargetPosition(encodersToGoal);
        robot.rightMotor.setTargetPosition(encodersToGoal);

        robot.leftMotor.setPower(.75);
        robot.rightMotor.setPower(.75);

        while (robot.leftMotor.isBusy()) {
            telemetry.addData("left Counts", robot.leftMotor.getCurrentPosition());
            telemetry.addData("right Counts", robot.rightMotor.getCurrentPosition());
            telemetry.addData("distance to goal", distanceToGoal);
            telemetry.addData("encoders to goal", encodersToGoal);
            telemetry.update();

            idle();
        }

        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

    }
}
