package com.firebase.productlistmvvmfirestore.ViewModal;

import androidx.lifecycle.ViewModel;

import com.firebase.productlistmvvmfirestore.ProductListLiveData;
import com.firebase.productlistmvvmfirestore.Repository.FirestoreProductListRepository;

public class ProductListViewModel extends ViewModel {
    //storing the reference of FirestoreProductListRepository in interface productListRepository
    //as this class is implementing the interface ProductListRepository
    private ProductListRepository productListRepository = new FirestoreProductListRepository();

    public ProductListLiveData getProductListLiveData() {
        return productListRepository.getProductListLiveData();
        //calling the method getProductListLiveData() of type ProductListLiveData
        // from FirestoreProductListRepository
    }

    public interface ProductListRepository {
        //this interface return the object of ProductListLiveData
        ProductListLiveData getProductListLiveData();
    }


    /*public class OverridenClass
    for reference to understanding above code as interface
{
    public static void main(String[] args)
    {
     Pritable objParent = new Parent();
     objParent.sysout();
     objParent.displayName();
    }
}

interface Pritable
{
    void sysout();
}

class Parent implements Pritable
{
    public void displayName()
    {
     System.out.println("This is Parent Name");
    }

    public void sysout()
    {
        System.out.println("I am Printable Interfacein Parent Class");
    }
}*/


}
