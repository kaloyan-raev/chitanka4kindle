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
package name.raev.kaloyan.kindle.chitanka.model.search.overview;

import java.io.IOException;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import name.raev.kaloyan.kindle.chitanka.model.search.BaseContentHandler;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchParser;

public class SearchOverview extends SearchParser {

	private String query = null;
	private int persons = 0;
	private int texts = 0;
	private int books = 0;
	private int sequences = 0;
	private int categories = 0;

	public SearchOverview(String url) throws IOException, ParseException {
		super(url);
	}

	public String getQuery() {
		return query;
	}

	public int getTotal() {
		return books + texts + persons + categories + sequences;
	}

	public int getPersons() {
		return persons;
	}

	public int getTexts() {
		return texts;
	}

	public int getBooks() {
		return books;
	}

	public int getSequences() {
		return sequences;
	}

	public int getCategories() {
		return categories;
	}

	protected ContentHandler getContentHandler() {
		return new BaseContentHandler() {
			
			public boolean primitive(Object value) throws ParseException, IOException {
				String key = (String) objects.peek();
				if ("text".equals(key)) {
					query = (String) value;
				} else if ("id".equals(key)) {
					String parentKey = getParentKey(2);
					if ("persons".equals(parentKey)) {
						persons++;
					}
					if ("texts".equals(parentKey)) {
						texts++;
					}
					if ("books".equals(parentKey)) {
						books++;
					}
					if ("sequences".equals(parentKey)) {
						sequences++;
					}
					if ("categories".equals(parentKey)) {
						categories++;
					}
				}
				return true;
			}
		};
	}
}
