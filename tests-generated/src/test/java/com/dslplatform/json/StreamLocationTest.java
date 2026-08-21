package com.dslplatform.json;

import com.dslplatform.json.generated.GA0A0Lc;
import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class StreamLocationTest {

	@Test
	public void streamLocationIssue() throws IOException {
		DslJson<Object> dslJson = new DslJson<Object>();
		String json = "{\"URI\":\"a94e4da8-0d14-48f2-97b3-045359aafad5\",\"ID\":\"a94e4da8-0d14-48f2-97b3-045359aafad5\",\"gE0A0Lc\":{\"URI\":\"cb172e84-60dd-4d17-b80f-372e5452fc9f\",\"p0A0Lc\":[null,{\"X\":0.0,\"Y\":0.0},{\"X\":-2.147483648E9,\"Y\":2.147483647E9},{\"X\":-1.0E9,\"Y\":1.0E9},{\"X\":1.401298464324817E-45,\"Y\":3.4028234663852886E38},{\"X\":-1.0000001192092896,\"Y\":1.0000001192092896},{\"X\":-2.000000000000345,\"Y\":1.000000000000234}],\"GA0A0LcID\":\"cb172e84-60dd-4d17-b80f-372e5452fc9f\"}}";
		byte[] bytes = json.getBytes("UTF-8");
		byte[] buffer = new byte[8192];
		GA0A0Lc deser = dslJson.deserialize(GA0A0Lc.class, new ByteArrayInputStream(bytes), buffer);

		Point2D[] points = deser.getGE0A0Lc().getP0A0Lc();
		Assert.assertEquals(7, points.length);
		Assert.assertEquals(1.000000000000234d, points[6].getY(), 0);
	}
}
