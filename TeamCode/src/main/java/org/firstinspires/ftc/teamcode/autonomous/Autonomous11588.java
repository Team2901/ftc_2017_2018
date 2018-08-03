package org.firstinspires.ftc.teamcode.autonomous;
import android.graphics.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.*;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.robot.Hardware11588;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.Locale;
import org.firstinspires.ftc.teamcode.util.*;

@Autonomous(name="11588: Iterative", group="Autonomous")
public class Autonomous11588 extends OpMode {
    public static String VUFORIA_KEY = "AeRW4MT/////AAAAGU3Wbz2I0kTQr15Yq15bnjZ4YrOv67kvhALKU6MH0u3z0ZyRUtRYxew6uBX5fpNuw9kYDqpGQsf/nDdlqo6DQqAhsWt+gXRvJkAeOL3bQktk+qf8ZppR9l7d26VmwjcGTqFvHoB/zWAnbakjGE8TbXDTT85bE5HvR1Jy8pvstALmW4ikG1hDsQ1x+KQHPVMsEaR6iqKxQLDazzKI6+FJDtVurJw8TSbhGB6ojJkG6LJLiYIusbscWtMvxC9tBIioB7O7DxaGSTQP2EVqJF/FwfgqAaXOuxxC+1chTAwCnq0l2p/DJEU0U4guo5iMkyfmkwZDeZ/2RIFVAHFUycyf6n8IQymSW+Aej5v+86IDlx0n";

    public Hardware11588 robot = new Hardware11588();// new Hardware11588();
    public VuforiaLocalizer vuforia;
    public List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    public int state = -1;
    public final int DEAD_RECKONING_1   = 0,
                     RANGE_SENSOR_TURN_90 = 1,
                     VUFORIA_FOLLOW = 2;

    public final int FIRST_DEAD = 44 * (int)robot.WHEEL_DIAMETER_INCHES;
    public double previous = 255;
    public double INITIAL_LEFT, INITIAL_RIGHT;

    public void init() {
        robot.init(hardwareMap);

        /******************************************************************************************/
        /*                                      Vuforia Setup                                     */
        /******************************************************************************************/

        String TAG = "Vuforia 11588";
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.
                Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables ftcAssets = this.vuforia.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackable wheelsAsset = ftcAssets.get(0);
        wheelsAsset.setName("Wheels");
        VuforiaTrackable toolsAsset = ftcAssets.get(1);
        toolsAsset.setName("Tools");
        VuforiaTrackable legosAsset = ftcAssets.get(2);
        legosAsset.setName("Legos");
        VuforiaTrackable gearsAsset = ftcAssets.get(3);
        gearsAsset.setName("Gears");

        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(ftcAssets);

        float mmPerInch        = 25.4f;
        float mmBotWidth       = 5 * mmPerInch;
        float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels
        float mmBoxWidth       = mmFTCFieldWidth / 6;
        float mmPicWidth       = 11 * mmPerInch;

        // Gear location
        OpenGLMatrix gearLocation = OpenGLMatrix
                .translation(-mmFTCFieldWidth / 2, -(mmBoxWidth + mmPicWidth) / 2, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 90, 0));
        gearsAsset.setLocation(gearLocation);

        RobotLog.ii(TAG, "Gears=%s", format(gearLocation));

        // Tool location

