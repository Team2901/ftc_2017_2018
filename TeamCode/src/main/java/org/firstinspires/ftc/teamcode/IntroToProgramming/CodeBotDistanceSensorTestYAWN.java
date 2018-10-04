
package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Autonomous(name = "CodeBotDistanceSensorTestYAWN", group = "Sensor")
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
            if (distance == 24) {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
            } else if (distance < 26) {
                leftMotor.setPower((distance * (1/24)) - 1);
                rightMotor.setPower(-1 * ((distance * (1/24)) - 1));
            } else if (distance > 50 || distance == DistanceSensor.distanceOutOfRange) {
                leftMotor.setPower(1);
                rightMotor.setPower(-1);
            } else {
                leftMotor.setPower(-1 * (distance - 24)*(1/26));
                rightMotor.setPower((distance - 24)*(1/26));
            }
            telemetry.addData("inch", "%.2f inch", rangeSensor.getDistance(DistanceUnit.INCH));
            telemetry.update();

        }
    }
}