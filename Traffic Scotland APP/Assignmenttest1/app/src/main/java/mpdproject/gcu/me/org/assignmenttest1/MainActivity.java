//
//
// Starter code for the Mobile Platform Development Assignment
// Seesion 2017/2018
//
//
package mpdproject.gcu.me.org.assignmenttest1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button current;
    Button roadworks;
    Button planned;
    //Button floods;

    String url1 = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    String url2 = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    String url3 = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    //String url4 = "http://floodline.sepa.org.uk/feed/";

    String result;
    String titles;
    String descriptions;
    String links;
    String pubDate;

    String info;

    TextView urlInput;
    ScrollView scrollView;

    List<String> list;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Traffic Scotland RSS Feed");

        urlInput = (TextView) findViewById(R.id.urlInput);
        scrollView = (ScrollView)findViewById(R.id.parentscreen);
        scrollView.setBackgroundColor(getResources().getColor(R.color.black, null));

        current = (Button) findViewById(R.id.current);
        current.setOnClickListener(this);

        roadworks = (Button) findViewById(R.id.roadworks);
        roadworks.setOnClickListener(this);

        planned = (Button) findViewById(R.id.planned);
        planned.setOnClickListener(this);

        /*floods = (Button) findViewById(R.id.planned);
        floods.setOnClickListener(this);*/
    }

    /**
     *
     * @param aview
     */
    @Override
    public void onClick(View aview) {
        if (aview == current.findViewById(R.id.current)) {
            startProgress1();
        } else if (aview == roadworks.findViewById(R.id.roadworks)) {
            startProgress2();
        } else if (aview == planned.findViewById(R.id.planned)) {
            startProgress3();
        } /*else if (aview == planned.findViewById(R.id.floods)) {
            startProgress4();
        }*/
    }

    public void startProgress1() {
        Toast.makeText(getApplicationContext(), "Alan Thomas Moore\nS1436102\nCurrent Incidents", Toast.LENGTH_LONG).show();
        urlInput.setText("Alan Thomas Moore\n\nS1436102\n\nGetting Current Incidents Information...");
        Log.e("INFO", "urlInput: cleared" );
        Log.e("INFO", "Currently fetching: " + url1);
        setTitle("Current Incidents - S1436102");
        new Thread(new Task(url1)).start();
    } //

    public void startProgress2() {
        Toast.makeText(getApplicationContext(), "Alan Thomas Moore\nS1436102\nRoadworks", Toast.LENGTH_LONG).show();
        urlInput.setText("Alan Thomas Moore\n\nS1436102\n\nGetting Roadworks Information...");
        Log.e("INFO", "urlInput: cleared" );
        Log.e("INFO", "Currently fetching: " + url2);
        setTitle("Roadworks - S1436102");
        new Thread(new Task(url2)).start();
    } //

    public void startProgress3() {
        Toast.makeText(getApplicationContext(), "Alan Thomas Moore\nS1436102\nPlanned Roadworks", Toast.LENGTH_LONG).show();
        urlInput.setText("Alan Thomas Moore\n\nS1436102\n\nGetting Planned Roadworks Information...");
        Log.e("INFO", "urlInput: cleared" );
        Log.e("INFO", "Currently fetching: " + url3);
        setTitle("Planned Roadworks - S1436102");
        new Thread(new Task(url3)).start();
    } //

    /*public void startProgress4() {
        Toast.makeText(getApplicationContext(), "Roadworks", Toast.LENGTH_LONG).show();
        urlInput.setText("Getting Floodline Information...");
        new Thread(new Task(url4)).start();
    } */

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    class Task extends AsyncTask<Integer, Integer, String> implements Runnable {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        Exception exception = null;

        String url;

        /**
         *
         * @param aurl
         */
        public Task(String aurl) {
            url = aurl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Be patient it is working...");
            progressDialog.show();
        }

        @Override
        public void run() {

            result = "";

            try {
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                xmlPullParserFactory.setNamespaceAware(false);
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(getInputStream(new URL(url)), null);
                xmlPullParser.nextTag();

                boolean insideItem = false;
                list= new ArrayList<>();

                int eventType = xmlPullParser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        if (xmlPullParser.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles = xmlPullParser.nextText();
                            }
                        } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                descriptions = xmlPullParser.nextText();
                            }
                        } else if (xmlPullParser.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links = xmlPullParser.nextText();
                            }
                        } else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem) {
                                pubDate = xmlPullParser.nextText();
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {

                        insideItem = false;
                        info = titles + "\n \n" + descriptions + "\n \n" + links + "\n \n" + "Date Published: " + "\n" + pubDate + "\n \n" + "___________________________________________" + "\n \n";
                        list.add(info);
                    }
                    eventType = xmlPullParser.next();
                }

            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < list.size(); i++) {
                result += list.get(i);
                i++;
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    urlInput.setText(result);
                }
            });
        }

        @Override
        protected String doInBackground(Integer... integers) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }
}