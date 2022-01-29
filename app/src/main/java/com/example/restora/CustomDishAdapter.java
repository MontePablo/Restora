package com.example.restora;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import java.util.ArrayList;

import daos.UserDao;
import models.CustomDish;

public class CustomDishAdapter extends FirestoreRecyclerAdapter<CustomDish,CustomDishViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    CustomDishButtonInterface listener;
    public CustomDishAdapter(@NonNull FirestoreRecyclerOptions<CustomDish> options, CustomDishButtonInterface listener) {
        super(options);
        this.listener=listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomDishViewHolder holder, int position, @NonNull CustomDish model) {
        holder.customDishName.setText(model.recipeName);
        holder.customDishDetails.setText(model.recipeDetail);
        holder.totalLikes.setText(model.likes);
        listener.setImagesToLayout(model.images,holder.customDishImageLayout);
        String currentUserId=(new UserDao()).userId;
        if(model.likedBy.contains(currentUserId)){
            holder.customDishLike.setImageDrawable(ContextCompat.getDrawable(holder.customDishLike.getContext(),R.drawable.ic_liked));
        }else{
            holder.customDishLike.setImageDrawable(ContextCompat.getDrawable(holder.customDishLike.getContext(),R.drawable.ic_unliked));
        }
        if(model.userId.equals(currentUserId)){
            holder.customDishCancel.setVisibility(View.VISIBLE);
        }else{
            holder.customDishCancel.setVisibility(View.INVISIBLE);
        }
        holder.customDishDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gotoDialog(model.recipeDetail);
            }
        });
    }

    @NonNull
    @Override
    public CustomDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_dish,parent,false);
        CustomDishViewHolder customDishViewHolder=new CustomDishViewHolder(view);
        customDishViewHolder.customDishCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<CustomDish> snapshots=getSnapshots();
                String customDishId=snapshots.getSnapshot(customDishViewHolder.getBindingAdapterPosition()).getId();
                listener.onCancelButtonClick(customDishId);
            }
        });
        customDishViewHolder.customDishLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<CustomDish> snapshots=getSnapshots();
                String customDishId=snapshots.getSnapshot(customDishViewHolder.getBindingAdapterPosition()).getId();
                listener.likeUnlike(customDishId);
            }
        });

        return customDishViewHolder;
    }
}
class CustomDishViewHolder extends RecyclerView.ViewHolder{
    TextView customDishName,customDishDetails;
    Button customDishCancel;
    ImageView customDishLike;
    TextView totalLikes;
    LinearLayout customDishImageLayout;
    public CustomDishViewHolder(@NonNull View itemView) {
        super(itemView);
        totalLikes=itemView.findViewById(R.id.totalLikes);
        customDishCancel=itemView.findViewById(R.id.customDishCancel);
        customDishName=itemView.findViewById(R.id.customDishName);
        customDishDetails=itemView.findViewById(R.id.customDishDetail);
        customDishImageLayout=itemView.findViewById(R.id.customDishImageLayout);
        customDishLike=itemView.findViewById(R.id.likeButton);
    }
}
interface CustomDishButtonInterface{
    void onCancelButtonClick(String customDishId);
    void setImagesToLayout(ArrayList<String> images,LinearLayout customDishImageLayout);
    void likeUnlike(String customDishId);
    void gotoDialog(String customDishId);
}


