package com.example.monitoringplatform;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MqttServiceStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, mqtt_sub.class));
    }
}
