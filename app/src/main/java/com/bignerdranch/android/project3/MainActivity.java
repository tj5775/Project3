package com.bignerdranch.android.project3;
/*By Melissa Hazlewood (SID 26653885) and Albert Toscano (SID 27795675)
CECS 453 - Mobile Application Development
June 24, 2021

Project 3 is an Android app with one main activity and three fragments(Left, List, and Right fragments)
that simulates a car trader app, which allows the user to look for cars on sale. The main activity
instantiates the three fragments which communicate each other to get the corresponding data requested
by the user. LeftFragment consist of two spinners, so the user can select make and model, and this data
is send to ListFragment which shows the available cars depending with the make and model the user selected.
On the ListFragment the user selects one available car, and this action take the user to RightFragment.
Finally, RightFragment shows all details of the specific car.*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CommunicatorLeft, CommunicatorList, CommunicatorRight {
    FragmentManager fm = getSupportFragmentManager();
    Fragment leftFragment, listFragment, rightFragment;
    int orientation, portraitMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //In case RightFragment is still inflated after rotation
        Fragment temp1 = fm.findFragmentById(R.id.fragmentContainerRight);
        if(temp1 != null){
            fm.beginTransaction().remove(temp1).commit();
        }//In case RightFragmentLand is still inflated after rotation
        Fragment temp2 = fm.findFragmentById(R.id.fragmentContainerRightLand);
        if(temp2 != null){
            fm.beginTransaction().remove(temp2).commit();
        }

        orientation = getResources().getConfiguration().orientation;                // to detect orientation
        portraitMode = Configuration.ORIENTATION_PORTRAIT;                          // if orientation is portrait Mode
        if(orientation == portraitMode) {
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeft);
            if (leftFragment == null) {
                leftFragment = new LeftFragment();
                fm.beginTransaction().add(R.id.fragmentContainerLeft, leftFragment) // Adding fragment
                        .commit();
            }
        }
        else{                                                                       // if orientation is landscape Mode
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeftLand);
            if (leftFragment == null) {
                leftFragment = new LeftFragment();
                fm.beginTransaction().add(R.id.fragmentContainerLeftLand, leftFragment)// Adding fragment
                        .commit();
            }
        }

    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // passDataLeftToList method communicates LeftFragment with ListFragment to load the list of available cars
    // depending on the make and model of user selection on LeftFragment

    @Override
    public void passDataLeftToList(ArrayList<String> data) {
        if(orientation == portraitMode) {                                           // if orientation is portrait Mode
            listFragment = fm.findFragmentById(R.id.fragmentContainerList);
            listFragment = new ListFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            listFragment.setArguments(bundle);

            fm.beginTransaction().replace(R.id.fragmentContainerList, listFragment)// Fragment replacement
                    .addToBackStack(null).commit();
        }
        else{                                                                       // if orientation is landscape Mode
            if(rightFragment != null){
                fm.beginTransaction().remove(rightFragment).commit();
            }

            listFragment = fm.findFragmentById(R.id.fragmentContainerListLand);
            listFragment = new ListFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            listFragment.setArguments(bundle);


            fm.beginTransaction().replace(R.id.fragmentContainerListLand, listFragment)// Fragment replacement
                    .addToBackStack(null).commit();
            //fm.beginTransaction().remove(rightFragment).commit();
        }
    }
    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // passDataListToRight method communicates ListFragment with RightFragment to load the details of the cars

    @Override
    public void passDataListToRight(ArrayList<String> data) {
        if(orientation == portraitMode) {                                           // if orientation is portrait Mode
            rightFragment = fm.findFragmentById(R.id.fragmentContainerRight);
            rightFragment = new RightFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            rightFragment.setArguments(bundle);


            fm.beginTransaction().replace(R.id.fragmentContainerRight, rightFragment)// Fragment replacement
                    .addToBackStack(null).commit();
            fm.beginTransaction().remove(leftFragment).commit();
            fm.beginTransaction().remove(listFragment).commit();
        }
        else{                                                                       // if orientation is landscape Mode
            rightFragment = fm.findFragmentById(R.id.fragmentContainerRightLand);
            rightFragment = new RightFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data);
            rightFragment.setArguments(bundle);

            fm.beginTransaction().replace(R.id.fragmentContainerRightLand, rightFragment)// Fragment replacement
                    .addToBackStack(null).commit();
            fm.beginTransaction().remove(listFragment).commit();
        }
    }
//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // passDataRightToLeft method communicates RightFragment with LeftFragment to load the details of the cars

    @Override
    public void passDataRightToLeft() {

        if(orientation == portraitMode) {                                           // if orientation is portrait Mode
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeft);
            leftFragment = new LeftFragment();

            fm.beginTransaction().replace(R.id.fragmentContainerLeft, leftFragment)// Fragment replacement
                    .addToBackStack(null).commit();
            fm.beginTransaction().remove(rightFragment).commit();
        }
        else{                                                                       // if orientation is landscape Mode
            leftFragment = fm.findFragmentById(R.id.fragmentContainerLeftLand);
            leftFragment = new LeftFragment();

            fm.beginTransaction().replace(R.id.fragmentContainerLeftLand, leftFragment)// Fragment replacement
                    .addToBackStack(null).commit();
            fm.beginTransaction().remove(rightFragment).commit();
        }
    }

}