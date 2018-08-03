package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/*
 * Created by kearneyg20428 on 10/23/2017.
 *
 * This is my full autonomous
 * it is my baby
 * please talk with me when editing
 *
 *  1. it inits everything
 *  2. Then takes picture and samples it
 *  3. Finds the vuforia and saves the mark number
 *  4. moves to knock of jewel
 */


@Autonomous(name = "Test3")
public class Test3 extends BaseCombinedAutonomous {


    @Override
    public void runOpMode() throws InterruptedException {

        teamColor = "blue";

        myinit();
        waitForStart();

        pinch(false);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(-1140);
        robot.lift.setPower(1);

        while(robot.lift.isBusy());

        robot.jewelKnockDevice.setPosition(.42);

        knockJewelLeft();

        robot.jewelKnockDevice.setPosition(1);



        robot.driveForword(1.5, .3);

        turn(95, "left");

        int mark = 1;

        robot.driveForword(.2, .3);

        turn(90 , "right");

        robot.driveForword(.6,.3);

        robot.open();

        robot.driveBackword(.4 , .3);

        push();

        robot.lift.setTargetPosition(0);
        robot.lift.setPower(.85);

        while (opModeIsActive()) ;
    }

}