        OpenGLMatrix toolsLocation = OpenGLMatrix
                .translation(-mmFTCFieldWidth / 2, mmBoxWidth * 1.5f - (mmPicWidth / 2), 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 90, 0));
        toolsAsset.setLocation(toolsLocation);

        RobotLog.ii(TAG, "Tools=%s", format(toolsLocation));

        // Legos location

        OpenGLMatrix legosLocation = OpenGLMatrix
                .translation(-mmBoxWidth * 1.5f - (mmPicWidth / 2), mmFTCFieldWidth / 2, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 0, 0));
        legosAsset.setLocation(legosLocation);

        RobotLog.ii(TAG, "Legos=%s", format(legosLocation));

        // Wheels location

        OpenGLMatrix wheelsLocation = OpenGLMatrix
                .translation((mmBoxWidth - mmPicWidth) / 2, mmFTCFieldWidth / 2, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 0, 0));
        wheelsAsset.setLocation(wheelsLocation);

        RobotLog.ii(TAG, "Wheels=%s", format(wheelsLocation));

        // Phone location on robot

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(0,0,0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.YZY,
                        AngleUnit.DEGREES, -90, 0, 0));
        RobotLog.ii(TAG, "phone=%s", format(phoneLocationOnRobot));

        ((VuforiaTrackableDefaultListener)wheelsAsset.getListener())
                .setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)legosAsset.getListener())
                .setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)toolsAsset.getListener())
                .setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)gearsAsset.getListener())
                .setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);

        ftcAssets.activate();
    }

    public void start() {
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        INITIAL_LEFT = robot.leftMotor.getCurrentPosition();
        INITIAL_RIGHT = robot.rightMotor.getCurrentPosition();
        state = 0;
    }

    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }

    public void updateTrackables() {
        for (VuforiaTrackable trackable : allTrackables) {
            OpenGLMatrix robotLocationTransform =
                    ((VuforiaTrackableDefaultListener)trackable.getListener())
                            .getUpdatedRobotLocation();

            if (robotLocationTransform != null) {
                // telemetry.addData(trackable.getName() + " Pos: ", format(robotLocationTransform));
            }
        }

    }

    public boolean seeAnything() {
        for (VuforiaTrackable trackable : allTrackables) {
             OpenGLMatrix robotLocationTransform =
                        ((VuforiaTrackableDefaultListener)trackable.getListener())
                                .getUpdatedRobotLocation();

             if (robotLocationTransform != null) {
                return true;
             }
        }

        return false;
    }

    public void loop() {
        switch (state) {
            case DEAD_RECKONING_1: {
                telemetry.addData("Left: ", robot.leftMotor.getCurrentPosition());
                telemetry.addData("Right: ", robot.rightMotor.getCurrentPosition());

                double dist = FIRST_DEAD * robot.ENCODER_COUNTS_PER_INCH;

                if (robot.leftMotor.getCurrentPosition() >= dist + INITIAL_LEFT ||
                    robot.rightMotor.getCurrentPosition() >= dist + INITIAL_RIGHT)
                {

                    state = RANGE_SENSOR_TURN_90;

                    robot.leftMotor.setPower(-0.2);
                    robot.rightMotor.setPower(0.2);
                    telemetry.addData("Now ", "Turning");
                } else {
                    robot.leftMotor.setPower(1);
                    robot.rightMotor.setPower(1);
                }

                /* robot.move(FIRST_DEAD * robot.ENCODER_COUNTS_PER_INCH,
                           FIRST_DEAD * robot.ENCODER_COUNTS_PER_INCH, 1, 1); */

                break;
            }
            case RANGE_SENSOR_TURN_90: {
                double dist = robot.rangeSensorFront.getDistance(DistanceUnit.INCH);

                telemetry.addData("Dist: ", dist);
                telemetry.addData("previous: ", previous);

                if (dist <= previous) {
                    previous = dist;
                } else if (dist - previous <= 10 && dist <= 40 && previous <= 40) {
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);

                    state = VUFORIA_FOLLOW;
                    telemetry.addData("Now trying to", " follow");
                }

                break;
                /* robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0);
                telemetry.addData("Dist: ", robot.rangeSensorFront.getDistance(DistanceUnit.INCH));
                break; */
            }
            case VUFORIA_FOLLOW: {
                for (VuforiaTrackable trackable : allTrackables) {
                    OpenGLMatrix robotLocationTransform =
                            ((VuforiaTrackableDefaultListener)trackable.getListener())
                                    .getUpdatedRobotLocation();

                    if (robotLocationTransform != null) {
                        telemetry.addData("I see ", "it");
                        // RobotLog.ii("Autonomous11588", "Position: ", format(robotLocationTransform));
                        double dist = robot.rangeSensorFront.getDistance(DistanceUnit.INCH);

                        if (dist <= 12) {
                            robot.leftMotor.setPower(0);
                            robot.rightMotor.setPower(0);
                            state++;

                            Beacon beacon = new Beacon(vuforia);
                            if (beacon.getTeam() == Team.BLUE) {
                                beacon.beep(100);
                            } else {
                                beacon.beep(500);
                            }

                            return;
                        }

                        Orientation o = Orientation.getOrientation(robotLocationTransform,
                                                                   AxesReference.EXTRINSIC,
                                                                   AxesOrder.XYZ,
                                                                   AngleUnit.DEGREES);

                        double angle = o.thirdAngle % 360;

                        telemetry.addData("Angle: ", angle);

                        if (Math.abs(angle) <= 2) {
                            robot.leftMotor.setPower(0.2);
                            robot.rightMotor.setPower(0.2);
                        } else {
                            if (angle > 0) {
                                robot.leftMotor.setPower(0.4);
                                robot.rightMotor.setPower(0);
                            } else {
                                robot.rightMotor.setPower(0.4);
                                robot.leftMotor.setPower(0);
                            }
                        }
                    }
                }

                break;
            }
        }

        telemetry.update();
    }
}
