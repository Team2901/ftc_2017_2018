package org.firstinspires.ftc.teamcode.Presentation;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "PresentationTurnatWall")
public class PresentationTurnAtWall extends OpMode {

    PresentationBotHardware robot = new PresentationBotHardware();

    public void init() {
        // Inits robot that we creates in Hardware
        robot.init(hardwareMap);

    }

    public void loop() {
        //Determines distance to the nearest surface IN INCHES
        double distance = robot.distanceSensor.getDistance(DistanceUnit.INCH);
        //If less than 2ft, turn if not jeep going
        if (distance < 24) {
            turn(90);
        } else {
            robot.leftMotor.setPower(.75);
            robot.rightMotor.setPower(.75);
        }

    }

    //Using The IMU to turn 90^
    double getPower(double absCurrent, double absGoal, double absStart) {

        double relCurrent = AngleUnit.normalizeDegrees(absCurrent - absStart);
        double relGoal = AngleUnit.normalizeDegrees(absGoal-absStart);
        if (relCurrent < (relGoal) / 2) {

            return (.01 * relCurrent + (Math.signum(relCurrent) * .075));
        } else {
            return (.01 * (relGoal - relCurrent + (Math.signum(absCurrent) * .075)));
        }
    }

    public void turn(double relGoal) {

        ElapsedTime timer = new ElapsedTime();
        Double goalTime = null;

        double absStart = AngleUnit.normalizeDegrees(robot.getAngle());
        double absGoal = AngleUnit.normalizeDegrees(relGoal + robot.getAngle());

        while (goalTime == null || timer.time() - goalTime < 5){
            double absCurrent = robot.getAngle();
            if (Math.abs(absGoal - absCurrent) < 1) {
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0);

                if (goalTime == null) {

                    goalTime = timer.time();
                }
            } else {
                robot.leftMotor.setPower(-getPower(absCurrent, absGoal, absStart));
                robot.rightMotor.setPower(getPower(absCurrent, absGoal, absStart));

                goalTime = null;
            }
            telemetry.addData("Angle", absCurrent);
            telemetry.update();


        }

        telemetry.addData("I BROKE FREEE", "");
        telemetry.update();
    }}