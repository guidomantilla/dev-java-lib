package dev.feather.orm.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Class SpringSequenceMapper.
 */
public class SpringSequenceMapper implements RowMapper<BigDecimal> {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSequenceMapper.class);

    /**
     * Map row.
     *
     * @param resultSet the result set
     * @param rowNum    the row num
     * @return the big decimal
     * @throws SQLException the SQL exception
     */
    @Override
    public BigDecimal mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        BigDecimal sequence = null;

        try {

            sequence = new BigDecimal(resultSet.getString(1));

        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return sequence;
    }
}
