package com.wusj.rxjavademo;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView;
    TextView resultTextView;
    ArrayList<Integer> numList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_combine).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
        findViewById(R.id.btn_map).setOnClickListener(this);
        findViewById(R.id.btn_timer).setOnClickListener(this);
        textView = (TextView) findViewById(R.id.tv_num);
        resultTextView = (TextView) findViewById(R.id.tv_result);
        init();
        
        int length = numList.size();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i ++){
            sb.append(numList.get(i) +",");
        }

        textView.setText(sb.toString());
    }

    private void init() {
        numList = new ArrayList<>();
        numList.add(1);
        numList.add(2);
        numList.add(3);
        numList.add(1);
        numList.add(2);
        numList.add(3);
        numList.add(4);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
//                loginBtnClick();
                break;
            case R.id.btn_combine:
                combineBtnClick();
                break;
            case R.id.btn_filter:
                filterBtnClick();
                break;
            case R.id.btn_map:
                mapBtnClick();
                break;
            case R.id.btn_timer:
                timerBtnClick();
                break;
           default:
               break;
        }
    }

    private void loginBtnClick(){

    }

    private void combineBtnClick(){
        //整合 merge zip combineLatest scan,buffer
        resultTextView.setText("");
        Observable<Integer> ob = Observable.from(numList);
        ArrayList<Integer> nextList = new ArrayList<>();
        nextList.add(4);
        nextList.add(3);
        nextList.add(2);
        nextList.add(1);
        nextList.add(5);
        Observable<Integer> ob2 = Observable.from(nextList);
        Observable.merge(ob,ob2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        String text = resultTextView.getText().toString();
                        resultTextView.setText(TextUtils.isEmpty(text)? integer + "" : text + "," + integer);
                    }
                });
//        Observable.combineLatest(ob, ob2, new Func2<Integer, Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer, Integer integer2) {
//                return integer + integer2;
//            }
//        }).subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        String text = resultTextView.getText().toString();
//                        resultTextView.setText(TextUtils.isEmpty(text)? integer + "" : text + "," + integer);
//                    }
//                });


    }

    private void filterBtnClick(){
        //过滤:filter,take,distanct...
        resultTextView.setText("");
        Observable.from(numList)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer.intValue() != 3; //filter 3
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        String text = resultTextView.getText().toString();
                        resultTextView.setText(TextUtils.isEmpty(text)? integer + "" : text + "," + integer);
                    }
                });
    }

    private void mapBtnClick(){
        //映射：map,flatMap,concatMap,
        resultTextView.setText("");
        Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i = 0; i < numList.size(); i ++){
                    subscriber.onNext(numList.get(i));
                }
                subscriber.onCompleted();

            }
        }
        ).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                int value = integer.intValue();
                value = value *10 + value;
                Log.d("currentThread:", Thread.currentThread().getName());
                Log.d("Thread:",Looper.getMainLooper().getThread().getName());
                return new Integer(value);
            }
        }).observeOn(AndroidSchedulers.mainThread())  //回调发生在主线程中: onCompleted,onError,onNext
                .subscribeOn(Schedulers.computation())  //subscribribe 发生在其他线程
                .subscribe(new Observer<Integer>() {
                               @Override
                               public void onCompleted() {
                                    String text = resultTextView.getText().toString();
                                   resultTextView.setText(TextUtils.isEmpty(text)?  "" : text + "!!" );

                               }

                               @Override
                               public void onError(Throwable e) {
                                  Log.d("MainActivity",e.toString());
                               }

                               @Override
                               public void onNext(Integer integer) {
                                   Log.d("mainThread:",Thread.currentThread().getName());
                                   String text = resultTextView.getText().toString();
                                   resultTextView.setText(TextUtils.isEmpty(text) ? integer + "" : text + "," + integer );
                               }
                           }
                );

    }

    private void timerBtnClick(){
        //定时：timer,interval,repeat
//        Observable.interval(1000, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        String text = resultTextView.getText().toString();
//                        resultTextView.setText(TextUtils.isEmpty(text) ? aLong + "" : text + "," + aLong );
//                    }
//                });
        resultTextView.setText("");
        Observable.timer(1,TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
//                        String text = resultTextView.getText().toString();
//                        resultTextView.setText(text + "!!" );
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long integer) {
//                        String text = resultTextView.getText().toString();
//                        resultTextView.setText(TextUtils.isEmpty(text) ? integer + "" : text + "," + integer );
                        resultTextView.setText(integer +" ");
                    }
                });
//
    }


    /***
     * 1 最基本的使用：create,from,just,throws
     *
     * 2 线程的切换:Schedulers.io,Schedulers.computation  ...
     *
     * 3 过滤:filter,take,distanct...
     *
     * 4 映射：map,flatMap,concatMap,
     *
     * 5 整合 merge zip combineLatest scan,buffer
     *
     * 6 定时：timer,interval,repeat
     *
//     * 7 Android特有的几个Observable:
//     * AppObservable,ViewObservable,WidgetObservable,LifecycleObservable
     *
     *
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
