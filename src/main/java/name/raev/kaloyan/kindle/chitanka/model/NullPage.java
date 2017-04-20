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
package name.raev.kaloyan.kindle.chitanka.model;

import java.io.IOException;

public class NullPage implements Page {

	public String getTitle() throws IOException {
		return null;
	}

	public String getSubtitle() throws IOException {
		return null;
	}

	public int getItemsCount() throws IOException {
		return 0;
	}

	public Item[] getItems() throws IOException {
		return new Item[0];
	}

	public Item[] getItems(int index, int length) throws IOException {
		return new Item[0];
	}

	public Item getItem(int index) {
		return null;
	}

}
