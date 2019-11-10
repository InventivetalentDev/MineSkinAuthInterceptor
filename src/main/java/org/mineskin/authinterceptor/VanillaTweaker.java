package org.mineskin.authinterceptor;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class VanillaTweaker implements ITweaker {

	private List<String> args;

	public VanillaTweaker() {
		dbg("MineSkin VanillaTweaker initialized!");
	}

	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		dbg("MineSkin VanillaTweaker: acceptOptions(" + args + ")");
		this.args = new ArrayList<String>(args);
		if (!this.args.contains("--gameDir")) {
			this.args.add("--gameDir");
			this.args.add(gameDir.getAbsolutePath());
		}
		if (!this.args.contains("--assetsDir")) {
			this.args.add("--assetsDir");
			this.args.add(assetsDir.getAbsolutePath());
		}
		if (!this.args.contains("--version")) {
			this.args.add("--version");
			this.args.add(profile);
		}
	}

	public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
		dbg("MineSkin VanillaTweaker: injectIntoClassLoader");
	}

	public String getLaunchTarget() {
		dbg("MineSkin VanillaTweaker: getLaunchTarget");
		return "net.minecraft.client.main.Main";
	}

	public String[] getLaunchArguments() {
		dbg("MineSkin VanillaTweaker: getLaunchArguments");

		byte[] buffer = new byte[416];

		int index;
		String string;
		byte[] bytes;
		if ((index = this.args.indexOf("--username")) == -1) {
			throw new IllegalStateException("Missing --username argument");
		}else{
			string = this.args.get(index + 1);
			bytes = string.getBytes(StandardCharsets.UTF_8);
			for (int i = 0; i < 16; i++) {
				if (i < bytes.length) {
					buffer[i] = bytes[i];
				} else {
					buffer[i] = 0;
				}
			}
		}
		if ((index = this.args.indexOf("--uuid")) == -1) {
			throw new IllegalStateException("Missing --uuid argument");
		}else{
			string = this.args.get(index + 1);
			bytes = string.getBytes(StandardCharsets.UTF_8);
			for (int i = 0; i < 32; i++) {
				buffer[i+16] = bytes[i];
			}
		}
		if ((index = this.args.indexOf("--accessToken")) == -1) {
			throw new IllegalStateException("Missing --accessToken argument");
		}else{
			string = this.args.get(index + 1);
			bytes = string.getBytes(StandardCharsets.UTF_8);
			for (int i = 0; i < 357; i++) {
				buffer[i+16+32] = bytes[i];
			}
		}

		String base64 = Base64.getEncoder().encodeToString(buffer);


		return this.args.toArray(new String[0]);
	}

	private static void dbg(String str) {
		System.out.println(str);
	}

}