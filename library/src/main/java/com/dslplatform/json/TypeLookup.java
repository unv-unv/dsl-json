package com.dslplatform.json;

import org.jspecify.annotations.Nullable;

interface TypeLookup {
	<T> JsonReader.@Nullable ReadObject<T> tryFindReader(Class<T> manifest);
	<T> JsonReader.@Nullable BindObject<T> tryFindBinder(Class<T> manifest);
}
