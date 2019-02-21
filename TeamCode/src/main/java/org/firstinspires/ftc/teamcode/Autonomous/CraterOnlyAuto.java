package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
@Autonomous(name = "CraterOnlyAuto")
public class CraterOnlyAuto extends BaseRoverRuckusAuto {
    public CraterOnlyAuto() {
        super(StartCorner.BLUE_CRATER, GoldPosition.MIDDLE, true, false, false);
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