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

public class CustomSearchRecyclerAdapter extends RecyclerView.Adapter<CustomSearchRecyclerAdapter.SearchViewHoler> implements Filterable {
    ArrayList<SearchResult> originList=new ArrayList<>();
    ArrayList<SearchResult> filterList;

    Context context;
    public CustomSearchRecyclerAdapter(Context context,ArrayList<SearchResult> originList){
        this.originList=originList;
        filterList=new ArrayList<>(originList);
        this.context=context;
    }


    @NonNull
    @Override
    public CustomSearchRecyclerAdapter.SearchViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item,viewGroup,false);
      final SearchViewHoler viewHoler=new SearchViewHoler(view);
       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


          SearchFloatList floatContent=new SearchFloatList(originList.get(viewHoler.getAdapterPosition()).getProdId(),
                  originList.get(viewHoler.getAdapterPosition()).getCateName());

          floatContent.show(HomeFragment.fm,"floatup");


           }
       });
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHoler searchViewHoler, int i) {

        searchViewHoler.productName.setText(originList.get(i).getProdName());
        searchViewHoler.productPrice.setText(originList.get(i).getProdPrice());
        searchViewHoler.categoryName.setText(originList.get(i).getCateName());

    }



    @Override
    public int getItemCount() {
        return (null!=originList ? originList.size():0);
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchResult> filteredList = new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filteredList.addAll(filterList);
            }else{
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(SearchResult item:filterList){
                    if(item.getProdName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            originList.clear();
            originList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };


    class SearchViewHoler extends RecyclerView.ViewHolder{
         TextView productName;
         TextView productPrice;
         TextView categoryName;
        public SearchViewHoler(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.searchProductName);
            productPrice=itemView.findViewById(R.id.searchProductPrice);
            categoryName=itemView.findViewById(R.id.searchCategoryName);
        }
    }
}
