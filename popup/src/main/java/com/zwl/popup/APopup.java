package com.zwl.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author zwl
 * @describe PopupWindows
 * @date on 2021/5/12
 * @email 982242260@qq.com
 */
public class APopup {

    private Context context;
    private PopupWindow popupWindow;
    private View contentView;
    private int layoutId;
    private int animationStyle;
    private int width;
    private int height;
    private int offsetX;
    private int offsetY;
    private int triangleWidth;
    private int triangleHeight;
    private int triangleColor;
    private Drawable backgroundDrawable;
    private PopupWindow.OnDismissListener onDismissListener;
    private boolean outsideTouchable;
    private boolean touchable;
    private View target;
    private @APopupGravity int gravity;
    private String popupContent;
    private int maxWidth = 200;
    private APopupTriangleView triangleView;
    private OnPopupViewListener onPopupViewListener;
    int popupWidth ;
    int popupHeight;

    private APopup() {
    }

    public static Builder create(Context context) {
        return new Builder(context);
    }


    /**
     * 获取 contentView
     *
     * @return
     */
    private View getContentView() {
        if (contentView == null) {
            //自定义布局
            if (layoutId != -1) {
                contentView = LayoutInflater.from(context).inflate(layoutId, null);
            }
            //默认的
            else {
                contentView = LayoutInflater.from(context).inflate(R.layout._apopup_text_layout, null);
                TextView textView = contentView.findViewById(R.id.popup_default_text);
                textView.setText(TextUtils.isEmpty(popupContent) ? "" : popupContent);
                textView.setMaxWidth(APopupUtil.dp2px(context, maxWidth));
                textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            }
        }
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width=contentView.getMeasuredWidth();
        int height=contentView.getMeasuredHeight();
        return contentView;
    }


    /**
     * 获取Popup布局
     *
     * @return
     */
    private View getPopupLayout() {
        LinearLayout popupLayout = new LinearLayout(context);
        triangleView = new APopupTriangleView(context, Color.TRANSPARENT, gravity, triangleWidth, triangleHeight);
        if (gravity == APopupGravity.TOP_LEFT
                || gravity == APopupGravity.TOP_RIGHT
                || gravity == APopupGravity.TOP_CENTER) {
            popupLayout.setOrientation(LinearLayout.VERTICAL);
            popupLayout.addView(contentView);
            popupLayout.addView(triangleView);
        } else if (gravity == APopupGravity.BOTTOM_LEFT
                || gravity == APopupGravity.BOTTOM_CENTER
                || gravity == APopupGravity.BOTTOM_RIGHT) {
            popupLayout.setOrientation(LinearLayout.VERTICAL);
            popupLayout.addView(triangleView);
            popupLayout.addView(contentView);
        }
        popupLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int width=popupLayout.getMeasuredWidth();
        int height=popupLayout.getMeasuredHeight();
        if (onPopupViewListener != null) {
            onPopupViewListener.bindView(popupLayout, target);
        }
        return popupLayout;
    }

