package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;

@Autonomous(name = "GoToPositionTest", group = "Test")
public class GoToPositionTest extends BaseRoverRuckusAuto {

    public GoToPositionTest() {
        super(StartCorner.BLUE_CRATER);
     }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        telemetry.addData("startCorner", startCorner);
        telemetry.update();

        goToPosition(new PolarCoord(0,0), new PolarCoord(24, 24));

        waitForStart();

        goToPosition(new PolarCoord(24,24), new PolarCoord(-24, -24));
    }
}
