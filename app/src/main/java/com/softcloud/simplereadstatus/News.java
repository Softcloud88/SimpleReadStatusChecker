package com.softcloud.simplereadstatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j-renzhexin on 2016/6/30.
 */
public class News {
    private String title;
    private String content;
    private boolean hasRead = false;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public static List<News> getMockedList() {
        List<News> mockedList = new ArrayList<>();
        int newsNum = 10;
        while (newsNum > 0) {
            News mockedNews = new News();
            mockedNews.title = "news mocked title " + (11 - newsNum--);
            mockedNews.content = "news mocked content";
            mockedList.add(mockedNews);
        }
        return mockedList;
    }
}
