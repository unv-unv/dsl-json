package com.dslplatform.json.sql;

import com.dslplatform.json.*;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class ConfigureJavaSql implements Configuration {

	private static final JsonReader.ReadObject<Date> SQL_DATE_READER = new JsonReader.ReadObject<Date>() {
		@Override
		public @Nullable Date read(JsonReader reader) throws IOException {
			return reader.wasNull() ? null : Date.valueOf(JavaTimeConverter.deserializeLocalDate(reader));
		}
	};

	private static final JsonReader.ReadObject<Timestamp> SQL_TIMESTAMP_READER = new JsonReader.ReadObject<Timestamp>() {
		@Override
		public @Nullable Timestamp read(JsonReader reader) throws IOException {
			return reader.wasNull() ? null : Timestamp.from(JavaTimeConverter.deserializeDateTime(reader).toInstant());
		}
	};

	@Override
	public void configure(DslJson json) {
		register(json);
	}

	private static <T> void register(DslJson<T> json) {
		json.registerWriter(ResultSet.class, new ResultSetConverter(json));
		json.registerReader(Date.class, SQL_DATE_READER);
		json.registerWriter(Date.class, (writer, value) -> {
			if (value == null) writer.writeNull();
			else JavaTimeConverter.serialize(value.toLocalDate(), writer);
		});
		json.registerReader(Timestamp.class, SQL_TIMESTAMP_READER);
		json.registerWriter(Timestamp.class, (writer, value) -> {
			if (value == null) writer.writeNull();
			else JavaTimeConverter.serialize(OffsetDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()), writer);
		});
		// java.sql.Date instances handed over as java.util.Date must still serialize as date-only
		json.registerWriter(java.util.Date.class, (writer, value) -> {
			if (value == null) writer.writeNull();
			else if (value instanceof Date) JavaTimeConverter.serialize(((Date) value).toLocalDate(), writer);
			else JavaTimeConverter.serialize(OffsetDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()), writer);
		});
	}
}
