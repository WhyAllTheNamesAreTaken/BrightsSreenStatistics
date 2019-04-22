package com.blackheart.brightscreenstatistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ScreenListener {
  private Context mContext;
  private ScreenBroadcastReceiver receiver;
  private ScreenStateListener mScreenStateListener;
  private boolean isStarted = false;

  public ScreenListener(Context context) {
    mContext = context;
    receiver = new ScreenBroadcastReceiver();
  }

  public void register(ScreenStateListener screenStateListener) {
    if (!isStarted) {
      if (screenStateListener != null) {
        mScreenStateListener = screenStateListener;
      }
      if (receiver != null) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(receiver, filter);
      }
      isStarted = true;
    }
  }

  public void unregister() {
    if (receiver != null) {
      mContext.unregisterReceiver(receiver);
    }
  }

  private class ScreenBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent != null) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
          if (mScreenStateListener != null) {
            Log.e("ScreenListener", "ScreenBroadcastReceiver --> ACTION_SCREEN_ON");
            mScreenStateListener.onScreenOn();
          }
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
          if (mScreenStateListener != null) {
            Log.e("ScreenListener", "ScreenBroadcastReceiver --> ACTION_SCREEN_OFF");
            mScreenStateListener.onScreenOff();
          }
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
          if (mScreenStateListener != null) {
            Log.e("ScreenListener", "ScreenBroadcastReceiver --> ACTION_USER_PRESENT");
            mScreenStateListener.onUserPresent();
          }
        }
      }
    }
  }

  public interface ScreenStateListener {
    void onScreenOn();

    void onScreenOff();

    void onUserPresent();
  }
}