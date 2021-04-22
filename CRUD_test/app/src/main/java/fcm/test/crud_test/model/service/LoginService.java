package fcm.test.crud_test.model.service;

import fcm.test.crud_test.model.data.SignUpRequest;
import fcm.test.crud_test.model.data.UserToken;
import fcm.test.crud_test.model.dto.SignInRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface LoginService {
    @POST("auth")
    Call<UserToken> getToken(@Body SignInRequest signInRequest);

    @PUT("auth")
    Call<UserToken> getNewToken(@Header("X-Refresh-Token") String refreshToken);

    @POST("user")
    Call<Void> signUp(@Body SignUpRequest signUpRequest);
}