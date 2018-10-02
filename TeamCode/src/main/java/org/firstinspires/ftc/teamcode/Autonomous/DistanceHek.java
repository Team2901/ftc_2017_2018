package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="DistanceHek", group="Concept")
public class DistanceHek extends LinearOpMode {
    DistanceSensor distanceSensor;
    DcMotor leftMotor;
    DcMotor rightMotor;

    public void runOpMode() {
        distanceSensor = hardwareMap.get(DistanceSensor.class, "sensor_range");
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        waitForStart();

        while (opModeIsActive()) {
            double distanceBoi=distanceSensor.getDistance(DistanceUnit.INCH);
            if(distanceBoi<=24){
                leftMotor.setPower(00000000000000000000000000000000000000000000000000000000);
                rightMotor.setPower(00000000000000000000000000000000000000000000000000000000);
            } else {
                leftMotor.setPower(1);
                rightMotor.setPower(1);
            }
            telemetry.addData("inches", "%.2f", distanceSensor.getDistance(DistanceUnit.INCH));
            telemetry.update();
        }
    }
}
