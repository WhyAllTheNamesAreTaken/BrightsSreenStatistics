package com.blackheart.brightscreenstatistics.service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.blackheart.brightscreenstatistics.MainActivity;
import com.blackheart.brightscreenstatistics.R;
import com.blackheart.brightscreenstatistics.ScreenListener;
import com.blackheart.brightscreenstatistics.db.RecordTime;
import com.blackheart.brightscreenstatistics.util.ConvertTimeUtil;
import java.util.Date;

public class ServiceKeepService extends Service {

  private ScreenListener listener;


  public ServiceKeepService() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    //setNotification();
    Log.e("ServiceKeepService","onStartCommand");


    //return super.onStartCommand(intent, flags, startId);
    return Service.START_STICKY;
  }

  @Override public void onCreate() {
    super.onCreate();


  }

  @Override public void onDestroy() {
    Log.e("ServiceKeepService", "AppTimeService onDestroy");
    stopForeground(true);
    Intent intent = new Intent("Service Destroy");
    intent.setComponent(new ComponentName("com.blackheart.brightscreenstatistics",
        "com.blackheart.brightscreenstatistics.AppTimeReceiver"));
    sendBroadcast(intent);


    super.onDestroy();
  }

  private void setNotification() {
    String id = "my_channel_01";
    String name = "我是渠道名字";
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification notification = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel mChannel =
          new NotificationChannel(id, name, NotificationManager.IMPORTANCE_MIN);
      //Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
      //Log.i(TAG, mChannel.toString());
      notificationManager.createNotificationChannel(mChannel);
      notification = new Notification.Builder(this)
          .setChannelId(id)
          .setContentTitle("亮屏统计")
          .setContentText("为确保数据准确,需后台运行")
          .setSmallIcon(R.mipmap.ic_launcher).build();
      startForeground(1, notification);
    }
    else {
      NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
          .setContentTitle("亮屏统计")
          .setContentText("为确保数据准确,需后台运行")
          .setSmallIcon(R.mipmap.ic_launcher)
          .setOngoing(true);
          //.setChannel(id);//无效
      notification = notificationBuilder.build();
    }

  }
}
