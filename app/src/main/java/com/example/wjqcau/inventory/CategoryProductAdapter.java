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

/**
 * @author wjqcau
 * This class mainly used in Home Screen which control the Category items.
 * The Category will be displayed in the style of recycleview. It also nest the productsAdapter  in it.
 * All the Nested RecyclerView are controlled in this class.
 *
 */

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder> {
   //declare the constant of product view's hight
    private static int productRecyclerViewHeight;
    //declare the variable to decide the #number of the click the chevel iamge
    private static int countIndex=0;
    //Declare the varialbe to judage the whether database search result is correct
    public static int categoryId=-100;
    //Declare the fragmentmanager
    FragmentManager fm;
    //Declare the context variable and ProductCategory Array
    Context context;
    ArrayList<ProductCategory> prodCategoryList =new ArrayList<>();

    /**
     * Contructor: Populate the context and productCategory arraylist
     * @param context Context object which is passed from the HomeScreen
     * @param prodCategoryList ProductCategory objects arraylist which is passed from HomeScreen
     */
    public CategoryProductAdapter(Context context, ArrayList<ProductCategory> prodCategoryList){
       //Populate the context and arraylist
        this.context=context;
       this.prodCategoryList=prodCategoryList;
    }

    @NonNull
    @Override
    //Create the viewholder object which is defined as the inner class
    public CategoryProductAdapter.CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Get the view from the layout:category_product_list
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_product_list,viewGroup,false);
       //Declare and the viewholder object
        final CategoryProductViewHolder viewHolder=new CategoryProductViewHolder(view);

  /*************************************************************************************************
  *  +++++++++++++++++++++++++ Edit dialog and its's event+++++++++++++++++++++++++++++++++++++
  * *************************************************************************************************/
     //Define the dialogue which is used for edit and fix the category title
      final   Dialog  editCateDialog=new Dialog(context);
        editCateDialog.setContentView(R.layout.editcategory);
        editCateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      //top right X image which will close the dialogue
        ImageView  closeImage=editCateDialog.findViewById(R.id.close_add_category);
       //When user click the "X" image, the dialogue will close
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCateDialog.dismiss();
            }
        });


        //deleteCategory textview which is in the pop up editdialogue
      final TextView  deletCateChoice=editCateDialog.findViewById(R.id.deleteCategory);
      //updateCategory textview which is in the pop up editdialogue
      TextView  updateCateChoice=editCateDialog.findViewById(R.id.changeCategoryTitle);
      //Get addproduct textview which is in the Pop up eiditdialogue
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
         * (1)In EditDialog Add product jump to other AddFragment
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
               //Dismiss the eiditDialogue after jump to the AddFragment
                editCateDialog.dismiss();
            }
        });
        /*******************************************************************************************
         * (2)In  Delete Category
         ********************************************************************************************/
       //Reuse delete product dialog layout
        //This is cumstomerized dialogue
        final Dialog  deleteProdDialog=new Dialog(context);
        //This dialogue layout from the layout:deleteconfirmlayout.xml
        deleteProdDialog.setContentView(R.layout.deleteconfirmlayout);
        deleteProdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView deleteConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductYes);
        TextView cancelConfirm=(TextView)deleteProdDialog.findViewById(R.id.deleteProductNo);
       //add event for deleteChoice textview when user click and choose the delete textview
        deletCateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show the deleteprodDialogue
               deleteProdDialog.show();
               //dismiss the editCategory
               editCateDialog.dismiss();
            }
        });
        //When user click the cancel button, the delete dialogue will dismiss
        cancelConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteProdDialog.dismiss();
            }
        });
        //When user confirm to delete the category

        deleteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Step 1:delete the category from database
                DatabaseHandler db=new DatabaseHandler(context);
                db.deleteCategory(prodCategoryList.get(viewHolder.getAdapterPosition()));
                db.close();
                deleteProdDialog.dismiss();
                //need to fix
              // HomeFragment.fm.popBackStack();

                //Step 2: load HomeScreen again to refresh the category and product again
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
         //Define the closeimage to dismiss the dialogue
          ImageView closeUpdateDialogImage=updateCateDialog.findViewById(R.id.close_update_category);
          //Declare the button and populate for the view
          Button  updateCateButton=updateCateDialog.findViewById(R.id.update_category_button);
          //Declare the edittext which user input the new category name
          final  EditText newCateTitle=updateCateDialog.findViewById(R.id.update_titleInput);

        /*******************************************************************************************
         * (3) In EditDialog Show Update  Category Dialog
         ********************************************************************************************/
        updateCateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCateDialog.show();
                //Set the the user input category name
                newCateTitle.setText(prodCategoryList.get(viewHolder.getAdapterPosition()).getTitle());
               //dismiss the edit category dialogue
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

        //Add event for button in the update category dialogue
        updateCateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update the the category name in the database
                DatabaseHandler db=new DatabaseHandler(context);
                db.updateCategoryTitle(newCateTitle.getText().toString(),prodCategoryList.get(viewHolder.getAdapterPosition()).getId());
                db.close();
                //dismiss the dialogue
                updateCateDialog.dismiss();
                //Fresh the HomeScreen
                HomeFragment.transaction=HomeFragment.fm.beginTransaction();
                HomeFragment.transaction.addToBackStack(null);
                HomeFragment.transaction.replace(R.id.content,new HomeFragment());
                HomeFragment.transaction.commit();


            }
        });

        //Set the shevel when user click the shevel icon
        viewHolder.chevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When user click the cheverl ,the count index will increasement by 1
                if(countIndex==0){
                    //Get the current height of view holder view
                 productRecyclerViewHeight=viewHolder.recyclerView.getHeight();
                 countIndex++;
             }
                //If the height is more than 50, it will be set to hidden
                if(viewHolder.recyclerView.getLayoutParams().height>50){
                    viewHolder.recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 0));
                   //Refresh the view of the category
                    notifyItemChanged(viewHolder.getAdapterPosition());
                }else{
                    //If is not more than 50, set to the origin height which is got above
                    viewHolder.recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, productRecyclerViewHeight));
                   //refresh the frame
                    notifyItemChanged(viewHolder.getAdapterPosition());
                }


            }
        });


        return viewHolder;
    }
   //Binding all the component in the category with value
    @Override
    public void onBindViewHolder(@NonNull final CategoryProductViewHolder categoryProductViewHolder, int i) {
        //Get the productCategory object
        ProductCategory  prodCategory=prodCategoryList.get(i);
        //Get he product object arraylist from the category object
     ArrayList productList=prodCategory.getProductList();
     //Set the categorytile to the category title textview
     categoryProductViewHolder.cateTitle.setText(prodCategory.getTitle());
        /**
         * Define the nested adapter which is used show and manage the product recycleview
         * which is scrollable horizontally
         * The nested recyclerview is also in the viewholder of category
         */

     ProductItemAdapter adapter= new ProductItemAdapter(context,productList);
     //Define the recyclerview has fixed size
     categoryProductViewHolder.recyclerView.setHasFixedSize(true);
     //set the adapter to the nested product recyclyerview
     categoryProductViewHolder.recyclerView.setAdapter(adapter);
     //Set the recyclerview's layout with horizontally scrollable feature
     categoryProductViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,
             false));
    }

    @Override
    public int getItemCount() {
        return (null!=prodCategoryList?prodCategoryList.size():0);
    }

    /**
     * Define the inner class which is category view holder
     */
    class CategoryProductViewHolder extends RecyclerView.ViewHolder{
       //declare the component in the viewholder
        TextView cateTitle;
        ImageView chevel;
        ImageView edit;
        //This recyclerview object is the container to hold the product recylerview under the category
        RecyclerView recyclerView;
        //Constructor to populate the properties
        public CategoryProductViewHolder(@NonNull View itemView) {
            super(itemView);
            //populate all the properties in the viewholder
            cateTitle=itemView.findViewById(R.id.categoryTitle);
            chevel=itemView.findViewById(R.id.category_chevel);
            edit=itemView.findViewById(R.id.category_edit);
            recyclerView=itemView.findViewById(R.id.category_product_RecycleView);

        }
    }
}
