package exercise.android.nami.sandwiches;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class YourOrderIsReadyActivity extends AppCompatActivity {
    App app;
    FirebaseFirestore db;

    private Button button;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        app = new App(this);
        db = FirebaseFirestore.getInstance();
        button = findViewById(R.id.doneButton);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        button.setOnClickListener(v -> doneOrder());
    }

    private void doneOrder(){
        db.collection("orders").document(order.getId()).
                update("status", "done").addOnSuccessListener(u -> {
            order.setStatus("done");
            app.resetOrderId();
            Toast toast = Toast.makeText(this,"Have a nice day!", Toast.LENGTH_LONG);
            toast.show();
            // and restart
            Intent intent = new Intent(this, NewOrderActivity.class);
            startActivity(intent);
            finish();
        });
    }
}