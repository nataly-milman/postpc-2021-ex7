package exercise.android.nami.sandwiches;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App app = new App(this);
        String orderId = app.getOrderId();

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        if (!orderId.equals("")) {
            goToOrderStatus(orderId);
            return;
        }

        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToOrderStatus(String orderId) {
        db.collection("orders").document(orderId).get().
                addOnSuccessListener(res -> {
                    if (res == null) {
                        Intent intent = new Intent(this, NewOrderActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    Order order = res.toObject(Order.class);
                    if (order == null || order.getStatus().equals("done")) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = null;
                        if (order.getStatus().equals("in progress")) {
                            intent = new Intent(this, YourOrderInMakingActivity.class);
                        } else if (order.getStatus().equals("waiting")) {
                            intent = new Intent(this, EditYourOrderActivity.class);
                        } else if (order.getStatus().equals("ready")) {
                            intent = new Intent(this, YourOrderIsReadyActivity.class);
                        }
                        if (intent != null) {
                            intent.putExtra("order", order);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

}