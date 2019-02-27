package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RoverRuckusBotHardware extends BaseRRHardware {

    public static final double MARKER_INIT_POSITION = 0;
    public static final double MARKER_DROP_POSITION = 1;
    public static final double INCHES_PER_ROTATION = 9.35;

    public static final int THE_ROBOT_BROKE_SO_I_DONT_KNOW_THIS_AND_I_AM_MAD_ABOUT_IT = 1;

    public DcMotor elbow;
    public DcMotor shoulder;
    public CRServo intake;

    public RoverRuckusBotHardware() {
        super(MARKER_INIT_POSITION, MARKER_DROP_POSITION, INCHES_PER_ROTATION);
    }

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);

        intake = hardwareMap.crservo.get("intake");

        shoulder = hardwareMap.dcMotor.get("shoulder");
        shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elbow = hardwareMap.dcMotor.get("elbow");
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void armOut(LinearOpMode linearOpMode) {
        shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shoulder.setTargetPosition(6222);
        shoulder.setPower(.5);

        Integer lastPosition = null;
        boolean isNotStuck = true;

        while (shoulder.isBusy() && linearOpMode.opModeIsActive() && isNotStuck) {
            if (lastPosition != null && Math.abs(shoulder.getCurrentPosition()) >= 400) {
                isNotStuck = Math.abs(lastPosition - shoulder.getCurrentPosition()) > 5;
            }
            linearOpMode.telemetry.addData("lastPosition", lastPosition);
            lastPosition = shoulder.getCurrentPosition();
            linearOpMode.telemetry.addData("currentPosition", lastPosition);
            linearOpMode.telemetry.addData("isNotStuck", isNotStuck);
            linearOpMode.telemetry.update();
        }
        shoulder.setPower(0);
        shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

      /*
        if(isNotStuck) {
            elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elbow.setTargetPosition(THE_ROBOT_BROKE_SO_I_DONT_KNOW_THIS_AND_I_AM_MAD_ABOUT_IT);
            elbow.setPower(.5);
            lastPosition = null;
            while(elbow.isBusy()  && linearOpMode.opModeIsActive() && isNotStuck){
                if(lastPosition != null) {
                    isNotStuck = Math.abs(lastPosition - elbow.getCurrentPosition()) > 100;
                }
                lastPosition = elbow.getCurrentPosition();
            }

        }
        elbow.setPower(0);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        */
    }
}