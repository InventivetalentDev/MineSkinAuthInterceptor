package org.mineskin.authinterceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));


		try {
			Class<?> mcMain = Class.forName("net.minecraft.client.main.Main");
			Method mainMethod = mcMain.getMethod("main", String[].class);
			mainMethod.invoke(null, new Object[] { args });
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}


}
