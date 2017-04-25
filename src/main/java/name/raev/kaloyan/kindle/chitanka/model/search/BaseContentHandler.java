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

import java.io.IOException;
import java.util.Stack;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

public class BaseContentHandler implements ContentHandler {

	protected Stack objects = new Stack();

	public void startJSON() throws ParseException, IOException {
	}

	public void endJSON() throws ParseException, IOException {
	}

	public boolean startArray() throws ParseException, IOException {
		return true;
	}

	public boolean endArray() throws ParseException, IOException {
		return true;
	}

	public boolean startObject() throws ParseException, IOException {
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
		return true;
	}

	protected String getParentKey() {
		return objects.isEmpty() ? null : (String) objects.peek();
	}

	protected String getParentKey(int depth) {
		return (objects.size() < depth) ? null : (String) objects.elementAt(objects.size() - 2);
	}

}
