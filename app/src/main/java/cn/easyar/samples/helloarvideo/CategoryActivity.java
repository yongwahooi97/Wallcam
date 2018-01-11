package cn.easyar.samples.helloarvideo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    public static final String TAG = "eayar.samples.helloarvideo";
    public static String videoPath = "Wallcam.mp4";
    ListView listViewCategory;
    List<Category> caList;
    private ProgressDialog pDialog;
    private String toast;
    private static String GET_URL = "https://wallcam.000webhostapp.com/getAll.php";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listViewCategory = (ListView) findViewById(R.id.listViewRecords);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();

        BottomNavigationView bottomNavigationItemView = (BottomNavigationView) findViewById(R.id.NavBot);
        bottomNavigationItemView.setSelectedItemId(R.id.Category);
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.Scan) {
                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.Profile) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.About) {
                    Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        downloadCategory(getApplicationContext(), GET_URL);

        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = position;
                if(i == 0){  videoPath = "Brick.mp4"; toast="Brick pattern wallpaper"; }
                else if(i == 1){ videoPath = "Blue.mp4"; toast="Blue Star pattern wallpaper"; }
                else if(i == 2){ videoPath = "Wooden.mp4"; toast="Wooden pattern wallpaper";}
                else if(i ==3){ videoPath = "Word.mp4"; toast="Word pattern wallpaper"; }
                else{ videoPath = "Art.mp4"; toast="Art Photo"; }

                Toast.makeText(getApplicationContext(), toast + " is selected.\nPlease use the watermark to display. ", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }

        });
    }

    private void downloadCategory(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog.isShowing())
            pDialog.setMessage("Syn with server...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            caList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject categoryResponse = (JSONObject) response.get(i);
                                String id = categoryResponse.getString("id");
                                String url = categoryResponse.getString("url");
                                String description = categoryResponse.getString("description");
                                String price = categoryResponse.getString("price");
                                Category category = new Category(id, url, description, price);
                                caList.add(category);
                            }
                            loadCategory();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        volleyError.printStackTrace();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void loadCategory() {
        final CategoryAdapter adapter = new CategoryAdapter(this, R.layout.content_main, caList);
        listViewCategory.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(), "Count :" + caList.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    public String getVideoPath(){
        //Save sharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        String videoPath = sharedpreferences.getString("videoKey", "");
        return videoPath;
    }
}
