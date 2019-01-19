package com.droppages.pedrohenrique.pocketcoin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.droppages.pedrohenrique.pocketcoin.ConfiguracaoFragment;
import com.droppages.pedrohenrique.pocketcoin.HomeFragment;
import com.droppages.pedrohenrique.pocketcoin.LogFragment;
import com.droppages.pedrohenrique.pocketcoin.R;
import com.droppages.pedrohenrique.pocketcoin.view.CarteiraActivity;
import com.droppages.pedrohenrique.pocketcoin.view.CategoriaActivity;
import com.droppages.pedrohenrique.pocketcoin.view.TagActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar                 toolbar;
    private FloatingActionButton    fab;
    private DrawerLayout            drawer;
    private ActionBarDrawerToggle   toggle;
    private NavigationView          navigationView;
    private FrameLayout             frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind
        toolbar     = findViewById(R.id.toolbar);
        drawer      = findViewById(R.id.drawer_layout);
        fab         = findViewById(R.id.fab);
        frameLayout = findViewById(R.id.frame_layout);

        // FloatActionButton
        setSupportActionBar(toolbar);
        fab.setOnClickListener(
                c -> Snackbar.make(this.navigationView, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        );

        // AppBar
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Menu navegação
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Inicia fragmento inicial
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new LogFragment()).commit();
        } else if (id == R.id.nav_slideshow) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ConfiguracaoFragment()).commit();
        } else if (id == R.id.nav_manage) {
            sair();
        } else if (id == R.id.nav_share) {
            return true;
        } else if (id == R.id.nav_send) {
            return true;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void cadastrarCarteira(){
        startActivity(new Intent(this, CarteiraActivity.class));
    }

    public void cadastrarCategoria(){ startActivity(new Intent(this, CategoriaActivity.class)); }

    public void cadastrarTag(){ startActivity(new Intent(this, TagActivity.class)); }

    public void sair(){ this.finish(); }
}