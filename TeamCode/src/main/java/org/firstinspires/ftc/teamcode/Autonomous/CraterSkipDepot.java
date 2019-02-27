package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.GoldPosition.MIDDLE;

@Autonomous(name = "Crater: Jewel and Parking", group = "RoverRuckusPartial")
public class CraterSkipDepot extends BaseRoverRuckusAuto {

    public CraterSkipDepot() {
        super(StartCorner.BLUE_CRATER);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        runOpModeSkipDepot();

        while(opModeIsActive()) {
            idle();
        }
    }

    public PolarCoord runOpModeSkipDepot() {
        final PolarCoord preJewelPosition = getPreJewelPosition();
        final PolarCoord jewelPosition = getJewelPosition();

        if (goldPosition != MIDDLE) {
            // Skip going to startPosition if jewel is in the middle
            currentPosition = goToPosition(currentPosition, startPosition, true);
        }

        if (useVuforiaNav) {
            PolarCoord vuforiaCurrentPosition = getVuforiaCurrentPosition();
            if (vuforiaCurrentPosition != null) {
                currentPosition = vuforiaCurrentPosition;
            }
        }

        currentPosition = goToPosition(currentPosition, preJewelPosition);

        goToPosition(preJewelPosition, jewelPosition);

        telemetry.addData(String.format("%10s", startPosition.name), formatMovement(dropPosition, startPosition));
        telemetry.addData(String.format("%10s", preJewelPosition.name), formatMovement(startPosition, preJewelPosition));
        telemetry.addData(String.format("%10s", jewelPosition.name), formatMovement(preJewelPosition, jewelPosition));
        telemetry.update();

        return currentPosition;
    }
}
