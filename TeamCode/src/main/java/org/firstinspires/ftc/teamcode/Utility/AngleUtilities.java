package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class AngleUtilities {
    /*
A problem we ran into using the IMU sensor was the fact that the angles were not as though they were on the unit circle
This function normalizes the angle so the robot starts at zero when the IMU is initialized and right is 360 and left is 0.
This allows only one problem spot to remain
*/
    public static int normalizeAngle(int angle) {
        return (angle + 360) % 360;
    }

    /*
     * This returns a normalized current angle
     */
    public static int getCurrentAngle(BNO055IMU imu) {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return normalizeAngle((int) angles.firstAngle);
    }

    //In order to allow the robot to move across the line that divides 360 &
    public static int getRelativePosition(int current, int goal, String direction, int start) {
        if (direction == "right" && goal > start && current > goal) {
            return Math.abs(360 - (current - start));
        } else if (direction == "right") {
            return Math.abs(current - start);
        } else if (direction == "left" && start > goal && current < goal) {
            return Math.abs(360 - (start - current));
        } else {
            return Math.abs(start - current);
        }
    }

}
