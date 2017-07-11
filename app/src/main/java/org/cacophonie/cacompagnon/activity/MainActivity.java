package org.cacophonie.cacompagnon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import java.util.EnumMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private enum fragmentKey {
        HOME,
        CATEGORIES,
        MPLIST,
        PRESENCES
    }
    private EnumMap<fragmentKey, Fragment> fragments; // List of the fragments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the view from layout
        setContentView(R.layout.activity_main);

        // Add the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the fragment list
        fragments = new EnumMap<>(fragmentKey.class);

        // Set up the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create the 1st fragment and display it
        fragments.put(fragmentKey.HOME, new HomeFragment());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragments.get(fragmentKey.HOME)).commit();
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
        // Find the selected menu item
        switch (item.getItemId()) {
            default:
            case R.id.nav_home:
                // If no fragment exists, create one
                if (!fragments.containsKey(fragmentKey.HOME))
                    fragments.put(fragmentKey.HOME, new HomeFragment());
                currentFragment = fragments.get(fragmentKey.HOME);
                break;
            case R.id.nav_categories:
                if (!fragments.containsKey(fragmentKey.CATEGORIES))
                    fragments.put(fragmentKey.CATEGORIES, new CategoriesFragment());
                currentFragment = fragments.get(fragmentKey.CATEGORIES);
                break;
            case R.id.nav_mp:
                if (!fragments.containsKey(fragmentKey.MPLIST))
                    fragments.put(fragmentKey.MPLIST, new MPListFragment());
                currentFragment = fragments.get(fragmentKey.MPLIST);
                break;
            case R.id.nav_presence:
                if (!fragments.containsKey(fragmentKey.PRESENCES))
                    fragments.put(fragmentKey.PRESENCES, new PresenceFragment());
                currentFragment = fragments.get(fragmentKey.PRESENCES);
                break;
            case R.id.nav_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, LoginActivity.LOGIN_INTENT_CODE);
                return true;
        }

        // Replace the actual fragment by the new one
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, currentFragment).commit();

        // Finally, close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == LoginActivity.LOGIN_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                // login successful
                String session = intent.getExtras().getString(LoginActivity.LOGIN_INTENT_SESSION_KEY);

                // API creation

                // Drawer update
                TextView userName = (TextView) findViewById(R.id.userName);
                TextView userMail = (TextView) findViewById(R.id.userMail);
                ImageView profilePicture = (ImageView) findViewById(R.id.profilePicture);

            }
            else {
                // cancelled
            }
        }
    }
}
