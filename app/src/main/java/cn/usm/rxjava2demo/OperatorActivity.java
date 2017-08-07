package cn.usm.rxjava2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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

    /**
     * 安顺序发射  ob1  ob2
     *
     * @param view
     */
    public void concat(View view) {

      /*  Observable observable1 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                e.onNext(1);
              //  Thread.sleep(500);
                e.onNext(2);
               // Thread.sleep(500);
                e.onNext(3);
              //  Thread.sleep(500);
            }
        });*/

        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer i) throws Exception {
                Log.d(TAG, "concat " + i);
            }
        });
    }

    /**
     * 去重操作符
     *
     * @param view
     */
    public void distinct(View view) {
        Observable.just(1, 2, 3, 3, 3, 4, 5, 6, 8, 8).distinct().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "distinct :" + integer);
            }
        });
    }

    /**
     * 过滤操作符
     *
     * @param view
     */
    public void filter(View view) {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        //符合条件
                        return integer >= 5;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "filter :" + integer);
            }
        });
    }

    public void buffer(View view) {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
                .buffer(3, 1)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG, "buffer size" + integers.size());
                        for (Integer i : integers) {
                            Log.d(TAG, "buffer :" + i);
                        }
                    }
                });
    }

    public void timer(View view) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Log.d(TAG, "click timer start:" + sdf.format(new Date(System.currentTimeMillis())));
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //延迟 发射事件
                        Log.d(TAG, "click timer end:" + sdf.format(new Date(System.currentTimeMillis())));
                    }
                });
    }

    public void interval(View view) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Log.d(TAG, "click interval start:" + sdf.format(new Date(System.currentTimeMillis())));
        Observable.interval(2, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //延迟 发射事件 ,间隔多久发送事件
                        Log.d(TAG, "click interval end:" + sdf.format(new Date(System.currentTimeMillis())));
                    }
                });
    }

    public void doOnNext(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext :" + integer);
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "subscribe :" + integer);
            }
        });
    }

    public void skip(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "skip 2 之后 输出 :" + integer);
                    }
                });
    }

    public void take(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "只拿3个数据 :" + integer);
                    }
                });
    }

    /**
     * 去除发送频率过快的项，看起来好像没啥用处，但你信我，后面绝对有地方很有用武之地。
     *
     * @param view
     */
    public void debounce(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1); // skip
                Thread.sleep(400);
                emitter.onNext(2); // deliver
                Thread.sleep(505);
                emitter.onNext(3); // skip
                Thread.sleep(100);
                emitter.onNext(4); // deliver
                Thread.sleep(605);
                emitter.onNext(5); // deliver
                Thread.sleep(510);
                emitter.onComplete();

            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "debounce :" + integer);
                    }
                });
    }

    /**
     * last 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项
     *
     * @param view
     */
    public void last(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .last(4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "last :" + integer);
                    }
                });
    }

    /**
     * merge 顾名思义，熟悉版本控制工具的你一定不会不知道 merge 命令，
     * 而在 Rx 操作符中，merge 的作用是把多个 Observable 结合起来，接受可变参数，也支持迭代器集合。
     * 注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
     *
     * @param view
     */
    public void merge(View view) {

        Observable observable1 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                e.onNext(1);
                Thread.sleep(500);
                e.onNext(2);
                Thread.sleep(500);
                e.onNext(3);
                Thread.sleep(500);
            }
        }).subscribeOn(Schedulers.io());

        Observable.merge(observable1, Observable.just(4, 5, 6).subscribeOn(Schedulers.io()))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "merge :" + integer);
                    }
                });
    }

    /**
     * V
     * reduce 操作符每次用一个方法处理一个值，可以有一个 seed 作为初始值
     *
     * @param view
     */
    public void reduce(View view) {
        Observable.just(1, 2, 3)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "reduce " + integer);
            }
        });
    }

    /**
     * scan 操作符作用和上面的 reduce 一致，唯一区别是 reduce 是个只追求结果的坏人，而  scan 会始终如一地把每一个步骤都输出
     *
     * @param view
     */
    public void scan(View view) {
        Observable.just(1, 2, 3)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "reduce " + integer);
            }
        });
    }

    /**
     * 按照实际划分窗口，将数据发送给不同的 Observable
     * @param view
     */
    public void window(View view) {
        Observable.interval(1,TimeUnit.SECONDS)
                .take(15)
                .window(3,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(Observable<Long> longObservable) throws Exception {
                        Log.e(TAG, "Sub Divide begin...");
                        longObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        Log.e(TAG, "window: " + aLong );
                                    }
                                });
                    }
                });
    }
}
