package name.raev.kaloyan.kindle.chitanka.screen;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import org.json.simple.parser.ParseException;

import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KPanel;

import name.raev.kaloyan.kindle.chitanka.ConnectivityManager;
import name.raev.kaloyan.kindle.chitanka.search.SearchOverview;
import name.raev.kaloyan.kindle.chitanka.widget.KActionLabel;
import name.raev.kaloyan.kindle.chitanka.widget.KSearchField;

public class SearchScreen extends Screen {

	SearchScreen(String url) {
		super(url);
	}

	protected void createContent(Container container) throws IOException {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.insets = new Insets(8, 32, 8, 16);
		c.anchor = GridBagConstraints.WEST;

		// add title
		String urlQuery = new URL(getUrl()).getQuery();
		String query = urlQuery.substring(urlQuery.indexOf('=') + 1);
		KLabel title = new KLabel("Резултати за „" + query + "“");
		title.setFont(FONT_PAGE_TITLE);
		container.add(title, c);

		try {
			SearchOverview overview = new SearchOverview(getUrl());

			KLabel total = new KLabel("Общо ".concat(Integer.toString(overview.getTotal())).concat(" резултата"));
			total.setFont(FONT_PAGE_SUBTITLE);
			container.add(total, c);

			addLink(container, c, "Книги", overview.getBooks(), "/books/search.opds?q=" + query);
			addLink(container, c, "Творби", overview.getTexts(), null);
			addLink(container, c, "Автори", overview.getPersons(), null);
			addLink(container, c, "Категории", overview.getCategories(), null);
			addLink(container, c, "Поредици", overview.getSequences(), null);

			// add empty filler
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1.0; // request any extra vertical space
			KPanel filler = new KPanel();
			filler.setFocusable(false);
			container.add(filler, c);

			addSearch(container, query);
		} catch (ParseException e) {
			Screen.displayError(e);
		}
	}

	private void addLink(Container container, GridBagConstraints c, String text, int count, final String link) {
		KActionLabel label = new KActionLabel(text + " (" + count + ")");
		label.setFont(FONT_LINK);
		if (count == 0) {
			label.setEnabled(false);
			label.setForeground(Color.GRAY);
		}
		container.add(label, c);
		
		if (link != null) {
			label.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ConnectivityManager.getInstance().navigateTo(link);
				}
			});
		}
	}

	private void addSearch(Container container, String query) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		KSearchField search = new KSearchField(query);
		container.add(search, c);
	}

	protected void updateContent(Container container) throws IOException {
	}

	protected int getTotalPages() throws IOException {
		return 1;
	}

	protected int getPageSize() {
		return 5;
	}

}
