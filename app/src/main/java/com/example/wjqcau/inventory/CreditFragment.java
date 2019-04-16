package com.example.wjqcau.inventory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditFragment newInstance(String param1, String param2) {
        CreditFragment fragment = new CreditFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //Get the CreditFragment View
       View view=inflater.inflate(R.layout.fragment_credit, container, false);
       //Get the logoView which hold the logo
        ImageView logoView=view.findViewById(R.id.creditsIV);
        //Get the screen size of the current screen, it will be: Portain, Landscape
        int screenSize = getResources().getConfiguration().screenLayout &
                SCREENLAYOUT_SIZE_MASK;
        //Judge if the screen is current on landscape, set the logo with the big size
        switch(screenSize) {
            case SCREENLAYOUT_SIZE_XLARGE:
                //Reset the logo with big size, width and height
               logoView.getLayoutParams().width=600;
               logoView.getLayoutParams().height=600;
               //refresh the Logo in the view
               logoView.requestLayout();
                break;

        }
       //Define the viewpager using the id in the Fragment_Credit.xml
        ViewPager creditViewPager=view.findViewById(R.id.credits_viewpager);
        //Define the viewpager adapter object
        CustomAdpter adpter=new CustomAdpter(getChildFragmentManager());
        //Set the adapter
        creditViewPager.setAdapter(adpter);
        //Set the viewpager with the Depth animation
        creditViewPager.setPageTransformer(true,new CreditDepthPageTransformer());

        return view;
    }

    /**
     * This inner class is the customerize viewpager adapter
     */
    public class CustomAdpter extends FragmentPagerAdapter{
        //define the Constructor
        public CustomAdpter(FragmentManager fm) {
            super(fm);
        }

        /**
         * @param i : the #number of the each pager while swipe the screen,start index is 0
         * @return  : the instance of the each fragment pager
         */
        @Override
        public Fragment getItem(int i) {
            switch (i){
                //Return the first pager with the value of iamge and icon
                case 0: return CreditItemFragment.newInstance(getString(R.string.credits_header_image),getString(R.string.credits_image_description));
                //Return the second pager with the value of API SDK
                case 1: return CreditItemFragment.newInstance(getString(R.string.credits_header_api),getString(R.string.credits_api_description));
                //Return the third pager with the value of iamge and icon
                default:return CreditItemFragment.newInstance(getString(R.string.credits_header_github),getString(R.string.credits_github_description));

            }

        }
        @Override
        public int getCount() {
            //Return the number of the pagers
            return 3;
        }
    }
    //This class define the depth animation for swipe each pager
    public class CreditDepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
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
