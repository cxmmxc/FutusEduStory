package com.terry;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.common.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String doc = msg.getData().getString("doc");
            textview.setText(doc);
        }
    };

    private Toolbar mToobar;

    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textview);
        textview.setMovementMethod(ScrollingMovementMethod.getInstance());
        mToobar = (Toolbar) findViewById(R.id.main_toobar);
        mToobar.setTitle("Mytitle");
        setSupportActionBar(mToobar);
        initData();
    }

    private void initData() {

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
//            File file = new File(Environment.getExternalStorageDirectory() + "/futus.txt");
//
//            try {
//                if(!file.exists()) {
//                    file.createNewFile();
//                }
//                FileWriter writer = new FileWriter(file.getAbsolutePath());
//                BufferedWriter bufferedWriter = new BufferedWriter(writer);
//                bufferedWriter.write(s);
//                bufferedWriter.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Document document = null;
            try {
                document = Jsoup.connect("http://www.qbaobei.com/hot/jiaoyu/tj/tjgs/List_1.html").timeout(9000)
                        .get();
                Element element = document.select("ul.list-conBox-ul").first();
                int toString = element.children().size();
                LogUtil.v(toString+"");
//                Elements elements = document.getElementsByClass("ulTextlist_2 clear");
//                Element page = document.select("div.page").first();
//                Elements children = page.children();
//                if(children.size() == 1 && "prev".equals(children.first().attr("class"))) {
//                    Log.i("cxm","the last one");
//                }
//
//                Elements elements = document.select("[class]");
//                for(Element element : elements){
//                    if(element == null){
//                        Log.v("cxm", "null");
//                    }else {
//                        String className = element.className();
//                        if("index-ul".equals(className)) {
////                            Log.v("cxm", "className="+className+"~~~~~~"+ element.toString()+"\n\n");
//
//                            Elements elements1 = element.select("li");
//                            Log.v("cxm", "size=" + elements1.size());
//                            for (Element child : elements1) {
//                                Element href = child.select("[href]").first();
//                                String name = href.text();
//                                Element img = child.select("img[src]").first();
//                                Log.w("cxm", "href=" + href.attr("href") + " ~~ name=" + name + " ~~ img=");
//                                if(null == img) {
//                                    Log.e("cxm", "img = null");
//                                }
//                            }
////                            Elements children = element.children();
////                            for( Element child : children) {
////                                Elements href = child.getElementsByAttribute("href");
////                                Element first = href.first();
////                                Log.v("cxm", first.text());
////
////                            }
//                        }
//                    }
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(document != null) {
                return document.toString();
            }
            return null;
        }
    }

//    class AsyTask extends AsyncTask<Void, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            textview.setText(s);
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            Document document = null;
//            try {
//                document = Jsoup.connect("http://www.haoyunbb.com/gushi/1.html").timeout(5000)
//                            .post();
////                Elements elements = document.getElementsByClass("ulTextlist_2 clear");
//                Elements elements = document.select("[class]");
//                for(Element element : elements){
//                    if(element == null){
//                        Log.v("cxm", "null");
//                    }else {
//                        String className = element.className();
//                        if("ulTextlist_2 clear".equals(className)) {
////                            Log.v("cxm", "className="+className+"~~~~~~"+ element.toString()+"\n\n");
//
//                            Elements children = element.children();
//                            for( Element child : children) {
//                                Elements href = child.getElementsByAttribute("href");
//                                Element first = href.first();
//                                Log.v("cxm", first.text());
//
//                            }
//                        }
//                    }
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return document.html();
//        }
//    }

}
