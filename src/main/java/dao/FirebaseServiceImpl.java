package dao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class FirebaseServiceImpl implements FirebaseService{

    private static Firestore db;


    public FirebaseServiceImpl() throws IOException {


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
    public void getById(String collectionName, String productId) {

    }

    @Override
    public void getAll(String collectionName) throws ExecutionException, InterruptedException {
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> query = db.collection(collectionName).get();
        QuerySnapshot querySnapshot = query.get();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("firstName"));
            if (document.contains("middleName")) {
                System.out.println("Middle: " + document.getString("middleName"));
            }
            System.out.println("Last: " + document.getString("lastName"));
        }
    }

    @Override
    public <T> List<T> getAll2(String collectionName, T dummy) throws ExecutionException, InterruptedException {
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> query = db.collection(collectionName).get();
        QuerySnapshot querySnapshot = query.get();
        List<T> docs = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        Object obj;

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println();
            Map<String, Object> data = document.getData();
            obj = mapper.convertValue(data, dummy.getClass());
            docs.add((T)obj);
        }
        return docs;
    }

}
