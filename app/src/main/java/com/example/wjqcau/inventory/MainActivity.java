package com.example.wjqcau.inventory;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        AddProductFragment.OnFragmentInteractionListener,
       UpdateProductFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        PriceReferFragment.OnFragmentInteractionListener,
         StockWarningFragment.OnFragmentInteractionListener{
    FragmentManager fm;
//    public static ActionBar actionBar;
   public static AddProductFragment addProductFragment;
  // public static SearchFragment searchFragment;
    //Declare a globe for add category
   public static ImageView addCategoryImage;
   //Declare a global variable for search product
  //  public static ImageView searchProductImage;
    //declare the fabbutton with public static
   public static FloatingActionButton fab;
  // public static ImageView addCategoryButton;
 // MaterialSearchView searchView;
    private ImageView searchProductImage;
    public static ActionBar  actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Toolbar  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fm=getSupportFragmentManager();
         final FragmentTransaction transaction=fm.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.content,new HomeFragment());
        transaction.commit();
        //Delete the app name from header
          actionBar= getSupportActionBar();
        actionBar.setTitle("");
        addCategoryImage=(ImageView)toolbar.findViewById(R.id.addCategoryImage);
        searchProductImage=(ImageView)toolbar.findViewById(R.id.searchProductImage);
      addProductFragment=new AddProductFragment();
    //  searchFragment=new SearchFragment();

       // searchView = (MaterialSearchView) findViewById(R.id.search_view);
     searchProductImage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             FragmentTransaction transaction1=fm.beginTransaction();
             transaction1.replace(R.id.content,new SearchFragment());
             transaction1.addToBackStack(null);
             transaction1.commit();
         }
     });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // mask the setting menu here
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
//         if(id==R.id.action_search){
//
//             FragmentTransaction transaction=fm.beginTransaction();
//               transaction.replace(R.id.content,new SearchFragment());
//               transaction.addToBackStack(null);
//               transaction.commit();
//         }
//
//       Log.d("ItemChoice","go there");
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction=fm.beginTransaction();
        if (id == R.id.nav_home) {
            // Handle the camera action
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new HomeFragment());


        } else if (id == R.id.nav_contact) {


        } else if (id == R.id.nav_credit) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_priceRef) {
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new PriceReferFragment());

        } else if (id == R.id.nav_warning) {

            // Handle the camera action
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new StockWarningFragment());
        }

        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 666:
                if(grantResults.length>0&&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    if(addProductFragment!=null){
                        Log.d("ExecuteNew:","goodMan");
                        addProductFragment.ExecuteImageUpload();}
                }else{

                }
                break;




        }
    }
}