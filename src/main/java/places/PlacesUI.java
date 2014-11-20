package places;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

@Theme("valo")
@Widgetset("AppWidgetset")
@VaadinUI
public class PlacesUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3660673570462099134L;

	@Autowired
	private PlacesTableView placesTableView;
	
	@Autowired
	private PlacesMapView placesMapView;
	
	@Override
	protected void init(VaadinRequest request) {
		
		Page.getCurrent().setTitle("'Bootiful' Vaadin Places");
		
		TabSheet sheet = new TabSheet();
		sheet.setSizeFull();
		sheet.addComponents(this.placesTableView,this.placesMapView);
		this.setContent(sheet);
	}

}

class PlaceModifiedEvent {
	private final Place place;

	public Place getPlace() {
		return place;
	}

	public PlaceModifiedEvent(Place place) {
		super();
		this.place = place;
	}
}