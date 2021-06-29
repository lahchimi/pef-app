package com.aymsou.rstaurantsapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.aymsou.rstaurantsapp.fragments.MapsActivity;
import com.aymsou.rstaurantsapp.utils.ApiService;
import com.aymsou.rstaurantsapp.utils.RetroClient;
import com.google.android.gms.maps.MapFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aymsou.rstaurantsapp.adapters.TabsVPAdapter;
import com.aymsou.rstaurantsapp.fragments.CatsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        TabsVPAdapter adapter = new TabsVPAdapter(getSupportFragmentManager());

        adapter.addFragment(new CatsFragment(), "CATEGORIES");
        adapter.addFragment(new MapsActivity(), "CARTE");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.format_list_bulleted);
        tabLayout.getTabAt(1).setIcon(R.drawable.google_maps);


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
    public boolean onNavigationItemSelected(MenuItem item) {

        Log.d("MainActi debug abdel:", "onNavigationItemSelected: ");
        int id = item.getItemId();
        if(id == R.id.nav_contact){
            Log.d("MainActi debug abdel:", "nav_contact ");
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.message_form, null);
            final EditText inputEmail = (EditText)dialogView.findViewById(R.id.inputEmail);
            final EditText inputFullname = (EditText)dialogView.findViewById(R.id.inputFullname);
            final EditText inputMessage = (EditText)dialogView.findViewById(R.id.inputMessage);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Contactez nous");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(inputEmail.getText().toString().trim().isEmpty() || inputFullname.getText().toString().trim().isEmpty() || inputMessage.getText().toString().trim().isEmpty())
                        return;
                    final ProgressDialog progress = new ProgressDialog(MainActivity.this);
                    progress.setMessage(getResources().getString(R.string.please_wait));
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.show();
                    ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
                    Call<String> call = api.sendMessage(inputEmail.getText().toString(), inputFullname.getText().toString(), inputMessage.getText().toString());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                progress.dismiss();
                                Toast.makeText(MainActivity.this, "Message à été envoyeé avec succes", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }


        //int id = item.getItemId();
        if(id == R.id.add_restaurant){
            Log.d("Main add_restaur abdel:", "add_restaurant ");
            startActivity(new Intent(MainActivity.this, AddRestaurantReq.class));
        }else if(id == R.id.nav_contact){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.message_form, null);
            final EditText inputEmail = (EditText)dialogView.findViewById(R.id.inputEmail);
            final EditText inputFullname = (EditText)dialogView.findViewById(R.id.inputFullname);
            final EditText inputMessage = (EditText)dialogView.findViewById(R.id.inputMessage);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Contactez nous");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(inputEmail.getText().toString().trim().isEmpty() || inputFullname.getText().toString().trim().isEmpty() || inputMessage.getText().toString().trim().isEmpty())
                        return;
                    final ProgressDialog progress = new ProgressDialog(MainActivity.this);
                    progress.setMessage(getResources().getString(R.string.please_wait));
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.show();
                    ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
                    Call<String> call = api.sendMessage(inputEmail.getText().toString(), inputFullname.getText().toString(), inputMessage.getText().toString());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                progress.dismiss();
                                Toast.makeText(MainActivity.this, "Message à été envoyeé avec succes", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }else if(id == R.id.nav_about){
            Log.d("Main nav_about abdel:", "nav_about ");
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("à propos");
            alertDialog.setMessage(getResources().getString(R.string.about_text));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else if(id == R.id.bookmarks) {
            Log.d("Main bookmarks abdel:", "bookmarks ");
            startActivity(new Intent(MainActivity.this, Bookmarks.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
