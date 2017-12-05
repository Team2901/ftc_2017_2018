package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeCamera;
import org.firstinspires.ftc.teamcode.Robot.Team2901RobotHardware;

/**
 * Created by gallagherb20503 on 11/7/2017.
 */

@Autonomous (name = "RedCode")
public class RedCode extends LinearOpModeCamera {

    Team2901RobotHardware robot = new Team2901RobotHardware();
    HardwareMap hwMap = null;

    public void runOpMode() {

        robot.init(hardwareMap);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("start","");
        telemetry.update();

        waitForStart();

        robot.leftMotor.setTargetPosition(3420);
        robot.leftMotor.setTargetPosition(3420);


        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setPower(0.5);
        robot.rightMotor.setPower(0.5);

        while(opModeIsActive());


    }
}



