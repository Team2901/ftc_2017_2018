package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;

@Autonomous(name = "GoToDistanceTest:V2", group = "Test")
public class GoToDistanceTest extends BaseRoverRuckusAuto {

    public GoToDistanceTest() {
        super(StartCorner.BLUE_CRATER);
     }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
          robot.tiltOffset = -robot.rawTilt();
          waitForStart();
          goToDistance(6*12,.75);
          while (opModeIsActive()){
              telemetry.addData("angle: " + robot.getAngle(), "");
              telemetry.addData("tilt: " + robot.getTilt(), "");
              telemetry.addData("rawTilt: " + robot.rawTiltOffset, "");
              telemetry.update();
        }
    }
}
