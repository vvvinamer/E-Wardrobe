package dao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FirebaseService {

    public void getById(String collectionName, String productId) throws Exception;

    public void getAll(String collectionName) throws Exception;

    public <T> List<T> getAll2(String colllectionName, T dummy) throws Exception;
}
