package com.dslplatform.json.sql;

import com.dslplatform.json.*;

import com.dslplatform.json.runtime.Settings;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SqlDateTest {

	public static class SqlDate {
		public java.util.Date date;
	}

	@Test
	public void sqlDateWillNotExplode() throws IOException {
		java.util.Date ud = new java.util.Date(119, 2, 10);
		SqlDate sql = new SqlDate();
		sql.date = new java.sql.Date(ud.getTime());
		DslJson<Object> dslJson = new DslJson<>(Settings.withRuntime().includeServiceLoader());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(sql, os);
		Assert.assertEquals("{\"date\":\"2019-03-10\"}", os.toString());
	}
}
