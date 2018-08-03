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
 * Created by piscullin18641 on 2/9/2018.
 */

package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.HardwareDxm;


    @TeleOp(name = "DXM TeleOp_Nick_Control", group = "TeleOp")
     class DXM_TeleOp_Nick_Control extends OpMode {

        HardwareDxm robot = new HardwareDxm();

        final static double SERVO_SHIFT = 0.01;
        //final static double PINCER_MAX = 1;
        //final static double PINCER_MIN = .25;
        public boolean speed = false;
        public boolean ctrlMode = false;

        double liftUpSpeed = 1;
        double liftDownSpeed = .85;
        double leftY1;
        double leftX1;
        double rightX1;


        Servo.Direction bLeft;
        Servo.Direction bRight;
        Servo.Direction tLeft;
        Servo.Direction tRight;


        public void init() {
            robot.init(hardwareMap);
        }

        @Override
        public void loop() {
            //Mechanum Drive
            if (!speed) {
                leftY1 = Math.abs(gamepad1.left_stick_y) > 0.3 ? -gamepad1.left_stick_y : 0;
                leftX1 = Math.abs(gamepad1.left_stick_x) > 0.3 ? gamepad1.left_stick_x : 0;
                rightX1 = Math.abs(gamepad1.right_stick_x) > 0.3 ? gamepad1.right_stick_x : 0;
            } else {
                leftY1 = Math.abs(gamepad1.left_stick_y) > 0.3 ? -gamepad1.left_stick_y / 2 : 0;
                leftX1 = Math.abs(gamepad1.left_stick_x) > 0.3 ? gamepad1.left_stick_x / 2 : 0;
                rightX1 = Math.abs(gamepad1.right_stick_x) > 0.3 ? gamepad1.right_stick_x / 2 : 0;
            }
            double triggerR2 = gamepad2.right_trigger;
            double triggerL2 = gamepad2.left_trigger;

            //double leftY2 = Math.abs(gamepad2.left_stick_y) > 0.3 ? -gamepad2.left_stick_y : 0;

            boolean up = -gamepad2.left_stick_y > .4;
            boolean down = -gamepad2.left_stick_y < -.4;
            boolean bumperR2 = gamepad2.right_bumper;
            boolean bumperL2 = gamepad2.left_bumper;

            double[] wheelPower = wheelPower(leftX1, leftY1, rightX1);
            robot.fLeft.setPower(wheelPower[0]);
            robot.fRight.setPower(wheelPower[1]);
            robot.bLeft.setPower(wheelPower[2]);
            robot.bRight.setPower(wheelPower[3]);

//Speed modifier
            if (gamepad1.x) {
                speed = !speed;
            }
//Change the servo moving direction
            if ((gamepad2.a) && (gamepad2.y)) {
                //robot.openTop();
            } else if ((gamepad2.b) && (gamepad2.y)) {
                //robot.releaseTop();
            } else if ((gamepad2.x) && (gamepad2.y)) {
                //????????/
            } else if (gamepad2.a) {
                //robot.openBottom();
            } else if (gamepad2.b) {
                //robot.releaseBottom();
            } else if (gamepad2.x) {
                //??????????
            }

       /* if (gamepad2.dpad_down) {
            robot.openTop();

        } else if (gamepad2.dpad_left) {
            robot.releaseTop();
        }
        */

          /*  if (gamepad2.dpad_up) {

            } else if (gamepad2.dpad_down) {
                robot.lift.;
            }*/

            telemetry.addData("left joystick y-value: ", leftY1);
            telemetry.addData("left joystick x-value: ", leftX1);
            telemetry.addData("right joystick x-value: ", rightX1);
            telemetry.addData("front left motor: ", wheelPower[0]);
            telemetry.addData("front right motor: ", wheelPower[1]);
            telemetry.addData("back left motor: ", wheelPower[2]);
            telemetry.addData("back right motor: ", wheelPower[3]);

//Jewel knock device control in case the arm is thought to be helpful in the TeleOp Match, or it falls
            if (gamepad1.a) {
                robot.jewelKnockDevice.setPosition(1);
            } else if (gamepad1.b) {
                robot.jewelKnockDevice.setPosition(.25);
            }
//Arm Control
            if (up) {
                robot.lift.setPower(-liftUpSpeed);
            } else if (down) {
                robot.lift.setPower(liftDownSpeed);
            } else {
                robot.lift.setPower(0);
            }
//Opening and Closing of the servo pincers


            if (gamepad2.right_stick_button) {
                ctrlMode = !ctrlMode;
            }

            if (!ctrlMode) {
                if (triggerL2 > 0.2) {
                    robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() - SERVO_SHIFT);
                    robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() - SERVO_SHIFT);
                } else if (triggerR2 > .2) {
                    robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() + SERVO_SHIFT);
                    robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() + SERVO_SHIFT);
                }
                if (gamepad2.left_bumper) {
                    robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() - SERVO_SHIFT);
                    robot.tLeftPincer.setPosition(robot.bLeftPincer.getPosition() - SERVO_SHIFT);
                } else if (gamepad2.right_bumper) {
                    robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() + SERVO_SHIFT);
                    robot.tLeftPincer.setPosition(robot.bLeftPincer.getPosition() + SERVO_SHIFT);
                }
            } else {
                if ((triggerL2 > 0.2) && (gamepad2.y)) {
                    robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() - SERVO_SHIFT);
                } else if (triggerL2 > 0.2) {
                    robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() + SERVO_SHIFT);
                }
                if ((triggerR2 > 0.2) && (gamepad2.y)) {
                    robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() - SERVO_SHIFT);
                } else if (triggerR2 > 0.2) {
                    robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() + SERVO_SHIFT);
                }
                if ((gamepad2.left_bumper) && (gamepad2.y)) {
                    robot.tLeftPincer.setPosition(robot.bLeftPincer.getPosition() - SERVO_SHIFT);
                } else if (gamepad2.left_bumper) {
                    robot.tLeftPincer.setPosition(robot.bLeftPincer.getPosition() + SERVO_SHIFT);
                }
                if ((gamepad2.right_bumper) && (gamepad2.y)) {
                    robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() - SERVO_SHIFT);
                } else if (gamepad2.right_bumper) {
                    robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() + SERVO_SHIFT);
                }
            }

//

//Allow to switch direction of the servos for telemetry purposes

            bLeft = robot.bLeftPincer.getDirection();
            bRight = robot.bRightPincer.getDirection();
            tLeft = robot.tLeftPincer.getDirection();
            tRight = robot.tRightPincer.getDirection();

            telemetry.addData("bLeft Direction", bLeft);
            telemetry.addData("bRight Direction", bRight);
            telemetry.addData("tLeft Direction", tLeft);
            telemetry.addData("tRight Direction", tRight);

//**************************************************************************************************
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