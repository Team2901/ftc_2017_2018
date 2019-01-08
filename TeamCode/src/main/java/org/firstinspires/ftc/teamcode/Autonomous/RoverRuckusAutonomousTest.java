package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RoverRuckusAutonomous Test", group = "Test")
public class RoverRuckusAutonomousTest extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousTest() {
        super();
        startPosition = StartPosition.BLUE_CRATER;
        xStart = 0;
        yStart = 0;
        angleStart  = 0;

    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        telemetry.addData("startPosition", startPosition);
        telemetry.update();

        goToPosition(xStart, yStart, 24, 12);
        goToPosition(24, 12, 9, 30);
    }
}
