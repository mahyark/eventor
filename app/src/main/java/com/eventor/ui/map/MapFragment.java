package com.eventor.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import com.eventor.R;

public class MapFragment extends Fragment {
    MapView map = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = (MapView) root.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // set multitouch zoom
        map.setMultiTouchControls(true);

        // show location
        MyLocationNewOverlay overlay = new MyLocationNewOverlay(map);
        overlay.enableMyLocation();
        map.getOverlayManager().add(overlay);

        // set starting point
        IMapController mapController = map.getController();
        mapController.setZoom(16.0);
        GeoPoint startPoint;
        if (overlay.getLastFix() != null) {
            startPoint = new GeoPoint(overlay.getLastFix().getLatitude(), overlay.getLastFix().getLongitude());
            mapController.setCenter(startPoint);
        } else {
            startPoint = new GeoPoint(51.22117115, 4.40035205671144);
            mapController.setCenter(startPoint);
        }
        return root;
    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}