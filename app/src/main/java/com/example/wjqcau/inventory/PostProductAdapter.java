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

public class PostProductAdapter extends RecyclerView.Adapter<PostProductAdapter.CustomerViewHolder> {
   //ProgressDialog dialog;
    ArrayList<Product> products=new ArrayList<>();
    Context context;
    InputStream in;
    Product productPost;
    public PostProductAdapter(Context context,ArrayList<Product> products){
        this.context=context;
        this.products=products;

//        dialog=new ProgressDialog(context);
//        dialog.setMessage("Please waiting...");

    }


    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item,viewGroup,false);
        final CustomerViewHolder viewHolder=new CustomerViewHolder(view);
       // final Product product=products.get(viewHolder.getAdapterPosition());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           // dialog.show();
            productPost=products.get(viewHolder.getAdapterPosition());
                new RefreshChanges().execute();



            }
        });

        return viewHolder;
    }

    class RefreshChanges extends AsyncTask<String, Void, String> {
        private ProgressDialog mProgressDialog = new ProgressDialog(
                context);

        @Override
        protected void onPreExecute() {
           // mProgressDialog.setTitle("Whatever title");
            mProgressDialog.setMessage("Please Waiting ...");
            mProgressDialog.show();
        }

        protected String doInBackground(String... strings) {
            postToTwitter(productPost);
            return "";
        }

        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            Toast.makeText(context,"Post product Successfully",Toast.LENGTH_LONG).show();
            mProgressDialog = null;
        }
    }



    public void postToTwitter(Product product){

        String token =context.getString(R.string.token);

        String secret = context.getString(R.string.tokenSecret);

        AccessToken a = new AccessToken(token,secret);
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(context.getString(R.string.APIkey), context.getString(R.string.APIkeySecrete));
        twitter.setOAuthAccessToken(a);
        try {
            //Product product=products.get(viewHolder.getAdapterPosition());
            StatusUpdate statusUpdate = new StatusUpdate("Today's Recomendation product is:\n"+product.getName()+"\n"+"$"+product.getPrice());
            String imageUrl = product.getImageUrl();
            URL url = new URL(imageUrl);
            URLConnection urlConnection = url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            statusUpdate.setMedia("1.jpg", in);
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
        Product product=products.get(i);
        customerViewHolder.productPrice.setText(product.getPrice());
        customerViewHolder.productName.setText(product.getName());


    }



    @Override
    public int getItemCount() {
        return (null!=products?products.size():0);
    }
    public class CustomerViewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        TextView productPrice;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            productName= itemView.findViewById(R.id.postProductName);
            productPrice=itemView.findViewById(R.id.postProductPrice);


        }
    }


}
