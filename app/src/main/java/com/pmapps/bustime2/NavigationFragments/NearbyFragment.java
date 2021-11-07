package com.pmapps.bustime2.NavigationFragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pmapps.bustime2.R;

public class NearbyFragment extends Fragment {

    // Constants
    private static final int MIN_TIME_BTW_LOC_UPDATES = 60000; // 1 MIN = 60 S = 60,000 MS

    // Variables
    private Double latitude, longitude;
    LocationManager locationManager;

    // Views
    private RecyclerView mRecyclerView;

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        initiateViews(v);
        updateLocation();
        return v;
    }

    private void initiateViews(@NonNull View v) {
        mRecyclerView = v.findViewById(R.id.nearbyStopsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation()));
    }

    private void updateLocation() {
        boolean permission_granted = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!permission_granted) {
            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        Log.d("PERMISSION RESULT 1", "permission already/has been granted");

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BTW_LOC_UPDATES,0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BTW_LOC_UPDATES,0, locationListener);

    }

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
       if (!result) {
           Log.d("PERMISSION RESULT 2", "permission denied");
           System.exit(0);
       } else {
           Log.d("PERMISSION RESULT 2", "permission accepted");
           updateLocation();
       }
    });

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(requireContext(), String.valueOf(latitude), Toast.LENGTH_SHORT).show();
            Toast.makeText(requireContext(), String.valueOf(longitude), Toast.LENGTH_SHORT).show();
            // 1. Empty the list
            // 2. Notify Data Set Changed
            // TODO: to uncomment >> mBusStopList.clear();
            // TODO: to uncomment >> mBusStopAdapter.notifyDataSetChanged();

            // 3. Populate the list
            // 4. Notify Data Set Changed
            // TODO: to uncomment >> parseJsonAndUpdateData()
        }
    };

}