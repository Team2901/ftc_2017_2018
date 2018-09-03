package org.firstinspires.ftc.teamcode.Presentation;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Imu Demonstration")
public class ImuDemonstration extends LinearOpMode {

    PresentationBotHardware robot = new PresentationBotHardware();
    ElapsedTime timer = new ElapsedTime();
    double goal = 90;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        Double goalTime = null;
        waitForStart();

        while (opModeIsActive()) {

            while (opModeIsActive() && (goalTime == null || timer.time() - goalTime < 5)) {

                double angle = robot.getAngle();

                if (Math.abs(goal - angle) < 1) {
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                    if (goalTime == null) {
                        goalTime = timer.time();
                    }
                } else {
                    robot.leftMotor.setPower(-getPower(angle));
                    robot.rightMotor.setPower(getPower(angle));
                    goalTime = null;
                }
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
            telemetry.addData("I BROKE FREEE", "");
            telemetry.update();
        }
    }

    double getPower(double currentPosition) {
        if (currentPosition < goal / 2) {

            return (.01 * currentPosition + (Math.signum(currentPosition) * .075));
        } else {

            return (.01 * (goal - currentPosition + (Math.signum(currentPosition) * .075)));
        }
    }
}
