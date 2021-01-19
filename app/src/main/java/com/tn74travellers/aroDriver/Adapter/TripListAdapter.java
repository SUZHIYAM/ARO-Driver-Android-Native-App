package com.tn74travellers.aroDriver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Common.RecyclerTouchListener;
import com.tn74travellers.aroDriver.Main.DashboardActivity;
import com.tn74travellers.aroDriver.Main.JourneyDetailsActivity;
import com.tn74travellers.aroDriver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.MyViewHolder>
{
    private static final String TAG = TripListAdapter.class.getSimpleName();

    Context context;

    View view;

    String screen_name;

    JSONArray ja_main_item_list_container;

    JSONObject jo_main_item_list_container;

    RecyclerView rv_main_item_list_container;

    int current_position = -1;

    CommonFunctions commonFunctions = new CommonFunctions();

    public TripListAdapter(Context context, String screen_name, JSONArray ja_item_list_container, RecyclerView rv_item_list_container) {
        this.context = context;
        this.screen_name = screen_name;

        ja_main_item_list_container = ja_item_list_container;
        rv_main_item_list_container = rv_item_list_container;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(screen_name) {
            case CommonVariables.CASE_NEW_TRIP_LIST:
            case CommonVariables.CASE_EVENT_TRIP_LIST:
            {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_trip_list_item, parent, false);
                break;
            }
        }

        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        rv_main_item_list_container.addOnItemTouchListener(new RecyclerTouchListener(context, (view, position1) -> current_position = position1));

        switch(screen_name) {
            case CommonVariables.CASE_NEW_TRIP_LIST:
            {
                try
                {
                    jo_main_item_list_container = ja_main_item_list_container.getJSONObject(position);

                    holder.tv_trip_type.setText(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE));
                    holder.tv_trip_date.setText(jo_main_item_list_container.getString("tripDate"));
                    holder.tv_trip_time.setText(jo_main_item_list_container.getString("tripTime"));
                    holder.tv_customer_name.setText(jo_main_item_list_container.getString("passengerName"));
                    holder.tv_trip_id.setText(String.valueOf("Trip ID : " + jo_main_item_list_container.getInt("tripId")));

                    if(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_AIRPORT_TRIP))
                    {
                        holder.cl_airport_trip_location.setVisibility(View.VISIBLE);
                        holder.cl_rental_pack.setVisibility(View.GONE);
                        holder.ll_location_container.setVisibility(View.GONE);
                        holder.iv_car_icon.setBackgroundResource(R.drawable.aeroplane);
                        holder.tv_trip_date_label.setText("Flight Date");
                        holder.tv_trip_time_label.setText("Flight Time");

                        holder.tv_airport_hour_pack.setText(String.valueOf(jo_main_item_list_container.getInt("package")));

                        if(jo_main_item_list_container.getJSONArray("locationList").length()>0)
                        {
                            holder.tv_airport_location.setText("");
                            holder.tv_airport_location.setText(jo_main_item_list_container.getJSONArray("locationList").getString(0));
                        }
                    }
                    else if(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_RENTAL_TRIP))
                    {
                        holder.cl_rental_pack.setVisibility(View.VISIBLE);
                        holder.cl_airport_trip_location.setVisibility(View.GONE);
                        holder.ll_location_container.setVisibility(View.GONE);
                        holder.iv_car_icon.setBackgroundResource(R.drawable.ic_car_yellow);
                        holder.tv_trip_date_label.setText("Trip Date");
                        holder.tv_trip_time_label.setText("Trip Time");

                        holder.tv_hours_pickup_location.setText(jo_main_item_list_container.getString("passengerPickUpLocation"));

                        holder.tv_hour_pack.setText(String.valueOf(jo_main_item_list_container.getInt("package")));

//                        if(jo_main_item_list_container.getJSONArray("hoursPack").length()>0)
//                        {
//                            holder.tv_hour_pack.setText(jo_main_item_list_container.getJSONArray("hoursPack").getJSONObject(0).getString("packName"));
//                        }
                    }
                    else if(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ONE_WAY_TRIP) ||
                            jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ROUND_TRIP))
                    {
                        holder.cl_rental_pack.setVisibility(View.GONE);
                        holder.cl_airport_trip_location.setVisibility(View.GONE);
                        holder.ll_location_container.setVisibility(View.VISIBLE);
                        holder.iv_car_icon.setBackgroundResource(R.drawable.ic_car_yellow);
                        holder.tv_trip_date_label.setText("Trip Date");
                        holder.tv_trip_time_label.setText("Trip Time");

                        if(jo_main_item_list_container.getJSONArray("locationList").length()>0)
                        {
                            holder.ll_location_container.removeAllViews();
                            for(int i=0;i<jo_main_item_list_container.getJSONArray("locationList").length();i++)
                            {
                                int si = i+1;
                                TextView text = new TextView(context);
                                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                text.setText(String.valueOf(si+". "+jo_main_item_list_container.getJSONArray("locationList").getString(i)));
                                Log.e("locationList : ", jo_main_item_list_container.getJSONArray("locationList").getString(i));
                                text.setTextColor(context.getColor(R.color.colorPrimary));
                                text.setTextSize(16);
                                //or to support all versions use
                                Typeface typeface = ResourcesCompat.getFont(context, R.font.quicksand_medium);
                                text.setTypeface(typeface);
                                text.setPadding(5, 0, 5, 0);
                                holder.ll_location_container.addView(text);
                            }
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("JSONException ", "CASE_NEW_TRIP_LIST : "+e.getMessage());
                }

                holder.tv_accept.setOnClickListener(view -> {
                    if(context instanceof DashboardActivity)
                    {
                        try {
                            ((DashboardActivity) context).tripAcceptFunc(ja_main_item_list_container.getJSONObject(current_position));
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("JSONException ", "CASE_NEW_TRIP_LIST to DashboardActivity: "+e.getMessage());
                        }
                    }
                });

                holder.iv_right_arrow.setVisibility(View.GONE);

                break;
            }
            case CommonVariables.CASE_EVENT_TRIP_LIST:
            {
                try
                {
                    jo_main_item_list_container = ja_main_item_list_container.getJSONObject(position);

                    holder.tv_trip_type.setText(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE));
                    holder.tv_trip_date.setText(jo_main_item_list_container.getString("startDate"));
                    holder.tv_trip_time.setText(jo_main_item_list_container.getString("startTime"));
                    holder.tv_customer_name.setText(jo_main_item_list_container.getString("passengerName"));
                    holder.tv_trip_id.setText(String.valueOf("Trip ID : " + jo_main_item_list_container.getInt("tripId")));

                    if(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_AIRPORT_TRIP))
                    {
                        holder.cl_airport_trip_location.setVisibility(View.VISIBLE);
                        holder.cl_rental_pack.setVisibility(View.GONE);
                        holder.ll_location_container.setVisibility(View.GONE);
                        holder.iv_car_icon.setBackgroundResource(R.drawable.aeroplane);
                        holder.tv_trip_date_label.setText("Flight Date");
                        holder.tv_trip_time_label.setText("Flight Time");

                        holder.tv_airport_hour_pack.setText(String.valueOf(jo_main_item_list_container.getInt("package")));

                        if(jo_main_item_list_container.getJSONArray("locationList").length()>0)
                        {
                            holder.tv_airport_location.setText("");
                            holder.tv_airport_location.setText(jo_main_item_list_container.getJSONArray("locationList").getString(0));
                        }
                    }

                    if(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_RENTAL_TRIP))
                    {
                        holder.cl_rental_pack.setVisibility(View.VISIBLE);
                        holder.cl_airport_trip_location.setVisibility(View.GONE);
                        holder.ll_location_container.setVisibility(View.GONE);
                        holder.iv_car_icon.setBackgroundResource(R.drawable.ic_car_yellow);
                        holder.tv_trip_date_label.setText("Trip Date");
                        holder.tv_trip_time_label.setText("Trip Time");

                        holder.tv_hours_pickup_location.setText(jo_main_item_list_container.getString("passengerPickUpLocation"));

                        holder.tv_hour_pack.setText(String.valueOf(jo_main_item_list_container.getInt("package")));

//                        if(jo_main_item_list_container.getJSONArray("hoursPack").length()>0)
//                        {
//                            holder.tv_hour_pack.setText(jo_main_item_list_container.getJSONArray("hoursPack").getJSONObject(0).getString("packName"));
//                        }
                    }

                    if(jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ONE_WAY_TRIP) || jo_main_item_list_container.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ROUND_TRIP))
                    {
                        holder.cl_rental_pack.setVisibility(View.GONE);
                        holder.cl_airport_trip_location.setVisibility(View.GONE);
                        holder.ll_location_container.setVisibility(View.VISIBLE);
                        holder.iv_car_icon.setBackgroundResource(R.drawable.ic_car_yellow);
                        holder.tv_trip_date_label.setText("Trip Date");
                        holder.tv_trip_time_label.setText("Trip Time");

                        if(jo_main_item_list_container.getJSONArray("locationList").length()>0)
                        {
                            holder.ll_location_container.removeAllViews();
                            for(int i=0;i<jo_main_item_list_container.getJSONArray("locationList").length();i++)
                            {
                                int si = i+1;
                                TextView text = new TextView(context);
                                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                text.setText(String.valueOf(si+". "+jo_main_item_list_container.getJSONArray("locationList").getString(i)));
                                text.setTextColor(context.getColor(R.color.colorPrimary));
                                text.setTextSize(16);
                                //or to support all versions use
                                Typeface typeface = ResourcesCompat.getFont(context, R.font.quicksand_medium);
                                text.setTypeface(typeface);
                                text.setPadding(5, 0, 5, 0);
                                holder.ll_location_container.addView(text);
                            }
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("JSONException ", "CASE_EVENT_TRIP_LIST : "+e.getMessage());
                }

               holder.tv_accept.setVisibility(View.GONE);

                holder.rl_right_arrow.setOnClickListener(view -> {
                    try {
                        commonFunctions.setSharedPrefString(context,"journey_details",ja_main_item_list_container.getJSONObject(position).toString());
                        context.startActivity(new Intent(context, JourneyDetailsActivity.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException ", "CASE_EVENT_TRIP_LIST to JourneyDetailsActivity : "+e.getMessage());
                    }
                });

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return ja_main_item_list_container.length();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        /************ Trip List ***************/

        TextView tv_trip_type;
        TextView tv_trip_date;
        TextView tv_trip_time;
        TextView tv_customer_name;
        TextView tv_accept;
        TextView tv_airport_location;
        TextView tv_hours_pickup_location;
        TextView tv_hour_pack;
        TextView tv_airport_hour_pack;
        TextView tv_trip_id;

        ConstraintLayout cl_airport_trip_location;
        ConstraintLayout cl_rental_pack;

        LinearLayout ll_location_container;

        TextView tv_trip_date_label;
        TextView tv_trip_time_label;

        RelativeLayout rl_right_arrow;

        ImageView iv_car_icon;
        ImageView iv_right_arrow;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            /* ---------- Trip List ---------*/

            tv_trip_type = itemView.findViewById(R.id.tv_trip_type);
            tv_trip_date = itemView.findViewById(R.id.tv_trip_date);
            tv_trip_time = itemView.findViewById(R.id.tv_trip_time);
            tv_customer_name = itemView.findViewById(R.id.tv_customer_name);
            tv_accept = itemView.findViewById(R.id.tv_accept);
            tv_airport_location = itemView.findViewById(R.id.tv_airport_location);
            tv_hours_pickup_location = itemView.findViewById(R.id.tv_hours_pickup_location);
            tv_hour_pack = itemView.findViewById(R.id.tv_hour_pack);
            tv_airport_hour_pack = itemView.findViewById(R.id.tv_airport_hour_pack);
            tv_trip_date_label = itemView.findViewById(R.id.tv_trip_date_label);
            tv_trip_time_label = itemView.findViewById(R.id.tv_trip_time_label);
            tv_trip_id = itemView.findViewById(R.id.tv_trip_id);

            cl_airport_trip_location = itemView.findViewById(R.id.cl_airport_trip_location);
            cl_rental_pack = itemView.findViewById(R.id.cl_rental_pack);

            ll_location_container = itemView.findViewById(R.id.ll_location_container);

            iv_car_icon = itemView.findViewById(R.id.iv_car_icon);
            iv_right_arrow = itemView.findViewById(R.id.iv_right_arrow);

            rl_right_arrow = itemView.findViewById(R.id.rl_right_arrow);
        }
    }
}
