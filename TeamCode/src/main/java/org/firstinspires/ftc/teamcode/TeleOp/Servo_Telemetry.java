/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Created by piscullin18641 on 1/30/2018.
 */

package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.HardwareDxm;


@TeleOp(name="Servo Test" , group = "TeleOp")
public class Servo_Telemetry extends OpMode {

    HardwareDxm robot = new HardwareDxm();

    final static double SERVO_SHIFT = 0.01;
    final static double PINCER_MAX = 1;
    final static double PINCER_MIN = .25;
    public boolean speed = false;

    double liftSpeed = 1;
    double leftY1;
    double leftX1;
    double rightX1;

    Servo.Direction bLeft;
    Servo.Direction bRight;
    Servo.Direction tLeft;
    Servo.Direction tRight;

    public void init() {
        robot.init(hardwareMap);

        robot.open();
    }

    @Override
    public void loop() {

        /*
         * Left trigger : increase servo position
         * Right trigger: decrease servo position
         * Left Bumper: swap servo direction

         */
        if (gamepad1.left_trigger > .3) {
            if (gamepad1.a) {
                robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() + .01);
            } else if (gamepad1.b) {
                robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() + .01);
            } else if (gamepad1.x) {
                robot.tLeftPincer.setPosition(robot.tLeftPincer.getPosition() + .01);
            } else if (gamepad1.y) {
                robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() + .01);
            }
        } else if (gamepad1.right_trigger > .3) {
            if (gamepad1.a) {
                robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() - .01);
            } else if (gamepad1.b) {
                robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() - .01);
            } else if (gamepad1.x) {
                robot.tLeftPincer.setPosition(robot.tLeftPincer.getPosition() - .01);
            } else if (gamepad1.y) {
                robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() - .01);
            }
        } else if (gamepad1.left_bumper) {
            bLeft = robot.bLeftPincer.getDirection();
            bRight = robot.bRightPincer.getDirection();
            tLeft = robot.tLeftPincer.getDirection();
            tRight = robot.tRightPincer.getDirection();

            if (gamepad1.a) {
                bLeft = (bLeft == Servo.Direction.FORWARD) ? Servo.Direction.REVERSE : Servo.Direction.FORWARD;
                robot.bLeftPincer.setDirection(bLeft);
            } else if (gamepad1.b) {
                bRight = (bRight == Servo.Direction.FORWARD) ? Servo.Direction.REVERSE : Servo.Direction.FORWARD;
            } else if (gamepad1.x) {
                tLeft = (tLeft == Servo.Direction.FORWARD) ? Servo.Direction.REVERSE : Servo.Direction.FORWARD;
            } else if (gamepad1.y) {
                tRight = (tRight == Servo.Direction.FORWARD) ? Servo.Direction.REVERSE : Servo.Direction.FORWARD;
            }
        }



        telemetry.addData("bLeft Pincer", robot.bLeftPincer.getPosition());
        telemetry.addData("bRight Pincer", robot.bRightPincer.getPosition());
        telemetry.addData("tLeft Pincer", robot.tLeftPincer.getPosition());
        telemetry.addData("tRight Pincer", robot.tRightPincer.getPosition());

        telemetry.addData("bLeft Direction", bLeft);
        telemetry.addData("bRight Direction", bRight);
        telemetry.addData("tLeft Direction", tLeft);
        telemetry.addData("tRight Direction", tRight);

        telemetry.update();
    }


    public double[] wheelPower(double x, double y, double r) {

        double speed = speed(x, y);
        double angle = angle(x, y);
        double pFL = (speed * (Math.sin((angle) + ((Math.PI) / 4)))) + r;
        double pFR = (speed * (Math.cos((angle) + ((Math.PI) / 4)))) - r;
        double pBL = (speed * (Math.cos((angle) + ((Math.PI) / 4)))) + r;
        double pBR = (speed * (Math.sin((angle) + ((Math.PI) / 4)))) - r;
        double[] wP = {pFL, pFR, pBL, pBR};
        telemetry.addData("speed: ", speed);
        telemetry.addData("angle: ", angle);

        return wP;
    }

    public double speed(double x, double y) {
        return Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
    }

    public double angle(double x, double y) {
        if ((y == 0) && (x == 0))
            return 0;
        else
            return Math.atan2(x, y);
    }

    public void setPincer(double x) {
        robot.bLeftPincer.setPosition(x);
        robot.bRightPincer.setPosition(x);
        robot.tLeftPincer.setPosition(x);
        robot.tRightPincer.setPosition(x);
    }
}