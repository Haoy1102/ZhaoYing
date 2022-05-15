package com.example.zhaoying_v13.ui.home.cameraX

import android.os.Environment
import java.io.File

/**
 * 常量
 */
object Constants {
    var FILE_PATH =
        Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM/Camera"
    val filePath: String
        get() {
            if (!File(FILE_PATH).exists()) {
                File(FILE_PATH).mkdirs()
            }
            return FILE_PATH
        }
}