package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;

@Autonomous(name = "GoToDistanceTest", group = "Test")
public class GoToDistanceTest extends BaseRoverRuckusAuto {

    public GoToDistanceTest() {
        super(StartCorner.BLUE_CRATER);
     }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        goToDistance(6*12,.75);
    }
}
