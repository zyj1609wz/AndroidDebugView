package com.debug;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debug.util.DebugViewManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yiba_zyj on 2017/12/8.
 */

public class DebugActivity extends BaseActivity {

    private int showEventInfoState;
    private static final SimpleDateFormat logDataFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private int logViewHeight;
    private DebugViewManager.Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        call = new DebugViewManager.Call() {
            @Override
            public void call(String event) {
                showLog(event);
            }
        };
        DebugViewManager.get().register(call);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        logViewHeight = (metric.heightPixels / 4) * 3;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getRepeatCount() == 0) {
                showEventInfoState++;
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (showEventInfoState == 2) {
                toggle();
            }
            if (event.getRepeatCount() == 0) {
                showEventInfoState = 0;
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    TextView debugView;

    private void toggle() {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);

        if (debugView == null) {
            debugView = new TextView(getApplicationContext());
            debugView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            debugView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            debugView.setTextColor(0xFF_FF_FF_FF);
            debugView.setBackgroundColor(0xB3_00_00_00);
            debugView.setTextSize(15);
            debugView.setPadding(20, 20, 20, 20);
            debugView.setId(R.id.debug_view);
            viewGroup.addView(debugView);
        } else {
            if (debugView.getVisibility() == View.GONE) {
                debugView.setVisibility(View.VISIBLE);
            } else {
                debugView.setVisibility(View.GONE);
            }
        }

        if (debugView.getVisibility() == View.VISIBLE) {
            debugView.setText("\n\n同时按下音量加减键，关闭调试信息");
            debugView.append("\n");
        }

    }

    private void showLog(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (debugView != null && debugView.getVisibility() == View.VISIBLE) {
                    debugView.append("\n");
                    debugView.append(logDataFormat.format(new Date()).toString() + " -> ");
                    debugView.append(log);

                    if ((debugView.getLineHeight() * debugView.getLineCount()) > logViewHeight) {
                        debugView.scrollBy(0, debugView.getLineHeight());
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        DebugViewManager.get().unregister(call);
        super.onDestroy();
    }
}
