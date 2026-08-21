import org.jspecify.annotations.NullMarked;

@NullMarked
module com.dslplatform.json.xml {
	requires java.xml;
	requires com.dslplatform.json;
	requires static org.jspecify;

	exports com.dslplatform.json.xml;

	provides com.dslplatform.json.Configuration
			with com.dslplatform.json.xml.ConfigureJavaXml;
}
