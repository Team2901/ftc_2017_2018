package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.PidBotHardware;

@Autonomous(name = "PidPractice")
public class PidPractice extends LinearOpMode {

    PidBotHardware pidBot = new PidBotHardware();
    double goal = 90;

    @Override
    public void runOpMode() throws InterruptedException {
       pidBot.init(hardwareMap);


        waitForStart();
        while (opModeIsActive()) {
            double angle = pidBot.getAngle();

            if (Math.abs(goal-angle) < 1){
                pidBot.leftMotor.setPower(0);
                pidBot.rightMotor.setPower(0);
            }else{
                pidBot.leftMotor.setPower(-getPower(angle));
                pidBot.rightMotor.setPower(getPower(angle));
            }
            telemetry.addData("Angle" , angle);
            telemetry.update();

        }

    }
    double getPower(double currentPosition){
        if(currentPosition<goal/2){
            return (.05*currentPosition);
        }else{
            return (.05*(goal-currentPosition);
        }
    }
}