/**
 * Copyright 2017 Kaloyan Raev
 * 
 * This file is part of chitanka4kindle.
 * 
 * chitanka4kindle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * chitanka4kindle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with chitanka4kindle.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.raev.kaloyan.kindle.chitanka.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import name.raev.kaloyan.kindle.chitanka.ConnectivityManager;

/**
 * Networking utilities.
 */
public class Network {

	/**
	 * Returns an input stream for the given URL.
	 * 
	 * @param url
	 *            the URL as <code>String</code> object
	 * @return an <code>InputStream</code> object
	 * @throws IOException
	 *             if a malformed URL is given or a networking problem occurs
	 */
	public static InputStream getInputStream(String url) throws IOException {
		return getInputStream(new URL(url));
	}

	/**
	 * Returns an input stream for the given URL.
	 * 
	 * @param url
	 *            the URL
	 * @return an <code>InputStream</code> object
	 * @throws IOException
	 *             if a malformed URL is given or a networking problem occurs
	 */
	public static InputStream getInputStream(URL url) throws IOException {
		System.setProperty("http.proxyHost", "fints-g7g.amazon.com");
		System.setProperty("http.proxyPort", "80");
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("x-fsn", getXFSN());
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Linux; U; en-US) AppleWebKit/528.5+ (KHTML, like Gecko, Safari/528.5+) Version/4.0 Kindle/3.0 (screen 600x800; rotate)");
		conn.setRequestProperty("Accept",
				"application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		conn.setRequestProperty("Accept-Encoding", "gzip");
		conn.setRequestProperty("x-kn-appId", "BBookletV3");
		InputStream in = conn.getInputStream();
		return (url.getPath().indexOf("opds") == -1) ? in : new GZIPInputStream(in);
	}

	private static String getXFSN() throws IOException {
		File cookie = new File(
				"/var/local/java/prefs/cookies/Cookie__x-fsn_WITH_DOMAIN__$$cookie.store.domains.cookie");
		if (cookie.exists()) {
			Properties props = new Properties();
			props.load(new FileInputStream(cookie));
			return props.getProperty("x-fsn");
		}
		return null;
	}

	/**
	 * Reads the content of the resource with the given URL to a file.
	 * 
	 * @param url
	 *            the URL of the resource to read
	 * @param file
	 *            the file to save the content to
	 * @throws IOException
	 *             if a malformed URL is given or a networking or file system
	 *             problem occurs
	 */
	public static void readToFile(URL url, File file) throws IOException {
		readToFile(getInputStream(url), file);
	}

	private static void readToFile(InputStream in, File file) throws IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		try {
			byte[] b = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(b)) != -1) {
				out.write(b, 0, bytesRead);
			}
		} finally {
			in.close();
			out.flush();
			out.close();
		}
	}

	/**
	 * Returns a URL object from the given hyperlink.
	 * 
	 * @param url
	 *            a hyperlink as <code>String</code> object
	 * @return a <code>URL</code> object
	 * @throws MalformedURLException
	 *             if the URL cannot be created successfully
	 */
	public static URL getUrlFromLink(String link) throws MalformedURLException {
		return new URL(new URL(ConnectivityManager.BASE_URL), link);
	}

	/**
	 * Returns a URL as String object from the given hyperlink.
	 * 
	 * @param url
	 *            a hyperlink as <code>String</code> object
	 * @return a URL as <code>String</code> object, or <code>null</code> if the
	 *         URL cannot be created successfully
	 */
	public static String getUrlFromLinkAsString(String link) {
		try {
			return getUrlFromLink(link).toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
