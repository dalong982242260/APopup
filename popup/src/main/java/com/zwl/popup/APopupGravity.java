package com.zwl.popup;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        APopupGravity.TOP_LEFT,
        APopupGravity.TOP_CENTER,
        APopupGravity.TOP_RIGHT,
        APopupGravity.BOTTOM_LEFT,
        APopupGravity.BOTTOM_CENTER,
        APopupGravity.BOTTOM_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface APopupGravity {
    //上左
    int TOP_LEFT = 0;
    //上中
    int TOP_CENTER = 1;
    //上右
    int TOP_RIGHT = 2;

    //下左
    int BOTTOM_LEFT = 3;
    //下中
    int BOTTOM_CENTER = 4;
    //下右
    int BOTTOM_RIGHT = 5;

}