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

import name.raev.kaloyan.kindle.chitanka.model.opds.OpdsPage;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchOverviewPage;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchPersonsPage;

public class ScreenManager {

	private static Screen currentScreen;

	public static Screen createScreen(String url) {
		try {
			URL u = new URL(url);
			String path = u.getPath();

			if ("/catalog.opds".equals(path)) {
				return new HomeScreen(new OpdsPage(url));
			} else if ("/authors.opds".equals(path)
					|| "/translators.opds".equals(path)
					|| "/books.opds".equals(path) || "/texts.opds".equals(path)) {
				return new ShortListScreen(new OpdsPage(url));
			} else if ("/authors/first-name.opds".equals(path)
					|| "/authors/last-name.opds".equals(path)
					|| "/translators/first-name.opds".equals(path)
					|| "/translators/last-name.opds".equals(path)
					|| "/books/alpha.opds".equals(path)
					|| "/texts/alpha.opds".equals(path)
					|| "/series.opds".equals(path)
					|| "/sequences.opds".equals(path)) {
				return new AlphaScreen(new OpdsPage(url));
			} else if ("/books/category.opds".equals(path)
					|| "/texts/label.opds".equals(path)
					|| "/texts/type.opds".equals(path)
					|| path.indexOf("/country/") != -1
					|| path.indexOf("/first-name/") != -1
					|| path.indexOf("/last-name/") != -1
					|| path.indexOf("/series/alpha/") != -1
					|| path.indexOf("/sequences/alpha/") != -1) {
				return new LongListScreen(new OpdsPage(url));
			} else if (path.indexOf("/search.json") != -1) {
				String query = u.getQuery();
				if (query.endsWith("&filter=persons")) {
					return new LongListScreen(new SearchPersonsPage(url));
				} else {
					return new ShortListScreen(new SearchOverviewPage(url));
				}
			}
		} catch (MalformedURLException e) {
			// return the default screen
		}

		// default screen
		return new BookListScreen(new OpdsPage(url));
	}

	public static void setCurrentScreen(Screen screen) {
		currentScreen = screen;
	}

	public static Screen getCurrentScreen() {
		return currentScreen;
	}

}
