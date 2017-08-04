package cn.usm.rxjava2demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
    }

    public void createObservable(View view) {
        Intent intent = new Intent(mContext, ObservableCreateActivity.class);
        mContext.startActivity(intent);
    }

    public void operatorObservable(View view) {
        Intent intent = new Intent(mContext, OperatorActivity.class);
        mContext.startActivity(intent);
    }

    public void createFlowable(View view){
        Intent intent = new Intent(mContext, FlowableCreateActivity.class);
        mContext.startActivity(intent);
    }

}
