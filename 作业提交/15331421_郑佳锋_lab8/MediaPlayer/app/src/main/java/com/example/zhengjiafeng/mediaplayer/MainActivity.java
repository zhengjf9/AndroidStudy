package com.example.zhengjiafeng.mediaplayer;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private ImageView image_rotate;
    private Button btPlay;
    private Button btQuit;
    private Button btStop;
    private SeekBar bar;
    private TextView show_state;
    private TextView current;
    private TextView duration;
    private int flag = 0;
    private  int last_state = 0;

    private PlayerConn conn;
    private IMyBinder binder;

    private boolean isDrag; //进度条是否正在拖拽
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    ObjectAnimator animator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        bar.setOnSeekBarChangeListener(this);
        verifyStoragePermissions(this);
        conn = new PlayerConn();
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);


        animator = ObjectAnimator.ofFloat(image_rotate, "rotation", 360);
        image_rotate.setScaleType(ImageView.ScaleType.CENTER_CROP);
        animator.setDuration(15000);
        animator.setRepeatCount(-1);


        btPlay.setOnClickListener(this);
        btQuit.setOnClickListener(this);
        btStop.setOnClickListener(this);
        intent.setAction(MyService.ACTION_PLAY);
        startService(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initViews() {
        image_rotate = (ImageView) findViewById(R.id.image_rotate);
        btPlay = (Button) findViewById(R.id.play);
        btQuit = (Button) findViewById(R.id.quit);
        btStop = (Button) findViewById(R.id.stop);
        bar = (SeekBar) findViewById(R.id.bar);
        show_state = (TextView)findViewById(R.id.show_state);
        current = (TextView)findViewById(R.id.current);
        duration = (TextView)findViewById(R.id.duration);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,MyService.class);
        if (btPlay == v) {
            if(flag == 0) {
                intent.setAction(MyService.ACTION_PLAY);
                intent.putExtra("progress", bar.getProgress());
                btPlay.setText("PAUSED");
                show_state.setText("Playing");
                if(last_state == 0 || last_state == 2) {
                    animator.start();
                }
                else if(last_state == 1){
                    animator.resume();
                }
                flag = 1;
            }
            else if(flag == 1){
                intent.setAction(MyService.ACTION_PAUSE);
                btPlay.setText("PLAY");
                show_state.setText("Paused");
                animator.pause();
                flag = 0;
                last_state = 1;
            }
        } else if (btQuit == v) {
            intent.setAction(MyService.ACTION_STOP);
            MainActivity.this.finish();
        } else if (btStop == v) {
            intent.setAction(MyService.ACTION_STOP);
            btPlay.setText("PLAY");
            show_state.setText("Stopped");
            flag = 0;
            animator.end();
            bar.setProgress(0);
            current.setText("00:00");
            last_state = 2;
        }
        startService(intent);
        if(btStop == v){
            Intent intent1 = new Intent(this,MyService.class);
            intent1.setAction(MyService.ACTION_PLAY);
            startService(intent1);
        }
    }
    private int dur;
    private  Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyService.WHAT_DURATION:
                    bar.setMax(msg.arg1);
                    dur = msg.arg1;
                    duration.setText(calculateTime(msg.arg1/1000));
                    break;
                case MyService.WHAT_CURRENT:
                    if (!isDrag) {
                        bar.setProgress(msg.arg1);
                        current.setText(calculateTime(msg.arg1/1000));
                    }
                    break;
            }
        }
    };
    public String calculateTime(int time) {
        int minute;
        int second;

        String strMinute;

        if (time >= 60) {
            minute = time / 60;
            second = time % 60;

            //分钟在0-9
            if(minute>=0&&minute<10)
            {
                //判断秒
                if(second>=0&&second<10)
                {
                    return "0"+minute+":"+"0"+second;
                }else
                {
                    return "0"+minute+":"+second;
                }
            }else
            //分钟在10以上
            {
                //判断秒
                if(second>=0&&second<10)
                {
                    return minute+":"+"0"+second;
                }else
                {
                    return minute+":"+second;
                }
            }

        } else if (time < 60) {
            second = time;
            if(second>=0&&second<10)
            {
                return "00:"+"0"+second;
            }else
            {
                return "00:" + second;
            }

        }
        return null;
    }
    private class PlayerConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IMyBinder) service;
            binder.setHandler(handler); //设定handler
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if(fromUser){
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeMessages(MyService.WHAT_CURRENT);
        isDrag = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
            Intent intent = new Intent(this, MyService.class);
            if (flag == 0) {
                intent.setAction(MyService.ACTION_CHANGE);
                intent.putExtra("progress", bar.getProgress());
            }
            else{
                intent.setAction(MyService.ACTION_PLAY);
                intent.putExtra("progress", bar.getProgress());
            }
            startService(intent);
            isDrag = false;
        current.setText(calculateTime((int)bar.getProgress()/1000));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除服务
        unbindService(conn);
    }
}
