package dao;
import Utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import model.FirestoreDocument;
import model.Product;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FirebaseDaoImpl implements FirebaseDao {

    private static Firestore db;

    public FirebaseDaoImpl() throws IOException {
        // Use a service account
        InputStream serviceAccount = new FileInputStream("src/main/resources/static/Ewardrobe_key.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(credentials)
          .build();
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    @Override
    public <T extends FirestoreDocument> T getDocumentById(String collectionName, String documentId,
                                                           Class<T> cls)
      throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collectionName).document(documentId);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        return document.toObject(cls);
    }

    @Override
    public <T> List<T> getAllDocumentsByCollection(String collectionName, Class<T> cls) throws
      ExecutionException, InterruptedException {
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> query = db.collection(collectionName).get();
        QuerySnapshot querySnapshot = query.get();
        List<T> docs = new ArrayList<>();

        T obj;

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println(document.getId());
            obj = document.toObject(cls);
            docs.add(obj);
        }
        return docs;
    }

//    @TODO whenever a new Document(Product is created), it would be added in User Docs
    @Override
    public <T extends FirestoreDocument> String saveDocumentForCollection(String collectionName, T document)
      throws Exception {

        String documentId = Utils.generateDocId();
        DocumentReference docRef = db.collection(collectionName).document(documentId);
        ApiFuture<WriteResult> result = docRef.set(document);

        if (Product.class.equals(document.getClass())) {
            addProductForUser(((Product)document).getOwnerId(), documentId);
        }
        return result.get().getUpdateTime().toString();
    }


    private void addProductForUser(String ownerId, String productId) throws ExecutionException,
                                                                               InterruptedException {
        DocumentReference docRef = db.collection("User")
                                     .document(ownerId)
                                     .collection("product")
                                     .document(productId);
        ApiFuture<WriteResult> result = docRef.set(new HashMap<>());
    }


}
