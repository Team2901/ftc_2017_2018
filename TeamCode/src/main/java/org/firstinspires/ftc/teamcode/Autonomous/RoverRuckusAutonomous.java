package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
@Autonomous(name = "RoverRuckusAutonomous")
public class RoverRuckusAutonomous extends LinearOpMode {
    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();
    public static final int TARGET_POSITION = 1;
    public String initialPosition = "depot";

    @Override
    public void runOpMode() throws InterruptedException {
        //step -2: initialize hardware
        robot.init(hardwareMap);
        // step -1: initialize vuforia

        //step 0: locate cheddar

        //step 1: drop down from lander
        dropFromLander();
        //step 2: do vuforia to determine position

        //step 3: go to the cheddar pivot point

        //step 4:turn to face depot point

        //step 5: gooooo

        //step 6: stop

        //step 7: do corner action(if depot: drop team marker/if crater: park)

    }

    public void initVuforia() {
        //TODO Bennett
    }

    public void locateGoldJewel() {
        //TODO Brigitte
    }

    public void dropFromLander() {
        DcMotor.RunMode originalValue = robot.lift.getMode();
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(TARGET_POSITION);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) {
            idle();
        }
        robot.lift.setMode(originalValue);
    }

    public void determinePosition() {
        //TODO Gillian
    }

    public void goToPivotPoint() {
        //TODO Gillian
    }

    public void faceDepotPoint() {
        //TODO Gillian
    }

    public void goToDepot() {
        //TODO Gillian
    }

    public void doCornerAction() {
        if (initialPosition.equals("depot")) {
            robot.marker.setPosition(1);
        } else if(initialPosition.equals("crater")){
            ;
        } else{
            robot.left.setPower(1);
            final double currentTime = time;
            double targetTime = currentTime + 5;
            while(targetTime < time){
                idle();
            }
            robot.left.setPower(0);
        }
    }
}
