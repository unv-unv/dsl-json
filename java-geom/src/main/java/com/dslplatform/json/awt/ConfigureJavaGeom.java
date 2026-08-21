package com.dslplatform.json.awt;

import com.dslplatform.json.*;

public class ConfigureJavaGeom implements Configuration {
	@Override
	public void configure(DslJson json) {
		JavaGeomConverter.registerDefault(json);
	}
}
