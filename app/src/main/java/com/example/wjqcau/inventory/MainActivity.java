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

/**
 * @author wjqcau
 * The application entry
 * Which control the whole framework of the app
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        AddProductFragment.OnFragmentInteractionListener,
       UpdateProductFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        PriceReferFragment.OnFragmentInteractionListener,
         StockWarningFragment.OnFragmentInteractionListener,
         ContactFragment.OnFragmentInteractionListener,
         CreditFragment.OnFragmentInteractionListener,
         CreditItemFragment.OnFragmentInteractionListener,
         SettingFragment.OnFragmentInteractionListener,
         PostFragment.OnFragmentInteractionListener{
        FragmentManager fm;
//  Declare a static fragement for resued its public method
   public static AddProductFragment addProductFragment;

    //Declare a global variable for add category
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
        //Initialize the fragmentManager
        fm=getSupportFragmentManager();
        //Transfer From Actity_main layout to Home screen
         final FragmentTransaction transaction=fm.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.content,new HomeFragment());
        transaction.commit();
        //Delete the app name from header
          actionBar= getSupportActionBar();
        actionBar.setTitle("");

        //Retrieve the add category Icon on the toolbar container
        addCategoryImage=(ImageView)toolbar.findViewById(R.id.addCategoryImage);

        //Retrieve the search icon on the Toolbar container
        searchProductImage=(ImageView)toolbar.findViewById(R.id.searchProductImage);
      addProductFragment=new AddProductFragment();
    //  searchFragment=new SearchFragment();

       //While user click the search Icon on the toolbar , The Screen will launch to Search Fragment
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Declare the FragmentTransaction object
        FragmentTransaction transaction=fm.beginTransaction();
        if (id == R.id.nav_home) {
            // Navitation to home Fragment
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new HomeFragment());


        } else if (id == R.id.nav_contact) {
            //Navigation to Contact Fragment
         transaction.addToBackStack(null);
         transaction.replace(R.id.content,new ContactFragment());

        } else if (id == R.id.nav_credit) {
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new CreditFragment());

        } else if (id == R.id.nav_setting) {
            //Navigation to setting fragment
            transaction.addToBackStack(null);
            //add animation to setting preference list
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
           //transaction.add(R.id.content,new SettingFragment(),"settingpage");
            transaction.replace(R.id.content,new SettingFragment());

        } else if (id == R.id.nav_priceRef) {
            //Navigation to Price Reference fragment
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new PriceReferFragment());

        } else if (id == R.id.nav_warning) {

            //Navigation to Stock Warning fragment
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new StockWarningFragment());
        }else if(id==R.id.nav_post){
            transaction.addToBackStack(null);
            transaction.replace(R.id.content,new PostFragment());
        }

        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     *
     * @param requestCode code which dispatches different permission request
     * @param permissions permission arrays in manifest.xml
     * @param grantResults  arrays hold the user's grants permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            //666 stands for camera requests from AddProductFragment
            case 666:
                if(grantResults.length>0&&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    if(addProductFragment!=null){
                        //call the asynchonization background running method
                       addProductFragment.ExecuteImageUpload();}
                }else{
                // else do nothing
                }
                break;

        }
    }
}