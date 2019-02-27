package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "GoToDistance", group = "Test")
public class GoToDistanceTest extends BaseRoverRuckusAuto {

    public GoToDistanceTest() {
        super(StartCorner.BLUE_CRATER,
                DEFAULT_GOLD_POSITION,
                false,
                false,
                false);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        goToDistance(6*12,.75, "test");

        while(opModeIsActive()){
            idle();
        }
    }
}
