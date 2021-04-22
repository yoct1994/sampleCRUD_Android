package fcm.test.crud_test.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fcm.test.crud_test.R;
import fcm.test.crud_test.listener.UserTokenListener;
import fcm.test.crud_test.model.LoginModel;
import fcm.test.crud_test.model.data.UserToken;
import fcm.test.crud_test.model.dto.SignInRequest;

public class LoginActivity extends AppCompatActivity {

    private LoginModel loginModel;

    EditText id_edit;
    EditText password_edit;
    TextView signUp;
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        id_edit = (EditText) findViewById(R.id.id_edit);
        password_edit = (EditText) findViewById(R.id.pass_edit);
        login = (Button) findViewById(R.id.login_btn);
        signUp = (TextView) findViewById(R.id.sign_up);

        loginModel = new LoginModel();

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            String id = id_edit.getText().toString();
            String password = password_edit.getText().toString();

            if(id.length() < 1 || password.length() < 1) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            loginModel.login(new SignInRequest(id, password), new UserTokenListener() {
                @Override
                public void loadToken(UserToken userToken) {
                    setToken(userToken);
                }

                @Override
                public void onFail() {
                    loginFail();
                }
            });
        });
    }

    public void setToken(UserToken userToken) {
        SharedPreferences tokens = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = tokens.edit();
        editor.putString("accessToken", userToken.getAccessToken());
        editor.putString("refreshToken", userToken.getRefreshToken());
        editor.apply();

        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginFail() {
        Toast.makeText(this, "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
    }
}
