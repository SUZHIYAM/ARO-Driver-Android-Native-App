package com.tn74travellers.aroDriver.PushNotification;

/**> Created by enterkey on 12/7/18. <*/

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tn74travellers.aroDriver.App.Config;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;


public class PushNotificationRetrieveRegId
{
    /**> Normal Variables <*/

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Context context;

    private CommonFunctions commonFunctions = new CommonFunctions();

    /**> Constructor To Retrieve Class Context <*/
    public PushNotificationRetrieveRegId(Context context)
    {
        this.context = context;
    }


    /**> On BroadCastReceiver Receive The Notification That Registration Has Completed
     * And Receive Push Notification Message <*/
    public void pushNotiBroadCastReceive()
    {
        mRegistrationBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                /**> Checking For Type Intent Filter <*/

                Log.e("intent.getAction(): ", intent.getAction());

                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE))
                {
                    /**> Gcm Successfully Registered Now Subscribe To `Global` Topic
                     * To Receive App Wide Notifications <*/

                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                }
                else if (intent.getAction().equals(Config.PUSH_NOTIFICATION))
                {
                    /**> New Push Notification Is Received <*/

                    Log.e("push noti msg: ", intent.getStringExtra("message"));
                }
            }
        };
    }


    /**> Fetches Reg Id From Shared Preferences And Displays On The Screen <*/
    public void displayFirebaseRegId()
    {
        SharedPreferences pref  = context.getSharedPreferences(Config.SHARED_PREF, 0);
        String regId            = pref.getString("regId", null);

        /*Log.e("Firebase reg id: ", regId);*/

        if (!TextUtils.isEmpty(regId))
        {
            Log.e("Firebase Reg Id: ", regId);

            commonFunctions.setSharedPrefString(context, CommonVariables.SP_GCM_ID,regId);
        }
        else
        {
            Log.e("Firebase Reg Id : ", "Firebase Reg Id is not received yet!");
        }

    }



    /**> Call This Method When Resuming This Activity <*/
    public void whileResuming()
    {
        /**> Register GCM Registration Complete Receiver <*/

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        /**> Register New Push Message Receiver By Doing This, The Activity
            Will Be Notified Each Time A New Message Arrives <*/

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        /**> Clear The Notification Area When The App Is Opened <*/

        NotificationUtils.clearNotifications(context);
    }



    /**> Call This Method When Back Pressed This Activity <*/
    public void whileBackPressing()
    {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
