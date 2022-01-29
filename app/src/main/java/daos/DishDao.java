package daos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import models.Dish;

public class DishDao {
    FirebaseFirestore db;
    public CollectionReference dishCollections;
    public UserDao userDao;

    public DishDao() {
        db = FirebaseFirestore.getInstance();
        dishCollections = db.collection("dishes");
        userDao=new UserDao();
    }

    public void addDish(String image,String name,String extras,int price) {
        String currentUserId = userDao.userId;
        Log.d("TAG","Dishdao Userid:"+currentUserId);

        Dish Dish = new Dish(image,name,extras,price,currentUserId);
        dishCollections.document().set(Dish)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG","DishDao upload onSuccess");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { Log.d("TAG","DishDao upload failed!:"+e.getMessage());}
        });
    }
    public Task<DocumentSnapshot> getDishById(String dishId){
        return dishCollections.document(dishId).get();
    }
    public void deleteDish(String dishId){
        dishCollections.document(dishId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG","delete Dish Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","delete Dish failed :"+e.getMessage());
            }
        });
    }
}