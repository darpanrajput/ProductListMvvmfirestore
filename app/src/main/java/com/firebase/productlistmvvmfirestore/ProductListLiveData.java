package com.firebase.productlistmvvmfirestore;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.firebase.productlistmvvmfirestore.Constants.LIMIT;


public class ProductListLiveData extends LiveData<Operation>
        implements EventListener<QuerySnapshot> {
    private static final String TAG="ProductListLiveData";

    private Query query;
    private ListenerRegistration listenerRegistration;
    private OnLastVisibleProductCallback onLastVisibleProductCallback;
    private OnLastProductReachedCallback onLastProductReachedCallback;

    /*As you can see, the constructor of the class has three arguments,
     a Firestore Query and two Callbacks, one of them helps us know which is the last visible
      product in the list and the second one helps us know when we reached the last product
      in the list.*/

    public ProductListLiveData(Query query,
                               OnLastVisibleProductCallback onLastVisibleProductCallback,
                               OnLastProductReachedCallback onLastProductReachedCallback) {
        this.query = query;
        this.onLastVisibleProductCallback = onLastVisibleProductCallback;
        this.onLastProductReachedCallback = onLastProductReachedCallback;
    }


    /*
     * So in the first method we attach the listener while in the second we remove
     * like we did in onStart and onDestroy
     * to start listening and to remove the listener from the adapter*/


    /*What we are trying to achieve when implementing the EventListenererinterface
    is that we want to make this class be it’s own listener.
    I think that you already noticed that the type of our LiveData is Operation.
    This is a simple Java class that contains two fields:
     */

    @Override
    protected void onActive() {
        listenerRegistration = query.addSnapshotListener(this);
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                        @Nullable FirebaseFirestoreException e) {

        if (e != null)
        {
            onLastProductReachedCallback.firebaseErrorException(e);
            return;
        }
        /*
    Now in the onEvent() method, we get the QuerySnapshot object.
    To know which document is changed, we iterate using getDocumentChanges() method to get a
    DocumentChange. According to its type, we create then an Operation object that
    can be passed directly to the setValue() method. This method triggers all the observers
    that are interested in this data. If there are more obervers, all of them will be invoked.
    */
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges())
        {
            switch (documentChange.getType()) {
                case ADDED:
                    Product addedProduct = documentChange.getDocument().toObject(Product.class);
                    Operation addOperation = new Operation(addedProduct, R.string.added);
                    setValue(addOperation);//livedata method
                    break;

                case MODIFIED:
                    Product modifiedProduct = documentChange.getDocument().toObject(Product.class);
                    Operation modifyOperation = new Operation(modifiedProduct, R.string.modified);
                    setValue(modifyOperation);
                    break;

                case REMOVED:
                    Product removedProduct = documentChange.getDocument().toObject(Product.class);
                    Operation removeOperation = new Operation(removedProduct, R.string.removed);
                    setValue(removeOperation);
            }
        }

        int querySnapshotSize = queryDocumentSnapshots.size();
        System.out.println(TAG+"querySnapshotSize="+querySnapshotSize+
                "\tLIMIT="+LIMIT);

        if (querySnapshotSize < LIMIT) {
            onLastProductReachedCallback.setLastProductReached(true);
        } else {
            DocumentSnapshot lastVisibleProduct = queryDocumentSnapshots.
                    getDocuments().get(querySnapshotSize - 1);
            //taking and setting the last visible product

            onLastVisibleProductCallback.setLastVisibleProduct(lastVisibleProduct);
        }
    }

    public interface OnLastVisibleProductCallback {
        void setLastVisibleProduct(DocumentSnapshot lastVisibleProduct);
    }

    public interface OnLastProductReachedCallback {
        void setLastProductReached(boolean isLastProductReached);
        void firebaseErrorException(FirebaseFirestoreException Eception);
    }

}


