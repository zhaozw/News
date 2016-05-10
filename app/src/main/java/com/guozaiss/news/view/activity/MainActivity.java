package com.guozaiss.news.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guozaiss.news.BuildConfig;
import com.guozaiss.news.Constants;
import com.guozaiss.news.R;
import com.guozaiss.news.adapters.NewsAdapter;
import com.guozaiss.news.common.base.BaseActivity;
import com.guozaiss.news.common.utils.LogUtils;
import com.guozaiss.news.common.utils.ToastUtil;
import com.guozaiss.news.common.utils.http.DataUtils;
import com.guozaiss.news.entities.Data;
import com.guozaiss.news.entities.HotWord;
import com.guozaiss.news.view.customer.swipeLayout.SwipeRefreshLayout;
import com.guozaiss.news.view.customer.swipeLayout.SwipeRefreshLayoutDirection;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends BaseActivity implements Callback<Data>, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    List<Data.Result> result;
    private NewsAdapter newsAdapter;
    private List<String> hotwords;
    private ListView listView;
    private boolean refresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        newsAdapter = new NewsAdapter(this, result, R.layout.item_news);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(newsAdapter);
        DataUtils.getDataService().getHotWord(Constants.AppKey).enqueue(new Callback<HotWord>() {

            @Override
            public void onResponse(Response<HotWord> response, Retrofit retrofit) {
                hotwords = response.body().getResult();
                DataUtils.getDataService().getData(Constants.AppKey, hotwords.get(0)).enqueue(MainActivity.this);
                hotwords.remove(0);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        if (BuildConfig.debug) {
            ToastUtil.showToastOfLong("true");
        } else {
            ToastUtil.showToastOfLong("false");
        }
    }

    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if (hotwords.size() > 0) {
            if (direction == SwipeRefreshLayoutDirection.TOP) {
                refresh = true;
            } else if (direction == SwipeRefreshLayoutDirection.BOTTOM) {
                refresh = false;
            }
            DataUtils.getDataService().getData(Constants.AppKey, hotwords.get(0)).enqueue(this);
            hotwords.remove(0);
        }
    }


    @Override
    public void onResponse(Response<Data> response, Retrofit retrofit) {
        LogUtils.e("成功：" + response.body().toString());
        swipeRefreshLayout.setRefreshing(false);
        result = response.body().getResult();
        if (refresh) {
            newsAdapter.changeLists(result);
        } else {
            newsAdapter.addLists(result);
        }
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Throwable t) {
        LogUtils.e(t.getMessage() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Data.Result item = ((NewsAdapter) parent.getAdapter()).getItem(position);
        Intent intent = new Intent(this, HtmlActivity.class);
        intent.putExtra("url", item.getUrl());
        startActivity(intent);
    }
}
