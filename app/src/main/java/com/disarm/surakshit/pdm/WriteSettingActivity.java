package com.disarm.surakshit.pdm;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by disarm on 13/7/16.
 */
public class WriteSettingActivity extends AppCompatActivity {
    private int Allow_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasSelfPermission = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasSelfPermission = Settings.System.canWrite(this);
        }
        else{
            callMainActivity();
        }
        if (hasSelfPermission) {
            callMainActivity();
        } else {
            setContentView(R.layout.activity_writesetting);
            Button allow = (Button) findViewById(R.id.allowWriteSetting);
            allow.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    callWriteSettingIntent();
                }
            });

        }
    }

    private void callMainActivity(){
        Intent intent = new Intent(this, UI_Map.class);
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callWriteSettingIntent(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent,Allow_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Allow_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_CANCELED) {
                callMainActivity();
            }
        }
    }
}
