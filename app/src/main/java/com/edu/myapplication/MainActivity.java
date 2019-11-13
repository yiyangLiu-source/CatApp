package com.edu.myapplication;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edu.nju.myapplication.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CatAdapter favAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewGroup favContainer = findViewById(R.id.favorite_container);
        final ViewGroup catContainer = findViewById(R.id.search_container);

        RecyclerView catRv = findViewById(R.id.cat_rv);
        RecyclerView favRv = findViewById(R.id.favo_rv);
        final CatAdapter catAdapter = initRv(catRv);
        favAdapter = initRv(favRv);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CatApplication.sCatService.search(query).enqueue(new Callback<List<Breed>>() {
                    @Override
                    public void onResponse(Call<List<Breed>> call, Response<List<Breed>> response) {
                        if (response.isSuccessful()) {
                            List<Breed> re = response.body();
                            catAdapter.setData(re);
                        } else {
                            Toast.makeText(MainActivity.this, "Network error, unable to query", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Breed>> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(MainActivity.this, "Network error, unable to query", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catContainer.getVisibility() != View.VISIBLE) {
                    catContainer.setVisibility(View.VISIBLE);
                    favContainer.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.favorite_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favContainer.getVisibility() != View.VISIBLE) {
                    favContainer.setVisibility(View.VISIBLE);
                    catContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        favAdapter.setData(CatApplication.breeds);
    }

    private CatAdapter initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        CatAdapter adapter = new CatAdapter();
        rv.setAdapter(adapter);
        return adapter;
    }


}
