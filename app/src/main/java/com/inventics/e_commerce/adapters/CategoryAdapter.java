package com.inventics.e_commerce.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.modal.categories;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    Context context;
    ArrayList<categories> data;

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Log.i("8826",data.toString());

        Log.i("8825",data.get(position).getCategory().toString());
            holder.categoryChip.setText(data.get(position).getCategory());

            holder.categoryChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, data.get(position).getCategory(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    public CategoryAdapter(Context context, ArrayList<categories> data) {
        this.context = context;
        this.data = data;
    }

    public CategoryAdapter() {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        Chip categoryChip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.chip_categories);
        }
    }


}
