package com.example.wjqcau.inventory;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
   private ArrayList<ProductCategory> categories;
   Spinner cateSpinner;
   //private ArrayList<String> categoryNames;
   private ArrayList<Product> products;
    LineChart mChart;


    View  fragmentView;



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
        categories=new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_stock_warning, container, false);
        DatabaseHandler db=new DatabaseHandler(getContext());
        categories=db.getAllCategories();
        db.close();
        cateSpinner=(Spinner)fragmentView.findViewById(R.id.spinner);

//        for(ProductCategory category : categories){
//            categoryNames.add(category.getTitle());
//        }
        ArrayAdapter<ProductCategory> dataAdapter = new ArrayAdapter<ProductCategory>(getActivity(),
                android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cateSpinner.setAdapter(dataAdapter);
        Button choiceCategoryButton=fragmentView.findViewById(R.id.choiceCategory);
        mChart=(LineChart)fragmentView.findViewById(R.id.LineChart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

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
                for(int i=0;i<products.size();i++){
                    yValues.add(new Entry((float) i,Float.parseFloat(products.get(i).getAmount())));
                }
               //addDataSetToChart();

                LineDataSet dataSetSet1=new LineDataSet(yValues,category.getTitle());
                dataSetSet1.setFillAlpha(110);
                dataSetSet1.setColor(Color.GREEN);
                dataSetSet1.setLineWidth(3f);
                dataSetSet1.setValueTextSize(15f);
                dataSetSet1.setValueTextColor(Color.RED);
                dataSetSet1.setCircleColor(Color.BLACK);

                //add Limitation
                LimitLine limitLineHigh=new LimitLine(35f,"Tow Much Stock");
                limitLineHigh.setLineWidth(4f);
                limitLineHigh.enableDashedLine(10f,10f,0);
                limitLineHigh.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                limitLineHigh.setTextSize(10f);

                LimitLine limitLineLow=new LimitLine(10f,"Tow Less Stock");
                limitLineLow.setLineWidth(4f);
                limitLineLow.enableDashedLine(10f,10f,0);
                limitLineLow.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                limitLineLow.setTextSize(10f);

                YAxis leftAxis=mChart.getAxisLeft();
                leftAxis.removeAllLimitLines();
                leftAxis.addLimitLine(limitLineHigh);
                leftAxis.addLimitLine(limitLineLow);


                leftAxis.setAxisMaximum(50f);
                leftAxis.setAxisMinimum(5f);
                leftAxis.enableGridDashedLine(10f,10f,10);
                leftAxis.setDrawLimitLinesBehindData(true);
                mChart.getAxisRight().setEnabled(false);
              //set product title

                String[] values=new String[products.size()];
                for(int i=0;i<products.size();i++){
                    values[i]=products.get(i).getName();
                }



                XAxis xAxis=mChart.getXAxis();
                xAxis.setValueFormatter(new MyXAxisFormatter(values));
                xAxis.setGranularity(1f);



                ArrayList<ILineDataSet> dataSets=new ArrayList<>();
                dataSets.add(dataSetSet1);
                LineData data=new LineData(dataSets);
                mChart.setData(data);
                mChart.notifyDataSetChanged();
                mChart.invalidate();


            }
        });







        return fragmentView;
    }

    public class MyXAxisFormatter implements IAxisValueFormatter {
        public String[] mValues;
        public MyXAxisFormatter(String[] mValues){
            this.mValues=mValues;

        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Log.d("dataset",value+" ");
           if((int)value<=this.mValues.length-1)
            return mValues[(int)value];
           else return null;
        }
    }


//    public void addDataSetToChart(){
//        yValues.clear();
//        for(int i=0;i<products.size();i++){
//            yValues.add(new Entry(i,Float.parseFloat(products.get(i).getAmount())));
//        }
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
