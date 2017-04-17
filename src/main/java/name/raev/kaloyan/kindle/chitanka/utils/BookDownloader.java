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

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Utilities for downloading books to the Kindle device.
 */
public class BookDownloader {

	/**
	 * Downloads a MOBI book from the MOBI mirror of chitanka.info given the
	 * EPUB hyperlink.
	 * 
	 * @param href
	 *            the huperlink to the EPUB book
	 * @throws IOException
	 *             if a networking problem occurs
	 */
	public static void downloadMobiFromEpubUrl(String href) throws IOException {
		URL urlEpub = Network.getUrlFromLink(href);

		String path = urlEpub.getFile();
		path = path.substring(0, path.length() - 4).concat("mobi");

		URL urlMobi = new URL("http", "bb.bulexpo-bg.com", path);

		String fileName = path.substring(path.lastIndexOf('/') + 1);

		downloadBook(urlMobi, fileName);
	}

	private static void downloadBook(URL url, String fileName) throws IOException {
		Network.readToFile(url, new File("/mnt/us/documents/", fileName));
	}

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
	public static void rescanDocuments() throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec("dbus-send --system /default com.lab126.powerd.resuming int32:1");
		p.waitFor();
	}

}
