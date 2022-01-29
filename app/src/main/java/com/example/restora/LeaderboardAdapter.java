package com.example.restora;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import daos.UserDao;
import models.CustomDish;
import models.User;

public class LeaderboardAdapter extends FirestoreRecyclerAdapter<CustomDish,LeaderboardViewHolder> {

    public LeaderboardAdapter(@NonNull FirestoreRecyclerOptions<CustomDish> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LeaderboardViewHolder leaderboardViewHolder, @SuppressLint("RecyclerView") int i, @NonNull CustomDish customDish) {
        UserDao userDao=new UserDao();
        userDao.getUserById(customDish.userId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user=task.getResult().toObject(User.class);
                leaderboardViewHolder.name.setText(user.displayName);
                leaderboardViewHolder.dishName.setText(customDish.recipeName);
                leaderboardViewHolder.rank.setText(Integer.toString(i+1));
                leaderboardViewHolder.likes.setText(customDish.likes);
                Glide.with(leaderboardViewHolder.image.getContext()).load(user.imageUrl).into(leaderboardViewHolder.image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","fetch user failed in LeaderboardAdapter :"+e.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_item,parent,false);
        LeaderboardViewHolder leaderboardViewHolder=new LeaderboardViewHolder(view);

        return leaderboardViewHolder;
    }
}

class LeaderboardViewHolder extends RecyclerView.ViewHolder{
    ImageView image;
    TextView name,dishName,likes,rank;
    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.leaderboardImage);
        name=itemView.findViewById(R.id.leaderboardName);
        dishName=itemView.findViewById(R.id.leaderboardDish);
        likes=itemView.findViewById(R.id.leaderboardLikes);
        rank=itemView.findViewById(R.id.leaderboardRank);
    }
}
