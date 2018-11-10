
package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Autonomous(name = "CodeBotDistanceSensorTestYAWN", group = "Sensor")
@Disabled

public class CodeBotDistanceSensorTestYAWN extends LinearOpMode {

    DistanceSensor rangeSensor;
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() {

        rangeSensor = hardwareMap.get(DistanceSensor.class, "sensor_range");
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            double distance = rangeSensor.getDistance(DistanceUnit.INCH);
            double power = power(distance);
            rightMotor.setPower(power);
            leftMotor.setPower(power);
            telemetry.addData("inch", "%.2f inch", rangeSensor.getDistance(DistanceUnit.INCH));
            telemetry.addData("power", "%.2f", power);
            telemetry.update();

        }
    }

    double power(double distance) {
        if (distance > 23 && distance < 25) {
            return 0;
        } else if (distance < 23) {
            return ((distance / 23) - 1);
        } else if (distance > 50 || distance == DistanceSensor.distanceOutOfRange) {
            return (1);
        } else {
            return ((distance - 25) / 25);
        }
    }
}