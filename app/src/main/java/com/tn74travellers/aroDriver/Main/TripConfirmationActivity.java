package com.tn74travellers.aroDriver.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.R;

public class TripConfirmationActivity extends AppCompatActivity {

    Context context = TripConfirmationActivity.this;

    /***************XML Variables Declaration ************************/

    ImageView iv_back_arrow;

    /****************Normal Variables Declaration ************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_confirmation);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListenersfunc();
    }


    private void viewInit()
    {
            iv_back_arrow = findViewById(R.id.iv_back_arrow);

    }

    private void initialWorks()
    {

    }

    private void clickListenersfunc()
    {

        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,DashboardActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,DashboardActivity.class));
    }
}