package com.potatoprogrammers.doit.notifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class NotifyService extends IntentService {
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    private static final int TIME_BETWEEN_CHECKS = 60000; //every 1 minute

    public NotifyService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(this, NotificationBroadcastReceiver.class);
        //put extra stuff to intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), TIME_BETWEEN_CHECKS, pi);
    }
}
