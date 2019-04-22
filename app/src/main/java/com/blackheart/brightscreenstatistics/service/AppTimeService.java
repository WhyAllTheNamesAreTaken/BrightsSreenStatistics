package com.blackheart.brightscreenstatistics.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.blackheart.brightscreenstatistics.MainActivity;
import com.blackheart.brightscreenstatistics.R;
import com.blackheart.brightscreenstatistics.ScreenListener;
import com.blackheart.brightscreenstatistics.db.AppTime;
import com.blackheart.brightscreenstatistics.db.RecordTime;
import com.blackheart.brightscreenstatistics.receiver.NotificationClickReceiver;
import com.blackheart.brightscreenstatistics.util.ConvertTimeUtil;
import com.blackheart.brightscreenstatistics.util.RunningTaskUtil;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AppTimeService extends Service {

  //Timer timer;
  //TimerTask timerTask;
  RunningTaskUtil runningTaskUtil;
  private ScreenListener listener;

  private String mAppName = "";
  private boolean isStarted = false;
  private long appStartTime;
  private long appStopTime;
  private RecordTime recordTime;
  private long mScreenOnTime;
  private long mScreenOffTime;
  AppTime mAppTime;
  Handler handler = new Handler();
  public static final String TAG = "AppTimeService";
  private boolean isOnUserPresent;

  public AppTimeService() {
  }

  @Override public void onCreate() {
    super.onCreate();
    runningTaskUtil = new RunningTaskUtil(getApplicationContext());
    //timer = new Timer();
    listener = new ScreenListener(this);
    Log.e(TAG, "onCreate");
    mScreenOnTime = System.currentTimeMillis();
  }

  @Override public void onDestroy() {
    Log.e(TAG, "onDestroy");
    Log.e(TAG, listener.toString());
    try {
      listener.unregister();
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
    }

    handler.removeCallbacks(runnableListenApp);

    stopForeground(true);// 只是移除通知，没有停止服务的功能

    Intent intent = new Intent("Service Destroy");
    intent.setComponent(new ComponentName("com.blackheart.brightscreenstatistics",
        "com.blackheart.brightscreenstatistics.AppTimeReceiver"));
    sendBroadcast(intent);
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Log.e(TAG, "onStartCommand");
    setNotification();

    mAppName = runningTaskUtil.getTopRunningTasks().getPackageName();

    if (!isStarted) {
      appStartTime = System.currentTimeMillis();
      handler.postDelayed(runnableListenApp, 0);
      isStarted = true;
    }
    listener.register(new ScreenListener.ScreenStateListener() {
      @Override public void onScreenOn() {
        Log.e(TAG, "onScreenOn");
        mScreenOnTime = System.currentTimeMillis();
        appStartTime = System.currentTimeMillis();
      }

      @Override public void onScreenOff() {
        Log.e(TAG, "onScreenOff");
        handler.removeCallbacks(runnableListenApp);
        isStarted = false;
        mScreenOffTime = System.currentTimeMillis();



        //日期数据
        Log.e(TAG, "亮屏时间:" + (mScreenOffTime - mScreenOnTime));
        Log.e(TAG, mAppName);
        if (mScreenOffTime - mScreenOnTime > 1000 * 12 || isOnUserPresent) {
          Log.e(TAG, "亮屏时间大于12秒");
          recordTime = new RecordTime();
          recordTime.setScreenOnTime(mScreenOnTime);
          recordTime.setScreenOffTime(mScreenOffTime);
          recordTime.setScreenOnDate(ConvertTimeUtil.ms2YyyyMMdd(mScreenOnTime));
          recordTime.setDuration(mScreenOffTime - mScreenOnTime);
          recordTime.save();

          //获取锁屏时APP时间
          mAppTime = new AppTime();
          mAppTime.setAppName(mAppName);
          mAppTime.setAppDate(ConvertTimeUtil.ms2YyyyMMdd(appStartTime));
          mAppTime.setAppStartTime(appStartTime);
          mAppTime.setAppStopTime(mScreenOffTime);
          mAppTime.setAppRunTimeDuration(mScreenOffTime - appStartTime);
          Log.e(TAG, "AppRunTimeDuration " + (mScreenOffTime - appStartTime));
          mAppTime.save();
        }
        mScreenOnTime = System.currentTimeMillis();
        appStartTime = System.currentTimeMillis();
        isOnUserPresent = false;
      }

      @Override public void onUserPresent() {
        Log.e(TAG, "onUserPresent");
        mScreenOnTime = System.currentTimeMillis();
        appStartTime = System.currentTimeMillis();
        mAppName = runningTaskUtil.getTopRunningTasks().getPackageName();
        isOnUserPresent = true;
        if (!isStarted) {

          handler.postDelayed(runnableListenApp, 0);
          isStarted = true;
        }
      }
    });

    //return super.onStartCommand(intent, flags, startId);
    return Service.START_STICKY;
  }

  //private void startCheckImproved() {
  //
  //  mAppName = runningTaskUtil.getTopRunningTasks().getPackageName();
  //  appStartTime = System.currentTimeMillis();
  //
  //  if (timerTask != null) {
  //    timerTask.cancel();
  //  }
  //  timerTask = new TimerTask() {
  //    @Override
  //    public void run() {
  //      String tempName = runningTaskUtil.getTopRunningTasks().getPackageName();
  //      Log.e(TAG,mAppName);
  //      if (!mAppName.equals(tempName)) {
  //        appStopTime = System.currentTimeMillis();
  //
  //        mAppTime = new AppTime();
  //        mAppTime.setAppName(mAppName);
  //        mAppTime.setAppDate(ConvertTimeUtil.ms2YyyyMMdd(appStartTime));
  //        mAppTime.setAppStartTime(appStartTime);
  //        mAppTime.setAppStopTime(appStopTime);
  //        mAppTime.setAppRunTimeDuration(appStopTime - appStartTime);
  //        Log.e(TAG, "AppRunTimeDuration "+(appStopTime - appStartTime));
  //        mAppTime.save();
  //
  //        appStartTime = System.currentTimeMillis();
  //        mAppName = tempName;
  //      } else {
  //      }
  //    }
  //  };
  //}

  Runnable runnableListenApp = new Runnable() {
    @Override
    public void run() {
      // handler自带方法实现定时器
      try {
        // 在此处添加执行的代码
        //Log.e(TAG, "Runnable");
        String tempName = runningTaskUtil.getTopRunningTasks().getPackageName();
        Log.e(TAG, mAppName);
        if (!mAppName.equals(tempName)) {
          appStopTime = System.currentTimeMillis();

          mAppTime = new AppTime();
          mAppTime.setAppName(mAppName);
          mAppTime.setAppDate(ConvertTimeUtil.ms2YyyyMMdd(appStartTime));
          mAppTime.setAppStartTime(appStartTime);
          mAppTime.setAppStopTime(appStopTime);
          mAppTime.setAppRunTimeDuration(appStopTime - appStartTime);
          Log.e(TAG, "AppRunTimeDuration " + (appStopTime - appStartTime));
          mAppTime.save();

          appStartTime = System.currentTimeMillis();
          mAppName = tempName;
        } else {
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      handler.postDelayed(this, 1000);
    }
  };

  private void setNotification() {
    String id = "my_channel_01";
    String name = "我是渠道名字";
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification notification = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel mChannel =
          new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
      notificationManager.createNotificationChannel(mChannel);

      Intent intent = new Intent(this,MainActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

      notification = new Notification.Builder(this)
          .setChannelId(id)
          .setContentTitle("亮屏统计")
          .setContentText("为确保数据准确,需后台运行")
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentIntent(pendingIntent).build();



      startForeground(1, notification);
    } else {
      Intent intent = new Intent(this,MainActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

      NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
          .setContentTitle("亮屏统计")
          .setContentText("为确保数据准确,需后台运行")
          .setSmallIcon(R.mipmap.ic_launcher)
          .setOngoing(true)
          .setContentIntent(pendingIntent);
      //.setChannel(id);//无效

      notification = notificationBuilder.build();
    }
  }
}
