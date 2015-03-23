package com.garlicg.ghost;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Ghost {

    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    final Context mContext;
    final GN mGN;
    int mDuration;
    View mNextView;

    public Ghost(Context context) {
        mContext = context;
        mGN = new GN();
        mGN.mY = context.getResources().getDimensionPixelSize(
                R.dimen.toast_y_offset);
        mGN.mGravity = context.getResources().getInteger(
                R.integer.config_toastDefaultGravity);
    }

    public static void show(Context context, CharSequence text) {
        makeText(context , text , LENGTH_SHORT).show();
    }
    public static void showLong(Context context, CharSequence text) {
        makeText(context , text , LENGTH_LONG).show();
    }

    public void show() {
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }

        GN gn = mGN;
        gn.mNextView = mNextView;
        gn.show(mDuration);
    }

    public void cancel() {
        mGN.hide();
    }

    public void setView(View view) {
        mNextView = view;
    }

    public View getView() {
        return mNextView;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        mGN.mHorizontalMargin = horizontalMargin;
        mGN.mVerticalMargin = verticalMargin;
    }

    public float getHorizontalMargin() {
        return mGN.mHorizontalMargin;
    }

    public float getVerticalMargin() {
        return mGN.mVerticalMargin;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        mGN.mGravity = gravity;
        mGN.mX = xOffset;
        mGN.mY = yOffset;
    }

    public int getGravity() {
        return mGN.mGravity;
    }

    public int getXOffset() {
        return mGN.mX;
    }

    public int getYOffset() {
        return mGN.mY;
    }

    public static Ghost makeText(Context context, CharSequence text, int duration) {
        Ghost result = new Ghost(context);

        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.transient_notification, null);
        TextView tv = (TextView)v.findViewById(android.R.id.message);
        tv.setText(text);

        result.mNextView = v;
        result.mDuration = duration;

        return result;
    }

    public static Ghost makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public void setText(int resId) {
        setText(mContext.getText(resId));
    }

    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(android.R.id.message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }


    private static class GN {
        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final Handler mHandler = new Handler();
        int mGravity;
        int mX, mY;
        float mHorizontalMargin;
        float mVerticalMargin;
        View mView;
        View mNextView;
        WindowManager mWM;

        final Runnable mShow = new Runnable() {
            @Override
            public void run() {
                handleShow();
            }
        };

        final Runnable mHide = new Runnable() {
            @Override
            public void run() {
                handleHide();
                mNextView = null;
            }
        };

        GN() {
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = android.R.style.Animation_Toast;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }

        void show(long duration) {
            mHandler.post(mShow);
            long delay = duration == Toast.LENGTH_LONG ? 3500 : 2000;
            mHandler.postDelayed(mHide , delay);
        }

        void hide() {
            mHandler.post(mHide);
        }

        void handleShow() {
            if (mView != mNextView) {
                handleHide();
                mView = mNextView;
                Context context = mView.getContext().getApplicationContext();
                if (context == null) {
                    context = mView.getContext();
                }
                mWM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                    final Configuration config = mView.getContext().getResources().getConfiguration();
                    mParams.gravity = Gravity.getAbsoluteGravity(mGravity, config.getLayoutDirection());
                }
                final int gravity = mParams.gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0f;
                }
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                mWM.addView(mView, mParams);
            }
        }


        public void handleHide() {
            if (mView != null) {
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                mView = null;
            }
        }
    }
}
