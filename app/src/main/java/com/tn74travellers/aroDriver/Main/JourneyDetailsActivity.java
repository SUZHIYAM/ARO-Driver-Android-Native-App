package com.tn74travellers.aroDriver.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.R;

import org.json.JSONException;
import org.json.JSONObject;

public class JourneyDetailsActivity extends AppCompatActivity {

    Context context = JourneyDetailsActivity.this;

    Activity activity = JourneyDetailsActivity.this;

    private static final String TAG = "JourneyDetailsActivity";

    /***************XML Variables Declaration ************************/

    ImageView iv_back_arrow;

    TextView tv_trip_type;
    TextView tv_start_date;
    TextView tv_start_time;
    TextView tv_end_date;
    TextView tv_end_time;
    TextView tv_customer_name;
    TextView tv_passenger_mobile_num;
    TextView tv_pickup_location;
    TextView tv_airport_location;
    TextView tv_hour_pack;
    TextView tv_pickup_location_label;
    TextView tv_map;
    TextView tv_trip_id;
    TextView tv_trip_status;
    TextView tv_expected_amount;
    TextView tv_advance_paid_amount;
    TextView tv_balance_amount;
    TextView tv_travelled_amount;
    TextView tv_travelled_balance_amount;
    TextView tv_distance_travelled;
    TextView tv_grand_total_amount;
    TextView tv_online_payment_amount;

    EditText et_halt;
    EditText et_halt_amount;
    EditText et_hill;
    EditText et_hill_amount;
    EditText et_batta;
    EditText et_batta_amount;
    EditText et_penalty_amount;
    EditText et_penalty_reason;
    EditText et_cash_payment_amount;

    View view_line_6;

    ConstraintLayout cl_airport_trip_location;
    ConstraintLayout cl_rental_pack;

    LinearLayout ll_location_container;
    LinearLayout ll_passenger_mobile_num;
    LinearLayout ll_finished_trip_status_block;
    LinearLayout ll_trip_details_container;

    RelativeLayout rl_pick_this_trip_container;

    ImageView iv_car_center;

    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    ProgressDialog progress_dialog;

    JSONObject jo_journey_details;

    String[] permissions_for =
            {
                    Manifest.permission.CALL_PHONE
            };

    AlertDialog.Builder alert_dialog_builder;
    AlertDialog alert_dialog;

    private static final int PERMISSION_REQUEST_CODE    = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListeners();

