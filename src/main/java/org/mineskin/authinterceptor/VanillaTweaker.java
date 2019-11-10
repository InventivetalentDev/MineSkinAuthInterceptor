package org.mineskin.authinterceptor;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
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

		try {
			File f = new File("args.txt");
			f.createNewFile();
			FileWriter writer = new FileWriter(f);
			writer.write(this.args.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.args.toArray(new String[0]);
	}

	private static void dbg(String str) {
		System.out.println(str);
	}

}
