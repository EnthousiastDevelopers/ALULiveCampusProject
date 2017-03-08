package com.tolotranet.livecampus.Head;
//mine

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Voice.Voice_DialogActivity;

public class Head_Service_Activity extends android.app.Service {

    private WindowManager windowManagerHeadBubble, windowManagerContentMain;
    private WindowManager.LayoutParams paramsHeadSticky;
    private WindowManager.LayoutParams paramsContentMain;
    private RelativeLayout dummyLineLeft, dummyLineTop;
    private WindowManager wm = null;
    private View headBubbleStickyIconView = null;
    private View mainContentView = null;
    private Button popupBtn;
    private ImageView imgViewheadBubbleStickyIcon, headBubbleMainContentView;
    private boolean hasCreatedMainContent = false;


    static Boolean FileExists;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
//        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Log.d("EXIT", "ondestroy!");

        if (imgViewheadBubbleStickyIcon != null) {
            windowManagerHeadBubble.removeView(headBubbleStickyIconView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        startService(new Intent(getApplication(), Head_Service_Activity.class));
        super.onCreate();

        final LayoutInflater rootInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        createPopupHeadBubbleSticker(rootInflater);


        //this code is for dragging the chat head
        imgViewheadBubbleStickyIcon.setOnTouchListener(new MyDragOnTouchListener());
        imgViewheadBubbleStickyIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("hello", "lonpressed bubble");
//                Intent dialogIntent = new Intent(Head_Service_Activity.this, Voice_DialogActivity.class);
//                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(dialogIntent);

                Intent i = new Intent();
                i.setClass(Head_Service_Activity.this, Voice_DialogActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(i);
                return true;
            }
        });

        //IMAGE HEAD STICKY ONLY ONCLICKLISTENER//
        imgViewheadBubbleStickyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello", "I can hear you Img outer");
                if (hasCreatedMainContent) {
                    headBubbleStickyIconView.setVisibility(View.GONE);
                    mainContentView.setVisibility(View.VISIBLE);

                } else {
                    Log.d("hello", "I can hear you Img outer");
                    hasCreatedMainContent = true; // to avoid the mainConentView to be added more than once to the rootInflater

                    //hide the bubble head icon just clicked first
                    headBubbleStickyIconView.setVisibility(View.GONE);
                    //show maincontent
                    createPopupMainContent(rootInflater);

                    //those 3 views make the maincontent view gone and the head popup view visible onClick
                    headBubbleMainContentView.setOnClickListener(new OnCloserMainContentClickListener());
                    dummyLineLeft.setOnClickListener(new OnCloserMainContentClickListener());
                    dummyLineTop.setOnClickListener(new OnCloserMainContentClickListener());


                    mainContentView.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            Log.d("hello", "any key is pressed");
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                Log.d("hello", "back pressed");
                                headBubbleMainContentView.performClick();
                                return true;
                            }
                            return false;
                        }
                    });
                    Log.d("hello", "after startapplication");
                }//else if not hasCreatedMainContent
            }//end onclick

        });
        //IMAGE HEAD STICKY ONLY ONCLICKLISTENER//

    }//end of OnCreate


    private void createPopupHeadBubbleSticker(LayoutInflater inflater) {

        windowManagerHeadBubble = (WindowManager) getSystemService(WINDOW_SERVICE); //the windowmanager of the draggable content head
        //parameters of the floating head bubble
        paramsHeadSticky = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsHeadSticky.gravity = Gravity.TOP | Gravity.LEFT;
        paramsHeadSticky.x = 500;
        paramsHeadSticky.y = 100;

        //layout of the headBubble
        headBubbleStickyIconView = inflater.inflate(R.layout.head_ic_head, null); //sticky icon head only
        //image inside the headBubbleLayout, use to drag and click
        imgViewheadBubbleStickyIcon = (ImageView) headBubbleStickyIconView.findViewById(R.id.imgViewheadBubbleStickyIcon);
        //setting up the headerbubble
        windowManagerHeadBubble.addView(headBubbleStickyIconView, paramsHeadSticky);

    }

    private void createPopupMainContent(LayoutInflater inflater) {
        //the windowmanager of the maincontent, not visible by default
        windowManagerContentMain = (WindowManager) getSystemService(WINDOW_SERVICE);
        //parameters of the main content
        paramsContentMain = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        paramsContentMain.gravity = Gravity.TOP | Gravity.RIGHT;
        paramsContentMain.x = 10;
        paramsContentMain.y = 10;
        //paramsContentMain.width = 400;
        //paramsContentMain.height = 700;
        paramsContentMain.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE;

        mainContentView = inflater.inflate(R.layout.head_content_main, null);
        headBubbleMainContentView = (ImageView) mainContentView.findViewById(R.id.imgViewheadBubbleStickyIcon);
        dummyLineTop = (RelativeLayout) mainContentView.findViewById(R.id.dummyLineLeft);
        dummyLineLeft = (RelativeLayout) mainContentView.findViewById(R.id.dummyLineTop);
        //setting up the maincontent
        windowManagerContentMain.addView(mainContentView, paramsContentMain);

    }

    private class MyDragOnTouchListener implements View.OnTouchListener {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Log.d("hello", "Touched sticky header");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = paramsHeadSticky.x;
                    initialY = paramsHeadSticky.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return false;
                case MotionEvent.ACTION_UP:
                    return false;
                case MotionEvent.ACTION_MOVE:
                    paramsHeadSticky.x = initialX
                            + (int) (event.getRawX() - initialTouchX);
                    paramsHeadSticky.y = initialY
                            + (int) (event.getRawY() - initialTouchY);
                    windowManagerHeadBubble.updateViewLayout(headBubbleStickyIconView, paramsHeadSticky);
                    return false;
            } // while(event.getAction()
            return true;
        }//on touch
    }

    ;


    private class OnCloserMainContentClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            headBubbleStickyIconView.setVisibility(View.VISIBLE);
            mainContentView.setVisibility(View.GONE);

        }
    }

    ;
}
