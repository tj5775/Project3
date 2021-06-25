package com.bignerdranch.android.project3;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LeftFragment extends Fragment{
    private CommunicatorLeft mCommunicator;
    private static String url = "https://thawing-beach-68207.herokuapp.com/";
    private Spinner spin1;
    private Spinner spin2;
    private ArrayList<String> makeIDs;
    private ArrayList<String> modelIDs;
    private ArrayList<String> vehicle_makes;
    private ArrayList<String> vehicle_models;
    private int selectedMake;
    private int selectedModel;
    private ArrayList<String> makeModelIDs;



    public LeftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_left_layout, container, false);
        // Initializing variables
        makeIDs = new ArrayList<>();
        modelIDs = new ArrayList<>();
        vehicle_makes = new ArrayList<>();
        vehicle_models = new ArrayList<>();
        makeModelIDs = new ArrayList<>();
        vehicle_makes.add("(make)");
        vehicle_models.add("Model");


        setSpinner1(view);                  // Calls setSpinner1 method
        setSpinner2(view);                  // Calls setSpinner2 method
        setListView(view);                  // Calls setListView method

        return view;
    }

//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // setSpinner1 method loads spin1 spinner with the cars' makes data

    private void setSpinner1(View view){
        spin1 = view.findViewById(R.id.spinner_make);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {                                                      //doInBackground
                Log.d("doInBackground", "HERE");

                getIDs(url, "carmakes", "",                         // calls getIDs method
                        "vehicle_make", makeIDs, vehicle_makes);

                getActivity().runOnUiThread(new Runnable() {                        // postExecute
                    @Override
                    public void run() {
                        Log.d("postExecute", "HERE");
                        // setting up spinner adapter
                        ArrayAdapter<String> aa = new ArrayAdapter<>(getContext().getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, vehicle_makes);
                        //Setting the ArrayAdapter data on the Spinner
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin1.setAdapter(aa);

                    }
                });
            }
        });

    }


//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // setSpinner2 method loads spin2 spinner with the car's models data using a listener on spin1
    // depending on make selection

    private void setSpinner2(View view){
        spin2 = view.findViewById(R.id.spinner_model);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {              // Spinner listener
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMake = position - 1;            // to save selected make
                makeModelIDs.clear();                   // clears makeModelIDs array list

                if(selectedMake >= 0) {                 // if user selects a make
                    modelIDs.clear();                   // clears modelIDs array list
                    vehicle_models.clear();             // clears vehicle_models array list
                    vehicle_models.add("Model");        // adds title to spin2
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {                                             //doInBackground
                            Log.d("doInBackground", "HERE");
                            getIDs(url, "carmodelmakes/", makeIDs.get(selectedMake),
                                    "model", modelIDs, vehicle_models);

                            getActivity().runOnUiThread(new Runnable() {                // postExecute
                                @Override
                                public void run() {
                                    Log.d("postExecute", "HERE");
                                    spin2.setAdapter(null); // Clears spin2
                                    // setting up spinner adapter
                                    ArrayAdapter<String> aa = new ArrayAdapter<>(getContext().getApplicationContext(),
                                            android.R.layout.simple_spinner_dropdown_item, vehicle_models);
                                    //Setting the ArrayAdapter data on the Spinner
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spin2.setAdapter(aa);

                                }
                            });
                        }
                    });
                }
                else{
                    spin2.setAdapter(null);                         // setting up spinner adapter
                    ArrayList<String> emptyList = new ArrayList<>();
                    mCommunicator.passDataLeftToList(emptyList);    // sends user to ListFragment with empty list
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // setListView method sends the make and model selected by the user to ListFragment
    // placing a listener on spin2 after user selects the model

    private void setListView(View view){
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = position - 1;
                ArrayList<String> temp = new ArrayList<>();     // ArrayList for make and model

                if(selectedModel >= 0) {
                    temp.add(makeIDs.get(selectedMake));        // add make id
                    temp.add(modelIDs.get(selectedModel));      // add model id
                }
                    mCommunicator.passDataLeftToList(temp);     // communicator sends make and model IDs to ListFragment
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // getIDs method

    public void getIDs(String url_one, String url_two, String url_three, String makeModel, ArrayList<String> ids, ArrayList<String> make){
        String fullURL = url_one + url_two + url_three; // to get full URL
        HttpHandler sh = new HttpHandler();             // creating a new HttpHandler object
        String jsonStr = sh.makeServiceCall(fullURL);   // to the json string from URL

        if(jsonStr.charAt(0) != '{'){                   // if JSON array does not have a name
            jsonStr = "{\"lists\":" + jsonStr + "}";    // adds lists as a name
        }
        if(jsonStr != null){
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);           // Creating a JSON object with json string from URL

                JSONArray contacts = jsonObj.getJSONArray("lists"); // Calling Json Array

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject d = contacts.getJSONObject(i);
                    String id = d.getString("id");          // gets id from JSON object
                    ids.add(id);                                  // adds id in ids arrayList
                    String vehicleMake = d.getString(makeModel);  // gets model or list from JSON object
                    make.add(vehicleMake);                        // adds to vehicle_makes or _model array list

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach( Context context) {
        super.onAttach(context);
        if(context instanceof CommunicatorLeft){
            mCommunicator = (CommunicatorLeft) context; //listener gets the context
        }
        else{   // to handle an exception
            throw new RuntimeException(context.toString() + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicator = null;               // sets mCommunicator to null
    }
}