package exercise.android.nami.sandwiches;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class App extends Application {
    private String orderId;
    SharedPreferences sp;

    public App(Context context) {
        this.orderId = "";
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        setOrderId();
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setOrderId() {
        this.orderId = sp.getString("order", "");
    }

    public void resetOrderId() {
        this.orderId = "";
        sp.edit().putString("orderId", "").apply();
    }

    public void saveOrderId(String id) {
        this.orderId = id;
        sp.edit().putString("order", this.orderId).apply();
    }

}