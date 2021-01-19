package com.tn74travellers.aroDriver.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tn74travellers.aroDriver.Adapter.TripListAdapter;
import com.tn74travellers.aroDriver.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CommonFunctions
{
    /**> Setting Values to Universal Recycler View Adapter And Adapting To Current Recycler View <*/
    public void setToRecyclerViewAdapter(Context context, String screen_name, JSONArray ja_item_list_container, RecyclerView rv_item_list_container)
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

        rv_item_list_container.setLayoutManager(layoutManager);

        rv_item_list_container.setAdapter(new TripListAdapter(context, screen_name, ja_item_list_container, rv_item_list_container));
    }

    /**> Setting Values to Universal Recycler View Adapter And Adapting To Current Recycler View <*/
//    public void set_to_product_list_model_recycler_view_adapter_func(Context context, String screen_name, List<ProductDetailsModel> productListArr, RecyclerView rv_item_list_container)
//    {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//
//        rv_item_list_container.setLayoutManager(layoutManager);
//
//        rv_item_list_container.setAdapter(new FeaturedBestSellerProductListAdapter(context, screen_name, productListArr, rv_item_list_container));
//    }


    /**> Setting Values to Universal Recycler View Adapter And Adapting To Current Recycler View With Grid View <*/
//    public void set_to_recycler_view_adapter_grid_view_func(Context context, String screen_name, List<CategoryModel> ja_item_list_container, RecyclerView rv_item_list_container)
//    {
//        CategoryAdapter common_recycler_view_list_Items_adapter = new CategoryAdapter(context, screen_name, ja_item_list_container, rv_item_list_container);
//        rv_item_list_container.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
//        rv_item_list_container.setLayoutManager(layoutManager);
//        rv_item_list_container.setItemAnimator(new DefaultItemAnimator());
//        rv_item_list_container.setAdapter(common_recycler_view_list_Items_adapter);
//    }
//

    /**> Setting Integer Values In Shared Preferences Storage <*/
    public void set_shared_pref_int_func(Context context, String key_name, int value)
    {
        SharedPreferences sp             = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sp_edit = sp.edit();
        sp_edit.putInt(key_name, value);
        sp_edit.apply();
    }


    /**> Retrieving Integer Values From Shared Preferences Storage <*/
    public int get_shared_pref_int_func(Context context, String key_name)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key_name, 0);
    }



    /**> Setting Boolean Values In Shared Preferences Storage <*/
    public void setSharedPrefBoolean(Context context, String key_name, boolean value)
    {
        SharedPreferences sp             = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sp_edit = sp.edit();
        sp_edit.putBoolean(key_name, value);
        sp_edit.apply();
    }



    /**> Retrieving Boolean Values From Shared Preferences Storage <*/
    public boolean getSharedPrefBoolean(Context context, String key_name)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key_name, false);
    }


    /**> Setting String Values In Shared Preferences Storage <*/
    public void setSharedPrefString(Context context, String key_name, String value)
    {
        SharedPreferences sp             = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sp_edit = sp.edit();
        sp_edit.putString(key_name, value);
        sp_edit.apply();
    }


    /**> Setting Custom Toast  <*/
    public void set_custom_toast_msg(Context context, String toast_msg, View view)
    {
        // Get the custom layout view.


        // Initiate the Toast instance.
        Toast toast = new Toast(context);
        // Set custom view in toast.
        toast.setView(view);
        TextView text = view.findViewById(R.id.tv_toast_msg);
        text.setText(toast_msg);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0,100);
        toast.show();
    }


    /**> Setting Values and Calling Progress Dialog <*/
    public ProgressDialog setLoadingProgress(Context context, String progress_dialog_msg)
    {
        ProgressDialog loading_progress = new ProgressDialog(context);

        loading_progress.setMessage(progress_dialog_msg);
        loading_progress.setCancelable(false);
        loading_progress.setIndeterminate(false);
        loading_progress.show();
        return loading_progress;
    }



    /**> Cancelling Progress Dialog If It Is Already Showing <*/
    public void cancelLoadingProgress(ProgressDialog loading_progress)
    {
        if(loading_progress != null)
        {
            if(loading_progress.isShowing())
            {
                loading_progress.dismiss();
            }
        }
    }






    /**> Retrieving String Values From Shared Preferences Storage <
     * @return*/
    public String getSharedPrefString(Context context, String key_name)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key_name, null);
    }

    /**> Checking Network Status If Net Available Return True Else False <*/
    public boolean checkNetConnectivity(Context context)
    {
        ConnectivityManager connectivity_manager    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network_info                    = connectivity_manager.getActiveNetworkInfo();

        return (network_info != null && network_info.isConnected());
    }



    /**> Convert Selected Bitmap To File And Make A Copy Then Paste To
     * Given Destination Location Path
     *
     * @param source_bitmap Selected Bitmap From Source Path Location (Copy Bitmap)
     * @param destination_path Given Destination Location Path (Paste File)
     *
     * @return true if pasting file process completes successfully
     * false if any exception or blank path or file
     * not exists or bitmap is null problem raises <*/
    public boolean pasteBitmapFromOnePathToAnotherFPV(Bitmap source_bitmap, String destination_path)
    {
        try
        {
            if(source_bitmap == null)
            {
                //Log.e(TAG, "source_bitmap is null");
                return false;
            }
            else
            {
                /*if(!(new File(destination_path).isFile()))
                {
                Log.e(TAG, "destination_path has no file");
                return false;
                }
                else
                {
                File file = new File(destination_path);
                FileOutputStream fos = new FileOutputStream(file);

                source_bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                Log.e(TAG, "paste bitmap to another location process completed successfully");

                return true;
                }*/

                File file = new File(destination_path);
                FileOutputStream fos = new FileOutputStream(file);

                source_bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                Log.e(TAG, "paste bitmap to another location process completed successfully");

                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            Log.e(TAG, "pasteBitmapFromOnePathToAnotherFPV Exception : "+e.getMessage());

            return false;
        }
    }



    /**> Set Headers And Data And Call Http Request Service <*/
    public String send_http_request_func(String url_path, String data, String check_header,
                                         String get_or_post, String response_type,
                                         String file_path, String jo_header_str)
    {
        InputStream input_stream;
        String response_output = null;

        try
        {
            URL url = new URL(url_path);

            Log.e(TAG, " url_path : "+url_path);

            URLConnection connection    = url.openConnection();

            JSONObject jo_header        = new JSONObject(jo_header_str);

            switch(jo_header.getString(CommonVariables.FROM))
            {
                case CommonVariables.CASE_VERSION_NUMBER:
                case CommonVariables.CASE_LOGIN:
                case CommonVariables.CASE_NEW_TRIP_LIST:
                case CommonVariables.CASE_TRIP_ACCEPT:
                case CommonVariables.CASE_ONGOING_TRIP:
                case CommonVariables.CASE_EVENT_TRIP_LIST:
                case CommonVariables.CASE_TRIP_STATUS_LIST:
                case CommonVariables.CASE_ONGOING_TRIP_CHECK:
                case CommonVariables.CASE_TRIP_DATE_LIST:
                case CommonVariables.CASE_PICK_TRIP:
                case CommonVariables.CASE_TRIP_STATUS_CHANGE:
                case CommonVariables.CASE_GET_IMAGE_URL:
                case CommonVariables.CASE_PAYMENT_STATUS_CHANGE:
                case CommonVariables.CASE_ADD_FEEDBACK:
                case CommonVariables.CASE_GET_PAYMENT_RESPONSE:
                case CommonVariables.CASE_UPDATE_PUSH_NOTI:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    break;
                }

//                case CommonVariables.CASE_GET_USER_INFO:
//                case CommonVariables.CASE_PROFILE_UPDATE:
//                case CommonVariables.CASE_DRIVER_STATUS_UPDATE:
//                case CommonVariables.CASE_MY_LOAD_DETAILS:
//                {
//                    connection.setRequestProperty("Content-Type","application/json");
//                    connection.setRequestProperty("authorization", jo_header.getString("authorization"));
//                    break;
//                }
                /*
                case CommonVariables.CASE_VERSION_NUMBER:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    break;
                }
                case CommonVariables.CASE_CATEGORY:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    break;
                }
                case CommonVariables.CASE_PRODUCTS_ALL_LIST:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    break;
                }
                case CommonVariables.CASE_PRODUCTS_BY_CATEGORY:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    break;
                }
                case CommonVariables.CASE_REGISTER:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    break;
                }
                case CommonVariables.CASE_LOGIN:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    break;
                }
                case CommonVariables.CASE_LOGIN_USER_DATA:
                {
                    Log.e("login_user_data","hi i m mobile_validate user data");
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    connection.setRequestProperty("authorization",jo_header.getString("authorization"));
                    break;
                }
                case CommonVariables.CASE_CHANGE_PASSWORD:
                {

                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    connection.setRequestProperty("authorization", jo_header.getString("authorization"));
                    break;
                }
                case CommonVariables.CASE_FORGOT_PASSWORD:
                {
                    Log.e("Bearer",jo_header.getString("authorization")+"");
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    connection.setRequestProperty("authorization", jo_header.getString("authorization"));
                    break;
                }
                case CommonVariables.CASE_UPDATE_USER_DETAILS:
                {
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("x-api-key","d6fc3b34-a94d-4fa2-85d7-526c452de876");
                    connection.setRequestProperty("authorization", jo_header.getString("authorization"));
                    break;
                }*/
            }

            try
            {
                switch (get_or_post)
                {
                    case CommonVariables.POST:
                    {

                        Log.e(TAG, "get_or_post : "+CommonVariables.POST);
                        Log.e(TAG, "Request data "+data);

                        // Send Post Request

                        connection.setDoOutput(true);
                        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                        wr.writeBytes(data);
                        wr.flush();
                        wr.close();

                        break;
                    }
                }

                switch (check_header)
                {
                    case CommonVariables.HEADER:
                    {
                        Log.e(TAG, "check_header : "+CommonVariables.HEADER);

                        Map<String, List<String>> map = connection.getHeaderFields();

                        Log.e(TAG,"Printing Response Header...");

                        for (Map.Entry<String, List<String>> entry : map.entrySet())
                        {
                            Log.e(TAG, "Key : "+entry.getKey() + " ,Value : " + entry.getValue());
                        }
                        break;
                    }

                    case CommonVariables.NO_HEADER:
                    {
                        Log.e("check_header : ", CommonVariables.NO_HEADER);
                        break;
                    }
                }

                Log.e(TAG, "***********************************");

                input_stream = connection.getInputStream();

                Log.e(TAG, "(((((((((((((((((((((((((((((((((");

                switch (response_type)
                {
                    case CommonVariables.JSON:
                    {
                        Log.e(TAG, "response_type : "+CommonVariables.JSON);
                        response_output = retrieve_http_response_json_func(input_stream);
                        break;
                    }

                    case CommonVariables.FILE:
                    {
                        Log.e(TAG, "response_type : "+CommonVariables.FILE);
                        response_output = retrieve_http_response_file_func(input_stream, file_path);
                        break;
                    }
                }

                return response_output;
            }
            catch (IOException ioe)
            {
                Log.e(TAG, "catch IOException : "+ioe.getMessage());

                if(connection instanceof HttpURLConnection)
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                    switch(jo_header.getString(CommonVariables.FROM))
                    {
                        case CommonVariables.CASE_VERSION_NUMBER:
                        case CommonVariables.CASE_LOGIN:
                        case CommonVariables.CASE_NEW_TRIP_LIST:
                        case CommonVariables.CASE_TRIP_ACCEPT:
                        case CommonVariables.CASE_ONGOING_TRIP:
                        case CommonVariables.CASE_EVENT_TRIP_LIST:
                        case CommonVariables.CASE_TRIP_STATUS_LIST:
                        case CommonVariables.CASE_ONGOING_TRIP_CHECK:
                        case CommonVariables.CASE_TRIP_DATE_LIST:
                        case CommonVariables.CASE_PICK_TRIP:
                        case CommonVariables.CASE_TRIP_STATUS_CHANGE:
                        case CommonVariables.CASE_GET_IMAGE_URL:
                        case CommonVariables.CASE_PAYMENT_STATUS_CHANGE:
                        {
                            httpURLConnection.setRequestProperty("Content-Type","application/json");
                            break;
                        }
//                        case CommonVariables.CASE_GET_USER_INFO:
//                        case CommonVariables.CASE_PROFILE_UPDATE:
//                        case CommonVariables.CASE_DRIVER_STATUS_UPDATE:
//                        case CommonVariables.CASE_MY_LOAD_DETAILS:
//                        {
//                            httpURLConnection.setRequestProperty("Content-Type","application/json");
//                            httpURLConnection.setRequestProperty("authorization", jo_header.getString("authorization"));
//                            break;
//                        }
                    }

                    switch (get_or_post)
                    {
                        case CommonVariables.GET:
                        {
                            Log.e(TAG, "catch get_or_post : "+CommonVariables.GET);
                            //httpURLConnection.setRequestMethod(CommonVariables.GET);
                            break;
                        }
                        case CommonVariables.POST:
                        {
                            Log.e(TAG, "catch get_or_post : "+CommonVariables.POST);
                            httpURLConnection.setRequestMethod(CommonVariables.POST);
                            break;
                        }

                    }

                    Log.e(TAG, "catch Request data "+data);

                    // Send Post Request

                    httpURLConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                    wr.writeBytes(data);
                    wr.flush();
                    wr.close();

                    switch (check_header)
                    {
                        case CommonVariables.HEADER:
                        {
                            Log.e(TAG, "check_header : "+CommonVariables.HEADER);

                            Map<String, List<String>> map = httpURLConnection.getHeaderFields();

                            Log.e(TAG,"Printing Response Header...");

                            for (Map.Entry<String, List<String>> entry : map.entrySet())
                            {
                                Log.e(TAG, "Key : "+entry.getKey() + " ,Value : " + entry.getValue());
                            }
                            break;
                        }

                        case CommonVariables.NO_HEADER:
                        {
                            Log.e("check_header : ", CommonVariables.NO_HEADER);
                            break;
                        }
                    }

                    Log.e(TAG, "httpURLConnection.getRequestMethod() : "+httpURLConnection.getRequestMethod());

                    int responseCode = httpURLConnection.getResponseCode();

                    Log.e(TAG, "catch Response Code : "+responseCode);

                    switch (String.valueOf(responseCode))
                    {
                        case CommonVariables.SERVICE_RESPONSE_CODE_200://Success
                        case CommonVariables.SERVICE_RESPONSE_CODE_202://Success
                        {
                            input_stream = httpURLConnection.getInputStream();

                            switch (response_type)
                            {
                                case CommonVariables.JSON:
                                {
                                    Log.e("response_type : ",CommonVariables.JSON);
                                    response_output = retrieve_http_response_json_func(input_stream);
                                    break;
                                }
                                case CommonVariables.FILE:
                                {
                                    Log.e("response_type : ", CommonVariables.FILE);
                                    response_output = retrieve_http_response_file_func(input_stream, file_path);
                                    break;
                                }
                            }
                            break;
                        }
                        case CommonVariables.SERVICE_RESPONSE_CODE_401://UnAuthorized
                        {
                            response_output = String.valueOf(CommonVariables.SERVICE_RESPONSE_CODE_401);
                            break;
                        }
                        case CommonVariables.SERVICE_RESPONSE_CODE_404://Given Mobile Number Not Found
                        {
                            response_output = String.valueOf(CommonVariables.SERVICE_RESPONSE_CODE_404);
                            break;
                        }
                        default:
                        {
                            response_output = null;
                        }
                    }

                    return response_output;
                }
            }

        }
        catch (Exception e)
        {
            Log.e(TAG, "send_http_request_func Exception : " +e.getMessage());

            e.printStackTrace();
        }

        return null;
    }



    /**> Retrieve Response From Http Response Service And Convert To Json <*/
    public String retrieve_http_response_json_func(InputStream retrieved_response)
    {
        String retrieved_formatted_result  = "";

        try
        {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(retrieved_response));
            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                Log.i("line : ",line);

                retrieved_formatted_result += line;
            }

            bufferedReader.close();
            retrieved_response.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Log.i("func : ","4.1 retrieved_formatted_result : "+retrieved_formatted_result);

        return retrieved_formatted_result;
    }



    /**> Retrieve Response From Http Response Service And Convert To File <*/
    public String retrieve_http_response_file_func(InputStream retrieved_response, String file_path)
    {
        String retrieved_formatted_result = null;

        try
        {
            File file                               = new File(file_path);
            FileOutputStream fileOutputStream       = new FileOutputStream(file);

            int read;

            byte[] buffer = new byte[1024];

            while((read = retrieved_response.read(buffer)) != -1)
            {
                fileOutputStream.write(buffer,0, read);
                Log.i("read : ",read+"");
            }

            retrieved_formatted_result = file_path;

            retrieved_response.close();
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return retrieved_formatted_result;
    }



}
