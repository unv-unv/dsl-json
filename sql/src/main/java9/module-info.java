import org.jspecify.annotations.NullMarked;

@NullMarked
module com.dslplatform.json.sql {
	requires java.sql;
	requires com.dslplatform.json;
	requires static org.jspecify;

	exports com.dslplatform.json.sql;

	provides com.dslplatform.json.Configuration
			with com.dslplatform.json.sql.ConfigureJavaSql;
}
