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
package name.raev.kaloyan.kindle.chitanka.model.search.text;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import name.raev.kaloyan.kindle.chitanka.model.Item;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchPage;

public class SearchTextsPage extends SearchPage {

	private Text[] texts;
	private Item[] items;

	public SearchTextsPage(String url) {
		super(url);
	}

	public String getTitle() throws IOException {
		return "Творби с „" + getQuery() + "“";
	}

	public Item[] getItems() throws IOException {
		if (items == null) {
			items = new Item[getTexts().length];
			for (int i = 0; i < items.length; i++) {
				items[i] = new TextItem(getTexts()[i]);
			}
		}
		return items;
	}

	private Text[] getTexts() throws IOException {
		if (texts == null) {
			try {
				texts = new SearchTexts(url).getTexts();
			} catch (ParseException e) {
				throw new IOException(e.getMessage());
			}
		}
		return texts;
	}

}
