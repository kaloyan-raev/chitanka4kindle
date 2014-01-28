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
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import name.raev.kaloyan.kindle.chitanka.ConnectivityManager;
import name.raev.kaloyan.kindle.chitanka.OpdsItem;
import name.raev.kaloyan.kindle.chitanka.Utils;
import name.raev.kaloyan.kindle.chitanka.widget.KActionLabel;

import com.amazon.kindle.kindlet.ui.KImage;

public class HomeScreen extends AbstractScreen {

	HomeScreen(String opdsUrl) {
		super(opdsUrl);
	}

	protected int getPageSize() {
		return 8;
	}

	protected void createContent(Container container) {
		// set the layout
		container.setLayout(new GridBagLayout());

		addLogo(container);
		addLinks(container);
	}

	protected void updateContent(Container container) {
		// nothing to update
	}

	private void addLogo(Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		Image img = Utils.loadBuiltinImage("logo.jpg");
		KImage image = new KImage(img, 688 / 2, 720 / 2);
		container.add(image, c);
	}

	private void addLinks(Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		OpdsItem[] items = opdsPage.getItems();
		for (int i = 0; i < items.length; i++) {
			final OpdsItem item = items[i];

			KActionLabel label = new KActionLabel(item.getTitle());

			c.gridx = i % 2;
			container.add(label, c);

			label.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ConnectivityManager.getInstance().navigateTo(
							item.getNavigationLink());
				}
			});
		}
	}

}
