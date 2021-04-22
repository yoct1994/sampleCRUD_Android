package fcm.test.crud_test.model.service;

import java.util.List;

import fcm.test.crud_test.model.data.GetPost;
import fcm.test.crud_test.model.data.UpdateList;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WriteService {
    @Multipart
    @POST("list")
    Call<Void> postList(
            @Part("title") RequestBody title,
            @Part("content")RequestBody content,
            @Part MultipartBody.Part image,
            @Header("Authorization") String token
    );

    @GET("list")
    Call<List<GetPost>> getList(@Header("Authorization")String token);

    @PUT("list/{listId}")
    Call<Void> updateList(@Body UpdateList updateList,
                          @Header("Authorization") String token,
                          @Path("listId") Integer listId);

    @Multipart
    @PUT("list/image/{imageName}")
    Call<Void> updateImageList(@Header("Authorization") String token,
                               @Path("imageName") String imageName,
                               @Part MultipartBody.Part image);

    @DELETE("list/{listId}")
    Call<Void> deleteList(@Header("Authorization") String token,
                          @Path("listId") Integer listId);

    @DELETE("list/image/{imageName}")
    Call<Void> deleteListImage(@Header("Authorization") String token,
                               @Path("imageName") String imageName);
}
