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
package name.raev.kaloyan.kindle.chitanka.widget;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.amazon.kindle.kindlet.ui.KPanel;

public class KBorderedPanel extends KPanel {

	private static final long serialVersionUID = 8840237893389861608L;

	public KBorderedPanel() {
		super();
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (g.getClipBounds() != null) {
			Rectangle a = new Rectangle(0, 0, getWidth(), getHeight());
			g.setColor(getForeground());
			g.drawRoundRect(a.x, a.y, a.width - 1, a.height - 1, 40, 40);
			g.drawRoundRect(a.x + 1, a.y + 1, a.width - 3, a.height - 3, 38, 38);
		}
	}

}
