package com.example.wjqcau.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.example.wjqcau.inventory.JavaBean.ProductCategory;
import com.example.wjqcau.inventory.JavaBean.SearchResult;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author wjqcau
 * date_created:20190301
 * This class mainly define all the database and it's operation
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //declare some constants
    public static final int DATABASE_VERSION=1;
    //Database Name
    public static final String DATABASE_NAME="inventory";
    //Declare the table name
    public static final String TABLE_PRODUCT="product";
    //Declare the table name
    public static final String TABLE_CATEGORY="category";

   //Product Table column id which reused for each table
    public static final String COLUMN_ID="id";

    //Category Table column
    public static final String COLUMN_CATE_TITLE="category_title";

    //Product table column for product table
    public static final String COLUMN_PROD_NAME="product_name";
    public static final String COLUMN_PROD_PRICE="product_price";
    public static final String COLUMN_PROD_IMAGE_URL="product_image_url";
    public static final String COLUMN_PROD_AMOUNT="product_amount";
    public static final String COLUMN_PROD_UNIT="product_unit";
    //This colum is the foreign key
    public static final String COLUMN_CATE_ID="prod_cate_id";


    //CREATE  Categrory TABLE
   public static final String CREATE_CATEGORY_TABLE="CREATE TABLE "+TABLE_CATEGORY+"("+
            COLUMN_ID+" INTEGER PRIMARY KEY,"+COLUMN_CATE_TITLE+" TEXT)";

    //create product table

    public static final String CREATE_PRODUCT_TABLE="CREATE TABLE "+TABLE_PRODUCT+"("+
            COLUMN_ID+" INTEGER PRIMARY KEY,"+COLUMN_PROD_NAME+" TEXT,"+
            COLUMN_PROD_PRICE+" TEXT,"+COLUMN_PROD_AMOUNT+" TEXT,"+
            COLUMN_PROD_IMAGE_URL+" TEXT,"+COLUMN_PROD_UNIT+" TEXT,"+COLUMN_CATE_ID+
            " INTEGER REFERENCES "+TABLE_CATEGORY+
            "("+COLUMN_ID+") ON DELETE CASCADE)";

    //Define the constructor to populate the database and version
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create the category table
     db.execSQL(CREATE_CATEGORY_TABLE);
     //create teh product table
     db.execSQL(CREATE_PRODUCT_TABLE);
    }

    /****************************************************************************************
     *                                   Insert operation
     *****************************************************************************************/

    /**
     *
     * @param cateTitle add the record of category
     */


    /**
     * Define the Add table
     * @param cateTitle: category name
     */

    public void addCatetory(String cateTitle){
        SQLiteDatabase db=this.getWritableDatabase();
        //Declare the ContentValue object
        ContentValues values=new ContentValues();
        //push the value to the contentvalue object
        values.put(COLUMN_CATE_TITLE,cateTitle);
        //insert into the table
        db.insert(TABLE_CATEGORY,null,values);
        db.close();
    }
    public void addProduct(Product product){
        SQLiteDatabase db=getWritableDatabase();
        //Declare the ContentValue object
        ContentValues values=new ContentValues();
       // values.put(COLUMN_ID,product.getId());
        values.put(COLUMN_PROD_NAME,product.getName());
        //put all the values to corresponding columns in the product table
        values.put(COLUMN_PROD_PRICE,product.getPrice());
        values.put(COLUMN_PROD_AMOUNT,product.getAmount());
        values.put(COLUMN_PROD_IMAGE_URL,product.getImageUrl());
        values.put(COLUMN_PROD_UNIT,product.getUnit());
        values.put(COLUMN_CATE_ID,product.getCategoryID());
        db.insert(TABLE_PRODUCT,null,values);
        db.close();

    }

    /******************************************************************************************
     *                                  Read Operation
     *****************************************************************************************/


    /**
     *
     * @param id  Id of the category
     * @return Category Object
     */
