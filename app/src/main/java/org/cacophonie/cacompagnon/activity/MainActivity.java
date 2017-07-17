package org.cacophonie.cacompagnon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.fragment.CategoriesFragment;
import org.cacophonie.cacompagnon.fragment.HomeFragment;
import org.cacophonie.cacompagnon.fragment.MPListFragment;
import org.cacophonie.cacompagnon.fragment.PresenceFragment;
import org.cacophonie.cacompagnon.utils.VanillaAPI;

import java.util.EnumMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private VanillaAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the view from layout
        setContentView(R.layout.activity_main);

        // Add the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        if (savedInstanceState == null) {
            // Create the 1st fragment and display it
            getSupportFragmentManager().beginTransaction().add(R.id.frame, new CategoriesFragment(), "CATEGORIES").commit();
        }

        api = new VanillaAPI("https://smaiz.fr/vanilla/api");
    }

    public VanillaAPI getAPI() {
        return api;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Launch settings activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment currentFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Find the selected menu item
        switch (item.getItemId()) {
            default:
            case R.id.nav_home:
                // If no fragment exists, create one
                currentFragment = fragmentManager.findFragmentByTag("HOME");
                if (currentFragment == null)
                    currentFragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.frame, currentFragment, "HOME").addToBackStack(null).commit();
                break;
            case R.id.nav_categories:
                currentFragment = fragmentManager.findFragmentByTag("CATEGORIES");
                if (currentFragment == null)
                    currentFragment = new CategoriesFragment();
                fragmentManager.beginTransaction().replace(R.id.frame, currentFragment, "CATEGORIES").addToBackStack(null).commit();
                break;
            case R.id.nav_mp:
                currentFragment = fragmentManager.findFragmentByTag("MP");
                if (currentFragment == null)
                    currentFragment = new MPListFragment();
                fragmentManager.beginTransaction().replace(R.id.frame, currentFragment, "MP").addToBackStack(null).commit();
                break;
            case R.id.nav_presence:
                currentFragment = fragmentManager.findFragmentByTag("PRESENCES");
                if (currentFragment == null)
                    currentFragment = new PresenceFragment();
                fragmentManager.beginTransaction().replace(R.id.frame, currentFragment, "PRESENCES").addToBackStack(null).commit();
                break;
            case R.id.nav_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, LoginActivity.LOGIN_INTENT_CODE);
                return true;
        }

        // Finally, close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == LoginActivity.LOGIN_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                // Login successful
                String cookies = intent.getExtras().getString(LoginActivity.LOGIN_INTENT_SESSION_KEY);
                api.addSessionCookie(cookies);
                // Destroy the fragments to force the renewal of their data
                destroyFragments();
                // Create a new HomeFragment and display it
                getSupportFragmentManager().beginTransaction().add(R.id.frame, new HomeFragment(), "HOME").commitAllowingStateLoss();

                /* TODO: Drawer update
                * For now, there is a bug ine the API, I cannot get the user information
                * We need to update its information here (picture, name and email) */
                TextView userName = (TextView) findViewById(R.id.userName);
                TextView userMail = (TextView) findViewById(R.id.userMail);
                ImageView profilePicture = (ImageView) findViewById(R.id.profilePicture);

                NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
                nv.getMenu().getItem(0).setChecked(true);

                // Finally, close the drawer
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
            else {
                // Cancelled. Nothing to do for now
            }
        }
    }

    private void destroyFragments() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f;
        f = fm.findFragmentByTag("HOME");
        if (f != null)
            fm.beginTransaction().remove(f).commitNowAllowingStateLoss();
        f = fm.findFragmentByTag("CATEGORIES");
        if (f != null)
            fm.beginTransaction().remove(f).commitNowAllowingStateLoss();
        f = fm.findFragmentByTag("MP");
        if (f != null)
            fm.beginTransaction().remove(f).commitNowAllowingStateLoss();
        f = fm.findFragmentByTag("PRESENCES");
        if (f != null)
            fm.beginTransaction().remove(f).commitNowAllowingStateLoss();
    }
}
