package edu.washington.devinb5.arewethereyet;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;

public class MainActivity extends Activity {
    private PendingIntent toaster;
    private int interval;
    private String phoneNumber, message, intervalString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText intervalText = (EditText) findViewById(R.id.edit_interval);

        final EditText phoneNum = (EditText) findViewById(R.id.edit_phone);

        final EditText messageText = (EditText) findViewById(R.id.edit_message);




        final Button weToasting = (Button) findViewById(R.id.btn_st);

        weToasting.setOnClickListener(new View.OnClickListener() {
        AlarmManager toasts = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            @Override
            public void onClick(View v) {

                if(weToasting.getText().toString().equals("Start")) {
                    intervalString = intervalText.getText().toString();
                    phoneNumber = phoneNum.getText().toString();
                    message = messageText.getText().toString();
                    boolean missingInterval = intervalString.equals("");
                    boolean missingNumber = phoneNumber.equals("");
                    boolean missingMessage = message.equals("");
                    if(!missingInterval && !missingNumber && !missingNumber) {
                        interval = Integer.parseInt(intervalText.getText().toString());
                        interval = interval * 60 * 1000;

                        Intent toast = new Intent(MainActivity.this, ToastReceiver.class);
                        toast.putExtra("Message", message);
                        toast.putExtra("PhoneNumber", phoneNumber);
                        toaster = PendingIntent.getBroadcast(MainActivity.this, 1995, toast, PendingIntent.FLAG_CANCEL_CURRENT);
                        toasts.setInexactRepeating(AlarmManager.RTC_WAKEUP, 10, interval,toaster);
                        weToasting.setText("Stop");
                    } else {
                        String missing = "You are missing: ";
                        if(missingInterval) {
                            missing += "an interval";
                        }
                        if(missingNumber) {
                            missing += " a phone number";
                        }
                        if(missingMessage) {
                            missing += " a message";
                        }

                        Toast.makeText(MainActivity.this, missing, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    weToasting.setText("Start");
                    toasts.cancel(toaster);
                }

            }
        });




    }
    public static class ToastReceiver extends BroadcastReceiver {

        public ToastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String[] toastContents = {intent.getStringExtra("PhoneNumber"), intent.getStringExtra("Message")};
            Toast.makeText(context, toastContents[0] + ": " + toastContents[1], Toast.LENGTH_SHORT).show();
            int i = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(toastContents[0], null, toastContents[1], null, null);
        }



    }
}
