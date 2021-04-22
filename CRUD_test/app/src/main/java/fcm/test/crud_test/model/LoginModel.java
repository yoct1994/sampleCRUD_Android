package fcm.test.crud_test.model;

import android.util.Log;

import fcm.test.crud_test.listener.UserSignUpListener;
import fcm.test.crud_test.listener.UserTokenListener;
import fcm.test.crud_test.model.data.SignUpRequest;
import fcm.test.crud_test.model.data.UserToken;
import fcm.test.crud_test.model.dto.SignInRequest;
import fcm.test.crud_test.model.service.LoginService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModel {

    private LoginService loginService;

    public LoginModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://220.90.237.33:7004/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginService = retrofit.create(LoginService.class);
    }

    public void login(SignInRequest signInRequest, UserTokenListener userTokenListener) {
        Call<UserToken> call = loginService.getToken(signInRequest);
        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                Log.d("login", Integer.toString(response.code()));
                if(response.code() == 200) userTokenListener.loadToken(response.body());
                if(response.code() == 404) userTokenListener.onFail();
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {

            }
        });
    }

    public void getNewToken(String refreshToken, UserTokenListener userTokenListener) {
        Call<UserToken> call = loginService.getNewToken(refreshToken);
        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                if(response.code() == 200) userTokenListener.loadToken(response.body());
                if (response.code() == 404) userTokenListener.onFail();
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {

            }
        });

    }

    public void signUp(SignUpRequest signUpRequest, UserSignUpListener userSignUpListener) {
        Call<Void> call = loginService.signUp(signUpRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200) userSignUpListener.succeedSignUp();
                if(response.code() == 409) userSignUpListener.onFail();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
