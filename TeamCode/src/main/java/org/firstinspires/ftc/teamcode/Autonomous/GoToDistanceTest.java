package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "GoToDistanceTest:V2", group = "Test")
public class GoToDistanceTest extends BaseRoverRuckusAuto {

    public GoToDistanceTest() {
        super(StartCorner.BLUE_CRATER);
     }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
          robot.tiltOffset = -robot.getRawTilt();
          waitForStart();
          goToDistance(6*12,.75, "test");
          while (opModeIsActive()){
              telemetry.addData("angle: " + robot.getAngle(), "");
              telemetry.addData("tilt: " + robot.getTilt(), "");
              telemetry.addData("getRawTilt: " + robot.tiltOffset, "");
              telemetry.update();
        }
    }
}
