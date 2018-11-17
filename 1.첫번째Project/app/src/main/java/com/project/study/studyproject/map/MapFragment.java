package com.project.study.studyproject.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.project.study.studyproject.DataModel.DataModel_Map;
import com.project.study.studyproject.DbHandler;
import com.project.study.studyproject.Dictionary;
import com.project.study.studyproject.R;
import com.project.study.studyproject.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = "[MAP]";
    private static final boolean DEBUG = true;

    private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(127.066865, 37.265692);
    private static final int NMAP_ZOOMLEVEL_DEFAULT = 13;
    private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;

    private static final String KEY_CENTER_LONGITUDE = "NMapViewer.centerLongitudeE6";
    private static final String KEY_CENTER_LATITUDE = "NMapViewer.centerLatitudeE6";
    private static final String KEY_ZOOM_LEVEL = "NMapViewer.zoomLevel";
    private static final String KEY_VIEW_MODE = "NMapViewer.viewMode";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private NMapContext mMapContext;
    private NMapView mapView;
    private NMapController mapController;
    private static final String CLIENT_ID = "ykIR0n5VuX9TlqNLdjgo";// 애플리케이션 클라이언트 아이디 값

    Button btnMarker, btnFloatingMarker;
//    Button btnDelete;

    private SharedPreferences mPreferences;

    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapOverlayManager mOverlayManager;

    private NMapPOIdataOverlay mFloatingPOIdataOverlay;
    private NMapPOIitem mFloatingPOIitem;

    // For Markers
    private static final String DB_NAME = "DB_MAP";
    private static final String TABLE_NAME = "map_marker";
    private static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(`id_marker`	BIGINT(11)	NOT NULL	PRIMARY KEY DEFAULT AUTOINCREMENT, " +
//                                                "`id_marker_type`	BIGINT(11)	NOT NULL	DEFAULT AUTOINCREMENT, " +
//                                                "`id_detail_info`	BIGINT(11)	NOT NULL	DEFAULT AUTOINCREMENT, " +
                                                "`name`	VARCHAR(128)	NOT NULL, " +
                                                "`lat`	VARCHAR(64)	NOT NULL," +
                                                "`lng`	VARCHAR(64)	NOT NULL," +
                                                "`date`	DATETIME	NOT NULL)";
//    DBHelper db;
    private ArrayList<DataModel_Map> markerArrayList = new ArrayList<>();

    ListView listview;
    ListViewAdapter adapter;
    private String curSelectId;


    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        mContext = getActivity();

        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        btnMarker = (Button)v.findViewById(R.id.btn_marker);
        btnFloatingMarker = (Button)v.findViewById(R.id.btn_floating_marker);

//        btnDelete = (Button)v.findViewById(R.id.map_button_delete);

        btnMarker.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOverlayManager.clearOverlays();
//                testPOIdataOverlay();
                LoadMarkers();
            }
        });

        btnFloatingMarker.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOverlayManager.clearOverlays();
                testFloatingPOIdataOverlay();
            }
        });

//        btnDelete.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DeleteMarker(curSelectId);
//            }
//        });

