package com.example.android.shushme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor>{

    // Constants
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 111;

    // Member variables
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState The Bundle that contains the data supplied in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaceListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        GoogleApiClient client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,this)
                .build();

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "API Client Connection Successful");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, ":API Client Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "API Client Connection FAILED");
    }

    public void onAddPlaceButton (View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Enable location permission", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        CheckBox locationPermission = findViewById(R.id.location_permission_checkbox);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            locationPermission.setChecked(false);
        } else {
            locationPermission.setChecked(true);
            locationPermission.setEnabled(false);
        }
    }

    public void onLocationPermission (View view) {
        ActivityCompat.requestPermissions(MainActivity.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_FINE_LOCATION);
    }
}
