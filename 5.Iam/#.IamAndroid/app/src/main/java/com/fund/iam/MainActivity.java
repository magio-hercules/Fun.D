package com.fund.iam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.fund.iam.fragment.TestFragment;
import com.fund.iam.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity
        implements TestFragment.OnFragmentInteractionListener,
                     HomeFragment.OnFragmentInteractionListener {
    private static final String TAG = "[IAM][MAIN]";

    BottomNavigationView bottomNavigation;
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initBottomNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initBottomNavigation() {
        Log.d(TAG, "initBottomNavigation");

        bottomNavigation = findViewById(R.id.bottom_navigation);

        navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_test:
                                Log.d(TAG, "navigation_test");
                                openFragment(TestFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_home:
                                Log.d(TAG, "navigation_home fragment");
                                openFragment(HomeFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_bookmark:
                                Log.d(TAG, "navigation_bookmark");
//                                openFragment(SmsFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_search:
                                Log.d(TAG, "navigation_search");
//                                openFragment(NotificationFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_setting:
                                Log.d(TAG, "navigation_setting");
//                                openFragment(NotificationFragment.newInstance("", ""));
                                return true;
                        }
                        return false;
                    }
                };

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(TestFragment.newInstance("", ""));
    }

    @Override
    public void onFragmentInteraction(String fragmentName){
        Log.d(TAG, "MainActivity onFragmentInteraction()");

        switch (fragmentName) {
            case "test":
                openFragment(TestFragment.newInstance("", ""));
                break;
            case "login":
//                openFragment(TestFragment.newInstance("", ""));
                break;
            case "home":
                openFragment(HomeFragment.newInstance("", ""));
                break;
            case "boomark":
                break;
            case "search":
                break;
            case "setting":
                break;
        }
    }
}
