package com.softcloud.simplereadstatus;

import java.util.List;

/**
 * Created by Softcloud on 16/6/28.
 */
public interface ReadableManager<T> {
    String getContentMarker(T readable);

    void markRead(T readable);

    void markNotRead(T readable);

    void onCheckFinish(List<T> readables);
}
