package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.OpModeCamera;
import org.firstinspires.ftc.teamcode.Hardware.RRCoachBotHardware;

@TeleOp (name = "RRCoachBotTeleOp")
public class RRCoachBotTeleOp extends OpMode {

    RRCoachBotHardware robot = new RRCoachBotHardware();

    @Override
    public void init() {
     robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;

        leftPower(left);
        rightPower(right);

        telemetry.addData("rightBack", robot.rightBack.getCurrentPosition());
        telemetry.addData("rightFront", robot.rightFront.getCurrentPosition());
        telemetry.addData("leftBack" ,robot.leftBack.getCurrentPosition());
        telemetry.addData("leftFront" , robot.leftFront.getCurrentPosition());
        telemetry.update();
    }

    void leftPower (double power){
        robot.leftBack.setPower(power);
        robot.leftFront.setPower(power);
    }
    void rightPower (double power){
        robot.rightBack.setPower(power);
        robot.rightFront.setPower(power);
    }
}
