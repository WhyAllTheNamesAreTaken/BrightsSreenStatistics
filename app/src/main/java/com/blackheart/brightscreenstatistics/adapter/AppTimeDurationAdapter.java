package com.blackheart.brightscreenstatistics.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blackheart.brightscreenstatistics.R;
import com.blackheart.brightscreenstatistics.util.ConvertTimeUtil;
import java.util.List;

public class AppTimeDurationAdapter extends RecyclerView.Adapter<AppTimeDurationAdapter.MyHolder> {
  private Context context;
  private List<String> mAppNameList;
  private List<Long> mAppDurationList;
  private List<Drawable> mAppIconList;
  private long mTotalTime;
  private AppTimeDurationAdapter.OnItemClickListener onItemClickListener;

  //
  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }

  public void setOnItemClickListener(
      AppTimeDurationAdapter.OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public AppTimeDurationAdapter(Context context, List<String> appNameList,
      List<Long> appDurationList, List<Drawable> appIconList, Long totalTime) {
    this.context = context;
    this.mAppNameList = appNameList;
    this.mAppDurationList = appDurationList;
    this.mAppIconList = appIconList;
    this.mTotalTime = totalTime;
  }

  @NonNull
  @Override
  public AppTimeDurationAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
    AppTimeDurationAdapter.MyHolder myHolder = new AppTimeDurationAdapter.MyHolder(view);
    return myHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull AppTimeDurationAdapter.MyHolder holder,
      final int position) {

    String appName = mAppNameList.get(position);
    long appDuration = mAppDurationList.get(position);
    Drawable appIcon = mAppIconList.get(position);

    holder.tvAppName.setText(appName);
    holder.tvAppTime.setText(ConvertTimeUtil.ms2HHmmssTimeZone0(appDuration));
    holder.ivAppIcon.setImageDrawable(appIcon);


    //设置背景色
    holder.tvLeftScale.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT, appDuration));
    holder.tvRightScale.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT, mTotalTime - appDuration));
    Log.e("AppTimeDurationAdapter", "mTotalTime"+mTotalTime );
    Log.e("AppTimeDurationAdapter", "appDuration"+appDuration );
    //Log.e("AppTimeDurationAdapter",(mTotalTime-appDuration)+"");
    holder.llAppItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Toast.makeText(context, mAppDurationList.get(position), Toast.LENGTH_SHORT).show();

        onItemClickListener.onItemClick(v, position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return mAppNameList.size();
  }

  class MyHolder extends RecyclerView.ViewHolder {
    TextView tvAppName;
    TextView tvAppTime;
    View llAppItem;
    ImageView ivAppIcon;
    TextView tvLeftScale;
    TextView tvRightScale;

    MyHolder(View itemView) {
      super(itemView);
      tvAppName = itemView.findViewById(R.id.tv_app_name);
      tvAppTime = itemView.findViewById(R.id.tv_app_time);
      llAppItem = itemView.findViewById(R.id.ll_app_item);
      ivAppIcon = itemView.findViewById(R.id.iv_icon);
      tvLeftScale = itemView.findViewById(R.id.tv_left_scale);
      tvRightScale = itemView.findViewById(R.id.tv_right_scale);
    }
  }

  public void update(List<String> appNameList, List<Long> durationList,
      List<Drawable> appIconList, Long totalTime) {
    this.mAppNameList = appNameList;
    this.mAppDurationList = durationList;
    this.mAppIconList = appIconList;
    this.mTotalTime = totalTime;
    notifyDataSetChanged();
  }

  public void remove() {

  }

  public void removeAll() {
    this.mAppNameList.clear();
    this.mAppDurationList.clear();
    this.mAppIconList.clear();
    this.mTotalTime=0;
    notifyDataSetChanged();
  }
}
