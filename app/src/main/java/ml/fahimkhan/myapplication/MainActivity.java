package ml.fahimkhan.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_LIST_ITEMS = 101;
    private QuestionAdapter mAdapter;
    private static int displayedposition = 0;
    ArrayList<Integer> imagearry = new ArrayList<Integer>();
    ArrayList<Boolean> booleanitemClicked = new ArrayList<>();

    private RecyclerView recyclerView;
    Random r = new Random();
    int random = r.nextInt(12);
    int[] images = {R.mipmap.image0, R.mipmap.image1, R.mipmap.image2, R.mipmap.image3, R.mipmap.image4, R.mipmap.image5, R.mipmap.image6
            , R.mipmap.image7, R.mipmap.image8, R.mipmap.image9, R.mipmap.image10, R.drawable.flag};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Animation blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        TextView revealAnswer = (TextView) findViewById(R.id.next);
        revealAnswer.startAnimation(blink);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.listitem);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new QuestionAdapter(getApplicationContext(), NUM_LIST_ITEMS, random, imagearry, booleanitemClicked);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                displayedposition = gridLayoutManager.findFirstVisibleItemPosition();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myBinding();
    }

    public void nextIntent(View view) {
        boolean show = false;
        if (!mAdapter.pastindex.isEmpty()) {
            show = true;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final boolean finalShow = show;
        dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(getApplicationContext(), AnswerActivity.class).putExtra("random", random).putExtra("images", images).putExtra("position", displayedposition).putExtra("show", finalShow));
            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.setMessage("Did you get your Symbol?");
        dialog.setCancelable(false);
        dialog.show();
    }


    public void myBinding() {
        Toast.makeText(this, "Shuffling.!!", Toast.LENGTH_SHORT).show();
        random = r.nextInt(12);
        booleanitemClicked.clear();

        for (int listIndex = 0; listIndex <= 100; listIndex++) {
            int index = listIndex % 12;
            if (listIndex < 9) {
                Random r = new Random();
                int random = r.nextInt(12);
                imagearry.add(listIndex, images[random]);

            } else if (listIndex % 9 == 0) {
                imagearry.add(listIndex, images[random]);
            } else {
                imagearry.add(listIndex, images[index]);

            }
            booleanitemClicked.add(false);
            booleanitemClicked.set(listIndex, false);

        }

    }


}
