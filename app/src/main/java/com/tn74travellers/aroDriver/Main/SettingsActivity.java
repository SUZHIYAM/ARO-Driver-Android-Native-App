package com.tn74travellers.aroDriver.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tn74travellers.aroDriver.BuildConfig;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {

    Context context = SettingsActivity.this;

    /***************XML Variables Declaration ************************/

    ImageView iv_back_arrow;

    LinearLayout ll_profile;
    LinearLayout ll_about_us;
    LinearLayout ll_terms_and_condition;
    LinearLayout ll_privacy_policy;
    LinearLayout ll_report_problem;
    LinearLayout ll_logout;

    private TextView tv_version_number;

    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    AlertDialog.Builder alert_dialog_builder;
    AlertDialog alert_dialog;

    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListenersfunc();
    }


    private void viewInit()
    {
        iv_back_arrow = findViewById(R.id.iv_back_arrow);

        ll_profile = findViewById(R.id.ll_profile);
        ll_about_us = findViewById(R.id.ll_about_us);
        ll_terms_and_condition = findViewById(R.id.ll_terms_and_condition);
        ll_privacy_policy = findViewById(R.id.ll_privacy_policy);
        ll_report_problem = findViewById(R.id.ll_report_problem);
        ll_logout = findViewById(R.id.ll_logout);

        tv_version_number = findViewById(R.id.tv_version_name);
    }

    private void initialWorks()
    {
        tv_version_number.setText(String.valueOf("Version " + BuildConfig.VERSION_NAME));
    }

    private void clickListenersfunc()
    {
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ProfileActivity.class));
            }
        });


        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,DashboardActivity.class));
            }
        });


        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateAlertDialogBuilder();
            }
        });
    }


    /**> Generate New Alert Dialog Builder <*/
    private void generateAlertDialogBuilder()
    {
        alert_dialog_builder = new AlertDialog.Builder(context);

        alert_dialog_builder.setTitle("Logout");
        alert_dialog_builder.setMessage("Sure to logout!");
        alert_dialog_builder.setCancelable(false);

        alert_dialog_builder.setPositiveButton(CommonVariables.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (commonFunctions.checkNetConnectivity(context))
                {

                    progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
                    new AsyncTaskForHttpServiceLogoutClass().execute(ServiceUrlVariables.LOGOUT);

                }
                else
                {
                    Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert_dialog_builder.setNegativeButton(CommonVariables.NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert_dialog.dismiss();

            }
        });

        alert_dialog = alert_dialog_builder.create();

        alert_dialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceLogoutClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_logout_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_LOGIN);

                jo_logout_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_logout_request.put(CommonVariables.URLRQ_MOBILE_TYPE,CommonVariables.URLRQ_ANDROID);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            String data = jo_logout_request.toString();

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
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceLogoutClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            JSONObject jo_response_data = jo_mobile_no_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);

                            if (jo_response_data.getString("logoutStatus").equals("true")) {
                                commonFunctions.setSharedPrefBoolean(context, CommonVariables.SP_LOGIN_STATUS, false);

                                Toast.makeText(context, CommonVariables.LOGOUT_SUCCESSFULLY, Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(context, LoginActivity.class));
                            } else {
                                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                            }
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
        startActivity(new Intent(context,DashboardActivity.class));
    }
}