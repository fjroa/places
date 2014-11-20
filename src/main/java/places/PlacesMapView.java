package places;

import org.springframework.beans.factory.InitializingBean;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;

@UIScope
@VaadinComponent

public class PlacesMapView extends MVerticalLayout implements InitializingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2548366376194806916L;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}
