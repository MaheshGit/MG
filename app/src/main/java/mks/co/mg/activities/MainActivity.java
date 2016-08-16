package mks.co.mg.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mks.co.mg.adapters.ImageAdapter;
import mks.co.mg.R;
import mks.co.mg.network.FlickrApiManager;
import mks.co.mg.network.model.FlickrResponse;
import mks.co.mg.network.model.Item;
import mks.co.mg.utils.CommonUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    private TextView timerText;
    private ImageView findThisImage;
    private String[] imageurls;
    private List<String> imageUrlList;
    private List<Item> items;
    private String imageFindUrl;
    private String clickedImageUrl;
    private int imageCounter = 0;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        getImages();
        gridview = (GridView) findViewById(R.id.gridview);

        findThisImage = (ImageView) findViewById(R.id.findImage);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                clickedImageUrl = imageurls[position];
                if (clickedImageUrl.equals(imageFindUrl)) {
                    imageCounter++;
                    if (imageCounter < imageUrlList.size()) {
                        CommonUtilities.loadImage(MainActivity.this, imageUrlList.get(imageCounter), findThisImage);
                        imageFindUrl = imageUrlList.get(imageCounter);
                    } else {
                        findThisImage.setVisibility(View.GONE);
                        imageCounter = 0;
                        createStartGameAgainDialog();
                    }
                } else {
                    CommonUtilities.showToastMessage(MainActivity.this, "You selected wrong image. Try by selecting another image");
                }
            }
        });
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void createStartGameAgainDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Start Game Again")
                .setMessage("Congratulations you have finished the game.Click OK to start game again.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImages();
                    }
                }).create().show();
    }

    public void getImages() {
        items = new ArrayList<>();
        imageurls = new String[9];
        Call<FlickrResponse> call = FlickrApiManager.getApiManager().getFLickrImages("json", 1);
        call.enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                items = response.body().getItems();
                Log.i("Size", " " + items.size());
                for (int i = 0; i < 9; i++) {
                    imageurls[i] = items.get(i).getMedia().getM();
                }
                for (int i = 0; i < imageurls.length; i++) {
                    Log.i("Image url", i + "  " + imageurls[i]);
                }

                imageUrlList = new ArrayList<String>();
                for (int index = 0; index < imageurls.length; index++) {
                    imageUrlList.add(imageurls[index]);
                }
                gridview.setAdapter(new ImageAdapter(MainActivity.this, imageurls));
                timerText = (TextView) findViewById(R.id.timer);
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    CommonUtilities.showToastMessage(MainActivity.this, "Please connect to internet.");
                }
                t.printStackTrace();
            }
        });
    }

    public class GameTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public GameTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText("You have : " + millisUntilFinished / 1000 + " seconds left to remember all images position");
        }

        @Override
        public void onFinish() {
            timerText.setText("Start finding image position : ");
            imageCounter = 0;
            Collections.shuffle(imageUrlList);
            findThisImage.setVisibility(View.VISIBLE);
            imageFindUrl = imageUrlList.get(imageCounter);
            CommonUtilities.loadImage(MainActivity.this, imageFindUrl, findThisImage);
        }
    }

    public void startTimer(Context context) {
        new GameTimer(15000, 1000).start();
    }
}
