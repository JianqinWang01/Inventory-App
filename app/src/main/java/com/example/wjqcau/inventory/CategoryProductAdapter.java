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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjqcau.inventory.JavaBean.ProductCategory;

import java.util.ArrayList;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder> {
    private static int productRecyclerViewHeight;
    private static int countIndex=0;
    public static int categoryId=-100;

    FragmentManager fm;
    Context context;
    ArrayList<ProductCategory> prodCategoryList =new ArrayList<>();


    public CategoryProductAdapter(Context context, ArrayList<ProductCategory> prodCategoryList){
       this.context=context;
       this.prodCategoryList=prodCategoryList;
    }

    @NonNull
    @Override
    public CategoryProductAdapter.CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_product_list,viewGroup,false);
        final CategoryProductViewHolder viewHolder=new CategoryProductViewHolder(view);

  /*************************************************************************************************
  *  +++++++++++++++++++++++++ Edit dialog and its's event+++++++++++++++++++++++++++++++++++++
  * *************************************************************************************************/

      final   Dialog  editCateDialog=new Dialog(context);
        editCateDialog.setContentView(R.layout.editcategory);
        editCateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      ImageView  closeImage=editCateDialog.findViewById(R.id.close_add_category);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCateDialog.dismiss();
            }
        });


        //Main category dialog
      final TextView  deletCateChoice=editCateDialog.findViewById(R.id.deleteCategory);
      TextView  updateCateChoice=editCateDialog.findViewById(R.id.changeCategoryTitle);
      TextView  addProductChoice=editCateDialog.findViewById(R.id.addProduct);

        /*******************************************************************************************
         *  Show the edit dialog
         ********************************************************************************************/
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context,"you click me",Toast.LENGTH_SHORT);
                editCateDialog.show();
            }
        });




        /*******************************************************************************************
         * (1)In EditDialog Add product jump to other add fragment
         ********************************************************************************************/

        addProductChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                //This id will be used in AddProductFragment
               categoryId=prodCategoryList.get(viewHolder.getAdapterPosition()).getId();
                //Swith to add product fragment
                HomeFragment.transaction=HomeFragment.fm.beginTransaction();
                HomeFragment.transaction.addToBackStack(null);

                HomeFragment.transaction.replace(R.id.content,MainActivity.addProductFragment);
                HomeFragment.transaction.commit();
                editCateDialog.dismiss();
            }
        });
        /*******************************************************************************************
         * (2)In  Delete Category
         ********************************************************************************************/
       //Reuse delete product dialog layout
        final Dialog  deleteProdDialog=new Dialog(context);
        deleteProdDialog.setContentView(R.layout.deleteconfirmlayout);
        deleteProdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView deleteConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductYes);
        TextView cancelConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductNo);



        deletCateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteProdDialog.show();
               editCateDialog.dismiss();


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
                db.deleteCategory(prodCategoryList.get(viewHolder.getAdapterPosition()));
                db.close();
                deleteProdDialog.dismiss();
                //need to fix
              // HomeFragment.fm.popBackStack();
                HomeFragment.transaction=HomeFragment.fm.beginTransaction();
                HomeFragment.transaction.addToBackStack(null);
                HomeFragment.transaction.replace(R.id.content,new HomeFragment());
                HomeFragment.transaction.commit();
            }
        });

         //Define update dialog then, show update when click on update choice
          final Dialog  updateCateDialog=new Dialog(context);
          updateCateDialog.setContentView(R.layout.updatecategory);
           updateCateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          ImageView closeUpdateDialogImage=updateCateDialog.findViewById(R.id.close_update_category);
          Button  updateCateButton=updateCateDialog.findViewById(R.id.update_category_button);
          final  EditText newCateTitle=updateCateDialog.findViewById(R.id.update_titleInput);

        /*******************************************************************************************
         * (3) In EditDialog Show Update  Category Dialog
         ********************************************************************************************/
        updateCateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCateDialog.show();
                newCateTitle.setText(prodCategoryList.get(viewHolder.getAdapterPosition()).getTitle());
                editCateDialog.dismiss();
            }
        });




        /*************************************************************************************************
         *  ++++++++++++++After (3), The UpdateCateDialog shows, this dialog and its's event+++++++++
         * *************************************************************************************************/
        //Update category title dialog


        closeUpdateDialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { updateCateDialog.dismiss(); }
        });


        updateCateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CurrentCateId"+prodCategoryList.get(viewHolder.getAdapterPosition()).getId());
                DatabaseHandler db=new DatabaseHandler(context);
                db.updateCategoryTitle(newCateTitle.getText().toString(),prodCategoryList.get(viewHolder.getAdapterPosition()).getId());

                db.close();
                updateCateDialog.dismiss();
                HomeFragment.transaction=HomeFragment.fm.beginTransaction();
                HomeFragment.transaction.addToBackStack(null);
                HomeFragment.transaction.replace(R.id.content,new HomeFragment());
                HomeFragment.transaction.commit();


            }
        });

        //Set the shevel
        viewHolder.chevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(countIndex==0){
                 productRecyclerViewHeight=viewHolder.recyclerView.getHeight();
                 countIndex++;
             }
           //  System.out.println("Heightnew:"+viewHolder.recyclerView.getHeight());


                if(viewHolder.recyclerView.getLayoutParams().height>50){
                    viewHolder.recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 0));
                    notifyItemChanged(viewHolder.getAdapterPosition());
                }else{
                    viewHolder.recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, productRecyclerViewHeight));
                    notifyItemChanged(viewHolder.getAdapterPosition());
                }


            }
        });





        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryProductViewHolder categoryProductViewHolder, int i) {
     ProductCategory  prodCategory=prodCategoryList.get(i);
     ArrayList productList=prodCategory.getProductList();
     categoryProductViewHolder.cateTitle.setText(prodCategory.getTitle());

     ProductItemAdapter adapter= new ProductItemAdapter(context,productList);

    // categoryProductViewHolder.recyclerView.setHasFixedSize(true);
     categoryProductViewHolder.recyclerView.setAdapter(adapter);
     categoryProductViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,
             false));


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
