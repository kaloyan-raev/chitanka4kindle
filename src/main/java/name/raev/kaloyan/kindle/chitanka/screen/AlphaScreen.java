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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import name.raev.kaloyan.kindle.chitanka.OpdsItem;
import name.raev.kaloyan.kindle.chitanka.widget.KActionLabel;

import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KPanel;

public class AlphaScreen extends AbstractScreen {
	
	AlphaScreen(String opdsUrl) {
		super(opdsUrl);
	}

	protected int getPageSize() {
		return 30;
	}

	protected void createContent(Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.insets = new Insets(8, 32, 8, 16);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 3;

		// add title
		KLabel title = new KLabel(getPageTitle());
		title.setFont(FONT_PAGE_TITLE);
		container.add(title, c);

		c.gridwidth = 1;
		// add item links
		OpdsItem[] items = opdsPage.getItems();
		for (int i = 0; i < items.length; i++) {
			final OpdsItem item = items[i];

			KActionLabel label = new KActionLabel(item.getTitle());
			label.setFont(FONT_LINK);
			
			c.gridx = i % 3;
			container.add(label, c);

			label.addActionListener(new LinkActionListener(this, i));
		}
		
		// add empty filler
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0; // request any extra vertical space
		container.add(new KPanel(), c);

	}

	protected void updateContent(Container container) {
		// nothing to update
	}

}
