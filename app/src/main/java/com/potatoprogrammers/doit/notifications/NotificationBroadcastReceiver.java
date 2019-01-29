package com.potatoprogrammers.doit.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.enums.DayOfTheWeek;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.models.UserActivityDate;
import com.potatoprogrammers.doit.models.UserStats;
import com.potatoprogrammers.doit.utilities.Utils;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static int minutesBeforeNotification = 60; //1 hour before
    private static final Map<String, Integer> notifyIds = new HashMap<>();
    @Override
    public void onReceive(Context context, Intent intent) {

        new Thread(() -> {
            User u = User.getLoggedInUser();
            if (!u.getSettings().isNotificationActive()) return;
            String dateAsString = Utils.getDateAsString(Calendar.getInstance().getTime());
            UserStats todayStats = u.getStats().get(dateAsString);

            minutesBeforeNotification = u.getSettings().getNotificationHour() * 60 + u.getSettings().getNotificationMinutes();

            List<UserActivity> toNotify = User.getLoggedInUser().getActivities().stream()
                    .filter(UserActivity::isActive) //get only from active activities
                    .filter(a -> a.isDayActive(DayOfTheWeek.getDayOfTheWeekFromCalendar(Calendar.getInstance()))) //get only for current day
                    .filter(a -> !todayStats.getActivitiesStatus().getOrDefault(a.getUuid(), Boolean.FALSE)) //get not finished tasks
                    .filter(a -> this.getMinutesUntilActivity(a) < minutesBeforeNotification) //get only 1hr before the activity
                    .collect(Collectors.toList());
            for (UserActivity activity: toNotify) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(activity.getName())
                        .setContentText(context.getString(R.string.notification_body))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                if (!notifyIds.containsKey(activity.getUuid())) {
                    notifyIds.put(activity.getUuid(), (int)System.currentTimeMillis());
                }

                notificationManager.notify(notifyIds.get(activity.getUuid()), notificationBuilder.build());
            }
        }).start();
    }

    private int getMinutesUntilActivity(UserActivity ua) {
        Calendar cal = Calendar.getInstance();
        DayOfTheWeek day = DayOfTheWeek.getDayOfTheWeekFromCalendar(cal);
        LocalTime currentTime = new LocalTime(cal.getTimeInMillis());
        UserActivityDate date = ua.getUserActivityDates().get(day.ordinal());
        LocalTime activityTime = new LocalTime().withHourOfDay(date.getHour()).withMinuteOfHour(date.getMinute());
        return Minutes.minutesBetween(currentTime, activityTime).getMinutes();
    }
}
