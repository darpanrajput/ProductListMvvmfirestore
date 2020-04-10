package com.firebase.productlistmvvmfirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.widget.AbsListView;

import androidx.annotation.NonNull;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.productlistmvvmfirestore.Adapter.ProductsAdapter;
import com.firebase.productlistmvvmfirestore.ViewModal.ProductListViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView productsRecyclerView;
    private List<Product> productList = new ArrayList<>();

    private ProductsAdapter productsAdapter;
    private ProductListViewModel productListViewModel;
    private boolean isScrolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initProductsRecyclerView();
        initProductsAdapter();
        initProductListViewModel();
        getProducts();
        initRecyclerViewOnScrollListener();
    }



    private void initProductsRecyclerView() {
        productsRecyclerView = findViewById(R.id.products_recycler_view);
        System.out.println("initProductsRecyclerView called");
    }

    private void initProductsAdapter() {
        System.out.println("initProductsAdapter called");

        productsAdapter = new ProductsAdapter(productList);
        productsRecyclerView.setAdapter(productsAdapter);
    }

    private void initProductListViewModel() {
        System.out.println("initProductListViewModel called");

        productListViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
    }

    private void getProducts() {
        System.out.println("getProducts called");

        ProductListLiveData productListLiveData = productListViewModel.getProductListLiveData();
        if (productListLiveData != null) {
            productListLiveData.observe(this, new Observer<Operation>() {
                @Override
                public void onChanged(Operation operation) {
                    switch (operation.type) {
                        case R.string.added:
                            Product addedProduct = operation.product;
                            MainActivity.this.addProduct(addedProduct);
                            break;

                        case R.string.modified:
                            Product modifiedProduct = operation.product;
                            MainActivity.this.modifyProduct(modifiedProduct);
                            break;

                        case R.string.removed:
                            Product removedProduct = operation.product;
                            MainActivity.this.removeProduct(removedProduct);
                    }
                    productsAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void addProduct(Product addedProduct) {
        productList.add(addedProduct);
        System.out.println("addProduct called");

    }

    private void modifyProduct(Product modifiedProduct) {
        System.out.println("modifyProduct called");

        for (int i = 0; i < productList.size(); i++) {
            Product currentProduct = productList.get(i);
            if (currentProduct.id.equals(modifiedProduct.id)) {
                productList.remove(currentProduct);
                productList.add(i, modifiedProduct);
            }
        }
    }

    private void removeProduct(Product removedProduct) {
        System.out.println("removeProduct called");

        for (int i = 0; i < productList.size(); i++) {
            Product currentProduct = productList.get(i);
            if (currentProduct.id.equals(removedProduct.id)) {
                productList.remove(currentProduct);
            }
        }
    }

    private void initRecyclerViewOnScrollListener() {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (layoutManager != null) {
                    int firstVisibleProductPosition = layoutManager.findFirstVisibleItemPosition();
                    int visibleProductCount = layoutManager.getChildCount();
                    int totalProductCount = layoutManager.getItemCount();

                    if (isScrolling && (firstVisibleProductPosition + visibleProductCount == totalProductCount)) {
                        isScrolling = false;
                        getProducts();
                    }
                }
            }
        };
        productsRecyclerView.addOnScrollListener(onScrollListener);
    }

}
