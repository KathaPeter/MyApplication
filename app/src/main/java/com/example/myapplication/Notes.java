package com.example.myapplication;

public class Notes {

    /*
    *
    *
    *  private static final String CHANNEL_ID = "com.example.myapplication.Healthcare";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PUSH_CHANNEL";//getString(R.string.channel_name);
            String description = "PUSH_CHANNEL"; // getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    *
    * */




    /*
    *  createNotificationChannel();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 4711;
        notificationManager.notify(notificationId, builder.build());

    *
    * */
}
