package com.example.notificationlistenerservice;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;

public class MainActivity extends AppCompatActivity implements ItemTouchCallback, ItemFilterListener<NotificationView> {

    private static final String TAG = "NotificationDATA";
    private static final String TAG_PRE = "["+MainActivity.class.getSimpleName()+"] ";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    //Initial value of isEnablesNLS is  false so that every time user installs the app, he has to manually enable it.
    private boolean isEnabledNLS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        


    }

    //PRINTS THE STATUS OF isEnabledNLS on Logs
    @Override
    protected void onResume() {
        super.onResume();
        isEnabledNLS = isEnabled();
        logNLS("isEnabledNLS = " + isEnabledNLS);
        if (!isEnabledNLS) {
            showConfirmDialog();
        }
    }

    // METHOD TO ENABLE NOTIFICATION ACCESS EVERY TIME THE USER INSTALLS APP
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    // DIALOG BOX TO ENABLE NOTIFICATION ACCESS FOR THE APP BY USER
    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Please enable NotificationMonitor access")
                .setTitle("Notification Access")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openNotificationAccess();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        })
                .create().show();
    }

    //OPENS NOTIFICATION ACCESS ON YOUR PHONE
    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    //ACITIVITY TO DSIPLAY LOGS
    private void logNLS(Object object) {
        Log.i(TAG, TAG_PRE+object);
    }

    //ITEM LISTENER DEFAULT METHODS (Do not Delete them)


    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        return false; }

    @Override
    public void itemTouchDropped(int oldPosition, int newPosition) { }
}
