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
            dataList = getArguments().getStringArrayList("data");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        mListView = view.findViewById(R.id.listView);
        mListView.setAdapter(null);
        mProgressBar = view.findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.INVISIBLE);
        if(!dataList.isEmpty()) {
            available_models = new ArrayList<>();
            makeModelIDs = new ArrayList<>();

            setListView(view);
            setListViewListener();
        }



        return view;
    }
    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    private void setListView(View view){
        available_models.clear();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                Log.d("doInBackground", "HERE");
                mProgressBar.setVisibility(View.VISIBLE);

                HttpHandler sh = new HttpHandler();

                String jsonStr= sh.makeServiceCall(getModelAvailableURL(dataList.get(0), dataList.get(1)));

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray contacts = jsonObj.getJSONArray("lists");

                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject d = contacts.getJSONObject(i);

                            String model = d.getString("model");
                            String make = d.getString("vehicle_make");
                            String itemNumb = "Vehicle # " + (i + 1);
                            String price = d.getString("price");
                            String id = d.getString("id");
                            String mileage = d.getString("mileage");
                            makeModelIDs.add(id);
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("itemNumb" , itemNumb);
                            contact.put("model", model);
                            contact.put("make", make);
                            contact.put("price", price);
                            contact.put("mileage", mileage);
                            available_models.add(contact);
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

                        if(available_models.isEmpty()){
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("itemNumb", "Vehicles NOT available");
                            contact.put("model", "Try a different make or model");
                            contact.put("price", "");
                            contact.put("mileage", "");
                            available_models.add(contact);
                        }


                        ListAdapter adapter = new SimpleAdapter(getContext(), available_models,
                                R.layout.model_list, new String[] {"itemNumb", "model", "price", "mileage",},
                                new int[]{R.id.txtViewItemNumber, R.id.txtViewModels, R.id.txtViewPrices, R.id.txtViewMileages});
                        mListView.setAdapter(adapter);
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
private void setListViewListener(){
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int index = (int) id;
            String model = available_models.get(index).get("model");
            String make = available_models.get(index).get("make");

            if(!model.equals("Try a different make or model")) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(makeModelIDs.get(index));
                temp.add(make);
                temp.add(model);

                mCommunicatorList.passDataListToRight(temp);
            }
        }
    });
}
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

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
        mCommunicatorList = null;
    }
}