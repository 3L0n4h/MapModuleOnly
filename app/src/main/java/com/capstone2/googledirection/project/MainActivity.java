package com.capstone2.googledirection.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.capstone2.googledirection.project.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_waypoints).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_waypoints) {
            goToWaypointsDirection();
        }
    }

    /*public void goToSimpleDirection() {
        openActivity(SimpleDirectionActivity.class);
    }
    public void goToTransitDirection() {
        openActivity(TransitDirectionActivity.class);
    }
    public void goToAlternativeDirection() {
        openActivity(AlternativeDirectionActivity.class);
    }*/
    public void goToWaypointsDirection() {
        openActivity(WaypointsDirectionActivity.class);
    }
    public void openActivity(Class<?> cs) {
        startActivity(new Intent(this, cs));
    }
}
