package com.inventics.e_commerce.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.activities.ProductDescriptionActivity;
import com.inventics.e_commerce.modal.Product;

import java.util.ArrayList;

public class ProductDataAdapter extends RecyclerView.Adapter<ProductDataAdapter.ViewHolder>{
    Context context;
    ArrayList<Product> data;


    @NonNull
    @Override
    public ProductDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDataAdapter.ViewHolder holder, int position) {
        holder.productTitle.setText(data.get(position).getTitle());
        holder.productPrice.setText("INR "+data.get(position).getPrice().toString());
        holder.productCategory.setText(data.get(position).getCategory());
        holder.productRating.setRating(data.get(position).getRating().getRate());
        holder.productRateCount.setText(data.get(position).getRating().getCount()+"Reviews");
        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Storing the the product key in the sharedPreference and will get that productKey in the productDescription activity so that
                // I can get that particular product
                SharedPreferences productKeyPreference = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor  myEditor = productKeyPreference.edit();
                myEditor.putString("p_id",data.get(position).getKey());
                myEditor.apply();

                context.startActivity(new Intent(context, ProductDescriptionActivity.class));
            }
        });


        String imageUrl = data.get(position).getImage();
        Log.d("TAG", "onBindViewHolder: "+imageUrl);

        Glide.with(context)
                .load(imageUrl)
                .into(holder.productImage);



    }

    public ProductDataAdapter(ArrayList<Product> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productCategory,productTitle,productPrice, productRateCount;
        RatingBar productRating;

        CardView productCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productTitle = itemView.findViewById(R.id.productTitle);
            productCategory = itemView.findViewById(R.id.productCategory);
            productImage = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            productRateCount = itemView.findViewById(R.id.productRateCount);
            productCard = itemView.findViewById(R.id.productCard);


        }
    }
}
