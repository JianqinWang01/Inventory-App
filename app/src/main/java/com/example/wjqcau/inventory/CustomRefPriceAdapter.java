package com.example.wjqcau.inventory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wjqcau.inventory.JavaBean.RefProduct;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author wjqcau
 * Create date: 2019-04-01
 * This class is mainly used in the PriceRefFragment
 * It define the recyclerview adapter which will display the result coming from the Walmart Api
 */
public class CustomRefPriceAdapter extends RecyclerView.Adapter<CustomRefPriceAdapter.RefPriceViewHolder> {
//Declare the properties for this adapter
    ArrayList<RefProduct> refProducts=new ArrayList<>();
    Context context;
    //Constructor: populate the properties
    public CustomRefPriceAdapter(Context context,ArrayList<RefProduct> refProducts){
        //populate context object
        this.context=context;
        //populate the refProducts arraylist
        this.refProducts=refProducts;
    }

    @NonNull
    @Override
    //This method mainly Create the viewholer and return back
    public RefPriceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Populate the view with the layout:price_item
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.priceref_item,viewGroup,false);
       //Define the RefpriceViewholder object
        RefPriceViewHolder viewHolder=new RefPriceViewHolder(view);
        return viewHolder;
    }
    //Binding and populate the value with the viewholder's components
    @Override
    public void onBindViewHolder(@NonNull RefPriceViewHolder refPriceViewHolder, int i) {
       //Get the Reproduct object according the
        RefProduct product=refProducts.get(i);
        //Set the price value for the price textview
        refPriceViewHolder.showPrice.setText(product.getSalePrice());
        //Set the product name
        refPriceViewHolder.prodName.setText(product.getName());
        //Set the category name
        refPriceViewHolder.showCateName.setText(product.getCategory());
        //*****************need picasso
        //Grab the image from the api address
        Picasso.with(context)
                .load(product.getImageUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).
                networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                into(refPriceViewHolder.showImage);
    }


    @Override
    public int getItemCount() {
        return (null!=refProducts?refProducts.size():0);
    }

   //Define the viewholder class
    public class RefPriceViewHolder extends RecyclerView.ViewHolder{
       //Declare the variables for holding the components in the viewholder
        ImageView showImage;
        //Price textview
        TextView showPrice;
        //Name textview
        TextView prodName;
        //Category name textview
        TextView showCateName;
        public RefPriceViewHolder(@NonNull View itemView) {
            super(itemView);
            //Get the name imageview
         showCateName=itemView.findViewById(R.id.categoryName);
         //Populate the product name
         prodName=itemView.findViewById(R.id.productName);
         //Populate the show price textview
         showPrice=itemView.findViewById(R.id.productPrice);
         showImage=itemView.findViewById(R.id.productIamge);
        }
    }
}
