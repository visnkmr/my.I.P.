package com.sortedcode.ipad;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    // Will show the string "data" that holds the results
    TextView ipaddr,dnametv,cnametv,pcodetv,radtv,citynametv,t1,t2;
    Button refreshbtn;
    // URL of object to be parsed
    String url = "https://ipvigilante.com/json/full";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue mRequestQueue;
        ipaddr=findViewById(R.id.ipaddr);
        dnametv=findViewById(R.id.dname);
        cnametv=findViewById(R.id.cname);
        pcodetv =findViewById(R.id.pcode);
        radtv=findViewById(R.id.rad);
        citynametv=findViewById(R.id.cityname);
        t1=findViewById(R.id.text1);
        t2=findViewById(R.id.text2);
        refreshbtn=findViewById(R.id.refresh);

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();

// Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String resultval =response;
                        Log.e(TAG, "Response from url: " + resultval);
                        JSONObject jObject,data;
                        try {
                            jObject = new JSONObject(response);
                            String mResponse = jObject.getString("data");
                            data = new JSONObject(mResponse);
                            String ipv4 = data.getString("ipv4");
                            ipaddr.setText(ipv4);
                            String cname = data.getString("country_name");
                            cnametv.setText(cname);
                            String dname = data.getString("subdivision_1_name");
                            dnametv.setText(dname);
                            String cityname = data.getString("city_name");
                            citynametv.setText(cityname);
                            String pcode = data.getString("postal_code");
                            pcodetv.setText(pcode);
                            String rad = data.getString("accuracy_radius");
                            String radu=rad+"km";
                            radtv.setText(radu);
                            t1.setVisibility(View.VISIBLE);
                            t2.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        // Do something with the response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      dnametv.setText("No Internet Connection!");  // Handle error
                    }
                });

// Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
}