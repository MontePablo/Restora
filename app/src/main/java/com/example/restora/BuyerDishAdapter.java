package com.example.restora;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;

import daos.UserDao;
import models.Dish;

public class BuyerDishAdapter extends FirestoreRecyclerAdapter<Dish,BuyerDishViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    AddToCartFunctionInterface listener;
    public BuyerDishAdapter(@NonNull FirestoreRecyclerOptions<Dish> options,AddToCartFunctionInterface listener) {
        super(options);
        this.listener=listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull BuyerDishViewHolder holder, int position, @NonNull Dish model) {
        holder.dishName.setText(model.name);
        holder.dishPrice.setText(String.valueOf(model.price));
        holder.dishExtras.setText(model.extras);
        Glide.with(holder.dishImage.getContext()).load(model.image).into(holder.dishImage);
        UserDao userDao=new UserDao();
        if(model.userId.equals(userDao.userId)){
            holder.deleteDish.setVisibility(View.VISIBLE);
        }else{
            holder.deleteDish.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public BuyerDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dish,parent,false);
        BuyerDishViewHolder buyerDishViewHolder=new BuyerDishViewHolder(view);
        buyerDishViewHolder.dishAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<Dish> snapshots=getSnapshots();
                String dishId=snapshots.getSnapshot(buyerDishViewHolder.getBindingAdapterPosition()).getId();
                listener.addToCart(dishId);
            }
        });
        buyerDishViewHolder.deleteDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<Dish> snapshots=getSnapshots();
                String dishId=snapshots.getSnapshot(buyerDishViewHolder.getBindingAdapterPosition()).getId();
                listener.delete(dishId);
            }
        });
        return buyerDishViewHolder;
    }
}

class BuyerDishViewHolder extends RecyclerView.ViewHolder{
    TextView dishName,dishPrice,dishExtras;
    ImageView dishImage;
    Button dishAddtoCart;
    Button deleteDish;
    public BuyerDishViewHolder(@NonNull View itemView) {
        super(itemView);
        dishName=itemView.findViewById(R.id.dishName);
        dishImage=itemView.findViewById(R.id.dishImage);
        dishPrice=itemView.findViewById(R.id.dishPrice);
        dishExtras=itemView.findViewById(R.id.dishExtras);
        dishAddtoCart=itemView.findViewById(R.id.dishAddToCart);
        deleteDish=itemView.findViewById(R.id.dishDelete);
    }
}
interface AddToCartFunctionInterface{
    void addToCart(String dishId);
    void delete(String dishId);
}