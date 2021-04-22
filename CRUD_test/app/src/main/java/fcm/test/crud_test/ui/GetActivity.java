package fcm.test.crud_test.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import fcm.test.crud_test.R;
import fcm.test.crud_test.adapter.GetListAdapter;
import fcm.test.crud_test.listener.GetListListener;
import fcm.test.crud_test.listener.ListPostListener;
import fcm.test.crud_test.listener.UserTokenListener;
import fcm.test.crud_test.model.ListModel;
import fcm.test.crud_test.model.LoginModel;
import fcm.test.crud_test.model.data.GetPost;
import fcm.test.crud_test.model.data.UserToken;

public class GetActivity extends AppCompatActivity {

    private ListModel listModel;
    private LoginModel loginModel;
    private final GetListAdapter getListAdapter = new GetListAdapter();

    SharedPreferences tokens;

    RecyclerView re_list;
    SwipeRefreshLayout swipeRefreshLayout;

    Integer listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        listModel = new ListModel();
        loginModel = new LoginModel();

        re_list = (RecyclerView) findViewById(R.id.get_re_view);
        re_list.setAdapter(getListAdapter);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            listModel.getList(getToken(), new GetListListener() {
                @Override
                public void loadList(List<GetPost> lists) {
                    getList(lists);
                }

                @Override
                public void onFail() {
                    retryGetList();
                }
            });

            swipeRefreshLayout.setRefreshing(false);
        });

        listModel.getList(getToken(), new GetListListener() {
            @Override
            public void loadList(List<GetPost> lists) {
                getList(lists);
            }

            @Override
            public void onFail() {
                retryGetList();
            }
        });


        getListAdapter.setListener((viewHolder, view, position) -> {
            GetPost getPost = getListAdapter.getPost(position);
            listId = getPost.getListId();

            Log.d("user", getPost.getName());
            Log.d("isMine", Boolean.toString(getPost.isMine()));
            if(!getPost.isMine()) {
                Toast.makeText(this, "본인 게시글이 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("이 게시물을 어떻게 하시겠습니까?");
            alertDialog.setPositiveButton("수정", (dialog, which) -> {
                Toast.makeText(this, "수정합니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, UpdateListActivity.class);

                intent.putExtra("listId", getPost.getListId());
                intent.putExtra("imageName", getPost.getImageName());

                startActivity(intent);
                finish();
            });
            alertDialog.setNegativeButton("삭제", (dialog, which) -> {
                listModel.deleteList(getToken(), getPost.getListId(), new ListPostListener() {
                    @Override
                    public void postList() {
                        succeedDelete();
                    }

                    @Override
                    public void onFail() {
                        retryDelete();
                    }

                    @Override
                    public void notFoundUser() {

                    }
                });
            });

            AlertDialog listPopup = alertDialog.create();
            listPopup.setIcon(R.mipmap.ic_launcher_round);
            listPopup.setTitle("게시물 관리");
            listPopup.show();
        });
    }

    public void getList(List<GetPost> lists) {
        Log.d("list", lists.get(0).getTitle());
        getListAdapter.setList(lists);
    }

    @SuppressLint("ShowToast")
    public void refreshTokenFail() {
        Intent intent = new Intent(this, LoginActivity.class);
        Toast.makeText(this, "토큰이 만료되었습니다", Toast.LENGTH_SHORT);
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

    public void succeedDelete() {
        Toast.makeText(this, "삭제되셨습니다", Toast.LENGTH_SHORT).show();

        listModel.getList(getToken(), new GetListListener() {
            @Override
            public void loadList(List<GetPost> lists) {
                getList(lists);
            }

            @Override
            public void onFail() {
                retryGetList();
            }
        });
    }

    public void retryDelete() {
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

        listModel.deleteList(getToken(), listId, new ListPostListener() {
            @Override
            public void postList() {
                succeedDelete();
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

    public void retryGetList() {
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

        listModel.getList(getToken(), new GetListListener() {
            @Override
            public void loadList(List<GetPost> lists) {
                getList(lists);
            }

            @Override
            public void onFail() {
                refreshTokenFail();
            }
        });
    }
}