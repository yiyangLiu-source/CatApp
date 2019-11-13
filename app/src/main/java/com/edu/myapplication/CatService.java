package com.edu.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heleninsa on 2019-11-12.
 *
 * @author heleninsa
 */
public interface CatService {

    @GET("breeds/search")
    Call<List<Breed>> search(@Query("q") String query);

    @GET("images/search")
    Call<List<ImgResult>> img(@Query("breed_ids") String bid);


}
