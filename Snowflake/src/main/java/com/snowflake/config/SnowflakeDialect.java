package com.snowflake.config;

import java.sql.Types;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

public class SnowflakeDialect extends Dialect {

	public SnowflakeDialect() {

		super();

		registerColumnType(Types.VARCHAR, 4000, "varchar($l)");

		registerColumnType(Types.CHAR, "char($l)");

		registerColumnType(Types.INTEGER, "integer");

		registerColumnType(Types.BIGINT, "bigint");

		registerColumnType(Types.FLOAT, "float");

		registerColumnType(Types.DOUBLE, "double");

		registerColumnType(Types.DECIMAL, "decimal($p,$s)");

		registerColumnType(Types.BOOLEAN, "boolean");

	}

	@Override

	public boolean supportsLimit() {

		return true;

	}

	@Override

	public String getAddColumnString() {

		return "ADD COLUMN";

	}

	@Override

	public IdentityColumnSupport getIdentityColumnSupport() {

		return new SnowflakeIdentityColumnSupport();

	}

	public static class SnowflakeIdentityColumnSupport extends IdentityColumnSupportImpl {

		@Override

		public boolean supportsIdentityColumns() {

			return true;

		}

		@Override

		public String getIdentitySelectString(String table, String column, int type) {

			return "select last_insert_id()";

		}

		@Override

		public String getIdentityColumnString(int type) {

			return "integer not null autoincrement";

		}

	}

}
