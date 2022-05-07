package com.example.zhaoying_v13.ui.home.cameraX;

import android.os.Environment;

import java.io.File;

/**
 * 常量
 */
public class Constants {

    public static String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "DCIM/Camera";

    public static String getFilePath() {
        if (!new File(FILE_PATH).exists()) {
            new File(FILE_PATH).mkdirs();
        }
        return FILE_PATH;
    }
}
