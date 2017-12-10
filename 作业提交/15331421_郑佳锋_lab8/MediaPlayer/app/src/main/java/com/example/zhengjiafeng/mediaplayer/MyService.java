package com.example.zhengjiafeng.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;


/**
 * Created by zhengjiafeng on 2017/11/22.
 */

public class MyService extends Service {
    private MediaPlayer player = null;
    public static final String ACTION_PLAY = "play";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_STOP = "stop";
    public static final String ACTION_CHANGE = "change";

    public static final int WHAT_CURRENT = 1;
    public static final int WHAT_DURATION = 2;
    private PlayerState state = PlayerState.Idle;

    private Handler handler;
    private IMyBinder binder;
    private int progress = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_PLAY.equals(action)) {
            progress = intent.getIntExtra("progress", 0);
            toPlay(progress);
        } else if (ACTION_CHANGE.equals(action)){
            progress = intent.getIntExtra("progress", 0);
        }
        else if (ACTION_PAUSE.equals(action)) {
            pause();
        } else if (ACTION_STOP.equals(action)) {
            stop();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player != null){
            player.release();
            player = null;
        }
    }
    private void toPlay(int pos) {
        checkMediaPlayer();
        if (state == PlayerState.Initialized) {
            state = PlayerState.Preparing;
            player.prepareAsync();
        } else {
            play(pos);
        }
    }
    private void play(int pos) {
        if (state == PlayerState.Started || state == PlayerState.Prepared || state == PlayerState.Initialized
                || state == PlayerState.Paused
                || state == PlayerState.PlaybackCompleted) {
            player.seekTo(pos);
            player.start();
            state = PlayerState.Started;
            new Thread() {
                public void run() {
                    while (state == PlayerState.Started) {
                        send(WHAT_CURRENT, player.getCurrentPosition());
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        } else {
        }
    }
    private void pause() {
        if (state == PlayerState.Started) {
            player.pause();
            state = PlayerState.Paused;
        } else if (state == PlayerState.Paused) {
        } else {
        }
    }
    private void stop() {
        if (state == PlayerState.Started || state == PlayerState.Prepared
                || state == PlayerState.Paused
                || state == PlayerState.PlaybackCompleted) {
            try {
                player.stop();
            } catch (IllegalStateException e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                //e.printStackTrace();
                player = null;
                player = new MediaPlayer();
                player.stop();
            }
            player.release();
            player = null;
            state = PlayerState.End;
            send(WHAT_CURRENT,0);
        } else {
        }
    }
    private void send(int what, int value) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = value;
        handler.sendMessage(msg);
    }

    private void checkMediaPlayer() {
        if (player == null) {
            // 完成多媒体的初始化
            player = new MediaPlayer();
            state = PlayerState.Idle;
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            // 判断SDcard正常挂载
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                try {
                    player.setDataSource(Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/melt.mp3");
                    state = PlayerState.Initialized;
                    //异步准备
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            state = PlayerState.Prepared;
                            // 设定进度条的max值
                            send(WHAT_DURATION, player.getDuration());
                            // 准备完成
                        }
                    });
                    //播放完成
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            state = PlayerState.PlaybackCompleted;
                            play(0);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class MyBinder extends Binder implements IMyBinder {
        public void setHandler(Handler handler) {
            MyService.this.handler = handler;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) binder;
    }
    private enum PlayerState {
        Idle, Initialized, Preparing, Prepared, Started, Stopped, Paused, End, PlaybackCompleted,
    }
}
