package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "Servo TEst" , group = "Test")
public class ServoTest extends OpMode {

   Servo servo;
    @Override
    public void init() {
       servo = hardwareMap.servo.get("servo");
    }

    @Override
    public void loop() {

    }
}
