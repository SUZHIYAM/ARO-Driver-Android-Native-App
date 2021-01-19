package com.tn74travellers.aroDriver.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    Context context = ProfileActivity.this;

    /***************XML Variables Declaration ************************/

    ImageView iv_back_arrow;
    ImageView iv_profile_img;

    TextView tv_driver_name;
    TextView tv_driver_mobile;
    TextView tv_driver_sex;
    TextView tv_driver_city;
    TextView tv_experience;
    TextView tv_driver_age;

    RatingBar rb_driver_rating;

    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListenersfunc();
    }


    private void viewInit()
    {
        iv_back_arrow = findViewById(R.id.iv_back_arrow);
        iv_profile_img = findViewById(R.id.iv_profile_img);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_driver_mobile = findViewById(R.id.tv_driver_mobile);
        tv_driver_sex = findViewById(R.id.tv_driver_sex);
        tv_driver_city = findViewById(R.id.tv_driver_city);
        tv_experience = findViewById(R.id.tv_experience);
        tv_driver_age = findViewById(R.id.tv_driver_age);
        rb_driver_rating = findViewById(R.id.rb_driver_rating);
    }

    private void initialWorks()
    {
        if (commonFunctions.checkNetConnectivity(context))
        {
            progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
            new AsyncTaskForHttpServiceProfileClass().execute(ServiceUrlVariables.PROFILE);
        }
        else
        {
            Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }

    private void clickListenersfunc()
    {
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,SettingsActivity.class));
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceProfileClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_profile_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_LOGIN);

                jo_profile_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_profile_request.put(CommonVariables.URLRQ_MOBILE_TYPE,CommonVariables.URLRQ_ANDROID);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            String data = jo_profile_request.toString();

            assert data != null ;

            Log.e("data",data+"");

            return commonFunctions.send_http_request_func(params[0],
                    data,
                    CommonVariables.NO_HEADER,
                    CommonVariables.POST,
                    CommonVariables.JSON,
                    CommonVariables.BLANK_STRING,
                    jo_header.toString());

        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            commonFunctions.cancelLoadingProgress(progress_dialog);

            try
            {
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceProfileClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            JSONObject jo_response_data = jo_mobile_no_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);
                            tv_driver_name.setText(jo_response_data.getString("name"));
                            tv_driver_mobile.setText(jo_response_data.getString("mobile"));
                            tv_driver_sex.setText(jo_response_data.getString("gender"));
                            tv_driver_city.setText(jo_response_data.getString("address"));
                            tv_experience.setText(jo_response_data.getString("experience"));
                            tv_driver_age.setText(jo_response_data.getString("age"));
                            rb_driver_rating.setRating(Integer.parseInt(jo_response_data.getString("rating")));
                            Glide.with(context).load(jo_response_data.getString("smallProfileImg")).into(iv_profile_img);
                        } else {
                            Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,SettingsActivity.class));
    }
}