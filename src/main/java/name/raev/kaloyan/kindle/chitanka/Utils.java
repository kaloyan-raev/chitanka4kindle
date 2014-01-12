/**
 * Copyright 2014 Kaloyan Raev
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

	/**
	 * Forces rescan of the "documents" folder, so newly downloaded book appear
	 * on the Kindle's home screen.
	 * 
	 * <p>
	 * This method will fail with security exception with the default security
	 * policy on Kindle. The following persmission must be added to the
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
			throws FileNotFoundException, IOException {
		URL url = new URL(href);
		URLConnection connection = url.openConnection();
		ZipInputStream in = new ZipInputStream(connection.getInputStream());
		ZipEntry entry = in.getNextEntry();
		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				new File("/mnt/us/documents/", entry.getName())));
		try {
			byte[] b = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(b)) != -1) {
				out.write(b, 0, bytesRead);
			}
		} finally {
			in.closeEntry();
			in.close();
			out.flush();
			out.close();
		}
	}

}
