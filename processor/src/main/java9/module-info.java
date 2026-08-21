import org.jspecify.annotations.NullMarked;

@NullMarked
module com.dslplatform.json.processor {
	requires java.compiler;
	requires com.dslplatform.json;
	requires static org.jspecify;

	exports com.dslplatform.json.processor;

	provides javax.annotation.processing.Processor
			with com.dslplatform.json.processor.CompiledJsonAnnotationProcessor;
}
