package com.dslplatform.json.runtime;

public abstract class BeanNaming {

	public static String beanOrActualName(String name, boolean isBoolean) {

		if (isBoolean && name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2))) {
			String after = name.substring(2);
			if (name.length() == 3) return after.toLowerCase();
			return after.toUpperCase().equals(after)
					? after
					: Character.toLowerCase(after.charAt(0)) + after.substring(1);
		}

		if ((name.startsWith("get") || name.startsWith("set")) && name.length() > 3 && Character.isUpperCase(name.charAt(3))) {
			String after = name.substring(3);
			if (name.length() == 4) return after.toLowerCase();
			return after.toUpperCase().equals(after)
					? after
					: Character.toLowerCase(after.charAt(0)) + after.substring(1);
		}
		return name;
	}
}
