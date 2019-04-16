package com.example.wjqcau.inventory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wjqcau.inventory.JavaBean.Product;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * @author wjqcau
 * Date created: 2019-04-10
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProductFragment extends Fragment {
    //Decare the Fragmentmanager
    FragmentManager fm;
    //Declare the edittext for user to input the price,amount,product name
    EditText priceInput;
    EditText amountInput;
    EditText nameInput;
    //declare the image url for remote url
    String ImageURL="";
    //declare a spinner
    Spinner unitSpinner;
    //declare the spinner list
    List<String> spinerList;
    //Define variables for take photos
    private  int productMaxId;
    private String encoded_string,image_name;
    private Bitmap bitmap;
    private File file;
    private  Uri file_uri;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=10;
    ImageView showImage;
    private static Intent i;
    ImageView produtImage;
    //declare the sharedpreference
    SharedPreferences settingPref;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Product mParam1;
   // private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UpdateProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment UpdateProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProductFragment newInstance(Parcelable param1) {
        UpdateProductFragment fragment = new UpdateProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);

        }
              spinerList = new ArrayList<String>();
        MainActivity.addCategoryImage.setVisibility(View.INVISIBLE);
        fm=getFragmentManager();
        settingPref=PreferenceManager.getDefaultSharedPreferences(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_update_product, container, false);
        //get the imageview
        produtImage=view.findViewById(R.id.updateProductImage);
        //get the button
        Button takePhotoButton=view.findViewById(R.id.takePhotoButton);
        //inflator the updatebutton
        Button updateProductButton=view.findViewById(R.id.updateProductButton);
        //inflate the editinput text
        nameInput=view.findViewById(R.id.updateNameInput);
         priceInput=view.findViewById(R.id.updatePriceInput);
        amountInput=view.findViewById(R.id.updateAmountInput);
        spinerList.clear();


        //add spinner for the product unit

        if(settingPref.getString("unitkey","lb")!=null){
            //get the unit from the sharedprefence file
            spinerList.add(settingPref.getString("unitkey","lb"));
        }else{
            //else give the defalut value
            spinerList.add("lb");
        }
        spinerList.add("PKG");
        //get the spinner from the view
        unitSpinner=(Spinner) view.findViewById(R.id.updateseclectUnit);
        //define the adapter
          ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinerList);
          //set the adapter style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the spinner with the adapter
        unitSpinner.setAdapter(dataAdapter);

        if(mParam1!=null){
            //grab the information from the pacel of the product
            priceInput.setText(mParam1.getPrice());
            amountInput.setText(mParam1.getAmount());
            nameInput.setText(mParam1.getName());
            if(mParam1.getUnit().equals("PKG")){
            unitSpinner.setSelection(1);}
            else unitSpinner.setSelection(0);
            //load the old picture
            Picasso.with(getContext())
                    .load(mParam1.getImageUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).
                    networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                    into(produtImage);
           //when user click the take photo button
          takePhotoButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //Create the url
                  ImageURL="https://jwang.scweb.ca/PhotoServer/images/"+mParam1.getId()+".jpg";
                  StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                  StrictMode.setVmPolicy(builder.build());
                  //define the intent to take photo
                  i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                  image_name=mParam1.getId()+".jpg";
                  //Create local file Uri
                  file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+
                          File.separator+image_name);
                  file_uri= Uri.fromFile(file);
                  //Log.w("FileUri",file_uri.toString());
                  //put the data to the intent
                  i.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);
                  //start the activity to run the intent
                  startActivityForResult(i,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

              }
          });
          //Click the updatebutton
          updateProductButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //Step 1: update the product in the database
                  DatabaseHandler db=new DatabaseHandler(getContext());
                  db.updateProduct(nameInput.getText().toString(),
                          priceInput.getText().toString(),
                          amountInput.getText().toString(),
                         unitSpinner.getSelectedItem().toString(),mParam1.getId());
                  db.close();
                  //Step 2: clear the input edittext in the interface
                  nameInput.setText("");
                  priceInput.setText("");
                  amountInput.setText("");
                  //Step 3: transition to home screen
                  FragmentTransaction transaction=fm.beginTransaction();
                  transaction.addToBackStack(null);
                  transaction.replace(R.id.content,new HomeFragment());
                  transaction.commit();

              }
          });

        }
        return view;
    }

    /**
     * Mainly get permission and execute upload image to server
     * @param requestCode CAMERA_CAPTURE_IMAGE_REQUEST_CODE
     * @param resultCode constant of Result_ok
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the request is sent by camera
        if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode== RESULT_OK) {
            //Toast.makeText(getContext(), "No", Toast.LENGTH_LONG).show();
            Log.d("Result_call","enterResult");
            // new Encode_Image().execute();
          //Reuqest the permission
            if(ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    !=PackageManager.PERMISSION_GRANTED)
            {
                //at first add product had run
              //because update absolutely second time

            }else{
               // Log.d("PermissionSecond","PermissionSecondTime");
               //Define the class and execute task
                new Encode_Image().execute();
               // Log.d("LocalURL",file_uri.getPath());
                //show the image back to the interface
                Picasso.with(getContext())
                        .load("file://"+file_uri.getPath()).memoryPolicy(MemoryPolicy.NO_CACHE).
                        networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                        into(produtImage);

            }

        }else{
            Toast.makeText(getContext(),"Permission denied!",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Inner class used to compressed image and upload to server
     */
   private class Encode_Image extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //Log.d("BitMap",file_uri.getPath());
           //Define the bitmap from the the path
            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
           // Log.d("BitMap",bitmap == null?"Empty":"Good");

            //showImage.setImageBitmap(bitmap);
            // Log.d("showError",file_uri.getPath());
            //add the data to outputstream
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            //Compress the data
            bitmap.compress(Bitmap.CompressFormat.JPEG,1,stream);
            byte[] array=stream.toByteArray();
            //encoding the data with 64digits
            encoded_string=Base64.encodeToString(array,0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           //call the request
            makeRequest();

        }
    }
    private  void makeRequest(){
       //define the request queue object
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        //define the request object
        StringRequest request=new StringRequest(Request.Method.POST, Config.FILE_UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //add method here
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Define the hashmap
                HashMap<String,String> map=new HashMap<>();
                //put the hashmap with the encoded data and string
                map.put("encoded_string",encoded_string);
                map.put("image_name",image_name);
                return  map;
            }
        };
        //add the request to the requestquen
        requestQueue.add(request);


    }

    public void ExecuteImageUpload(){
        new Encode_Image().execute();
        //download picture from server


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

     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
