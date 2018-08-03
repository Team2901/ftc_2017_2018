package org.firstinspires.ftc.teamcode.autonomous;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.util.List;

/**
 * Created by Programmer on 12/10/2016.
 */

@Autonomous(name="motion sensor", group="Autonomous")
public class MotionSensorTest extends LinearOpMode implements SensorEventListener {
    private SensorManager mSensorManager;

    float rotation_x, // azimuth - yaw - angle between original and current Y axis = x*sin(theta/2)
          rotation_y,
          rotation_z;

    //  reference sites on using phone sensors
    // https://developer.android.com/guide/topics/sensors/sensors_overview.html
    //
    // TYPE_ROTATION_VECTOR - Measures the orientation of a device by providing
    // the three elements of the device's rotation vector.
    // uses: Accelerometer, Magnetometer, and Gyroscope
    //
    // SENSOR_TYPE_GAME_ROTATION_VECTOR
    // uses: Accelerometer and Gyroscope (no Magnetometer)..
    //
    // main objects to interact with sensors
    //  - SensorManager
    public void runOpMode() {
        Context context = FtcRobotControllerActivity.appContext;

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE); // requires gyroscope and accelerometer

        if (mSensor != null)
        {
            telemetry.addData("Rotation Sensor",  "Present");
            int minDelay = mSensor.getMinDelay();
            telemetry.addData("MinDelay", "%d", minDelay);

            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            telemetry.addData("Rotation Sensor",  "Not Present");
            telemetry.addLine("==Sensors===");
            List<Sensor> allSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
            for(Sensor s: allSensors)
            {
                telemetry.addData("Sensor", s.getName());
            }
        }

        waitForStart();
        while(opModeIsActive()) {
/*
            if (mSensor != null) {
                telemetry.addData("X rot", "%f", rotation_x);
                telemetry.addData("Y rot", "%f", rotation_y);
                telemetry.addData("Z rot", "%f", rotation_z);

            }
            */
            telemetry.update();
        }
        if (mSensor != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        rotation_x = sensorEvent.values[0];
        rotation_y = sensorEvent.values[0];
        rotation_z = sensorEvent.values[0];
/*
        float vec[] = sensorEvent.values.clone();
        float quat[] = new float[4];
        SensorManager.getQuaternionFromVector(quat, vec);

        float vec[] = event.values.clone();
        float quat[] = new float[4];
        SensorManager.getQuaternionFromVector(quat, vec);
        SensorManager.getRotationMatrixFromVector(
                mRotationMatrix, sensorEvent.values);
        SensorManager
                .remapCoordinateSystem(mRotationMatrix,
                        SensorManager.AXIS_X, SensorManager.AXIS_Z,
                        mRotationMatrix2);

        SensorManager.getOrientation(mRotationMatrix2, mOrientationValues);
        float yaw = mOrientationValues[0];
*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}