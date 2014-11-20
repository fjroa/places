package places;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.maddon.MBeanFieldGroup;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MFormLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;


@UIScope
@VaadinComponent
public class PlaceForm extends AbstractForm<Place> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7801597477207109319L;

	private MTextField name = new MTextField("Name");
	private MTextField category = new MTextField("Category");
	private MTextField about = new MTextField("About");
	private MTextField city = new MTextField("City");
	private MTextField state = new MTextField("State");
	private MTextField zip = new MTextField("Postal Code");
	private MultiFileUpload upload = new MultiFileUpload() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8301701730081066551L;

		@Override
		protected void handleFile(File file, String fileName, String mimeType,
				long length) {
			try {
				gridFsTemplate.store(new FileInputStream(file), place.getId());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	};

	private MTextField country = new MTextField("Country");

	private Image image;

	@Autowired
	private EventBus eventBus;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private PlaceRepository placeRepository;

	private Place place;

	private MFormLayout layout;

	@Override
	public MBeanFieldGroup<Place> setEntity(Place entity) {
		MBeanFieldGroup<Place> placeMBeanFieldGroup = super.setEntity(entity);
		this.place = entity;
		return placeMBeanFieldGroup;

	}

	public PlaceForm() {
		
		this.setSizeUndefined();
		this.setEagarValidation(true);
		this.setSavedHandler(place -> {
			this.placeRepository.save(place);
			this.eventBus.publish(this, new PlaceModifiedEvent(place));
		});
		
		this.getResetButton().setVisible(false);
	}

	@Override
	protected Component createContent() {
		layout = new MFormLayout(this.name, this.category, this.about,
				this.city, this.state, this.zip, this.country, this.upload)
				.withWidth("");
		this.displayImageIfAvailable(this.place.getId());

		return new MVerticalLayout(layout, getToolbar()).withWidth("");
	}

	protected void displayImageIfAvailable(String imageId) {
		if (image != null) {
			layout.removeComponent(image);
		}
		Optional.ofNullable(this.gridFsTemplate.getResource(imageId))
		.ifPresent(gfr ->{
			image = new Image("Image", new StreamResource(
					(StreamResource.StreamSource) () -> {
						try {
							return gridFsTemplate.getResource(imageId).getInputStream();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}, "photo" + this.place.getId() + System.currentTimeMillis() + imageId + ".jpg"));
			image.setWidth(400, Unit.PIXELS);
			layout.addComponent(image);
		});
		
	}
}
