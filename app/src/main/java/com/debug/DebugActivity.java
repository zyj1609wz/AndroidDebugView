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

    private void toggle() {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        TextView textView;
        if ((textView = (TextView) viewGroup.findViewById(R.id.debug_view)) == null) {
            textView = new TextView(getApplicationContext());
            textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setTextColor(0xFF_FF_FF_FF);
            textView.setBackgroundColor(0xB3_00_00_00);
            textView.setTextSize(15);
            textView.setPadding(20, 20, 20, 20);
            textView.setId(R.id.debug_view);
            viewGroup.addView(textView);
            textView.setVisibility(View.GONE);
        }
        if (textView.getVisibility() == View.GONE) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("\n\n同时按下音量加减键，关闭调试信息");
            textView.append("\n");
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void showLog(String log) {
        if (findViewById(R.id.debug_view) != null) {
            TextView textView = (TextView) findViewById(R.id.debug_view);
            textView.append("\n");
            textView.append(logDataFormat.format(new Date()).toString() + " -> ");
            textView.append(log);

            if ((textView.getLineHeight() * textView.getLineCount()) > logViewHeight) {
                textView.scrollBy(0, textView.getLineHeight());
            }

        }
    }

    @Override
    protected void onDestroy() {
        DebugViewManager.get().unregister(call);
        super.onDestroy();
    }
}
