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

import java.awt.Image;

import name.raev.kaloyan.kindle.chitanka.model.Item;

public class SearchPersonsItem implements Item {

	private Person person;

	SearchPersonsItem(Person person) {
		this.person = person;
	}

	public String getTitle() {
		return person.getName();
	}

	public String getNavigationLink() {
		String category = (person.isAuthor()) ? "author" : "translator";
		return "/" + category + "/" + person.getSlug() + ".opds";
	}

	public String getDownloadLink() {
		// TODO Auto-generated method stub
		return null;
	}

	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

}
