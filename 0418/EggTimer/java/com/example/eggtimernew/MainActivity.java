package com.example.eggtimernew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton;
    private static final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    private static final int REQUEST_CODE_NOTIFICATIONS = 1000;
    private String[] requestPermission = {"android.permission.POST_NOTIFICATIONS"};

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private boolean isPaused = false;
    private int timeLeft;  // 초 단위로 남은 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.edit);
        mButton = findViewById(R.id.button); // 버튼 id는 button이라고 가정

        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, requestPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, requestPermission, REQUEST_CODE_NOTIFICATIONS);
            }
        }

        mButton.setOnClickListener(this::startTimer);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Egg Timer Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("Channel for egg timer notifications");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void sendNotification(String title, String message) {
        Log.d("NOTIFICATION", "알림 보냄: " + message);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

    public void startTimer(View view) {
        if (isTimerRunning) {
            pauseTimer();
        } else {
            if (!isPaused) {
                String s = mEditText.getText().toString();
                int min = Integer.parseInt(s.substring(0, 2));
                int sec = Integer.parseInt(s.substring(3, 5));
                timeLeft = min * 60 + sec;
            }
            resumeTimer();
        }
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
            isPaused = true;
            mEditText.setText("일시정지됨 (" + timeLeft + "초 남음)");
            mButton.setText("재시작");
        }
    }

    private void resumeTimer() {
        countDownTimer = new CountDownTimer(timeLeft * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                mEditText.setText(timeLeft + "초");

                if (timeLeft % 10 == 0) {
                    sendNotification("Egg Timer", timeLeft + "초 남았습니다!");
                    showContinueDialog();
                }

                timeLeft--;
            }

            public void onFinish() {
                mEditText.setText("done!");
                sendNotification("Egg Timer", "계란 삶기가 완료되었습니다.");
                isTimerRunning = false;
                isPaused = false;
                mButton.setText("시작");
            }
        }.start();

        isTimerRunning = true;
        isPaused = false;
        mButton.setText("일시정지");
    }

    private void showContinueDialog() {
        runOnUiThread(() -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("타이머 일시정지")
                    .setMessage("계속하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        // 계속
                    })
                    .setNegativeButton("아니오", (dialog, which) -> {
                        pauseTimer();
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSION", "알림 권한 허용됨");
            } else {
                Log.d("PERMISSION", "알림 권한 거부됨");
                mEditText.setText("알림 권한이 없습니다");
            }
        }
    }
}
