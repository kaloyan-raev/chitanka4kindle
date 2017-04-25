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
package name.raev.kaloyan.kindle.chitanka.model.search.sequence;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import name.raev.kaloyan.kindle.chitanka.model.Item;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchPage;

public class SearchSequencesPage extends SearchPage {

	private Sequence[] sequences;
	private Item[] items;

	public SearchSequencesPage(String url) {
		super(url);
	}

	public String getTitle() throws IOException {
		return "Поредици с „" + getQuery() + "“";
	}

	public Item[] getItems() throws IOException {
		if (items == null) {
			items = new Item[getSequences().length];
			for (int i = 0; i < items.length; i++) {
				items[i] = new SequenceItem(getSequences()[i]);
			}
		}
		return items;
	}

	private Sequence[] getSequences() throws IOException {
		if (sequences == null) {
			try {
				sequences = new SearchSequences(url).getSequences();
			} catch (ParseException e) {
				throw new IOException(e.getMessage());
			}
		}
		return sequences;
	}

}
