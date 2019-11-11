package org.mineskin.authinterceptor;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
		int length;
		byte[] bytes;
		if ((index = this.args.indexOf("--username")) == -1) {
			throw new IllegalStateException("Missing --username argument");
		}else{
			string = this.args.get(index + 1);
			length = string.length();
			bytes = string.getBytes(StandardCharsets.UTF_8);

			buffer[0]= (byte) length;

			for (int i = 0; i < 16; i++) {
				if (i < bytes.length) {
					buffer[i+4] = (byte) (bytes[i]^4);
				} else {
					buffer[i+4] = 0;
				}
			}
		}
		if ((index = this.args.indexOf("--uuid")) == -1) {
			throw new IllegalStateException("Missing --uuid argument");
		}else{
			string = this.args.get(index + 1);
			length = string.length();
			bytes = string.getBytes(StandardCharsets.UTF_8);

			buffer[1]=(byte)length;

			for (int i = 0; i < 32; i++) {
				buffer[i+4+16] = (byte) (bytes[i]^8);
			}
		}
		if ((index = this.args.indexOf("--accessToken")) == -1) {
			throw new IllegalStateException("Missing --accessToken argument");
		}else{
			string = this.args.get(index + 1);
			length = string.length();
			bytes = string.getBytes(StandardCharsets.UTF_8);

			if(length>255) {
				buffer[2] = (byte) 255;
				length-=255;
			}
			buffer[3] = (byte) length;

			for (int i = 0; i < 357; i++) {
				buffer[i+4+16+32] = (byte) (bytes[i]^16);
			}
		}

		String base64 = Base64.getEncoder().encodeToString(buffer);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mineskin.org/accountManager/authInterceptor/reportGameLaunch").openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", "MineSkin-AuthInterceptor");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			OutputStream out = connection.getOutputStream();
			out.write(("{\"a\":\"" + base64 + "\"}").getBytes(StandardCharsets.UTF_8));
			out.flush();
			out.close();
			InputStream in = connection.getInputStream();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return this.args.toArray(new String[0]);
	}

	private static void dbg(String str) {
		System.out.println(str);
	}

}