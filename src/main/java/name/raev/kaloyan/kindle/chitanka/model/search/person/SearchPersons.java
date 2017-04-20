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
package name.raev.kaloyan.kindle.chitanka.model.search.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import name.raev.kaloyan.kindle.chitanka.model.search.BaseContentHandler;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchParser;

public class SearchPersons extends SearchParser {

	private static final String PERSONS_KEY = "persons";

	private List persons = new ArrayList();

	public SearchPersons(String url) throws IOException, ParseException {
		super(url);
	}

	public Person[] getPersons() {
		return (Person[]) persons.toArray(new Person[persons.size()]);
	}

	protected ContentHandler getContentHandler() {
		return new BaseContentHandler() {

			public boolean endArray() throws ParseException, IOException {
				return !PERSONS_KEY.equals(getParentKey());
			}

			public boolean startObject() throws ParseException, IOException {
				if (PERSONS_KEY.equals(getParentKey())) {
					persons.add(new Person());
				}
				return true;
			}

			public boolean primitive(Object value) throws ParseException, IOException {
				String key = (String) objects.peek();
				if ("slug".equals(key)) {
					if (PERSONS_KEY.equals(getParentKey(2))) {
						getLast().setSlug((String) value);
					}
				} else if ("name".equals(key)) {
					if (PERSONS_KEY.equals(getParentKey(2))) {
						getLast().setName((String) value);
					}
				} else if ("isAuthor".equals(key)) {
					if (PERSONS_KEY.equals(getParentKey(2))) {
						getLast().setAuthor(((Boolean) value).booleanValue());
					}
				}
				return true;
			}
		};
	}

	private Person getLast() {
		return (Person) persons.get(persons.size() - 1);
	}
}
