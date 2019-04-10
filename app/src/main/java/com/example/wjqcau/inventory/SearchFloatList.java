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

@SuppressLint("ValidFragment")
public class SearchFloatList extends BottomSheetDialogFragment {
   private int productId;
   private String categoryName;
    public SearchFloatList(int productId,String categoryName){
      this.productId=productId;
      this.categoryName=categoryName;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);

        View view=inflater.inflate(R.layout.searchselectlistview,container,false);
        DatabaseHandler db=new DatabaseHandler(getContext());
       final Product product=db.getProduct(productId);
        db.close();
        TextView productName=view.findViewById(R.id.productName);
        TextView productQuantity=view.findViewById(R.id.productQuantity);
        TextView productPrice=view.findViewById(R.id.productPrice);
        TextView currenySign=view.findViewById(R.id.currencySign);
        TextView unitPrice=view.findViewById(R.id.unitPrice);
        TextView unit=view.findViewById(R.id.unit);
        TextView categoryNameView=view.findViewById(R.id.categoryName);
        ImageView produtImage=view.findViewById(R.id.productIamge);

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

      ImageView shareProduct=view.findViewById(R.id.shareProduct);
      shareProduct.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent shareIntent = new Intent();
              shareIntent.setAction(Intent.ACTION_SEND);
              shareIntent.putExtra(Intent.EXTRA_TEXT,"Name: "+product.getName()+"\n"+"Price: $"+product.getPrice()
                      +"  /"+product.getUnit()+
                      "\n"+"Image link:\n"+product.getImageUrl());

              shareIntent.setType("text/plain");

              startActivity(Intent.createChooser(shareIntent, "Share product..."));

          }
      });




      //  Log.d("PassValue:",categoryName);
        return  view;
    }
}
