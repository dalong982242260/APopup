package com.zwl.apopup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.zwl.popup.APopup;
import com.zwl.popup.APopupGravity;
import com.zwl.popup.APopupUtil;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.img);
        findViewById(R.id.btn).setOnClickListener(v -> showPoppupindow(APopupGravity.BOTTOM_CENTER));
    }

    private void showPoppupindow(int gravity) {
        APopup.create(this)
                .setPopupContent("我是一个一个弹出框我是一个一个弹出框我是一个一个弹出框我是一个一个弹出框")
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT))
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e("APopup", "onDismiss");
                    }
                })
                .setOnPopupViewListener(new APopup.OnPopupViewListener() {
                    @Override
                    public void bindView(View popupWindow, View target) {
                        Log.e("APopup", "bindView");
                    }
                })
                .setTarget(imageView)
                .setGravity(gravity)
                .build()
                .show();
    }

}

