import org.jspecify.annotations.NullMarked;

@NullMarked
module com.dslplatform.json {
	requires static org.jspecify;
	requires static paranamer;

	exports com.dslplatform.json;
	exports com.dslplatform.json.internal;
	exports com.dslplatform.json.runtime;
	exports dsl_json.java.util;

	uses com.dslplatform.json.Configuration;
}
