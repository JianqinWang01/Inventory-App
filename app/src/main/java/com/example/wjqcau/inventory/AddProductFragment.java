package com.example.wjqcau.inventory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {
    FragmentManager fm;
    EditText priceInput;
     EditText amountInput;
      EditText nameInput;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        MainActivity.addCategoryImage.setVisibility(View.INVISIBLE);
        fm=getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =  inflater.inflate(R.layout.fragment_add_product, container, false);
        Button addProductButtton=view.findViewById(R.id.addProductButton);
        priceInput=view.findViewById(R.id.addproductPriceInput);
         amountInput=view.findViewById(R.id.addproductAmountInput);
        nameInput=view.findViewById(R.id.addproductNameInput);

        final ImageView showProductImage=view.findViewById(R.id.addProductImage);
        showProductImage.setImageResource(R.drawable.camera);

        Button takePhotoButton=view.findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//
//                Log.d("ShowProduct","Done");
                Picasso.with(getContext())
                        .load("https://jwang.scweb.ca/PhotoServer/images/mytest.jpg").memoryPolicy(MemoryPolicy.NO_CACHE).
                        networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                        into(showProductImage);
            }
        });



        addProductButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product=new Product(nameInput.getText().toString(),priceInput.getText().toString(),
                        amountInput.getText().toString(),"https://jwang.scweb.ca/PhotoServer/images/mytest.jpg",CategoryProductAdapter.categoryId);

               DatabaseHandler db=new DatabaseHandler(getContext());
               db.addProduct(product);
               db.close();
                FragmentTransaction transaction=fm.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content,new HomeFragment());
                transaction.commit();
            }
        });



        return view;
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