//        db = new DBHelper(getActivity(), DB_NAME, TABLE_NAME, CREATE_QUERY);

        initMap(v);

        LoadMarkers();

        initList(v);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        initMap();
    }

    @Override
    public void onClick(View v) {
        View oParentView = (View)v.getParent(); // 부모의 View를 가져온다. 즉, 아이템 View임.
//        Button btnDelete = (Button) oParentView.findViewById(R.id.map_button_delete);
        int position = (int) oParentView.getTag();

        DeleteMarker(position);

        Log.d(LOG_TAG, "onClick: position(" + position + ")");

//        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
//                android.R.style.Theme_DeviceDefault_Light_Dialog);
//
//        String strMsg = "선택한 아이템의 position 은 "+position+" 입니다.\nTitle 텍스트 :" + oTextTitle.getText();
//        oDialog.setMessage(strMsg)
//                .setPositiveButton("확인", null)
//                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
//                .show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Fragment에 포함된 NMapView 객체를 반환함
     */
    private NMapView findMapView(View v) {

        if (!(v instanceof ViewGroup)) {
            return null;
        }

        ViewGroup vg = (ViewGroup)v;
        if (vg instanceof NMapView) {
            return (NMapView)vg;
        }

        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);
            if (!(child instanceof ViewGroup)) {
                continue;
            }

            NMapView mapView = findMapView(child);
            if (mapView != null) {
                return mapView;
            }
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();

        saveInstanceState();

//        if (db != null) {
//            db.close();
//        }

        super.onDestroy();
    }


    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    private void initMap(View view) {
//        mapView = (NMapView)getView().findViewById(R.id.mapView);
        mapView = (NMapView)view.findViewById(R.id.mapView);

        if (mapView == null) {
            throw new IllegalArgumentException("NMapFragment dose not have an instance of NMapView.");
        }

        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        // NMapActivity를 상속하지 않는 경우에는 NMapView 객체 생성후 반드시 setupMapView()를 호출해야함.
        mMapContext.setupMapView(mapView);

        // set a registered Client Id for Open MapViewer Library
        mapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mapView.setOnMapViewDelegate(onMapViewTouchDelegate);

        mapController = mapView.getMapController();
        mapController.setZoomEnabled(true);
        mapController.setPanEnabled(true);


        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(mContext);

        // set data provider listener
        mMapContext.setMapDataProviderListener(onDataProviderListener);

        // create overlay manager
        mOverlayManager = new NMapOverlayManager(mContext, mapView, mMapViewerResourceProvider);
//        // register callout overlay listener to customize it.
//        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
//        // register callout overlay view listener to customize it.
//        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);
    }

    private void restoreInstanceState() {
        Context context = getActivity();
//        mPreferences = context.getPreferences(MODE_PRIVATE);
        mPreferences = context.getSharedPreferences( getString(R.string.preference_file_key), MODE_PRIVATE);

        // map center
        int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
        int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
        int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
        int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);

        mapController.setMapViewMode(viewMode);
        mapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);
    }

    private void saveInstanceState() {
        if (mPreferences == null) {
            return;
        }

        NGeoPoint center = mapController.getMapCenter();
        int level = mapController.getZoomLevel();
        int viewMode = mapController.getMapViewMode();
        boolean trafficMode = mapController.getMapViewTrafficMode();
        boolean bicycleMode = mapController.getMapViewBicycleMode();

        SharedPreferences.Editor edit = mPreferences.edit();

        edit.putInt(KEY_CENTER_LONGITUDE, center.getLongitudeE6());
        edit.putInt(KEY_CENTER_LATITUDE, center.getLatitudeE6());
        edit.putInt(KEY_ZOOM_LEVEL, level);
        edit.putInt(KEY_VIEW_MODE, viewMode);

        edit.commit();
    }

    private void testPOIdataOverlay() {

        // Markers for POI item
        int markerId = NMapPOIflagType.PIN;

        if (mapController != null) {
            mapController.setMapCenter(127.066865, 37.265692);
        }

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        NMapPOIitem item = poiData.addPOIitem(127.066865, 37.265692, "우리집", markerId, 0);
        item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
        poiData.addPOIitem(127.0698865, 37.263692, "영흥공원", markerId, 0);
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // select an item
        poiDataOverlay.selectPOIitem(0, true);

        // show all POI data
        //poiDataOverlay.showAllPOIdata(0);
    }

    private void testFloatingPOIdataOverlay() {
        // Markers for POI item
        int marker1 = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        NMapPOIitem item = poiData.addPOIitem(null, "Touch & Drag to Move", marker1, 0);
        if (item != null) {
            // initialize location to the center of the map view.
            item.setPoint(mapController.getMapCenter());
            // set floating mode
            item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);
            // show right button on callout
//            item.setRightButton(true);
            item.setRightButton(false);

            mFloatingPOIitem = item;
        }
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        if (poiDataOverlay != null) {
            poiDataOverlay.setOnFloatingItemChangeListener(onPOIdataFloatingItemChangeListener);

            // set event listener to the overlay
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

            poiDataOverlay.selectPOIitem(0, false);

            mFloatingPOIdataOverlay = poiDataOverlay;
        }
    }

    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.
                restoreInstanceState();
            } else { // fail
                Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());
                Toast.makeText(getActivity(), errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onMapCenterChangeFine");
            }
        }
    };

    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLongPressCanceled(NMapView mapView) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {

        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
        }

        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

    };

    private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {

        @Override
        public boolean isLocationTracking() {
//            if (mMapLocationManager != null) {
//                if (mMapLocationManager.isMyLocationEnabled()) {
//                    return mMapLocationManager.isMyLocationFixed();
//                }
//            }
            return false;
        }

    };

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
            }

            // [[TEMP]] handle a click event of the callout
            Toast.makeText(mContext, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                if (item != null) {
                    Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.i(LOG_TAG, "onFocusChanged: ");
                }
            }
        }
    };

    /* NMapDataProvider Listener */
    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            if (DEBUG) {
                Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
                        + ((placeMark != null) ? placeMark.toString() : null));
            }

            if (errInfo != null) {
                Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                Toast.makeText(mContext, errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
                mFloatingPOIdataOverlay.deselectFocusedPOIitem();

                if (placeMark != null) {
                    mFloatingPOIitem.setTitle(placeMark.toString());
                }
                mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);

                NGeoPoint point = mFloatingPOIitem.getPoint();
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String getTime = sdf.format(date);

                AddMarker(placeMark.toString(), ""+point.getLatitude(), ""+point.getLongitude(), getTime);
            }
        }
    };

    private final NMapPOIdataOverlay.OnFloatingItemChangeListener onPOIdataFloatingItemChangeListener = new NMapPOIdataOverlay.OnFloatingItemChangeListener() {

        @Override
        public void onPointChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            NGeoPoint point = item.getPoint();

            if (DEBUG) {
                Log.i(LOG_TAG, "onPointChanged: point=" + point.toString());
            }

            mMapContext.findPlacemarkAtLocation(point.longitude, point.latitude);

            item.setTitle(null);

            Double lat = point.getLatitude();
            Double lng = point.getLongitude();

//            AddMarker("Test", lat.toString(), lng.toString(), "1111");
        }
    };

    private void initList(View view) {
        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView)view.findViewById(R.id.map_listview);
        listview.setAdapter(adapter);

