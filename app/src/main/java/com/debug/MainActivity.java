package com.debug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.debug.util.DebugViewManager;

public class MainActivity extends DebugActivity implements android.view.View.OnClickListener {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv);
        textView.setOnClickListener(this);

        DebugViewManager.get().event("onCreate");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv:
                DebugViewManager.get().event("正在跳转");
                startActivity(new Intent(MainActivity.this, Activity2.class));
                DebugViewManager.get().event("跳转结束");
                break;
        }
    }
}
