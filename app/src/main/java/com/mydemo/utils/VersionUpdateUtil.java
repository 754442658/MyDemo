package com.mydemo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.mydemo.R;
import com.mydemo.TApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


public class VersionUpdateUtil {

    private static NotificationManager manager;
    private static Notification notif;
    private static double downFileSize = 0.0;
    private static double fileLength = 0.0;

    private static AlertDialog dialog;
    private static Context context;

    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 2002) {
                // 更新通知栏
                double len = (Double) msg.obj;
                String pro = new DecimalFormat("#.00").format(len / fileLength);
                pro = pro.replace(".", "");
                if (pro.charAt(0) == '0') {
                    pro = pro.substring(1);
                }
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
                        new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);

                manager = (NotificationManager) context
                        .getSystemService(Activity.NOTIFICATION_SERVICE);

                notif = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(mPendingIntent)
                        .setTicker("新通知")
                        .setContentTitle(pro + "%")
                        .setProgress((int) fileLength, (int) len, false)
                        .setAutoCancel(true)
                        .build();

                //0是notif这条通知的id
                manager.notify(0, notif);
            } else if (msg.what == 2001) {
                manager.cancel(0);
                downFileSize = 0;
                fileLength = 0;
                // 安装下载好的apk文件
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile((File) msg.obj),
                        "application/vnd.android.package-archive");
                // 这里的EcmobileApp.context是application的context
                TApplication.context.startActivity(intent);
            }
        }
    };

    /**
     * 关闭对话框
     */
    public static void close() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 获取sd卡的路径
     */
    private static String getSdPath() {
        String sdPath = "-1";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            sdPath = "-1";
        } else {
            sdPath = Environment.getExternalStorageDirectory().toString();
        }
        return sdPath;
    }

    /**
     * 下载最新版本的apk
     *
     * @param url 下载链接
     * @param con
     */
    public static void ShowDownloadApkDialog(final String url, final Context con) {
        final String sdPah = Environment.getExternalStorageDirectory().toString() + "/MiTao";
        dialog = new AlertDialog.Builder(con).setTitle("提示")
                .setMessage("有新版本，是否更新？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (sdPah.equals("-1")) {
                            Toast.makeText(con, "未识别到SD卡，下载失败！", Toast.LENGTH_SHORT).show();
                            arg0.dismiss();
                            return;
                        }
                        int permission = ActivityCompat.checkSelfPermission(con, Manifest
                                .permission.WRITE_EXTERNAL_STORAGE);
                        int permission1 = ActivityCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permission != PackageManager.PERMISSION_GRANTED || permission1 !=
                                TApplication.context.getPackageManager().PERMISSION_GRANTED) {
                            T.showShort(con, "未获取存储权限");
                            arg0.dismiss();
                            return;
                        }
                        Toast.makeText(con, "正在下载...", Toast.LENGTH_SHORT).show();
                        StartDownApk(url, con, sdPah);
                        arg0.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                }).setCancelable(false).create();
        dialog.show();
    }

    /**
     * 开始下载
     *
     * @param con
     * @param sdPah
     */
    private static void StartDownApk(final String apdUrl, Context con,
                                     final String sdPah) {

        context = con;
        PendingIntent mPendingIntent = PendingIntent.getActivity(con, 0,
                new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);

        manager = (NotificationManager) con
                .getSystemService(Activity.NOTIFICATION_SERVICE);

        notif = new NotificationCompat.Builder(con)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(mPendingIntent)
                .setTicker("新通知")
                .setContentTitle("正在下载")
                .setAutoCancel(true)
                .build();
        manager.notify(0, notif);

        new Thread(new Runnable() {

            @Override
            public void run() {
                final File apkPath;
                URL url = null;
                HttpURLConnection urlConn = null;
                InputStream is = null;
                FileOutputStream out = null;
                File packagePath = new File(sdPah + "/Apk");
                if (!packagePath.exists()) {
                    packagePath.mkdirs();
                }
                try {
                    apkPath = new File(packagePath + "/视秀直播.apk");
                    if (!apkPath.exists()) {
                        apkPath.createNewFile();
                    } else {
                        apkPath.delete();
                    }
                    url = new URL(apdUrl);
                    urlConn = (HttpURLConnection) url.openConnection();
                    // 获取要下载的文件大小
                    fileLength = urlConn.getContentLength();
                    Log.e("tag", "" + fileLength);
                    is = urlConn.getInputStream();
                    out = new FileOutputStream(apkPath);

                    final Timer timer = new Timer();
                    // 立即执行，并且每隔1秒执行一次
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 2002;
                            msg.obj = downFileSize;
                            handler.sendMessage(msg);
                            Log.e("tag", "" + downFileSize);
                            if (downFileSize == fileLength) {
                                // 关闭
                                timer.cancel();
                                // 文件下载完毕
                                Message apkMsg = new Message();
                                apkMsg.what = 2001;
                                apkMsg.obj = apkPath;
                                // apkHandler.sendMessage(apkMsg);
                                handler.sendMessage(apkMsg);
                                Log.e("下载apk", "更新文件下载完成！");
                            }
                        }
                    }, 0, 1000);

                    int byteCount = 0;
                    byte[] data = new byte[2048];
                    while ((byteCount = is.read(data)) != -1) {
                        out.write(data, 0, byteCount);
                        downFileSize += byteCount;
                        // L.e("" + downFileSize);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                            out = null;
                        }
                        if (is != null) {
                            is.close();
                            is = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
