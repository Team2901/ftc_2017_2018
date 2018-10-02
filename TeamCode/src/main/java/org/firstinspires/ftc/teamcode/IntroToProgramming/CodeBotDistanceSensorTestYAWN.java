
package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Autonomous(name = "CodeBotDistanceSensorTestYAWN2", group = "Sensor")
public class CodeBotDistanceSensorTestYAWN extends LinearOpMode {

    DistanceSensor rangeSensor;
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() {

        rangeSensor = hardwareMap.get(DistanceSensor.class, "sensor_range");
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");


        waitForStart();

        while (opModeIsActive()) {

            double distance = rangeSensor.getDistance(DistanceUnit.INCH);
            if (distance >= 22 && distance <= 26) {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
            } else if (distance > 26) {
                leftMotor.setPower(.5);
                rightMotor.setPower(-.5);
            } else {
                leftMotor.setPower(-.5);
                rightMotor.setPower(.54);
            }
            telemetry.addData("inch", "%.2f inch", rangeSensor.getDistance(DistanceUnit.INCH));
            telemetry.update();

        }
    }
}