package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

@Autonomous(name = "TICKS Per inch", group = "RoverRuckus")
public class TicksPerINCH extends BaseRoverRuckusAuto {

    public TicksPerINCH() {
        super(BLUE_CRATER, GoldPosition.MIDDLE, false, false,false
        );
    }

    @Override
    public void runOpMode() throws InterruptedException {
      super.runOpMode();
      robot.armOut(this);
      while(opModeIsActive()) {
            idle();
        }
    }
}
