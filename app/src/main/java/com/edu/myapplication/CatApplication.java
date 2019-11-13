package com.edu.myapplication;

import android.app.Application;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heleninsa on 2019-11-12.
 *
 * @author heleninsa
 */
public class CatApplication extends Application {

    private final static Gson gson = new Gson();
    public static LinkedList<Breed> breeds = new LinkedList<>();
    public static CatService sCatService;
    private static CatApplication app;

    public static void addToFavorite(Breed info) {
        try (PrintWriter writer = new PrintWriter(new File(app.getFilesDir(), "data.txt"))) {
            breeds.addFirst(info);
            for (Breed it: breeds) {
                writer.println(gson.toJson(it));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(Breed info) {
        Breed toR = null;
        for (Breed it: breeds) {
            if (it.getId().equals(info.getId())) {
                toR = it;
                break;
            }
        }
        breeds.remove(toR);

        try (PrintWriter writer = new PrintWriter(new File(app.getFilesDir(), "data.txt"))) {
            for (Breed it: breeds) {
                writer.println(gson.toJson(it));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("x-api-key", "3b3fcc82-9ef1-4aac-8d94-dd841b6ddb13").build();
                return chain.proceed(request);
            }
        });

//        https://api.thecatapi.com/v1/images/search?breed_ids=beng
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create()).
                baseUrl("https://api.thecatapi.com/v1/").
                client(httpClient.build()).
                build();
        sCatService = retrofit.create(CatService.class);


        try {
            File file = new File(getFilesDir(), "data.txt");
            if (!file.exists()) {
                file.createNewFile();
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                Breed info = gson.fromJson(line, Breed.class);
                breeds.add(info);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
