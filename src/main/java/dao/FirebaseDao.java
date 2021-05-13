package dao;

import model.FirestoreDocument;

import java.util.List;

public interface FirebaseDao {

    public <T extends FirestoreDocument> T getDocumentById(String collectionName, String documentId,
                                                           Class<T> t) throws Exception;

    public <T> List<T> getAllDocumentsByCollection(String collectionName, Class<T> cls) throws Exception;

    public <T extends FirestoreDocument> String saveDocumentForCollection(String collectionName, T document)
      throws Exception;
}
