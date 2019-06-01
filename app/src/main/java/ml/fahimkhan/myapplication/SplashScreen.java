package ml.fahimkhan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    ImageView img1;
    TextView name;
    Animation fadeIn,fade;

    // private  static int SPLASH_TIME_OUT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img1 = (ImageView) findViewById(R.id.img);
        name= (TextView) findViewById(R.id.textView4);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        img1.setVisibility(View.VISIBLE);
        img1.startAnimation(fadeIn);
        fade=AnimationUtils.loadAnimation(this,R.anim.blink);
        name.startAnimation(fade);

        Thread timerThread = new Thread() {
            public void run() {
                try {

                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, QuestionActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
