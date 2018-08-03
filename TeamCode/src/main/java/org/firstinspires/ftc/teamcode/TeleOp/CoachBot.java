package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by butterss21317 on 12/2/2017.
 */

//@TeleOp(name="CoachBot" , group = "TeleOp")
public class CoachBot extends OpMode {
    DcMotor armMotor;
    Servo handServo;
    @Override
    public void init() {
        armMotor   = hardwareMap.dcMotor.get("arm_motor");
        handServo = hardwareMap.servo.get("hand_servo");
    }

    @Override
    public void loop() {
        if (gamepad1.left_bumper) {
            armMotor.setPower(1.0);
        }
        else if (gamepad1.left_trigger != 0.0) {
            armMotor.setPower(-1.0);
        }
        else {
            armMotor.setPower(0.0);
        }
    //gamepad1.

    }
}
