package com.dslplatform.json;

import org.jspecify.annotations.Nullable;

public class SerializationException extends RuntimeException {
	public SerializationException(@Nullable String reason) {
		super(reason);
	}

	public SerializationException(@Nullable Throwable cause) {
		super(cause);
	}

	public SerializationException(@Nullable String reason, @Nullable Throwable cause) {
		super(reason, cause);
	}
}
