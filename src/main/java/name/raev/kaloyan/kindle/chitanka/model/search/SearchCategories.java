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
package name.raev.kaloyan.kindle.chitanka.model.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import name.raev.kaloyan.kindle.chitanka.utils.Network;

public class SearchCategories {

	private Stack objects = new Stack();

	private List categories = new ArrayList();

	public SearchCategories(String url) throws IOException, ParseException {
		parse(url);
	}

	public Category[] getCategories() {
		return (Category[]) categories.toArray(new Category[categories.size()]);
	}

	private void parse(String url) throws IOException, ParseException {
		Reader reader = new BufferedReader(new InputStreamReader(Network.getInputStream(url)));

		new JSONParser().parse(reader, new ContentHandler() {

			public void startJSON() throws ParseException, IOException {
			}

			public void endJSON() throws ParseException, IOException {
			}

			public boolean startArray() throws ParseException, IOException {
				return true;
			}

			public boolean endArray() throws ParseException, IOException {
				return !"categories".equals(objects.peek());
			}

			public boolean startObject() throws ParseException, IOException {
				if (!objects.isEmpty() && "categories".equals(objects.peek())) {
					categories.add(new Category());
				}
				return true;
			}

			public boolean endObject() throws ParseException, IOException {
				return true;
			}

			public boolean startObjectEntry(String key) throws ParseException, IOException {
				objects.push(key);
				return true;
			}

			public boolean endObjectEntry() throws ParseException, IOException {
				objects.pop();
				return true;
			}

			public boolean primitive(Object value) throws ParseException, IOException {
				String key = (String) objects.peek();
				if ("slug".equals(key)) {
					String parentKey = (String) objects.elementAt(objects.size() - 2);
					if ("categories".equals(parentKey)) {
						Category last = (Category) categories.get(categories.size() - 1);
						last.setSlug((String) value);
					}
				} else if ("name".equals(key)) {
					String parentKey = (String) objects.elementAt(objects.size() - 2);
					if ("categories".equals(parentKey)) {
						Category last = (Category) categories.get(categories.size() - 1);
						last.setName((String) value);
					}
				} else if ("nrOfBooks".equals(key)) {
					String parentKey = (String) objects.elementAt(objects.size() - 2);
					if ("categories".equals(parentKey)) {
						Category last = (Category) categories.get(categories.size() - 1);
						last.setNumberOfBooks(((Long) value).intValue());
					}
				}
				return true;
			}
		});
	}
}
