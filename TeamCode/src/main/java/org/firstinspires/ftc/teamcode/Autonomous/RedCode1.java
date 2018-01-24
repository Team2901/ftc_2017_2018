package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by gallagherb20503 on 11/7/2017.
 */

@Autonomous (name = "BlueCode1")
public class RedCode1 extends BaseCode {

    public void runOpMode() {
        teamColor = "red";
        super.runOpMode();



        int turnticks;
        int forwardmonkeys;

        if(isJewelOnLeft) {
            turnticks= 3000;
            forwardmonkeys= 3000;
        }
        else {
            turnticks= 3000 + 1140;
            forwardmonkeys= 3000 + 500;
        }

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftMotor.setTargetPosition(robot.leftMotor.getCurrentPosition() - turnticks); ///need//
        robot.rightMotor.setTargetPosition(robot.rightMotor.getCurrentPosition() + turnticks); //need//
        robot.leftMotor.setPower(.75); //  need//
        robot.rightMotor.setPower(-.75); //need//
        while (robot.leftMotor.isBusy()) {

        }

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftMotor.setTargetPosition(robot.leftMotor.getCurrentPosition() + forwardmonkeys);
        robot.rightMotor.setTargetPosition(robot.rightMotor.getCurrentPosition() + forwardmonkeys);
        robot.leftMotor.setPower(-.75);
        robot.rightMotor.setPower(.75);
    }
}