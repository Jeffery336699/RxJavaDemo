package cn.com.chaoba.rxjavademo.utility;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class TimeoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("timeout");
        mLButton.setOnClickListener(e -> {
            timeoutObserver().compose(bindToLifecycle()).subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    log(e);
                }

                @Override
                public void onNext(Integer integer) {
                    log("timeout:" + integer);
                }
            });
        });
        mRButton.setText("timeoutobserver");
        mRButton.setOnClickListener(e -> {
            timeoutobserverObserver().compose(bindToLifecycle()).subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    log(integer);
                }
            });
        });
    }

    private Observable<Integer> timeoutObserver() {
        return createObserver().timeout(200, TimeUnit.MILLISECONDS);
    }

    private Observable<Integer> timeoutobserverObserver() {
        return createObserver()
                .timeout(200, TimeUnit.MILLISECONDS, Observable.just(5, 6));
    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i <= 3; i++) {
                    try {
                        Thread.sleep(i * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
    }
}