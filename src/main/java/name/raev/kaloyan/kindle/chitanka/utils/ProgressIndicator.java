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
package name.raev.kaloyan.kindle.chitanka.utils;

import com.amazon.kindle.kindlet.ui.KProgress;

import name.raev.kaloyan.kindle.chitanka.ContextManager;

/**
 * Utility with convenient methods for starting and stopping the progress
 * indicator.
 */
public class ProgressIndicator {

	/**
	 * Starts the progress indicator with no status message.
	 */
	public static void start() {
		start(null);
	}

	/**
	 * Starts the progress indicator with a status message.
	 * 
	 * @param status
	 *            the status message to display
	 */
	public static void start(String status) {
		KProgress progress = ContextManager.getContext().getProgressIndicator();
		progress.setIndeterminate(true);
		progress.setString(status);
	}

	/**
	 * Stops the progress indicator.
	 */
	public static void stop() {
		KProgress progress = ContextManager.getContext().getProgressIndicator();
		progress.setIndeterminate(false);
	}

}
