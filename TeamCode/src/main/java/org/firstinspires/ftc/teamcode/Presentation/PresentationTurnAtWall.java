package org.firstinspires.ftc.teamcode.Presentation;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class PresentationTurnAtWall extends OpMode {

    PresentationBotHardware robot = new PresentationBotHardware();

    public void init() {

        robot.init(hardwareMap);

    }

    public void loop() {

        double distance = robot.distanceSensor.getDistance(DistanceUnit.INCH);

        if (distance < 24) {
            turn();
        }else {
            robot.leftMotor.setPower(.75);
            robot.rightMotor.setPower(.75);
        }

    }

    public void turn() {

    }
}