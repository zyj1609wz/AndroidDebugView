package com.debug.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiba_zyj on 2017/12/11.
 */

public class DebugViewManager {

    private static class SingletonHolder {
        public static DebugViewManager instance = new DebugViewManager();
    }

    private DebugViewManager() {
    }

    public static DebugViewManager get() {
        return SingletonHolder.instance;
    }

    private List<Call> callList = new ArrayList<>();

    public void register(Call call) {
        callList.add(call);
    }

    public void unregister(Call call) {
        callList.remove(call);
    }

    public void event(String event) {
        for (Call call : callList) {
            call.call(event);
        }
    }

    public interface Call {
        void call(String event);
    }
}
