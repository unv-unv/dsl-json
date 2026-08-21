package com.dslplatform.json;

import com.dslplatform.json.xml.ConfigureJavaXml;

import dsl_json.android.graphics.*;

public class ConfigureAndroid implements Configuration {
	@Override
	public void configure(DslJson json) {
		new PointFDslJsonConverter().configure(json);
		new PointDslJsonConverter().configure(json);
		new RectDslJsonConverter().configure(json);
		new BitmapDslJsonConverter().configure(json);
		new ConfigureJavaXml().configure(json);
	}
}
