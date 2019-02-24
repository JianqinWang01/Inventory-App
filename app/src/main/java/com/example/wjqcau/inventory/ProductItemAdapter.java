package com.example.wjqcau.inventory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjqcau.inventory.JavaBean.Product;

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
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_card,viewGroup,false);
       ProductItemViewHolder viewHolder=new ProductItemViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder productItemViewHolder, int i) {

      Product product=prodList.get(i);
      productItemViewHolder.prodName.setText(product.getName());
      productItemViewHolder.prodPrice.setText(product.getPrice());
      productItemViewHolder.prodImage.setImageResource(product.getProdImage());
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
       //ImageButton prodEditBut;

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.productName);
            prodImage=itemView.findViewById(R.id.productIamge);
            prodPrice=itemView.findViewById(R.id.productPrice);
            prodAmount=itemView.findViewById(R.id.productQuantity);
           // prodEditBut =itemView.findViewById(R.id.prodEditbut);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(),"You make me painful!",Toast.LENGTH_SHORT).show();

                }
            });

        }



    }
}
