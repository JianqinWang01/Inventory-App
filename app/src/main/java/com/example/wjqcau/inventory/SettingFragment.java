package com.example.wjqcau.inventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;


import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends PreferenceFragmentCompat  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //delcare three preference for setting
    android.support.v7.preference.Preference unitPreference;
   android.support.v7.preference.Preference stockLinePreference;
   android.support.v7.preference.Preference exitPreference;
    //delcare customerized dialog for stock warning low line value and high line value
    Dialog stockSettingDialog;
    //delcare button and image icon to close or operate on the dialog
   Button cancelButton;
   Button setButton;
   ImageView closeSettingImage;
   //declare edit text to input the low and high value limit
   EditText lowInput;
   EditText hightInput;

    //declare the system default shared preference
    SharedPreferences stockSharedPref;
    //declare the editor for shared preference
    SharedPreferences.Editor editor;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
      //initialize the stocksetting dialog
        stockSettingDialog=new Dialog(getContext());
        //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
        stockSettingDialog.setContentView(R.layout.set_stock_warning_layout);
        stockSettingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Initialize the system default sharedPreference
        stockSharedPref=PreferenceManager.getDefaultSharedPreferences(getContext());
         editor=stockSharedPref.edit();
         //Initialize the button component in the stocksetting dialog
        cancelButton=stockSettingDialog.findViewById(R.id.setting_cancel);
        setButton=stockSettingDialog.findViewById(R.id.setting_setting);
        //Initialize the textinut
        lowInput=stockSettingDialog.findViewById(R.id.lowstockinput);
        hightInput=stockSettingDialog.findViewById(R.id.highstockInput);
        closeSettingImage=stockSettingDialog.findViewById(R.id.close_setting);
//      if(closeSettingImage!=null){
//         // closeSettingImage.setVisibility(View.INVISIBLE);
//        Log.d("CloseImage","hello");}
        /**
         * The event method to close the dialog
         */
        closeSettingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockSettingDialog.dismiss();
            }
        });
        //The event method to close the dialog ,if user choose cancal button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockSettingDialog.dismiss();
            }
        });
      //If user click set the value, the value will be write to the shared preference file
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("CloseImage","pipi");
                String lowStock=lowInput.getText().toString().trim();
                String highStock=hightInput.getText().toString().trim();
                //Write to the System default preference.
                editor.putString("lowStockLine",lowStock);
                editor.putString("highStockLine",highStock);
                editor.commit();
                stockSettingDialog.dismiss();
            }
        });


    }

    /**
     *
     * @param bundle
     * @param s
     * Show all the setting preference lists in window
     */

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //load the prefence layout
        addPreferencesFromResource(R.xml.setting_layout);
        //retrieve the preference list using "key"
       unitPreference= getPreferenceManager().findPreference("unitkey");
       stockLinePreference=getPreferenceManager().findPreference("stockwarningkey");
       //show the stocksetting dialog if user choose the preference item.
      stockLinePreference.setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
       @Override
       public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {

          stockSettingDialog.show();
          //Initialize the lowstockLine and highStockLine value from the sharedpreference file
        if(stockSharedPref.getString("lowStockLine","")!=null){
            lowInput.setText(stockSharedPref.getString("lowStockLine",""));
        }
        if(stockSharedPref.getString("highStockLine","")!=null){
            hightInput.setText(stockSharedPref.getString("highStockLine",""));
        }

           return true;
       }
   });

   //Retrieve the exit preference
   exitPreference=getPreferenceManager().findPreference("exitkey");
        /**
         * Show the exit dialog if user click the preference list
         */
   exitPreference.setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
       @Override
       public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
           //Customerize the dialog
          Log.d("exitpreference","i get it");
           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
           alertDialogBuilder.setTitle("Exit Application?");
           alertDialogBuilder
                   .setMessage("Click yes to exit!")
                   .setCancelable(false)
                   .setPositiveButton("Yes",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   //moveTaskToBack(true);
                                   //Exit the system
                                   android.os.Process.killProcess(android.os.Process.myPid());
                                   System.exit(1);
                               }
                           })

                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {

                           dialog.cancel();
                       }
                   });
          //show the AlertDialog
           AlertDialog alertDialog = alertDialogBuilder.create();
           alertDialog.show();

           return true;
       }
   });



    }











    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
