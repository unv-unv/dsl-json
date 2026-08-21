package com.dslplatform.json;

import org.jspecify.annotations.Nullable;

import java.io.IOException;

interface UnknownSerializer {
	void serialize(JsonWriter writer, @Nullable Object unknown) throws IOException;
}
