package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
@Autonomous(name = "DepotOnlyAuto")
public class DepotOnlyAuto extends BaseRoverRuckusAuto {
    public DepotOnlyAuto() {
        super(StartCorner.BLUE_DEPOT, GoldPosition.MIDDLE);
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
        PolarCoord depotPosition = getDepotPosition();
        telemetry.addData("go to depot", "");
        telemetry.update();
        goToPosition(dropPosition, depotPosition);
        telemetry.addData("drop marker", "");
        telemetry.update();
        dropMarker();
        telemetry.addData("moving back for opponents", "");
        telemetry.update();
        goToDistance(-20);
    }
}
