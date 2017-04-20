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

import org.json.simple.parser.ParseException;

import name.raev.kaloyan.kindle.chitanka.model.Item;
import name.raev.kaloyan.kindle.chitanka.model.search.SearchPage;

public class SearchOverviewPage extends SearchPage {

	private SearchOverview overview;
	private Item[] items;

	public SearchOverviewPage(String url) {
		super(url);
	}

	public String getTitle() throws IOException {
		return "Резултати за „" + getQuery() + "“";
	}

	public String getSubtitle() throws IOException {
		return "Общо " + getOverview().getTotal() + " резултата";
	}

	public Item[] getItems() throws IOException {
		if (items == null) {
			items = new Item[] {
					new SearchOverviewItem("Книги", getOverview().getBooks(), "/books/search.opds?q=" + getQuery()),
					new SearchOverviewItem("Творби", getOverview().getTexts(),
							"/search.json?q=" + getQuery() + "&filter=texts"),
					new SearchOverviewItem("Автори и преводачи", getOverview().getPersons(),
							"/search.json?q=" + getQuery() + "&filter=persons"),
					new SearchOverviewItem("Категории", getOverview().getCategories(),
							"/search.json?q=" + getQuery() + "&filter=categories"),
					new SearchOverviewItem("Поредици", getOverview().getSequences(),
							"/search.json?q=" + getQuery() + "&filter=sequences"),
					new SearchOverviewItem("Серии", getOverview().getSeries(),
							"/search.json?q=" + getQuery() + "&filter=series") };
		}
		return items;
	}

	private SearchOverview getOverview() throws IOException {
		if (overview == null) {
			try {
				overview = new SearchOverview(url);
			} catch (ParseException e) {
				throw new IOException(e.getMessage());
			}
		}
		return overview;
	}

}
