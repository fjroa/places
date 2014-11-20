package places;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.maddon.button.ConfirmButton;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

@UIScope
@VaadinComponent
public class PlacesTableView extends MVerticalLayout implements
		InitializingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8714581004619180713L;

	@Autowired 
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceForm form;

	@Autowired
	private EventBus eventBus;

	private MTable<Place> list = new MTable<>(Place.class)
			.withProperties("name", "street", "city", "state", "zip",
					"category")
			.withColumnHeaders("name", "street", "city", "state", "zip",
					"category").withFullWidth();

	private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O,
			clickEvent -> {
				form.setEntity(this.placeRepository.findOne(list.getValue()
						.getId()));
				form.openInModalPopup().setHeight("90%");
			});

	private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
			"¿Seguro que desea eliminar la entrada?", clickEvent -> {
				Place place = list.getValue();
				placeRepository.delete(place);
				list.setValue(null);
				listEntities();
				eventBus.publish(this, new PlaceModifiedEvent(place));
			});

	@PostConstruct
	protected void init() {
		setCaption("Tabular View");
		addComponents(new MVerticalLayout(new MHorizontalLayout(edit, delete),
				list).expand(list));
		listEntities();
		list.addMValueChangeListener(e -> adjustActionButtonState());
	}

	private void adjustActionButtonState() {
		boolean hasSelection = list.getValue() != null;
		edit.setEnabled(hasSelection);
		delete.setEnabled(hasSelection);
	}

	private void listEntities() {
		list.setBeans(placeRepository.findAll());
		adjustActionButtonState();
	}

	@EventBusListenerMethod
	protected void onPlaceModifiedEvente(PlaceModifiedEvent event) {
		listEntities();
		UI.getCurrent().getWindows().forEach(UI.getCurrent()::removeWindow);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		eventBus.subscribe(this);
	}

}
