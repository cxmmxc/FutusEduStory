package com.terry;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String doc = msg.getData().getString("doc");
            textview.setText(doc);
        }
    };

    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textview);
        textview.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyTask().execute();
    }

    class AsyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textview.setText(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            Document document = null;
            try {
                document = Jsoup.connect("http://www.haoyunbb.com/gushi/1.html").timeout(5000)
                            .post();
//                Elements elements = document.getElementsByClass("ulTextlist_2 clear");
                Elements elements = document.select("[class]");
                for(Element element : elements){
                    if(element == null){
                        Log.v("cxm", "null");
                    }else {
                        String className = element.className();
                        if("ulTextlist_2 clear".equals(className)) {
//                            Log.v("cxm", "className="+className+"~~~~~~"+ element.toString()+"\n\n");

                            Elements children = element.children();
                            for( Element child : children) {
                                Elements href = child.getElementsByAttribute("href");
                                Element first = href.first();
                                Log.v("cxm", first.text());
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return document.html();
        }
    }

}
