package com.tn74travellers.aroDriver.PushNotification;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tn74travellers.aroDriver.App.Config;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.Main.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

/**> Created by enterkey on 6/11/17 <*/

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    Context context = MyFirebaseMessagingService.this;

    /**> Normal Variables <*/

    private NotificationUtils notificationUtils;

    private String title;
    private String message;
    private String received_time;

    private static CommonFunctions commonFunctions = new CommonFunctions();

    /**> Handles Notification <*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void handleNotification(String message)
    {
        Log.i("handleNotification :", "hai");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext()))
        {
            /**> App Is In Foreground, Broadcast The Push Message <*/

            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            //pushNotification.putExtra("send_time", eventTime);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            /**> Play Notification Sound <*/

//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//
//            Log.i("sett_notification_sound", " : "+commonFunctions.get_shared_pref_boolean_func(getApplicationContext(), "sett_notification_sound"));
//
//            notificationUtils.playNotificationSound();

            Intent foreground_Intent = new Intent(getApplicationContext(), DashboardActivity.class);
            foreground_Intent.putExtra("message", message);
           // foreground_Intent.putExtra("send_time", eventTime);

            showNotificationMessage(getApplicationContext(), title, message,received_time, foreground_Intent);
        }
        else// App Is In Background
        {
            Log.e(TAG,"app is in background");
            /**> App Is In Background, Show The Notification In Notification Tray <*/

            String image_url = CommonVariables.BLANK_STRING;

            Intent background_Intent = new Intent(getApplicationContext(), DashboardActivity.class);
            background_Intent.putExtra("message", message);

//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            if (TextUtils.isEmpty(image_url))
            {
                showNotificationMessage(getApplicationContext(), title, message, received_time, background_Intent);
            }
            else
            {
                /**> Image Is Present, Show Notification With Image <*/

                showNotificationMessageWithBigImage(getApplicationContext(), title, message, received_time, background_Intent);
            }

//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//
//            Log.i("sett_notification_sound", " : "+commonFunctions.get_shared_pref_boolean_func(getApplicationContext(), "sett_notification_sound"));
//
//            notificationUtils.playNotificationSound();
        }


    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.e(TAG, "token: " + token);


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


            /**> Check If Message Contains A Notification Payload <*/

            if (remoteMessage.getNotification() != null)
            {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    handleNotification(remoteMessage.getNotification().getBody());
                }
            }

            /**> Check If Message Contains A Data Payload <*/

            if (remoteMessage.getData().size() > 0)
            {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());


                /****************************************/

//                commonFunctions.set_shared_pref_int_func(context, SharedPreferenceVariables.NOTIFICATION_COUNT,
//                        commonFunctions.get_shared_pref_int_func(context, SharedPreferenceVariables.NOTIFICATION_COUNT) + 1);
//
//                Log.e(TAG, "notification count: " + commonFunctions.get_shared_pref_int_func(context, SharedPreferenceVariables.NOTIFICATION_COUNT));

                //commonFunctions.set_shared_pref_boolean_func(context,CommonVariables.SP_FROM_NOTIFICATION,true);

            /*utility_container.set_shared_pref_int_func(getApplicationContext(), SharedPrefKeyVariables.NOTIFICATION_COUNT,
                    utility_container.get_shared_pref_int_func(getApplicationContext(), SharedPrefKeyVariables.NOTIFICATION_COUNT) + 1);

            if(getApplicationContext() instanceof DashboardActivity)
            {
                try
                {
                    Method m = DashboardActivity.class.getMethod("showNewNotificationCountBadge", String[].class);
                    m.invoke(this, String[].class);

                    Log.e(TAG, " : DashboardActivity");
                }
                catch (NoSuchMethodException e)
                {
                    e.printStackTrace();

                    Log.e(TAG, "NoSuchMethodException : "+e.getMessage());
                }
                catch (InvocationTargetException | IllegalAccessException e)
                {
                    e.printStackTrace();

                    Log.e(TAG, "NoSuchMethodException : "+e.getMessage());
                }
            }
            else
            {
                Log.e(TAG, " : Not Worked");
            }*/

                /****************************************/

                try
                {
                    String data_payload_str = remoteMessage.getData().toString();

                    Log.e(TAG, "data_payload_str : "+data_payload_str);

                    JSONObject json = new JSONObject(data_payload_str);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        handleDataMessage(json);
                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }

