package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

@Disabled
@Autonomous(name = "GoToPosition", group = "Test")
public class GoToPositionTest extends BaseRoverRuckusAuto {

    public GoToPositionTest() {
        super(BLUE_CRATER,
                DEFAULT_GOLD_POSITION,
                false,
                false,
                false);
     }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        goToPosition(new PolarCoord(0,0), new PolarCoord(24, 24));

        goToPosition(new PolarCoord(24,24), new PolarCoord(-24, -24));

        while(opModeIsActive()){
            idle();
        }
    }
}
