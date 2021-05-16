package com.example.rssreader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FeedActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView updatingText;
    ListView feedList;
    NodeList nodeList;

    ArrayList<ListItem> listItems = new ArrayList<>();
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        URL = getIntent().getExtras().get("URL").toString();
        updatingText = findViewById(R.id.updatingText);
        progressBar = findViewById(R.id.progressBar);
        feedList = findViewById(R.id.feed_list);
        new DownloadRSS().execute(URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh)
            new DownloadRSS().execute(URL);//If "Refresh" button pressed
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadRSS extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (BuildConfig.DEBUG) Log.d("INFO", "Starting XML downloading");
            progressBar.setVisibility(ProgressBar.VISIBLE);
            updatingText.setVisibility(TextView.VISIBLE);//Hiding list and showing progress bar
            feedList.setVisibility(ListView.INVISIBLE);
            updatingText.setText(getResources().getString(R.string.updating_text));
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                java.net.URL url = new URL(strings[0]);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
//                if (BuildConfig.DEBUG) Log.d("INFO", "document loaded");
                nodeList = doc.getElementsByTagName("item");//collecting all elements with tag "item"
//                if (BuildConfig.DEBUG) Log.d("NODELIST LENGTH", Integer.toString(nodeList.getLength()));
            } catch (Exception e) {
//                if (BuildConfig.DEBUG) Log.e("ERROR", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listItems.clear();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);//enumerating all "items"
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    listItems.add(new ListItem(getNode("title", element),//getting title, pubdate, description, link from RSS item
                            getNode("pubDate", element),
                            getNode("description", element),
                            getNode("link", element)));
                    ListItemAdapter listAdapter = new ListItemAdapter(getApplicationContext(), listItems);
                    feedList.setAdapter(listAdapter);
                    feedList.setOnItemClickListener((parent, view, position, id) -> {
                        final ListItem listItem = listItems.get(position);
                        openURI(listItem.getLink());// opening link in web browser
                    });
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    updatingText.setVisibility(TextView.INVISIBLE);//hiding progress bar and showing list
                    feedList.setVisibility(ListView.VISIBLE);
                }
            }
        }

        private String getNode(String tag, Element element) {//get child element from "item" by tag
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node value = nodeList.item(0);
            return value.getNodeValue();
        }
    }

    void openURI(String URL) {
        Uri uri = Uri.parse(URL);
//        if (BuildConfig.DEBUG) Log.d("INFO", "Opening URL: " + URL);
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(openLinkIntent);
    }
}