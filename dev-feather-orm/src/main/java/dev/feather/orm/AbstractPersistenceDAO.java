package dev.feather.orm;


import dev.feather.orm.sql.EntityQueryHelper;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * The Class AbstractPersistenceDAO.
 *
 * @param <E> the element type
 */
public abstract class AbstractPersistenceDAO<E> implements GenericPersistenceDAO<E> {

    /** The insert SQL. */
    private final String insertSQL;

    /** The update SQL. */
    private final String updateSQL;

    /** The delete SQL. */
    private final String deleteSQL;

    /** The select all SQL. */
    private final String selectAllSQL;

    /** The select by key SQL. */
    private final String selectByKeySQL;

    /** The clazz. */
    private final Class<E> clazz;

    /**
     * Instantiates a new abstract persistence DAO.
     *
     * @param namedParameter the named parameter
     */
    @SuppressWarnings("unchecked")
    protected AbstractPersistenceDAO(boolean namedParameter) {

        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();

        this.clazz = (Class<E>) genericSuperclass.getActualTypeArguments()[0];

        this.insertSQL = EntityQueryHelper.createInsertSQL(clazz, namedParameter);
        this.updateSQL = EntityQueryHelper.createUpdateSQL(clazz, namedParameter);
        this.deleteSQL = EntityQueryHelper.createDeleteSQL(clazz, namedParameter);
        this.selectAllSQL = EntityQueryHelper.createSelectAllSQL(clazz);
        this.selectByKeySQL = EntityQueryHelper.createSelectByKeySQL(clazz, namedParameter);
    }

    /**
     * Select all.
     *
     * @return the list
     * @throws RuntimeException the runtime exception
     */
    protected abstract List<E> selectAll() throws RuntimeException;

    /**
     * Select all.
     *
     * @param rowInit the row init
     * @param rowEnd  the row end
     * @return the list
     * @throws RuntimeException the runtime exception
     */
    protected abstract List<E> selectAll(int rowInit, int rowEnd) throws RuntimeException;

    /**
     * Select by.
     *
     * @param sql        the sql
     * @param whereParam the where param
     * @return the list
     * @throws RuntimeException the runtime exception
     */
    protected abstract List<E> selectBy(String sql, Map<String, Object> whereParam) throws RuntimeException;

    /**
     * Select by.
     *
     * @param sql        the sql
     * @param whereParam the where param
     * @param rowInit    the row init
     * @param rowEnd     the row end
     * @return the list
     * @throws RuntimeException the runtime exception
     */
    protected abstract List<E> selectBy(String sql, Map<String, Object> whereParam, int rowInit, int rowEnd)
            throws RuntimeException;

    /**
     * Select by key.
     *
     * @param keyParam the key param
     * @return the e
     * @throws RuntimeException the runtime exception
     */
    protected abstract E selectByKey(Map<String, Object> keyParam) throws RuntimeException;

    /**
     * Select by unique.
     *
     * @param sql        the sql
     * @param whereParam the where param
     * @return the e
     * @throws RuntimeException the runtime exception
     */
    protected abstract E selectByUnique(String sql, Map<String, Object> whereParam) throws RuntimeException;

    /**
     * Retrieve sequence.
     *
     * @param sequenceName the sequence name
     * @return the big decimal
     * @throws RuntimeException the runtime exception
     */
    protected abstract BigDecimal retrieveSequence(String sequenceName) throws RuntimeException;

    /**
     * Gets the insert sql.
     *
     * @return the insert sql
     */
    protected String getInsertSQL() {

        return insertSQL;
    }

    /**
     * Gets the update sql.
     *
     * @return the update sql
     */
    protected String getUpdateSQL() {

        return updateSQL;
    }

    /**
     * Gets the delete sql.
     *
     * @return the delete sql
     */
    protected String getDeleteSQL() {

        return deleteSQL;
    }

    /**
     * Gets the select all sql.
     *
     * @return the select all sql
     */
    protected String getSelectAllSQL() {

        return selectAllSQL;
    }

    /**
     * Gets the select by key sql.
     *
     * @return the select by key sql
     */
    protected String getSelectByKeySQL() {

        return selectByKeySQL;
    }

    /**
     * Gets the clazz.
     *
     * @return the clazz
     */
    protected Class<E> getClazz() {

        return clazz;
    }
}
