package com.nocturnaldev.parcelableinpendingintent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String BROADCAST_ACTION = "BROADCAST_ACTION";
    private static final String DATA_EXTRA = "com.nocturnaldev.parcelableinpendingintent.DATA_EXTRA";
    private static final String MARSHALLED_DATA_EXTRA = "MARSHALLED_DATA_EXTRA";

    private MyBroadcastReceiver mMyBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMyBroadcastReceiver == null) {
            mMyBroadcastReceiver = new MyBroadcastReceiver();
            registerReceiver(mMyBroadcastReceiver, new IntentFilter(BROADCAST_ACTION));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mMyBroadcastReceiver != null) {
            unregisterReceiver(mMyBroadcastReceiver);
            mMyBroadcastReceiver = null;
        }
    }

    public void onSendWithExceptionClicked(View view) {
        Data data = new Data();
        data.setInt(1);
        data.setStr("a");

        Log.d(TAG, "Sending : " + data);

        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(DATA_EXTRA, data);
        intent.setExtrasClassLoader(Data.class.getClassLoader());

        setupPendingIntent(intent);
    }

    public void onSafeSendClicked(View view) {
        Data data = new Data();
        data.setInt(2);
        data.setStr("b");

        Log.d(TAG, "Sending : " + data);

        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(MARSHALLED_DATA_EXTRA, parcel.marshall());

        setupPendingIntent(intent);
    }

    private void setupPendingIntent(Intent intent) {
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC, System.currentTimeMillis(), pi);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Action " + intent.getAction() + " received");
            Data data = null;
            if (intent.hasExtra(DATA_EXTRA)) {
                data = intent.getParcelableExtra(DATA_EXTRA);
            } else if (intent.hasExtra(MARSHALLED_DATA_EXTRA)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(MARSHALLED_DATA_EXTRA);
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(byteArrayExtra, 0, byteArrayExtra.length);
                parcel.setDataPosition(0);
                data = Data.CREATOR.createFromParcel(parcel);
            }
            Log.d(TAG, "Data: " + data);
        }
    }
}
