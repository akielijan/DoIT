package com.potatoprogrammers.doit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.potatoprogrammers.doit.fragments.SettingsFragment;
import com.potatoprogrammers.doit.fragments.UserActivitiesFragment;
import com.potatoprogrammers.doit.fragments.PlanFragment;
import com.potatoprogrammers.doit.fragments.StatisticsFragment;
import com.potatoprogrammers.doit.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.models.UserActivityStep;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        changeFragment(new PlanFragment());

        getUserFromFirestore();
        Snackbar.make(findViewById(R.id.drawer_layout), String.format(Locale.getDefault(), "Hello %s", FirebaseAuth.getInstance().getCurrentUser().getDisplayName()), Snackbar.LENGTH_SHORT).show();
    }

    private void getUserFromFirestore() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(User.COLLECTION)
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User.setLoggedInUser(documentSnapshot.toObject(User.class));
                    } else {
                        createUserData(uid);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "Error occurred during getting user info from the Firestore: %s", e.getLocalizedMessage()), Toast.LENGTH_SHORT).show());
    }

    private void createUserData(String uid) {
        if (User.getLoggedInUser() == null) {
            User.setLoggedInUser(new User());
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(User.COLLECTION)
                .document(uid)
                .set(User.getLoggedInUser())
                //.addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Welcome, new user!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            changeFragment(new SettingsFragment());
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);

        int id = item.getItemId();

        if (id == R.id.nav_plan) {
            changeFragment(new PlanFragment());
        } else if (id == R.id.nav_activities) {
            changeFragment(new UserActivitiesFragment());
        } else if (id == R.id.nav_statistics) {
            changeFragment(new StatisticsFragment());
        } else if (id == R.id.nav_sign_out) {
            signOut();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
        client.signOut();
    }
}
