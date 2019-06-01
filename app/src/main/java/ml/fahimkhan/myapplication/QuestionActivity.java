package ml.fahimkhan.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    CardView card1, card2, card3;
    Animation fade1,fade2,fade3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        card1 = (CardView) findViewById(R.id.txt2);
        card2 = (CardView) findViewById(R.id.cardtxt3);
        card3 = (CardView) findViewById(R.id.cardtxt4);
        fade1 = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        fade2 = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        fade3 = AnimationUtils.loadAnimation(this, R.anim.left_to_right);

        card1.startAnimation(fade1);
        fade2.setStartOffset(500);
        card2.startAnimation(fade2);
        fade3.setStartOffset(1000);
        card3.startAnimation(fade3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void DoneQ(MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        dialog.setMessage("Did you choose your number?");
        dialog.setCancelable(true);
        dialog.show();
    }
}
