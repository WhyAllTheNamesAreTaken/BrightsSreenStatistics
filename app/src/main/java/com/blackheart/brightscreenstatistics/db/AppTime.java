package com.blackheart.brightscreenstatistics.db;

import java.io.Serializable;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class AppTime extends LitePalSupport {
  private int id;
  private String appName;
  private long appStartTime;
  private long appStopTime;
  private long appRunTimeDuration;
  private String appDate;

  public String getAppDate() {
    return appDate;
  }

  public void setAppDate(String appDate) {
    this.appDate = appDate;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public long getAppStartTime() {
    return appStartTime;
  }

  public void setAppStartTime(long appStartTime) {
    this.appStartTime = appStartTime;
  }

  public long getAppStopTime() {
    return appStopTime;
  }

  public void setAppStopTime(long appStopTime) {
    this.appStopTime = appStopTime;
  }

  public long getAppRunTimeDuration() {
    return appRunTimeDuration;
  }

  public void setAppRunTimeDuration(long appRunTimeDuration) {
    this.appRunTimeDuration = appRunTimeDuration;
  }
}
