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
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {
    //Declare the FragmentManager for fragment transition
    FragmentManager fm;
    //Declare the components in the AddFragment
    //user's input price
    EditText priceInput;
    //user's input stock amount
    EditText amountInput;
    //user's input product name

    EditText nameInput;
    //Image UrL

    String ImageURL="";
      Spinner unitSpinner;
    List<String> spinerList;
    //Define variables for taking photos
    private  int productMaxId;
    //Declare the encoded_string is the compressed value of the image
    private String encoded_string,image_name;
    //Declare the bitmap object to hold the image taking from the camera
    private Bitmap bitmap;
    //Declare the image file and the file's uri
    private File file;
    private  Uri file_uri;
    //declare the intent code which will send to the Activity_result method
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=10;
    ImageView showImage;
    private static  Intent i;
    ImageView showProductImage;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    SharedPreferences settingPref;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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
        //set the Addproduct icon visible
        MainActivity.addCategoryImage.setVisibility(View.INVISIBLE);
        //Initialize the fragmentmanger object and spinnerList
        fm=getFragmentManager();
        spinerList = new ArrayList<String>();
        //initialize the user's setting value for Shared Preference file
        settingPref=PreferenceManager.getDefaultSharedPreferences(getContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =  inflater.inflate(R.layout.fragment_add_product, container, false);
     //Clear the spinner list because each time the user can change the setting value and load again
     spinerList.clear();
      //add spinner
       if(settingPref.getString("unitkey","lb")!=null){
           //if user setting the product unit, then assign the value
           spinerList.add(settingPref.getString("unitkey","lb"));
       }else{
           //if user's didnot setting the value, assign the default value
           spinerList.add("lb");
       }
       //PKG is the package unit, not the weight unit
        spinerList.add("PKG");

        unitSpinner = (Spinner) view.findViewById(R.id.seclectUnit);
        //set the default selection position is "0"
        unitSpinner.setSelection(0);
       //Declare the spinner adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinerList);
        //setting the spinner adapter's style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(dataAdapter);
        //Retrieve the components from the AddProductFragment view
        Button addProductButtton=view.findViewById(R.id.addProductButton);
        priceInput=view.findViewById(R.id.addproductPriceInput);
         amountInput=view.findViewById(R.id.addproductAmountInput);
        nameInput=view.findViewById(R.id.addproductNameInput);
        showProductImage=view.findViewById(R.id.addProductImage);
        showProductImage.setImageResource(R.drawable.camera);
        Button takePhotoButton=view.findViewById(R.id.takePhotoButton);

        //Add clickListener to the takePhotoButton
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We use the new inserted id plus extention as the picture name
                //Step1: get  Max product id from database
                DatabaseHandler db=new DatabaseHandler(getContext());
                //Step 2: add 1 , so the productMaxId is the newest inserting product's id
              productMaxId=db.getProductMaxId()+1;
              db.close();
            //Step 3:Integrate the Image URL which consist of the remote Server's folder,product's id and extension
             ImageURL="https://jwang.scweb.ca/PhotoServer/images/"+productMaxId+".jpg";
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                //Initialize the intent with the taking photo's intent
                i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Assign the name to the variable
                image_name=productMaxId+".jpg";
              //Create local file Uri
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+
                        File.separator+image_name);
                file_uri= Uri.fromFile(file);
                //Step 4: put value to intent
               i.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);
               //Call the Activity_resutl, while passing the Request_code
                startActivityForResult(i,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

            }
        });

        //Add button event listener, when user click the button, all the information will be stored in the database and
        //show the product in the HomeScreen
        addProductButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // get the float value of the user's input
                Float price=Float.parseFloat(priceInput.getText().toString());
               // Format the value with the fixed format, keeping 2 digital decimal
                String formattedPrice=String.format("%.2f",price);
                //Initialize the product object
                Product product=new Product(nameInput.getText().toString(),formattedPrice,
                        amountInput.getText().toString(),ImageURL,unitSpinner.getSelectedItem().toString(),
                        CategoryProductAdapter.categoryId);
              // Save  the new product object to the database
               DatabaseHandler db=new DatabaseHandler(getContext());
               db.addProduct(product);
               db.close();
              //Clear all the user's input, including name,price,and amount
               nameInput.setText("");
               priceInput.setText("");
               amountInput.setText("");
               //Transfer to HomeScreen
                FragmentTransaction transaction=fm.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content,new HomeFragment());
                transaction.commit();
            }
        });

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
        //If the app doesn't get the permission
        if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode== RESULT_OK) {
           //If this request is sent by taking photos and the intent start is ok
            //If the app doesn't get the permission
            if(ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    !=PackageManager.PERMISSION_GRANTED)
            {  //App lauch first tim
                //Ask the user to allow to use camera
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                }else {

                  //If the user's have allowed the permission, ask the activity to execute background task
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            666);
                      }


            }else{
                //For the second time, user have get the permission
               //Declare the background object and then execute the asynchronized task which will compress the image
                //and upload the image to Scweb Server
                new Encode_Image().execute();
               //Load the remote image on the server into showProductImage (Imageview)
                Picasso.with(getContext())
                        .load("file://"+file_uri.getPath()).memoryPolicy(MemoryPolicy.NO_CACHE).
                        networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                        into(showProductImage);

            }

        }else{
            //If the user deny the permission, show the result
            Toast.makeText(getContext(),"Permission denied!",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @author wjqcau
     * Inner class used to compressed image and upload to server
     */
    public class Encode_Image extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
           //Step 1 :Get the bitmap from the local device
            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            //Declare the outputstream to transfer the image
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            //compress the bitmap format iamge to jpg format
            bitmap.compress(Bitmap.CompressFormat.JPEG,1,stream);
            byte[] array=stream.toByteArray();
            //Encoding the data from byte data style to String style
            encoded_string=Base64.encodeToString(array,0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //call the background method above
            super.onPostExecute(aVoid);
            //Call the request method below
            makeRequest();

        }
    }
    private  void makeRequest(){
        //Define a requestQueue object
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        //Define the request object which will hold the information to upload to server
        StringRequest request=new StringRequest(Request.Method.POST, Config.FILE_UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            //Declare method to get the map object to hold the parameter's
            protected Map<String, String> getParams() throws AuthFailureError {
                // declare the hashmap
                HashMap<String,String> map=new HashMap<>();
                map.put("encoded_string",encoded_string);
                map.put("image_name",image_name);
                return  map;
            }
        };
        //Add the task to task queue
        requestQueue.add(request);


    }
//This method built for outer module to call it(In mainactivity )
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
         * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
