package daos;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import models.Order;

public class OrderDao {
    CollectionReference orderCollection;
    UserDao userDao;
    CollectionReference orderUploadReference;
        public OrderDao(){
            orderCollection = FirebaseFirestore.getInstance().collection("orders");
            userDao=new UserDao();
            orderUploadReference=orderCollection.document(userDao.userId).collection(userDao.userId);
        }
        public void uploadOrder(Order order){
            orderUploadReference.document(order.orderId).set(order);
        }
        public Task<DocumentSnapshot> getOrderById(String orderId){
            return orderUploadReference.document(orderId).get();
        }
}
