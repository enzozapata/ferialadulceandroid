package com.enzozapata.ladulce;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enzozapata.ladulce.data.models.Posts;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class activity_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Posts> list = new ArrayList<>();
    private ImageView imgView;
    private String id_fb;
    private TextView txtNombre;
    private TextView txtPuestoRango;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("La Dulce");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        navigationView.getMenu().add(0,0, 0, "Probando menu.");
        imgView = (ImageView) hView.findViewById(R.id.imageView);
        MyApp app = ((MyApp) this.getApplication());
        id_fb = app.getId_fb();
        txtPuestoRango = (TextView) hView.findViewById(R.id.txtPuestoRango);
        txtNombre = (TextView) hView.findViewById(R.id.txtNombres);
        txtPuestoRango.setText(app.getRango()+": "+app.getPuesto());
        txtNombre.setText(app.getNombre_apellido());
        Glide.with(this.getApplicationContext()).load("https://graph.facebook.com/" +id_fb+ "/picture?type=small").apply(RequestOptions.circleCropTransform()).into(imgView);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, activity_new.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
