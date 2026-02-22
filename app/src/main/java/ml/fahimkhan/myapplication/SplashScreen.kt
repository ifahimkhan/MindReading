package ml.fahimkhan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = SplashScreen.class.getName();
    ImageView img1;
    TextView name;
    Animation fadeIn,fade;

    // private  static int SPLASH_TIME_OUT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "oncreate: ");
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

                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashScreen.this, QuestionActivity.class);
                            startActivity(intent);
                        }
                    });
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
