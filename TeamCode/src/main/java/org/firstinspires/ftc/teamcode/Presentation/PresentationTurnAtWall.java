package org.firstinspires.ftc.teamcode.Presentation;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "PresentationTurnatWall")
public class PresentationTurnAtWall extends OpMode {

    PresentationBotHardware robot = new PresentationBotHardware();

    public void init() {
        // Inits robot that we creates in Hardware
        robot.init(hardwareMap);

    }
//I LOVE PUPPIES
    public void loop() {
        //Determines distance to the nearest surface IN INCHES
        double distance = robot.distanceSensor.getDistance(DistanceUnit.INCH);
        //If less than 2ft, turn if not jeep going
        if (distance < 24) {
            turn();
        } else {
            robot.leftMotor.setPower(.75);
            robot.rightMotor.setPower(.75);
        }

    }

    //Using The IMU to turn 90^
    public void turn() {

    }
}