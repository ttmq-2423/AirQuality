package com.example.airquality;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airquality.API.APIInterface;
import com.example.airquality.API.ApIClient;
import com.example.airquality.API.ApiService;
import com.example.airquality.Model.Asset;
import com.example.airquality.Model.AssetID;
import com.example.airquality.Model.Map;
import com.github.mikephil.charting.BuildConfig;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.CustomZoomButtonsDisplay;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {
    private APIInterface apiInterface;
    private String ID = null;
    private String token=null;
    private MapView map = null;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    Boolean getapi =false;

    private AssetID[] Assetposition;
    private MapView myOpenMapView;
    private MapController myMapController;
    ArrayList<OverlayItem> anotherOverlayItemArray;
    Map mMap;
    Button view;
    RelativeLayout myRelativeLayout;
    TextView infoTextView;
    String User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        map = (MapView)findViewById(R.id.MapView);
        infoTextView = findViewById(R.id.info);
        view = findViewById(R.id.button_view);
        myRelativeLayout = findViewById(R.id.myRelativeLayout);

        map.setBuiltInZoomControls(true);
        myMapController = (MapController) map.getController();
        map.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean longPressHelper(GeoPoint p) {

                return false;
            }
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (myRelativeLayout.getVisibility()!=GONE){
                    myRelativeLayout.setVisibility(View.GONE);}
                return false;
            }

        }));

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        User = bundle.getString("user");
        token = bundle.getString("token");
        apiInterface = ApIClient.setToken(token);
        apiInterface = ApIClient.getClient().create(APIInterface.class);

        Call<Map> call = apiInterface.getMap();

        call.enqueue(new Callback<Map>() {
                         @Override
                         public void onResponse(Call<Map> call, Response<Map> response) {
                             if(response.isSuccessful()){
                                 Map Map = response.body();
                                 setMap(Map);
                             }
                         }
                         @Override
                         public void onFailure(Call<Map> call, Throwable t) {
                             Toast.makeText(MapActivity.this, "onFailure",Toast.LENGTH_SHORT).show();
                             Log.e("onCreate: ", "error");
                         }
                     }
        );

        apiInterface = ApIClient.setToken(token);
        apiInterface = ApIClient.getClient().create(APIInterface.class);

        Call<AssetID[]> call1 = apiInterface.getAssetID();
        call1.enqueue(new Callback<AssetID[]>() {
            @Override
            public void onResponse(Call<AssetID[]> call, Response<AssetID[]> response) {
                Log.d("API CALL", response.code() + "");
                Log.d("API CALL", response.toString());
                if(response.isSuccessful()){
                    Assetposition = response.body();
                    setPosition(Assetposition);

                }
            }
            @Override
            public void onFailure(Call<AssetID[]> call, Throwable t) {
            }
        });
    }
    private void setPosition(AssetID[] Assetposition) {
        ArrayList<OverlayItem> tmp =  new ArrayList<OverlayItem>();
        for (AssetID position : Assetposition) {
            double alatitude;
            double alongtitude;
            if (position.attributes.location != null &&
                    position.attributes.location.getValue() != null) {
                alongtitude = position.attributes.location.getValue().getCoordinates()[0];
                alatitude = position.attributes.location.getValue().getCoordinates()[1];
                GeoPoint mpoition = new GeoPoint(alatitude, alongtitude);
                Marker startMarker = new Marker(map);
                startMarker.setPosition(mpoition);
                startMarker.setTitle(position.name);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                map.getOverlays().add(startMarker);

                startMarker.setOnMarkerClickListener((marker, mapView) -> {

                    myRelativeLayout.setVisibility(View.VISIBLE);
                    infoTextView.setVisibility(View.VISIBLE);
                    view.setVisibility(INVISIBLE);

                    infoTextView.setText(position.name);
                    Log.d("Click", "setPosition: "+startMarker.getTitle());
                    if(position.attributes.place!= null){
                        infoTextView.append("\nPlace: "+position.attributes.place.value);
                        infoTextView.append("\nTemperature: "+ position.attributes.temp.value);
                        infoTextView.append("\nHumidity: "+ position.attributes.hum.value);
                        infoTextView.append("\nRainfall: "+position.attributes.rainfall.value);
                        view.setVisibility(View.VISIBLE);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MapActivity.this, DashBoardActivity_custom.class);
                                Bundle mybundle = new Bundle();
                                mybundle.putString("token",token);
                                mybundle.putString("user",User);
                                mybundle.putString("assetid",position.id);
                                intent.putExtra("mypackage",mybundle);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        if(position.attributes.bright.value != null)
                        {
                            Log.d("Email", "setPosition: "+position.attributes.bright.value);
                            infoTextView.append("\nBrightness: "+ position.attributes.bright.value);
                        }
                        if(position.attributes.colour.value != null)
                        {
                            infoTextView.append("\nColourTemperature: "+position.attributes.colour.value);
                        }
                        if (position.attributes.email.value != null)
                        {
                            infoTextView.append("\nEmail: "+position.attributes.email.value);
                        }}
                    return true;
                });
            }
        }
    }
    private void setMap(Map Map) {

        double[] position = Map.getOptions().getDefaultOptions().getCenter();
        double alatitude = position[1];
        double alongtitude = position[0];

//        Context ctx = this.getApplicationContext();
//        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.MapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(Map.getOptions().getDefaultOptions().getZoom());


        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setLatNorth(Map.getOptions().getDefaultOptions().getBounds()[3]);
        boundingBox.setLonEast(Map.getOptions().getDefaultOptions().getBounds()[2]);
        boundingBox.setLatSouth(Map.getOptions().getDefaultOptions().getBounds()[1]);
        boundingBox.setLonWest(Map.getOptions().getDefaultOptions().getBounds()[0]);
        map.setScrollableAreaLimitDouble(boundingBox);


        //requestPermissionsIfNecessary(new String[]{
        //        "Manifest.permission.WRITE_EXTERNAL_STORAGE", "Manifest.permission.ACCESS_COARSE_LOCATION", "Manifest.permission.ACCESS_NETWORK_STATE", "Manifest.permission.ACCESS_WIFI_STATE", "Manifest.permission.INTERNET"
        // });


        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.getZoomController().getDisplay().setPositions(false, CustomZoomButtonsDisplay.HorizontalPosition.RIGHT, CustomZoomButtonsDisplay.VerticalPosition.TOP);
        map.setMultiTouchControls(true);
        //hiển thị la bàn
        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);


        //set vị trí
        GeoPoint point = new GeoPoint(alatitude,alongtitude
        );
        map.getController().setCenter(point);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}