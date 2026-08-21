import org.jspecify.annotations.NullMarked;

@NullMarked
module com.dslplatform.json.jsonb {
	requires com.dslplatform.json;
	requires java.json.bind;
	requires static java.json;
	requires static org.jspecify;

	exports com.dslplatform.json.jsonb;

	provides javax.json.bind.spi.JsonbProvider
			with com.dslplatform.json.jsonb.DslJsonbProvider;
}
