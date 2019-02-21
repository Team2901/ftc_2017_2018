package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.GoldPosition.MIDDLE;

@Autonomous(name = "DepotOnlyAuto")
public class DepotOnlyAuto extends BaseRoverRuckusAuto {

    public DepotOnlyAuto() {
        super(StartCorner.BLUE_DEPOT, GoldPosition.MIDDLE, true, false, false);
    }


    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        final PolarCoord depotPosition = getDepotPosition();

        goToPosition(dropPosition, depotPosition);

        dropMarker();

        goToDistance(-20, .75, "back out of depot");

        while(opModeIsActive()) {
            idle();
        }
    }
}
