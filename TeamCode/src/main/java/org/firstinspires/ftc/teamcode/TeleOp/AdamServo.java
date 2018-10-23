package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name = "AdamServo", group = "TeleOp")

public class AdamServo extends OpMode {
    Servo servo = null;
    @Override
    public void init() {
        servo = this.hardwareMap.servo.get("servo");
    }

    @Override
    public void loop() {
        servo.setPosition(0.5);
    }
}
