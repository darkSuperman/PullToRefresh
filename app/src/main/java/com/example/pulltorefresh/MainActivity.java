package com.example.pulltorefresh;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.example.pulltorefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected static final int LOAD_NEW_DATA = 0;
    protected static final int LOAD_MORE_DATA = 1;
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LOAD_NEW_DATA:
                    String result = (String) msg.obj;
                    list.add(0, result);
                    adapter.notifyDataSetChanged();
                    plv.finishLoading(true);
                    break;
                case LOAD_MORE_DATA:
                    result = (String) msg.obj;
                    list.add(result);
                    adapter.notifyDataSetChanged();
                    plv.finishLoading(false);
                    break;
                default:
                    break;
            }
        };
    };
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private PullToRefreshListView plv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plv = (PullToRefreshListView) findViewById(R.id.plv);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData());
        plv.setAdapter(adapter);

        plv.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadNewData();
            }

            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    protected void loadMoreData() {
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = "我是加载更多的数据";
                handler.obtainMessage(LOAD_MORE_DATA, result).sendToTarget();
            };
        }.start();
    }

    protected void loadNewData() {
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = "我是新的数据";
                handler.obtainMessage(LOAD_NEW_DATA, result).sendToTarget();
            };
        }.start();
    }
    private List<String> getData() {
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("darkSuperman is Good!:"+i);
        }
        return list;
    }
}
