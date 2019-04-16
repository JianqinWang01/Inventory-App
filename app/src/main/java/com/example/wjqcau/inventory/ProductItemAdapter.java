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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author wjqcau
 * Date created: 2019-04-15
 * This adapter is used in the HomeFragment
 * It is nesed in the CategoryAdapter and show the products within that category
 * user can delete,update the products
 */
public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder> {
   //Declare the properties for the adapter
    Context context;
    ArrayList<Product> prodList=new ArrayList<>();
   //Constructor to populate the properties
    public ProductItemAdapter(Context context, ArrayList<Product> prodList){
        this.context=context;
        this.prodList=prodList;
    }

    @NonNull
    @Override
    public ProductItemAdapter.ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      //Inflator the view
     final   View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_card,viewGroup,false);
      //Define the productItem viewholder and pass the view as parameter
       final ProductItemViewHolder viewHolder=new ProductItemViewHolder(view);
       //Populate the delete product dialogue
        final Dialog  deleteProdDialog=new Dialog(context);
        //Set the dialogue style with layout fiel:deleteconfirmlayout.xml
        deleteProdDialog.setContentView(R.layout.deleteconfirmlayout);
        deleteProdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView deleteConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductYes);
        TextView cancelConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductNo);


      //-------------------------Delete product-------------------------------------------------
       viewHolder.deleteProductImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //show the delete dialogue
           deleteProdDialog.show();

           }
       });
       //if user cancel to delete the product
       cancelConfirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //dismiss the dialogue
               deleteProdDialog.dismiss();
           }
       });
        //if user confirm to delete the product

        deleteConfirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Step1: delete the product from the dababase
               DatabaseHandler db=new DatabaseHandler(context);
               db.deleteProduct(prodList.get(viewHolder.getAdapterPosition()));
               db.close();
               //Step2:dismiss the dialogue
               deleteProdDialog.dismiss();
               //Notifty the adapter to upadate the data
               notifyItemRemoved(viewHolder.getAdapterPosition());
               notifyItemChanged(viewHolder.getAdapterPosition());
               //Refresh all the framework
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
                //Transition to the update framgement
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
     // get the product object accrording the position in the array
      Product product=prodList.get(i);
      //binding the values with the products to the components in the view holder
      productItemViewHolder.prodName.setText(product.getName());
      productItemViewHolder.prodPrice.setText(product.getPrice());
      productItemViewHolder.unitPrice.setText("/"+product.getUnit());
     //After setting shared preference it will show
      productItemViewHolder.currencySign.setText("$");
      productItemViewHolder.unit.setText(product.getUnit());
      //*****************need picasso to load the image to
        Picasso.with(context)
                .load(product.getImageUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).
                networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
                into(productItemViewHolder.prodImage);
      //Picasso.with(context).load(product.getImageUrl()).into(productItemViewHolder.prodImage);
       // productItemViewHolder.prodImage.setImageResource(product.getProdImage());
      productItemViewHolder.prodAmount.setText(product.getAmount());

    }



    @Override
    public int getItemCount() {
        return (null!=prodList ?prodList.size():0);
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder {
      //Declare the properties for the view holder
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
        //Constructor to populate the properties
        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //get all the properties from the view with id
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
