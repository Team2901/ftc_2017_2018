package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Presentation.PresentationBotHardware;

@Autonomous(name = "PidPractice")
public class PidPractice extends LinearOpMode {

    PresentationBotHardware pidBot = new PresentationBotHardware();
    ElapsedTime timer = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        pidBot.init(hardwareMap);

        waitForStart();

        turn(90);

    }


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

        double absStart = AngleUnit.normalizeDegrees(pidBot.getAngle());
        double absGoal = AngleUnit.normalizeDegrees(relGoal + pidBot.getAngle());

        while (opModeIsActive() && (goalTime == null || timer.time() - goalTime < 5)) {
            double absCurrent = pidBot.getAngle();
            if (Math.abs(absGoal - absCurrent) < 1) {
                pidBot.leftMotor.setPower(0);
                pidBot.rightMotor.setPower(0);

                if (goalTime == null) {

                    goalTime = timer.time();
                }
            } else {
                pidBot.leftMotor.setPower(-getPower(absCurrent, absGoal, absStart));
                pidBot.rightMotor.setPower(getPower(absCurrent, absGoal, absStart));

                goalTime = null;
            }
            telemetry.addData("Angle", absCurrent);
            telemetry.update();


        }

        telemetry.addData("I BROKE FREEE", "");
        telemetry.update();
    }
}