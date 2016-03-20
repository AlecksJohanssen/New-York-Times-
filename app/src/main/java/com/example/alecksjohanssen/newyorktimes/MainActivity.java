package com.example.alecksjohanssen.newyorktimes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alecksjohanssen.newyorktimes.Activities.ArticleActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MainActivity extends AppCompatActivity {

    GridView gvResults;
    Button btnsearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    TextView mEditText;
    String type;
    String type2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isNetworkAvailable();
        isOnline();
        SendNetWorkRequest();
        setupViews();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticle();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    public boolean isOnline(){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            if(exitValue != 0)
            {
                Toast.makeText(MainActivity.this,"Could not connect to the Internet ",Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"Check your Internet Connection",Toast.LENGTH_SHORT).show();
            }
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo.toString() == null)
        {
            Toast.makeText(MainActivity.this,"Could not connect to the Network",Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this,"Check your Network Connection",Toast.LENGTH_SHORT).show();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                type = data.getStringExtra("selection");
                type2 = data.getStringExtra("date");
                Log.d("debug", type2);
            }
        }
    }

    private void showSettings() {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivityForResult(intent, 0, null);
        //startActivity(intent);
    }

    public void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResult);
        btnsearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", article);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    showSettings();

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.put("api-key", "67446d0bde26aebfed2261c9c950bc08:11:74724105");
        params.put("fq", type);
        params.put("page", 0);
        if (!TextUtils.isEmpty(type2)) {
            params.put("begin_date", type2);
        }
        return params;
    }

    public void onArticle() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        client.get(url, getParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleResults = null;
                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articleResults.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void SendNetWorkRequest() {
        AsyncHttpClient testOnline = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String url = "https://www.google.com/";
        testOnline.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MainActivity.this,"Could not Connect to the NewYork Times",Toast.LENGTH_SHORT).show();
                Log.e("Network error", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(MainActivity.this,"NewYork Times Connected",Toast.LENGTH_SHORT).show();
                Log.e("Connected", responseString);

            }
        });
    }
}
  //  public void onSportArticle(View view){
      //  String query = etQuery.getText().toString();
      //  AsyncHttpClient client2 = new AsyncHttpClient();
      //  String url ="http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=romney&facet_field=day_of_week&begin_date="+begin_date+"&end_date=20120101&api-key=67446d0bde26aebfed2261c9c950bc08:11:74724105"
        //http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=romney&api-key=67446d0bde26aebfed2261c9c950bc08:11:74724105




