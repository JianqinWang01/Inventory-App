package com.example.wjqcau.inventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjqcau.inventory.JavaBean.Product;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * @author wjqcau
 * Date created: 2019-04-11
 * The class will be used in PostProductFragment
 * decalre the adapter for display all the product list with recyclerview style
 * When click one product item , it will tweets the certain product to twitter
 */
public class PostProductAdapter extends RecyclerView.Adapter<PostProductAdapter.CustomerViewHolder> {
   //ProgressDialog dialog;
    //declare the properties for adapter
    ArrayList<Product> products=new ArrayList<>();
    Context context;
    InputStream in;
    Product productPost;
    //Constructor to populate all the properties
    public PostProductAdapter(Context context,ArrayList<Product> products){
        //Populate the context object
        this.context=context;
        //populate the produts arraylist
        this.products=products;
    }


    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       //Inflator the viewholder
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item,viewGroup,false);
       //Define the customerviewholder object
        final CustomerViewHolder viewHolder=new CustomerViewHolder(view);
       // final Product product=products.get(viewHolder.getAdapterPosition());
       //Declare the click event
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           //get the produt according the item position
            productPost=products.get(viewHolder.getAdapterPosition());
            //call the asycronized method to pubish the product
                new RefreshChanges().execute();
            }
        });

        return viewHolder;
    }
    //Inner class to execute the asynronize task
    class RefreshChanges extends AsyncTask<String, Void, String> {
        //delcare the progress bar to show the state
        private ProgressDialog mProgressDialog = new ProgressDialog(
                context);

        @Override
        protected void onPreExecute() {
           // mProgressDialog.setTitle("Whatever title");
            mProgressDialog.setMessage("Please Waiting ...");
           //show the pregress dialogue before the task execute
            mProgressDialog.show();
        }
        //Declare the method to execute task
        protected String doInBackground(String... strings) {
           //call the method to execute
            postToTwitter(productPost);
            return "";
        }
        //This method will be called after the task done
        protected void onPostExecute(String result) {
            //dismiss the progress bar
            mProgressDialog.dismiss();
            //Toast the result
            Toast.makeText(context,"Post product Successfully",Toast.LENGTH_LONG).show();
           //release the result
            mProgressDialog = null;
        }
    }
   //method to post the produt to twitter
    public void postToTwitter(Product product){
        //Get the token
        String token =context.getString(R.string.token);
        //Get the secret
        String secret = context.getString(R.string.tokenSecret);
        //populate the accesstoken with the tocken and secrete
        AccessToken a = new AccessToken(token,secret);
        //Define the twitter instance
        Twitter twitter = new TwitterFactory().getInstance();
        //Get the authorization with api key
        twitter.setOAuthConsumer(context.getString(R.string.APIkey), context.getString(R.string.APIkeySecrete));
        twitter.setOAuthAccessToken(a);
        try {
            //Define a object of StatusUpdate with the text which will be tweets
            StatusUpdate statusUpdate = new StatusUpdate("Today's Recomendation product is:\n"+product.getName()+"\n"+"$"+product.getPrice());
            //Get the image url from the product
            String imageUrl = product.getImageUrl();
            URL url = new URL(imageUrl);
            //Build a connection
            URLConnection urlConnection = url.openConnection();
            //download the iamge from the remote url to memory
            in = new BufferedInputStream(urlConnection.getInputStream());
            //add the media to to statusUpte object
            statusUpdate.setMedia("1.jpg", in);
            //Use the third party library to tweets the information
            twitter4j.Status status = twitter.updateStatus(statusUpdate);

            in.close();
            // twitter.updateStatus("First Test");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder customerViewHolder, int i) {
        //Get the product in the item
        Product product=products.get(i);
        //binding the value to the components in the viewholder
        customerViewHolder.productPrice.setText(product.getPrice());
        customerViewHolder.productName.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return (null!=products?products.size():0);
    }
    //Define the customer viewholder
    public class CustomerViewHolder extends RecyclerView.ViewHolder{
       //declare the properties
        TextView productName;
        TextView productPrice;
       //Constructor to populate the components in the viewholder
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            productName= itemView.findViewById(R.id.postProductName);
            productPrice=itemView.findViewById(R.id.postProductPrice);
        }
    }


}
