package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.RED_CRATER;

@Disabled
@Autonomous(name = "RoverRuckus Red Crater", group = "RoverRuckus")
public class RoverRuckusAutonomousRedCrater extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousRedCrater() {
        super(RED_CRATER);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        runOpModeCraterCorner();

        while(opModeIsActive()) {
            idle();
        }
    }
}
