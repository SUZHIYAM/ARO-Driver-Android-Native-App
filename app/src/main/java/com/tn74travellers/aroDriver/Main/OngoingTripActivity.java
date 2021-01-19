package com.tn74travellers.aroDriver.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.tn74travellers.aroDriver.BuildConfig;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.FilePathVariables;
import com.tn74travellers.aroDriver.Common.ImageZoomActivity;
import com.tn74travellers.aroDriver.Common.ServiceUrlVariables;
import com.tn74travellers.aroDriver.Models.FileUploadOutput;
import com.tn74travellers.aroDriver.R;
import com.tn74travellers.aroDriver.Retrofit.ApiInterface;
import com.tn74travellers.aroDriver.Retrofit.RetrofitClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OngoingTripActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    Context context = OngoingTripActivity.this;

    Activity activity = OngoingTripActivity.this;

    private static final String TAG = OngoingTripActivity.class.getSimpleName();

    /***************XML Variables Declaration ************************/

    ImageView iv_back_arrow;
    ImageView iv_starting_km;
    ImageView iv_ending_km;
    ImageView iv_refresh;

    TextView tv_trip_id;
    TextView tv_passenger_name;
    TextView tv_mobile_number;
    TextView tv_passenger_location;
    TextView tv_trip_type;
    TextView tv_start_date;
    TextView tv_start_time;
    TextView tv_end_date;
    TextView tv_end_time;
    TextView tv_online_payment_amount;
    TextView tv_advance_paid_amount;
    TextView tv_balance_amount;
    TextView tv_travelled_amount;
    TextView tv_travelled_balance_amount;
    TextView tv_grand_total_amount;

    Spinner sp_change_trip_status;

    ScrollView sv_on_going_trip_cards;

    ImageView iv_car_center;

    ConstraintLayout cl_airport_trip_location;
    ConstraintLayout cl_rental_pack;
    ConstraintLayout cl_passenger_details;

    LinearLayout ll_location_container;
    LinearLayout ll_trip_details_container;
    LinearLayout ll_spinner_container;
    LinearLayout ll_reason_for_cancel_container;
    LinearLayout ll_driver_manual_charging;
    LinearLayout ll_amount_container;
    LinearLayout ll_amount_paid_container;

    EditText et_starting_km;
    EditText et_ending_km;
    EditText et_halt;
    EditText et_halt_amount;
    EditText et_hill;
    EditText et_hill_amount;
    EditText et_batta;
    EditText et_batta_amount;
    EditText et_penalty_amount;
    EditText et_penalty_reason;
    EditText et_cash_payment_amount;
    EditText et_feedback;
    EditText et_reason_for_cancel;

    Button btn_capture;
    Button btn_gallery;
    Button btn_update;

    TextView tv_airport_location;
    TextView tv_hours_pickup_location;
    TextView tv_hour_pack;
    TextView tv_airport_hour_pack;
    TextView tv_distance_travelled;
    TextView tv_rating_send;
    TextView tv_expected_amount;
    //TextView tv_pickup_location_label;
    // TextView tv_pickup_location;

    private RelativeLayout rl_glass_whole_container;
    private RelativeLayout rl_passenger_details_up_arrow;
    private RelativeLayout rl_passenger_details_down_arrow;
    private RelativeLayout rl_trip_details_down_arrow;
    private RelativeLayout rl_trip_details_up_arrow;

    RatingBar rb_card_rating;

    CardView cv_feedback;

    LinearLayout ll_online_payment_container;
    LinearLayout ll_cash_payment_container;


    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    ProgressDialog progress_dialog;

    AlertDialog.Builder alert_dialog_builder;
    AlertDialog alert_dialog;

    String tripId = "";
    String reason = "";
    String feedback = "";
    float rating = 0;

    String[] permissions_for =
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };

    private static final int PERMISSION_REQUEST_CODE = 0;

    private boolean camera_selected = true;

    String tempSavedKmImagePath = null;
    String file_path_1 = null;
    String tripStatus = "";
    String tripStatusFromResponse = "";
    String startingTravelledKm = "";
    String endingTravelledKm = "";
    String halt = "";
    String penalty = "";
    String penaltyReason = "";
    String balance_amount = "";
    String closedAmount = "";

    Integer haltCount = 0;

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int CROP_IMAGE_REQUEST = 3;
    private static final int CALL_REQUEST = 4;

    private static final String SELECT_PICTURE = "Select Picture";


    public static FilePathVariables file_path_variables;

    String selectedPhoto = "/IMG1" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpeg";

    boolean is_image_upload;
    boolean is_trip_cancelled = false;

    String startingImgUrl = "";
    String endingImgUrl = "";

    double halt_amount = 0;
    double hill_amount = 0;
    double batta_amount = 0;
    double penalty_amount = 0;
    double grand_total_to_be_paid = 0;
    double total_balance_amount = 0;
    double travelled_balance_amount = 0;
    double basicHaltAmount = 0;
    double basicHillAmount = 0;
    double basicBattaAmount = 0;

    DecimalFormat dfCurrency = new DecimalFormat("0.00");

    Handler handler_get_payment_response = null;
    Runnable runnable_get_payment_response = null;

    String[] statusTxtListArr;
    String[] statusListArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_trip);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListeners();

        callService();
    }


    private void viewInit() {
        iv_back_arrow = findViewById(R.id.iv_back_arrow);
        iv_car_center = findViewById(R.id.iv_car_center);
        iv_starting_km = findViewById(R.id.iv_starting_km);
        iv_ending_km = findViewById(R.id.iv_ending_km);
        iv_refresh = findViewById(R.id.iv_refresh);

        sv_on_going_trip_cards = findViewById(R.id.sv_on_going_trip_cards);

        rl_passenger_details_down_arrow = findViewById(R.id.rl_passenger_details_down_arrow);
        rl_passenger_details_up_arrow = findViewById(R.id.rl_passenger_details_up_arrow);
        rl_trip_details_down_arrow = findViewById(R.id.rl_trip_details_down_arrow);
        rl_trip_details_up_arrow = findViewById(R.id.rl_trip_details_up_arrow);

        tv_trip_id = findViewById(R.id.tv_trip_id);
        tv_passenger_name = findViewById(R.id.tv_passenger_name);
        tv_mobile_number = findViewById(R.id.tv_mobile_number);
        tv_passenger_location = findViewById(R.id.tv_passenger_location);
        tv_trip_type = findViewById(R.id.tv_trip_type);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_online_payment_amount = findViewById(R.id.tv_online_payment_amount);
        tv_advance_paid_amount = findViewById(R.id.tv_advance_paid_amount);
        tv_balance_amount = findViewById(R.id.tv_balance_amount);
        tv_travelled_amount = findViewById(R.id.tv_travelled_amount);
        tv_travelled_balance_amount = findViewById(R.id.tv_travelled_balance_amount);
        tv_grand_total_amount = findViewById(R.id.tv_grand_total_amount);
        tv_distance_travelled = findViewById(R.id.tv_distance_travelled);
        tv_rating_send = findViewById(R.id.tv_rating_send);
        //tv_pickup_location = findViewById(R.id.tv_pickup_location);
        tv_airport_location = findViewById(R.id.tv_airport_location);
        tv_hours_pickup_location = findViewById(R.id.tv_hours_pickup_location);
        tv_hour_pack = findViewById(R.id.tv_hour_pack);
        tv_airport_hour_pack = findViewById(R.id.tv_airport_hour_pack);
        tv_expected_amount = findViewById(R.id.tv_expected_amount);

        rb_card_rating = findViewById(R.id.rb_card_rating);

        cl_airport_trip_location = findViewById(R.id.cl_airport_trip_location);
        cl_rental_pack = findViewById(R.id.cl_rental_pack);
        cl_passenger_details = findViewById(R.id.cl_passenger_details);
        ll_amount_container = findViewById(R.id.ll_amount_container);
        ll_amount_paid_container = findViewById(R.id.ll_amount_paid_container);

        ll_driver_manual_charging = findViewById(R.id.ll_driver_manual_charging);
        ll_location_container = findViewById(R.id.ll_location_container);
        ll_trip_details_container = findViewById(R.id.ll_trip_details_container);
        ll_spinner_container = findViewById(R.id.ll_spinner_container);
        ll_reason_for_cancel_container = findViewById(R.id.ll_reason_for_cancel_container);
        ll_online_payment_container = findViewById(R.id.ll_online_payment_container);
        ll_cash_payment_container = findViewById(R.id.ll_cash_payment_container);

        sp_change_trip_status = findViewById(R.id.sp_change_trip_status);

        et_starting_km = findViewById(R.id.et_starting_km);
        et_ending_km = findViewById(R.id.et_ending_km);
        et_halt = findViewById(R.id.et_halt);
        et_hill = findViewById(R.id.et_hill);
        et_batta = findViewById(R.id.et_batta);
        et_halt_amount = findViewById(R.id.et_halt_amount);
        et_hill_amount = findViewById(R.id.et_hill_amount);
        et_batta_amount = findViewById(R.id.et_batta_amount);
        et_penalty_amount = findViewById(R.id.et_penalty_amount);
        et_penalty_reason = findViewById(R.id.et_penalty_reason);
        et_cash_payment_amount = findViewById(R.id.et_cash_payment_amount);
        et_feedback = findViewById(R.id.et_feedback);
        et_reason_for_cancel = findViewById(R.id.et_reason_for_cancel);

        btn_capture = findViewById(R.id.btn_capture);
        btn_gallery = findViewById(R.id.btn_gallery);
        btn_update = findViewById(R.id.btn_update);

        rl_glass_whole_container = findViewById(R.id.rl_glass_whole_container);

        cv_feedback = findViewById(R.id.cv_feedback);
    }


    private void initialWorks() {
        file_path_variables = new FilePathVariables(context);

        rl_passenger_details_up_arrow.setVisibility(View.GONE);
        rl_passenger_details_down_arrow.setVisibility(View.VISIBLE);
        rl_trip_details_up_arrow.setVisibility(View.GONE);
        rl_trip_details_down_arrow.setVisibility(View.VISIBLE);

        cl_passenger_details.setVisibility(View.GONE);

        ll_trip_details_container.setVisibility(View.GONE);
        ll_online_payment_container.setVisibility(View.VISIBLE);
        ll_cash_payment_container.setVisibility(View.VISIBLE);
        ll_reason_for_cancel_container.setVisibility(View.GONE);

        rl_glass_whole_container.setVisibility(View.GONE);

        cv_feedback.setVisibility(View.GONE);

        btn_gallery.setVisibility(View.GONE);

        et_cash_payment_amount.setEnabled(false);
    }


    private void clickListeners() {
        sp_change_trip_status.setOnItemSelectedListener(this);

        iv_back_arrow.setOnClickListener(view -> generateAlertDialogBuilder("Wait", "Sure to go back!", 1));

        iv_refresh.setOnClickListener(v -> {
            startActivity(new Intent(context, OngoingTripActivity.class));
        });

        rl_glass_whole_container.setOnClickListener(view -> {
            /* Do Nothing just used to block user interaction on background */
        });

        rl_passenger_details_down_arrow.setOnClickListener(view -> {
            rl_passenger_details_up_arrow.setVisibility(View.VISIBLE);
            rl_passenger_details_down_arrow.setVisibility(View.GONE);
            cl_passenger_details.setVisibility(View.VISIBLE);
            rl_trip_details_up_arrow.setVisibility(View.GONE);
            rl_trip_details_down_arrow.setVisibility(View.VISIBLE);
            ll_trip_details_container.setVisibility(View.GONE);
        });

        rl_passenger_details_up_arrow.setOnClickListener(view -> {
            rl_passenger_details_up_arrow.setVisibility(View.GONE);
            rl_passenger_details_down_arrow.setVisibility(View.VISIBLE);
            cl_passenger_details.setVisibility(View.GONE);
            rl_trip_details_up_arrow.setVisibility(View.GONE);
            rl_trip_details_down_arrow.setVisibility(View.VISIBLE);
            ll_trip_details_container.setVisibility(View.GONE);
        });

        rl_trip_details_down_arrow.setOnClickListener(view -> {
            rl_trip_details_up_arrow.setVisibility(View.VISIBLE);
            rl_trip_details_down_arrow.setVisibility(View.GONE);
            ll_trip_details_container.setVisibility(View.VISIBLE);
            rl_passenger_details_up_arrow.setVisibility(View.GONE);
            rl_passenger_details_down_arrow.setVisibility(View.VISIBLE);
            cl_passenger_details.setVisibility(View.GONE);
        });

        rl_trip_details_up_arrow.setOnClickListener(view -> {
            rl_trip_details_up_arrow.setVisibility(View.GONE);
            rl_trip_details_down_arrow.setVisibility(View.VISIBLE);
            ll_trip_details_container.setVisibility(View.GONE);
            rl_passenger_details_up_arrow.setVisibility(View.GONE);
            rl_passenger_details_down_arrow.setVisibility(View.VISIBLE);
            cl_passenger_details.setVisibility(View.GONE);
        });

        btn_gallery.setOnClickListener(v -> {
            camera_selected = false;

            if ((ContextCompat.checkSelfPermission(context, permissions_for[0]) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context, permissions_for[1]) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context, permissions_for[2]) == PackageManager.PERMISSION_GRANTED)) {
                afterGrantedPermissions();
            } else {
                ActivityCompat.requestPermissions(activity, permissions_for, PERMISSION_REQUEST_CODE);
            }
        });

        btn_capture.setOnClickListener(view -> {
            camera_selected = true;

            if ((ContextCompat.checkSelfPermission(context, permissions_for[0]) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context, permissions_for[1]) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context, permissions_for[2]) == PackageManager.PERMISSION_GRANTED)) {
                afterGrantedPermissions();
            } else {
                ActivityCompat.requestPermissions(activity, permissions_for, PERMISSION_REQUEST_CODE);
            }
        });

        btn_update.setOnClickListener(view -> {
            hideKeyboard();
            try {
                switch (tripStatus) {
                    case CommonVariables.CHANGE_TRIP_STATUS: {
                        Toast.makeText(context, "Change the trip status", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case CommonVariables.URLRQ_TRIP_STARTED: {
                        startingTravelledKm = et_starting_km.getText().toString();

                        Log.e("starting_km :", startingTravelledKm);

                        if (startingTravelledKm.isEmpty()) {
                            Toast.makeText(context, "Please Enter Starting Kilometer", Toast.LENGTH_SHORT).show();
                        } else if (tempSavedKmImagePath == null) {
                            Toast.makeText(context, "Please Capture Starting Kilometer Image", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("tempSavedKmImagePath :", tempSavedKmImagePath);

                            is_image_upload = true;

                            generateAlertDialogBuilder("Update", "Sure to update the changes!", 0);
                        }
                        break;
                    }//closed_amount
                    case CommonVariables.URLRQ_TRIP_COMPLETED: {
                        tripCompleteOrCancel();
                        break;
                    }
                    case CommonVariables.URLRQ_TRIP_CANCELLED: {
                        String reason_for_cancel = et_reason_for_cancel.getText().toString();
                        if (reason_for_cancel.isEmpty()) {
                            Toast.makeText(context, "Please type trip cancel reason!", Toast.LENGTH_SHORT).show();
                        } else if (reason_for_cancel.length() == 1) {
                            Toast.makeText(context, "Need minimum of 2 characters for trip cancel reason!", Toast.LENGTH_SHORT).show();
                        } else {
                            tripCompleteOrCancel();
                        }
                        break;
                    }
                    case CommonVariables.URLRQ_TRIP_PAYMENT_COMPLETED: {
                        closedAmount = et_cash_payment_amount.getText().toString();
                        if (grand_total_to_be_paid > 0 && closedAmount.isEmpty()) {
                            Toast.makeText(context, "Please enter cash amount", Toast.LENGTH_SHORT).show();
                        } else {
                            generateAlertDialogBuilder("Update", "Sure to update the changes!", 0);
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                Log.e("Exception ", "btn_update : " + e.getMessage());
            }
        });

        tv_rating_send.setOnClickListener(v -> {
            feedback = et_feedback.getText().toString();
            rating = rb_card_rating.getRating();

            //if (feedback.isEmpty()) {
            feedback = "";
            //}

            if (rating == 0) {
                Toast.makeText(context, "Add rating for trip completion", Toast.LENGTH_SHORT).show();
            } else {
                hideKeyboard();

                if (commonFunctions.checkNetConnectivity(context)) {
                    progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);

                    new AsyncTaskForHttpServiceAddFeedbackClass().execute(ServiceUrlVariables.ADD_RATING);
                } else {
                    Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_starting_km.setOnClickListener(v -> {

            if (!startingImgUrl.equals("")) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra(CommonVariables.INTENT_IMAGE_ZOOM_PATH_TYPE, CommonVariables.CASE_IMAGE_ZOOM_IMG_PATH);
                intent.putExtra(CommonVariables.CASE_IMAGE_URL, startingImgUrl);
                startActivity(intent);
            } else if (tempSavedKmImagePath != null) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra(CommonVariables.INTENT_IMAGE_ZOOM_PATH_TYPE, CommonVariables.CASE_IMAGE_ZOOM_URL_PATH);
                intent.putExtra(CommonVariables.INTENT_IMAGE_ZOOM_IMG_PATH, tempSavedKmImagePath);
                startActivity(intent);
            }
        });

        iv_ending_km.setOnClickListener(v -> {
            if (!endingImgUrl.equals("")) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra(CommonVariables.INTENT_IMAGE_ZOOM_PATH_TYPE, CommonVariables.CASE_IMAGE_ZOOM_IMG_PATH);
                intent.putExtra(CommonVariables.CASE_IMAGE_URL, endingImgUrl);
                startActivity(intent);
            } else if (tempSavedKmImagePath != null) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra(CommonVariables.INTENT_IMAGE_ZOOM_PATH_TYPE, CommonVariables.CASE_IMAGE_ZOOM_URL_PATH);
                intent.putExtra(CommonVariables.INTENT_IMAGE_ZOOM_IMG_PATH, tempSavedKmImagePath);
                startActivity(intent);
            }
        });

        et_halt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                grand_total_to_be_paid = travelled_balance_amount;
                Log.e("travelled_balance ", "" + travelled_balance_amount);
                Log.e("grand_total ", "" + grand_total_to_be_paid);

                calculateGrandTotalAmount();

                if (s.length() == 0) {
                    et_halt_amount.setText("0.00");
                }
            }
        });

        et_hill.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                grand_total_to_be_paid = travelled_balance_amount;
                Log.e("travelled_balance ", "" + travelled_balance_amount);
                Log.e("grand_total ", "" + grand_total_to_be_paid);

                calculateGrandTotalAmount();

                if (s.length() == 0) {
                    et_hill_amount.setText("0.00");
                }
            }
        });

        et_batta.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                grand_total_to_be_paid = travelled_balance_amount;
                Log.e("travelled_balance ", "" + travelled_balance_amount);
                Log.e("grand_total ", "" + grand_total_to_be_paid);

                calculateGrandTotalAmount();

                if (s.length() == 0) {
                    et_batta_amount.setText("0.00");
                }
            }
        });

        et_penalty_amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                grand_total_to_be_paid = travelled_balance_amount;
                Log.e("travelled_balance ", "" + travelled_balance_amount);
                Log.e("grand_total ", "" + grand_total_to_be_paid);

                calculateGrandTotalAmount();
            }
        });
    }


    private void tripCompleteOrCancel() {
        Log.e("ending_km :", endingTravelledKm);
        Log.e("starting_km :", startingTravelledKm);
        Log.e("penalty_amount :", penalty_amount + "");

        endingTravelledKm = et_ending_km.getText().toString();

        penaltyReason = et_penalty_reason.getText().toString();

        if (endingTravelledKm.isEmpty()) {
            Toast.makeText(context, "Please Enter Ending Kilometer", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(endingTravelledKm) < Double.parseDouble(startingTravelledKm)) {
            Toast.makeText(context, "Check Ending Kilometer", Toast.LENGTH_SHORT).show();
        } else if (tempSavedKmImagePath == null) {
            Toast.makeText(context, "Please Capture Ending Kilometer Image", Toast.LENGTH_SHORT).show();
        } else if (penalty_amount != 0.0 && penaltyReason.isEmpty()) {
            Toast.makeText(context, "Please type penalty reason", Toast.LENGTH_SHORT).show();
        } else if (penaltyReason.length() == 1) {
            Toast.makeText(context, "Need minimum of 2 characters for penalty reason!", Toast.LENGTH_SHORT).show();
        } else {
            is_image_upload = true;

            Log.e("tempSavedKmImagePath :", tempSavedKmImagePath);

            generateAlertDialogBuilder("Update", "Sure to update the changes!", 0);
        }
    }


    private void calculateGrandTotalAmount() {
        String halt_str;
        if (!et_halt.getText().toString().equalsIgnoreCase(".")) {
            halt_str = et_halt.getText().toString();
        } else {
            halt_str = "0" + et_halt.getText().toString();
        }
        Log.e("halt_str :", halt_str);

        String hill_str;
        if (!et_hill.getText().toString().equalsIgnoreCase(".")) {
            hill_str = et_hill.getText().toString();
        } else {
            hill_str = "0" + et_hill.getText().toString();
        }
        Log.e("hill_str :", hill_str);

        String batta_str;
        if (!et_batta.getText().toString().equalsIgnoreCase(".")) {
            batta_str = et_batta.getText().toString();
        } else {
            batta_str = "0" + et_batta.getText().toString();
        }
        Log.e("batta_str :", batta_str);

        String penalty_str;
        if (!et_penalty_amount.getText().toString().equalsIgnoreCase(".")) {
            penalty_str = et_penalty_amount.getText().toString();
        } else {
            penalty_str = "0" + et_penalty_amount.getText().toString();
        }
        Log.e("penalty_str :", penalty_str);

        halt_amount = 0;
        hill_amount = 0;
        batta_amount = 0;
        penalty_amount = 0;

        if (!halt_str.equals("")) {
            halt_amount = Double.parseDouble(halt_str) * basicHaltAmount;
            et_halt_amount.setText(dfCurrency.format(Float.parseFloat(String.valueOf(halt_amount))));
        }
        Log.e("halt_amount :", halt_amount + "");

        if (!hill_str.equals("")) {
            hill_amount = Double.parseDouble(hill_str) * basicHillAmount;
            et_hill_amount.setText(dfCurrency.format(Float.parseFloat(String.valueOf(hill_amount))));
        }
        Log.e("hill_amount :", hill_amount + "");

        if (!batta_str.equals("")) {
            batta_amount = Double.parseDouble(batta_str) * basicBattaAmount;
            et_batta_amount.setText(dfCurrency.format(Float.parseFloat(String.valueOf(batta_amount))));
        }
        Log.e("batta_str :", batta_str + "");

        if (!penalty_str.equals("")) {
            penalty_amount = Double.parseDouble(penalty_str);
        }
        Log.e("penalty_amount :", penalty_amount + "");

        total_balance_amount = grand_total_to_be_paid + halt_amount + hill_amount + batta_amount + penalty_amount;

        tv_grand_total_amount.setText(dfCurrency.format(Float.parseFloat(String.valueOf(total_balance_amount))));

        Log.e("total_balance_amount :", total_balance_amount + "");
    }


    private void calFileUploadService() {
        if (commonFunctions.checkNetConnectivity(context)) {
            progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);

            Retrofit retrofit = RetrofitClass.create();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            File file = null;
            RequestBody reqFile = null;

            if (tempSavedKmImagePath != null) {
                file = new File(tempSavedKmImagePath);
                reqFile = RequestBody.create(okhttp3.MediaType.parse("image/jpeg"), file);
            }

            //RequestBody reqFile =  RequestBody.create(MediaType.parse("multipart/form-data"), file);

            Log.e("calFileUploadService : ", "selected action_type : " + tripStatus);

            RequestBody trip_id = RequestBody.create(MediaType.parse("text/plain"), tripId);
            RequestBody driver_id = RequestBody.create(MediaType.parse("text/plain"), commonFunctions.getSharedPrefString(context, CommonVariables.SP_DRIVER_ID));
            RequestBody action_type = RequestBody.create(MediaType.parse("text/plain"), tripStatus);

            MultipartBody.Part pickup_img = null;
            MultipartBody.Part drop_img = null;

            RequestBody pickup_km = null;
            RequestBody drop_km = null;
            RequestBody halt = null;
            RequestBody hill = null;
            RequestBody toll = null;
            RequestBody batta = null;
            RequestBody penalty = null;
            RequestBody penalty_reason = null;
            RequestBody closed_amount = null;

            switch (tripStatus) {
                case CommonVariables.URLRQ_TRIP_STARTED: {
                    pickup_img = MultipartBody.Part.createFormData("pickup_img",
                            file.getName(), reqFile);

                    pickup_km = RequestBody.create(MediaType.parse("text/plain"), startingTravelledKm);
                    Log.e("calFileUploadService : ", "pickup_img : " + reqFile);
                    break;
                }
                case CommonVariables.URLRQ_TRIP_COMPLETED:
                case CommonVariables.URLRQ_TRIP_CANCELLED: {
                    pickup_km = null;
                    drop_img = MultipartBody.Part.createFormData("drop_img",
                            file.getName(), reqFile);

                    drop_km = RequestBody.create(MediaType.parse("text/plain"), endingTravelledKm);

                    halt = RequestBody.create(MediaType.parse("text/plain"), et_halt.getText().toString());
                    hill = RequestBody.create(MediaType.parse("text/plain"), et_hill.getText().toString());
                    toll = RequestBody.create(MediaType.parse("text/plain"), "100");
                    batta = RequestBody.create(MediaType.parse("text/plain"), et_batta.getText().toString());
                    penalty = RequestBody.create(MediaType.parse("text/plain"), et_penalty_amount.getText().toString());
                    penalty_reason = RequestBody.create(MediaType.parse("text/plain"), penaltyReason);
                    Log.e("calFileUploadService : ", "drop_img : " + reqFile);
                    break;
                }
                case CommonVariables.URLRQ_TRIP_PAYMENT_COMPLETED: {
                    if(ll_amount_paid_container.getVisibility() == View.VISIBLE)
                    {
                        closed_amount = RequestBody.create(MediaType.parse("text/plain"), "0");
                    }
                    else
                    {
                        closed_amount = RequestBody.create(MediaType.parse("text/plain"), closedAmount);
                    }
                    break;
                }
            }

            Log.e("calFileUploadService : ", "action_type : " + tripStatus +
                    ", pickup_km : " + startingTravelledKm +
                    ", drop_km : " + endingTravelledKm +
                    ", et_halt : " + et_halt.getText().toString() +
                    ", et_hill : " + et_hill.getText().toString() +
                    ", toll :100 " +
                    ", et_batta : " + et_batta.getText().toString() +
                    ", et_penalty_amount : " + et_penalty_amount.getText().toString() +
                    ", penaltyReason : " + penaltyReason +
                    ", closedAmount : " + closedAmount +
                    ", reqFile : " + reqFile);

            Call<FileUploadOutput> call = apiInterface.callFileUpload("",
                    pickup_img,
                    drop_img,
                    trip_id,
                    driver_id,
                    action_type,
                    pickup_km,
                    drop_km,
                    halt,
                    hill,
                    toll,
                    batta,
                    penalty,
                    penalty_reason,
                    closed_amount);

            call.enqueue(new Callback<FileUploadOutput>() {
                @Override
                public void onResponse(Call<FileUploadOutput> call, Response<FileUploadOutput> response) {

                    commonFunctions.cancelLoadingProgress(progress_dialog);

                    try {
                        JSONObject jo_response = new JSONObject(new GsonBuilder().setPrettyPrinting().create().toJson(response));

                        Log.e("Trip Action Response : ", jo_response.toString());

                        if (String.valueOf(jo_response.getJSONObject("rawResponse").getInt("code")).equalsIgnoreCase(CommonVariables.SERVICE_RESPONSE_CODE_200)) {
                            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();

                            if (commonFunctions.checkNetConnectivity(context)) {
                                progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);

                                sv_on_going_trip_cards.fullScroll(ScrollView.FOCUS_UP);

                                new AsyncTaskForHttpServiceOngoingTripDetailsClass().execute(ServiceUrlVariables.TRIP_ONGOING_DETAILS);
                            } else {
                                Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.e("Trip-Action", "JSONException : " + e.getMessage());

                        Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FileUploadOutput> call, Throwable t) {
                    commonFunctions.cancelLoadingProgress(progress_dialog);
                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceAddFeedbackClass extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            JSONObject jo_mobile_no_validate_request = new JSONObject();
            JSONObject jo_header = new JSONObject();

            try {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_ADD_FEEDBACK);

                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID, commonFunctions.getSharedPrefString(context, CommonVariables.SP_DRIVER_ID));
                jo_mobile_no_validate_request.put(CommonVariables.URL_TRIP_ID, tripId);
                jo_mobile_no_validate_request.put("rating", rating);
                jo_mobile_no_validate_request.put("feedback", feedback);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String data = jo_mobile_no_validate_request.toString();

            assert data != null;

            Log.e("data", data + "");

            return commonFunctions.send_http_request_func(params[0],
                    data,
                    CommonVariables.NO_HEADER,
                    CommonVariables.POST,
                    CommonVariables.JSON,
                    CommonVariables.BLANK_STRING,
                    jo_header.toString());

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            commonFunctions.cancelLoadingProgress(progress_dialog);

            try {
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceAddFeedbackClass : " + result);

                if (result != null) {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            JSONObject jo_response_data = jo_mobile_no_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);

                            if (jo_response_data.getBoolean("Status")) {
                                Toast.makeText(context, "Rating Added Successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(context, DashboardActivity.class));
                            }
                        } else {
                            Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void afterGrantedPermissions() {
        if (camera_selected) {
            selectProfileImageFromCamera();
        } else {
            selectProfileImageFromGallery();
        }
    }


    /**
     * > Overriding onAnimationRepeat Method <
     */
    public void selectProfileImageFromGallery() {

        Intent pick_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pick_intent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT), "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pick_intent});

        startActivityForResult(chooserIntent, GALLERY_REQUEST);
    }


    public void selectProfileImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(Intent.createChooser(intent, SELECT_PICTURE), CAMERA_REQUEST);
    }


    /**
     * > Calling Service Method If Network Is Available Or Show No Network Message <
     */
    private void callService() {
        if (commonFunctions.checkNetConnectivity(context)) {
            progress_dialog = commonFunctions.setLoadingProgress(context, CommonVariables.MSG_LOADING_DOTS);

            new AsyncTaskForHttpServiceOngoingTripDetailsClass().execute(ServiceUrlVariables.TRIP_ONGOING_DETAILS);
        } else {
            Toast.makeText(context, CommonVariables.MSG_NETWORK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Log.e("position : ", position + "");
        Log.e("statusListArr : ", statusListArr[position]);
        tripStatus = statusListArr[position];

        ll_reason_for_cancel_container.setVisibility(View.GONE);

        if (tripStatus.equals(CommonVariables.URLRQ_TRIP_CANCELLED)) {
            ll_reason_for_cancel_container.setVisibility(View.VISIBLE);
        }

        enableDisableButtons();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceOngoingTripDetailsClass extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            JSONObject jo_mobile_no_validate_request = new JSONObject();
            JSONObject jo_header = new JSONObject();

            try {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_ONGOING_TRIP);

                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID, commonFunctions.getSharedPrefString(context, CommonVariables.SP_DRIVER_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String data = jo_mobile_no_validate_request.toString();

            assert data != null;

            Log.e("data", data + "");

            return commonFunctions.send_http_request_func(params[0],
                    data,
                    CommonVariables.NO_HEADER,
                    CommonVariables.POST,
                    CommonVariables.JSON,
                    CommonVariables.BLANK_STRING,
                    jo_header.toString());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            commonFunctions.cancelLoadingProgress(progress_dialog);

            try {
                Log.e("onPostExecute of ", "AsyncTaskForHttpServiceOngoingTripDetailsClass : " + result);

                if (result != null) {
                    if (CommonVariables.SERVICE_RESPONSE_CODE_404.equals(result)) {
                        Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jo_mobile_no_response = new JSONObject(result);

                        if (CommonVariables.URLRQ_SUCCESS.equals(jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))) {
                            JSONObject jo_response_data = jo_mobile_no_response.getJSONObject(CommonVariables.URLRS_RESPONSE_DATA);

                            JSONObject jo_passenger_info = jo_response_data.getJSONObject("passenger_info");
                            JSONObject jo_trip_info = jo_response_data.getJSONObject("trip_info");
                            JSONObject jo_payment_info = jo_response_data.getJSONObject("payment_info");
                            JSONObject jo_vehicle_info = jo_response_data.getJSONObject("vehicle_info");

                            tripId = String.valueOf(jo_trip_info.getInt("tripId"));

                            tv_trip_id.setText(tripId);

                            tv_passenger_name.setText(jo_passenger_info.getString("passengerName"));
                            tv_mobile_number.setText(jo_passenger_info.getString("passengerMobileNum"));
                            tv_passenger_location.setText(jo_passenger_info.getString("passengerPickUpLocation"));

                            tv_trip_type.setText(jo_trip_info.getString("tripTypeTxt"));
                            tv_start_date.setText(jo_trip_info.getString("startDate"));
                            tv_start_time.setText(jo_trip_info.getString("startTime"));
                            tv_end_date.setText(jo_trip_info.getString("endDate"));

                            JSONArray arrJsonTxt = jo_response_data.getJSONArray("statusTxtList");
                            JSONArray arrJson = jo_response_data.getJSONArray("statusList");

                            statusTxtListArr = new String[arrJsonTxt.length() + 1];
                            statusListArr = new String[arrJson.length() + 1];

                            statusTxtListArr[0] = "Change Trip Status";
                            statusListArr[0] = CommonVariables.CHANGE_TRIP_STATUS;

                            Log.e("statusTxtListArr 0 : ", statusTxtListArr[0]);

                            for (int i = 1; i <= arrJson.length(); i++) {
                                statusTxtListArr[i] = arrJsonTxt.getString(i - 1);
                                statusListArr[i] = arrJson.getString(i - 1);
                            }

                            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, statusTxtListArr);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_change_trip_status.setAdapter(aa);

                            tripStatus = jo_trip_info.getString("tripStatus");
                            tripStatusFromResponse = tripStatus;

                            //tripStatus = statusListArr[0];

                            et_cash_payment_amount.setText("");

                            tv_expected_amount.setText(jo_payment_info.getString("exp_amount"));
                            tv_advance_paid_amount.setText(jo_payment_info.getString("advance"));
                            tv_balance_amount.setText(jo_payment_info.getString("balance"));

                            grand_total_to_be_paid = Double.parseDouble(jo_payment_info.getString("travelled_balance"));
                            travelled_balance_amount = grand_total_to_be_paid;

                            ll_amount_container.setVisibility(View.VISIBLE);
                            ll_amount_paid_container.setVisibility(View.GONE);
                            tv_grand_total_amount.setText(dfCurrency.format(Float.parseFloat(String.valueOf(grand_total_to_be_paid))));

                            if ((grand_total_to_be_paid == 0) && (Double.parseDouble(jo_payment_info.getString("travelled_amount")) != 0)) {
                                ll_amount_container.setVisibility(View.GONE);
                                ll_amount_paid_container.setVisibility(View.VISIBLE);
                            }

                            basicHaltAmount = jo_vehicle_info.getDouble("haltCharge");
                            basicHillAmount = jo_vehicle_info.getDouble("hillCharge");
                            basicBattaAmount = jo_vehicle_info.getDouble("battaCharge");

                            tv_online_payment_amount.setText(jo_payment_info.getString("online_payment"));

                            Log.e("tripStatus : ", tripStatus);

                            switch (tripStatus) {
                                case CommonVariables.URLRQ_TRIP_READY_TO_PICK: {
                                    et_halt.setEnabled(false);
                                    et_hill.setEnabled(false);
                                    et_batta.setEnabled(false);
                                    et_penalty_amount.setEnabled(false);
                                    et_penalty_reason.setEnabled(false);
                                    et_cash_payment_amount.setEnabled(false);
                                    et_ending_km.setEnabled(false);

                                    ll_driver_manual_charging.setVisibility(View.GONE);
                                    break;
                                }
                                case CommonVariables.URLRQ_TRIP_STARTED: {
                                    et_halt.setEnabled(true);
                                    et_hill.setEnabled(true);
                                    et_batta.setEnabled(true);
                                    et_penalty_amount.setEnabled(true);
                                    et_penalty_reason.setEnabled(true);
                                    et_cash_payment_amount.setEnabled(false);
                                    et_ending_km.setEnabled(true);

                                    ll_driver_manual_charging.setVisibility(View.VISIBLE);

                                    startingTravelledKm = jo_trip_info.getString("startingTravelledKm");
                                    startingImgUrl = jo_trip_info.getString("startingImgUrl");
                                    et_starting_km.setText(String.valueOf(startingTravelledKm + " Km"));
                                    et_starting_km.setEnabled(false);

                                    Glide.with(context).load(startingImgUrl).into(iv_starting_km);

                                    tempSavedKmImagePath = null;
                                    break;
                                }
                                case CommonVariables.URLRQ_TRIP_COMPLETED:
                                case CommonVariables.URLRQ_TRIP_CANCELLED: {
                                    et_halt.setEnabled(false);
                                    et_hill.setEnabled(false);
                                    et_batta.setEnabled(false);
                                    et_penalty_amount.setEnabled(false);
                                    et_penalty_reason.setEnabled(false);

                                    if(ll_amount_paid_container.getVisibility() == View.VISIBLE)
                                    {
                                        et_cash_payment_amount.setEnabled(false);
                                        et_cash_payment_amount.setHint("NIL");
                                    }
                                    else
                                    {
                                        et_cash_payment_amount.setEnabled(true);
                                        et_cash_payment_amount.setHint("0.00");
                                    }

                                    startingImgUrl = jo_trip_info.getString("startingImgUrl");
                                    endingImgUrl = jo_trip_info.getString("endingImgUrl");

                                    startingTravelledKm = jo_trip_info.getString("startingTravelledKm");
                                    endingTravelledKm = jo_trip_info.getString("endingTravelledKm");

                                    et_starting_km.setText(String.valueOf(startingTravelledKm + " Km"));
                                    et_ending_km.setText(String.valueOf(endingTravelledKm + " Km"));

                                    et_starting_km.setEnabled(false);
                                    et_ending_km.setEnabled(false);

                                    tv_distance_travelled.setText(String.valueOf(jo_trip_info.getDouble("travelledKm") + " Km"));

                                    tv_end_time.setText(jo_trip_info.getString("endTime"));

                                    tv_travelled_amount.setText(jo_payment_info.getString("travelled_amount"));
                                    tv_travelled_balance_amount.setText(jo_payment_info.getString("travelled_balance"));

                                    grand_total_to_be_paid = Double.parseDouble(jo_payment_info.getString("travelled_balance"));

                                    //tv_grand_total_amount.setText(dfCurrency.format(Float.parseFloat(String.valueOf(grand_total_to_be_paid))));

                                    Glide.with(context).load(jo_trip_info.getString("startingImgUrl")).into(iv_starting_km);
                                    Glide.with(context).load(jo_trip_info.getString("endingImgUrl")).into(iv_ending_km);

                                    tempSavedKmImagePath = null;

                                    btn_capture.setVisibility(View.GONE);
                                    btn_gallery.setVisibility(View.GONE);

                                    ll_online_payment_container.setVisibility(View.VISIBLE);
                                    ll_cash_payment_container.setVisibility(View.VISIBLE);
                                    ll_driver_manual_charging.setVisibility(View.GONE);
                                    break;
                                }
                                case CommonVariables.URLRQ_TRIP_PAYMENT_COMPLETED: {
                                    et_halt.setEnabled(false);
                                    et_hill.setEnabled(false);
                                    et_batta.setEnabled(false);
                                    et_penalty_amount.setEnabled(false);
                                    et_penalty_reason.setEnabled(false);
                                    et_cash_payment_amount.setEnabled(false);

                                    rl_glass_whole_container.setVisibility(View.VISIBLE);
                                    cv_feedback.setVisibility(View.VISIBLE);
                                    ll_driver_manual_charging.setVisibility(View.GONE);
                                    break;
                                }
                            }

                            if (jo_trip_info.getString("tripTypeTxt").equalsIgnoreCase(CommonVariables.TRIP_TYPE_AIRPORT_TRIP)) {
                                cl_airport_trip_location.setVisibility(View.VISIBLE);
                                cl_rental_pack.setVisibility(View.GONE);
                                ll_location_container.setVisibility(View.GONE);
                                iv_car_center.setBackgroundResource(R.drawable.aeroplane);

                                tv_airport_hour_pack.setText(jo_trip_info.getString("package"));

                                if (jo_response_data.getJSONArray("locationList").length() > 0) {
                                    tv_airport_location.setText("");
                                    tv_airport_location.setText(jo_response_data.getJSONArray("locationList").getString(0));
                                }
                            } else if (jo_trip_info.getString("tripTypeTxt").equalsIgnoreCase(CommonVariables.TRIP_TYPE_RENTAL_TRIP)) {
                                cl_rental_pack.setVisibility(View.VISIBLE);
                                cl_airport_trip_location.setVisibility(View.GONE);
                                ll_location_container.setVisibility(View.GONE);
                                iv_car_center.setBackgroundResource(R.drawable.ic_car_yellow);

                                //tv_pickup_location_label.setVisibility(View.GONE);
                                //tv_pickup_location.setVisibility(View.GONE);
                                //view_line_6.setVisibility(View.GONE);

                                tv_hours_pickup_location.setText(jo_passenger_info.getString("passengerPickUpLocation"));
                                tv_hour_pack.setText(jo_trip_info.getString("package"));

//                                if (jo_response_data.getJSONArray("hoursPack").length() > 0) {
//                                    tv_hour_pack.setText(jo_response_data.getJSONArray("hoursPack").getJSONObject(0).getString("packName"));
//                                }
                            } else if (jo_trip_info.getString("tripTypeTxt").equalsIgnoreCase(CommonVariables.TRIP_TYPE_ONE_WAY_TRIP) ||
                                    jo_trip_info.getString("tripTypeTxt").equalsIgnoreCase(CommonVariables.TRIP_TYPE_ROUND_TRIP)) {
                                cl_rental_pack.setVisibility(View.GONE);
                                cl_airport_trip_location.setVisibility(View.GONE);
                                ll_location_container.setVisibility(View.VISIBLE);
                                iv_car_center.setBackgroundResource(R.drawable.ic_car_yellow);

                                if (jo_response_data.getJSONArray("locationList").length() > 0) {
                                    ll_location_container.removeAllViews();
                                    for (int i = 0; i < jo_response_data.getJSONArray("locationList").length(); i++) {
                                        int si = i + 1;
                                        TextView text = new TextView(context);
                                        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        text.setText(si + ". " + jo_response_data.getJSONArray("locationList").getString(i));
                                        text.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        text.setTextSize(16);
                                        //or to support all versions use
                                        Typeface typeface = ResourcesCompat.getFont(context, R.font.quicksand_medium);
                                        text.setTypeface(typeface);
                                        text.setPadding(5, 0, 5, 0);
                                        ll_location_container.addView(text);
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();

                Log.e("OnGoing Exception : ", e.getMessage());

                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void enableDisableButtons() {
        switch (tripStatus) {
            case CommonVariables.URLRQ_TRIP_STARTED: {
                et_halt.setEnabled(false);
                et_hill.setEnabled(false);
                et_batta.setEnabled(false);
                et_penalty_amount.setEnabled(false);
                et_penalty_reason.setEnabled(false);
                et_cash_payment_amount.setEnabled(false);
                et_starting_km.setEnabled(true);
                et_ending_km.setEnabled(false);
                break;
            }
            case CommonVariables.URLRQ_TRIP_COMPLETED: {
                et_halt.setEnabled(true);
                et_hill.setEnabled(true);
                et_batta.setEnabled(true);
                et_penalty_amount.setEnabled(true);
                et_penalty_reason.setEnabled(true);
                et_cash_payment_amount.setEnabled(false);
                et_starting_km.setEnabled(false);
                et_ending_km.setEnabled(true);
                break;
            }
            case CommonVariables.URLRQ_TRIP_CANCELLED: {
                et_halt.setEnabled(true);
                et_hill.setEnabled(true);
                et_batta.setEnabled(true);
                et_penalty_amount.setEnabled(true);
                et_penalty_reason.setEnabled(true);
                et_cash_payment_amount.setEnabled(false);
                et_reason_for_cancel.setEnabled(true);
                et_starting_km.setEnabled(false);
                et_ending_km.setEnabled(true);
                break;
            }
            case CommonVariables.URLRQ_TRIP_PAYMENT_COMPLETED: {
                et_halt.setEnabled(false);
                et_hill.setEnabled(false);
                et_batta.setEnabled(false);
                et_penalty_amount.setEnabled(false);
                et_penalty_reason.setEnabled(false);

                if(ll_amount_paid_container.getVisibility() == View.VISIBLE)
                {
                    et_cash_payment_amount.setEnabled(false);
                    et_cash_payment_amount.setHint("NIL");
                }
                else
                {
                    et_cash_payment_amount.setEnabled(true);
                    et_cash_payment_amount.setHint("0.00");
                }

                et_starting_km.setEnabled(false);
                et_ending_km.setEnabled(false);
                break;
            }
            case CommonVariables.CHANGE_TRIP_STATUS: {
                et_halt.setEnabled(false);
                et_hill.setEnabled(false);
                et_batta.setEnabled(false);
                et_penalty_amount.setEnabled(false);
                et_penalty_reason.setEnabled(false);
                et_cash_payment_amount.setEnabled(false);
                et_starting_km.setEnabled(false);
                et_ending_km.setEnabled(false);
                et_reason_for_cancel.setEnabled(false);
                break;
            }
        }
    }


    /**
     * > Generate New Alert Dialog Builder <
     */
    private void generateAlertDialogBuilder(String title, String msg, int from) {
        alert_dialog_builder = new AlertDialog.Builder(context);

        alert_dialog_builder.setTitle(title);
        alert_dialog_builder.setMessage(msg);
        alert_dialog_builder.setCancelable(false);

        alert_dialog_builder.setPositiveButton(CommonVariables.YES, (dialog, which) -> {
            Log.e("alert_dialog_builder : ", "YES");
            switch (from) {
                case 0: // Click on Upload Button
                {
                    calFileUploadService();
                    break;
                }
                case 1:// Back button pressed
                {
                    startActivity(new Intent(context, DashboardActivity.class));
                    break;
                }
            }
        });

        alert_dialog_builder.setNegativeButton(CommonVariables.NO, (dialog, which) -> {
            Log.e("alert_dialog_builder : ", "NO");
            alert_dialog.dismiss();
        });

        alert_dialog = alert_dialog_builder.create();

        alert_dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("xy : ", requestCode + "");

        if (requestCode == PERMISSION_REQUEST_CODE) {
            Log.e("xy permission : ", "onRequestPermissionsResult");
            boolean gotAllPermitted = false;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    gotAllPermitted = true;
                }
            }

            Log.e("xy gotAllPermitted : ", gotAllPermitted + "");

            if (gotAllPermitted) {
                afterGrantedPermissions();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GALLERY_REQUEST: {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Log.e(TAG, "GALLERY_REQUEST data : " + data);

                    try {
                        Intent crop_intent = new Intent("com.android.camera.action.CROP");
                        crop_intent.setDataAndType(data.getData(), "image");
                        crop_intent.putExtra("crop", "true");
                        crop_intent.putExtra("aspectX", 1);
                        crop_intent.putExtra("aspectY", 1);
                        crop_intent.putExtra("outputX", 256);
                        crop_intent.putExtra("outputY", 256);
                        crop_intent.putExtra("scaleUpIfNeeded", true);
                        crop_intent.putExtra("return-data", true);
                        startActivityForResult(crop_intent, CROP_IMAGE_REQUEST);
                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.e(TAG, "GALLERY_REQUEST Exception : " + e.getMessage());

                        Uri uri = data.getData();

                        String[] projection = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                        assert cursor != null;
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(projection[0]);
                        String picturePath = cursor.getString(columnIndex); // returns null
                        cursor.close();

                        Log.e("picturePath : ", picturePath);

                        file_path_1 = picturePath;

                        if (commonFunctions.pasteBitmapFromOnePathToAnotherFPV(BitmapFactory.decodeFile(file_path_1),
                                file_path_variables.aro_container_folder.getAbsolutePath())) {
                            detectTempSavedKmImage();
                        } else {
                            Log.e(TAG, "Selected file FilePathVariables.EDIT_PROFILE_TEMP_IMAGE does not exists");
                        }
                    }
                } else {
                    // commonFunctions.set_custom_toast_msg(context,CommonVariables.UNABLE_TO_FIND_PATH,toastView);
                    Toast.makeText(context, CommonVariables.UNABLE_TO_FIND_PATH, Toast.LENGTH_SHORT).show();

                    Log.e(TAG, "Selected file bundle is null");
                }
                break;
            }
            case CAMERA_REQUEST: {
                if (RESULT_OK == resultCode) {
                    Log.e(TAG, "CAMERA_REQUEST data : " + data);

                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        Bitmap photo = bundle.getParcelable("data");

                        // Log.e("path2222 : ",file_path_variables.aro_container_folder.getAbsolutePath()+selected_photo);

                        if (commonFunctions.pasteBitmapFromOnePathToAnotherFPV(photo,
                                file_path_variables.aro_container_folder.getAbsolutePath() + selectedPhoto)) {
                            detectTempSavedKmImage();
                        } else {
                            Log.e(TAG, "Selected file FilePathVariables.EDIT_PROFILE_TEMP_IMAGE does not exists");
                        }
                    } else {
                        Log.e(TAG, "Selected file bundle is null");

                        Toast.makeText(context, CommonVariables.UNABLE_TO_CROP, Toast.LENGTH_SHORT).show();
                    }



                    /*Bundle bundle = data.getExtras();

                    if(bundle != null)
                    {
                        Bitmap photo = bundle.getParcelable("data");

                        //Log.e("path1111 : ",file_path_variables.aro_container_folder.getAbsolutePath()+selected_photo);

                        try
                        {

//                            Uri photo_uri = FileProvider.getUriForFile(context,
//                                    BuildConfig.APPLICATION_ID + ".provider",
//                                    new File(file_path_variables.aro_container_folder.getAbsolutePath()+ selectedPhoto));

                            Uri photo_uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    new File(file_path_variables.aro_container_folder.getAbsolutePath() + selectedPhoto));

                            Intent crop_intent = new Intent("com.android.camera.action.CROP");
                            crop_intent.setDataAndType(photo_uri, "image");
                            crop_intent.putExtra("crop","true");
                            crop_intent.putExtra("aspectX", 1);
                            crop_intent.putExtra("aspectY", 1);
                            crop_intent.putExtra("outputX", 256);
                            crop_intent.putExtra("outputY", 256);
                            crop_intent.putExtra("scaleUpIfNeeded", true);
                            crop_intent.putExtra("return-data", true);
                            crop_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(crop_intent, CROP_IMAGE_REQUEST);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();

                            Log.e(TAG, "CAMERA_REQUEST Exception : "+e.getMessage());

                            if(commonFunctions.pasteBitmapFromOnePathToAnotherFPV(photo,
                                    file_path_variables.aro_container_folder.getAbsolutePath()+ selectedPhoto))
                            {
                                detectTempSavedKmImage();
                            }
                            else
                            {
                                Log.e(TAG, "Selected file FilePathVariables.EDIT_PROFILE_TEMP_IMAGE does not exists");
                            }
                        }
                    }*/
                } else {
                    Log.e(TAG, "Selected file bundle is null");

                    Toast.makeText(context, CommonVariables.UNABLE_TO_FIND_IMAGE_PATH, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case CROP_IMAGE_REQUEST: {
                if (RESULT_OK == resultCode) {
                    Log.e(TAG, "CROP_IMAGE_REQUEST data : " + data);

                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        Bitmap photo = bundle.getParcelable("data");

                        // Log.e("path2222 : ",file_path_variables.aro_container_folder.getAbsolutePath()+selected_photo);

                        if (commonFunctions.pasteBitmapFromOnePathToAnotherFPV(photo,
                                file_path_variables.aro_container_folder.getAbsolutePath() + selectedPhoto)) {
                            detectTempSavedKmImage();
                        } else {
                            Log.e(TAG, "Selected file FilePathVariables.EDIT_PROFILE_TEMP_IMAGE does not exists");
                        }
                    } else {
                        Log.e(TAG, "Selected file bundle is null");

                        Toast.makeText(context, CommonVariables.UNABLE_TO_CROP, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }


    /**
     * > Detect If Speedometer Image Exists Or Not
     * If Exists Then Show That Image Or Else Show The Default Image <
     */
    public void detectTempSavedKmImage() {
        if (file_path_variables.aro_container_folder.exists()) // Image Found
        {
            Log.e("detectTempSavedKmImage ", "Image Found");

            tempSavedKmImagePath = file_path_variables.aro_container_folder.getAbsolutePath() + selectedPhoto;
            Bitmap bitmap = BitmapFactory.decodeFile(tempSavedKmImagePath);

            if (tempSavedKmImagePath != null) {
                Log.e("detectTempSavedKmImage ", "tempSavedKmImagePath : " + tempSavedKmImagePath);

                Log.e("detectTempSavedKmImage ", "tripStatusFromResponse : " + tripStatusFromResponse);

                if (tripStatusFromResponse.equals(CommonVariables.URLRQ_TRIP_READY_TO_PICK)) {
                    iv_starting_km.setImageResource(android.R.color.transparent);
                    iv_starting_km.setImageBitmap(bitmap);
                } else if (tripStatusFromResponse.equals(CommonVariables.URLRQ_TRIP_STARTED)) {
                    iv_ending_km.setImageResource(android.R.color.transparent);
                    iv_ending_km.setImageBitmap(bitmap);
                }
            } else {
                Log.e("detectTempSavedKmImage ", "tempSavedKmImagePath is null");
            }
        } else {
            Log.e("detectTempSavedKmImage ", "Image Not Found");
        }
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onBackPressed() {
        generateAlertDialogBuilder("Wait", "Sure to go back!", 1);
    }
}
