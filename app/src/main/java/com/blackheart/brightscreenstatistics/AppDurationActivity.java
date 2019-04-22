package com.blackheart.brightscreenstatistics;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.blackheart.brightscreenstatistics.adapter.AppTimeDurationAdapter;
import com.blackheart.brightscreenstatistics.db.AppTime;
import com.blackheart.brightscreenstatistics.util.ConvertTimeUtil;
import com.blackheart.brightscreenstatistics.util.GetAppInfoUtil;
import com.blackheart.brightscreenstatistics.util.ValueComparatorUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.litepal.LitePal;

public class AppDurationActivity extends AppCompatActivity {

  RecyclerView recyclerApp;

  AppTimeDurationAdapter appTimeDurationAdapter;
  LinearLayoutManager linearLayoutManager;
  private ArrayList<String> mAppNameList;
  private ArrayList<Long> mAppDurationTimeList;
  private ArrayList<Drawable> mAppIconList;
  private long totalTime = 0;
  private String mTrackDate;
  private TitleBar mTitleBar;
  public static final String TAG = "AppDurationActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_duration);

    recyclerApp = findViewById(R.id.app_recycler_view);

    //MainActivity获取传递的消息
    Intent intent = getIntent();
    mTrackDate = intent.getStringExtra("date");

    uiHandler.sendEmptyMessage(0);
  }

  @Override protected void onStart() {
    super.onStart();

    mAppNameList = new ArrayList<>();
    mAppDurationTimeList = new ArrayList<>();
    mAppIconList = new ArrayList<>();
    TreeMap<String, Long> treeMap = countTime(mTrackDate);

    for (TreeMap.Entry<String, Long> entry : treeMap.entrySet()) {

      mAppDurationTimeList.add(entry.getValue());

      mAppIconList.add(
          GetAppInfoUtil.getApplicationIconByPackageName(AppDurationActivity.this, entry.getKey()));

      mAppNameList.add(
          GetAppInfoUtil.getApplicationNameByPackageName(AppDurationActivity.this, entry.getKey()));

      totalTime += entry.getValue();
    }


  }

  private Handler uiHandler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case 0:
          linearLayoutManager = new LinearLayoutManager(AppDurationActivity.this);
          linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
          recyclerApp.setLayoutManager(linearLayoutManager);
          appTimeDurationAdapter =
              new AppTimeDurationAdapter(AppDurationActivity.this, mAppNameList,
                  mAppDurationTimeList, mAppIconList, totalTime);
          recyclerApp.setAdapter(appTimeDurationAdapter);

          //Item点击传值
          appTimeDurationAdapter.setOnItemClickListener(
              new AppTimeDurationAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
              });

          //title bar点击事件
          mTitleBar = (TitleBar) findViewById(R.id.tb_app_duration_tool_bar);
          mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(View v) {
              finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
          });
          break;
        case 1:
          appTimeDurationAdapter.update(mAppNameList, mAppDurationTimeList, mAppIconList,
              totalTime);
        default:
          break;
      }
      return false;
    }
  });

  /**
   * 统计APP使用时间
   *
   * @param date 查询日期
   * @return TreeMap<APP名称   ,   使用时间>时间降序排列
   */
  private TreeMap countTime(String date) {

    HashMap<String, Long> appHM = new HashMap<>();

    List<AppTime> appTimesList = LitePal.where("appDate=?", date).find(AppTime.class);

    if (!appTimesList.isEmpty()) {

      //提取所有当日使用的APP名字
      for (AppTime appTime1 : appTimesList) {

        if (appHM.containsKey(appTime1.getAppName())
            && appHM.get(appTime1.getAppName()) != null) {
          appHM.put(appTime1.getAppName(),
              appHM.get(appTime1.getAppName()) + appTime1.getAppRunTimeDuration());
        } else {
          appHM.put(appTime1.getAppName(), appTime1.getAppRunTimeDuration());
        }
      }
    }

    TreeMap<String, Long> appTM = new TreeMap<>(new ValueComparatorUtil(appHM));
    appTM.putAll(appHM);

    return appTM;
  }
}
