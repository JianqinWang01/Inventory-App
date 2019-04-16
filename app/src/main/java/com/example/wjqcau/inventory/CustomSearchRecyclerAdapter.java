package com.example.wjqcau.inventory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.example.wjqcau.inventory.JavaBean.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author wjqcau
 * Date created: 2019-03-15
 * This class is used in SearchFragment
 * It define the recycleview to show the search result
 * It will also dynamic upate the search result accroding searching condition
 * Which implement the Filterble interface
 */
public class CustomSearchRecyclerAdapter extends RecyclerView.Adapter<CustomSearchRecyclerAdapter.SearchViewHoler> implements Filterable {
   //Declare two arraylists to hold the origin data and the filtered data
    ArrayList<SearchResult> originList=new ArrayList<>();
    ArrayList<SearchResult> filterList;
    Context context;
    //Constructor to populate the properties
    public CustomSearchRecyclerAdapter(Context context,ArrayList<SearchResult> originList){
        //Populate the origin data
        this.originList=originList;
        //Populate the filetered data
        filterList=new ArrayList<>(originList);
        this.context=context;
    }

    @NonNull
    @Override
    public CustomSearchRecyclerAdapter.SearchViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item,viewGroup,false);
       //Define the viewholder object
      final SearchViewHoler viewHoler=new SearchViewHoler(view);
      //Declare the click event while user click on one item
       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Populate the the floatList class
               SearchFloatList floatContent=new SearchFloatList(originList.get(viewHoler.getAdapterPosition()).getProdId(),
                  originList.get(viewHoler.getAdapterPosition()).getCateName());
           //Show the bottom framgment which will floating from bottom
          floatContent.show(HomeFragment.fm,"floatup");


           }
       });
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHoler searchViewHoler, int i) {
       //Binding all the values to the components
        searchViewHoler.productName.setText(originList.get(i).getProdName());
        searchViewHoler.productPrice.setText(originList.get(i).getProdPrice());
        searchViewHoler.categoryName.setText(originList.get(i).getCateName());

    }

    @Override
    public int getItemCount() {
        return (null!=originList ? originList.size():0);
    }
    //Implement the method
    @Override
    public Filter getFilter() {
        return searchFilter;
    }
    //Define the filter object
    private Filter searchFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchResult> filteredList = new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                //if the user does not input the search text, then return the previous value
                filteredList.addAll(filterList);
            }else{
                //If the user input the text value in the search  bar
                String filterPattern=constraint.toString().toLowerCase().trim();
                //Loop to filter the each items
                for(SearchResult item:filterList){
                    //if the user input is contained in the name
                    if(item.getProdName().toLowerCase().contains(filterPattern)){
                      //add the list to the filetered list
                        filteredList.add(item);
                    }
                }

            }
            //define the filer list
            FilterResults results = new FilterResults();
            //set the value with the arraylist
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          //clear the origin arraylist
            originList.clear();
            //use the new value to as the origin arraylist
            originList.addAll((List)results.values);
            //update the search result
            notifyDataSetChanged();

        }
    };

//The inner class define the viewholder
    class SearchViewHoler extends RecyclerView.ViewHolder{
       //declare the properties in the viewholder
         TextView productName;
         TextView productPrice;
         TextView categoryName;
        public SearchViewHoler(@NonNull View itemView) {
            super(itemView);
            //get the components in the viewholder
            productName=itemView.findViewById(R.id.searchProductName);
            productPrice=itemView.findViewById(R.id.searchProductPrice);
            categoryName=itemView.findViewById(R.id.searchCategoryName);
        }
    }
}
