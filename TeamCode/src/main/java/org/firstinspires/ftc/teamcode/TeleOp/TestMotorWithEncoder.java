package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "TestMotorWithEncoder" , group = "Test")
public class TestMotorWithEncoder extends OpMode {

    DcMotor motor;

    @Override
    public void init() {
       motor = hardwareMap.dcMotor.get("motor");
    }

    @Override
    public void loop() {
    motor.setPower( - gamepad1.right_stick_y);

    telemetry.addData("Joystick" , gamepad1.right_stick_y);
    telemetry.addData("MotorPower" , motor.getPower());
    telemetry.addData("Motor Position" , motor.getCurrentPosition());
    telemetry.addData("Ticks per Revolution" , motor.getMotorType().getTicksPerRev());
    telemetry.update();
    }
}
