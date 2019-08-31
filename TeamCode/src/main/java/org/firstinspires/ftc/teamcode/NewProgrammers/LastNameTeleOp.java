package org.firstinspires.ftc.teamcode.NewProgrammers;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


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
    }

    @Override
    public void loop() {

        double leftValue = -gamepad1.left_stick_y;
        double rightValue = -gamepad1.right_stick_y;

        topRight.setPower(1);
        bottomRight.setPower(1);

        topLeft.setPower(1);
        bottomLeft.setPower(1);
    }
}