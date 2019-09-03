package org.firstinspires.ftc.teamcode.NewProgrammers;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name="LastNameTeleOp")
                                public class LastNameTeleOp extends OpMode {
    public DcMotor topRight;

    public DcMotor topLeft;
    public DcMotor bottomRight;
    public DcMotor bottomLeft;

    @Override
    public void init() {
        topRight = hardwareMap.get(DcMotor.class, "topRight");
        topLeft = hardwareMap.get(DcMotor.class, "topLeft");
        bottomRight = hardwareMap.get(DcMotor.class, "bottomRight");
        bottomLeft = hardwareMap.get(DcMotor.class, "bottomLeft");

        topRight.setDirection(DcMotorSimple.Direction.REVERSE);
        bottomRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

        double leftValue = -gamepad1.left_stick_y;
        double rightValue = -gamepad1.right_stick_y;

        topRight.setPower(rightValue);
        bottomRight.setPower(rightValue);

        topLeft.setPower(leftValue);
        bottomLeft.setPower(leftValue);
    }
}