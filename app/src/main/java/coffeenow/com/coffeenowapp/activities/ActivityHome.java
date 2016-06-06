package coffeenow.com.coffeenowapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import coffeenow.com.coffeenowapp.CoffeeNowApp;
import coffeenow.com.coffeenowapp.fragments.FragmentCoffeeMakers;
import coffeenow.com.coffeenowapp.R;
import coffeenow.com.coffeenowapp.fragments.FragmentLogin;
import coffeenow.com.coffeenowapp.fragments.FragmentUsers;
import coffeenow.com.coffeenowapp.models.User;

public class ActivityHome extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FragmentUsers.OnFragmentInteractionListener,
        FragmentLogin.OnFragmentInteractionListener {

    private static final String LOG_TAG = ActivityHome.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private CoffeeNowApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (CoffeeNowApp) getApplicationContext();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();

        CoffeeNowApp app = (CoffeeNowApp) getApplicationContext();

        if (TextUtils.isEmpty(app.getUser().getId()) || TextUtils.isEmpty(app.getUser().getAuthToken())) {
            mFragmentManager.beginTransaction()
                    .add(R.id.content, new FragmentLogin())
                    .commit();
        } else if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.content, new FragmentCoffeeMakers())
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.activity_home, menu);
        User user = mApplication.getUser();
        if (user == null || TextUtils.isEmpty(user.getId()) || TextUtils.isEmpty(user.getAuthToken())) {
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_coffee_makers) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.content, new FragmentCoffeeMakers())
                    .addToBackStack("coffee-makers")
                    .commit();
        } else if (id == R.id.nav_users) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.content, new FragmentUsers())
                    .addToBackStack("users")
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }

    @Override
    public void onLoginDone() {
        mFragmentManager.beginTransaction()
                .replace(R.id.content, new FragmentCoffeeMakers())
                .commit();
    }

    public void logout() {
        User user = mApplication.getUser();
        user.setUsername(null);
        user.setId(null);
        user.setAuthToken(null);
        mFragmentManager.beginTransaction()
                .replace(R.id.content, new FragmentLogin())
                .commit();
    }
}
