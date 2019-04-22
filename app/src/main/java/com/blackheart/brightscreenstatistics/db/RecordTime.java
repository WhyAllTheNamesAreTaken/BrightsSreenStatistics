package com.blackheart.brightscreenstatistics.db;

import org.litepal.crud.LitePalSupport;

public class RecordTime extends LitePalSupport {
  private int id;
  private long screenOnTime;
  private long screenOffTime;
  private long duration;
  private String screenOnDate;

  public String getScreenOnDate() {
    return screenOnDate;
  }

  public void setScreenOnDate(String screenOnDate) {
    this.screenOnDate = screenOnDate;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getScreenOnTime() {
    return screenOnTime;
  }

  public void setScreenOnTime(long screenOnTime) {
    this.screenOnTime = screenOnTime;
  }

  public long getScreenOffTime() {
    return screenOffTime;
  }

  public void setScreenOffTime(long screenOffTime) {
    this.screenOffTime = screenOffTime;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }
}
