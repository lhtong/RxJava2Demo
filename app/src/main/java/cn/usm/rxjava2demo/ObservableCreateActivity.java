package cn.usm.rxjava2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class ObservableCreateActivity extends AppCompatActivity {

    public static final String TAG = ObservableCreateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable_create);
    }

    public void observableClick(View view){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                //ObservableEmitter 老板发号施令
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe被回调，表示该员工被正式聘用了");
                mDisposable = d;
                //该员工不是铁人，如果老板分配的事情太多，员工可以不做
                //Disposable可以让员工不在干活
            }

            @Override
            public void onNext(@NonNull Integer value) {
                if (value == 2) {
                    // mDisposable.dispose();
                }
                Log.d(TAG, "value ：" + value);
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
