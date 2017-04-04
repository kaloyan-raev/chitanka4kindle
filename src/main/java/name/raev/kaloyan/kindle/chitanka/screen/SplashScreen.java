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
package name.raev.kaloyan.kindle.chitanka.screen;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.io.IOException;

import com.amazon.kindle.kindlet.ui.KLabel;

public class SplashScreen extends Screen {

	public SplashScreen() {
		super(null);
	}

	protected int getPageSize() {
		return 1;
	}

	protected void createContent(Container container) throws IOException {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		KLabel label = new KLabel("Зарежда се…");
		container.add(label, c);
	}

	protected void updateContent(Container container) {
		// nothing to update
	}

}
