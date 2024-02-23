package com.inventics.e_commerce.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventics.e_commerce.R;
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
        holder.productPrice.setText(data.get(position).getPrice().toString());
        holder.productCategory.setText(data.get(position).getCategory());
        holder.productRating.setRating(data.get(position).getRating().getRate());
        holder.productRateCount.setText(data.get(position).getRating().getCount()+"Reviews");


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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productTitle = itemView.findViewById(R.id.productTitle);
            productCategory = itemView.findViewById(R.id.productCategory);
            productImage = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            productRateCount = itemView.findViewById(R.id.productRateCount);

        }
    }
}
