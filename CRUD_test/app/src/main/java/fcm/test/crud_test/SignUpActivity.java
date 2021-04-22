package fcm.test.crud_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fcm.test.crud_test.listener.UserSignUpListener;
import fcm.test.crud_test.model.LoginModel;
import fcm.test.crud_test.model.data.SignUpRequest;

public class SignUpActivity extends AppCompatActivity {

    EditText ed_name, ed_id, ed_pw, ed_ckPW;
    Button signUp;

    final LoginModel loginModel = new LoginModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ed_name = findViewById(R.id.edit_name);
        ed_id = findViewById(R.id.edit_id);
        ed_pw = findViewById(R.id.edit_password);
        ed_ckPW = findViewById(R.id.edit_check_password);
        signUp = findViewById(R.id.sign_up_btn_real);

        signUp.setOnClickListener(v -> {
            String name = ed_name.getText().toString();
            String id = ed_id.getText().toString();
            String pw = ed_pw.getText().toString();
            String ck_pw = ed_ckPW.getText().toString();

            if(!pw.equals(ck_pw)) {
                Toast.makeText(this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            loginModel.signUp(new SignUpRequest(name, id, pw), new UserSignUpListener() {
                @Override
                public void succeedSignUp() {
                    succeed_SignUp();
                }

                @Override
                public void onFail() {
                    failSignUp();
                }
            });
        });
    }

    public void succeed_SignUp() {
        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void failSignUp() {
        Toast.makeText(this, "이미 유저가 존제합니다 로그인 혹은 다시 가입해주세요.", Toast.LENGTH_SHORT).show();
    }
}