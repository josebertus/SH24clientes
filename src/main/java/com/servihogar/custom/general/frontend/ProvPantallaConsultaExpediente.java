package com.servihogar.custom.general.frontend;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csi_ti.itaca.architecture.tools.webmodule.pantallas.ItacaView;
import com.servihogar.custom.general.api.service.GeneralBusinessServiceImpl;
import com.servihogar.custom.general.server.jdbc.PAC_SHWEB_CLIENTES;
import com.servihogar.custom.general.server.jdbc.WS_AMA;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@Theme("tests-valo-reindeer")
public class ProvPantallaConsultaExpediente extends Panel implements ItacaView {

	public TabSheet tabExp = new TabSheet();
	private static GeneralBusinessServiceImpl service;
	
	ProvDatosTitularExpediente provDatosTitularExpediente;
	ProvDatosDetalleExpediente provDatosDetalleExpediente;
	ProvDatosObservacionesExpediente provDatosObservacionesExpediente;
	ProvDatosComunicadosExpediente provDatosComunicadosExpediente;
	
	
	VerticalLayout tabObservaciones = new VerticalLayout();
	VerticalLayout tabComunicados = new VerticalLayout();
	
	List<Map> valor;
	List<Map> valorDetalle;
	Map<String, Object> retornoTelefonos;
	
	VerticalLayout layoutPrincipal = new VerticalLayout();
	
	public ProvPantallaConsultaExpediente () {
		
		//System.out.println("entramos en el constructor");
		service = (GeneralBusinessServiceImpl) UI.getCurrent().getSession().getAttribute("service");
		setStyleName("panel-detalle");
		setCaption("EXPEDIENTE: " + UI.getCurrent().getSession().getAttribute("expediente"));
				
		
		
		// Recuperamos los datos del expediente
		// Recuperamos los datos del expediente
		// Recuperamos los datos del expediente
		// Recuperamos los datos del expediente
		
			
		// EstablecerConexion();
		PAC_SHWEB_CLIENTES llamada = null;
		HashMap respuesta = null;
		try {
			llamada = new PAC_SHWEB_CLIENTES(service.plsqlDataSource.getConnection());			
			respuesta = llamada.ejecutaPAC_SHWEB_CLIENTES__DATOSFIJOEXPEDIENTE(
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString())
					);
			
			valor = (List<Map>) respuesta.get("EXPEDIENTE");
			valorDetalle = (List<Map>) respuesta.get("DETALLE");
			System.out.println("************* Valor >>>>>>>> " + valor.get(0).toString());
			System.out.println("************* ValorDetalle >>>>>>>> " + valorDetalle.get(0).toString());
			
			llamada = new PAC_SHWEB_CLIENTES(service.plsqlDataSource.getConnection());
			respuesta = llamada.ejecutaPAC_SHWEB_CLIENTES__F_OBTENER_TELEFONOS(
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString())
					);
			retornoTelefonos = new HashMap<String, Object>(respuesta);
			List<Map> valor = (List<Map>) retornoTelefonos.get("TELEFONOS");			
			System.out.println("Respuesta: " + valor);
			
			/*llamada = new PAC_SHWEB_CLIENTES(service.plsqlDataSource.getConnection());
			respuesta = llamada.ejecutaPAC_SHWEB_CLIENTES__F_RELACION_SEGUIMIENTOS(
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString())
					);
			System.out.println("respuestaComunicados: " + respuesta);*/
				
					
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		provDatosDetalleExpediente = new ProvDatosDetalleExpediente(this, valor, valorDetalle);
		provDatosTitularExpediente = new ProvDatosTitularExpediente(valor);
		provDatosObservacionesExpediente = new ProvDatosObservacionesExpediente();
		
		//Map<String, Object> retornoComunicados = new HashMap<String, Object>(null);
		provDatosComunicadosExpediente = new ProvDatosComunicadosExpediente(null,this);
		provDatosComunicadosExpediente.recargarComunicados();
		
				
		// Detalle del expediente
		
