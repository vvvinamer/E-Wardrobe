package dao;

import com.google.firebase.database.utilities.Pair;
import model.FirestoreDocument;

import java.util.List;

public interface FirebaseDao {

    public <T extends FirestoreDocument> T getDocumentById(String collectionName, String documentId,
                                                           Class<T> t) throws Exception;

    public <T> List<T> getAllDocumentsForCollection(String collectionName, Class<T> cls,
                                                   List<Pair<String, Object>> filters) throws Exception;

    public <T extends FirestoreDocument> String saveDocumentForCollection(String collectionName, T document)
      throws Exception;
}
