package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

@Autonomous (name = "VuNavTest")
public class VuNavTest extends LinearOpMode {

    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();
    //for never rest 40s
    public static final double ENCODER_COUNTS_PER_REV = 1120;
    //measured may not be exact
    public static final double INCHES_PER_ROTATION = 7.75;

    static final double INCHES_TO_ENCODERCOUNTS = ((1 / INCHES_PER_ROTATION) * ENCODER_COUNTS_PER_REV);
    VuforiaLocalizer vuforia;
    public final double MM_TO_INCHES = 0.0393701;
    public final double INCHES_TO_MM = 25.4;
    public final double FIELD_RADIUS = 1828.8;



    double x;
    double y;
    double z;
    float angleVu;

    double xGoal = 24;
    double yGoal = 48;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AQQpWjP/////AAABmWf3iVzlb0FUp3bUlTfyu04cg6nObJiyAcRVvdXnI9UGwJLT8PeUmQnawxjoZEpxQX4SACGC67Ix1pI2PTCBBrPOug9cDMLwL3g2TKSlKCfpMru3ooxbXaZ9ulWIc0rzWGCzLfmYN1mijxVwJPELqB2klhfU4FJMNGAZsHbkUJQqtCYhd5+psmXGukR9DUVFPFlAk/SJrpyCuLPZYgcqlOgqhvHH4PCFQqwHFpTKqnF/cgsNbrhiEpGhh6eWq2vvY+pP+/E8BxzM65XzIgKgUj2Uce6nYsD4oCTOpsmLywPxTExDflqSYtkfC+rLL8j601v3TsFI26x/UlE+YZg1UQkQo/eJI5aTEDL6ypVAmuZe";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);


        VuforiaTrackables roverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");


        waitForStart();

        roverRuckus.activate();
        OpenGLMatrix location = null;
        while (location == null) {
            location = VuforiaUtilities.getLocation(VuforiaUtilities.blue, VuforiaUtilities.red,
                    VuforiaUtilities.front, VuforiaUtilities.back);

            VectorF translation = location.getTranslation();

            Orientation orientation = Orientation.getOrientation(location,
                    AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

            x = (translation.get(0) * MM_TO_INCHES);
            y = (translation.get(1) * MM_TO_INCHES);
            z = (translation.get(2) * MM_TO_INCHES);
            angleVu = orientation.thirdAngle;

            telemetry.addData("X", "%.2f", x);
            telemetry.addData("Y", "%.2f", y);
            telemetry.addData("Z", "%.2f", z);
            telemetry.addData("Angle", "%.2f", angleVu);
            telemetry.update();


            idle();
        }


        goToPosition(x, y, xGoal, yGoal, angleVu);

        while (opModeIsActive()) {
            idle();
        }
    }


    double getPower(double currentPosition, double goal) {
       /*
        If under halfway to the goal, have the robot speed up by .01 for every angle until it is
        over halfway there
         */

        if (currentPosition < goal / 2) {

            return (.01 * currentPosition + (Math.signum(currentPosition) * .075));
        } else {
// Starts to slow down by .01 per angle closer to the goal.
            return (.01 * (goal - currentPosition + (Math.signum(currentPosition) * .075)));
        }
    }

    //Only use once per class and ALWAYS FOR VUFORIA
    public void goToPosition(double startX, double startY, double goalX, double goalY, double angleVu) {
        double angleImu = robot.getAngle();

        robot.offset = angleVu - angleImu;

        double xDiff = goalX - startX;
        double yDiff = goalY - startY;

        double angleGoal = Math.atan2(yDiff, xDiff) * (180 / Math.PI);

        angleImu = robot.getAngle();

        while (Math.abs(angleGoal - angleImu) > 1) {
            angleImu = robot.getAngle();

            robot.left.setPower(-getPower(angleImu, angleGoal));
            robot.right.setPower(getPower(angleImu, angleGoal));

            telemetry.addData("Goal Angle", angleGoal);
            telemetry.addData("angleGoal-angle ", angleGoal - angleImu);
            telemetry.addData("Power", getPower(angleImu, angleGoal));
            telemetry.update();
            idle();
        }

        robot.left.setPower(0);
        robot.right.setPower(0);

        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

        int encodersToGoal = (int) (INCHES_TO_ENCODERCOUNTS * distanceToGoal);

        telemetry.addData("distance to goal", distanceToGoal);
        telemetry.addData("encoders to goal", encodersToGoal);
        telemetry.update();

        robot.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.left.setTargetPosition(encodersToGoal);
        robot.right.setTargetPosition(encodersToGoal);

        robot.left.setPower(.75);
        robot.right.setPower(.75);

        while (robot.left.isBusy()) {
            telemetry.addData("left Counts", robot.left.getCurrentPosition());
            telemetry.addData("right Counts", robot.right.getCurrentPosition());
            telemetry.addData("distance to goal", distanceToGoal);
            telemetry.addData("encoders to goal", encodersToGoal);
            telemetry.update();

            idle();
        }

        robot.left.setPower(0);
        robot.right.setPower(0);

    }
}

