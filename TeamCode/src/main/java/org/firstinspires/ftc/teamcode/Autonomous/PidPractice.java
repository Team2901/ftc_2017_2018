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




    double getPower(double currentPosition , double goalAngleFinal, double startAngleFinal) {

        double relativeCurrent =  AngleUnit.normalizeDegrees(currentPosition - startAngleFinal);
        if (relativeCurrent < (goalAngleFinal-startAngleFinal) / 2) {

            //Stopped Here fixing
            return (.01 * currentPosition + (Math.signum(currentPosition) * .075));
        } else {
            return (.01 * (goalAngleFinal - currentPosition + (Math.signum(currentPosition) * .075)));
        }
    }

    public void turn( double goalAngleFromCurrent ){

        ElapsedTime timer = new ElapsedTime();
        Double goalTime = null;

        double goalAngleFinal =  AngleUnit.normalizeDegrees(goalAngleFromCurrent + pidBot.getAngle());

        while (opModeIsActive() && (goalTime == null || timer.time() - goalTime < 5)) {
            double angle = pidBot.getAngle();
            if (Math.abs(goalAngleFinal - angle) < 1) {
                pidBot.leftMotor.setPower(0);
                pidBot.rightMotor.setPower(0);

                if (goalTime == null){

                    goalTime= timer.time();
                }
            } else {
               // pidBot.leftMotor.setPower(-getPower(angle, goalAngleFinal));
                //pidBot.rightMotor.setPower(getPower(angle , goalAngleFinal));

                goalTime = null;
            }
            telemetry.addData("Angle", angle);
            telemetry.update();


        }

        telemetry.addData("I BROKE FREEE", "");
        telemetry.update();
    }
}