
package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Autonomous(name = "AdamDistanceSensorTest", group = "Sensor")
@Disabled

public class AdamDistanceSensorTest extends LinearOpMode {

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
            if (distance >= 11 && distance <= 13) {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                telemetry.addData("leftPower", "0");
                telemetry.addData("rightPower", "0");
            } else if (distance > 50 || distance == DistanceSensor.distanceOutOfRange) {
                leftMotor.setPower(-1 * (distance - 12) * (1.0/26));
                rightMotor.setPower((distance - 12) * (1.0/26));
                telemetry.addData("leftPower", (distance - 24) * (1.0/26));
                telemetry.addData("rightPower", -1 * (distance - 24) * (1.0/26));
            } else {
                leftMotor.setPower((distance - 12) * (1.0/26));
                rightMotor.setPower(-1 * (distance - 12) * (1.0/26));
                telemetry.addData("leftPower", -1 * (distance - 12) * (1.0/26));
                telemetry.addData("rightPower", (distance - 12) * (1.0/26));
            }
            telemetry.addData("inch", "%.2f inch", rangeSensor.getDistance(DistanceUnit.INCH));
            telemetry.update();

        }
    }
}