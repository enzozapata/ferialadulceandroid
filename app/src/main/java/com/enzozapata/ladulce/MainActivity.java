package com.enzozapata.ladulce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.enzozapata.ladulce.data.models.Posts;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Posts> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        } else {
            AndroidNetworking.get("http://ladulce.enzozapata.com/wp-json/wp/v2/posts?_embed")
                    .setPriority(Priority.MEDIUM)
                    .setTag("Posts")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {

                                Posts post = new Posts();
                                JSONObject wppost = new JSONObject();
                                try {
                                    wppost = response.getJSONObject(i);
                                    JSONObject titulo = wppost.getJSONObject("title");
                                    post.setTitulo(titulo.getString("rendered"));
                                    post.setId(wppost.getString("id"));
                                } catch (JSONException error){
                                    showResponse(error.toString());
                                }
                                try{
                                    ObjectMapper mapper = new ObjectMapper();
                                    JsonNode node = mapper.readTree(wppost.toString());
                                    post.setFecha_pub(node.findPath("date").asText());
                                    post.setIcon(node.findPath("thumbnail").get("source_url").asText());
                                    post.setIcon_full(node.findPath("full").get("source_url").asText());
                                    post.setPrecio(node.findPath("precio_producto").asText());
                                } catch (java.io.IOException exep){
                                    showResponse(exep.toString());
                                }
                                list.add(i, post);
                            }
                                mAdapter = new MyAdapter(list);
                                recyclerView.setAdapter(mAdapter);
                        }
                        @Override
                        public void onError(ANError error) {
                            showResponse(error.getErrorBody());
                        }
                    });
        }
    }

    public void showResponse(String response) {
        Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG);
        toast.show();
    }
    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
