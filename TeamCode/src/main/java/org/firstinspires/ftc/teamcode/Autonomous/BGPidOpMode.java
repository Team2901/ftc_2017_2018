package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Hardware.PidBotHardware;

@Autonomous(name="PidBotPracticeBG")
public class BGPidOpMode extends LinearOpMode{
    double targetAngle= 90;
   /* double GetPower (double targetAngle, double currentAngle) {
        if (currentAngle < targetAngle/2){
            //Finish next time//
        } else {

        }

    }
*/
    PidBotHardware pidBot= new PidBotHardware();
    @Override
    public void runOpMode() throws InterruptedException {
        pidBot.init(hardwareMap);
        waitForStart();
        while ( opModeIsActive()){
            double angle = pidBot.getAngle() ;
            if (Math.abs(90-angle)<10){
                pidBot.leftMotor.setPower(0);
                pidBot.rightMotor.setPower(0);

            } else {
                pidBot.leftMotor.setPower(-.5);
                pidBot.rightMotor.setPower(.5);


            }
            telemetry.addData("angle:",angle);
            telemetry.update();
        }
    }
    double getPower (double currentPostition){
        if (currentPostition< 45)
        {
            return .05 * currentPostition + Math.signum(currentPostition)*.01;
        }
        else
            {
            return .05 * (targetAngle - currentPostition) + Math.signum(targetAngle-currentPostition)*.01;
        }
    }
}
