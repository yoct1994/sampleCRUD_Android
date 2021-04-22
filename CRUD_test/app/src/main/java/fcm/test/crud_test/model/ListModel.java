package fcm.test.crud_test.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import fcm.test.crud_test.listener.GetListListener;
import fcm.test.crud_test.listener.ListPostListener;
import fcm.test.crud_test.model.data.GetPost;
import fcm.test.crud_test.model.data.UpdateList;
import fcm.test.crud_test.model.service.WriteService;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListModel {

    private WriteService writeService;

    public ListModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://220.90.237.33:7004/")
                .build();
        writeService = retrofit.create(WriteService.class);
    }

    public void postList(String token, RequestBody title, RequestBody content, MultipartBody.Part image, ListPostListener listPostListener) {
        Call<Void> call = writeService.postList(title, content, image, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("list", Integer.toString(response.code()));
                if(response.code() == 200) listPostListener.postList();
                if(response.code() == 403) listPostListener.onFail();
                if(response.code() == 404) listPostListener.notFoundUser();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("fail", "실패실패");
                Log.d("fail_reason", t.getMessage());
            }
        });
    }

    public void getList(String token, GetListListener getListListener) {
        Call<List<GetPost>> call = writeService.getList(token);
        call.enqueue(new Callback<List<GetPost>>() {
            @Override
            public void onResponse(Call<List<GetPost>> call, Response<List<GetPost>> response) {
                Log.d("get", Integer.toString(response.code()));
                if(response.code() == 200) getListListener.loadList(response.body());
                if(response.code() == 403) getListListener.onFail();
            }

            @Override
            public void onFailure(Call<List<GetPost>> call, Throwable t) {
                Log.d("getFail", t.getMessage());
            }
        });
    }

    public void updateList(String token, UpdateList updateList, Integer listId, ListPostListener listPostListener) {
        Call<Void> call = writeService.updateList(updateList, token, listId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("updateList", Integer.toString(response.code()));
                if(response.code() == 200) listPostListener.postList();
                if(response.code() == 403) listPostListener.onFail();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("updateListFail", t.getMessage());
            }
        });
    }

    public void updateListImage(String token, MultipartBody.Part image, String imageName, ListPostListener listPostListener) {
        Call<Void> call = writeService.updateImageList(token, imageName, image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("updateImage", Integer.toString(response.code()));
                if(response.code() == 200) listPostListener.postList();
                if(response.code() == 403) listPostListener.onFail();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("updateFail", t.getMessage());
            }
        });
    }

    public void deleteList(String token, Integer listId, ListPostListener listPostListener) {
        Call<Void> call = writeService.deleteList(token, listId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("delete", Integer.toString(response.code()));
                if(response.code() == 200) listPostListener.postList();
                if(response.code() == 403) listPostListener.onFail();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("deleteFail", t.getMessage());
            }
        });
    }
}
