package com.example.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText rss_channel;
    Button button_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rss_channel = findViewById(R.id.rss_channel_edit_text);//RSS URL input field
        button_load = findViewById(R.id.load_news_button);

        button_load.setOnClickListener(v -> {
            if (URLUtil.isValidUrl(rss_channel.getText().toString())) {//URL validating
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                intent.putExtra("URL", rss_channel.getText());
                startActivity(intent);
            } else {
                Toast.makeText(this, getResources().getString(R.string.enter_valid_url), Toast.LENGTH_SHORT).show();
            }
        });
    }
}