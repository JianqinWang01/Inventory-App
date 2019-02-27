package com.example.wjqcau.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.example.wjqcau.inventory.JavaBean.ProductCategory;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    //declare some constants
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="inventory";
    public static final String TABLE_PRODUCT="product";
    public static final String TABLE_CATEGORY="category";

//Product Table column
    public static final String COLUMN_ID="id";

    //Category Table column
    public static final String COLUMN_CATE_TITLE="category_title";

    //Product table column
    public static final String COLUMN_PROD_NAME="product_name";
    public static final String COLUMN_PROD_PRICE="product_price";
    public static final String COLUMN_PROD_IMAGE="product_image";
    public static final String COLUMN_PROD_AMOUNT="product_amount";
    public static final String COLUMN_CATE_ID="prod_cate_id";

    //CREATE  Categrory TABLE
   public static final String CREATE_CATEGORY_TABLE="CREATE TABLE "+TABLE_CATEGORY+"("+
            COLUMN_ID+" INTEGER PRIMARY KEY,"+COLUMN_CATE_TITLE+" TEXT)";

    //create product table

    public static final String CREATE_PRODUCT_TABLE="CREATE TABLE "+TABLE_PRODUCT+"("+
            COLUMN_ID+" INTEGER PRIMARY KEY,"+COLUMN_PROD_NAME+" TEXT,"+
            COLUMN_PROD_PRICE+" TEXT,"+COLUMN_PROD_AMOUNT+" TEXT,"+
            COLUMN_PROD_IMAGE+" INTEGER,"+COLUMN_CATE_ID+
            " INTEGER REFERENCES "+TABLE_CATEGORY+
            "("+COLUMN_ID+") ON DELETE CASCADE)";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(CREATE_CATEGORY_TABLE);
     db.execSQL(CREATE_PRODUCT_TABLE);
    }

    /****************************************************************************************
     *                                   Insert
     *****************************************************************************************/


    /**
     *
     * @param cateTitle add the record of category
     */


//Add table
    public void addCatetory(String cateTitle){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_CATE_TITLE,cateTitle);
        db.insert(TABLE_CATEGORY,null,values);
        db.close();
    }
    public void addProduct(Product product){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
       // values.put(COLUMN_ID,product.getId());
        values.put(COLUMN_PROD_NAME,product.getName());
        values.put(COLUMN_PROD_PRICE,product.getPrice());
        values.put(COLUMN_PROD_AMOUNT,product.getAmount());
        values.put(COLUMN_PROD_IMAGE,product.getProdImage());
        values.put(COLUMN_CATE_ID,product.getCategoryID());
        db.insert(TABLE_PRODUCT,null,values);
        db.close();

    }

    /******************************************************************************************
     *                                  Read
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
       // String queryStr="SELECT * FROM "+TABLE_CATEGORY+" WHERE "+COLUMN_ID+
              //  "=?"+String.valueOf(id);
       Cursor cursor= db.query(TABLE_CATEGORY, new String[]{COLUMN_ID,COLUMN_CATE_TITLE},COLUMN_ID+"=?",
               new String[]{String.valueOf(id)},null,null,null);
       if(cursor!=null){
           cursor.moveToFirst();
           ArrayList<Product> products=new ArrayList<>();
           products=getProuductsInCategory(id);

           productCategory=new ProductCategory(Integer.parseInt(cursor.getString(0)),
                   cursor.getString(1),products);

       }
        db.close();
        return  productCategory;
  }

   public ArrayList<ProductCategory> getAllCategories(){
      ArrayList<ProductCategory> categories=new ArrayList<>();
      SQLiteDatabase db=getReadableDatabase();

      Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_CATEGORY,null);
       ArrayList<Product> products;
      if(cursor.moveToFirst()){

          do{
              products=getProuductsInCategory(Integer.parseInt(cursor.getString(0)));


              categories.add(new ProductCategory(Integer.parseInt(cursor.getString(0)),
                      cursor.getString(1),products));

          }while (cursor.moveToNext());


      }

      db.close();
//       for (ProductCategory category:categories) {
//           if(category.getProductList().size()>0){
//           System.out.println("CateId:"+category.getId()+
//                   " price1"+category.getProductList().get(0).getPrice()+
//                   " price2"+category.getProductList().get(1).getPrice());
//
//
//       }}
      return categories;

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
      COLUMN_PROD_PRICE,COLUMN_PROD_AMOUNT,COLUMN_PROD_IMAGE,COLUMN_CATE_ID},COLUMN_ID+"=?",
              new String[]{String.valueOf(id)},null,null,null);
      if(cursor.moveToFirst()){

          product=new Product(Integer.parseInt(cursor.getString(0)),
                              cursor.getColumnName(1),
                              cursor.getColumnName(2),
                              cursor.getColumnName(3),
                              Integer.parseInt(cursor.getString(4)),
                              Integer.parseInt(cursor.getString(5))

                  );
      }


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
                           COLUMN_PROD_AMOUNT,COLUMN_PROD_IMAGE,COLUMN_CATE_ID},COLUMN_CATE_ID+"=?",
                new String[]{String.valueOf(categoryId)},null,null,null);
        if(cursor.moveToFirst()){

            do{
                products.add(new Product(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5))));


            }while (cursor.moveToNext());

        }

        db.close();


      return products;
    }

    /***************************************************************************************
     *                            Update
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

  public void updateProduct(String name,String price,String amount,int imageResource,Product product){
      SQLiteDatabase db=this.getWritableDatabase();
      ContentValues values=new ContentValues();
      if((name!=null)&&(name!=" ")&&(name!=""))
      {values.put(COLUMN_PROD_NAME,name);}
      if((price!=null)&&(price!=" ")&&(price!=" "))
      {values.put(COLUMN_PROD_PRICE,price);}
      if((amount!=null)&&(amount!="")&&(amount!=" "))
      {values.put(COLUMN_PROD_AMOUNT,amount);}
      if(imageResource!=0)
      {values.put(COLUMN_PROD_IMAGE,imageResource);}
      db.update(TABLE_PRODUCT,values,COLUMN_ID+"=?",
              new String[]{String.valueOf(product.getId())});
      db.close();
  }

    /**************************************************************************************************
     *                               DELETE
     *************************************************************************************************/

     public void deleteProduct(Product product){
         SQLiteDatabase db=this.getWritableDatabase();
         db.delete(TABLE_PRODUCT,COLUMN_ID+"=?",new String[]{String.valueOf(product.getId())});
         db.close();
     }

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
