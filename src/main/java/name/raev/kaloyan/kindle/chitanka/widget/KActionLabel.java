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
package name.raev.kaloyan.kindle.chitanka.widget;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.amazon.kindle.kindlet.event.KindleKeyCodes;
import com.amazon.kindle.kindlet.ui.KLabel;

/**
 * A KLabel that can be focusable and receive ActionEvents.
 * 
 * <p>
 * The implementation is inspired by the <a href=
 * "https://github.com/apetresc/Kindle-Widget-Toolkit/blob/master/src/org/kwt/ui/KWTSelectableLabel.java"
 * >KWTSelectableLabel</a> by Adrian Petrescu.
 * </p>
 * 
 * @author Kaloyan Raev
 */
public class KActionLabel extends KLabel {

	private static final long serialVersionUID = 4428367054990871058L;

	private static final int UNDERLINE_THICKNESS = 6;
	private static final int UNDERLINE_GAP = 2;

	private List actionListeners;

	/*
	 * This is a dirty hack to get around the fact that KLabels do not respond
	 * to setPosition(). Instead, when painting the superclass, we have to trick
	 * it into thinking its size is smaller than it actually is. However, we
	 * don't want this faulty size to be read at any other time. Hence this flag
	 * for when to spoof the size.
	 */
	private boolean spoofSize = false;

	/**
	 * Constructs a new action label with no text.
	 */
	public KActionLabel() {
		this(null);
	}

	/**
	 * Constructs a new action label with the given text. The text will be
	 * clipped if it extends past the label's maximum size.
	 * 
	 * @param text
	 *            the label's text
	 */
	public KActionLabel(String text) {
		super(text);
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		setFocusable(true);
		actionListeners = new LinkedList();
	}

	/**
	 * {@inheritDoc }
	 */
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	/**
	 * {@inheritDoc }
	 */
	public Dimension getMinimumSize() {
		// Use KLabel's preferred size for its minimum size, to work around a
		// known bug in the KDK.
		Dimension d = super.getPreferredSize();
		if (spoofSize)
			return new Dimension(d.width, d.height - UNDERLINE_GAP
					- UNDERLINE_THICKNESS);
		return new Dimension(d.width, d.height + UNDERLINE_GAP
				+ UNDERLINE_THICKNESS + 1);
	}

	/**
	 * {@inheritDoc }
	 */
	public Dimension getSize() {
		Dimension d = super.getSize();
		if (spoofSize)
			return new Dimension(d.width, d.height - UNDERLINE_GAP
					- UNDERLINE_THICKNESS);
		return new Dimension(d.width, d.height + UNDERLINE_GAP
				+ UNDERLINE_THICKNESS + 1);
	}

	/**
	 * {@inheritDoc }
	 */
	public void paint(Graphics g) {
		spoofSize = true;
		super.paint(g);
		spoofSize = false;

		int y = super.getSize().height - (UNDERLINE_GAP + UNDERLINE_THICKNESS);

		if (isFocusable()) {
			// draw a thin dash underline to point that the label is selectable
			for (int i = 0; i < this.getWidth(); i += 2) {
				g.fillRect(i, y + UNDERLINE_GAP - 1, 1, 1);
			}
		}

		if (isFocusOwner()) {
			// draw a thick solid underline to point that the label is selected
			g.fillRect(0, y + UNDERLINE_GAP, this.getWidth() - 1,
					UNDERLINE_THICKNESS - 1);
		}
	}

	/**
	 * Registers a listener who wishes to be notified whenever this label is
	 * clicked by the user.
	 * 
	 * @param listener
	 *            a listener who wishes to be notified
	 */
	public void addActionListener(ActionListener listener) {
		this.actionListeners.add(listener);
	}

	/**
	 * {@inheritDoc }
	 */
	protected void processEvent(AWTEvent e) {
		if (e.getID() != KeyEvent.KEY_PRESSED)
			return;

		if (((KeyEvent) e).getKeyCode() != KindleKeyCodes.VK_FIVE_WAY_SELECT)
			return;

		Iterator iter = actionListeners.iterator();
		while (iter.hasNext()) {
			ActionListener listener = (ActionListener) iter.next();
			listener.actionPerformed(new ActionEvent(this,
					KeyEvent.KEY_PRESSED, null));
		}
	}
}