package com.example.wjqcau.inventory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * @author wjqcau
 * Date created:2019-04-15
 * This class will show the view floating from bottom while user click the produt item in the search
 * screen, it is used in the SearchFragment
 */
@SuppressLint("ValidFragment")
public class SearchFloatList extends BottomSheetDialogFragment {
    //Decare the properties which is contained in the fragment view
   private int productId;
   private String categoryName;
   //Constructor to populate the properties
    public SearchFloatList(int productId,String categoryName){
      this.productId=productId;
      this.categoryName=categoryName;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);

        View view=inflater.inflate(R.layout.searchselectlistview,container,false);
        //Get the produt accroding the id passed into
        DatabaseHandler db=new DatabaseHandler(getContext());
       final Product product=db.getProduct(productId);
        db.close();
        //Get the components in the view accrodint the id
        TextView productName=view.findViewById(R.id.productName);
        //get the product quantity textview
        TextView productQuantity=view.findViewById(R.id.productQuantity);
        //get the product quantity textview
        TextView productPrice=view.findViewById(R.id.productPrice);
        //get the product currencysigh textview
        TextView currenySign=view.findViewById(R.id.currencySign);
        //get the product quantity textview
        TextView unitPrice=view.findViewById(R.id.unitPrice);
        TextView unit=view.findViewById(R.id.unit);
        //Get the category name textview
        TextView categoryNameView=view.findViewById(R.id.categoryName);
        //get the iamgeview of the product
        ImageView produtImage=view.findViewById(R.id.productIamge);
        //Populate all the componets with values from product object properties
        productName.setText(product.getName());
        productQuantity.setText(product.getAmount());
        productPrice.setText(product.getPrice());
        currenySign.setText("$");
        unit.setText(product.getUnit());
        unitPrice.setText("/"+product.getUnit());
//        Picasso.with(getContext())
//                .load(product.getImageUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).
//                networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.chicken).
//                into(produtImage);
Picasso.with(getContext()).load(product.getImageUrl()).error(R.drawable.chicken).into(produtImage);
      categoryNameView.setText(categoryName);
         //Get he shared product iamge
      ImageView shareProduct=view.findViewById(R.id.shareProduct);
      //Add click event while user choose to share to media
      shareProduct.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //Declare the share intent
              Intent shareIntent = new Intent();
              //Set the intent type
              shareIntent.setAction(Intent.ACTION_SEND);
              //put the value to intent
              shareIntent.putExtra(Intent.EXTRA_TEXT,"Name: "+product.getName()+"\n"+"Price: $"+product.getPrice()
                      +"  /"+product.getUnit()+
                      "\n"+"Image link:\n"+product.getImageUrl());
              //set the intent style
              shareIntent.setType("text/plain");
             //start activity
              startActivity(Intent.createChooser(shareIntent, "Share product..."));

          }
      });


        return  view;
    }
}
