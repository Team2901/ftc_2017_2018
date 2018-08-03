package org.firstinspires.ftc.teamcode.util;
import android.graphics.Bitmap;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;

import android.graphics.Color;
import android.media.*;
import android.app.*;
import android.os.Environment;

public class Beacon extends Thread {
    public Bitmap bitmap;
    public VuforiaLocalizer vuforia;

    public Beacon(VuforiaLocalizer vuforia) {
        this.vuforia = vuforia;
        bitmap = takePicture();
    }

    public Bitmap takePicture() {
        Bitmap ret = null;

        try {
            BlockingQueue<VuforiaLocalizer.CloseableFrame> queue = vuforia.getFrameQueue();
            VuforiaLocalizer.CloseableFrame closeableFrame = queue.take();

            for (int i = 0; i < closeableFrame.getNumImages(); i++) {
                Image img = closeableFrame.getImage(i);
                int format = img.getFormat();
                if (img.getFormat() == PIXEL_FORMAT.RGB565) {
                    ret = Bitmap.createBitmap(img.getWidth(), img.getHeight(),
                            Bitmap.Config.RGB_565);
                    ret.copyPixelsFromBuffer(img.getPixels());
                    break;
                }
            }

            closeableFrame.close();
        } catch (InterruptedException e) {
            return null;
        }

        return ret;
    }

    public boolean savePicture(Bitmap bitmap) {
        try {
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.ENGLISH);
            String name = String.format("%s_picture.png", s.format(new Date()));
            FileOutputStream out = new FileOutputStream(name);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.close();

            beep(1000);

            return true;
        } catch (IOException e) {

            return false;
        }
    }

    public float getHue(float r, float g, float b) {
        r /= 255;
        g /= 255;
        b /= 255;

        float min = Math.min(Math.min(r, g), b);
        float max = Math.max(Math.max(r, g), b);

        float hue = 0;

        if (max == r) {
            hue = (g - b) / (max - min);
        } else if (max == g) {
            hue = (float)2 + (b - r) / (max - min);
        } else {
            hue = (float)4 + (r - g) / (max - min);
        }

        if ((hue *= 60) < 0) {
            hue += 360;
        }

        return hue % 360;
    }

    public Team getTeam() { // Gets the team on the left side of the beacon
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];

        try {
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Team.UNKNOWN;
        } catch (IllegalArgumentException e) {
            return Team.UNKNOWN;
        }

        Team team = Team.UNKNOWN;

        long bx, bc, rx, rc;
        bx = bc = rx = rc = 0;

        try {
           for (int i = 0; i < pixels.length; i++) {
                int pixel = pixels[i];
                float r = (pixel >> 16) & 0xFF;
                float g = (pixel >> 8) & 0xFF;
                float b = pixel & 0xFF;
                float hue = getHue(r, g, b);

                if (hue >= 350 || hue <= 10) {
                    rc++;
                    rx += i % width;
                } else if (hue >= 210 && hue < 250) {
                    bc++;
                    bx += i % width;
                } else {
                    pixels[i] = Color.rgb(0, 0, 0);
                }
            }
        } catch (Exception e) {
            return team;
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        float ba = bx / bc;
        float ra = rx / rc;

        return ba < ra ? Team.BLUE : Team.RED;
    }

    public static void beep(int ms) {
        ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        tone.startTone(ToneGenerator.TONE_DTMF_S, ms);
    }

    public void run() {
        bitmap = takePicture();
        if (getTeam() == Team.BLUE) {
            beep(1000);
        } else {
            beep(100);
        }

        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/saved_images");
        dir.mkdirs();

        String name = "image.jpg";
        File file = new File(dir, name);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {

        }
    }
}
