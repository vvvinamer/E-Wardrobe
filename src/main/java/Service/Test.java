package Service;

import Utils.Utils;
import com.google.firebase.database.utilities.Pair;
import dao.FirebaseDao;
import dao.FirebaseDaoImpl;
import model.FirestoreDocument;
import model.Product;

import java.util.Arrays;
import java.util.List;

public class Test {

    private static FirebaseDao firebaseDao;

    public static void main(String[] args) throws Exception {

        firebaseDao = new FirebaseDaoImpl();

        Product sample = Product.builder()
          .brandName("Skechers")
          .category("Shoes")
          .price(5500L)
          .ownerId("vvvinamer")
          .build();


//        System.out.println(firebaseDao.saveDocumentForCollection("Product", sample));
        List<Product> products = firebaseDao.getAllDocumentsForCollection("Product",
                                                                          Product.class,
                                                                          Arrays.asList(new Pair<>(
                                                                            "category", "Shoes")));
        products.forEach(System.out::println);
    }

}
