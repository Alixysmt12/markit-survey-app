package com.example.markitsurvey.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bosphere.filelogger.FL;
import com.bosphere.filelogger.FLConfig;
import com.bosphere.filelogger.FLConst;
import com.example.markitsurvey.MyDrawerController;
import com.example.markitsurvey.R;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.UIHelper;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.logger.CustomFormatter;
import com.example.markitsurvey.models.UserDecode;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.markitsurvey.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity implements MyDrawerController {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    DrawerLayout drawer;
    NavigationView navigationView;

    KeyValueDB keyValueDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        keyValueDB = new KeyValueDB(getSharedPreferences(getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));

        String token = keyValueDB.getValue("token", "");

        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        UserDecode user = UserDecode.ConvertToUserEntityUser(token);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_my_projects, R.id.nav_synchronize, R.id.nav_logs, R.id.nav_about_app, R.id.nav_setting, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        View headerView = navigationView.getHeaderView(0);
        TextView headerName = headerView.findViewById(R.id.textView4);
        TextView headerEmail = headerView.findViewById(R.id.textView);

        // headerName.setText(user.getName());
        //headerEmail.setText(user.getUserName());
        //ImageView navBack = headerView.findViewById(R.id.bc);

        hide_Appbar();
        enableLogger(user.getId());

        binding.navView.getMenu().findItem(R.id.nav_log_out).setOnMenuItemClickListener(menuItem -> {


            AppLogger.i("ButtonClicked", "LogOut");
            UIHelper.logout("LogOut", "Are you sure do you want to logout?", MainActivity.this);
            return true;
        });

        binding.navView.getMenu().findItem(R.id.nav_permission).setOnMenuItemClickListener(menuItem -> {

            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            return true;
        });

        /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_my_projects:
                        NavController navController2 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController2.navigate(R.id.nav_my_projects);
                        break;

                    case R.id.nav_log_out:
                        Toast.makeText(getApplicationContext(), "log out", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_permission:
                        Toast.makeText(getApplicationContext(), "Permission", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_synchronize:
                        NavController navController3 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController3.navigate(R.id.nav_synchronize);
                        break;

                    case R.id.nav_logs:
                        NavController navController4 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController4.navigate(R.id.nav_logs);
                        break;
                    case R.id.nav_profile:

                        NavController navController5 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController5.navigate(R.id.nav_profile);
                        break;

                    case R.id.nav_about_app:
                        NavController navController7 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController7.navigate(R.id.nav_about_app);
                        break;

                    case R.id.nav_setting:
                        NavController navController8 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController8.navigate(R.id.nav_setting);
                        break;
                }


                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });*/
    }

    private void enableLogger(int id) {


        Utility.createDirectory("/sdcard/.MarkitSurvey/AppLogger/");
        FL.init(new FLConfig.Builder(this)
                .minLevel(FLConst.Level.V)
                .logToFile(true)
                .formatter(new CustomFormatter("" + id))
                .dir(new File(new File("/sdcard/.MarkitSurvey/"), "AppLogger"))
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
                .build());
        FL.setEnabled(true);
        AppLogger.i(this.getClass().getName());

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setQueryHint("Type here to search...");
        // searchView.setBackgroundColor(Color.parseColor("#093EFE"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setDrawer_locked() {

        //==================== code to lock drawer ====================

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        binding.appBarMain.toolbar.setVisibility(View.GONE);
    }

    @Override
    public void setDrawer_UnLocked() {

        //==================== code to unlock drawer ====================
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
    }

    public void hide_Appbar() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) binding.appBarMain.appBarLayout.getLayoutParams();
        lp.height = 0;
        //binding.appBarMain.appBarLayout.setLayoutParams(lp);
    }

/*    private void show_Appbar() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) binding.appBarMain.appBarLayout.getLayoutParams();
        lp.height = 60;
        binding.appBarMain.appBarLayout.setLayoutParams(lp);
    }*/



/*    @Override
    public void onBackPressed() {
        super.onBackPressed();

        binding.appBarMain.appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));

    }*/

    @SuppressLint("WrongConstant")
    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);
    }

    public void toolbarHideShow() {

        binding.appBarMain.appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onBackPressed() {
        if (Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                .getCurrentDestination().getId() == R.id.nav_my_projects) {
            // handle back button the way you want here
            finish();
            return;
        } else {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);


            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.nav_my_projects:
                            hide_Appbar();
                            NavController navController2 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                            navController2.navigate(R.id.nav_my_projects);
                            break;

                        case R.id.nav_log_out:
                            Toast.makeText(getApplicationContext(), "log out", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.nav_permission:
                            Toast.makeText(getApplicationContext(), "Permission", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.nav_synchronize:
                            hide_Appbar();
                            NavController navController3 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                            navController3.navigate(R.id.nav_synchronize);
                            break;

                        case R.id.nav_logs:
                            hide_Appbar();
                            NavController navController4 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                            navController4.navigate(R.id.nav_logs);
                            break;
                        case R.id.nav_profile:

                            hide_Appbar();
                            NavController navController5 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                            navController5.navigate(R.id.nav_profile);

                            break;

                        case R.id.nav_about_app:
                            hide_Appbar();
                            NavController navController7 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                            navController7.navigate(R.id.nav_about_app);
                            break;

                        case R.id.nav_setting:
                            hide_Appbar();
                            NavController navController8 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                            navController8.navigate(R.id.nav_setting);
                            break;
                    }


                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }
        super.onBackPressed();
    }

}