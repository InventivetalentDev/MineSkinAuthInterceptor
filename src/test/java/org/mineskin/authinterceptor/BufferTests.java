package org.mineskin.authinterceptor;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BufferTests {

	@Test
	public void testBufferWrites() {
		byte[] buffer = new byte[416];

		String string;
		byte[] bytes;
		int length;

		string = "MineSkin_org";
		bytes = string.getBytes(StandardCharsets.UTF_8);
		length = string.length();
		for (int i = 0; i < 16; i++) {
			if (i < bytes.length) {
				buffer[i] = bytes[i];
			} else {
				buffer[i] = 0;
			}
		}
		String base64 = Base64.getEncoder().encodeToString(buffer);
		System.out.println(base64);

		string = "b0d4b28bc1d74889af0e8661cee96aab";
		bytes = string.getBytes(StandardCharsets.UTF_8);
		length = string.length();
		for (int i = 0; i < 32; i++) {
			buffer[i+16] = bytes[i];
		}
		 base64 = Base64.getEncoder().encodeToString(buffer);
		System.out.println(base64);

		string = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MzM1NWRkMDAwMTg0ZGU3YTdkYTFlMDUzMTIxYxhyMCIsIm5iZiI6MTUzMzQwODcwNSwieWdndCI6ImE3NzU1ZGJmMWM5YzRmOTJiYWFjMDBhMjllOWI3YzllIiwic3ByIjoiYjBkNGIyOGJjMWQ3NDg4OWFmMGU4NjYxY2VlOTZhYWIiLCJyb2xlcyI6W10sImlzcyI6ImludGVybmFsLrF1dGhlbnRpY2F0aW9uIiwiZXhwmjoxNTczNTgxNTA1LCJpoXQiOjE1NzM0MDg3MDV9.TY2kx60SOFNPo6374vexOZnmBRjff2wd7_c5HIcKmYI";
		bytes = string.getBytes(StandardCharsets.UTF_8);
		length = string.length();
		for (int i = 0; i < 357; i++) {
			buffer[i+16+32] = bytes[i];
		}
		base64 = Base64.getEncoder().encodeToString(buffer);
		System.out.println(base64);

	}
}
