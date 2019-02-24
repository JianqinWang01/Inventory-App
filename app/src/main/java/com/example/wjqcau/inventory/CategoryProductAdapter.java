package com.example.wjqcau.inventory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjqcau.inventory.JavaBean.ProductCategory;

import java.util.ArrayList;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder> {

    Context context;
    ArrayList<ProductCategory> prodCategoryList =new ArrayList<>();
    public CategoryProductAdapter(Context context,ArrayList<ProductCategory> prodCategoryList){
       this.context=context;
       this.prodCategoryList=prodCategoryList;
    }

    @NonNull
    @Override
    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_product_list,viewGroup,false);
        CategoryProductViewHolder viewHolder=new CategoryProductViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryProductViewHolder categoryProductViewHolder, int i) {
     ProductCategory prodCategory=prodCategoryList.get(i);
     ArrayList productList=prodCategory.getProductList();
     categoryProductViewHolder.cateTitle.setText(prodCategory.getTitle());
     ProductItemAdapter adapter= new ProductItemAdapter(context,productList);

     categoryProductViewHolder.recyclerView.setHasFixedSize(true);
     categoryProductViewHolder.recyclerView.setAdapter(adapter);
     categoryProductViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,
             false));


     //Set the shevel
      categoryProductViewHolder.chevel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //categoryProductViewHolder.recyclerView.removeItemDecorationAt(
                //      categoryProductViewHolder.getAdapterPosition());
              notifyItemRemoved(categoryProductViewHolder.getAdapterPosition());
          }
      });

     categoryProductViewHolder.edit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Toast.makeText(context,"you click me",Toast.LENGTH_SHORT);
         }
     });




    }

    @Override
    public int getItemCount() {
        return (null!=prodCategoryList?prodCategoryList.size():0);
    }

    class CategoryProductViewHolder extends RecyclerView.ViewHolder{

        TextView cateTitle;
        ImageView chevel;
        ImageView edit;
        RecyclerView recyclerView;

        public CategoryProductViewHolder(@NonNull View itemView) {
            super(itemView);
            cateTitle=itemView.findViewById(R.id.categoryTitle);
            chevel=itemView.findViewById(R.id.category_chevel);
            edit=itemView.findViewById(R.id.category_edit);
            recyclerView=itemView.findViewById(R.id.category_product_RecycleView);

        }
    }
}
