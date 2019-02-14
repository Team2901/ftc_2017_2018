package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
@Autonomous(name = "CraterOnlyAuto")
public class CraterOnlyAuto extends BaseRoverRuckusAuto {
    public CraterOnlyAuto() {
        super(StartCorner.BLUE_CRATER, GoldPosition.MIDDLE);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        robot.offset = dropPosition.theta;
        telemetry.addData("drop from lander", "");
        telemetry.update();
        if (dropSupported) {
            dropFromLander();
        }
        PolarCoord craterPosition = getJewelPosition();
        telemetry.addData("go to crater", "");
        telemetry.update();
        goToPosition(dropPosition, craterPosition);
    }
}