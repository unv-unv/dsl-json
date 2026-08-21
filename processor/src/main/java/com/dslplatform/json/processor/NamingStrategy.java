package com.dslplatform.json.processor;

import org.jspecify.annotations.Nullable;

import java.util.Map;

public interface NamingStrategy {
	@Nullable
	Map<String, String> prepareNames(Map<String, AttributeInfo> attributes);
}
