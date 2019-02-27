package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.RRCoachBotHardware;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;


@TeleOp(name="EncoderTest")
public class EncoderTest extends OpMode {
    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);

    }

    @Override
    public void loop() {

        if(gamepad1.a){
            robot.shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            robot.shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        telemetry.addData("Shoulder:", robot.shoulder.getCurrentPosition());
        telemetry.addData("Elbow:", robot.elbow.getCurrentPosition());
        telemetry.update();



    }

}