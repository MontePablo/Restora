package daos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import models.CustomDish;
import models.User;

public class CustomDishDao {
    public CollectionReference customDishCollection;
    public CollectionReference customDishReference;
    public String currentUserId;
    public CustomDishDao(){
        currentUserId=(new UserDao()).userId;
        customDishCollection= FirebaseFirestore.getInstance().collection("customDishes");
        customDishReference=customDishCollection;
    }
    public void uploadCustomDish(CustomDish customDish){
        Log.d("TAG","upload customDish start");
        customDishReference.document().set(customDish).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","customDish upload Failed :"+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG","customDish upload success");
            }
        });
    }
    public Task<DocumentSnapshot> getCustomDishById(String id){
        return customDishReference.document(id).get();
    }

    public void updateLikes(String customDishId) {
        getCustomDishById(customDishId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                CustomDish cd=task.getResult().toObject(CustomDish.class);
                if(cd.likedBy.contains(currentUserId)){
                    cd.likedBy.remove(currentUserId);
                    cd.likes=Integer.toString(cd.likedBy.size());
                }else{
                    cd.likedBy.add(currentUserId);
                    cd.likes=Integer.toString(cd.likedBy.size());
                }
                customDishReference.document(customDishId).set(cd);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","getCustomDishById failed :"+e.getMessage());
            }
        });
    }
}
