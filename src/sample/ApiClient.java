package sample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiClient {
    @GET("api/v1/comments")
    Call<List<Comment>> getPosts(@Query("size") int size);

    @POST("api/v1/comments")
    Call<Comment> addPost(@Body Comment comment);
}