package com.example.wjqcau.inventory;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wjqcau.inventory.JavaBean.RefProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author wjqcau
 * Date created: 2019-04-11
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceReferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceReferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceReferFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //Declare the product arraylist
    private ArrayList<RefProduct> refProducts;
    //declare the recyclerview
    RecyclerView refRecyclerView;
    //declare the product name arraylis which will be used in the spinner
    private ArrayList<String> productNames;
    //Decalre spinner and dialouge
    private ProgressDialog dialog;
    private Spinner spinner;

    public PriceReferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriceReferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PriceReferFragment newInstance(String param1, String param2) {
        PriceReferFragment fragment = new PriceReferFragment();
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
        refProducts=new ArrayList<>();
        productNames=new ArrayList<>();
        //Populate the products from database
        DatabaseHandler db=new DatabaseHandler(getContext());
        productNames=db.getAllProductNames();
        db.close();
        //populate the dialogue
        dialog=new ProgressDialog(getContext());
        dialog.setMessage("Please waiting...");
        //Invisible the AddCategory Image
        MainActivity.addCategoryImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_price_refer, container, false);


       // Get the spinner from the id in the view
         spinner=view.findViewById(R.id.productNameSpinner);
         //Define the spinner adapter for the spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, productNames);
        //set the spinner layout with the simple drop down item
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the spinner adapater
        spinner.setAdapter(dataAdapter);
        //get the recyclerview
        refRecyclerView=view.findViewById(R.id.refRecyclerView);
        //Get the button in the view accroding the id
        Button GrabJoson=view.findViewById(R.id.getJsonBut);
        //set the button click event
        GrabJoson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Step 1: clear the product arraylist
                refProducts.clear();
                //show the progress dialogue
                dialog.show();
                //call the method to grab the data from walmart
              GrabData();

            }
        });

        return view;
    }

    // Grab the Json from Warlmart api and populate the product arraylist Initial the Arraylist
    public void GrabData(){
        //set the request url
        String url=Config.WalmartString+spinner.getSelectedItem().toString()+Config.WalmartKey;
       // Log.d("URLoutput",url);
        //Using volley to grab the json
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        //Define the jsonobject
        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            //Get the json array
                            // String showString="";
                            JSONArray jsonArray=response.getJSONArray("items");
                            //For loop to get each refprice and add them to the arraylist
                            for(int i=0;i<jsonArray.length();i++){
                               //Get the json object of each item
                                JSONObject productItem=jsonArray.getJSONObject(i);
                                //Get each value in the productItem
                                String productName=productItem.getString("name");
                                String productPrice=productItem.getString("salePrice");
                                String imageURl=productItem.getString("thumbnailImage");
                                String cateName=productItem.getString("categoryPath");
                                //showString+=productName+";"+productPrice+"\n"+imageURl+"\n";
                                //Populate one refproduct object and add it to the array
                                refProducts.add(new RefProduct(productName,productPrice,
                                        imageURl,cateName));
                            }
                            //Declare the dapater
                            CustomRefPriceAdapter adapter=new CustomRefPriceAdapter(getContext(),refProducts);
                            //Set the adapater to the recylceriew
                            refRecyclerView.setAdapter(adapter);
                            //Set the layout of the recyclerview
                            refRecyclerView.setHasFixedSize(true);
                            refRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                           //dismiss the progress dialogue
                            dialog.dismiss();

                            //showJosonText.setText(showString);


                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", "onErrorResponse: ");
                    }
                }


        );
        requestQueue.add(objectRequest);

    }



//    public void showProductList(){
//
//    }
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
