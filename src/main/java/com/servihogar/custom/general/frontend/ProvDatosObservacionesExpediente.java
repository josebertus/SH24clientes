package com.servihogar.custom.general.frontend;

import com.csi_ti.itaca.architecture.tools.webmodule.pantallas.ItacaView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;

@Theme("tests-valo-reindeer")
public class ProvDatosObservacionesExpediente extends Panel implements ItacaView {

    TextArea observaciones = new TextArea();

	private static final long serialVersionUID = -304344441663815443L;

	// constructor inicial
	public ProvDatosObservacionesExpediente(  ) {


		
		
		// TAB TITULAR
		GridLayout dtitLayout = new GridLayout(1,2);
		dtitLayout.setStyleName("layout-lineas-juntas");
		dtitLayout.addComponent(new Label("Observaciones:"),0,0);
		
		dtitLayout.addComponent(observaciones);
		observaciones.setValue("");
		/*try {
		observaciones.setValue(retorno.get("OBSERVACIONES").toString());
		} catch ( Exception e) {};*/
		
		observaciones.setReadOnly(true);
		observaciones.setWidth("100%");
		observaciones.setHeight("100%");
		observaciones.setRows(25);


		dtitLayout.setWidth("100%");
		setContent(dtitLayout);

		
	}
	

	

	
	@Override
	public void enter(ViewChangeEvent event) {

	}	


}