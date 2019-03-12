package com.example.wjqcau.inventory;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder> {
    Context context;
    ArrayList<Product> prodList=new ArrayList<>();

    public ProductItemAdapter(Context context, ArrayList<Product> prodList){
        this.context=context;
        this.prodList=prodList;


    }

    @NonNull
    @Override
    public ProductItemAdapter.ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
     final   View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_card,viewGroup,false);
       final ProductItemViewHolder viewHolder=new ProductItemViewHolder(view);

        final Dialog  deleteProdDialog=new Dialog(context);
        deleteProdDialog.setContentView(R.layout.deleteconfirmlayout);
        deleteProdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView deleteConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductYes);
        TextView cancelConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductNo);


      //-------------------------Delete product-------------------------------------------------
       viewHolder.deleteProductImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           deleteProdDialog.show();

           }
       });
       cancelConfirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               deleteProdDialog.dismiss();
           }
       });
       deleteConfirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatabaseHandler db=new DatabaseHandler(context);
               db.deleteProduct(prodList.get(viewHolder.getAdapterPosition()));
               db.close();
               deleteProdDialog.dismiss();
               notifyItemRemoved(viewHolder.getAdapterPosition());
               notifyItemChanged(viewHolder.getAdapterPosition());
               HomeFragment.transaction=HomeFragment.fm.beginTransaction();
               HomeFragment.transaction.addToBackStack(null);
               HomeFragment.transaction.replace(R.id.content,new HomeFragment());
               HomeFragment.transaction.commit();
           }
       });

      //---------------------Update product-----------------------------------------------------
        viewHolder.updateProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.transaction=HomeFragment.fm.beginTransaction();
                HomeFragment.transaction.addToBackStack(null);
                HomeFragment.transaction.replace(R.id.content,UpdateProductFragment.newInstance(prodList.get(viewHolder.getAdapterPosition())));
               // HomeFragment.transaction.replace(R.id.content,new UpdateProductFragment());
                HomeFragment.transaction.commit();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder productItemViewHolder, int i) {

      Product product=prodList.get(i);
      productItemViewHolder.prodName.setText(product.getName());
      productItemViewHolder.prodPrice.setText(product.getPrice());
      productItemViewHolder.unitPrice.setText("/"+product.getUnit());
     //After setting shared preference it will show
      productItemViewHolder.currencySign.setText("$");
      productItemViewHolder.unit.setText(product.getUnit());
      //*****************need picasso
        Picasso.with(context)
                .load(product.getImageUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).
                networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                into(productItemViewHolder.prodImage);
       // Picasso.with(context).load(product.getImageUrl()).into(productItemViewHolder.prodImage);
       // productItemViewHolder.prodImage.setImageResource(product.getProdImage());
      productItemViewHolder.prodAmount.setText(product.getAmount());

    }



    @Override
    public int getItemCount() {
        return (null!=prodList ?prodList.size():0);
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder {
       TextView prodName;
       ImageView prodImage;
       TextView prodPrice;
       TextView prodAmount;
       ImageView deleteProductImage;
       ImageView updateProductImage;
       TextView currencySign;
       TextView unitPrice;
       TextView unit;
       //ImageButton prodEditBut;

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.productName);
            prodImage=itemView.findViewById(R.id.productIamge);
            prodPrice=itemView.findViewById(R.id.productPrice);
            prodAmount=itemView.findViewById(R.id.productQuantity);
            deleteProductImage=itemView.findViewById(R.id.deleteProduct);
            updateProductImage=itemView.findViewById(R.id.updateProduct);
            currencySign=itemView.findViewById(R.id.currencySign);
            unitPrice=itemView.findViewById(R.id.unitPrice);
            unit=itemView.findViewById(R.id.unit);


        }



    }

}
