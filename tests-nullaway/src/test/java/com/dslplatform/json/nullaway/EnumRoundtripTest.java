package com.dslplatform.json.nullaway;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonAttribute;
import com.dslplatform.json.ParsingException;
import org.jspecify.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EnumRoundtripTest {

	@CompiledJson
	public enum Status {
		ACTIVE,
		INACTIVE,
		PENDING
	}

	@CompiledJson
	public record Task(
			@JsonAttribute(name = "status") Status status,
			@JsonAttribute(name = "previous") @Nullable Status previous
	) {
	}

	private final DslJson<Object> dslJson = new DslJson<>();

	@Test
	public void roundtripWithNullableUnset() throws IOException {
		Task task = new Task(Status.ACTIVE, null);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(task, os);
		Assert.assertEquals("{\"status\":\"ACTIVE\",\"previous\":null}", os.toString(StandardCharsets.UTF_8));
		Assert.assertEquals(task, deserialize(os.toByteArray()));
	}

	@Test
	public void roundtripWithNullableSet() throws IOException {
		Task task = new Task(Status.PENDING, Status.INACTIVE);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(task, os);
		Assert.assertEquals("{\"status\":\"PENDING\",\"previous\":\"INACTIVE\"}", os.toString(StandardCharsets.UTF_8));
		Assert.assertEquals(task, deserialize(os.toByteArray()));
	}

	@Test
	public void enumRoundtripsStandalone() throws IOException {
		for (Status status : Status.values()) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			dslJson.serialize(status, os);
			Assert.assertEquals('"' + status.name() + '"', os.toString(StandardCharsets.UTF_8));
			Assert.assertEquals(status, dslJson.deserialize(Status.class, new ByteArrayInputStream(os.toByteArray())));
		}
	}

	@Test
	public void nullIntoNonNullEnumIsRejected() {
		byte[] json = "{\"status\":null}".getBytes(StandardCharsets.UTF_8);
		ParsingException ex = Assert.assertThrows(
				ParsingException.class,
				() -> dslJson.deserialize(Task.class, new ByteArrayInputStream(json)));
		Assert.assertTrue(String.valueOf(ex.getMessage()).contains("Property 'status' is not allowed to be null"));
	}

	@Test
	public void unknownEnumValueIsRejected() {
		byte[] json = "{\"status\":\"NOPE\"}".getBytes(StandardCharsets.UTF_8);
		// generated converter falls through to Enum.valueOf, so this is not wrapped into ParsingException
		Assert.assertThrows(
				IllegalArgumentException.class,
				() -> dslJson.deserialize(Task.class, new ByteArrayInputStream(json)));
	}

	private Task deserialize(byte[] json) throws IOException {
		Task result = dslJson.deserialize(Task.class, new ByteArrayInputStream(json));
		Assert.assertNotNull(result);
		return result;
	}
}
