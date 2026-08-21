package com.dslplatform.json.runtime;

import org.jspecify.annotations.Nullable;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;

import java.lang.reflect.AccessibleObject;

class ParanamerParameterNameExtractor implements ParameterNameExtractor {
	private final Paranamer paranamer = new AdaptiveParanamer();

	@Override
	public String @Nullable [] extractNames(AccessibleObject ctorOrMethod) {
		String[] names = paranamer.lookupParameterNames(ctorOrMethod, false);
		return names == Paranamer.EMPTY_NAMES ? null : names;
	}
}