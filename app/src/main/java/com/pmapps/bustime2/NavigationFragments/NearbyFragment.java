package com.pmapps.bustime2.NavigationFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pmapps.bustime2.BusStop.BusStopItem;
import com.pmapps.bustime2.BusStop.BusStopItemAdapter;
import com.pmapps.bustime2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearbyFragment extends Fragment {

    // Constants
    private static final int MIN_TIME_BTW_LOC_UPDATES = 120000; // 2 MIN = 120 S = 120,000 MS
    private static final String API_KEY = "";
    private static final String TIH_URL_1 = "https://tih-api.stb.gov.sg/transport/v1/bus_stop?";
    private static final String COMMA = "%2C";
    private static final String RADIUS = "50";
    private static final int NEARBY_STOPS_LIMIT = 10;

    // Variables
    private Double latitude, longitude;
    LocationManager locationManager;
    private List<BusStopItem> nearbyStopsList;
    private BusStopItemAdapter busStopItemAdapter;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;

    // Required empty public constructor
    public NearbyFragment() {
    }

    // What to do when the view is created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        initiateViews(v);
        initiateData();
        updateLocation();
        return v;
    }

    private void initiateViews(@NonNull View v) {
        recyclerView = v.findViewById(R.id.nearbyStopsRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));
    }

    private void initiateData() {
        nearbyStopsList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(requireContext());
        busStopItemAdapter = new BusStopItemAdapter(requireContext(), nearbyStopsList);
        recyclerView.setAdapter(busStopItemAdapter);
    }

    private void updateLocation() {
        boolean permission_granted = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!permission_granted) {
            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BTW_LOC_UPDATES,0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BTW_LOC_UPDATES,0, locationListener);
    }

    // called when location is updated successfully
    private void fetchNearbyStops() {
        String url
                = TIH_URL_1
                + "location=" + latitude + COMMA + longitude
                + "&radius=" + RADIUS
                + "&apikey=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(url, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("data");

                for (int i = 0; i < Math.min(NEARBY_STOPS_LIMIT, jsonArray.length()); i++) {
                    JSONObject busStopHit = jsonArray.getJSONObject(i);
                    String busStopTitle = busStopHit.getString("description");
                    String busStopRoad = busStopHit.getString("roadName");
                    String busStopCode = busStopHit.getString("code");

                    BusStopItem busStopItem = new BusStopItem(busStopTitle,busStopCode,busStopRoad);
                    nearbyStopsList.add(busStopItem);
                }
                busStopItemAdapter.notifyItemRangeInserted(0, nearbyStopsList.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    // location permissions
    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
       if (!result) {
           System.exit(0);
       } else {
           updateLocation();
       }
    });

    // fetches the location (when called in updateLocation())
    private final LocationListener locationListener = new LocationListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (latitude != null) {
                // 1. Empty the list
                nearbyStopsList.clear();
                // 2. Notify Data Set Changed
                busStopItemAdapter.notifyDataSetChanged();

                // 3. Populate the list
                fetchNearbyStops();
                // 4. Notify Data Set Changed
                busStopItemAdapter.notifyItemRangeInserted(0, nearbyStopsList.size());
            }
        }
    };
}