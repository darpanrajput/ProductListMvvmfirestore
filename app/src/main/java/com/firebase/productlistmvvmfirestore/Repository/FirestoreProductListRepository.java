package com.firebase.productlistmvvmfirestore.Repository;

import com.firebase.productlistmvvmfirestore.ProductListLiveData;
import com.firebase.productlistmvvmfirestore.ViewModal.ProductListViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import static com.firebase.productlistmvvmfirestore.Constants.LIMIT;
import static com.firebase.productlistmvvmfirestore.Constants.PRODUCTS_COLLECTION;
import static com.firebase.productlistmvvmfirestore.Constants.PRODUCT_NAME_PROPERTY;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;

public class FirestoreProductListRepository implements
        ProductListViewModel.ProductListRepository,//interface in view modal ProductListRepository
        ProductListLiveData.OnLastVisibleProductCallback,
        ProductListLiveData.OnLastProductReachedCallback {
    public static final String TAG = "FirestoreProductListRepository";

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    //getting the reference of our product collection
    private CollectionReference productsRef = firebaseFirestore.collection(PRODUCTS_COLLECTION);

    //querying the product collection
    private Query query = productsRef
            .orderBy(PRODUCT_NAME_PROPERTY, ASCENDING)
            .limit(LIMIT);

    private DocumentSnapshot lastVisibleProduct;
    private boolean isLastProductReached;
    private FirebaseFirestoreException FX;

    @Override
    public ProductListLiveData getProductListLiveData() {
        /*here overiding a method whose having the
        * return type of ProductListLiveData*/


        try {
            System.out.println(TAG + "isLastProductReached=" + isLastProductReached);
        } catch (Exception e) {
            System.out.println(TAG + " Exceeption=getProductListLiveData:" + e.getMessage());
        }
        if (isLastProductReached) {
            return null;
        }
        if (lastVisibleProduct != null) {
            query = query.startAfter(lastVisibleProduct);
        }
        return new ProductListLiveData(query,
                this,
                this);//return statement will call the ProductListLiveData class
    }

    @Override
    public void setLastVisibleProduct(DocumentSnapshot lastVisibleProduct) {
        this.lastVisibleProduct = lastVisibleProduct;
    }

    @Override
    public void setLastProductReached(boolean isLastProductReached) {
        this.isLastProductReached = isLastProductReached;
    }

    @Override
    public void firebaseErrorException(FirebaseFirestoreException Exception) {
        this.FX = Exception;

    }


}
