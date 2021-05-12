package Service;

import dao.FirebaseService;
import dao.FirebaseServiceImpl;
import model.Product;

import java.util.List;

public class Test {

    private static FirebaseService firebaseService;

    public static void main(String[] args) throws Exception {

        firebaseService = new FirebaseServiceImpl();
        List<Product> products = firebaseService.getAll2("Product", new Product());
        products.forEach(System.out::println);
    }
}
