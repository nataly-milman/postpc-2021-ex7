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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderId = preferences.getString("id", "");

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToOrderStatus(String orderId){
        db.collection("orders").document(orderId).get().
                addOnSuccessListener(res -> {

                    if (res == null){
                        Intent intent = new Intent(this, NewOrderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

}