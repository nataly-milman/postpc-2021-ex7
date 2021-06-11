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
import com.google.firebase.firestore.ListenerRegistration;

import javax.annotation.Nullable;

public class EditYourOrderActivity extends AppCompatActivity {
    Order order;
    CheckBox hummus;
    CheckBox tahini;
    EditText nameEdit;
    EditText commentsEdit;
    Slider picklesSlider;
    Button orderButton;
    FirebaseFirestore db;

    ListenerRegistration status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        db = FirebaseFirestore.getInstance();
//        sp = PreferenceManager.getDefaultSharedPreferences(this);
        hummus = findViewById(R.id.hummusCheckBox);
        tahini = findViewById(R.id.tahiniCheckBox);
        nameEdit = findViewById(R.id.nameEdit);
        commentsEdit = findViewById(R.id.commentsView);
        picklesSlider = findViewById(R.id.picklesSlider);
        orderButton = findViewById(R.id.orderNowButton);

        order = new Order();
        Button saveButton = findViewById(R.id.saveChangesButton);
        Button deleteButton = findViewById(R.id.deleteOrderButton);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        hummus.setChecked(order.getHummus());
        tahini.setChecked(order.getTahini());
        nameEdit.setText(order.getCustomerName());
        commentsEdit.setText(order.getComment());
        picklesSlider.setValue(order.getPickles());

        saveButton.setOnClickListener(v -> updateOrder());
        deleteButton.setOnClickListener(v -> deleteOrder());
        status = orderStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status.remove();
    }

    private ListenerRegistration orderStatus() {
        return db.collection("orders").document(order.getId())
                .addSnapshotListener((value, error) -> {
                    if (value != null && value.exists()) {
                        Order order = value.toObject(Order.class);
                        assert order != null;
                        if (order.getStatus().equals("done")) {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = null;
                            if (order.getStatus().equals("in progress")) {
                                intent = new Intent(this, YourOrderInMakingActivity.class);
                            } else if (order.getStatus().equals("ready")) {
                                intent = new Intent(this, YourOrderIsReadyActivity.class);
                            }
                            if (intent != null) {
                                intent.putExtra("order", order);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                        sp.edit().putString("id", null).apply();
                        startActivity(new Intent(EditYourOrderActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }


    private void updateOrder() {
        if (TextUtils.isEmpty(nameEdit.getText())) {
            Toast toast = Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        order.setCustomerName(this.nameEdit.getText().toString());
        db.collection("orders").document(order.getId()).update("customerName", nameEdit.getText().toString());
        order.setHummus(this.hummus.isChecked());
        db.collection("orders").document(order.getId()).update("hummus", hummus.isChecked());
        order.setTahini(this.tahini.isChecked());
        db.collection("orders").document(order.getId()).update("tahini", tahini.isChecked());
        order.setPickles((int) picklesSlider.getValue());
        db.collection("orders").document(order.getId()).update("pickles", (int) picklesSlider.getValue());
        order.setComment(this.commentsEdit.getText().toString());
        db.collection("orders").document(order.getId()).update("comment", commentsEdit.getText().toString());
    }

    private void deleteOrder() {
        db.collection("orders").document(order.getId()).
                delete().addOnSuccessListener(u -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error deleting order", Toast.LENGTH_SHORT).show();
        });

    }


}