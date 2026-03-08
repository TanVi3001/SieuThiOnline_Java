package business.sql;

import java.util.ArrayList;

public interface SqlInterface<T> {
    public int insert(T t);
    public int update(T t);
    public int delete(String id); 
    public ArrayList<T> selectAll();
    public T selectById(String id);
}