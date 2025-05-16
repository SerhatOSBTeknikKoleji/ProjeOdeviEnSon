package com.example.projeodevienson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    CartAdapter cartAdapter;
    List<Product> cartProductList;
    TextView totalItemsText, totalPriceText;
    FloatingActionButton completeOrderFab;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Veritabanı bağlantısını alıyoruz
        databaseHelper = new DatabaseHelper(this);

        // Görünüm bileşenlerini bağlama
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalItemsText = findViewById(R.id.totalItemsText);
        totalPriceText = findViewById(R.id.totalPriceText);
        completeOrderFab = findViewById(R.id.completeOrderFab);

        // RecyclerView için LayoutManager
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sepetteki ürünleri veritabanından çekiyoruz
        cartProductList = databaseHelper.getCartItems();

        // Adapter ile verileri RecyclerView'a bağlıyoruz
        cartAdapter = new CartAdapter(cartProductList, this, () -> updateCartSummary());
        cartRecyclerView.setAdapter(cartAdapter);


        // Sepet toplamını hesapla
        updateCartSummary();

        completeOrderFab.setOnClickListener(v -> {
            databaseHelper.clearCart(); // ✅ Sepeti temizler
            Toast.makeText(CartActivity.this, "Alışverişiniz bitmiştir. Bizi tercih ettiğiniz için teşekkür ederiz!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.topAppBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Bu aktiviteyi kapat
            }
        });
    }

    // Sepet toplamını ve adetini güncelle
    private void updateCartSummary() {
        int totalItems = databaseHelper.getTotalQuantity();
        double totalPrice = databaseHelper.getTotalPrice();

        totalItemsText.setText("Toplam Ürün: " + totalItems);
        totalPriceText.setText("Toplam: ₺" + totalPrice);
    }

}