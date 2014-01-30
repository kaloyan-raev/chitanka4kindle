/**
 * Copyright 2014 Kaloyan Raev
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
package name.raev.kaloyan.kindle.chitanka.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import name.raev.kaloyan.kindle.chitanka.ConnectivityManager;
import name.raev.kaloyan.kindle.chitanka.OpdsItem;

public class LinkActionListener implements ActionListener {
	
	private AbstractScreen screen;
	private int index;
	
	public LinkActionListener(AbstractScreen screen, int index) {
		this.screen = screen;
		this.index = index;
	}
	
	public void actionPerformed(ActionEvent e) {
		OpdsItem opdsItem = screen.opdsPage.getItem(screen.pageIndex + index);
		String link = opdsItem.getNavigationLink();
		if (link != null) {
			ConnectivityManager.getInstance().navigateTo(link);
		} else {
			link = opdsItem.getDownloadLink();
			if (link != null) {
				ConnectivityManager.getInstance().downloadBook(
						link);
			}
		}
	}

}
