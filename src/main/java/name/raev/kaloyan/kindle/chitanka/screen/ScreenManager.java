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
package name.raev.kaloyan.kindle.chitanka.screen;

import java.net.MalformedURLException;
import java.net.URL;

public class ScreenManager {

	private static Screen currentScreen;

	public static Screen createScreen(String url) {
		try {
			String path = new URL(url).getPath();

			if ("/catalog.opds".equals(path)) {
				return new HomeScreen(url);
			} else if ("/authors.opds".equals(path)
					|| "/translators.opds".equals(path)
					|| "/books.opds".equals(path) || "/texts.opds".equals(path)) {
				return new ShortListScreen(url);
			} else if ("/authors/first-name.opds".equals(path)
					|| "/authors/last-name.opds".equals(path)
					|| "/translators/first-name.opds".equals(path)
					|| "/translators/last-name.opds".equals(path)
					|| "/books/alpha.opds".equals(path)
					|| "/texts/alpha.opds".equals(path)
					|| "/series.opds".equals(path)
					|| "/sequences.opds".equals(path)) {
				return new AlphaScreen(url);
			} else if ("/books/category.opds".equals(path)
					|| "/texts/label.opds".equals(path)
					|| "/texts/type.opds".equals(path)
					|| path.indexOf("/country/") != -1
					|| path.indexOf("/first-name/") != -1
					|| path.indexOf("/last-name/") != -1
					|| path.indexOf("/series/alpha/") != -1
					|| path.indexOf("/sequences/alpha/") != -1) {
				return new LongListScreen(url);
			} else if (path.indexOf("/search.xml") != -1) {
				return new SearchScreen(url);
			}
		} catch (MalformedURLException e) {
			// return the default screen
		}

		// default screen
		return new BookListScreen(url);
	}

	public static void setCurrentScreen(Screen screen) {
		currentScreen = screen;
	}

	public static Screen getCurrentScreen() {
		return currentScreen;
	}

}
