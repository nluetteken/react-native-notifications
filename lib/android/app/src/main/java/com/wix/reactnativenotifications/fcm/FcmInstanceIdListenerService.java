package com.wix.reactnativenotifications.fcm;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wix.reactnativenotifications.BuildConfig;
import com.wix.reactnativenotifications.core.notification.IPushNotification;
import com.wix.reactnativenotifications.core.notification.PushNotification;

import static com.wix.reactnativenotifications.Defs.LOGTAG;

/**
 * Instance-ID + token refreshing handling service. Contacts the FCM to fetch the updated token.
 *
 * @author amitd
 */
public class FcmInstanceIdListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message){
        Bundle bundle = message.toIntent().getExtras();
        // Access urban airship push id -> Filter it out if send by airship
        String airshipPushId = message.getData().get("com.urbanairship.push.PUSH_ID");

        if(BuildConfig.DEBUG) Log.d(LOGTAG, "New message from FCM: " + bundle);
        if(BuildConfig.DEBUG) Log.d(LOGTAG, "Found Airship push id: " + airshipPushId);

        if (airshipPushId == null) {
            try {
                final IPushNotification notification = PushNotification.get(getApplicationContext(), bundle);
                notification.onReceived();
            } catch (IPushNotification.InvalidNotificationException e) {
                // An FCM message, yes - but not the kind we know how to work with.
                if(BuildConfig.DEBUG) Log.v(LOGTAG, "FCM message handling aborted", e);
            }
        }
    }
}
