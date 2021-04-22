package fcm.test.crud_test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import java.io.File;

import fcm.test.crud_test.listener.ListPostListener;
import fcm.test.crud_test.listener.UserTokenListener;
import fcm.test.crud_test.model.ListModel;
import fcm.test.crud_test.model.LoginModel;
import fcm.test.crud_test.model.data.UserToken;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

public class WriteActivity extends AppCompatActivity {

    private ListModel listModel;
    private LoginModel loginModel;
    SharedPreferences tokens;

    EditText edit_title;
    EditText edit_content;
    ImageView image;
    Button choose;
    Button post;

    Part inputImage;
    RequestBody title;
    RequestBody content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_layout);

        loginModel = new LoginModel();
        listModel = new ListModel();

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_content = (EditText) findViewById(R.id.edit_content);
        image = (ImageView) findViewById(R.id.image_view_write);
        choose = (Button) findViewById(R.id.choose_image);
        post = (Button) findViewById(R.id.post_btn);

        choose.setOnClickListener(v -> {
            verifyStoragePermissions(this);

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, 0);
        });

        post.setOnClickListener(v -> {
            title = RequestBody.create(MediaType.parse("multipart/form-data"), edit_title.getText().toString());
            content = RequestBody.create(MediaType.parse("multipart/form-data"), edit_content.getText().toString());

            String editTitle = edit_content.getText().toString();
            String editContent = edit_title.getText().toString();

            if(editTitle.length() < 0 || editContent.length() < 0) {
                Toast.makeText(this, "제목 혹은 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if(inputImage == null) {
                Toast.makeText(this, "이미지를 첨부해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            listModel.postList(getToken(), title, content, inputImage, new ListPostListener() {
                @Override
                public void postList() {
                    listPost();
                }

                @Override
                public void onFail() {
                    retryPost();
                }

                @Override
                public void notFoundUser() {

                }
            });
        });
    }

    @SuppressLint("ShowToast")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    assert data != null;

                    Uri uri = data.getData();
                    File file = new File(getRealPathFromURI(uri));

                    //Bitmap bitmap = BitmapFactory.decodeFile(getRealPathFromURI(uri));

                    Log.d("file size", Long.toString(file.length()));

                    if(file.length() > 1048576) {
                        Toast.makeText(this, "파일 크기가 너무 큽니다. 다시선택해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    inputImage = Part.createFormData("image", file.getName(), requestBody);

                    Log.d("EditProfileActivity", "onActivityResult: " + getRealPathFromURI(uri));

                    image.setImageURI(uri);
                } catch (Exception ignored) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();

        assert cursor != null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

    public void listPost() {
        Toast.makeText(this, "게시물 작성하셨습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public String getToken() {
        tokens = getSharedPreferences("token", MODE_PRIVATE);
        return tokens.getString("accessToken", "null");
    }

    public void setNewToken(UserToken userToken) {
        tokens = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = tokens.edit();
        editor.putString("accessToken", userToken.getAccessToken());
        editor.putString("refreshToken", userToken.getRefreshToken());
        editor.apply();
    }

    @SuppressLint("ShowToast")
    public void refreshTokenFail() {
        Intent intent = new Intent(this, LoginActivity.class);
        Toast.makeText(this, "토큰이 만료되었습니다", Toast.LENGTH_SHORT);
        startActivity(intent);
        finish();
    }

    public void retryPost() {
        tokens = getSharedPreferences("token", MODE_PRIVATE);

        loginModel.getNewToken(tokens.getString("refreshToken", "null"), new UserTokenListener() {
            @Override
            public void loadToken(UserToken userToken) {
                setNewToken(userToken);
            }

            @Override
            public void onFail() {
                refreshTokenFail();
            }
        });

        listModel.postList(getToken(), title, content, inputImage, new ListPostListener() {
            @Override
            public void postList() {
                postList();
            }

            @Override
            public void onFail() {
                refreshTokenFail();
            }

            @Override
            public void notFoundUser() {

            }
        });

        Toast.makeText(this, "게시물 작성하셨습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
