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
    private ArrayList<HashMap<String, String>> available_models;
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
        makeIDs = new ArrayList<>();
        modelIDs = new ArrayList<>();
        vehicle_makes = new ArrayList<>();
        vehicle_models = new ArrayList<>();
        available_models = new ArrayList<>();
        makeModelIDs = new ArrayList<>();
        vehicle_makes.add("(make)");
        vehicle_models.add("Model");


        setSpinner1(view);
        setSpinner2(view);
        setListView(view);

        return view;
    }

//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    private void setSpinner1(View view){
        spin1 = view.findViewById(R.id.spinner_make);
        //String url_two = "carmakes";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                Log.d("doInBackground", "HERE");

                getIDs(url, "carmakes", "","vehicle_make", makeIDs, vehicle_makes);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // postExecute
                        Log.d("postExecute", "HERE");
                        ArrayAdapter<String> aa = new ArrayAdapter<>(getContext().getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, vehicle_makes);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Setting the ArrayAdapter data on the Spinner
                        spin1.setAdapter(aa);

                    }
                });
            }
        });

    }


//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    private void setSpinner2(View view){
        spin2 = view.findViewById(R.id.spinner_model);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMake = position - 1;
                makeModelIDs.clear();

                if(selectedMake >= 0) {
                    modelIDs.clear();
                    vehicle_models.clear();
                    vehicle_models.add("Model");
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            //doInBackground
                            Log.d("doInBackground", "HERE");
                            getIDs(url, "carmodelmakes/", makeIDs.get(selectedMake), "model", modelIDs, vehicle_models);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // postExecute
                                    Log.d("postExecute", "HERE");
                                    spin2.setAdapter(null);
                                    ArrayAdapter<String> aa = new ArrayAdapter<>(getContext().getApplicationContext(),
                                            android.R.layout.simple_spinner_dropdown_item, vehicle_models);
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Setting the ArrayAdapter data on the Spinner
                                    spin2.setAdapter(aa);

                                }
                            });
                        }
                    });
                }
                else{
                    spin2.setAdapter(null);
                    ArrayList<String> emptyList = new ArrayList<>();
                    mCommunicator.passDataLeftToList(emptyList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    private void setListView(View view){
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = position - 1;
                ArrayList<String> temp = new ArrayList<>();

                if(selectedModel >= 0) {
                    temp.add(makeIDs.get(selectedMake));
                    temp.add(modelIDs.get(selectedModel));
                }
                    mCommunicator.passDataLeftToList(temp);
                    available_models.clear();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    public void getIDs(String url_one, String url_two, String url_three, String makeModel, ArrayList<String> ids, ArrayList<String> make){
        String fullURL = url_one + url_two + url_three;
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(fullURL);

        if(jsonStr.charAt(0) != '{'){
            jsonStr = "{\"lists\":" + jsonStr + "}";
        }
        if(jsonStr != null){
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Calling Json Array
                JSONArray contacts = jsonObj.getJSONArray("lists");

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject d = contacts.getJSONObject(i);

                    String id = d.getString("id");
                    ids.add(id);
                    String vehicleMake = d.getString(makeModel);
                    make.add(vehicleMake);

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
        mCommunicator = null;
    }
}