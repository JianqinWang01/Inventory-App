package com.example.wjqcau.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.example.wjqcau.inventory.JavaBean.ProductCategory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


/**
 * @author wjqcau
 * Date created:2019-03-12
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StockWarningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StockWarningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockWarningFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //Declare the category arraylist
   private ArrayList<ProductCategory> categories;
   //declare the spinner
   Spinner cateSpinner;
   //declare the product arraylist
   private ArrayList<Product> products;
   //Declare the LineChart object
    LineChart mChart;
   //Declare the view
    View  fragmentView;
 //Delare the Shareprefernece file
   SharedPreferences settingPref;
   //Declare two variables to how the low and high values
   float lowStockLine,highStockLine;
    public StockWarningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockWarningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StockWarningFragment newInstance(String param1, String param2) {
        StockWarningFragment fragment = new StockWarningFragment();
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
        //Define the arraylist
        categories=new ArrayList<>();
      //define the sharedpreference
      settingPref=PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_stock_warning, container, false);
        //Get he categories from database
        DatabaseHandler db=new DatabaseHandler(getContext());
        categories=db.getAllCategories();
        db.close();
        cateSpinner=(Spinner)fragmentView.findViewById(R.id.spinner);
        //Define the adapter for spinner
        ArrayAdapter<ProductCategory> dataAdapter = new ArrayAdapter<ProductCategory>(getActivity(),
                android.R.layout.simple_spinner_item, categories);
        //Set the spinner with the layout
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the spinner adapter
        cateSpinner.setAdapter(dataAdapter);
        //get the button component
        Button choiceCategoryButton=fragmentView.findViewById(R.id.choiceCategory);
        //Get the LineChart object accroding its id in the view
        mChart=(LineChart)fragmentView.findViewById(R.id.LineChart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        //If user alread setting the value
       if(settingPref.getString("lowStockLine","10")!=null){
           //Get he low value
           lowStockLine=Float.parseFloat(settingPref.getString("lowStockLine","10"));
       }//else set the default value with
       else lowStockLine=10f;
       //if user set the hight value
       if(settingPref.getString("highStockLine","40")!=null){
           //get the high value
           highStockLine=Float.parseFloat(settingPref.getString("highStockLine","40"));
       }else{
           //esle give the default value
           highStockLine=35f;
       }

      //set the button event for create the line chart
        choiceCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //step 1:populate products array
               // products.clear();
                ProductCategory category=(ProductCategory) cateSpinner.getSelectedItem();
                products=category.getProductList();
               // Log.d("productList",products.size()+"");
                ArrayList<Entry> yValues=new ArrayList<>();
                yValues.clear();
                //Set the Y axis value
                for(int i=0;i<products.size();i++){
                    yValues.add(new Entry((float) i,Float.parseFloat(products.get(i).getAmount())));
                }
               //add DataSet ToChart;
                LineDataSet dataSetSet1=new LineDataSet(yValues,category.getTitle());
                //set the color ,alpha
                dataSetSet1.setFillAlpha(110);
                dataSetSet1.setColor(Color.GREEN);
                //set the line width
                dataSetSet1.setLineWidth(3f);
                //set the text font
                dataSetSet1.setValueTextSize(15f);
                //set the text color
                dataSetSet1.setValueTextColor(Color.RED);
                //set the circle color
                dataSetSet1.setCircleColor(Color.BLACK);

                //add Limitation line for high and low line
                LimitLine limitLineHigh=new LimitLine(highStockLine,"Too Much Stock");
               //set he hight line width
                limitLineHigh.setLineWidth(4f);
                //set the line with the dashed type
                limitLineHigh.enableDashedLine(10f,10f,0);
                //set the label position
                limitLineHigh.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                limitLineHigh.setTextSize(10f);
               //Define the low line
                LimitLine limitLineLow=new LimitLine(lowStockLine,"Too Less Stock");
                //set the line width
                limitLineLow.setLineWidth(4f);
                //set he line dashed style
                limitLineLow.enableDashedLine(10f,10f,0);
                //set the lable position
                limitLineLow.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                limitLineLow.setTextSize(10f);
                //Define the Axis of Y
                YAxis leftAxis=mChart.getAxisLeft();
                //remove default value
                leftAxis.removeAllLimitLines();
                //add two new line to the axis
                leftAxis.addLimitLine(limitLineHigh);
                leftAxis.addLimitLine(limitLineLow);

                //set the axis minimum and max value
                leftAxis.setAxisMaximum(50f);
                leftAxis.setAxisMinimum(5f);
                //Set the style with grid line
                leftAxis.enableGridDashedLine(10f,10f,10);
                leftAxis.setDrawLimitLinesBehindData(true);
                mChart.getAxisRight().setEnabled(false);

                //set product title in an arraylist
               //using loop
                String[] values=new String[products.size()];
                for(int i=0;i<products.size();i++){
                    values[i]=products.get(i).getName();
                   }

                 //add the view with this axix
                XAxis xAxis=mChart.getXAxis();
                //set the valueformat
                xAxis.setValueFormatter(new MyXAxisFormatter(values));
                xAxis.setGranularity(1f);
                //Define the datasets
                ArrayList<ILineDataSet> dataSets=new ArrayList<>();
                //add the dataset1 in the dataset
                dataSets.add(dataSetSet1);
                //load the data
                LineData data=new LineData(dataSets);
                //set the the linedata to machart
                mChart.setData(data);
                //reload the data if the datachaged
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        });

      //return this view

        return fragmentView;
    }
  //Difine the inner class
    //Show the product name at the top of axis
    public class MyXAxisFormatter implements IAxisValueFormatter {
        public String[] mValues;
        public MyXAxisFormatter(String[] mValues){
            this.mValues=mValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
//            Log.d("dataset",value+" ");
            if(mValues.length==1){
                return mValues[0];
            }
           if((int)value<=this.mValues.length-1)
            return mValues[(int)value];

           else return null;
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
