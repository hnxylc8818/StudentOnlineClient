package com.stuonline;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.stuonline.utils.SharedUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean isNight= SharedUtil.getModel(this);
        if (isNight){
            setTheme(R.style.night);
        } else {
            setTheme(R.style.def);
        }
    }
}
