package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
@Autonomous(name = "Depo to Jewel and Back", group = "RoverRuckusPartial")
public class DepoToJewelAndBack extends BaseRoverRuckusAuto {

    public DepoToJewelAndBack() {
        super(BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT,
                BaseRoverRuckusAuto.GoldPosition.MIDDLE,
                DEFAULT_IS_DROP_SUPPORTED, DEFAULT_USE_WEBCAM, DEFAULT_USE_VUFORIA_NAV);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        runOpModeDepoToJewelAndBack();

        while(opModeIsActive())
        {
            idle();
        }
    }
    public PolarCoord runOpModeDepoToJewelAndBack() {

        final PolarCoord preJewelPosition = getPreJewelPosition();
        final PolarCoord depotPosition = getDepotPosition();

        currentPosition = goToPosition(dropPosition, startPosition, true);

        if (useVuforiaNav) {
            PolarCoord vuforiaCurrentPosition = getVuforiaCurrentPosition();
            if (vuforiaCurrentPosition != null) {
                currentPosition = vuforiaCurrentPosition;
            }
        }

        currentPosition = goToPosition(currentPosition, preJewelPosition);
        currentPosition = goToPosition(currentPosition, depotPosition);
        dropMarker();
        goToDistance(-PolarCoord.getDistanceBetween(depotPosition, preJewelPosition), 1, preJewelPosition.name);

        telemetry.addData(String.format("%10s", startPosition.name), formatMovement(dropPosition, startPosition));
        telemetry.addData(String.format("%10s", preJewelPosition.name), formatMovement(startPosition, preJewelPosition));
        telemetry.addData(String.format("%10s", depotPosition.name), formatMovement(preJewelPosition, depotPosition));
        telemetry.update();

        return currentPosition;
    }
}
