package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.GoldPosition.MIDDLE;

@Autonomous(name = "Depot: Marker Only", group = "RoverRuckusPartial")
public class DepotOnlyAuto extends BaseRoverRuckusAuto {

    public DepotOnlyAuto() {
        super(StartCorner.BLUE_DEPOT,
                GoldPosition.MIDDLE,
                DEFAULT_IS_DROP_SUPPORTED,
                false,
                false);
    }


    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        final PolarCoord depotPosition = getDepotPosition();

        goToPosition(dropPosition, depotPosition);

        dropMarker();

        goToDistance(-48, .75, "back out of depot");

        while(opModeIsActive()) {
            idle();
        }
    }
}
