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
package name.raev.kaloyan.kindle.chitanka;

import java.awt.Container;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;

import name.raev.kaloyan.kindle.chitanka.screen.Screen;
import name.raev.kaloyan.kindle.chitanka.screen.ScreenManager;

import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;
import com.amazon.kindle.kindlet.ui.KProgress;
import com.amazon.kindle.kindlet.ui.KTextArea;

public class ConnectivityManager {

	private static ConnectivityManager instance;

	private Stack history = new Stack();

	public static ConnectivityManager getInstance() {
		if (instance == null) {
			instance = new ConnectivityManager();
		}
		return instance;
	}

	public void display(final String url) {
		Connectivity connectivity = ContextManager.getContext()
				.getConnectivity();
		connectivity.submitSingleAttemptConnectivityRequest(
				new ConnectivityHandler() {
					public void disabled(NetworkDisabledDetails details)
							throws InterruptedException {
						// TODO Auto-generated method stub

					}

					public void connected() throws InterruptedException {
						Screen screen = ScreenManager.createScreen(url);
						screen.display();

						ScreenManager.setCurrentScreen(screen);
					}
				}, true);
	}

	public void downloadBook(String href) {
		KProgress progress = ContextManager.getContext().getProgressIndicator();
		progress.setIndeterminate(true);

		try {
			Utils.downloadMobiFromEpubUrl(href);
			Utils.rescanDocuments();
		} catch (Throwable t) {
			Container root = ContextManager.getContext().getRootContainer();
			root.removeAll();

			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));

			root.add(new KTextArea(sw.toString()));
			root.repaint();
		}

		progress.setIndeterminate(false);
		progress.setString("Книгата е свалена");
	}

	public boolean canGoBack() {
		return !history.isEmpty();
	}

	public void goBack() {
		// display the previous page from history
		display((String) history.pop());
	}

	public void navigateTo(String url) {
		// push the current page to history
		history.push(ScreenManager.getCurrentScreen().getUrl());
		// navigate to the selected page
		display(url);
	}

}
