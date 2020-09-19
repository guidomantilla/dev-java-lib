package dev.feather.orm;

import dev.feather.orm.mapper.SpringEntityMapper;
import dev.feather.orm.mapper.SpringSequenceMapper;
import dev.feather.orm.sql.EntityQueryHelper;
import org.apache.ddlutils.PlatformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * The Class SpringPersistenceDAO.
 *
 * @param <E> the element type
 */
public abstract class SpringPersistenceDAO<E> extends AbstractPersistenceDAO<E> {

    /** The logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(SpringPersistenceDAO.class);

    /** The log template. */
    private static String logTemplate = "{}: {} = [ Query: {{}}, Values: {}, Return: {{}}]";

    /** The named parameter jdbc template. */
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /** The row mapper. */
    private RowMapper<E> rowMapper;

    /** The data base type. */
    private DataBaseType dataBaseType;

    /**
     * Instantiates a new spring persistence DAO.
     *
     * @param dataSource the data source
     */
    public SpringPersistenceDAO(DataSource dataSource) {

        super(true);

        initPersistenceDAO(dataSource, new SpringEntityMapper<>(getClazz()));
    }

    /**
     * Instantiates a new spring persistence DAO.
     *
     * @param dataSource the data source
     * @param rowMapper  the row mapper
     */
    public SpringPersistenceDAO(DataSource dataSource, RowMapper<E> rowMapper) {

        this(dataSource);

        initPersistenceDAO(dataSource, rowMapper);
    }

    /**
     * Inits the persistence DAO.
     *
     * @param dataSource the data source
     * @param rowMapper  the row mapper
     */
    private void initPersistenceDAO(DataSource dataSource, RowMapper<E> rowMapper) {

        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.rowMapper = rowMapper;

        String databaseType = new PlatformUtils().determineDatabaseType(dataSource).toLowerCase();
        if (databaseType.startsWith("MySQL".toLowerCase())) {
            this.dataBaseType = DataBaseType.MYSQL;
        }
        if (databaseType.startsWith("Oracle".toLowerCase())) {
            this.dataBaseType = DataBaseType.ORACLE;
        }
        if (databaseType.startsWith("DB2".toLowerCase())) {
            this.dataBaseType = DataBaseType.DB2;
        }
        if (databaseType.startsWith("MsSql".toLowerCase())) {
            this.dataBaseType = DataBaseType.SQLSERVER;
        }
    }

    /**
     * Select all.
     *
     * @return the list
     * @throws RuntimeException the runtime exception
     */
    /*
     * (non-Javadoc)
     *
     * @see ravenware.orm.AbstractPersistenceDAO#selectAll()
     */
    @Override
    protected List<E> selectAll() throws RuntimeException {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        List<E> list = namedParameterJdbcTemplate.query(getSelectAllSQL(), namedParameters, rowMapper);

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "selectAll", getSelectAllSQL(),
                namedParameters.getValues(), list);

