package com.example.restora;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import models.CartDish;

public class CartAdapter extends FirestoreRecyclerAdapter<CartDish,CartViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    CartDishButtonsInterface listener;
    public CartAdapter(@NonNull FirestoreRecyclerOptions<CartDish> options,CartDishButtonsInterface listener) {
        super(options);
        this.listener=listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartDish model) {
        holder.cartDishName.setText(model.name);
        holder.cartDishQuantity.setText(String.valueOf(model.quantity));
        holder.cartDishExtras.setText(model.extras);
        holder.cartDishPrice.setText(String.valueOf(model.price));
        Glide.with(holder.cartDishImage.getContext()).load(model.image).into(holder.cartDishImage);

        ObservableSnapshotArray<CartDish> snapshots=getSnapshots();
        String cartDishId=snapshots.getSnapshot(holder.getBindingAdapterPosition()).getId();
        listener.sendPriceToCartTotal(model.price,model.quantity,cartDishId);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartdish,parent,false);
        CartViewHolder cartViewHolder=new CartViewHolder(view);
        cartViewHolder.cartDishIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<CartDish> snapshots=getSnapshots();
                String cartDishId=snapshots.getSnapshot(cartViewHolder.getBindingAdapterPosition()).getId();
                listener.cartDishQuantityIncrease(cartDishId);
            }
        });
        cartViewHolder.cartDishDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<CartDish> snapshots=getSnapshots();
                String cartDishId=snapshots.getSnapshot(cartViewHolder.getBindingAdapterPosition()).getId();
                listener.cartDishQuantityDecrease(cartDishId);
            }
        });
        cartViewHolder.cartDishDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<CartDish> snapshots=getSnapshots();
                String cartDishId=snapshots.getSnapshot(cartViewHolder.getBindingAdapterPosition()).getId();
                listener.cartDishDelete(cartDishId);
            }
        });
        return cartViewHolder;
    }

}


class CartViewHolder extends RecyclerView.ViewHolder{
    TextView cartDishName,cartDishExtras,cartDishPrice,cartDishQuantity;
    Button cartDishIncreaseQuantity,cartDishDecreaseQuantity,cartDishDelete;
    ImageView cartDishImage;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartDishPrice=itemView.findViewById(R.id.cartdishPrice);
        cartDishName=itemView.findViewById(R.id.cartdishName);
        cartDishImage=itemView.findViewById(R.id.cartDishImage);
        cartDishExtras=itemView.findViewById(R.id.cartdishExtras);
        cartDishQuantity=itemView.findViewById(R.id.cartdishQuantity);
        cartDishDelete=itemView.findViewById(R.id.cartDishDelete);
        cartDishDecreaseQuantity=itemView.findViewById(R.id.cartDishDecreaseQuantity);
        cartDishIncreaseQuantity=itemView.findViewById(R.id.cartdishIncreaseQuantity);
    }
}
interface CartDishButtonsInterface{
    void cartDishQuantityIncrease(String cartDishId);
    void cartDishQuantityDecrease(String cartDishId);
    void cartDishDelete(String cartDishId);
    void sendPriceToCartTotal(int price,int quantity,String id);

}