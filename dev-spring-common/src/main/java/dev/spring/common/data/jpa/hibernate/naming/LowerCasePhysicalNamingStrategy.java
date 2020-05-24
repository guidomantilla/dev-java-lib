package dev.spring.common.data.jpa.hibernate.naming;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.io.Serializable;

public class LowerCasePhysicalNamingStrategy implements PhysicalNamingStrategy, Serializable {
    /**
     * Singleton access
     */
    public static final LowerCasePhysicalNamingStrategy INSTANCE = new LowerCasePhysicalNamingStrategy();

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return toLowerCase(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return toLowerCase(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return toLowerCase(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return toLowerCase(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return toLowerCase(name);
    }

    private Identifier toLowerCase(Identifier name) {
        if (name == null)
            return null;
        if (name.isQuoted())
            return name;
        String text = name.getText().toLowerCase();
        return Identifier.toIdentifier(text);
    }
}
