package com.softcloud.simplereadstatus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        initButtons();
    }

    private void initList() {
        ListView newsList = (ListView) findViewById(R.id.news_list);
        if (newsList == null) {
            return;
        }
        NewsAdapter adapter = new NewsAdapter(this);
        newsList.setAdapter(adapter);
        adapter.setData(News.getMockedList());
    }

    private void initButtons() {

    }
}

