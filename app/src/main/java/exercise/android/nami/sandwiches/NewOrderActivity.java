package exercise.android.nami.sandwiches;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import javax.annotation.Nullable;

public class NewOrderActivity extends AppCompatActivity {

        Order order;
        CheckBox hummus;
        CheckBox tahini;
        EditText nameEdit;
        EditText commentsEdit;
        Slider picklesSlider;
        Button orderButton;

//        App app;
        FirebaseFirestore db;

        private SharedPreferences sp;


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {

                System.out.println("In on create NOA");
                super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_new_order);
//            app = new App();
            db = FirebaseFirestore.getInstance();

            sp = PreferenceManager.getDefaultSharedPreferences(this);
            hummus = findViewById(R.id.hummusCheckBox);
            tahini = findViewById(R.id.tahiniCheckBox);
            nameEdit = findViewById(R.id.nameEdit);
            commentsEdit = findViewById(R.id.commentsView);
            picklesSlider = findViewById(R.id.picklesSlider);
            orderButton = findViewById(R.id.orderNowButton);

            order = new Order();

            orderButton.setOnClickListener(v-> placeOrder());
        }

        private void placeOrder(){
            if (TextUtils.isEmpty(nameEdit.getText())){
                nameEdit.setError("Please enter your name");
                return;
            }
            order.setHummus(hummus.isChecked());
            order.setTahini(tahini.isChecked());
            order.setPickles((int) picklesSlider.getValue());
            order.setCustomerName(nameEdit.getText().toString());
            order.setComment(commentsEdit.getText().toString());
            String uuid = UUID.randomUUID().toString();
            order.setId(uuid);
            db.collection("orders").document(uuid).set(order).addOnSuccessListener(
                    documentReference -> {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                        preferences.edit().putString("orderId", order.getId()).apply();
                    }
            ).addOnFailureListener(e-> {
                Toast.makeText(this, "Couldn't create order", Toast.LENGTH_SHORT).show();
            });
        }
}
