package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.PidBotHardware;

@Autonomous(name = "PidPractice")
public class PidPractice extends LinearOpMode {

    PidBotHardware pidBot = new PidBotHardware();
    ElapsedTime timer = new ElapsedTime();
    double goal = 90;

    @Override
    public void runOpMode() throws InterruptedException {
        pidBot.init(hardwareMap);


        Double goalTime = null;

        waitForStart();
        while (opModeIsActive() && (goalTime == null || timer.time() - goalTime < 5)) {
            double angle = pidBot.getAngle();
            if (Math.abs(goal - angle) < 1) {
                pidBot.leftMotor.setPower(0);
                pidBot.rightMotor.setPower(0);

                if (goalTime == null){

                    goalTime= timer.time();
                }
            } else {
                pidBot.leftMotor.setPower(-getPower(angle));
                pidBot.rightMotor.setPower(getPower(angle));

                goalTime = null;
            }
            telemetry.addData("Angle", angle);
            telemetry.update();


        }

        telemetry.addData("I BROKE FREEE", "");
        telemetry.update();

    }

    double getPower(double currentPosition) {
        if (currentPosition < goal / 2) {
            return (.01 * currentPosition + (Math.signum(currentPosition) * .075));
        } else {
            return (.01 * (goal - currentPosition + (Math.signum(currentPosition) * .075)));
        }
    }
}