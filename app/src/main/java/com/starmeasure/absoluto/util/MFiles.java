package com.starmeasure.absoluto.util;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class MFiles {

    public final static String ftpDirSent = "/Enviados/";

    public static File getExternalReadsFromSMReader() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/SMReader/leituras");
    }

    public static File getExternalReadsFromMGEReader() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/MGEReader/");
    }

    private static File getBaseDir() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+"MGE");
        } else {
            return new File(Environment.getExternalStorageDirectory().toString()+"/"+"MGE");
        }
    }

    public static File getSentDir() {
        return new File(getBaseDir(), "ENVIADAS");
    }

    public static File getImgDir() {
        return new File(getBaseDir(), "IMAGENS");
    }

    public static File getRouteDir() {
        return new File(getBaseDir(), "ROTAS");
    }

    public static File getReadsDir() {
        return new File(getBaseDir(), "LEITURAS");
    }

    public static File getSealsDir() {
        return new File(getBaseDir(), "LACRES");
    }

    public static File getXmlDir() {
        return new File(getBaseDir(), "RETORNO");
    }

    public static File getSearchDir() {
        return new File(getBaseDir(), "PESQUISA");
    }

    public static File getLogDir() {
        return new File(getBaseDir(), "LOGS");
    }

    public static void createRoot() {
        String tag = "CreateRoot->";
        if (!getBaseDir().exists()) {
            Log.d(tag, "Base dir not exists");
            if (getBaseDir().mkdir()) {
                Log.d(tag, "Base dir created");
            } else {
                Log.d(tag, "Base dir fail to create");
            }
        } else {
            Log.d(tag, "Base dir exists");
        }

        if (!getSealsDir().exists()) {
            getSealsDir().mkdir();
        }

        if (!getSentDir().exists()) {
            getSentDir().mkdir();
        }

        if (!getImgDir().exists()) {
            getImgDir().mkdir();
        }

        if (!getRouteDir().exists()) {
            getRouteDir().mkdir();
        }

        if (!getReadsDir().exists()) {
            getReadsDir().mkdir();
        }

        if (!getXmlDir().exists()) {
            getXmlDir().mkdir();
        }

        if (!getSearchDir().exists()) {
            getSearchDir().mkdir();
        }

        if (!getLogDir().exists()) {
            getLogDir().mkdir();
        }
    }

}
