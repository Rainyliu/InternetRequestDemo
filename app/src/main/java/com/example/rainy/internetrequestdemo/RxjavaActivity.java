package com.example.rainy.internetrequestdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import rx.Observable;
import rx.functions.Action1;

public class RxjavaActivity extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        tv = (TextView) findViewById(R.id.tv);
    }

    public  void hello(String... names){
        Observable.from(names).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                System.out.println("Hello " + s + "!");
                tv.setText("Hello " + s + "!");
            }

        });
    }

    public void click(View v){
        hello("lili","mama","yoyo");
    }
}
