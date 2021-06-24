package com.bignerdranch.android.project3;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RightFragment extends Fragment{
    private CommunicatorRight mCommunicator;
    private static String url = "https://thawing-beach-68207.herokuapp.com/cars/";
    private TextView txtViewMake;
    private TextView txtViewModel;
    private TextView txtViewPrice;
    private ImageView imageViewCar;
    private TextView txtViewDetails;
    private TextView txtViewUpdate;
    private ImageButton imageBtnBack;

    private ProgressBar mProgressBar;
    private String vehicleID;
    private String vehicle_make;
    private String vehicle_model;
    private ArrayList<String> arrayIDs;
    private ArrayList<String> carInfo;

    public RightFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            arrayIDs = getArguments().getStringArrayList("data");
            vehicleID = arrayIDs.get(0);
            vehicle_make = arrayIDs.get(1);
            vehicle_model = arrayIDs.get(2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_right_layout, container, false);

        wiringUp(view);
        setCarInfo();

        return view;
    }

//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    public void wiringUp(View view){
        carInfo = new ArrayList<>();
        txtViewMake = view.findViewById(R.id.textViewMake);
        txtViewModel = view.findViewById(R.id.textViewModel);
        txtViewPrice = view.findViewById(R.id.textViewPrice);
        imageViewCar = view.findViewById(R.id.imageViewCar);
        txtViewDetails = view.findViewById(R.id.textViewDetails);
        txtViewUpdate = view.findViewById(R.id.textViewUpdate);
        //mProgressBar = view.findViewById(R.id.progressbarRight);
        imageBtnBack = view.findViewById(R.id.backBtn);
        mProgressBar = view.findViewById(R.id.progressbarRight);
        mProgressBar.setVisibility(View.INVISIBLE);


        txtViewMake.setText("Make: " + vehicle_make);
        txtViewModel.setText("Model: " + vehicle_model);
        imageBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommunicator.passDataRightToLeft();
            }
        });
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    public void setCarInfo(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                Log.d("doInBackground", "HERE");
                mProgressBar.setVisibility(View.VISIBLE);
                HttpHandler sh = new HttpHandler();

                String jsonStr = "{\"cars_list\":";
                jsonStr += sh.makeServiceCall(url + vehicleID);
                jsonStr += "}";

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Calling Json Array
                        JSONArray contacts = jsonObj.getJSONArray("cars_list");

                        String price = "", updated_at = " ", veh_description = "";
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject d = contacts.getJSONObject(i);

                            price = d.getString("price");
                            updated_at = d.getString("updated_at");
                            veh_description = d.getString("veh_description");
                            carInfo.add(price);
                            carInfo.add(updated_at);
                            carInfo.add(veh_description);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // postExecute
                        Log.d("postExecute", "HERE");

                        txtViewPrice.setText("Price: $" + carInfo.get(0));
                        txtViewUpdate.setText("Last Update: " + carInfo.get(1));
                        txtViewDetails.setText(carInfo.get(2));
                        imageViewCar.setImageResource(R.drawable.car);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        //mProgressBar.setVisibility(getView().INVISIBLE);
                    }
                });
            }

        });
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    @Override
    public void onAttach( Context context) {
        super.onAttach(context);
        if(context instanceof CommunicatorLeft){
            mCommunicator = (CommunicatorRight) context; //listener gets the context
        }
        else{   // to handle an exception
            throw new RuntimeException(context.toString() + " must implement FragmentListener");
        }
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicator = null;
    }


}