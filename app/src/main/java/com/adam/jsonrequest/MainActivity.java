package com.adam.jsonrequest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String NUTRITIONIX_URL = "https://trackapi.nutritionix.com/v2/search/instant?query=apple&branded=false";

    ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";

    private Button NutritionixButton;

    TextView txtV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtV = findViewById(R.id.tvMain2);
        txtV.setText("Text View before click button!");

        progressDialog = new ProgressDialog(this);
        NutritionixButton = findViewById(R.id.button_nutrition);


        NutritionixButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nutritionixGetObjectRequest(NUTRITIONIX_URL);
            }
        });
    }



    public void nutritionixGetObjectRequest(String url){
        String REQUEST_TAG ="beh";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                Log.d(TAG, response.toString());
                txtV.setText(response.toString());
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                txtV.setText(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-app-key","c030c944f416765d8674debb3322fc2d");
                headers.put("x-app-id", "7b43b860");
                return headers;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest,REQUEST_TAG);
    }




    public void volleyCacheRequest(String url){
        Cache cache = AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache();
        Cache.Entry reqEntry = cache.get(url);
        if(reqEntry != null){
            try {
                String data = new String(reqEntry.data, "UTF-8");
                //Handle the Data here.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{

            //Request Not present in cache, launch a network request instead.
        }
    }

    public void volleyInvalidateCache(String url){
        AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().invalidate(url, true);
    }

    public void volleyDeleteCache(String url){
        AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().remove(url);
    }

    public void volleyClearCache(){
        AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
    }



}
