package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class LinearOpModeJewelCamera extends LinearOpModeCamera {
  protected JewelFinder jewel;

  public JewelFinder getJewel() {
    return jewel;
  }
}
