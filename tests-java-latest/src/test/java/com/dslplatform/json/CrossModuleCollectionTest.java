package com.dslplatform.json;

import dsljson.tests.api.Label;
import dsljson.tests.api.Status;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrossModuleCollectionTest {

	@CompiledJson
	public static class Place {
		public Status status = Status.ACTIVE;
		public Label label = new Label("l");
		public boolean enabled = true;
	}

	@CompiledJson
	public static class MapHolder {
		public Map<String, Place> places = new LinkedHashMap<>();
	}

	@CompiledJson
	public static class ListHolder {
		public List<Place> places = new ArrayList<>();
	}

	@CompiledJson
	public static class SetHolder {
		public Set<Place> places = new LinkedHashSet<>();
	}

	@CompiledJson
	public static class ArrayHolder {
		public Place[] places = new Place[0];
	}

	@CompiledJson
	public static class EnumKeyHolder {
		public Map<Status, Place> places = new LinkedHashMap<>();
	}

	@CompiledJson
	public static class EnumValueHolder {
		public Map<String, Status> statuses = new LinkedHashMap<>();
	}

	@CompiledJson
	public static class NestedHolder {
		public Map<String, MapHolder> nested = new LinkedHashMap<>();
	}

	private final DslJson<Object> dslJson = new DslJson<>();

	private static Place place(Status status, String label) {
		Place place = new Place();
		place.status = status;
		place.label = new Label(label);
		place.enabled = false;
		return place;
	}

	private <T> T roundtrip(T value, Class<T> manifest) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(value, os);
		return dslJson.deserialize(manifest, new ByteArrayInputStream(os.toByteArray()));
	}

	private String serialize(Object value) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(value, os);
		return os.toString(StandardCharsets.UTF_8);
	}

	@Test
	public void mapOfStructReferencingCrossModuleTypes() throws IOException {
		MapHolder holder = new MapHolder();
		holder.places.put("Default", place(Status.INACTIVE, "first"));
		holder.places.put("Other", place(Status.ACTIVE, "second"));

		MapHolder result = roundtrip(holder, MapHolder.class);

		Assert.assertEquals(2, result.places.size());
		Assert.assertEquals(Status.INACTIVE, result.places.get("Default").status);
		Assert.assertEquals("first", result.places.get("Default").label.text);
		Assert.assertEquals(Status.ACTIVE, result.places.get("Other").status);
		Assert.assertEquals("second", result.places.get("Other").label.text);
		Assert.assertFalse(result.places.get("Default").enabled);
		Assert.assertEquals(serialize(holder), serialize(result));
	}

	@Test
	public void listOfStructReferencingCrossModuleTypes() throws IOException {
		ListHolder holder = new ListHolder();
		holder.places.add(place(Status.INACTIVE, "only"));

		ListHolder result = roundtrip(holder, ListHolder.class);

		Assert.assertEquals(1, result.places.size());
		Assert.assertEquals(Status.INACTIVE, result.places.get(0).status);
		Assert.assertEquals("only", result.places.get(0).label.text);
		Assert.assertEquals(serialize(holder), serialize(result));
	}

	@Test
	public void setOfStructReferencingCrossModuleTypes() throws IOException {
		SetHolder holder = new SetHolder();
		holder.places.add(place(Status.ACTIVE, "only"));

		SetHolder result = roundtrip(holder, SetHolder.class);

		Assert.assertEquals(1, result.places.size());
		Assert.assertEquals("only", result.places.iterator().next().label.text);
		Assert.assertEquals(serialize(holder), serialize(result));
	}

	@Test
	public void arrayOfStructReferencingCrossModuleTypes() throws IOException {
		ArrayHolder holder = new ArrayHolder();
		holder.places = new Place[]{place(Status.ACTIVE, "a"), place(Status.INACTIVE, "b")};

		ArrayHolder result = roundtrip(holder, ArrayHolder.class);

		Assert.assertEquals(2, result.places.length);
		Assert.assertEquals("a", result.places[0].label.text);
		Assert.assertEquals(Status.INACTIVE, result.places[1].status);
		Assert.assertEquals(serialize(holder), serialize(result));
	}

	@Test
	public void mapWithCrossModuleEnumKeyAndStructValue() throws IOException {
		EnumKeyHolder holder = new EnumKeyHolder();
		holder.places.put(Status.ACTIVE, place(Status.ACTIVE, "a"));
		holder.places.put(Status.INACTIVE, place(Status.INACTIVE, "b"));

		EnumKeyHolder result = roundtrip(holder, EnumKeyHolder.class);

		Assert.assertEquals(2, result.places.size());
		Assert.assertEquals("a", result.places.get(Status.ACTIVE).label.text);
		Assert.assertEquals("b", result.places.get(Status.INACTIVE).label.text);
		Assert.assertEquals(serialize(holder), serialize(result));
	}

	@Test
	public void mapOfCrossModuleEnumValues() throws IOException {
		EnumValueHolder holder = new EnumValueHolder();
		holder.statuses.put("a", Status.ACTIVE);
		holder.statuses.put("b", Status.INACTIVE);

		EnumValueHolder result = roundtrip(holder, EnumValueHolder.class);

		Assert.assertEquals(Status.ACTIVE, result.statuses.get("a"));
		Assert.assertEquals(Status.INACTIVE, result.statuses.get("b"));
		Assert.assertEquals(serialize(holder), serialize(result));
	}

	@Test
	public void mapOfStructWhichItselfHoldsAMapOfStructs() throws IOException {
		MapHolder inner = new MapHolder();
		inner.places.put("Default", place(Status.INACTIVE, "deep"));

		NestedHolder holder = new NestedHolder();
		holder.nested.put("outer", inner);

		NestedHolder result = roundtrip(holder, NestedHolder.class);

		Assert.assertEquals(1, result.nested.size());
		Assert.assertEquals("deep", result.nested.get("outer").places.get("Default").label.text);
		Assert.assertEquals(serialize(holder), serialize(result));
	}

	@Test
	public void collectionsOfCrossModuleStructAreWrittenWithoutRuntimeConversion() throws IOException {
		MapHolder holder = new MapHolder();
		holder.places.put("Default", place(Status.ACTIVE, "x"));

		Assert.assertEquals(
				"{\"places\":{\"Default\":{\"status\":\"ACTIVE\",\"enabled\":false,\"label\":{\"text\":\"x\"}}}}",
				serialize(holder));
	}

	@Test
	public void listsOfCrossModuleStructSurviveEmptyAndNull() throws IOException {
		ListHolder holder = new ListHolder();
		holder.places = new ArrayList<>();

		ListHolder empty = roundtrip(holder, ListHolder.class);
		Assert.assertTrue(empty.places.isEmpty());

		holder.places = Arrays.asList(place(Status.ACTIVE, "a"));
		ListHolder single = roundtrip(holder, ListHolder.class);
		Assert.assertEquals(1, single.places.size());
	}
}
