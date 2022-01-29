package daos;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import models.CartDish;
import models.Dish;

public class CartDishDao {
    DishDao dishDao;
    public CollectionReference cartDishCollection;
    public CollectionReference cartDishIntoCartReference;
    String currentUserId;
    public CartDishDao(){
        cartDishCollection= FirebaseFirestore.getInstance().collection("cartDishes");
        currentUserId=(new UserDao()).userId;
        cartDishIntoCartReference=cartDishCollection;
        dishDao=new DishDao();
    }

    public void addDishFromHomeToCart(String dishId){
        dishDao.getDishById(dishId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Dish dish=task.getResult().toObject(Dish.class);
                CartDish cartDish=new CartDish(dish.image,dish.name,dish.extras,dish.price,1,currentUserId,dishId);
                cartDishIntoCartReference.document().set(cartDish)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG","cartDish add success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","cartDish add failed:"+e.getMessage());
                    }
                });
            }
        });
    }
    public Task<DocumentSnapshot> getCartDishById(String cartDishId){
        return cartDishIntoCartReference.document(cartDishId).get();
    }
    public void increaseQuantity(String cartDishId){
        getCartDishById(cartDishId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                CartDish cartDish=task.getResult().toObject(CartDish.class);
                cartDish.quantity=cartDish.quantity+1;
                cartDishIntoCartReference.document(cartDishId).set(cartDish);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","increaseQuantity failed:"+e.getMessage());
            }
        });
    }
    public void decreaseQuantity(Context context, String cartDishId){
        getCartDishById(cartDishId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                CartDish cartDish=task.getResult().toObject(CartDish.class);
                if(cartDish.quantity==1){
                    Toast.makeText(context,"Quantity is reached minimum\nplease press another button to remove!",Toast.LENGTH_SHORT).show();
                    return;
                }
                cartDish.quantity=cartDish.quantity-1;
                cartDishIntoCartReference.document(cartDishId).set(cartDish);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","decreaseQuantity failed:"+e.getMessage());
            }
        });
    }
    public Task<QuerySnapshot> getAllCartDish(){
        return cartDishIntoCartReference.orderBy("name", Query.Direction.ASCENDING).get();
    }
}
