package pl.marchuck.blenavigator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.FillableLoaderBuilder;
import com.github.jorgecastillo.clippingtransforms.PlainClippingTransform;

import pl.marchuck.blenavigator.common.WeakHandler;

public class SplashActivity extends AppCompatActivity {

    FillableLoader fillableLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fillableLoader = (FillableLoader) findViewById(R.id.fillableLoader);
//        FrameLayout rootView = (FrameLayout) findViewById(R.id.rootView);

//        FillableLoaderBuilder loaderBuilder = new FillableLoaderBuilder();
//        fillableLoader = loaderBuilder
//                .parentView((FrameLayout) rootView)
//                .layoutParams(new RelativeLayout.LayoutParams(300, 300))
//                .svgPath(Paths.GITHUB)
//                .originalDimensions(970, 970)
//                .strokeWidth(2)
//                .strokeColor(Color.parseColor("#1c9ade"))
//                .fillColor(Color.parseColor("#1c9ade"))
//                .strokeDrawingDuration(2000)
//                .fillDuration(5000)
//                .clippingTransform(new PlainClippingTransform())
//                .build();

        fillableLoader.setSvgPath(Paths.GITHUB);
        fillableLoader.start();
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                int in = android.R.anim.fade_in;
                int out = android.R.anim.fade_out;
//                overridePendingTransition(in, out);
                overridePendingTransition(out, in);
                finish();
            }
        }, 6000);
    }
}
