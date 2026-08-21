package com.dslplatform.json.xml;

import com.dslplatform.json.*;

public class ConfigureJavaXml implements Configuration {
	@Override
	public void configure(DslJson json) {
		XmlConverter.registerDefault(json);
	}
}
