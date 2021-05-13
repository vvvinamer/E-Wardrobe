package dao;

import Utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.utilities.Pair;
import model.FirestoreDocument;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public <T> List<T> getAllDocumentsForCollection(String collectionName, Class<T> cls,
                                                   List<Pair<String, Object>> filters) throws
      ExecutionException, InterruptedException {
        // asynchronously retrieve all documents
        CollectionReference collectionReference = db.collection(collectionName);
        Query query1;
        ApiFuture<QuerySnapshot> query = collectionReference.get();


        if(Objects.nonNull(filters)){
            query1 = collectionReference.whereEqualTo(filters.get(0).getFirst(), filters.get(0).getSecond());
            for(int i = 1; i < filters.size(); i++) {
                query1 = query1.whereEqualTo(filters.get(i).getFirst(), filters.get(i).getSecond());
            }
            query = query1.get();
        }

        QuerySnapshot querySnapshot = query.get();
        List<T> docs = new ArrayList<>();

        T obj;

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            obj = document.toObject(cls);
            docs.add(obj);
        }
        return docs;
    }

    @Override
    public <T extends FirestoreDocument> String saveDocumentForCollection(String collectionName, T document)
      throws Exception {

        String documentId = Utils.generateDocId();
        DocumentReference docRef = db.collection(collectionName).document(documentId);
        ApiFuture<WriteResult> result = docRef.set(document);
        return result.get().getUpdateTime().toString();
    }




}