//Grab the ArrayList category
//need to improve
  public ProductCategory getCategory(int id){
        ProductCategory productCategory=null;
        SQLiteDatabase db=this.getReadableDatabase();
       //Defien the cursor to point the position
        // String queryStr="SELECT * FROM "+TABLE_CATEGORY+" WHERE "+COLUMN_ID+
              //  "=?"+String.valueOf(id);
       Cursor cursor= db.query(TABLE_CATEGORY, new String[]{COLUMN_ID,COLUMN_CATE_TITLE},COLUMN_ID+"=?",
               new String[]{String.valueOf(id)},null,null,null);
       if(cursor!=null){
           //move to the first position
           cursor.moveToFirst();
           //Declare the product arraylist
           ArrayList<Product> products=new ArrayList<>();
           //get the product arraylist according cateogry id in the database
           products=getProuductsInCategory(id);
           //populate the category object
           productCategory=new ProductCategory(Integer.parseInt(cursor.getString(0)),
                   cursor.getString(1),products);

       }
        db.close();
        return  productCategory;
  }
  //Grab all the categories in the category table
   public ArrayList<ProductCategory> getAllCategories(){
      //Define the category arraylist to hold all the categories
      ArrayList<ProductCategory> categories=new ArrayList<>();
      SQLiteDatabase db=getReadableDatabase();
      //Populate the cursor to poit to the records
      Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_CATEGORY,null);
       ArrayList<Product> products;
      if(cursor.moveToFirst()){
          do{
              //Get the products arraylist in the category
              products=getProuductsInCategory(Integer.parseInt(cursor.getString(0)));
              //Populate the category arraylist
              categories.add(new ProductCategory(Integer.parseInt(cursor.getString(0)),
                      cursor.getString(1),products));

          }while (cursor.moveToNext());

      }
      db.close();
      return categories;

   }
   //Read the category name in the category table according the category id
    public String getCategoryName(int categoryId){
     String categoryName="";
     SQLiteDatabase db=getReadableDatabase();
     //populate the cursor to point the records
      Cursor cursor=db.query(TABLE_CATEGORY,new String[]{COLUMN_CATE_TITLE},COLUMN_ID+"=?",
              new String[]{String.valueOf(categoryId)},null,null,null);
      if(cursor.moveToFirst()){
          //set the category name
          categoryName=cursor.getString(0);
      }

     db.close();
      //return the category name
      return categoryName;
    }

    //Get the Search Result arraylist in the product table
    public ArrayList<SearchResult> getProductSearchResult(){
      ArrayList<SearchResult> produtSearchLists=new ArrayList<>();
      SQLiteDatabase db=getReadableDatabase();
      Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_PRODUCT,null);
      if(cursor.moveToFirst()){
          do{
              int productId=Integer.parseInt(cursor.getString(0));
              String productName=cursor.getString(1);
              String productPrice=cursor.getString(2);
              int cateId= Integer.parseInt(cursor.getString(6));
              String categoryName=getCategoryName(cateId);
              produtSearchLists.add(new SearchResult(productId,productName,productPrice,categoryName));

          }while(cursor.moveToNext()); }

      db.close();
      return produtSearchLists;
    }
    //Get the product name from the table
   public ArrayList<String> getAllProductNames(){
      ArrayList<String> names=new ArrayList<>();
      SQLiteDatabase db=getReadableDatabase();
      Cursor cursor=db.rawQuery("SELECT "+COLUMN_PROD_NAME+" FROM "+TABLE_PRODUCT,null);
      if(cursor.moveToFirst()){
          do{

             names.add(cursor.getString(0));

          }while(cursor.moveToNext());
      }

      db.close();
      return names;
   }

    /**
     *  @return all the products in the arraylist
     */
   public ArrayList<Product> getAllProducts(){
       //Define the arraylist to hold all the products
       ArrayList<Product> products=new ArrayList<>();
       SQLiteDatabase db=getReadableDatabase();
       //Populate the cursor to point the records position
       Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_PRODUCT,null);
       if(cursor.moveToFirst()){
           do{
              //Loop the grab all the columns in the table product and set to each item in the array
               //list
               products.add(new Product(Integer.parseInt(cursor.getString(0)),
                       cursor.getString(1),
                       cursor.getString(2),
                       cursor.getString(3),
                       cursor.getString(4),
                       cursor.getString(5),
                       Integer.parseInt(cursor.getString(6)))
               );

           }while(cursor.moveToNext());
       }

       db.close();
      return  products;
   }



    /**
     * @description Grab the product according its id
     * @param id  product id
     * @return  the product object
     */
  public Product getProduct(int id){
      Product product=null;
      SQLiteDatabase db=getReadableDatabase();
      Cursor cursor=db.query(TABLE_PRODUCT,new String[]{COLUMN_ID,COLUMN_PROD_NAME,
      COLUMN_PROD_PRICE,COLUMN_PROD_AMOUNT,COLUMN_PROD_IMAGE_URL,COLUMN_PROD_UNIT,COLUMN_CATE_ID},COLUMN_ID+"=?",
              new String[]{String.valueOf(id)},null,null,null);
      if(cursor.moveToFirst()){
          //Populate the the product object with the value get from produt comlumns
          product=new Product(Integer.parseInt(cursor.getString(0)),
                              cursor.getString(1),
                              cursor.getString(2),
                              cursor.getString(3),
                              cursor.getString(4),
                              cursor.getString(5),
                              Integer.parseInt(cursor.getString(6))

                  );  }

       //close the dababase connection instance
      db.close();
      return product;
      }

    /**
     * @depreption get all products in category
     * @param categoryId  id of category
     * @return  array of products
     */

    public ArrayList<Product> getProuductsInCategory(int categoryId){
        ArrayList<Product> products=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_PRODUCT,new String[]{COLUMN_ID,COLUMN_PROD_NAME,COLUMN_PROD_PRICE,
                           COLUMN_PROD_AMOUNT,COLUMN_PROD_IMAGE_URL,COLUMN_PROD_UNIT,COLUMN_CATE_ID},COLUMN_CATE_ID+"=?",
                new String[]{String.valueOf(categoryId)},null,null,null);
        if(cursor.moveToFirst()){

            do{
                //Loop the grab all the products in the category,
                //populate the product arraylist
                products.add(new Product(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        Integer.parseInt(cursor.getString(6))));


            }while (cursor.moveToNext());
        }
         //close the database connection instance
        db.close();
      return products;
    }

    //Grab the max id in product
    public int getProductMaxId(){
        int id=0;
        SQLiteDatabase db=getReadableDatabase();
        //Order will get the list of the id with descendent order,
        //So we get the first one which is the largest one
        String query="SELECT id FROM "+TABLE_PRODUCT+" ORDER BY id DESC LIMIT 1";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            //Assign the id to variable
            id=Integer.parseInt(cursor.getString(0));
        }
        db.close();
     return id;
    }



    /***************************************************************************************
     *                            Update database
     *******************************************************************************************/
    /**
     *
     * @param newTitle The new value will be updated
     * @param CategoryId  the category object will be updated
     */
  public void updateCategoryTitle(String newTitle,int CategoryId){
        SQLiteDatabase db=getWritableDatabase();
       // String upDate="UPDATE "+TABLE_CATEGORY+" SET "+COLUMN_CATE_TITLE+"="+newTitle;
       ContentValues values=new ContentValues();
       values.put(COLUMN_CATE_TITLE,newTitle);
       db.update(TABLE_CATEGORY,values,COLUMN_ID+"=?",
               new String[]{String.valueOf(CategoryId)});

        db.close();

  }
