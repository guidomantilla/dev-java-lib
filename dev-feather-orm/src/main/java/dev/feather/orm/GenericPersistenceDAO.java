package dev.feather.orm;

/**
 * The Interface GenericPersistenceDAO.
 *
 * @param <E> the element type
 */
public interface GenericPersistenceDAO<E> {

    /**
     * Persist.
     *
     * @param e the e
     * @return true, if successful
     * @throws RuntimeException the runtime exception
     */
    boolean persist(E e) throws RuntimeException;

    /**
     * Merge.
     *
     * @param e the e
     * @return true, if successful
     * @throws RuntimeException the runtime exception
     */
    boolean merge(E e) throws RuntimeException;

    /**
     * Delete.
     *
     * @param e the e
     * @return true, if successful
     * @throws RuntimeException the runtime exception
     */
    boolean delete(E e) throws RuntimeException;

}
