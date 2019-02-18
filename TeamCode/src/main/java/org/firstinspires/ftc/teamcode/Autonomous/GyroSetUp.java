package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

@Autonomous(name = "Gyro Setup", group = "Blue")
public class GyroSetUp extends BaseRoverRuckusAuto {

    public GyroSetUp() {
        super(BLUE_CRATER);
    }

    @Override
    public void runOpMode() throws InterruptedException {
     robot.init(hardwareMap);
       waitForStart();
        while(opModeIsActive()){
         
           telemetry.addData("angle" , robot.getAngle());
           telemetry.addData("tilt" , robot.getTilt());
           telemetry.update();
       }
    }
}
