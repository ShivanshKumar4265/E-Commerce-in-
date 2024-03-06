package com.inventics.e_commerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            deleteItem = itemView.findViewById(R.id.deleteItem);
            cart_productTitle = itemView.findViewById(R.id.cart_productTitle);
            cart_productDescription = itemView.findViewById(R.id.cart_productDescription);
            cart_productQuantity = itemView.findViewById(R.id.cart_productQuantity);
            cart_NoOfProduct = itemView.findViewById(R.id.cart_NoOfProduct);
            totalPrice = itemView.findViewById(R.id.totalPrice);

        }
    }

}