        return list;
    }

    /**
     * Select all.
     *
     * @param rowInit the row init
     * @param rowEnd  the row end
     * @return the list
     * @throws RuntimeException the runtime exception
     */
    @Override
    protected List<E> selectAll(int rowInit, int rowEnd) throws RuntimeException {

        String newSQL = "";
        int pageSize = rowEnd - rowInit + 1;
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        switch (dataBaseType) {
            case MYSQL:
                newSQL = getSelectAllSQL() + "LIMIT :rowInit, :pageSize";
                namedParameters.addValue("rowInit", rowInit);
                namedParameters.addValue("pageSize", pageSize);
                break;

            case ORACLE:
                newSQL = "SELECT * FROM (" + getSelectAllSQL() + ") WHERE rownum BETWEEN :rowInit and :rowEnd";
                namedParameters.addValue("rowInit", rowInit);
                namedParameters.addValue("rowEnd", rowEnd);
                break;

            case DB2:
            case SQLSERVER:
            case AS400:
                throw new UnsupportedOperationException("At this time, theres NO SUPPORT for DB2, SQLSERVER or AS400");
        }

        List<E> list = namedParameterJdbcTemplate.query(newSQL, namedParameters, rowMapper);

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "pagedSelectAll", getSelectAllSQL(),
                namedParameters.getValues(), list);

        return list;
    }

    /**
     * Select by.
     *
     * @param sql        the sql
     * @param whereParam the where param
     * @return the list
     * @throws RuntimeException the runtime exception
     */
    /*
     * (non-Javadoc)
     *
     * @see ravenware.orm.AbstractPersistenceDAO#selectBy(java.lang.String,
     * java.util.Map)
     */
    @Override
    protected List<E> selectBy(String sql, Map<String, Object> whereParam) throws RuntimeException {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(whereParam);

        List<E> list = namedParameterJdbcTemplate.query(sql, namedParameters, rowMapper);

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "selectBy", sql, namedParameters.getValues(), list);

        return list;
    }

    /**
     * Select by.
     *
     * @param sql        the sql
     * @param whereParam the where param
     * @param rowInit    the row init
     * @param rowEnd     the row end
     * @return the list
     */
    @Override
    protected List<E> selectBy(String sql, Map<String, Object> whereParam, int rowInit, int rowEnd) {

        String newSQL = "";
        int pageSize = rowEnd - rowInit + 1;

        switch (dataBaseType) {
            case MYSQL:
                newSQL = sql + "LIMIT :rowInit, :pageSize";
                whereParam.put("rowInit", rowInit);
                whereParam.put("pageSize", pageSize);
                break;

            case ORACLE:
                newSQL = "SELECT * FROM (" + sql + ") WHERE rownum BETWEEN :rowInit and :rowEnd";
                whereParam.put("rowInit", rowInit);
                whereParam.put("rowEnd", rowEnd);
                break;

            case DB2:
            case SQLSERVER:
            case AS400:
                throw new UnsupportedOperationException("At this time, theres NO SUPPORT for DB2, SQLSERVER or AS400");
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(whereParam);

        List<E> list = namedParameterJdbcTemplate.query(newSQL, namedParameters, rowMapper);

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "pagedSelectBy", sql, namedParameters.getValues(), list);

        return list;
    }

    /**
     * Select by key.
     *
     * @param keyParam the key param
     * @return the e
     * @throws RuntimeException the runtime exception
     */
    /*
     * (non-Javadoc)
     *
     * @see ravenware.orm.AbstractPersistenceDAO#selectByKey(java.util.Map)
     */
    @Override
    protected E selectByKey(Map<String, Object> keyParam) throws RuntimeException {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(keyParam);

        E e = null;

        List<E> list = namedParameterJdbcTemplate.query(getSelectByKeySQL(), namedParameters, rowMapper);

        if (list.size() == 1) {

            e = list.get(0);

        } else if (!list.isEmpty()) {

            throw new RuntimeException("More than one object retrieved");
        }

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "selectByKey", getSelectByKeySQL(),
                namedParameters.getValues(), e);

        return e;
    }

    /**
     * Select by unique.
     *
     * @param sql        the sql
     * @param whereParam the where param
     * @return the e
     * @throws RuntimeException the runtime exception
     */
    /*
     * (non-Javadoc)
     *
     * @see ravenware.orm.AbstractPersistenceDAO#selectByUnique(java.lang.String,
     * java.util.Map)
     */
    @Override
    protected E selectByUnique(String sql, Map<String, Object> whereParam) throws RuntimeException {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(whereParam);

        E e = null;

        List<E> list = namedParameterJdbcTemplate.query(sql, namedParameters, rowMapper);

        if (list.size() == 1) {

            e = list.get(0);

        } else if (!list.isEmpty()) {

            throw new RuntimeException("More than one object retrieved");
        }

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "selectByUnique", sql, namedParameters.getValues(), e);

        return e;
    }

    /**
     * Retrieve sequence.
     *
     * @param sequenceName the sequence name
     * @return the big decimal
     * @throws RuntimeException the runtime exception
     */
    @Override
    protected BigDecimal retrieveSequence(String sequenceName) throws RuntimeException {

        String sequenceSQL = "";
        switch (dataBaseType) {
            case MYSQL:
                sequenceSQL = "select nextval(':sequenceName')";
                break;

            case ORACLE:
                sequenceSQL = "SELECT :sequenceName.NEXTVAL FROM DUAL";
                break;

            case DB2:
                sequenceSQL = "VALUES NEXT VALUE FOR :sequenceName";
                break;

            case SQLSERVER:
            case AS400:
                throw new UnsupportedOperationException("At this time, theres NO SUPPORT for SQLSERVER or AS400");
        }
        sequenceSQL = sequenceSQL.replace(":sequenceName", sequenceName);

        List<BigDecimal> integerList = getNamedParameterJdbcTemplate().query(sequenceSQL, new SpringSequenceMapper());

        BigDecimal sequence = null;
        if (integerList.size() == 1) {
            sequence = integerList.get(0);
        }

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "retrieveSequence", sequenceSQL, sequenceName, sequence);

        return sequence;
    }

    /**
     * Persist.
     *
     * @param e the e
     * @return true, if successful
     * @throws RuntimeException the runtime exception
     */
    /*
     * (non-Javadoc)
     *
     * @see ravenware.orm.GenericPersistenceDAO#persist(java.lang.Object)
     */
    @Override
    public boolean persist(E e) throws RuntimeException {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(EntityQueryHelper.convert(e));

        int rowsQuantity = namedParameterJdbcTemplate.update(getInsertSQL(), namedParameters);

        boolean inserted = rowsQuantity != 0;

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "persist", getInsertSQL(), namedParameters.getValues(),
                inserted);

        return inserted;
    }

    /**
     * Merge.
     *
     * @param e the e
     * @return true, if successful
     * @throws RuntimeException the runtime exception
     */
    /*
     * (non-Javadoc)
     *
     * @see ravenware.orm.GenericPersistenceDAO#merge(java.lang.Object)
     */
    @Override
    public boolean merge(E e) throws RuntimeException {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(EntityQueryHelper.convert(e));

        int rowsQuantity = namedParameterJdbcTemplate.update(getUpdateSQL(), namedParameters);

        boolean merged = rowsQuantity != 0;

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "merge", getUpdateSQL(), namedParameters.getValues(),
                merged);

        return merged;
    }

    /**
     * Delete.
     *
     * @param e the e
     * @return true, if successful
     * @throws RuntimeException the runtime exception
     */
    /*
     * (non-Javadoc)
     *
     * @see ravenware.orm.GenericPersistenceDAO#delete(java.lang.Object)
     */
    @Override
    public boolean delete(E e) throws RuntimeException {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValues(EntityQueryHelper.convert(e));

        int rowsQuantity = namedParameterJdbcTemplate.update(getDeleteSQL(), namedParameters);

        boolean deleted = rowsQuantity != 0;

        LOGGER.debug(logTemplate, getClazz().getSimpleName(), "delete", getDeleteSQL(), namedParameters.getValues(),
                deleted);

        return deleted;
    }

    /**
     * Gets the named parameter jdbc template.
     *
     * @return the named parameter jdbc template
     */
    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {

        return namedParameterJdbcTemplate;
    }

    /**
     * Gets the row mapper.
     *
     * @return the row mapper
     */
    protected RowMapper<E> getRowMapper() {

        return rowMapper;
    }

    /**
     * Gets the Data Base Type.
     *
     * @return the Data Base Type
     */
    protected DataBaseType getDataBaseType() {

        return dataBaseType;
    }
}
