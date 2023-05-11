package com.example.bidfrenzy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddToCartRVAdapter extends RecyclerView.Adapter<AddToCartRVAdapter.ViewHolder> {
    private ArrayList<ProductRVModal> addToCartRVModalArrayList;
    private Context context;
    int lastPos = -1;
    private ProductClickInterface productClickInterface;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AddToCartRVAdapter(ArrayList<ProductRVModal> addToCartRVModalArrayList, Context context, ProductClickInterface productClickInterface) {
        this.addToCartRVModalArrayList = addToCartRVModalArrayList;
        this.context = context;
        this.productClickInterface = productClickInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.addtocart_rv_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos){
        final int position = holder.getAdapterPosition();
        holder.productNameTV.setText(addToCartRVModalArrayList.get(position).getProductName());
        holder.productPriceTV.setText("Rs. "+addToCartRVModalArrayList.get(position).getProductPrice());
        Picasso.get().load(addToCartRVModalArrayList.get(position).getProductImage()).into(holder.ProductIV);
        setAnimation(holder.itemView,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productClickInterface.onProductClick(position);
            }
        });

    }

    private void setAnimation(View itemView, int position){
        if(position>lastPos){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return addToCartRVModalArrayList.size();
    }

    public interface ProductClickInterface{
        void onProductClick(int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTV,productPriceTV,productCategory;
        private ImageView ProductIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTV = itemView.findViewById(R.id.idTVProductName);
            productPriceTV = itemView.findViewById(R.id.idTVProductPrice);
            ProductIV = itemView.findViewById(R.id.idIVCart);
            productCategory = itemView.findViewById(R.id.idTVCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

}
