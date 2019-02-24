package com.example.wjqcau.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wjqcau.inventory.JavaBean.Product;
import com.example.wjqcau.inventory.JavaBean.ProductCategory;


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
    public static final String COLUMN_PROD_IMAME="product_image";
    public static final String COLUMN_CATE_ID="prod_cate_id";

    //CREATE  Categrory TABLE
   public static final String CREATE_CATEGORY_TABLE="CREATE TABLE "+TABLE_CATEGORY+"("+
            COLUMN_ID+" INTEGER PRIMARY KEY,"+COLUMN_CATE_TITLE+" TEXT)";

    //create product table

    public static final String CREATE_PRODUCT_TABLE="CREATE TABLE "+TABLE_PRODUCT+"("+
            COLUMN_ID+" INTEGER PRIMARY KEY,"+COLUMN_PROD_NAME+" TEXT,"+
            COLUMN_PROD_PRICE+" TEXT,"+COLUMN_PROD_IMAME+" INTEGER,"+COLUMN_CATE_ID+
            " INTEGER REFERENCES "+TABLE_CATEGORY+
            "("+COLUMN_ID+"))";




    public DatabaseHandler( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(CREATE_CATEGORY_TABLE);
    }
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
        values.put(COLUMN_ID,product.getId());
        values.put(COLUMN_PROD_NAME,product.getName());
        values.put(COLUMN_PROD_PRICE,product.getPrice());
        values.put(COLUMN_PROD_IMAME,product.getProdImage());
        values.put(COLUMN_CATE_ID,product.getCategoryID());
        db.insert(TABLE_PRODUCT,null,values);
        db.close();

    }
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
           productCategory=new ProductCategory(Integer.parseInt(cursor.getString(0)),
                   cursor.getString(1),null);

       }


        return  productCategory;
  }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
