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

import java.awt.Image;

import name.raev.kaloyan.kindle.chitanka.model.Item;

public class SearchOverviewItem implements Item {

	private String text;
	private int count;
	private String link;

	SearchOverviewItem(String text, int count, String link) {
		this.text = text;
		this.count = count;
		this.link = link;
	}

	public String getTitle() {
		return text + " (" + count + ")";
	}

	public String getNavigationLink() {
		return link;
	}

	public String getDownloadLink() {
		return null;
	}

	public Image getImage() {
		return null;
	}

	public boolean isEnabled() {
		return count > 0;
	}

}
