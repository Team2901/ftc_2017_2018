package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by gallagherb20503 on 11/7/2017.
 */

@Autonomous (name = "BlueCode1")
public class BlueCode1 extends BaseCode {

    public void runOpMode() {
        teamColor = "blue";
        super.runOpMode();



        int turnticks;
        int forwardmonkeys;

        if(isJewelOnLeft) {
            turnticks= 1300;
            forwardmonkeys= 4200;
        }
        else {
            turnticks= 900;
            forwardmonkeys= 4200;
        }

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setTargetPosition(robot.leftMotor.getCurrentPosition() + turnticks);
        robot.rightMotor.setTargetPosition(robot.rightMotor.getCurrentPosition() - turnticks);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setPower(.5); //  need//
        robot.rightMotor.setPower(.5); //need//
        while (robot.leftMotor.isBusy() || robot.rightMotor.isBusy()) {

        }

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setTargetPosition(robot.leftMotor.getCurrentPosition() - forwardmonkeys);
        robot.rightMotor.setTargetPosition(robot.rightMotor.getCurrentPosition() - forwardmonkeys);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setPower(.5);
        robot.rightMotor.setPower(.5);

        while (opModeIsActive()){

        }
    }
}