package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "ServOwOTeleOwOp")
@Disabled

public class ServoTeleOwOp extends OpMode {

Servo servo;
    @Override
    public void init() {
        this.hardwareMap.servo.get("servo");
        servo = hardwareMap.servo.get("servo"); //OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO OwO
    }

    @Override
    public void loop() {
        if(gamepad1.x) {
            servo.setPosition(0);
            telemetry.addData("position", "0");
            telemetry.update();
        }
        else if (gamepad1.b) {
            servo.setPosition(1);
            telemetry.addData( "position ", "1");
            telemetry.update();
        }
    }
}
