package com.bignerdranch.android.project3;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ListFragment extends Fragment {
    private CommunicatorList mCommunicatorList;
    private ListView mListView;
    private ArrayList<String> dataList;
    private static String url = "https://thawing-beach-68207.herokuapp.com/";
    private ArrayList<String> makeModelIDs;
    private ArrayList<HashMap<String, String>> available_models;
    private ProgressBar mProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<>();

        if (getArguments() != null) {
            // receiving the data from LeftFragment
            dataList = getArguments().getStringArrayList("data"); // dataList contains make and model IDs
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        mListView = view.findViewById(R.id.listView);
        mListView.setAdapter(null);                     // clears the list view
        mProgressBar = view.findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.INVISIBLE);     // set progress bar to invisible
        if(!dataList.isEmpty()) {                       // if the received data is not empty
            available_models = new ArrayList<>();
            makeModelIDs = new ArrayList<>();

            setListView(view);                          // calls setListView method
            setListViewListener();                      // calls setListViewListener method
        }



        return view;
    }

    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // setListView method sets the list view with the available cars list and thier info

    private void setListView(View view){
        available_models.clear();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {                             //doInBackground
                Log.d("doInBackground", "HERE");
                mProgressBar.setVisibility(View.VISIBLE);   // set progress bar visible while loading available car's data

                HttpHandler sh = new HttpHandler();
                // getting the string from the URL
                String jsonStr= sh.makeServiceCall(getModelAvailableURL(dataList.get(0), dataList.get(1)));

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray contacts = jsonObj.getJSONArray("lists");

                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject d = contacts.getJSONObject(i);

                            String model = d.getString("model");        // getting models
                            String make = d.getString("vehicle_make");  // getting makes
                            String itemNumb = "Vehicle # " + (i + 1);         // Vehicle numbers in list
                            String price = d.getString("price");        // getting vehicles' prices
                            String id = d.getString("id");              // getting vehicles' IDs
                            String mileage = d.getString("mileage");    // getting mileages
                            makeModelIDs.add(id);                             // storing IDs in array list
                            HashMap<String, String> car = new HashMap<>();// hashmap to store vehicles info
                            car.put("itemNumb" , itemNumb);
                            car.put("model", model);
                            car.put("make", make);
                            car.put("price", price);
                            car.put("mileage", mileage);
                            available_models.add(car);                     // stores cars in available_models array list
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

                        if(available_models.isEmpty()){                     // if there are no available cars
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("itemNumb", "Vehicles NOT available");
                            contact.put("model", "Try a different make or model");
                            contact.put("price", "");
                            contact.put("mileage", "");
                            available_models.add(contact);
                        }

                        // sets up adapter
                        ListAdapter adapter = new SimpleAdapter(getContext(), available_models,
                                R.layout.model_list, new String[] {"itemNumb", "model", "price", "mileage",},
                                new int[]{R.id.txtViewItemNumber, R.id.txtViewModels, R.id.txtViewPrices, R.id.txtViewMileages});
                        mListView.setAdapter(adapter);
                        mProgressBar.setVisibility(View.INVISIBLE);     // sets progress bar invisible
                    }
                });
            }
        });

    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // setListViewListener method  set a click listener on the list view, which send the user to
    // RightFragment makeModel IDs, make and model selected

    private void setListViewListener(){
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int index = (int) id;
            String model = available_models.get(index).get("model");
            String make = available_models.get(index).get("make");

            if(!model.equals("Try a different make or model")) {// if there are model available
                ArrayList<String> temp = new ArrayList<>();
                temp.add(makeModelIDs.get(index));
                temp.add(make);
                temp.add(model);

                mCommunicatorList.passDataListToRight(temp);    // send data to RightFragment
            }
        }
    });
}
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // getModelAvailableURL method returns a string with the URL + car ID + model ID + a defined zip code

    public String getModelAvailableURL(String make, String model){
        String modelsAvailable = url + "cars/" + make + "/" + model + "/92603";
        return modelsAvailable;
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    @Override
    public void onAttach( Context context) {
        super.onAttach(context);
        if(context instanceof CommunicatorLeft){
            mCommunicatorList = (CommunicatorList) context; //listener gets the context
        }
        else{   // to handle an exception
            throw new RuntimeException(context.toString() + " must implement FragmentListener");
        }
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicatorList = null;       // sets mCommunicatorList to null
    }
}