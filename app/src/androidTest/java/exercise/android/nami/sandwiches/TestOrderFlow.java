package exercise.android.nami.sandwiches;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TestOrderFlow {

    Order order = new Order();
    private void setUp(){
        order.setId("TEST ID SAME FROM NOW ON");
        order.setCustomerName("Lina Inverse");  //I can go with popular names, do not sue me
        order.setPickles(10); // she would
        order.setTahini(true);
        order.setComment("Or else");
    }

    private static void onSuccessLinaTest(DocumentSnapshot documentSnapshot) {
        assertEquals("Lina Inverse", documentSnapshot.getString("name"));
        assertEquals("Or else", documentSnapshot.getString("comment"));
        assertEquals(10, documentSnapshot.get("pickles"));
        Boolean tahini = documentSnapshot.getBoolean("tahini");
        assertNotNull(tahini);
        assertTrue(tahini);
        Boolean humus = documentSnapshot.getBoolean("humus");
        assertFalse(humus);
    }

    private static void onSuccessXellosTest(DocumentSnapshot documentSnapshot) {
        assertEquals("Xellos", documentSnapshot.getString("name"));
        assertEquals("It's a secret!", documentSnapshot.getString("comment"));
        assertEquals(0, documentSnapshot.get("pickles"));
        Boolean tahini = documentSnapshot.getBoolean("tahini");
        assertNotNull(tahini);
        assertFalse(tahini);
        Boolean humus = documentSnapshot.getBoolean("humus");
        assertNotNull(humus);
        assertFalse(humus);
    }


    @Test
    public void test_db_order_creation()
    {
        this.setUp();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders").document(order.getId()).set(order);
        db.collection("orders").document(order.getId()).get()
                .addOnSuccessListener(TestOrderFlow::onSuccessLinaTest)
                .addOnFailureListener(documentSnapshot -> { fail(); });
    }

    @Test
    public void test_db_order_edit()
    {
        this.setUp();

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders").document(order.getId()).set(order);

        order.setCustomerName("Xellos");
        order.setComment("It's a secret!");
        order.setPickles(0);
        order.setTahini(false);
        order.setHummus(false);
        db.collection("orders").document(order.getId()).update("customerName","Xellos");
        db.collection("orders").document(order.getId()).update("hummus", false);
        db.collection("orders").document(order.getId()).update("tahini", false);
        db.collection("orders").document(order.getId()).update("pickles", 0);
        db.collection("orders").document(order.getId()).update("comment", "It's a secret!");

        db.collection("orders").document(order.getId()).get()
                .addOnSuccessListener(TestOrderFlow::onSuccessXellosTest)
                .addOnFailureListener(documentSnapshot -> { fail(); });

    }

    @Test
    public void test_db_order_delete() {
        this.setUp();

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders").document(order.getId()).set(order);
        db.collection("orders").document(order.getId()).delete().addOnFailureListener(documentSnapshot -> { fail(); });

    }
}