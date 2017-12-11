package com.debug;

import android.os.Bundle;

import com.debug.util.DebugViewManager;

public class Activity2 extends DebugActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        DebugViewManager.get().event("Activity2");
    }
}
