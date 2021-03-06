package com.naloaty.syncshare.util;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.naloaty.syncshare.R;
import com.naloaty.syncshare.service.CommunicationService;

/**
 * This class helps display a notification for {@link CommunicationService} in the status bar.
 */
public class CommunicationNotification {

    private static int SERVICE_NOTIFICATION_ID = 247830;
    private NotificationUtils notificationUtils;

    /**
     * @param notificationUtils The instance {@link NotificationUtils} that is used to display notifications.
     */
    public CommunicationNotification (NotificationUtils notificationUtils) {
        this.notificationUtils = notificationUtils;
    }

    /**
     * Shows a notification in the status bar.
     */
    public void showServiceNotification() {
        SSNotification notification =
                notificationUtils.createNotification(NotificationUtils.NOTIFICATION_CHANNEL_LOW, SERVICE_NOTIFICATION_ID);

        String titleContent = notificationUtils.getContext().getString(R.string.text_communicationServiceIdle);
        String textContent = notificationUtils.getContext().getString(R.string.text_communicationServiceIdleAction);

        //Setting up action on tap
        int requestCode = AppUtils.getUniqueNumber();

        Intent intent = new Intent(notificationUtils.getContext(), CommunicationService.class);
        intent.setAction(CommunicationService.ACTION_STOP_SHARING);

        PendingIntent contentAction =
                PendingIntent.getService(notificationUtils.getContext(), requestCode, intent, 0);

        //------------------------

        notification.setSmallIcon(R.drawable.ic_syncshare_wb_full)
                .setContentTitle(titleContent)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.setAutoCancel(true) //Notification will be automatically canceled after tap
                .setContentIntent(contentAction)
                .setOngoing(true);


        notification.show();
    }


    /**
     * Dismisses a notification in the status bar.
     */
    public void cancelNotification() {
        notificationUtils.cancelNotification(SERVICE_NOTIFICATION_ID);
    }
}
