package fcm.test.crud_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainPageActivity extends AppCompatActivity {

    Button write;
    Button get;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_layout);

        write = (Button) findViewById(R.id.write_btn);
        get = (Button) findViewById(R.id.get_btn);

        write.setOnClickListener(v -> {
            Intent intent = new Intent(this, WriteActivity.class);
            startActivity(intent);
        });

        get.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetActivity.class);
            startActivity(intent);
        });
    }
}
