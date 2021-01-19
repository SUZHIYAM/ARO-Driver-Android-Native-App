package com.tn74travellers.aroDriver.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDayTripListActivity extends AppCompatActivity {

    Context context = EventDayTripListActivity.this;

    /***************XML Variables Declaration ************************/

    ImageView iv_settings;
    ImageView iv_ongoing_trip_right_arrow;

    Button btn_new_trips;
    Button btn_calendar;

    RecyclerView rv_new_trip_list;

    RelativeLayout rl_back_to_calendar_container;

    CalendarView calendarView;

    ConstraintLayout cl_ongoing_layout;

    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    AlertDialog.Builder alert_dialog_builder;
    AlertDialog alertDialog;

    ProgressDialog progress_dialog;

    JSONObject jo_trip_details = new JSONObject();

    List<EventDay> events;

    String event_date;

    private List<Date> monthlyDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_based_trips);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListenersfunc();
    }

    private void viewInit()
    {
        iv_settings = findViewById(R.id.iv_settings);
        iv_ongoing_trip_right_arrow = findViewById(R.id.iv_ongoing_trip_right_arrow);

        btn_new_trips = findViewById(R.id.tv_new_trips);
        btn_calendar = findViewById(R.id.tv_calendar);

        rv_new_trip_list = findViewById(R.id.rv_new_trip_list);

        calendarView = findViewById(R.id.calendarView);

        cl_ongoing_layout = findViewById(R.id.cl_ongoing_layout);

        rl_back_to_calendar_container = findViewById(R.id.rl_back_to_calendar_container);

    }


    private void initialWorks()
    {

        rv_new_trip_list.setVisibility(View.VISIBLE);
        cl_ongoing_layout.setVisibility(View.GONE);
        btn_new_trips.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_grey));
        btn_calendar.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_yellow));

        calEventTripService();
    }

    private void clickListenersfunc()
    {
        iv_settings.setOnClickListener(v -> {
            startActivity(new Intent(context, SettingsActivity.class));
        });

        btn_new_trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(context, DashboardActivity.class));
            }
        });


        rl_back_to_calendar_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //commonFunctions.set_shared_pref_str_func(context,"from","back_to_cal");
                btn_new_trips.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_grey));
                btn_calendar.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_yellow));
            }
        });


        cl_ongoing_layout.setOnClickListener(v -> {
            startActivity(new Intent(context,OngoingTripActivity.class));
        });

    }



    /**> Calling Service Method If Network Is Available Or Show No Network Message <*/
    private void calEventTripService()
    {
        if (commonFunctions.checkNetConnectivity(context))
        {

            progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
            new AsyncTaskForHttpServiceOngoingTripCheckClass().execute(ServiceUrlVariables.GET_ONGOING_TRIP_CHECK);
        }
        else
        {
            Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceOngoingTripCheckClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_ONGOING_TRIP_CHECK);

                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                //jo_mobile_no_validate_request.put(CommonVariables.URL_USERID,"111");

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

            Log.e("ongoing trip check : ", ""+result);

            try
            {
                if(result != null)
                {
                    switch (result)
                    {
                        case CommonVariables.SERVICE_RESPONSE_CODE_404:
                        {
                            Log.e("get new trip list 1: ", ""+result);

                            Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();

                            break;
                        }
                        default:
                        {
                            JSONObject jo_mobile_no_response = new JSONObject(result);

                            switch (jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))
                            {
                                case "Data available":
                                {
                                    Log.e("get new trip list 2: ", ""+result);

                                    JSONArray ja_response_data = jo_mobile_no_response.getJSONArray("responseData");

                                    commonFunctions.setSharedPrefString(context,"on_going_trip_id",ja_response_data.getJSONObject(0).getString("tripId"));

                                    if(ja_response_data.getJSONObject(0).getString("pickThisTrip").equals("true"))
                                    {
                                        cl_ongoing_layout.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        cl_ongoing_layout.setVisibility(View.GONE);
                                    }

                                    break;
                                }
//                                case "No Data found!":
//                                {
//                                    Log.e("get new trip list 3: ", ""+result);
//
//                                    rl_no_new_trip_found_container.setVisibility(View.VISIBLE);
//                                    rv_new_trip_list.setVisibility(View.GONE);
//                                    rl_calendar_container.setVisibility(View.GONE);
//
//                                    break;
//                                }
                            }
                            break;
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
