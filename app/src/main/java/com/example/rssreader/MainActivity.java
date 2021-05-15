package com.example.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText rss_channel;
    Button button_load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rss_channel =  findViewById(R.id.rss_channel_edit_text);
        button_load = findViewById(R.id.load_news_button);

        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                intent.putExtra("URL", rss_channel.getText());
                startActivity(intent);
            }
        });
    }
}