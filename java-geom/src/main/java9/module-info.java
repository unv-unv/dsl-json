import org.jspecify.annotations.NullMarked;

@NullMarked
module com.dslplatform.json.awt {
	requires java.desktop;
	requires com.dslplatform.json;
	requires static org.jspecify;

	exports com.dslplatform.json.awt;

	provides com.dslplatform.json.Configuration
			with com.dslplatform.json.awt.ConfigureJavaGeom;
}
