package pl.marchuck.blenavigator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.FillableLoaderBuilder;
import com.github.jorgecastillo.clippingtransforms.PlainClippingTransform;

import pl.marchuck.blenavigator.common.WeakHandler;
import pl.marchuck.blenavigator.utils.Octocat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FillableLoader fillableLoader = (FillableLoader) findViewById(R.id.fillableLoader);
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.rootView);

        FillableLoaderBuilder loaderBuilder = new FillableLoaderBuilder();
        fillableLoader = loaderBuilder
                .parentView((RelativeLayout) rootView)
                .layoutParams(new RelativeLayout.LayoutParams(300, 300))
                .svgPath(Octocat.GITHUB)
                .originalDimensions(970, 970)
                .strokeWidth(2)
                .strokeColor(Color.parseColor("#1c9ade"))
                .fillColor(Color.parseColor("#1c9ade"))
                .strokeDrawingDuration(2000)
                .fillDuration(5000)
                .clippingTransform(new PlainClippingTransform())
                .build();

        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 10 * 6000);
    }
}
