package com.firebase.productlistmvvmfirestore.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.productlistmvvmfirestore.Product;
import com.firebase.productlistmvvmfirestore.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private List<Product> productList;

    public ProductsAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items
                , parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bindProduct(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameNextView, priceNextView;

        ProductViewHolder(View itemView) {
            super(itemView);
            nameNextView = itemView.findViewById(R.id.name_text_view);
            priceNextView = itemView.findViewById(R.id.price_text_view);
        }

        void bindProduct(Product product) {
            nameNextView.setText(product.name);
            priceNextView.setText(String.valueOf(product.price));
        }
    }
}