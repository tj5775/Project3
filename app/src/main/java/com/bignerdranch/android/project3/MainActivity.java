package com.bignerdranch.android.project3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CommunicatorLeft, CommunicatorList, CommunicatorRight {
    FragmentManager fm = getSupportFragmentManager();
    Fragment leftFragment, listFragment, rightFragment;
    int orientation, portraitMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment temp1 = fm.findFragmentById(R.id.fragmentContainerRight);
        if(temp1 != null){
            fm.beginTransaction().remove(temp1).commit();
        }
        Fragment temp2 = fm.findFragmentById(R.id.fragmentContainerRightLand);
        if(temp2 != null){
            fm.beginTransaction().remove(temp2).commit();
        }

        orientation = getResources().getConfiguration().orientation;
        portraitMode = Configuration.ORIENTATION_PORTRAIT;
        if(orientation == portraitMode) {
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeft);
            if (leftFragment == null) {
                leftFragment = new LeftFragment();
                fm.beginTransaction().add(R.id.fragmentContainerLeft, leftFragment)
                        .commit();
            }
        }
        else{
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeftLand);
            if (leftFragment == null) {
                leftFragment = new LeftFragment();
                fm.beginTransaction().add(R.id.fragmentContainerLeftLand, leftFragment)
                        .commit();
            }
        }

    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    @Override
    public void passDataLeftToList(ArrayList<String> data) {
        if(orientation == portraitMode) {
            listFragment = fm.findFragmentById(R.id.fragmentContainerList);
            listFragment = new ListFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            listFragment.setArguments(bundle);

            fm.beginTransaction().replace(R.id.fragmentContainerList, listFragment)
                    .addToBackStack(null).commit();
            //fm.beginTransaction().remove(leftFragment).commit();
            //fm.beginTransaction().remove(rightFragment).commit();
        }
        else{
            if(rightFragment != null){
                fm.beginTransaction().remove(rightFragment).commit();
            }

            listFragment = fm.findFragmentById(R.id.fragmentContainerListLand);
            listFragment = new ListFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            listFragment.setArguments(bundle);


            fm.beginTransaction().replace(R.id.fragmentContainerListLand, listFragment)
                    .addToBackStack(null).commit();
            //fm.beginTransaction().remove(rightFragment).commit();
        }
    }
    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    @Override
    public void passDataListToRight(ArrayList<String> data) {
        if(orientation == portraitMode) {
            //System.out.println("List to Right");
            rightFragment = fm.findFragmentById(R.id.fragmentContainerRight);
            rightFragment = new RightFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            rightFragment.setArguments(bundle);


            fm.beginTransaction().replace(R.id.fragmentContainerRight, rightFragment)
                    .addToBackStack(null).commit();
            fm.beginTransaction().remove(leftFragment).commit();
            fm.beginTransaction().remove(listFragment).commit();
        }
        else{
            rightFragment = fm.findFragmentById(R.id.fragmentContainerRightLand);
            rightFragment = new RightFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            rightFragment.setArguments(bundle);

            fm.beginTransaction().replace(R.id.fragmentContainerRightLand, rightFragment)
                    .addToBackStack(null).commit();
            fm.beginTransaction().remove(listFragment).commit();
        }
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    @Override
    public void passDataRightToLeft() {

        if(orientation == portraitMode) {
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeft);
            leftFragment = new LeftFragment();
            fm.beginTransaction().replace(R.id.fragmentContainerLeft, leftFragment)
                    .addToBackStack(null).commit();
            fm.beginTransaction().remove(rightFragment).commit();
        }
        else{
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeftLand);
            leftFragment = new LeftFragment();
            fm.beginTransaction().replace(R.id.fragmentContainerLeftLand, leftFragment)
                    .addToBackStack(null).commit();
            //fm.popBackStack();
            fm.beginTransaction().remove(rightFragment).commit();
        }
    }

}