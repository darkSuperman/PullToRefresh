package com.example.pulltorefresh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pulltorefresh.R;

import java.text.SimpleDateFormat;
import java.util.Date;

//TODO d 8.下拉刷新-确定继承关系
public class PullToRefreshListView extends ListView implements OnScrollListener {

	private int headerHeight;
	private TextView tvTitle;
	private TextView tvTime;
	private int downY;

	// 1.构造初始化 --> 2.测量onMeasure --> 3.布局 --> 绘制
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initHeader();

		initFooter();

		setOnScrollListener(this);
	}

	private void initFooter() {
		footerView = View.inflate(getContext(), R.layout.footer_view, null);
	}

	/**
	 * 初始化头部
	 */
	private void initHeader() {
		//TODO  9.下拉刷新-加载头部
		// 将参数2加载后 addView添加到参数3
		// 因为加入头部需要调用addHeaderView方法 所以 参数3需要传入null
		headerView = View.inflate(getContext(), R.layout.header_view, null);
		// 设置适配器之前添加头或脚是有效的 构造方法 肯定是在设置适配器之前调用
		addHeaderView(headerView);
        db = (ZDY) findViewById(R.id.db);
		tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
		tvTime = (TextView) headerView.findViewById(R.id.tv_time);
        fl = (FrameLayout) findViewById(R.id.fl);
        needle = (Needle) findViewById(R.id.needle);
		// 手动测量
		headerView.measure(0, 0);

		// getMeasuredHeight:获取测量的高度
		headerHeight = headerView.getMeasuredHeight();
		// Log.i("test", "headerHeight:"+headerHeight);

		finishLoading(true);
	}

	// 表示是否记录有效的downY 因为有效的downY只能被记录一次
	boolean isRecord;
	private View headerView;

	public static final int PULL_TO_REFRESH = 0;
	public static final int RELEASE_REFRESH = 1;
	public static final int REFRESHING = 2;

	private int state;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 当前显示的第一个可见条目为0时 表示在顶部 这时候记录的downy是有效的
			if (!isRecord && getFirstVisiblePosition() == 0) {
				downY = (int) ev.getY();
				isRecord = true; 
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// getFirstVisiblePosition: 完全显示的第一个条目索引
			// getLastVisiblePosition: 最后一个哪怕只是显示了一点点的条目索引
			// Log.i("test",
			// "getFirstVisiblePosition:"+getFirstVisiblePosition()+",getLastVisiblePosition:"+getLastVisiblePosition());
			// 如果用户不是从第0个条目开始下拉 那么需要在滑动的过程中 去记录有效的downY
			if (!isRecord && getFirstVisiblePosition() == 0) {
				downY = (int) ev.getY();
				isRecord = true;
			}

			int moveY = (int) ev.getY();

			int dy = moveY - downY;

			if (isRecord // 是否记录了有效的downY值
					&& getFirstVisiblePosition() == 0 // 判断是否在顶部
					&& dy > 0// 判断是否下拉 如果是下拉 我们自己处理 否则交给ListView处理
					&& state != REFRESHING) { // 在刷新中的状态时 不能再次让用户下拉
				// 1.在最顶部时
				// 2.往下拉
				//TODO  12.下拉刷新-将头部拉出
				int top = -headerHeight + dy / 3;
				headerView.setPadding(0, top, 0, 0);
				db.setProgress((int) (dy/1.8f));
				//TODO  13.下拉刷新-定义三种状态-改变状态
				// top >= 0: 表示头部完全出现了 就变为释放刷新
				if (top >= 0 && state == PULL_TO_REFRESH) {
					state = RELEASE_REFRESH;
					tvTitle.setText("释放刷新");
					//TODO  14.下拉刷新-箭头的旋转
				} else if (top < 0 && state == RELEASE_REFRESH) {
					state = PULL_TO_REFRESH;
					tvTitle.setText("下拉刷新");
				}

				// 自己处理事件
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			// 清空标记
			isRecord = false;

			//TODO 15.下拉刷新-抬起手的操作
			if (state == PULL_TO_REFRESH) {
				headerView.setPadding(0, -headerHeight, 0, 0);
			} else if (state == RELEASE_REFRESH) {
				// 改变状态为刷新中
				state = REFRESHING;
				// 更新标题信息
				tvTitle.setText("刷新中...");
				// 显示ProgressBar
				//隐藏头
				// 将头部完全显示
				headerView.setPadding(0, 0, 0, 0);
				//TODO  16.下拉刷新-结束加载
				db.setVisibility(View.GONE);
				fl.setVisibility(View.VISIBLE);
				needle.startScanning();
				if (listener != null) {
					listener.onRefresh();
				}
				
			}
			
			break;
		}
		return super.onTouchEvent(ev); // 不能自己处理 因为ListView本身已经处理了触摸事件
	}

	public interface OnRefreshListener {
		void onRefresh();

		void onLoadMore();
	}

	private OnRefreshListener listener;

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.listener = listener;
	}

	/**
	 * 箭头旋转的动画
	 * 
	 * @param isToReleaseRefresh
	 */

	/**
	 * 当调用该方法 表示数据加载完成
	 */
	public void finishLoading(boolean isRefresh) {// 初始化数据
		if (isRefresh) {
			//TODO  11.下拉刷新-初始化头部数据
			tvTitle.setText("下拉刷新");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			tvTime.setText("最后刷新时间:" + sdf.format(new Date()));
			state = PULL_TO_REFRESH;
			fl.setVisibility(View.GONE);
			db.setVisibility(View.VISIBLE);
			// 隐藏头部
			//TODO  10.下拉刷新-隐藏头部
			headerView.setPadding(0, -headerHeight, 0, 0);
		} else {
			//TODO  18.上拉加载-完成加载
			// 隐藏脚
			removeFooterView(footerView);
			// 重置状态
			isLoading = false;
		}
	}
	boolean isLoading;
	private View footerView;
	private ZDY db;
	private FrameLayout fl;
	private Needle needle;

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:

			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			//TODO  17.上拉加载-确定到底部添加脚
			if (!isLoading && getLastVisiblePosition() == getCount() - 1) {
				Log.i("test", "到底了!需要加载更多!");
				isLoading = true;

				addFooterView(footerView);

				// setSelection(getCount());
				smoothScrollToPosition(getCount());
												
				if (listener != null) {
					listener.onLoadMore();
				}
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

			break;
		}
	}

	// 太敏感 不好用
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

}
