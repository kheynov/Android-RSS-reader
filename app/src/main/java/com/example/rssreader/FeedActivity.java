package com.example.rssreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.lang.reflect.Array;
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
    private class DownloadRSS extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("INFO", "Starting XML downloading...");
            progressBar.setVisibility(ProgressBar.VISIBLE);
            updatingText.setVisibility(TextView.VISIBLE);
            feedList.setVisibility(ListView.INVISIBLE);
            updatingText.setText("Обновляем ленту");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                java.net.URL url = new URL(strings[0]);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
//                doc.getDocumentElement().normalize();
                Log.d("INFO", "document loaded");
                Log.d("DOC", doc.toString());
                nodeList = doc.getElementsByTagName("item");
                Log.d("NODELIST LENGTH", Integer.toString(nodeList.getLength()));
            } catch (Exception e) {
                Log.e("ERROR", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listItems.clear();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Log.d("DOC ELEMENT", element.toString());
                    listItems.add(new ListItem(getNode("title", element),
                            getNode("pubDate", element),
                            getNode("description", element)));
                    Log.i("GET LIST OF ITEMS", listItems.toString());
                    ListItemAdapter listAdapter = new ListItemAdapter(getApplicationContext(), listItems);
                    feedList.setAdapter(listAdapter);
                    feedList.setOnItemClickListener((parent, view, position, id) -> {
                        final ListItem listItem = listItems.get(position);
                        //TODO: добавить обработку нажатий на новость
                    });
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    updatingText.setVisibility(TextView.INVISIBLE);
                    feedList.setVisibility(ListView.VISIBLE);
                }
            }
        }
        private String getNode(String tag, Element element) {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node value = nodeList.item(0);
            return value.getNodeValue();
        }
    }
}