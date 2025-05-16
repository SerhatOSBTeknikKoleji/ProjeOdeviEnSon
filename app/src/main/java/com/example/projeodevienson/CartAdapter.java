package com.example.projeodevienson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Product> cartProductList;
    private final Context context;
    private final CartUpdateListener cartUpdateListener;
    private final DatabaseHelper databaseHelper;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(List<Product> cartProductList, Context context, CartUpdateListener listener) {
        this.cartProductList = cartProductList;
        this.context = context;
        this.cartUpdateListener = listener;
        this.databaseHelper = new DatabaseHelper(context); // DB erişimi
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productQuantity;
        FloatingActionButton addBtn, subtractBtn;

        public CartViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productNameHead);
            productPrice = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            addBtn = itemView.findViewById(R.id.addQuantityBtn);
            subtractBtn = itemView.findViewById(R.id.subtractQuantityBtn);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartProductList.get(position);

        holder.productImage.setImageResource(product.getImageResId());
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        holder.addBtn.setOnClickListener(v -> {
            int newQuantity = product.getQuantity() + 1;
            product.setQuantity(newQuantity);
            holder.productQuantity.setText(String.valueOf(newQuantity));
            databaseHelper.updateProductQuantity(product.getId(), newQuantity);
            if (cartUpdateListener != null) cartUpdateListener.onCartUpdated();
        });

        holder.subtractBtn.setOnClickListener(v -> {
            int currentQuantity = product.getQuantity();
            if (currentQuantity > 1) {
                int newQuantity = currentQuantity - 1;
                product.setQuantity(newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
                databaseHelper.updateProductQuantity(product.getId(), newQuantity);
            } else {
                // Ürün adedi 1 ise, sil
                databaseHelper.removeFromCart(product.getId());
                cartProductList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartProductList.size());
            }

            if (cartUpdateListener != null) cartUpdateListener.onCartUpdated();
        });

    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }
}
