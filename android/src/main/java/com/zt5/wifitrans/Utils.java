package com.zt5.wifitrans;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    public static String getHostIp(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }
        //判断wifi是否连接
        if (wifiManager.getConnectionInfo() == null) {
            return null;
        }

        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

    /**
     * 复制单个文件
     *
     * @param source String 原文件路径 如：c:/fqf.txt
     * @param dest   String 复制后路径 如：f:/fqf.txt
     */
    public static void copyFile(File source, File dest) {
        if (!dest.exists()) {
            if (source.isDirectory()) {
                //noinspection ResultOfMethodCallIgnored
                dest.mkdirs();
            } else {
                //noinspection ResultOfMethodCallIgnored
                dest.getParentFile().mkdirs();
            }
        }
        FileInputStream input = null;
        FileOutputStream output = null;
        try {

            input = new FileInputStream(source);
            output = new FileOutputStream(dest);

            FileChannel inputChannel = input.getChannel();
            FileChannel outputChannel = output.getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (Exception e) {
            Utils.closeSilent(input);
            Utils.closeSilent(output);
            Utils.LOG.log(Level.WARNING, "复制失败", e);
        }
    }

    public static void closeSilent(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}