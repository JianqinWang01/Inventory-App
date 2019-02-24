package com.example.wjqcau.inventory;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjqcau.inventory.JavaBean.ProductCategory;

import java.util.ArrayList;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder> {
    Dialog editCateDialog;
    Dialog updateCateDialog;
    ImageView closeUpdateDialogImage;
    Button updateCateButton;
    EditText newCateTitle;

    ImageView closeImage;
    TextView deletCateChoice;
    TextView updateCateChoice;
    TextView addProductChoice;
    int categoryId;
    //This variable used in the inner event method
    ProductCategory prodCategory;
    FragmentManager fm;
    Context context;
    ArrayList<ProductCategory> prodCategoryList =new ArrayList<>();
    public CategoryProductAdapter(Context context, ArrayList<ProductCategory> prodCategoryList){
       this.context=context;
       this.prodCategoryList=prodCategoryList;
    }

    @NonNull
    @Override
    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_product_list,viewGroup,false);
        CategoryProductViewHolder viewHolder=new CategoryProductViewHolder(view);
           editCateDialog=new Dialog(context);
        editCateDialog.setContentView(R.layout.editcategory);
        editCateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Update category title dialog
        updateCateDialog=new Dialog(context);
        updateCateDialog.setContentView(R.layout.updatecategory);
        updateCateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closeUpdateDialogImage=updateCateDialog.findViewById(R.id.close_update_category);
        updateCateButton=updateCateDialog.findViewById(R.id.update_category_button);
        newCateTitle=updateCateDialog.findViewById(R.id.update_titleInput);



        //Main category dialog
        deletCateChoice=editCateDialog.findViewById(R.id.deleteCategory);
        updateCateChoice=editCateDialog.findViewById(R.id.changeCategoryTitle);
        addProductChoice=editCateDialog.findViewById(R.id.addProduct);
        closeImage=editCateDialog.findViewById(R.id.close_add_category);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCateDialog.dismiss();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryProductViewHolder categoryProductViewHolder, int i) {
     prodCategory=prodCategoryList.get(i);
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

        /*******************************************************************************************
         *  Show the edit dialog
         ********************************************************************************************/
     categoryProductViewHolder.edit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // Toast.makeText(context,"you click me",Toast.LENGTH_SHORT);
             editCateDialog.show();
         }
     });

        /*******************************************************************************************
         *  Add product jump to other add fragment
         ********************************************************************************************/

     addProductChoice.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             //This id will be used in AddProductFragment
             categoryId=prodCategory.getId();
             //Swith to add product fragment
             HomeFragment.transaction=HomeFragment.fm.beginTransaction();
             HomeFragment.transaction.addToBackStack(null);
             HomeFragment.transaction.replace(R.id.content,new AddProductFragment());
             HomeFragment.transaction.commit();
             editCateDialog.dismiss();
         }
     });
        /*******************************************************************************************
         *  Delete Category
         ********************************************************************************************/
        deletCateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          DatabaseHandler db=new DatabaseHandler(context);
          db.deleteCategory(prodCategory);
          db.close();
          editCateDialog.dismiss();
                HomeFragment.transaction=HomeFragment.fm.beginTransaction();
                HomeFragment.transaction.addToBackStack(null);
                HomeFragment.transaction.replace(R.id.content,new HomeFragment());
                HomeFragment.transaction.commit();

            }
        });


        /*******************************************************************************************
         * Show Update  Category Dialogu
         ********************************************************************************************/
        updateCateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             updateCateDialog.show();
             newCateTitle.setText(prodCategory.getTitle());
             editCateDialog.dismiss();
            }
        });
        closeUpdateDialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCateDialog.dismiss();
            }
        });

       updateCateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatabaseHandler db=new DatabaseHandler(context);
               db.updateCategoryTitle(newCateTitle.getText().toString(),prodCategory);
               db.close();
               updateCateDialog.dismiss();
               HomeFragment.transaction=HomeFragment.fm.beginTransaction();
               HomeFragment.transaction.addToBackStack(null);
               HomeFragment.transaction.replace(R.id.content,new HomeFragment());
               HomeFragment.transaction.commit();


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
