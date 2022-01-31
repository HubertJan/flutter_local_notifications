package com.dexterous.flutterlocalnotifications;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class PlayAudio extends Service {
  private static final String LOGCAT = null;
  private RingtoneManager ringtoneManager;
  private Ringtone ringtone;

  @Override
  public void onCreate() {
    Log.d("myTag", "Hell World");
    super.onCreate();
    this.ringtoneManager = new RingtoneManager(this);
    this.ringtoneManager.setStopPreviousRingtone(true);
  }

  private int setupService(Intent intent, int flags, int startId) {
    Notification notification;
    int id;
    Bundle extras = intent.getExtras();

    if (extras == null) {
      throw new Error("");
    } else {
      notification = (Notification) extras.get("notification");
      id = (int) extras.get("id");
    }

    this.startForeground(id, notification);

    this.ringtone =
        ringtoneManager.getRingtone(
            this, ringtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM));
    this.ringtone.setStreamType(AudioManager.STREAM_ALARM);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      this.ringtone.setLooping(true);
    }
    this.ringtone.play();
    return Service.START_NOT_STICKY;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent.getAction().equals("StartService")) {
      return setupService(intent, flags, startId);
    } else if (intent.getAction().equals("StopService")) {
      stopForeground(true);
      stopSelfResult(startId);
    }
    return START_STICKY;
  }

  public void onStop() {
    this.ringtone.stop();
  }

  public void onPause() {
    this.ringtone.stop();
  }

  public void onDestroy() {
    this.ringtone.stop();
  }

  @Override
  public IBinder onBind(Intent objIndent) {
    return null;
  }
}
