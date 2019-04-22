package com.blackheart.brightscreenstatistics.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.blackheart.brightscreenstatistics.MainActivity;

public class NotificationClickReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO: This method is called when the BroadcastReceiver is receiving
    // an Intent broadcast.
    Intent newIntent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(newIntent);
  }
}
