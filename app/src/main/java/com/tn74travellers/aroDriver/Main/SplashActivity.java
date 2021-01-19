package com.tn74travellers.aroDriver.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tn74travellers.aroDriver.BuildConfig;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity  extends AppCompatActivity
{
    Context context = SplashActivity.this;

    private static final String TAG = SplashActivity.class.getSimpleName();

    /**----------- XML view Declaration ------------*/

    private RelativeLayout rl_glass_whole_update_container;
    private RelativeLayout rl_loading_progress_bar;

    View toastView;
    View toastErrorView;

    /**----------- Normal Variables Declaration------------*/

    // private PushNotificationRetrieveRegId clobj_push_noti_reg_id;

    private int oldVersion;

    TextView tv_content;
    TextView tv_skip_update;
    TextView tv_update_ok;

    public static final CommonFunctions commonFunctions = new CommonFunctions();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        onClickListenerFunc();
    }



    private void viewInit()
    {
        rl_glass_whole_update_container = findViewById(R.id.rl_glass_whole_update_container);
        rl_loading_progress_bar = findViewById(R.id.rl_loading_progress_bar);

        tv_content = findViewById(R.id.tv_content);
        tv_skip_update = findViewById(R.id.tv_skip_update);
        tv_update_ok = findViewById(R.id.tv_update_ok);
    }



    private void initialWorks()
    {
        toastView = getLayoutInflater().inflate(R.layout.custom_view_toast, null);
        toastErrorView = getLayoutInflater().inflate(R.layout.custom_view_toast_error, null);

        rl_glass_whole_update_container.setVisibility(View.GONE);

        commonFunctions.setSharedPrefString(context,"from","");

        oldVersion = BuildConfig.VERSION_CODE;

        tv_content.setText(CommonVariables.NEW_APP_VERSION_DOWNLOAD_MSG);

        /**> Call Push Notification From Firebase Status <*/

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( SplashActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

                commonFunctions.setSharedPrefString(context,CommonVariables.SP_DEVICE_TOKEN,newToken);
            }
        });

        callService();
    }



    private void onClickListenerFunc()
    {
        tv_skip_update.setOnClickListener(v -> {
            startActivity(new Intent(context,LoginActivity.class));
            finish();
        });

        tv_update_ok.setOnClickListener(v ->
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())))
        );
    }



    /**> Calling Service Method If Network Is Available Or Show No Network Message <*/
    private void callService()
    {
        if (commonFunctions.checkNetConnectivity(context))
        {
            rl_loading_progress_bar.setVisibility(View.VISIBLE);

            new AsyncTaskForHttpServiceVersionNumberClass().execute(ServiceUrlVariables.VERSION_NUMBER);
        }
        else
        {
            Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceVersionNumberClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_header                        = new JSONObject();
            JSONObject jo_version_num_request    = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_VERSION_NUMBER);

                jo_version_num_request.put(CommonVariables.URLRQ_MOBILE_TYPE,CommonVariables.URLRQ_ANDROID);
                jo_version_num_request.put(CommonVariables.URLRQ_USER_TYPE,"driver");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            String data = jo_version_num_request.toString();

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

            rl_loading_progress_bar.setVisibility(View.GONE);

            try
            {
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceVersionNumberClass : "+result);

                if(result != null)
                {
                    JSONObject jo_version_num_response = new JSONObject(result);

                    if (CommonVariables.SERVICE_RESPONSE_CODE_200.equals(jo_version_num_response.getString(CommonVariables.URLRS_RESPONSE_CODE))) {
                        if (jo_version_num_response.getString(CommonVariables.URLRS_RESPONSE_MSG).equals("Success")) {
                            JSONObject jo_response_data = jo_version_num_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);

                            commonFunctions.setSharedPrefString(context, CommonVariables.SP_VERSION_NUMBER, String.valueOf(jo_response_data.getInt("versionCode")));

                            int currentVersion = jo_response_data.getInt("versionCode");

                            if (oldVersion < currentVersion)
                            {
                                rl_glass_whole_update_container.setVisibility(View.VISIBLE);
                                rl_glass_whole_update_container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.bottom_up_anim_1));
                            }
                            else
                            {
                                startActivity(new Intent(context,LoginActivity.class));
                                finish();
                            }
                        } else {
                            Log.e(TAG, "onPostExecute result is Error");

                            startActivity(new Intent(context,LoginActivity.class));
                            finish();
                        }
                    } else {
                        Log.e(TAG, "onPostExecute result is Error");

                        startActivity(new Intent(context,LoginActivity.class));
                        finish();
                    }
                }
                else
                {
                    Log.e(TAG, "onPostExecute result is null");

                    startActivity(new Intent(context,LoginActivity.class));
                    finish();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                Log.e(TAG, "onPostExecute Exception : "+e.getMessage());

                startActivity(new Intent(context,LoginActivity.class));
                finish();
            }
        }
    }



//    private void getCurrentVersion() {
//        PackageInfo pInfo = null;
//        try {
//            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e1) {
//            e1.printStackTrace();
//        }
//        this.currentVersion = pInfo.versionName;
//        Log.d("CurrentVersion", BuildConfig.FLAVOR + this.currentVersion);
//        tv_version_number.setText(currentVersion);
//        commonFunctions.set_shared_pref_str_func(context,CommonVariables.SP_VERSION_NUMBER,currentVersion);
//
//    }


    /**> Overriding onResume Method <*/
    @Override
    protected void onResume()
    {
        super.onResume();

        //clobj_push_noti_reg_id.whileResuming();
    }



    /**> Overriding onBackPressed Method <*/
    @Override
    public void onBackPressed()
    {
       // clobj_push_noti_reg_id.whileBackPressing();

        /**> Close App <*/

        finishAffinity();
        finish();
    }
}
