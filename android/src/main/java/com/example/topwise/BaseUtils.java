package com.example.topwise;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class BaseUtils {
    private long oldTime = -1;
    public static final long DELAY_TIME = 200;

    synchronized boolean isNormalVelocityClick(long time) {
        long newTime = System.currentTimeMillis();
        if (oldTime == -1) {
            oldTime = newTime;
            return true;
        } else {
            if ((newTime - oldTime) <= time) {
                oldTime = newTime;
                return false;
            }
            oldTime = newTime;
        }
        return true;
    }

    public Bitmap getBmpFromAssets(String filename, Context context) {
        Bitmap mBitmap = null;
        AssetManager mAssetManager = context.getResources().getAssets();
        try {
            InputStream mInputStream = mAssetManager.open(filename);
            mBitmap = BitmapFactory.decodeStream(mInputStream);
            mInputStream.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            mBitmap = null;
        }
        return mBitmap;
    }

    public String getResString(int id,Context context) {
        return context.getResources().getString(id);
    }
}
