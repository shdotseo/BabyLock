package io.animal.monkey.touch;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.monkey.R;
import io.animal.monkey.bus.events.KidModeEvent;
import io.animal.monkey.bus.events.TapServiceEvent;

public class TouchEventView extends ContextWrapper {

    private final static String TAG = "TouchEventView";

    private int miniTouchGestureHeight;

    private View touchView;

    private ImageView kidModeImage;
    private ImageView stopButton;

    public TouchEventView(Context c) {
        super(c);
        init();

        // register event bus
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        touchView = inflate.inflate(R.layout.screen_lock, null);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "setOnTouchListener:");
                return false;
            }
        });

//        stopButton = touchView.findViewById(R.id.stop_button);
//        stopButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(TAG, "setOnTouchListener:");
//                return false;
//            }
//        });
        kidModeImage = touchView.findViewById(R.id.kid_mode);
//        touchView = new View(getApplicationContext());
//        touchView.setBackgroundColor(Color.parseColor("#44FF1122"));
    }

//    public void updateMiniTouchGestureHeight(@Nullable Integer heightPx) {
//        miniTouchGestureHeight = heightPx;
//    }

    public void updateParamsForLocation(WindowManager windowManager, Boolean isPortrait) {
        WindowManager w = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        w.addView(touchView, createTouchViewParams());

//        if (isPortrait) {
//            miniTouchGestureHeight = SPFManager.getTouchviewPortraitHeight(getApplicationContext());
            //transparent color
//            windowManager.addView(touchView,
//                    createTouchViewParams(
//                            SPFManager.getTouchviewPortraitHeight(getApplicationContext()),
//                            SPFManager.getTouchviewPortraitWidth(getApplicationContext()),
//                            SPFManager.getTouchviewPortraitPosition(getApplicationContext())));
//        } else {
//            miniTouchGestureHeight = SPFManager.getTouchviewLandscapeHeight(getApplicationContext());
            //transparent color
//            windowManager.addView(touchView,
//                    createTouchViewParams(
//                            SPFManager.getTouchviewLandscapeHeight(getApplicationContext()),
//                            SPFManager.getTouchviewLandscapeWidth(getApplicationContext()),
//                            SPFManager.getTouchviewLandscapePosition(getApplicationContext())));
//        }
    }

    /**
     * Enable user' touch
     *
     * @return
     */
    private WindowManager.LayoutParams createTouchViewParams() {
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    ,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    ,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        return params;
    }

    /**
     * Disable User' Touch
     *
     * @return
     */
    private WindowManager.LayoutParams updateTouchViewParams() {
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                     WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                             | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        return params;
    }

    public View getTouchView() {
        return touchView;
    }

    /// ---------------------------------------------------------------------------- android service

    private void sendBroadcastCloseSystemDialog() {
        Intent i = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        getApplicationContext().sendBroadcast(i);
    }

    private WindowManager getWindowManager() {
        return (WindowManager) getSystemService(Service.WINDOW_SERVICE);
    }

    /// ------------------------------------------------------------------------ android service end

    /// -------------------------------------------------------------------------- EventBus Listener

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showKidMode(KidModeEvent e) {
        Log.d(TAG, "showKizMode:");
        kidModeImage.animate().alpha(1.0f).setDuration(150).start();
        kidModeImage.setAlpha(1.0f);
        kidModeImage.animate().alpha(0.0f).setStartDelay(150).setDuration(150).start();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTapServiceEvent(TapServiceEvent event) {
        Log.d(TAG, "onTapServiceEvent:" + event.toString());

        if (event.isEnable()) {
            // Service Started
            // Ignore user'touch event at service start
            getWindowManager().updateViewLayout(getTouchView(), updateTouchViewParams());

            sendBroadcastCloseSystemDialog();
        } else {
            // Service ended
            // Enable user'touch event at service end
            getWindowManager().updateViewLayout(getTouchView(), createTouchViewParams());
        }
    }

    /// ---------------------------------------------------------------------- EventBus Listener end
}
