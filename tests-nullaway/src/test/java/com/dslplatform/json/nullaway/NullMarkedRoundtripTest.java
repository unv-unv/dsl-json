package com.dslplatform.json.nullaway;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonAttribute;
import org.jspecify.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NullMarkedRoundtripTest {

	@CompiledJson
	public record Model(
			@JsonAttribute(name = "name") String name,
			@JsonAttribute(name = "comment") @Nullable String comment
	) {
	}

	private final DslJson<Object> dslJson = new DslJson<>();

	@Test
	public void roundtripWithoutNullable() throws IOException {
		Model model = new Model("abc", null);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(model, os);
		Assert.assertEquals("{\"name\":\"abc\"}", os.toString(StandardCharsets.UTF_8));
		Assert.assertEquals(model, deserialize(os.toByteArray()));
	}

	@Test
	public void roundtripWithNullable() throws IOException {
		Model model = new Model("abc", "note");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(model, os);
		Assert.assertEquals("{\"name\":\"abc\",\"comment\":\"note\"}", os.toString(StandardCharsets.UTF_8));
		Assert.assertEquals(model, deserialize(os.toByteArray()));
	}

	private Model deserialize(byte[] json) throws IOException {
		Model result = dslJson.deserialize(Model.class, new ByteArrayInputStream(json));
		Assert.assertNotNull(result);
		return result;
	}
}
