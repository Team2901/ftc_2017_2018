package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
@Autonomous(name = "Crater Parking Only", group = "RoverRuckusPartial")
public class CraterOnlyAuto extends BaseRoverRuckusAuto {
    public CraterOnlyAuto() {
        super(StartCorner.BLUE_CRATER,
                GoldPosition.MIDDLE,
                DEFAULT_IS_DROP_SUPPORTED,
                false,
                false);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        final PolarCoord craterPosition = new PolarCoord(40, -40, "crater");

        goToPosition(dropPosition, craterPosition);

        while(opModeIsActive()) {
            idle();
        }
    }
}