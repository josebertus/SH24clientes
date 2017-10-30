package com.servihogar.custom.general.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csi_ti.itaca.architecture.tools.webmodule.pantallas.ItacaView;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@Theme("tests-valo-reindeer")
public class CliDatosTitularExpediente extends Panel implements ItacaView {


	public ArrayList lsCon = new ArrayList();
	
	
	    
    Property.ValueChangeListener listenerChgVip;
    
	Table tableTel = new Table();
	Label lCaption = new Label();
	Button btVolver = new Button();
	public Label dgenPolizaTitular = new Label();
	Label dgenContrato =new Label("Compañía:");
	Label dgenCia =new Label("Cia:");
		Label dgenTitular = new Label("Titular:");
	Label dgenNif = new Label("NIF:");
	Label dgenPoliza =new Label("Póliza:");
	Button btTelefonos = new Button();

	
	Label dgenPais = new Label();
	
	public Table tbTelefonos = new Table();
	

	

	

	private static final long serialVersionUID = -304344441663815443L;

	// constructor inicial
	public CliDatosTitularExpediente(List<Map> retorno) {

		String[] columnsexp ={"Descripcion","Telefono"};
		Object[] typesexp = {String.class,String.class};
		Object[] visibleColumnsexp = new Object[]{"Descripcion","Telefono"};
		Util.defineTable(tbTelefonos, columnsexp, typesexp, visibleColumnsexp,true);
		tbTelefonos.setColumnHeaders(new String[] {"Descripcion","Telefono" });

		tbTelefonos.setColumnExpandRatio("Descripcion", 60);
		tbTelefonos.setColumnExpandRatio("Telefono", 40);
		tbTelefonos.setWidth("60%");
		tbTelefonos.setColumnReorderingAllowed(false);
		tbTelefonos.setTabIndex(-1);
		tbTelefonos.setPageLength(3);
		
		// TAB TITULAR
		GridLayout dtitLayout = new GridLayout(8,4);
		dtitLayout.setStyleName("layout-lineas-juntas");
		
		dtitLayout.addComponent(new Label("Cia:"),0,0);
		dgenCia.setCaptionAsHtml(true);
		dgenCia.setContentMode(ContentMode.HTML);
		dgenCia.setValue("<h style='font-weight: normal;'>"+retorno.get(0).get("DSCLIENTE").toString()+" </h>");		
		dtitLayout.addComponent(dgenCia,1,0);
		
		dtitLayout.addComponent(new Label("Titular:"),2,0);
		dgenTitular.setCaptionAsHtml(true);
		dgenTitular.setContentMode(ContentMode.HTML);
		dgenTitular.setValue("<h style='font-weight: normal;'>"+retorno.get(0).get("TITULAR").toString()+" </h>");
		dtitLayout.addComponent(dgenTitular,3,0,6,0);
		
		dtitLayout.addComponent(new Label("Póliza"),0,1);
		dgenPoliza.setCaptionAsHtml(true);
		dgenPoliza.setContentMode(ContentMode.HTML);
		dgenPoliza.setValue("<h style='font-weight: normal;'>"+(String) retorno.get(0).get("POLIZA").toString()+" </h>"	);						
		dtitLayout.addComponent(dgenPoliza,1,1);

		dtitLayout.addComponent(new Label("Nif:"),2,1);
		dgenNif.setCaptionAsHtml(true);
		dgenNif.setContentMode(ContentMode.HTML);
		dgenNif.setValue("<h style='font-weight: normal;'>"+(String) retorno.get(0).get("NIF").toString() +" </h>");				
		dtitLayout.addComponent(dgenNif,3,1);
		
		dtitLayout.addComponent(new Label("Contrato:"),0,2);
		dgenContrato.setCaptionAsHtml(true);
		dgenContrato.setContentMode(ContentMode.HTML);
		dgenContrato.setValue("<h style='font-weight: normal;'>"+ retorno.get(0).get("CONTRATO").toString() +" </h>"	);		
		dtitLayout.addComponent(dgenContrato,1,2,4,2);
		
		dtitLayout.addComponent(btVolver,7,0);
		btVolver.setStyleName(ValoTheme.BUTTON_DANGER);
		dtitLayout.setComponentAlignment(btVolver, Alignment.MIDDLE_RIGHT);
		btVolver.setIcon(FontAwesome.CLOSE);
		btVolver.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				
				UI.getCurrent().getNavigator().navigateTo("CliPantallaBusquedaExpedientes");	 
			}
		});	
		
		if ( UI.getCurrent().getSession().getAttribute("exp")!=null) {
			btVolver.setVisible(false);
		}
		
		dtitLayout.setWidth("100%");
		dtitLayout.setColumnExpandRatio(0,7);
		dtitLayout.setColumnExpandRatio(1,27);
		dtitLayout.setColumnExpandRatio(2,7);
		dtitLayout.setColumnExpandRatio(3,27);
		dtitLayout.setColumnExpandRatio(4,8);
		dtitLayout.setColumnExpandRatio(5,25);
		
		dtitLayout.addComponent(tbTelefonos,0,3,5,3);
		dtitLayout.setComponentAlignment(tbTelefonos, Alignment.TOP_CENTER);
		tbTelefonos.setVisible(false);
				
		setCaption("Titular");
		setContent(dtitLayout);
		
		// telefonos
		tbTelefonos.removeAllItems();
		
		//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		if ( UI.getCurrent().getSession().getAttribute("telefonosExpediente")!=null ) {;
		//System.out.println("*********************************");
			List<Map> valor = (List<Map>) UI.getCurrent().getSession().getAttribute("telefonosExpediente");

			for (Map map : valor) {

				Object newItemId = tbTelefonos.addItem();
				Item row1 = tbTelefonos.getItem(newItemId);
				
				if ( map.get("DSTELEF")!=null ) {
					row1.getItemProperty("Descripcion").setValue(map.get("DSTELEF"));
				}
				if ( map.get("TXTELEF")!=null ) {
					row1.getItemProperty("Telefono").setValue(map.get("TXTELEF").toString());
				}


			}	
		}		


	}
	
	private class HorizontalRule extends Label {
		  public HorizontalRule() {
		    super("<hr style='color:gray;height:1px' />", ContentMode.HTML);
		  }
		}
	
	


	
	@SuppressWarnings("unchecked")
	public void recuperarDatosTitular ( ) {
		
			

			dgenPolizaTitular.setCaptionAsHtml(true);
			dgenPolizaTitular.setContentMode(ContentMode.HTML);
			dgenPolizaTitular.setValue("<h style='font-weight: bold;'>"+(String) UI.getCurrent().getSession().getAttribute("tit.nif") + " - "
					+ (String) UI.getCurrent().getSession().getAttribute("tit.nombretit") +" </h>"
					);
			
    

            
		
			dgenContrato.setValue((String) UI.getCurrent().getSession().getAttribute("tit.cia"));
			//dgenCliente.setValue((String) UI.getCurrent().getSession().getAttribute("tit.desccontra"));
			//dgenPolizas.setValue("P�li: " +(String) UI.getCurrent().getSession().getAttribute("tit.poliza") );
			

			dgenNif.setCaptionAsHtml(true);
			dgenNif.setContentMode(ContentMode.HTML);
			dgenNif.setValue("<h style='font-weight: bold;'>"+(String) UI.getCurrent().getSession().getAttribute("tit.domicilio")+"</h>");
			dgenTitular.setValue((String) UI.getCurrent().getSession().getAttribute("tit.despoblacion") + " - " 
						+ (String) UI.getCurrent().getSession().getAttribute("tit.descp")  
					);
			

			
			


			
			
			

	}
	

	
	@Override
	public void enter(ViewChangeEvent event) {

	}	


}