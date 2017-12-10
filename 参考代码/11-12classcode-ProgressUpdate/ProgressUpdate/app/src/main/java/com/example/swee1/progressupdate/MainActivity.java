package com.example.swee1.progressupdate;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.type;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar,progressBar1;
    Button start, stop, startRxOperationButton;
    myProgressbar task;
    myThread mThread;
    public String longRunningOperation() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // error
        }
        return "Complete!";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=(ProgressBar) this.findViewById(R.id.progressBar);
        progressBar1 = (ProgressBar) this.findViewById(R.id.progressBar1);
        start=(Button) this.findViewById(R.id.btn_start);
        stop = (Button) this.findViewById(R.id.btn_stop);
        startRxOperationButton = (Button) this.findViewById(R.id.btn_rxjava);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mThread = new myThread();
                mThread.start();
                task = new myProgressbar();
                task.execute();
            }
        });
        final Observable operationObservable = Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Subscriber subscriber = (Subscriber)o;
                String ret = longRunningOperation();
                subscriber.onNext(ret);
                subscriber.onCompleted();
            }
        }) .subscribeOn(Schedulers.io()) // subscribeOn the I/O thread
           .observeOn(AndroidSchedulers.mainThread()); // observeOn the UI Thread;;

        startRxOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                operationObservable.subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        v.setEnabled(true);
                    }
                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Object o) {
                        Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.obtainMessage(-1).sendToTarget();
                task.cancel(true);
            }
        });


    }
    class myThread extends Thread {
        int i = 0;
        @Override
        public void run() {
            i+=3;
            Message msg = handler.obtainMessage();
            msg.arg1=i;
            handler.sendMessage(msg);
            if(i >= 100 ) {
                handler.removeCallbacks(this);
            }
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    handler.removeCallbacks(mThread);
                    break;
                default:
                    progressBar.setProgress(msg.arg1);
                    handler.postDelayed(mThread,100);
            }
        }
    };
    class myProgressbar extends AsyncTask<Void, Integer, Void> {
        //模拟进度的更新
        @Override
        protected Void doInBackground(Void... arg0) {
            for ( int i = 0; i <= 100; i++ ) {
                if (isCancelled()) {
                    break;
                }
                i += 2;
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            if (isCancelled()) {
                return;
            }
            progressBar1.setProgress(values[0]);
        }

    }

}