		// Recuperamos los comunicados
		WS_AMA detalleExpedienteLlamada = null;
		try {
			detalleExpedienteLlamada = new WS_AMA(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuestaDetalleExpediente = null;

		//PUSUARIO, pORIGEN, pEXPEDIENTE).ejecutaPAC_SHWEB_PROVEEDORES__F_COMUNICADOS_EXPEDIENTE(
		try {
			respuestaDetalleExpediente = detalleExpedienteLlamada.ejecutaWS_AMA__F_DETALLE_EXPEDIENTE(
					UI.getCurrent().getSession().getAttribute("user").toString(),
					UI.getCurrent().getSession().getAttribute("origen").toString(),
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString())
					);
			
			Map<String, Object> retornoDetalleExpediente = new HashMap<String, Object>(respuestaDetalleExpediente);
			UI.getCurrent().getSession().setAttribute("cliente", respuestaDetalleExpediente.get("IDCLIENTE"));
			UI.getCurrent().getSession().setAttribute("contrato", respuestaDetalleExpediente.get("IDCONTRATO"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		layoutPrincipal.setMargin(false);
		layoutPrincipal.setSizeFull();
		layoutPrincipal.addComponent(provDatosTitularExpediente);
		layoutPrincipal.addComponent(provDatosDetalleExpediente);
		
		// Añadimos los telefonos
		Table tbTelefonos = new Table();
		Button btTelefonos = new Button();
		
		
		VerticalLayout popupContent = new VerticalLayout();
		popupContent.setWidth("500px");
		popupContent.setHeight("300px");
		tbTelefonos.setWidth("100%");
		tbTelefonos.setHeight("100%");
		popupContent.addComponent(tbTelefonos);

		

		// The component itself
		PopupView popup = new PopupView("Consultar Teléfonos", popupContent);
		layoutPrincipal.addComponent(popup);
		layoutPrincipal.setComponentAlignment(popup, Alignment.MIDDLE_CENTER);
		
		
		String[] columnsexp ={"Descripcion","Telefono"};
		Object[] typesexp = {String.class,String.class};
		Object[] visibleColumnsexp = new Object[]{"Descripcion","Telefono"};
		Util.defineTable(tbTelefonos, columnsexp, typesexp, visibleColumnsexp,true);
		tbTelefonos.setColumnHeaders(new String[] {"Descripcion","Telefono" });

		tbTelefonos.setColumnExpandRatio("Descripcion", 70);
		tbTelefonos.setColumnExpandRatio("Telefono", 30);
		tbTelefonos.setColumnReorderingAllowed(false);
		tbTelefonos.setTabIndex(-1);
		tbTelefonos.setPageLength(7);
		
		// telefonos
		tbTelefonos.removeAllItems();
		
		//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		if ( retornoTelefonos.get("TELEFONOS")!=null ) {;
		//System.out.println("*********************************");
			List<Map> valor = (List<Map>) retornoTelefonos.get("TELEFONOS");	;

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
		
		
		// A�adimos las pesta�as
		
		VerticalLayout vTabs = new VerticalLayout();
		vTabs.setMargin(true);
		
		tabExp.addTab(tabComunicados,"COMUNICADOS");
		//tabExp.addTab(tabObservaciones,"OBSERVACIONES");		
		
		tabExp.setSizeFull();
		tabObservaciones.setSizeFull();
		tabComunicados.setSizeFull();
		vTabs.setSizeFull();
		vTabs.addComponent(tabExp);
		
		// Cambiamos la pestaña
		tabExp.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub
		
				//System.out.println("Cambios la pestaña " + tabExp.getTabPosition(tabExp.getTab(tabExp.getSelectedTab())));
				
				/*if ( tabExp.getTabPosition(tabExp.getTab(tabExp.getSelectedTab()))==1) {
					
					
					}*/
 			
			}
		});

		
		//tabExp.setStyleName(ValoTheme.TABSHEET_FRAMED);
		tabExp.setStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		
		provDatosObservacionesExpediente.setWidth("100%");
		provDatosObservacionesExpediente.setHeight("100%");
		tabObservaciones.addComponent(provDatosObservacionesExpediente);
		//tabComunicados.addComponent(provDatosComunicadosExpediente);
				
		layoutPrincipal.addComponent(provDatosComunicadosExpediente);
		layoutPrincipal.setSizeFull();
		
		// Finalmente a�adimos la el layout principal donde hemos puestos el resto de componentes.
		setContent(layoutPrincipal);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
		
	}
	
}
