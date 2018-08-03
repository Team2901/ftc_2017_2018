package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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

@Autonomous(name = "Spot 1 Red")
public class Spot1Red extends BaseCombinedAutonomous {

    @Override
    public void runOpMode() throws InterruptedException {

        myinit();

        waitForStart();

        pinch(false);


        robot.jewelKnockDevice.setPosition(.32);

        teamColor = "red";

        boolean jewelAskLeft = isOurJewelOnLeft();


        if (jewelAskLeft) {
            int wait = 0;
            robot.driveBackword(.25, .3);
            while (wait < 500) {
                wait++;
            }
            robot.jewelKnockDevice.setPosition(.85);
            robot.driveForword(.25, .3);

        }

        knockJewelLeft();

        int mark = readVuImage();

        robot.jewelKnockDevice.setPosition(1);

        columnOneMove(mark);

        robot.lift.setTargetPosition(0);
        robot.lift.setPower(.85);
        robot.lift.setTargetPosition(-1140 / 2);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) ;


        turn(75, "right");

        robot.driveForword(.75, .3);

        robot.open();

        robot.driveBackword(.4, .3);

        push();
    }
}