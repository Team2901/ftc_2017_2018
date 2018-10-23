package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.CoachBotHardware;
import org.firstinspires.ftc.teamcode.Presentation.PresentationBotHardware;

@Autonomous (name = "VuNavTest")
public class VuNavTest extends LinearOpMode {

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;
    static final double     DRIVE_GEAR_RATIO        = 0.5 ;
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;
    static final double        INCHES_TO_ENCODERCOUNTS  =  (WHEEL_DIAMETER_INCHES * 3.1415) *
            DRIVE_GEAR_RATIO * COUNTS_PER_MOTOR_REV;


    CoachBotHardware robot = new CoachBotHardware();

    double xStart = 24;
    double yStart = 24;
    double angleStart = 45;

    double xGoal = 48;
    double yGoal = 24;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        double angleImu = robot.getAngle();

        double offset = angleStart - angleImu;

        double xDiff = xGoal - xStart;
        double yDiff = yGoal - yStart;

        double angleGoal = Math.atan2(yDiff, xDiff) + offset;

        double angle = robot.getAngle();

        telemetry.addData("angle To Goal" , angleGoal);
        telemetry.update();

        waitForStart();

        while (Math.abs(angleGoal - angle) < 1) {
            angle = robot.getAngle();

            robot.leftMotor.setPower(-getPower(angle, angleGoal));
            robot.rightMotor.setPower(getPower(angle, angleGoal));

            telemetry.addData("Goal Angle" , angleGoal);
            telemetry.addData("angleGoal-angle " , angleGoal-angle);
            telemetry.addData("Power" , getPower(angle, angleGoal));
            telemetry.update();
        }

        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) * Math.pow(xDiff, 2)));

        int encodersToGoal = (int) (INCHES_TO_ENCODERCOUNTS * distanceToGoal);

        robot.leftMotor.setTargetPosition(encodersToGoal);
        robot.rightMotor.setTargetPosition(encodersToGoal);

        robot.leftMotor.setPower(.5);
        robot.rightMotor.setPower(.5);

        while (robot.leftMotor.isBusy()) {
            idle();
        }
    }



    double getPower(double currentPosition , double goal) {
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
}
