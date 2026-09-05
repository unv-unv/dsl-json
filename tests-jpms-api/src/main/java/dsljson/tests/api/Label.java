package dsljson.tests.api;

import com.dslplatform.json.CompiledJson;

@CompiledJson
public class Label {
	public String text;

	public Label() {
	}

	public Label(String text) {
		this.text = text;
	}
}
