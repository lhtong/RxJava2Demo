package cn.usm.rxjava2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class OperatorActivity extends AppCompatActivity {

    public static final String TAG = OperatorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
    }

    public void observableClickMap(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer i) throws Exception {
                return "the result is " + i;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept " + s);
            }
        });
    }

    public void observableClickFlatMap(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                final List<String> list = new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    list.add("i am value " + i);
                }
                return Observable.fromIterable(list).delay(5, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept " + s);
            }
        });
    }

    public void observableClickConcatMap(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                final List<String> list = new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    list.add("i am value " + i);
                }
                return Observable.fromIterable(list).delay(5, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept " + s);
            }
        });
    }

    public void observableClickZip(View view) {

        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Thread.sleep(10);

                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Thread.sleep(10);

                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Thread.sleep(10);

                Log.d(TAG, "emit 4");
                emitter.onNext(4);
                Thread.sleep(10);

                Log.d(TAG, "emit onComplete1");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "emit A");
                emitter.onNext("A");
                Log.d(TAG, "emit B");
                emitter.onNext("B");
                Log.d(TAG, "emit C");
                emitter.onNext("C");
                Log.d(TAG, "emit onComplete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(@NonNull Integer i, @NonNull String s) throws Exception {
                return i + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });


    }
}
