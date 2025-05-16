package com.example.projeodevienson;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sepet.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CART = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_QUANTITY = "quantity";  // Yeni sütun

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tablo oluşturuluyor
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " TEXT,"
                + COLUMN_IMAGE + " INTEGER,"
                + COLUMN_QUANTITY + " INTEGER DEFAULT 1" + ")"; // Quantity sütunu ekledik
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Sepete ürün ekleme metodu
    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE, product.getImageResId());
        values.put(COLUMN_QUANTITY, 1); // Başlangıçta miktar 1

        db.insert(TABLE_CART, null, values);
        db.close();
    }

    // Sepetteki ürünleri almak için metod
    public List<Product> getCartItems() {
        List<Product> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
                @SuppressLint("Range") int imageResId = cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));

                cartItems.add(new Product(id, imageResId, name, price, quantity)); // Yeni constructor ile
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cartItems;
    }

    // Sepetteki bir ürünü güncelleme metodu (miktar değişikliği)
    public void updateProductQuantity(int productId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);

        db.update(TABLE_CART, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Sepetteki bir ürünü silme metodu
    public void removeFromCart(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }
    // Sepeti temizle
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null); // Doğru tablo ismi kullanıldı
        db.close();
    }


    // Sepetin toplam tutarını almak için metod
    public double getTotalPrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalPrice = 0;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));

                // Fiyatı sayıya çeviriyoruz ve miktar ile çarpıyoruz
                totalPrice += Double.parseDouble(price.replace("₺", "")) * quantity;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return totalPrice;
    }

    // Sepetteki toplam ürün sayısını almak için metod
    public int getTotalQuantity() {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalQuantity = 0;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
                totalQuantity += quantity;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return totalQuantity;
    }
}
