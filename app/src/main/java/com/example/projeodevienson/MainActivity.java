package com.example.projeodevienson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    List<Product> productList;
    FloatingActionButton goCartButton;
    Random random = new Random();

    // Gerçekçi ürün isimleri
    String[] productNames = {
            "Tır Lastiği", "Fren Balatası", "Motor Yağı", "Aydınlatma Sistemi", "Fren Hortumu",
            "Alt Merkez", "Fren Merkezi", "Hava Filtresi", "Radyatör", "Alternatör"
    };

    // Ürün resimleri (resimleri drawable klasöründen alıyoruz)
    int[] productImages = {
            R.drawable.img_tir_lastik, R.drawable.img_fren_balata, R.drawable.img_motor_yag,
            R.drawable.img_aydinlatma, R.drawable.img_fren_hortum,
            R.drawable.img_alt_merkez, R.drawable.img_fren_merkez, R.drawable.img_hava_filtre,
            R.drawable.img_radyator, R.drawable.img_altarnator
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goCartButton = findViewById(R.id.cartFab);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        productList = new ArrayList<>();

        for (int i = 0; i < productNames.length; i++) {
            String productName = productNames[i];
            int productImage = productImages[i];
            int price = (i + 1) * 50;

            productList.add(new Product(productImage, productName, "₺" + price));
        }

        productAdapter = new ProductAdapter(this, productList);
        productRecyclerView.setAdapter(productAdapter);

        goCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

}


