package ru.otr.opora.soa.utils;


import org.apache.commons.codec.binary.Base64;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.ZipEntryCallback;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.IOUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;


public class ZipHelper {


	public static String packZipBase64(ZipEntrySource[] entries) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ZipUtil.addEntries(new ByteArrayInputStream("".getBytes()), entries, out);
			return Base64.encodeBase64String(out.toByteArray());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
		}

		return null;
	}
	public static boolean packZip(String zipFile, ZipEntrySource[] entries)
	{
		return packZip(Paths.get(zipFile), entries);
	}

	public static boolean packZip(Path zipFile, ZipEntrySource[] entries) {

		OutputStream out;
		Path path = zipFile;
		File tmp = null;
		try {

			if (path.toFile().isFile() && path.toFile().canRead())
			ZipUtil.addOrReplaceEntries(zipFile.toFile(), entries);
			else
				ZipUtil.pack(entries, path.toFile());


			byte[] data = Files.readAllBytes(path);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (false)
				Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}


	private static class SingleZipEntryCallback implements ZipEntryCallback {

		private final String name;

		private final ZipEntryCallback action;

		private boolean found;

		public SingleZipEntryCallback(String name, ZipEntryCallback action) {
			this.name = name;
			this.action = action;
		}

		public void process(InputStream in, ZipEntry zipEntry) throws IOException {
			if (zipEntry.getName().matches(name)) {
				found = true;
				action.process(in, zipEntry);
			}
		}

		public boolean found() {
			return found;
		}

	}

	private static class ByteArrayUnpacker implements ZipEntryCallback {

		private byte[] bytes;

		public void process(InputStream in, ZipEntry zipEntry) throws IOException {
			bytes = IOUtils.toByteArray(in);
		}

		public byte[] getBytes() {
			return bytes;
		}

	}

	public static byte[] unpackEntry(InputStream is, String name) {
		ByteArrayUnpacker action = new ByteArrayUnpacker();
		if (!handle(is, name, action))
			return null; // entry not found
		return action.getBytes();
	}

	public static boolean handle(InputStream is, String name, ZipEntryCallback action) {
		SingleZipEntryCallback helper = new SingleZipEntryCallback(name, action);
		ZipUtil.iterate(is, helper);
		return helper.found();
	}


	public static String unpackZip(String base64Zip, String filePattern) {


		String result = null;
		try {

			result = new String(unpackEntry(new ByteArrayInputStream(Base64.decodeBase64(base64Zip)), filePattern), "utf-8");
	

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return result;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("sample.xml".matches("([^\\s]+(\\.(?i)(xml|png|gif|bmp))$)"));
		Path path = Paths.get("/Users/abv/test.zip");
		byte[] data = Files.readAllBytes(path);

		unpackZip(Base64.encodeBase64String(data), "([^\\s]+(\\.(?i)(xml|txt))$)");
		System.exit(0);
		ZipEntrySource[] entries = new ZipEntrySource[]{

				new ByteSource("sample.txt", "bar".getBytes())
		};

		ZipEntrySource[] entries2 = new ZipEntrySource[]{

				new ByteSource("sample2.txt", "bar".getBytes())
		};

		System.out.println(packZip("/Users/abv/test.zip", entries));

		System.out.println(packZip("/Users/abv/test.zip", entries2));

		//wso2bps-3.5.0/repository/components/lib/
	}
}
