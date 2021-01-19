package com.tn74travellers.aroDriver.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tn74travellers.aroDriver.BuildConfig;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Context context = LoginActivity.this;

    /***************XML Variables Declaration ************************/

    EditText et_mobile_num;
    EditText etPassword;

    Button btn_login;

    TextView tv_version_name;

    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    ProgressDialog progress_dialog;

    String mobile_no;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListeners();
    }

    private void viewInit()
    {
        etPassword      = findViewById(R.id.et_password);
        et_mobile_num   = findViewById(R.id.et_mobile_num);

        btn_login       = findViewById(R.id.btn_login);

        tv_version_name = findViewById(R.id.tv_version_name);
    }


    private void initialWorks()
    {
        progress_dialog = new ProgressDialog(context);

        //etPassword.setHint("Enter Password");
        //et_mobile_num.setHint("Enter Mobile Number");

        tv_version_name.setText(String.valueOf("Version " + BuildConfig.VERSION_NAME));

        if(commonFunctions.getSharedPrefBoolean(context, CommonVariables.SP_LOGIN_STATUS))
        {
            Log.e("Login Status:",commonFunctions.getSharedPrefBoolean(context,CommonVariables.SP_LOGIN_STATUS)+"");
            startActivity(new Intent(context, DashboardActivity.class));
        }
    }



    private void clickListeners()
    {
        btn_login.setOnClickListener(v -> {
            mobile_no   = et_mobile_num.getText().toString();
            pass        = etPassword.getText().toString();

            if(mobile_no.isEmpty())
            {
                Toast.makeText(context,CommonVariables.EM_FILL_MOBILE_NO,Toast.LENGTH_SHORT).show();
            }
            else if(mobile_no.length() < 10 || mobile_no.length() > 12 )
            {
                Toast.makeText(context,CommonVariables.EM_INVALID_MOBILE_NO,Toast.LENGTH_SHORT).show();
            }
            else if(pass.isEmpty())
            {
                Toast.makeText(context,CommonVariables.EM_FILL_PASSWORD,Toast.LENGTH_SHORT).show();
            }
            else if(pass.length() < 5)
            {
                Toast.makeText(context,CommonVariables.EM_INVALID_PASSWORD,Toast.LENGTH_SHORT).show();
            }
            else
            {
                hideKeyboard();
                callService();
            }
        });
    }



    /**> Calling Service Method If Network Is Available Or Show No Network Message <*/
    private void callService()
    {
        if (commonFunctions.checkNetConnectivity(context))
        {
            progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
            new AsyncTaskForHttpServiceLoginClass().execute(ServiceUrlVariables.LOGIN);
        }
        else
        {
            Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceLoginClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_LOGIN);

                jo_mobile_no_validate_request.put(CommonVariables.URL_MOBILE,mobile_no);
                jo_mobile_no_validate_request.put(CommonVariables.URL_PASSWORD,pass);
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DEVICE_TOKEN, commonFunctions.getSharedPrefString(context,CommonVariables.SP_DEVICE_TOKEN));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_MOBILE_TYPE,CommonVariables.URLRQ_ANDROID);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            String data = jo_mobile_no_validate_request.toString();

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
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceLoginClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result) || CommonVariables.SERVICE_RESPONSE_CODE_401.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        switch (jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG)) {
                            case CommonVariables.URLRQ_SUCCESS: {
                                JSONObject jo_response_data = jo_mobile_no_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);

                                commonFunctions.setSharedPrefString(context, CommonVariables.SP_DRIVER_ID, jo_response_data.getString("userId"));
                                commonFunctions.setSharedPrefString(context, CommonVariables.SP_TRAVELS_ID, String.valueOf(jo_response_data.getInt("travels_id")));
                                commonFunctions.setSharedPrefBoolean(context, CommonVariables.SP_LOGIN_STATUS, true);

                                startActivity(new Intent(context, DashboardActivity.class));

                                break;
                            }
                            case "Login Failed": {
                                Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();
                                break;
                            }
                            default: {
                                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                                break;
                            }
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



    private void hideKeyboard()
    {
        View view = getCurrentFocus();

        if (view != null)
        {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



    /**> Navigate When Overriding Back Pressed <*/
    @Override
    public void onBackPressed()
    {
        finishAffinity();
        finish();
    }
}
