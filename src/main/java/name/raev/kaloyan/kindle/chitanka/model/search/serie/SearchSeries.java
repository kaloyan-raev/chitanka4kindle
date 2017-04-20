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
package name.raev.kaloyan.kindle.chitanka.model.search.serie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import name.raev.kaloyan.kindle.chitanka.model.search.BaseContentHandler;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchParser;

public class SearchSeries extends SearchParser {

	private static final String SERIES_KEY = "series";

	private List series = new ArrayList();

	public SearchSeries(String url) throws IOException, ParseException {
		parse(url);
	}

	public Serie[] getSeries() {
		return (Serie[]) series.toArray(new Serie[series.size()]);
	}

	protected ContentHandler getContentHandler() {
		return new BaseContentHandler() {

			public boolean endArray() throws ParseException, IOException {
				return !SERIES_KEY.equals(getParentKey());
			}

			public boolean startObject() throws ParseException, IOException {
				if (SERIES_KEY.equals(getParentKey())) {
					series.add(new Serie());
				}
				return true;
			}

			public boolean primitive(Object value) throws ParseException, IOException {
				String key = (String) objects.peek();
				if ("slug".equals(key)) {
					if (SERIES_KEY.equals(getParentKey(2))) {
						getLast().setSlug((String) value);
					}
				} else if ("name".equals(key)) {
					if (SERIES_KEY.equals(getParentKey(2))) {
						getLast().setName((String) value);
					}
				}
				return true;
			}
		};
	}

	private Serie getLast() {
		return (Serie) series.get(series.size() - 1);
	}
}
