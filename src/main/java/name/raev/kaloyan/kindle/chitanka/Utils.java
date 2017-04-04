/**
 * Copyright 2014-2017 Kaloyan Raev
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
package name.raev.kaloyan.kindle.chitanka;

import java.awt.Image;
import java.awt.Toolkit;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.amazon.kindle.kindlet.ui.KProgress;

public class Utils {

	/**
	 * Forces rescan of the "documents" folder, so newly downloaded book appear
	 * on the Kindle's home screen.
	 * 
	 * <p>
	 * This method will fail with security exception with the default security
	 * policy on Kindle. The following permission must be added to the
	 * /opt/amazon/ebook/security/external.policy file inside the 'grant
	 * signedBy "Kindlet" {' block:
	 * </p>
	 * 
	 * <pre>
	 * permission java.io.FilePermission "<<ALL FILES>>", "execute";
	 * </pre>
	 * 
	 * <p>
	 * Kindle must be restarted after modifying the policy file.
	 * </p>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void rescanDocuments() throws IOException,
			InterruptedException {
		Process p = Runtime
				.getRuntime()
				.exec("dbus-send --system /default com.lab126.powerd.resuming int32:1");
		p.waitFor();
	}

	public static void downloadZippedBook(String href)
			throws IOException {
		URL url = new URL(href);
		ZipInputStream in = new ZipInputStream(Utils.getInputStream(url));
		ZipEntry entry = in.getNextEntry();
		
		downloadBook(in, entry.getName());
	}

	public static void downloadMobiFromEpubUrl(String href) throws IOException {
		URL urlEpub = getUrlFromLink(href);
		
		String path = urlEpub.getFile();
		path = path.substring(0, path.length() - 4).concat("mobi");
		
		URL urlMobi = new URL("http", "bb.bulexpo-bg.com", path);
		InputStream in = Utils.getInputStream(urlMobi);
		
		String fileName = path.substring(path.lastIndexOf('/') + 1);

		downloadBook(in, fileName);
	}
	
	public static void downloadBook(InputStream in, String fileName) throws IOException {
		readToFile(in, new File("/mnt/us/documents/", fileName));
	}

	public static Image loadBuiltinImage(String fileName) {
		URL url = Utils.class.getClassLoader().getResource(fileName);
		return Toolkit.getDefaultToolkit().getImage(url);
	}
	
	public static URL getUrlFromLink(String link) throws MalformedURLException {
		return new URL(new URL(ConnectivityManager.BASE_URL), link);
	}
	
	public static String getUrlFromLinkAsString(String link) {
		try {
			return getUrlFromLink(link).toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void startProgressIndicator() {
		startProgressIndicator(null);
	}
	
	public static void startProgressIndicator(String status) {
		KProgress progress = ContextManager.getContext().getProgressIndicator();
		progress.setIndeterminate(true);
		progress.setString(status);
	}
	
	public static void stopProgressIndicator() {
		KProgress progress = ContextManager.getContext().getProgressIndicator();
		progress.setIndeterminate(false);
	}
	
	public static InputStream getInputStream(String url) throws IOException {
		return getInputStream(new URL(url));
	}

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

	public static Image getImage(URL url) throws IOException {
		File homeDir = ContextManager.getContext().getHomeDirectory();
		File file = new File(homeDir, url.getPath());
		file.getParentFile().mkdirs();
		if (!file.exists()) {
			readToFile(getInputStream(url), file);
		}
		return Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
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

}