    /**
     * 显示Popup
     */
    public void show() {
        contentView = getContentView();
        View popupLayout = getPopupLayout();
        popupLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                autoAdjustTrianglePosition();
                popupLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        popupWindow.setContentView(popupLayout);
        popupWindow.setBackgroundDrawable(backgroundDrawable);
        popupWindow.setOutsideTouchable(outsideTouchable);
        popupWindow.setOnDismissListener(onDismissListener);
        popupWindow.setAnimationStyle(animationStyle);
        popupWindow.setTouchable(touchable);
        popupWindow.setWidth(width != 0 ? width : ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(height != 0 ? height : ViewGroup.LayoutParams.WRAP_CONTENT);

        autoAdjustPopupPosition();

    }

    private  void autoAdjustPopupPosition(){
        int[] locations = new int[2];
        target.getLocationOnScreen(locations);
        int left = locations[0];
        int top = locations[1];

        popupWindow.getContentView().measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int popupWidth = popupWindow.getContentView().getMeasuredWidth();
        int popupHeight = popupWindow.getContentView().getMeasuredHeight();

        int targetWidth = target.getWidth();
        int targetHeight = target.getHeight();


        Log.e("APopup", "left=" + left
                + "top=" + top
                + "popupWidth=" + popupWidth
                + " popupHeight=" + popupHeight
                + " targetWidth=" + targetWidth
                + " targetHeight=" + targetHeight
        );

        gravity = APopupUtil.checkGravity(gravity, target, contentView);
        switch (gravity) {
            case APopupGravity.TOP_LEFT:
                popupWindow.showAtLocation(target, Gravity.NO_GRAVITY, left + offsetX, top - popupHeight + offsetY);
                break;
            case APopupGravity.TOP_CENTER:
                int topCenterX = (targetWidth - popupWidth) / 2;
                popupWindow.showAtLocation(target, Gravity.NO_GRAVITY, left + topCenterX + offsetX, top - popupHeight + offsetY);
                break;
            case APopupGravity.TOP_RIGHT:
                popupWindow.showAtLocation(target, Gravity.NO_GRAVITY, left + targetWidth - popupWidth + offsetX, top - popupHeight + offsetY);
                break;
            case APopupGravity.BOTTOM_LEFT:
                popupWindow.showAsDropDown(target, offsetX, offsetY);
                break;
            case APopupGravity.BOTTOM_CENTER:
                int bottomCenterX = (targetWidth - popupWidth) / 2;
                popupWindow.showAsDropDown(target, bottomCenterX + offsetX, offsetY);
                break;
            case APopupGravity.BOTTOM_RIGHT:
                popupWindow.showAsDropDown(target, targetWidth - popupWidth + offsetX, offsetY);
                break;
        }
    }

    /**
     * 调整三角形位置
     */
    private void autoAdjustTrianglePosition() {
        int[] array = new int[2];
        contentView.getLocationOnScreen(array);
        int popupStartPosition = array[0];
        target.getLocationOnScreen(array);
        int anchorStartPosition = array[0];
        int marginStart = anchorStartPosition - popupStartPosition + target.getWidth() / 2 - triangleWidth / 2;
        LinearLayout.LayoutParams arrowParam = (LinearLayout.LayoutParams) triangleView.getLayoutParams();
        arrowParam.setMarginStart(marginStart);
        triangleView.setLayoutParams(arrowParam);
        triangleView.post(() -> triangleView.setColor(triangleColor));

    }


    /**
     * 关闭Popup
     */
    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


    public static class Builder {
        private final Context context;
        private final PopupWindow popupWindow;
        private View contentView;
        private int layoutId;
        private boolean outsideTouchable;
        private Drawable backgroundDrawable;
        private int animationStyle;
        private boolean touchable;
        private int width;
        private int height;
        private int offsetX;
        private int offsetY;
        private int triangleWidth;
        private int triangleHeight;
        private int triangleColor;
        private String popupContent;
        private int maxWidth;
        private View target;
        private @APopupGravity int gravity;
        private PopupWindow.OnDismissListener onDismissListener;
        private OnPopupViewListener onPopupViewListener;


        public Builder(Context context) {
            this.context = context;
            this.popupWindow = new PopupWindow(context);
            this.outsideTouchable = true;
            this.touchable = true;
            this.backgroundDrawable = new ColorDrawable(Color.TRANSPARENT);
            this.width = 0;
            this.height = 0;
            this.layoutId = -1;
            this.offsetX = 0;
            this.offsetY = 0;
            this.gravity = APopupGravity.BOTTOM_CENTER;
            this.triangleWidth = 48;
            this.triangleHeight = 30;
            this.triangleColor = Color.BLACK;
            this.maxWidth=200;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setBackgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setOutsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setOffsetX(int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public Builder setOffsetY(int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public Builder setGravity(@APopupGravity int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setAnimationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        public Builder setTouchable(boolean touchable) {
            this.touchable = touchable;
            return this;
        }

        public Builder setTarget(View target) {
            this.target = target;
            return this;
        }

        public Builder setTriangleWidth(int triangleWidth) {
            this.triangleWidth = triangleWidth;
            return this;
        }

        public Builder setTriangleHeight(int triangleHeight) {
            this.triangleHeight = triangleHeight;
            return this;
        }

        public Builder setTriangleColor(int triangleColor) {
            this.triangleColor = triangleColor;
            return this;
        }

        public Builder setPopupContent(String popupContent) {
            this.popupContent = popupContent;
            return this;
        }

        public Builder setOnPopupViewListener(OnPopupViewListener onPopupViewListener) {
            this.onPopupViewListener = onPopupViewListener;
            return this;
        }

        public APopup build() {
            APopup popupWindow = new APopup();
            setPopupWindowConfig(popupWindow);
            return popupWindow;
        }


        private void setPopupWindowConfig(APopup window) {
            if (context == null) {
                throw new RuntimeException("context can't be null");
            } else {
                window.context = this.context;
            }
            if (contentView != null && layoutId != -1) {
                throw new RuntimeException("contentView and layoutId can't be used together");
            }
            if (target == null) {
                throw new RuntimeException("target view can't be null");
            }
            window.width = this.width;
            window.height = this.height;
            window.contentView = this.contentView;
            window.layoutId = layoutId;
            window.popupWindow = this.popupWindow;
            window.outsideTouchable = this.outsideTouchable;
            window.backgroundDrawable = this.backgroundDrawable;
            window.onDismissListener = this.onDismissListener;
            window.animationStyle = this.animationStyle;
            window.touchable = this.touchable;
            window.offsetX = this.offsetX;
            window.offsetY = this.offsetY;
            window.target = this.target;
            window.gravity = this.gravity;
            window.triangleWidth = this.triangleWidth;
            window.triangleHeight = this.triangleHeight;
            window.triangleColor = this.triangleColor;
            window.popupContent = this.popupContent;
            window.onPopupViewListener = this.onPopupViewListener;
        }
    }


    public interface OnPopupViewListener {
        void bindView(View popupWindow, View target);
    }


}
