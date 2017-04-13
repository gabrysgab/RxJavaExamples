package com.example.mateusz.rxjavaexamples;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_one)
    TextView textView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.context = this;
        List<String> manyWords = Arrays.asList("one", "two", "three");
        rx.Observable.OnSubscribe observableAction = new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World!");
                subscriber.onCompleted();
            }
        };
        Observable<String> observable = Observable.create(observableAction);

        Observable<String> singleObservable = Observable.just("Hello World");

        Action1<String> textViewOnNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                textView.setText(s);
            }
        };

        Func1<String, String> toUppercaseMap = new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s.toUpperCase();
            }
        };

        singleObservable.observeOn(AndroidSchedulers.mainThread())
                .map(toUppercaseMap)
                .subscribe(textViewOnNextAction);


        Subscriber<String> toastSubscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        };

        observable.observeOn(AndroidSchedulers.mainThread())
                .map(toUppercaseMap)
                .subscribe(toastSubscriber);


        Action1<String> toastOnNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        };

//        Observable<String> oneByOneObservable = Observable.from(manyWords);
//        oneByOneObservable.observeOn(AndroidSchedulers.mainThread())
//                .subscribe(toastOnNextAction);






        Observable<List<String>> observableList = Observable.just(manyWords);
        Func1<List<String>, Observable<String>> getWords = new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        };

        Func2<String, String, String> mergeRoutine = new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return String.format("%s %s", s, s2);
            }
        };

        observableList
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(getWords)
                .reduce(mergeRoutine)
                .subscribe(toastOnNextAction);

















    }



}
