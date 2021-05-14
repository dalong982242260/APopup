package com.zwl.popup;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

/**
 * @author zwl
 * @describe Popup工具类
 * @date on 2021/5/13
 * @email 982242260@qq.com
 */
public class APopupUtil {

    /**
     * 检查Popup显示越界问题
     *
     * @param gravity
     * @param targetView
     * @param contentView
     * @return
     */
    public static int checkGravity(int gravity, final View targetView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        targetView.getLocationOnScreen(anchorLoc);
        final int targetWidth = targetView.getWidth();
        final int targetHeight = targetView.getHeight();
        final int screenHeight = getScreenHeight(targetView.getContext());
        final int screenWidth = getScreenWidth(targetView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int popupHeight = contentView.getMeasuredHeight();
        final int popupWidth = contentView.getMeasuredWidth();
        final boolean isNeedShowYUp = (screenHeight - anchorLoc[1] - targetHeight < popupHeight);
        final boolean isNeedShowXRight = (screenWidth - anchorLoc[0] - targetWidth < popupWidth);
        final boolean isNeedShowXLeft = (screenWidth - anchorLoc[0] - targetWidth < popupWidth);


//        if(isNeedShowYUp){
//            return APopupGravity.TOP_RIGHT;
//        }
//        if (isNeedShowYUp && isNeedShowXRight) {
//            return APopupGravity.TOP_RIGHT;
//        }
//        //必须右
//        else if (isNeedShowXRight) {
//            if (gravity == APopupGravity.TOP_LEFT || gravity == APopupGravity.TOP_CENTER) {
//                return APopupGravity.TOP_RIGHT;
//            }
//            if (gravity == APopupGravity.BOTTOM_LEFT || gravity == APopupGravity.BOTTOM_CENTER) {
//                return APopupGravity.BOTTOM_RIGHT;
//            }
//        }
//
//        else if (isNeedShowYUp) {
//            if (gravity == APopupGravity.BOTTOM_LEFT) {
//                return APopupGravity.TOP_LEFT;
//            }
//            if (gravity == APopupGravity.BOTTOM_CENTER) {
//                return APopupGravity.TOP_CENTER;
//            }
//            if (gravity == APopupGravity.BOTTOM_RIGHT) {
//                return APopupGravity.TOP_RIGHT;
//            }
//        }
        return gravity;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * DP->PX
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }
}
