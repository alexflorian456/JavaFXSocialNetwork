package Repository;

import Domain.Entity;
import Exceptions.RepoException;
import Exceptions.ValidationException;

import java.util.List;

public interface Repository<E extends Entity> {
    E findID(int id);
    E findID(int id1,int id2);
    List<E> getAll();
    E add(E e) throws RepoException, ValidationException;
    E remove(E e) throws RepoException, ValidationException;
    E update(E e);
}
