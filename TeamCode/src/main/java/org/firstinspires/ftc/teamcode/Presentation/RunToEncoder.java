package org.firstinspires.ftc.teamcode.Presentation;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class RunToEncoder extends LinearOpMode {

    PresentationBotHardware robot = new PresentationBotHardware();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        waitForStart();

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setTargetPosition(5000);
        robot.rightMotor.setTargetPosition(5000);

        robot.leftMotor.setPower(.75);
        robot.rightMotor.setPower(.75);

    }
}
