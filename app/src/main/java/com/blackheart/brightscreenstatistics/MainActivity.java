package com.blackheart.brightscreenstatistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.blackheart.brightscreenstatistics.adapter.BrightScreenDateAdapter;
import com.blackheart.brightscreenstatistics.db.AppTime;
import com.blackheart.brightscreenstatistics.db.RecordTime;
import com.blackheart.brightscreenstatistics.service.AppTimeService;
import com.blackheart.brightscreenstatistics.service.ServiceKeepService;
import com.blackheart.brightscreenstatistics.util.ConvertTimeUtil;
import com.blackheart.brightscreenstatistics.util.PackageUsageStatsUtil;
import com.blackheart.brightscreenstatistics.util.ValueComparatorUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

  TextView tvTodayTime;
  RecyclerView recyclerDate;
  LinearLayoutManager linearLayoutManager;
  BrightScreenDateAdapter brightScreenDateAdapter;
  NavigationView navigationView;
  List<String> mDateList;                 //存放亮屏数据
  List<String> mTotalDurationList;       //存放每天亮屏时间统计数据
  private TitleBar mTitleBar;
  DrawerLayout drawer;
  public static final String TAG = "MainActivity";

  @Override
  protected void onStart() {
    super.onStart();
    getData();
    uiHandler.sendEmptyMessage(1);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.e(TAG, "onCreate");
    checkPermission();

    InitView();

    //TitleBar.initStyle(new TitleBarLightStyle(this));
    Intent serviceIntent1 = new Intent(this, AppTimeService.class);
    Intent serviceIntent2= new Intent(this, ServiceKeepService.class);
    if (Build.VERSION.SDK_INT >= 26) {//Android8.0
      this.startForegroundService(serviceIntent1);
      this.startForegroundService(serviceIntent2);
    } else {
      this.startService(serviceIntent1);
      this.startService(serviceIntent2);
    }


    uiHandler.sendEmptyMessage(0);
    leftNavigationView();
  }

  private void InitView() {
    tvTodayTime = findViewById(R.id.tv_today_time);
    recyclerDate = findViewById(R.id.date_recycler_view);
    mTitleBar = (TitleBar) findViewById(R.id.tb_main_title_bar);
    navigationView = findViewById(R.id.navigation_view);
    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
  }

  /**
   * ui处理写在这里
   */
  private Handler uiHandler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case 0:
          linearLayoutManager = new LinearLayoutManager(MainActivity.this);
          linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
          recyclerDate.setLayoutManager(linearLayoutManager);
          brightScreenDateAdapter =
              new BrightScreenDateAdapter(MainActivity.this, mDateList, mTotalDurationList);
          recyclerDate.setAdapter(brightScreenDateAdapter);

          //Item点击传值
          brightScreenDateAdapter.setOnItemClickListener(
              new BrightScreenDateAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                  Intent intent = new Intent(MainActivity.this, AppDurationActivity.class);
                  intent.putExtra("date", mDateList.get(position));
                  startActivity(intent);
                }
              });

          //title bar点击事件

          mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(View v) {
              drawer.openDrawer(navigationView);
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
              Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
              startActivity(intent);
            }
          });
          break;

        case 1:
          brightScreenDateAdapter.update(mDateList, mTotalDurationList);
        default:
          break;
      }
      return false;
    }
  });

  //private TreeMap getDate() {
  //
  //  TreeMap<String, Long> appTM = new TreeMap<>();
  //
  //  List<AppTime> appTimes = LitePal.findAll(AppTime.class);
  //
  //  if (!appTimes.isEmpty()) {
  //
  //    for (AppTime appTime1 : appTimes)
  //
  //      if (appTM.containsKey(ConvertTimeUtil.ms2YyyyMMdd(appTime1.getAppStartTime()))
  //          && appTM.get(ConvertTimeUtil.ms2YyyyMMdd(appTime1.getAppStartTime())) != null) {
  //        appTM.put(ConvertTimeUtil.ms2YyyyMMdd(appTime1.getAppStartTime()),
  //            appTM.get(ConvertTimeUtil.ms2YyyyMMdd(appTime1.getAppStartTime()))
  //                + appTime1.getAppRunTimeDuration());
  //      } else {
  //        appTM.put(ConvertTimeUtil.ms2YyyyMMdd(appTime1.getAppStartTime()),
  //            appTime1.getAppRunTimeDuration());
  //      }
  //  }
  //
  //  return appTM;
  //}


  private TreeMap getDate(){
    TreeMap<String, Long> dateTM = new TreeMap<>();
    List<RecordTime> recordTimes = LitePal.findAll(RecordTime.class);

    if(!recordTimes.isEmpty()){
      for(RecordTime recordTime:recordTimes){
        if(dateTM.containsKey(recordTime.getScreenOnDate())&&dateTM.get(recordTime.getScreenOnDate())!=null ){
          dateTM.put(recordTime.getScreenOnDate(),dateTM.get(recordTime.getScreenOnDate())+recordTime.getDuration());
        }else {
          dateTM.put(recordTime.getScreenOnDate(),recordTime.getDuration());
        }
      }
    }
    Log.e(TAG,dateTM.toString());
    return dateTM;
  }


  private void getData() {
    mDateList = new ArrayList<>();                 //存放亮屏数据
    mTotalDurationList = new ArrayList<>();       //存放每天亮屏时间统计数据
    //Log.e(TAG, mDateList.toString());
    Map<String, Long> map = getDate().descendingMap();
    for (TreeMap.Entry<String, Long> entry : map.entrySet()) {
      mDateList.add(entry.getKey());
      mTotalDurationList.add(ConvertTimeUtil.ms2HHmmssTimeZone0(entry.getValue()));
      if (ConvertTimeUtil.ms2YyyyMMdd(new Date().getTime()).equals(entry.getKey())) {
        tvTodayTime.setText(ConvertTimeUtil.ms2HHmmTimeZone0(entry.getValue()));
      }
    }
  }



  private void checkPermission() {
    PackageUsageStatsUtil packageUsageStatsUtil = new PackageUsageStatsUtil(MainActivity.this);
    if (packageUsageStatsUtil.isHasOption()) {
      if (!packageUsageStatsUtil.isHasSwitch()) {

        //提示框
        new AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("1.请将APP查看使用情况设置为开启\r\n"
                + "2.将APP设为开机启动")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(
                    Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
              }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
              }
            })
            .show();
      } else {

      }
    }
  }

  private void leftNavigationView() {

    /**
     * 初始化NavigationView
     */

    //View view = navigationView.getHeaderView(0);
    ////通过view的findViewById 获取具体某个空间
    //ImageView hearImg = view.findViewById(R.id.imageView);
    //hearImg.setOnClickListener(new View.OnClickListener() {
    //  @Override
    //  public void onClick(View v) {
    //    Toast.makeText(MainActivity.this, "点击头部 IMG", Toast.LENGTH_SHORT).show();
    //  }
    //});

    //NavigationView 选项选择
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.nav_about) {
              Toast.makeText(MainActivity.this, "暂无该页面", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_setting) {
              Toast.makeText(MainActivity.this, "暂无该页面", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_day_night_mode) {
              int mode =
                  getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
              if (mode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
              } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
              }
              recreate();
            }

            //自动隐藏

            drawer.closeDrawer(GravityCompat.START);

            return true;
          }
        });
  }
}

