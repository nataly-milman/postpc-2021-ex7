package exercise.android.nami.sandwiches;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class YourOrderInMakingActivity extends AppCompatActivity {
    private Order order;
    private ListenerRegistration status;

    App app;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_making);

        app = new App(this);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        status = db.collection("orders").document(order.getId())
                .addSnapshotListener((val, error) -> {
                    if (val != null && val.exists()) {
                        Order order = val.toObject(Order.class);
                        Intent newIntent = null;
                        assert order != null;
                        if (order.getStatus().equals("done")) {
                            newIntent = new Intent(this, NewOrderActivity.class);
                        } else if (order.getStatus().equals("ready")) {
                            newIntent = new Intent(this, YourOrderIsReadyActivity.class);
                            newIntent.putExtra("order", order);
                        }
                        if (newIntent != null) {
                            startActivity(newIntent);
                            finish();
                        }
                    }
                });
    }
}