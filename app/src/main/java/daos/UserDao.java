package daos;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import models.User;

public class UserDao {

    private static int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    FirebaseFirestore db;
    public String userId;
    CollectionReference userCollection;
    public UserDao(){
        db = FirebaseFirestore.getInstance();
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        userCollection=db.collection("users");
    }

    public void addUser(User user){
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userCollection.document(user.uid).set(user);
            }
        });
    }
    public Task<DocumentSnapshot> getUserById(String uid){
        return userCollection.document(uid).get();
    }
}