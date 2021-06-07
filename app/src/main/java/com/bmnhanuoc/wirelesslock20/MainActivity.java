package com.bmnhanuoc.wirelesslock20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bmnhanuoc.wirelesslock20.bottomfrag.database.DatabaseFragment;
import com.bmnhanuoc.wirelesslock20.bottomfrag.home.HomeFragment;
import com.bmnhanuoc.wirelesslock20.bottomfrag.user.UserFragment;
import com.bmnhanuoc.wirelesslock20.topfrag.HelpsActivity;
import com.bmnhanuoc.wirelesslock20.topfrag.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        bottomNav.getMenu().findItem(R.id.nav_database).setVisible(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new UserFragment()).commit();

        SharedPreferences preferences = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart",true);

        if (firstStart) {
            showStartDialog();
        }

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Có người lạ đến nhà bạn")
                .setContentText("Kiểm tra ngay đấy là ai!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager!= null) {
            notificationManager.notify(1, notification);
        }

    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("AutoStart permission")
                .setMessage("Please enable autostart permission")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            addAutoStartup();
                    }
                })
                .create()
                .show();
        SharedPreferences preferences = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if ( id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if ( id == R.id.nav_privacy) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/e/2PACX-1vRjzFWnzIUlLjqtjVYHyQjWXkuyK2K7RpQyhK4jLu1NFglGHnt_tfdURCUDBtUJJFCQzBw876sqArIi/pub"));
            startActivity(intent);
            return true;
        }
        else if ( id == R.id.nav_terms_conditions) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/e/2PACX-1vTLPHqVN35xnVq3bZGetS8LFHuLArX5IvZhcX-y-mykQKoMqJrNxnBsKkHCjbXsK1mZsTr7t623X4DL/pub"));
            startActivity(intent);
            return true;
        }
        else if ( id == R.id.nav_help_feedback) {
            Intent intent = new Intent(MainActivity.this, HelpsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_database:
                    selectedFragment = new DatabaseFragment();
                    break;
//                case R.id.nav_log_info:
//                    selectedFragment = new LoginfoFragment();
//                    break;
                case R.id.nav_user:
                    selectedFragment = new UserFragment();
                    break;
//                case R.id.nav_runbg:
//                    startService(new Intent(getApplicationContext(), MyService.class));
//
//                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,selectedFragment).commit();
            return true;
        }
    };
    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}