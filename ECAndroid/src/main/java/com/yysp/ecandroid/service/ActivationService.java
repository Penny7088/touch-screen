package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ActivationService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:

                break;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
