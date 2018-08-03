package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
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

@Autonomous(name = "Spot 2 Red")
public class Spot2Red extends BaseCombinedAutonomous {

    @Override
    public void runOpMode() throws InterruptedException {

        myinit();



        waitForStart();

        pinch(false);

        robot.jewelKnockDevice.setPosition(.35);

        teamColor = "red";
        boolean jewelAskLeft = isOurJewelOnLeft();


        if (jewelAskLeft) {
            int wait1 = 0;
            robot.driveBackword(.25, .3);
            while (wait1 < 500) {
                wait1++;
            }
            robot.jewelKnockDevice.setPosition(.85);
            robot.driveForword(.25, .3);
        }
        knockJewelLeft();

        robot.jewelKnockDevice.setPosition(1);

        int mark = readVuImage();

        robot.driveForword(1, .3);

        robot.lift.setTargetPosition(0);
        robot.lift.setPower(.85);
        int wait2 = 1;

        while(wait2<500){
            wait2++;
        }
        robot.lift.setTargetPosition(-855);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) ;

        turn(90, "left");

        columnTwoMove(mark);

        turn(85 , "right");

        robot.driveForword(1,.3);

        robot.open();

        robot.driveBackword(.4 , .3);

        push();
    }
}