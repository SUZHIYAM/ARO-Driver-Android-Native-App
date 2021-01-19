package com.tn74travellers.aroDriver.Main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.tn74travellers.aroDriver.R;

import com.applandeo.materialcalendarview.*;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    public static final String TAG = DashboardActivity.class.getSimpleName();

    Context context = DashboardActivity.this;

    /***************XML Variables Declaration ************************/

    ImageView iv_settings;

    private TextView tv_new_trips;
    private TextView tv_calendar;

    RecyclerView rv_new_trip_list;
    RecyclerView rv_calendar_trip_list;

    RelativeLayout rl_back_to_calendar_container;

    CalendarView calendarView;

    ConstraintLayout cl_ongoing_layout;

    private RelativeLayout rl_calendar_container;
    private RelativeLayout rl_no_new_trip_found_container;
    private RelativeLayout rl_ongoing_trip_right_arrow;

    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    AlertDialog.Builder alert_dialog_builder;
    AlertDialog alertDialog;

    ProgressDialog progress_dialog;

    JSONObject jo_trip_details = new JSONObject();

    List<EventDay> events;

    String event_date;

    boolean isNewTripBtnEnabled = true;
    boolean isDashboardPageVisible = true;

    private int this_day = 0;
    private int this_month = 0;
    private int this_year = 0;

    private List<Date> monthlyDates = new ArrayList<>();
    private List<String> monthlyDatesTripStatus = new ArrayList<>();

    JSONArray ja_trip_date = new JSONArray();

    Handler handlerNewTripList = null;
    Runnable runnableGetNewTripList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListeners();
    }

    private void viewInit()
    {
        iv_settings = findViewById(R.id.iv_settings);
        rl_ongoing_trip_right_arrow = findViewById(R.id.rl_ongoing_trip_right_arrow);

        tv_new_trips = findViewById(R.id.tv_new_trips);
        tv_calendar = findViewById(R.id.tv_calendar);

        rv_new_trip_list = findViewById(R.id.rv_new_trip_list);
        rv_calendar_trip_list = findViewById(R.id.rv_calendar_trip_list);

        rl_back_to_calendar_container = findViewById(R.id.rl_back_to_calendar_container);

        calendarView = findViewById(R.id.calendarView);

        rl_calendar_container = findViewById(R.id.rl_calendar_container);
        rl_no_new_trip_found_container = findViewById(R.id.rl_no_new_trip_found_container);

        cl_ongoing_layout = findViewById(R.id.cl_ongoing_layout);
    }


    private void initialWorks()
    {
        cl_ongoing_layout.setVisibility(View.GONE);
        rl_calendar_container.setVisibility(View.GONE);
        rv_new_trip_list.setVisibility(View.GONE);
        rv_calendar_trip_list.setVisibility(View.GONE);
        rl_back_to_calendar_container.setVisibility(View.GONE);
        calendarView.setSwipeEnabled(false);
        commonFunctions.setSharedPrefString(context,CommonVariables.SP_PAYMENT_UPDATED,"");

        this_day = Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(Calendar.getInstance().getTime()));
        this_month = Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(Calendar.getInstance().getTime()));
        this_year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(Calendar.getInstance().getTime()));

        handlerNewTripList = new Handler();
        runnableGetNewTripList = () -> {
            Log.e("Cron Job Status : ", "Started");
            if (commonFunctions.checkNetConnectivity(context) && isNewTripBtnEnabled)
            {
                new AsyncTaskForHttpServiceNewTripListClass().execute(ServiceUrlVariables.GET_NEW_TRIP_LIST);
                new AsyncTaskForHttpServiceOngoingTripCheckClass().execute(ServiceUrlVariables.GET_ONGOING_TRIP_CHECK);
            }

            if(isDashboardPageVisible)
            {
                /* Enable this below line on production */
                handlerNewTripList.postDelayed(runnableGetNewTripList, 5000);
            }
        };

        handlerNewTripList.postDelayed(runnableGetNewTripList, 5000);

        callService();
    }

    private void clickListeners()
    {
        iv_settings.setOnClickListener(v -> {
            startActivity(new Intent(context,SettingsActivity.class));
        });

        tv_new_trips.setOnClickListener(view -> {
            rv_new_trip_list.setVisibility(View.VISIBLE);
            rv_calendar_trip_list.setVisibility(View.GONE);
            tv_new_trips.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_yellow));
            rl_calendar_container.setVisibility(View.GONE);
            tv_calendar.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_grey));
            rl_back_to_calendar_container.setVisibility(View.GONE);
            rl_no_new_trip_found_container.setVisibility(View.VISIBLE);

            isNewTripBtnEnabled = true;
        });

        cl_ongoing_layout.setOnClickListener(v -> {
            startActivity(new Intent(context,OngoingTripActivity.class));
        });

        tv_calendar.setOnClickListener(view -> {
            rv_new_trip_list.setVisibility(View.GONE);
            rv_calendar_trip_list.setVisibility(View.GONE);
            rl_no_new_trip_found_container.setVisibility(View.GONE);
            tv_new_trips.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_grey));
            rl_calendar_container.setVisibility(View.VISIBLE);
            tv_calendar.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_yellow));
            rl_back_to_calendar_container.setVisibility(View.GONE);

            isNewTripBtnEnabled = false;

            if (commonFunctions.checkNetConnectivity(context))
            {
                Calendar calendar = Calendar.getInstance();

                calendar.set(this_year, Math.abs(this_month-1), this_day);

                try {
                    calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e)
                {
                    e.printStackTrace();

                    Log.e("tv_calendar ","OutOfDateRangeException : "+e.getMessage());
                }

                Log.e("tv_calendar ","btn clicked");

                commonFunctions.cancelLoadingProgress(progress_dialog);

                progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);

                new AsyncTaskForHttpServiceTripListDatesClass().execute(ServiceUrlVariables.GET_TRIP_DATE,
                        (new SimpleDateFormat("yyyy-MM-dd", Locale.US)).format(Calendar.getInstance().getTime()));
            }
            else
            {
                Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }

        });

        rl_back_to_calendar_container.setOnClickListener(view -> {
            tv_new_trips.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_grey));
            tv_calendar.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_yellow));
            rv_new_trip_list.setVisibility(View.GONE);
            rv_calendar_trip_list.setVisibility(View.GONE);
            rl_back_to_calendar_container.setVisibility(View.GONE);
            rl_calendar_container.setVisibility(View.VISIBLE);
        });

        calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();

            int event_day = clickedDayCalendar.get(Calendar.DATE);

            int event_month = clickedDayCalendar.get(Calendar.MONTH)+1;

            int event_year = clickedDayCalendar.get(Calendar.YEAR);

            String eventMonthStr;
            String eventDayStr;

            if(event_month < 10)
            {
                eventMonthStr = "0"+event_month;
            }
            else
            {
                eventMonthStr = event_month+"";
            }

            if(event_day < 10)
            {
                eventDayStr = "0"+event_day;
            }
            else
            {
                eventDayStr = event_day+"";
            }

            Log.e("eventDay",event_day+"");
            Log.e("eventMonthStr",eventMonthStr);

            event_date = event_year+"-"+eventMonthStr+"-"+eventDayStr;

            Log.e("event_date",event_date);
            Log.e("ja_trip_date len : ", ja_trip_date.length()+"");

            ArrayList<String> dateArrayListStr = new ArrayList<>();

            for(int i= 0;i<ja_trip_date.length();i++)
            {
                try
                {
                    String eachTripDate = ja_trip_date.getJSONObject(i).getString("tripDate");

                    Log.e("eachTripDate : ",eachTripDate);

                    Log.e("dateArrayListStr : ", dateArrayListStr.contains(event_date)+"");

                    if(event_date.equals(eachTripDate) && !dateArrayListStr.contains(event_date))
                    {
                        commonFunctions.setSharedPrefString(context,"event_date", event_date);

                        dateArrayListStr.add(eachTripDate);

                        if (commonFunctions.checkNetConnectivity(context))
                        {
                            progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
                            new AsyncTaskForHttpServiceEventTripListClass().execute(ServiceUrlVariables.GET_TRIP_DAY_LIST);
                        }
                        else
                        {
                            Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();

                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            }
        });

        calendarView.setOnPreviousPageChangeListener(() -> {
            if (commonFunctions.checkNetConnectivity(context))
            {
                progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
                new AsyncTaskForHttpServiceTripListDatesClass().execute(ServiceUrlVariables.GET_TRIP_DATE,
                        (new SimpleDateFormat("yyyy-MM-dd", Locale.US)).format(calendarView.getCurrentPageDate().getTime()));
            }
            else
            {
                Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.setOnForwardPageChangeListener(() -> {
            if (commonFunctions.checkNetConnectivity(context))
            {
                progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
                new AsyncTaskForHttpServiceTripListDatesClass().execute(ServiceUrlVariables.GET_TRIP_DATE,
                        (new SimpleDateFormat("yyyy-MM-dd", Locale.US)).format(calendarView.getCurrentPageDate().getTime()));
            }
            else
            {
                Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**> Calling Service Method If Network Is Available Or Show No Network Message <*/
    private void callService()
    {
        if (commonFunctions.checkNetConnectivity(context))
        {
            progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
            new AsyncTaskForHttpServiceNewTripListClass().execute(ServiceUrlVariables.GET_NEW_TRIP_LIST);
            new AsyncTaskForHttpServiceOngoingTripCheckClass().execute(ServiceUrlVariables.GET_ONGOING_TRIP_CHECK);
            new AsyncTaskForHttpServiceUpdatePushNotiClass().execute(ServiceUrlVariables.UPDATE_PUSH_NOTI);
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

            try
            {
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceOngoingTripCheckClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Log.e("get new trip list 1: ", "" + result);

                        Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            Log.e("get new trip list 2: ", "" + result);

                            JSONObject jo_response_data = jo_mobile_no_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);
                            Log.e("tripOngoingStatus : ", jo_response_data.getBoolean("tripOngoingStatus")+"");

                            if (jo_response_data.getBoolean("tripOngoingStatus")) {
                                cl_ongoing_layout.setVisibility(View.VISIBLE);
                                commonFunctions.setSharedPrefString(context, CommonVariables.SP_ONGOING_TRIP_ID, jo_response_data.getString("tripId"));
                            } else {
                                cl_ongoing_layout.setVisibility(View.GONE);
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



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceNewTripListClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_NEW_TRIP_LIST);

                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_TRAVELS_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_TRAVELS_ID));
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
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceNewTripListClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404 == result) {
                        Log.e("get new trip list 1: ", "" + result);

                        Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        switch (jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG)) {
                            case CommonVariables.URLRQ_SUCCESS: {
                                Log.e("get new trip list 2: ", "" + result);

                                JSONArray ja_response_data = jo_mobile_no_response.getJSONArray(CommonVariables.URLRS_RESPONSE_DATA);

                                if(ja_response_data.length() == 0)
                                {
                                    tv_new_trips.setText("New Trips");

                                    rl_no_new_trip_found_container.setVisibility(View.VISIBLE);
                                    rv_new_trip_list.setVisibility(View.GONE);
                                }
                                else
                                {
                                    tv_new_trips.setText(String.valueOf(ja_response_data.length() + " New Trips"));

                                    rl_no_new_trip_found_container.setVisibility(View.GONE);
                                    rv_new_trip_list.setVisibility(View.VISIBLE);
                                }

                                commonFunctions.setToRecyclerViewAdapter(context, CommonVariables.CASE_NEW_TRIP_LIST, ja_response_data, rv_new_trip_list);

                                break;
                            }
                            case "No Data found!": {

                                rl_no_new_trip_found_container.setVisibility(View.VISIBLE);
                                rv_new_trip_list.setVisibility(View.GONE);
                                rl_calendar_container.setVisibility(View.GONE);

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

                //Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceTripListDatesClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                Log.e("params[1] : ", params[1]);
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_TRIP_DATE_LIST);

                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_TRAVELS_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_TRAVELS_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DATE,params[1]);
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
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceTripListDatesClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Log.e("get new trip list 1: ", "" + result);

                        Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            Log.e("get trip Date list 2: ", "" + result);

                            JSONArray ja_response_data = jo_response.getJSONArray(CommonVariables.URLRS_RESPONSE_DATA);
                            ja_trip_date = jo_response.getJSONArray(CommonVariables.URLRS_RESPONSE_DATA);

                            //monthlyDates.add(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2020-11-19"));

                            if(ja_response_data.length() != 0)
                            {
                                monthlyDates = new ArrayList<>();

                                for (int i = 0; i < ja_response_data.length(); i++) {
                                    monthlyDates.add(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(ja_response_data.getJSONObject(i).getString("tripDate")));
                                    monthlyDatesTripStatus.add(ja_response_data.getJSONObject(i).getString("tripStatus"));
                                    Log.e("1monthlyDates.size()", ja_response_data.getJSONObject(i).getString("tripDate")+"");
                                }

                                events = new ArrayList<>();

                                for (int i = 0; i < monthlyDates.size(); i++) {
                                    Date mDate = monthlyDates.get(i);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(mDate);

                                    String this_date = (new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().getTime()));
                                    String trip_date = ja_response_data.getJSONObject(i).getString("tripDate");

                                    Log.e("monthlyDatesTripStatus ", "["+i+"]"+monthlyDatesTripStatus.get(i));
                                    Log.e("this_date ", "["+i+"]"+this_date);
                                    Log.e("trip_date ", "["+i+"]"+trip_date);

                                    // if (CommonVariables.URLRQ_TRIP_PAYMENT_COMPLETED.equalsIgnoreCase(monthlyDatesTripStatus.get(i))) {

                                    if (this_date.equalsIgnoreCase(trip_date)) {
                                        events.add(new EventDay(calendar, R.drawable.circle_yellow_bg));
                                    } else {
                                        events.add(new EventDay(calendar, R.drawable.circle_grey_bg));
                                    }
                                }

                                calendarView.setEvents(events);
                            }
                            else
                            {
                                Log.e("onPostExecute","AsyncTaskForHttpServiceTripListDatesClass : Empty List");
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

                Log.e(TAG, "onPostExecute AsyncTaskForHttpServiceTripListDatesClass Exception : "+e.getMessage());

                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceEventTripListClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_EVENT_TRIP_LIST);

                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_TRAVELS_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_TRAVELS_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DATE,commonFunctions.getSharedPrefString(context,"event_date"));

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
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceEventTripListClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            JSONArray ja_response_data = jo_response.getJSONArray(CommonVariables.URLRS_RESPONSE_DATA);

                            commonFunctions.setToRecyclerViewAdapter(context, CommonVariables.CASE_EVENT_TRIP_LIST, ja_response_data, rv_calendar_trip_list);

                            //ht access file, add one more on root folder
                            rl_calendar_container.setVisibility(View.GONE);
                            rv_calendar_trip_list.setVisibility(View.VISIBLE);
                            rl_back_to_calendar_container.setVisibility(View.VISIBLE);
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



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceUpdatePushNotiClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_UPDATE_PUSH_NOTI);

                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DEVICE_TOKEN, commonFunctions.getSharedPrefString(context,CommonVariables.SP_DEVICE_TOKEN));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_MOBILE_TYPE,CommonVariables.URLRQ_ANDROID);
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
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

            try
            {
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceUpdatePushNotiClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Log.e("Update Push Noti : ", "" + result);
                    } else {
                        JSONObject jo_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            Log.e("Update Push Noti : ", "Updated Successful");
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }



    public void tripAcceptFunc(JSONObject jsonObject)
    {
        jo_trip_details = jsonObject;
        generateAlertDialogBuilder("Accept this trip", "Sure to accept this trip!", 0);
    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceTripAcceptClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_TRIP_ACCEPT);

                jo_mobile_no_validate_request.put(CommonVariables.URL_TRIP_ID,jo_trip_details.getString("tripId"));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_ACTION_TYPE,CommonVariables.URLRQ_TRIP_ACCEPTED);
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
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceTripAcceptClass : "+result);

                if(result != null)
                {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            JSONObject jo_response_data = jo_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);

                            if (jo_response_data.getBoolean("tripStatus")) {
                                //startActivity(new Intent(context, TripConfirmationActivity.class));
                                Toast.makeText(context, "Trip Accepted", Toast.LENGTH_SHORT).show();
                                tv_new_trips.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_grey));
                                tv_calendar.setBackground(getResources().getDrawable(R.drawable.bg_rect_fill_yellow));
                                startActivity(new Intent(context, DashboardActivity.class));
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



    /**> Generate New Alert Dialog Builder <*/
    private void generateAlertDialogBuilder(String title, String msg, int from)
    {
        alert_dialog_builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        alert_dialog_builder.setTitle(title);
        alert_dialog_builder.setMessage(msg);
        alert_dialog_builder.setCancelable(false);

        alert_dialog_builder.setPositiveButton(CommonVariables.YES, (dialog, which) -> {
            switch (from) {
                case 0: // Click on New Trip Accept Button
                {
                    if (commonFunctions.checkNetConnectivity(context))
                    {
                        progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);
                        new AsyncTaskForHttpServiceTripAcceptClass().execute(ServiceUrlVariables.TRIP_ACTION);
                    }
                    else
                    {
                        Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 1:// Back button pressed
                {
                    if (handlerNewTripList != null)
                    {
                        handlerNewTripList.removeCallbacks(runnableGetNewTripList);
                    }

                    finishAffinity();
                    finish();
                    break;
                }
            }
        });

        alert_dialog_builder.setNegativeButton(CommonVariables.NO, (dialog, which) -> alertDialog.dismiss());

        alertDialog = alert_dialog_builder.create();

        alertDialog.show();
    }



    @Override
    protected void onResume() {
        super.onResume();
        isDashboardPageVisible = true;
    }



    @Override
    protected void onPause() {
        super.onPause();
        isDashboardPageVisible = false;
    }



    @Override
    public void onBackPressed() {
        generateAlertDialogBuilder("Exit", "Sure to Exit!", 1);
    }
}
