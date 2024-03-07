package com.inventics.e_commerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.modal.Product;

import java.util.ArrayList;

public class CartItemAdapter  extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    Context context;
    ArrayList<Product> data;

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        holder.cart_productTitle.setText(data.get(position).getTitle());
        holder.cart_NoOfProduct.setText(data.get(position).getQty()+"");
        holder.cart_productDescription.setText(data.get(position).getDescription()+"");
        holder.totalPrice.setText("INR: " + (data.get(position).getPrice()) *(data.get(position).getQty()) );
        String imageUrl = data.get(position).getImage().toString();
        Glide.with(context)
                .load(imageUrl) // URL of the image
                .transition(DrawableTransitionOptions.withCrossFade()) // Optional animation
                .into(holder.cart_productImage);
    }

    public CartItemAdapter(Context context, ArrayList<Product> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cart_productImage, deleteItem;
        TextView cart_productTitle,cart_productDescription,cart_productQuantity,cart_NoOfProduct,totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cart_productImage = itemView.findViewById(R.id.cart_productImage);
            cart_productTitle = itemView.findViewById(R.id.cart_productTitle);
            cart_productDescription = itemView.findViewById(R.id.cart_productDescription);
            cart_productQuantity = itemView.findViewById(R.id.cart_productQuantity);
            cart_NoOfProduct = itemView.findViewById(R.id.cart_NoOfProduct);
            totalPrice = itemView.findViewById(R.id.totalPrice);

        }
    }

}
