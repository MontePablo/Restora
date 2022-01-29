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

import models.Address;

public class AddressDao {
    public CollectionReference addressCollection;
    public CollectionReference allAddressReference;
    public String currentUserId;
    public AddressDao(){
        currentUserId=(new UserDao()).userId;
        addressCollection= FirebaseFirestore.getInstance().collection("addresses");
        allAddressReference=addressCollection.document(currentUserId).collection(currentUserId);
    }
    public void uploadAddress(Address address){
        Log.d("TAG","uploadAddress start");
        allAddressReference.document().set(address)
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","address upload failed!:"+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG","address upload success!");
            }
        });
        Log.d("TAG","uploadAddress end");
    }
    public Task<DocumentSnapshot> getAddressById(String id){
        return allAddressReference.document(id).get();
    }
}
