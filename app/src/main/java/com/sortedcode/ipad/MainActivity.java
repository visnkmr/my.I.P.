package com.sortedcode.ipad;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipaddr = findViewById(R.id.ipaddr);
        dnametv = findViewById(R.id.dname);
        cnametv = findViewById(R.id.cname);
        pcodetv = findViewById(R.id.pcode);
        radtv = findViewById(R.id.rad);
        citynametv = findViewById(R.id.cityname);
        t1 = findViewById(R.id.text1);
        t2 = findViewById(R.id.text2);
        refreshbtn = findViewById(R.id.refresh);
        dnametv.setText("Loading...");
        sendRequest();
        refreshbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dnametv.setText("Loading...");
                ipaddr.setText("");
                cnametv.setText("");
                citynametv.setText("");
                pcodetv.setText("");
                radtv.setText("");
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                sendRequest();
            }
        });
    }
    private void sendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {


                    // Formulate the request and handle the response.
                    @Override
                    public void onResponse(String response) {
                        String resultval = response;
                        Log.e(TAG, "Response from url: " + resultval);
                        JSONObject jObject, data;
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
                            String radu = rad + "km";
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
                        dnametv.setText("No Internet Connection!");
                        ipaddr.setText("");
                        cnametv.setText("");
                        citynametv.setText("");
                        pcodetv.setText("");
                        radtv.setText("");
                        t1.setVisibility(View.GONE);
                        t2.setVisibility(View.GONE);
                        // Handle error
                    }
                });

// Add the request to the RequestQueue.
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(stringRequest);
    }
}