//        Map<String, String> data = remoteMessage.getData();
//        String title = data.get("title").toString();
//        String message = data.get("body").toString();
//        String message = data.get("badge").toString();


    /**> Handles Notification Data Message <*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void handleDataMessage(JSONObject json)
    {
        Log.e(TAG, "raw all json: " + json.toString());

        commonFunctions.setSharedPrefString(getApplicationContext(), CommonVariables.SP_NOTIFICATION, json.toString());

        try
        {
            JSONObject jo_payload   = json.getJSONObject("payload");

            title                   = jo_payload.getString("title");
            message                 = jo_payload.getString("body");
            received_time           = jo_payload.getString("sent_time");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
           Log.e(TAG, "received_time: " + received_time);

            message = URLDecoder.decode(message, "UTF-8");
            title   = URLDecoder.decode(title, "UTF-8");

            Log.e(TAG, "after message : "+message);
            Log.e(TAG, "after title : "+title);

            Log.e(TAG,"*************************************************************************");

            generateNotificationData();
        }
        catch (JSONException e)
        {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }



    /**> Generate Notification Data <*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void generateNotificationData()
    {
        /**> Incrementing Notification Badge Count <*/

//        commonFunctions.set_shared_pref_int_func(getApplicationContext(), SharedPreferenceVariables.NOTIFICATION_COUNT, commonFunctions.get_shared_pref_int_func(getApplicationContext(), SharedPreferenceVariables.NOTIFICATION_COUNT) + 1);
//
//        if(getApplicationContext() instanceof HomeActivity)
//        {
//            try
//            {
//                Method m = HomeActivity.class.getMethod("showNewNotificationCountBadge", String[].class);
//                m.invoke(this, String[].class);
//            }
//            catch (NoSuchMethodException e)
//            {
//                e.printStackTrace();
//
//                Log.e(TAG, "NoSuchMethodException : "+e.getMessage());
//            }
//            catch (InvocationTargetException | IllegalAccessException e)
//            {
//                e.printStackTrace();
//
//                Log.e(TAG, "NoSuchMethodException : "+e.getMessage());
//            }
//        }

        String image_url = CommonVariables.BLANK_STRING;

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext()))// App Is In foreground
        {
            Log.e(TAG,"app is in forground");
            /**> App Is In Foreground, Broadcast The Push Message <*/

            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            Intent local_push_notification = new Intent(CommonVariables.LOCAL_BROADCAST_ACTION_NEW_PUSH_MSG);
            local_push_notification.putExtra(CommonVariables.HAS_NEW_PUSH_MSG, true);
            LocalBroadcastManager.getInstance(this).sendBroadcast(local_push_notification);

            /**> Play Notification Sound <*/

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());

            Log.i("sett_notification_sound", " : "+commonFunctions.getSharedPrefBoolean(getApplicationContext(), "sett_notification_sound"));

            notificationUtils.playNotificationSound();

            Intent foreground_Intent = new Intent(getApplicationContext(), DashboardActivity.class);
            foreground_Intent.putExtra("message", message);

            showNotificationMessage(getApplicationContext(), title, message, received_time,foreground_Intent);
        }
        else// App Is In Background
        {
            Log.e(TAG,"app is in background");
            /**> App Is In Background, Show The Notification In Notification Tray <*/

            Intent background_Intent = new Intent(getApplicationContext(), DashboardActivity.class);
            background_Intent.putExtra("message", message);

            if (TextUtils.isEmpty(image_url))
            {
                showNotificationMessage(getApplicationContext(), title, message, received_time, background_Intent);
            }
            else
            {
                /**> Image Is Present, Show Notification With Image <*/

                showNotificationMessageWithBigImage(getApplicationContext(), title, message, received_time, background_Intent);
            }

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());

            Log.i("sett_notification_sound", " : "+commonFunctions.getSharedPrefBoolean(getApplicationContext(), "sett_notification_sound"));

            notificationUtils.playNotificationSound();
        }

    }



    /**> Showing notification with text only <*/
    private void showNotificationMessage(Context context, String title, String message, String time_stamp, Intent intent)
    {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message,time_stamp, intent);
    }



    /**> Showing notification with text and image <*/
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String time_stamp, Intent intent)
    {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message,time_stamp, intent);
    }
}
