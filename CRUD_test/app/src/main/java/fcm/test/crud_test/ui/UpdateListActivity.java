package fcm.test.crud_test.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import fcm.test.crud_test.R;
import fcm.test.crud_test.listener.ListPostListener;
import fcm.test.crud_test.listener.UserTokenListener;
import fcm.test.crud_test.model.ListModel;
import fcm.test.crud_test.model.LoginModel;
import fcm.test.crud_test.model.data.UpdateList;
import fcm.test.crud_test.model.data.UserToken;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateListActivity extends AppCompatActivity {

    LoginModel loginModel;
    ListModel listModel;

    SharedPreferences tokens;

    EditText edit_title;
    EditText edit_content;
    ImageView image;
    Button choose;
    Button post;

    MultipartBody.Part inputImage;

    Integer listId;
    String imageName;
    String title;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list);

        loginModel = new LoginModel();
        listModel = new ListModel();

        edit_title = (EditText) findViewById(R.id.edit_title_update);
        edit_content = (EditText) findViewById(R.id.edit_content_update);
        image = (ImageView) findViewById(R.id.image_view_update);
        choose = (Button) findViewById(R.id.choose_image_update);
        post = (Button) findViewById(R.id.upadte_btn);

        Intent getView = getIntent();
        this.listId = getView.getIntExtra("listId", 0);
        this.imageName = getView.getStringExtra("imageName");

        choose.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, 0);
        });

        post.setOnClickListener(v -> {
            title = edit_title.getText().toString();
            if(title.length() < 1) {
                title = null;
            }
            content = edit_content.getText().toString();
            if(content.length() < 1) {
                content = null;
            }

            listModel.updateList(getToken(), new UpdateList(title, content), listId, new ListPostListener() {
                @Override
                public void postList() {
                    listUpdate();
                }

                @Override
                public void onFail() {
                    retryUpdateList();
                }

                @Override
                public void notFoundUser() {

                }
            });
            if(inputImage != null) {
                listModel.updateListImage(getToken(), inputImage, imageName, new ListPostListener() {
                    @Override
                    public void postList() {

                    }

                    @Override
                    public void onFail() {
                        retryUpdateListImage();
                    }

                    @Override
                    public void notFoundUser() {

                    }
                });
            }
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
                    inputImage = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

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

    public void retryUpdateList() {
        loginModel.getNewToken(getRefreshToken(), new UserTokenListener() {
            @Override
            public void loadToken(UserToken userToken) {
                setNewToken(userToken);
            }

            @Override
            public void onFail() {
                refreshTokenFail();
            }
        });

        listModel.updateList(getToken(), new UpdateList(title, content), listId, new ListPostListener() {
            @Override
            public void postList() {
                listUpdate();
            }

            @Override
            public void onFail() {
                refreshTokenFail();
            }

            @Override
            public void notFoundUser() {

            }
        });
    }

    public void retryUpdateListImage() {
        loginModel.getNewToken(getRefreshToken(), new UserTokenListener() {
            @Override
            public void loadToken(UserToken userToken) {
                setNewToken(userToken);
            }

            @Override
            public void onFail() {
                refreshTokenFail();
            }
        });

        listModel.updateListImage(getToken(), inputImage, imageName, new ListPostListener() {
            @Override
            public void postList() {

            }

            @Override
            public void onFail() {
                refreshTokenFail();
            }

            @Override
            public void notFoundUser() {

            }
        });
    }

    public void listUpdate() {
        Toast.makeText(this, "게시물 수정하셨습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, GetActivity.class);
        startActivity(intent);
        finish();
    }

    public String getToken() {
        tokens = getSharedPreferences("token", MODE_PRIVATE);
        return tokens.getString("accessToken", "null");
    }

    public String getRefreshToken() {
        tokens = getSharedPreferences("token", MODE_PRIVATE);
        return tokens.getString("refreshToken", "null");
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
}