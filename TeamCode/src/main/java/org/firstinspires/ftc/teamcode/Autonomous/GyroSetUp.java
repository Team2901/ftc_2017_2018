package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

@Autonomous(name = "Gyro Setup Test", group = "Test")
public class GyroSetUp extends BaseRoverRuckusAuto {

    public GyroSetUp() {
        super(BLUE_CRATER,
                DEFAULT_GOLD_POSITION,
                false,
                false,
                false);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        while(opModeIsActive()){
            idle();
        }
    }
}
