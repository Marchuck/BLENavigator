package pl.marchuck.blenavigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.jorgecastillo.FillableLoader;

import pl.marchuck.blenavigator.common.WeakHandler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initFillableLoader();
    }

    private void initFillableLoader() {
        FillableLoader fillableLoader = (FillableLoader) findViewById(R.id.fillableLoader);
        fillableLoader.setSvgPath(Paths.GITHUB);
        fillableLoader.start();
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                int in = android.R.anim.fade_in;
                int out = android.R.anim.fade_out;
                overridePendingTransition(in, out);
                finish();
            }
        }, 6000);
    }
}
