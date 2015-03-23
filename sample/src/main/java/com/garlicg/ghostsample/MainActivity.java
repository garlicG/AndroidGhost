package com.garlicg.ghostsample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.garlicg.ghost.Ghost;

public class MainActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onShowGhostClick(View view){
        Ghost.makeText(this, "Ghost!", Toast.LENGTH_SHORT).show();
        // or
//        Ghost.show(this, "Ghost!");
//        Ghost.showLong(this, "Ghost!");
    }

    public void onShowToastClick(View view){
        Toast.makeText(this, "Toast!", Toast.LENGTH_SHORT).show();
    }

    public void onAppInfoClick(View view){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
