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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import name.raev.kaloyan.kindle.chitanka.ContextManager;

/**
 * Utilities for loading images.
 */
public class ImageLoader {

	/**
	 * Loads an image resource from the application's JAR/AZW2 file.
	 * 
	 * @param fileName
	 *            the file name of the image to load
	 * @return an AWT <code>Image</code> object
	 */
	public static Image loadBuiltinImage(String fileName) {
		URL url = ImageLoader.class.getClassLoader().getResource(fileName);
		return Toolkit.getDefaultToolkit().getImage(url);
	}

	/**
	 * Loads an Image resource from a URL.
	 * 
	 * @param url
	 *            the URL to load the image from
	 * @return an AWT <code>Image</code> object
	 * @throws IOException
	 *             if a malformed URL is given or a networking problem occurs
	 */
	public static Image getImage(URL url) throws IOException {
		File homeDir = ContextManager.getContext().getHomeDirectory();
		File file = new File(homeDir, url.getPath());
		file.getParentFile().mkdirs();
		if (!file.exists()) {
			Network.readToFile(url, file);
		}
		return Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
	}

}
