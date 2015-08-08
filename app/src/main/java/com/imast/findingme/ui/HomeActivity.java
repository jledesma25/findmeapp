package com.imast.findingme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.ui.fragments.HomeFragment;
import com.imast.findingme.ui.fragments.MyInfoFragment;
import com.imast.findingme.ui.fragments.MyPetsFragment;
import com.imast.findingme.ui.fragments.MySearchFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView txvUsername, txvEmail;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*Parse.initialize(this, "VxoGtmV3iqI6AlrSE1Q1FRgCSG1eHXEEDtNcLWq9", "yxhBk5gegYeagwQxRRPK6gSMb3VNVtt758LRu0nD");
        ParseInstallation.getCurrentInstallation().saveInBackground();*/

        setupToolbar();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        txvUsername = (TextView) findViewById(R.id.txv_header_username);
        txvEmail = (TextView) findViewById(R.id.txv_header_email);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(this);

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        if (Config.profile != null)
            txvUsername.setText(Config.profile.getFullName());

        txvEmail.setText(Config.user.getEmail());

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        HomeFragment fragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if(menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        toolbar.setTitle(menuItem.getTitle());

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()){

            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_home:
                selectItem(new HomeFragment());
                return true;
            case R.id.nav_my_info:
                selectItem(new MyInfoFragment());
                return true;
            case R.id.nav_my_pets:
                selectItem(new MyPetsFragment());
                return true;
            case R.id.nav_my_search:
                selectItem(new MySearchFragment());
                return true;
            case R.id.nav_sign_out:
                signOut();
                return true;
            default:
                return true;

        }
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void signOut() {

        Config.user = null;
        Config.pet = null;
        Config.profile = null;
        Config.lostPet = null;

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void selectItem(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

}