        getCurrentLocation();
    }


    private void viewInit()
    {
        iv_back_arrow = findViewById(R.id.iv_back_arrow);

        tv_trip_type = findViewById(R.id.tv_trip_type);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_date = findViewById(R.id.tv_end_date);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_passenger_mobile_num = findViewById(R.id.tv_passenger_mobile_num);
        tv_pickup_location = findViewById(R.id.tv_pickup_location);
        tv_airport_location = findViewById(R.id.tv_airport_location);
        tv_hour_pack = findViewById(R.id.tv_hour_pack);
        tv_pickup_location_label = findViewById(R.id.tv_pickup_location_label);
        tv_map = findViewById(R.id.tv_map);
        tv_trip_id = findViewById(R.id.tv_trip_id);
        tv_trip_status = findViewById(R.id.tv_trip_status);

        tv_expected_amount = findViewById(R.id.tv_expected_amount);
        tv_advance_paid_amount = findViewById(R.id.tv_advance_paid_amount);
        tv_balance_amount = findViewById(R.id.tv_balance_amount);
        tv_travelled_amount = findViewById(R.id.tv_travelled_amount);
        tv_travelled_balance_amount = findViewById(R.id.tv_travelled_balance_amount);
        tv_distance_travelled = findViewById(R.id.tv_distance_travelled);
        tv_grand_total_amount = findViewById(R.id.tv_grand_total_amount);

        view_line_6 = findViewById(R.id.view_line_6);

        cl_airport_trip_location = findViewById(R.id.cl_airport_trip_location);
        cl_rental_pack = findViewById(R.id.cl_rental_pack);
        ll_location_container = findViewById(R.id.ll_location_container);
        ll_passenger_mobile_num = findViewById(R.id.ll_passenger_mobile_num);
        ll_finished_trip_status_block = findViewById(R.id.ll_finished_trip_status_block);
        ll_trip_details_container = findViewById(R.id.ll_trip_details_container);

        rl_pick_this_trip_container = findViewById(R.id.rl_pick_this_trip_container);

        iv_car_center = findViewById(R.id.iv_car_center);

        et_halt = findViewById(R.id.et_halt);
        et_halt_amount = findViewById(R.id.et_halt_amount);
        et_hill = findViewById(R.id.et_hill);
        et_hill_amount = findViewById(R.id.et_hill_amount);
        et_batta = findViewById(R.id.et_batta);
        et_batta_amount = findViewById(R.id.et_batta_amount);
        et_penalty_amount = findViewById(R.id.et_penalty_amount);
        et_penalty_reason = findViewById(R.id.et_penalty_reason);
        tv_online_payment_amount = findViewById(R.id.tv_online_payment_amount);
        et_cash_payment_amount = findViewById(R.id.et_cash_payment_amount);
    }

    @SuppressLint("SetTextI18n")
    private void initialWorks()
    {
        try 
        {
            jo_journey_details = new JSONObject(commonFunctions.getSharedPrefString(context,"journey_details"));

            tv_trip_id.setText(jo_journey_details.getString("tripId"));
            tv_trip_type.setText(jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE));
            tv_start_date.setText(jo_journey_details.getString("startDate"));
            tv_start_time.setText(jo_journey_details.getString("startTime"));
            tv_end_date.setText(jo_journey_details.getString("endDate"));
            tv_end_time.setText(jo_journey_details.getString("endTime"));
            tv_customer_name.setText(jo_journey_details.getString("passengerName"));
            tv_pickup_location.setText(jo_journey_details.getString("passengerPickUpLocation"));

            tv_expected_amount.setText(jo_journey_details.getString("expAmount"));
            tv_advance_paid_amount.setText(jo_journey_details.getString("advance"));
            tv_balance_amount.setText(jo_journey_details.getString("balance"));
            tv_travelled_amount.setText(jo_journey_details.getString("travelledAmount"));
            tv_travelled_balance_amount.setText(jo_journey_details.getString("travelledBalance"));

            tv_grand_total_amount.setText(jo_journey_details.getString("travelledAmount"));

            tv_distance_travelled.setText(String.valueOf(jo_journey_details.getDouble("travelledKm") + " Km"));

            //passenger_pickup_lat[0] = Double.parseDouble(jo_journey_details.getString("pickupLocation_lat"));
            //passenger_pickup_lng[0] = Double.parseDouble(jo_journey_details.getString("pickupLocation_long"));

            Log.e(TAG,"jo_journey_details : "+jo_journey_details);

            Log.e("jo_journey_details : ", "tripStatus : "+jo_journey_details.getString("tripStatus"));

            JSONObject jo_payment_info = jo_journey_details.getJSONObject("paymentInfo");

            Log.e(TAG, "jo_payment_info : "+jo_payment_info);

            et_halt.setText(jo_payment_info.getString("halt"));
            et_halt_amount.setText(jo_payment_info.getString("halt_amount"));
            et_hill.setText(jo_payment_info.getString("hill"));
            et_hill_amount.setText(jo_payment_info.getString("hill_amount"));
            et_batta.setText(jo_payment_info.getString("batta"));
            et_batta_amount.setText(jo_payment_info.getString("batta_amount"));
            et_penalty_amount.setText(jo_payment_info.getString("penalty_amount"));
            et_penalty_reason.setText(jo_payment_info.getString("penalty_reason"));
            //tv_online_payment_amount.setText("0.00");
            //et_cash_payment_amount.setText("0.00");

            String trip_status = jo_journey_details.getString("tripStatus");
            
            switch (trip_status)
            {
                case CommonVariables.URLRQ_TRIP_ACCEPTED:
                {
                    tv_trip_status.setText("TRIP ACCEPTED");

                    ll_passenger_mobile_num.setVisibility(View.VISIBLE);
                    tv_passenger_mobile_num.setText(jo_journey_details.getString("passengerMobileNum"));

                    rl_pick_this_trip_container.setVisibility(View.VISIBLE);
                    ll_trip_details_container.setVisibility(View.GONE);
                    break;
                }
                case CommonVariables.URLRQ_TRIP_READY_TO_PICK:
                {
                    ll_passenger_mobile_num.setVisibility(View.GONE);
                    rl_pick_this_trip_container.setVisibility(View.GONE);
                    ll_trip_details_container.setVisibility(View.GONE);
                    break;
                }
                case CommonVariables.URLRQ_TRIP_COMPLETED:
                case CommonVariables.URLRQ_TRIP_CANCELLED:
                case CommonVariables.URLRQ_TRIP_PAYMENT_COMPLETED:
                {
                    ll_passenger_mobile_num.setVisibility(View.GONE);
                    rl_pick_this_trip_container.setVisibility(View.GONE);
                    ll_trip_details_container.setVisibility(View.VISIBLE);
                    break;
                }
            }

            String finished_trip_status = jo_journey_details.getString("finishedTripStatus");

            ll_finished_trip_status_block.setVisibility(View.GONE);

            switch (finished_trip_status)
            {
                case CommonVariables.URLRQ_TRIP_COMPLETED:
                {
                    tv_trip_status.setText("TRIP COMPLETED");
                    tv_trip_status.setTextColor(getResources().getColor(R.color.btn_green));
                    break;
                }
                case CommonVariables.URLRQ_TRIP_CANCELLED:
                {
                    tv_trip_status.setText("TRIP CANCELLED");
                    tv_trip_status.setTextColor(getResources().getColor(R.color.pure_red_990000));
                    break;
                }
                default:
                {
                    ll_finished_trip_status_block.setVisibility(View.GONE);
                    break;
                }
            }

            if(jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_AIRPORT_TRIP))
            {
                cl_airport_trip_location.setVisibility(View.VISIBLE);
                cl_rental_pack.setVisibility(View.GONE);
                ll_location_container.setVisibility(View.GONE);
                iv_car_center.setBackgroundResource(R.drawable.aeroplane);

                if(jo_journey_details.getJSONArray("locationList").length()>0)
                {
                    tv_airport_location.setText(jo_journey_details.getJSONArray("locationList").getString(0));
                }
            }

            if(jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_RENTAL_TRIP))
            {
                cl_rental_pack.setVisibility(View.VISIBLE);
                cl_airport_trip_location.setVisibility(View.GONE);
                ll_location_container.setVisibility(View.GONE);
                iv_car_center.setBackgroundResource(R.drawable.ic_car_yellow);

//                lat = new double[jo_journey_details.getJSONArray("locationList").length()];
//                lng = new double[jo_journey_details.getJSONArray("locationList").length()];

//                if(jo_journey_details.getJSONArray("hoursPack").length()>0)
//                {
//                    tv_hour_pack.setText(jo_journey_details.getJSONArray("hoursPack").getJSONObject(0).getString("packName"));
//                }

                tv_hour_pack.setText(String.valueOf(jo_journey_details.getInt("package")));
            }

            if(jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ONE_WAY_TRIP) ||
                    jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ROUND_TRIP))
            {
                cl_rental_pack.setVisibility(View.GONE);
                cl_airport_trip_location.setVisibility(View.GONE);
                ll_location_container.setVisibility(View.VISIBLE);
                iv_car_center.setBackgroundResource(R.drawable.ic_car_yellow);

                //MEMORY ALLOCATION FOR JAVA DOUBLE ARRAY
                //lat = new double[jo_journey_details.getJSONArray("locationList").length()];
                //lng = new double[jo_journey_details.getJSONArray("locationList").length()];

                if(jo_journey_details.getJSONArray("locationList").length()>0)
                {
                    for(int i=0;i<jo_journey_details.getJSONArray("locationList").length();i++)
                    {
                        int si = i+1;
                        TextView text = new TextView(context);
                        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        text.setText(si+"."+jo_journey_details.getJSONArray("locationList").getString(i));
                        text.setTextColor(getResources().getColor(R.color.colorPrimary));
                        text.setTextSize(16);
                        //or to support all versions use
                        Typeface typeface = ResourcesCompat.getFont(context, R.font.quicksand_medium);
                        text.setTypeface(typeface);

                        text.setTextColor(getResources().getColor(R.color.black));
                        text.setTextSize(16);
                        text.setPadding(5, 0, 5, 0);
                        ll_location_container.addView(text);
                    }

                }
            }
            
        } catch (JSONException e) {
            e.printStackTrace();

            Log.e(TAG, "initialWorks JSONException : "+e.getMessage());
        }
    }

    private void clickListeners()
    {
        iv_back_arrow.setOnClickListener(view -> onBackPressed());

        tv_map.setOnClickListener(view -> startActivity(new Intent(context,MapActivity.class)));

        rl_pick_this_trip_container.setOnClickListener(view -> {
            generateAlertDialogBuilder("Pick this trip", "Sure to pick this trip!");
        });

        ll_passenger_mobile_num.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission( context, permissions_for[0]) == PackageManager.PERMISSION_GRANTED) {
                callAction();
            }
            else {
                ActivityCompat.requestPermissions(activity, permissions_for, PERMISSION_REQUEST_CODE);
            }
        });
    }



    private void callAction()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + tv_passenger_mobile_num.getText().toString()));
        context.startActivity(intent);
    }



    private void getCurrentLocation()
    {
        if(commonFunctions.checkNetConnectivity(context))
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
        }
        else
        {
            //commonFunctions.set_custom_toast_msg(context,CommonVariables.MSG_NETWORK_UNAVAILABLE,toastView);
            Toast.makeText(context,CommonVariables.MSG_NETWORK_UNAVAILABLE,Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServicePickTripClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_PICK_TRIP);

                jo_mobile_no_validate_request.put(CommonVariables.URL_TRIP_ID,jo_journey_details.getString("tripId"));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_ACTION_TYPE,CommonVariables.URLRQ_TRIP_READY_TO_PICK);
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
                Log.e("onPostExecute of ", "AsyncTaskForHttpServicePickTripClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            Toast.makeText(context, "New Trip Picked Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, DashboardActivity.class));
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callAction();
            }
        }
    }



    /**
     * > Generate New Alert Dialog Builder <
     */
    private void generateAlertDialogBuilder(String title, String msg) {
        alert_dialog_builder = new AlertDialog.Builder(context);

        alert_dialog_builder.setTitle(title);
        alert_dialog_builder.setMessage(msg);
        alert_dialog_builder.setCancelable(false);

        alert_dialog_builder.setPositiveButton(CommonVariables.YES, (dialog, which) -> {
            Log.e("alert_dialog_builder : ", "YES");
            if (commonFunctions.checkNetConnectivity(context))
            {
                progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
                new AsyncTaskForHttpServicePickTripClass().execute(ServiceUrlVariables.TRIP_ACTION);
            }
            else
            {
                Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });

        alert_dialog_builder.setNegativeButton(CommonVariables.NO, (dialog, which) -> {
            Log.e("alert_dialog_builder : ", "NO");
            alert_dialog.dismiss();
        });

        alert_dialog = alert_dialog_builder.create();

        alert_dialog.show();
    }
}