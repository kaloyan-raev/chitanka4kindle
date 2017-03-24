package name.raev.kaloyan.kindle.chitanka.screen;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;

import com.amazon.kindle.kindlet.ui.KLabel;

public class SearchScreen extends Screen {

	SearchScreen(String opdsUrl) {
		super(opdsUrl);
		// TODO Auto-generated constructor stub
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

	}

	protected void updateContent(Container container) throws IOException {
		// TODO Auto-generated method stub

	}

	protected int getPageSize() {
		return 5;
	}

}
