package dev.feather.orm.mapper;

import dev.feather.orm.sql.EntityQueryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Class SpringEntityMapper.
 *
 * @param <E> the element type
 */
public class SpringEntityMapper<E> implements RowMapper<E> {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringEntityMapper.class);

    /** The clazz. */
    private final Class<E> clazz;

    /**
     * Instantiates a new spring entity mapper.
     *
     * @param clazz the clazz
     */
    public SpringEntityMapper(Class<E> clazz) {

        this.clazz = clazz;
    }

    /**
     * Map row.
     *
     * @param resultSet the result set
     * @param rowNumber the row number
     * @return the e
     * @throws SQLException the SQL exception
     */
    @Override
    public E mapRow(ResultSet resultSet, int rowNumber) throws SQLException {

        E entity = null;

        try {

            entity = EntityQueryHelper.convert(clazz, resultSet);

        } catch (RuntimeException e) {

            LOGGER.error(e.getMessage(), e);
        }

        return entity;
    }
}

