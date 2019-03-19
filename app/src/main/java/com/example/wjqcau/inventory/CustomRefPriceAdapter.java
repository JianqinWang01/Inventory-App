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

public class CustomRefPriceAdapter extends RecyclerView.Adapter<CustomRefPriceAdapter.RefPriceViewHolder> {

    ArrayList<RefProduct> refProducts=new ArrayList<>();
    Context context;
    public CustomRefPriceAdapter(Context context,ArrayList<RefProduct> refProducts){
        this.context=context;
        this.refProducts=refProducts;
    }

    @NonNull
    @Override
    public RefPriceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.priceref_item,viewGroup,false);
        RefPriceViewHolder viewHolder=new RefPriceViewHolder(view);




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RefPriceViewHolder refPriceViewHolder, int i) {
        RefProduct product=refProducts.get(i);
        refPriceViewHolder.showPrice.setText(product.getSalePrice());
        refPriceViewHolder.prodName.setText(product.getName());
        refPriceViewHolder.showCateName.setText(product.getCategory());
        //*****************need picasso
        Picasso.with(context)
                .load(product.getImageUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).
                networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                into(refPriceViewHolder.showImage);

    }


    @Override
    public int getItemCount() {
        return (null!=refProducts?refProducts.size():0);
    }



    public class RefPriceViewHolder extends RecyclerView.ViewHolder{
        ImageView showImage;
        TextView showPrice;
        TextView prodName;
        TextView showCateName;

        public RefPriceViewHolder(@NonNull View itemView) {
            super(itemView);
         showCateName=itemView.findViewById(R.id.categoryName);
         prodName=itemView.findViewById(R.id.productName);
         showPrice=itemView.findViewById(R.id.productPrice);
         showImage=itemView.findViewById(R.id.productIamge);
        }
    }
}
