package com.edu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nju.myapplication.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        Intent intent = getIntent();
        final Breed data = new Gson().fromJson(intent.getStringExtra("data"), Breed.class);

        ((TextView) findViewById(R.id.name)).setText("Name: " + data.getName());
        ((TextView) findViewById(R.id.desc)).setText("Description: " + data.getDescription());
        ((TextView) findViewById(R.id.weight)).setText("Weight: " + data.getWeight());
        ((TextView) findViewById(R.id.temp)).setText("getTemperament: " + data.getTemperament());
        ((TextView) findViewById(R.id.orign)).setText("Origin: " + data.getOrigin());
        ((TextView) findViewById(R.id.life)).setText("Life Span: " + data.getLife_span());
        ((TextView) findViewById(R.id.wiki)).setText("Wiki: " + data.getWikipedia_url());
        final ImageView imgView = findViewById(R.id.img);

        CatApplication.sCatService.img(data.getId()).enqueue(new Callback<List<ImgResult>>() {
            @Override
            public void onResponse(Call<List<ImgResult>> call, Response<List<ImgResult>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
//                    Log.e("Cat", "onResponse: " + new Gson().toJson(response.body().get(0)));
                    ImgResult re = response.body().get(0);
                    Picasso.get().load(re.getUrl()).into(imgView);
                }
            }

            @Override
            public void onFailure(Call<List<ImgResult>> call, Throwable t) {
            }
        });

        LinkedList<Breed> alreadyFav = CatApplication.breeds;
        boolean fav = false;
        for (Breed it : alreadyFav) {
            if (it.getId().equals(data.getId())) {
                fav = true;
                break;
            }
        }
        final ImageButton favBtn = findViewById(R.id.fav_btn);
        if (fav) {
            favBtn.setImageResource(R.drawable.ic_favorite);
        } else {
            favBtn.setImageResource(R.drawable.ic_un_favorite);
        }

        final boolean finalFav = fav;
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalFav) {
                    CatApplication.remove(data);
                    favBtn.setImageResource(R.drawable.ic_un_favorite);
                } else {
                    CatApplication.addToFavorite(data);
                    favBtn.setImageResource(R.drawable.ic_favorite);
                }
            }
        });


    }
}