//        // 첫 번째 아이템 추가.
//        adapter.addItem("1", "Box", "Account Box Black 36dp") ;
//        // 두 번째 아이템 추가.
//        adapter.addItem("2", "Circle", "Account Circle Black 36dp") ;
//        // 세 번째 아이템 추가.
//        adapter.addItem("3", "Ind", "Assignment Ind Black 36dp") ;
        DataModel_Map data;
        for (int i = 0; i < markerArrayList.size(); i++) {
            data = markerArrayList.get(i);

            adapter.addItem(""+data.getIdMarker(), data.getName(), data.getDate(), this);
        }

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                String strId = item.getId() ;
                String strName = item.getName() ;
                String strDate = item.getDate() ;

                curSelectId = strId;

                Toast.makeText(mContext, "onItemClick (" + strId + ")", Toast.LENGTH_SHORT).show();

                Log.d(LOG_TAG, "onItemClick: id(" + strId + "), name(" + strName + "), date(" + strDate + ")");
            }
        }) ;

    }


    private void LoadMarkers() {
        markerArrayList.clear();

        DBHelper db = new DBHelper(getActivity(), DB_NAME, TABLE_NAME, CREATE_QUERY);
        final ArrayList<DataModel_Map> dataList = db.getAllDatas();
        db.close();

        if (dataList.size() > 0) {
            //loop through contents
            for (int i = 0; i < dataList.size(); i++) {
                Log.d(LOG_TAG, "" + dataList.get(i).getIdMarker());
                Log.d(LOG_TAG, dataList.get(i).getName());
                Log.d(LOG_TAG, dataList.get(i).getLat());
                Log.d(LOG_TAG, dataList.get(i).getLng());
                Log.d(LOG_TAG, dataList.get(i).getDate());

                //add data to list used in adapter
                markerArrayList.add(dataList.get(i));
            }
        }

        ShowMarkers();
    }

    private void AddMarker(String name, String lat, String lng, String date) {
        DBHelper db = new DBHelper(getActivity(), DB_NAME, TABLE_NAME, CREATE_QUERY);
        DataModel_Map data = new DataModel_Map(name, lat, lng, date);
        long newId = db.addData(data);
        db.close();
//        Toast.makeText(mContext, "AddMarker", Toast.LENGTH_SHORT).show();

        adapter.addItem("" + newId, data.getName(), data.getDate(), this);
        Log.d(LOG_TAG, "AddMarker success");
    }

    public void DeleteMarker(int position) {
        int id = markerArrayList.get(position).getIdMarker();
        markerArrayList.remove(position);

        adapter.deleteItem("" + id);

        DBHelper db = new DBHelper(getActivity(), DB_NAME, TABLE_NAME, CREATE_QUERY);
//        DataModel_Map data = new DataModel_Map(name, lat, lng, date);
        db.deleteData(""+id);
        db.close();

        adapter.deleteItem(""+id);

        Toast.makeText(mContext, "DeleteMarker (" + id + ")", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "DeleteMarker success");
    }


    private void ShowMarkers() {
        // Markers for POI item
        int markerId = NMapPOIflagType.PIN;

        if (mapController != null) {
            mapController.setMapCenter(127.066865, 37.265692);
        }

        // set POI data
        int markerCount = markerArrayList.size();
        NMapPOIdata poiData = new NMapPOIdata(markerCount, mMapViewerResourceProvider);
        poiData.beginPOIdata(markerCount);

        // show all markers
        if (false) {
            NMapPOIitem item = poiData.addPOIitem(127.066865, 37.265692, "우리집", markerId, 0);
            item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
            poiData.addPOIitem(127.0698865, 37.263692, "영흥공원", markerId, 0);
        } else {
            NMapPOIitem item = null;
            DataModel_Map data;
            for (int i = 0; i < markerArrayList.size(); i++) {
                data = markerArrayList.get(i);
//                item = poiData.addPOIitem(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()),
                item = poiData.addPOIitem(Double.parseDouble(data.getLng()), Double.parseDouble(data.getLat()),
                        data.getName(), markerId, data.getIdMarker());
                item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
                Log.d(LOG_TAG, "showMarkers !!!");
            }
        }

        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // select an item
        poiDataOverlay.selectPOIitem(0, true);

        // show all POI data
        //poiDataOverlay.showAllPOIdata(0);
    }
}