//do not update image url while update image
    //update Product in the product table
  public void updateProduct(String name,String price,String amount,String unit,int productId){
      SQLiteDatabase db=this.getWritableDatabase();
      ContentValues values=new ContentValues();
      //PUt the new values to the corresponding columns
      values.put(COLUMN_PROD_NAME,name);
      values.put(COLUMN_PROD_PRICE,price);
      values.put(COLUMN_PROD_AMOUNT,amount);
      values.put(COLUMN_PROD_UNIT,unit);
     //Execute the update operation
      db.update(TABLE_PRODUCT,values,COLUMN_ID+"=?",
              new String[]{String.valueOf(productId)});
      db.close();
  }

    /**************************************************************************************************
     *                               DELETE Operation
     *************************************************************************************************/
    /**
     *
     * @param product : the object will deleted in the table product
     */
     public void deleteProduct(Product product){
         SQLiteDatabase db=this.getWritableDatabase();
         db.delete(TABLE_PRODUCT,COLUMN_ID+"=?",new String[]{String.valueOf(product.getId())});
         db.close();
     }
//delete all the products in a category
     public void deleteProductInCategory(int cateId){
         SQLiteDatabase db=this.getWritableDatabase();
         db.delete(TABLE_PRODUCT,COLUMN_CATE_ID+"=?",new String[]{String.valueOf(cateId)});
         db.close();
     }

    /**
     * @description used to cascade delete category and products under it
     * @param category  category object to be deleted
     */
    public void deleteCategory(ProductCategory category){
       SQLiteDatabase db=getWritableDatabase();
      // deleteProductInCategory(category.getId());
       db.delete(TABLE_CATEGORY,COLUMN_ID+"=?",new String[]{String.valueOf(category.getId())});
       db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
//This method mainly used for cascade delete records of products
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
}
