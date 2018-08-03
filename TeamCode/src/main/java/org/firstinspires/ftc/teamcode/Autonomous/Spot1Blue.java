package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by kearneyg20428 on 10/23/2017. *
 *  1. it inits everything
 *  2. Then takes picture and samples it
 *  3. Finds the vuforia and saves the mark number
 *  4. moves to knock of jewel
 */



@Autonomous(name = "Spot 1 Blue")
public class Spot1Blue extends BaseCombinedAutonomous {


    @Override
    public void runOpMode() throws InterruptedException {

        myinit();

        waitForStart();

        pinch(true);

        teamColor = "blue";

        boolean jewelAskLeft = isOurJewelOnLeft();

        if (!jewelAskLeft) {
            robot.jewelKnockDevice.setPosition(.35);
        }
        robot.driveForword(.5, .3);

        int mark = readVuImage();
        robot.jewelKnockDevice.setPosition(1);
        robot.driveBackword(.4, .3);
        if (jewelAskLeft) {
            robot.jewelKnockDevice.setPosition(.35);
        }
        knockJewelRight();

        robot.jewelKnockDevice.setPosition(1);

        columnOneMove(mark);

        robot.lift.setTargetPosition(0);
        robot.lift.setPower(.85);

        robot.lift.setTargetPosition(-885);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) ;

        turn(80, "right");

        robot.driveForword(.6, .3);

        robot.open();

        robot.driveBackword(.4, .3);

        push();
    }
}