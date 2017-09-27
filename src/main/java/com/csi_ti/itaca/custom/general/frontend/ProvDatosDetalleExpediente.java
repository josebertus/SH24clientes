package com.csi_ti.itaca.custom.general.frontend;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csi_ti.itaca.architecture.tools.webmodule.pantallas.ItacaView;
import com.csi_ti.itaca.custom.general.frontend.utiles.ValidarComunicado;
import com.csi_ti.itaca.custom.general.frontend.ventanas.ProvVenCerrarExpediente;
import com.csi_ti.itaca.custom.general.server.jdbc.PAC_SHWEB_PROVEEDORES;
import com.csi_ti.itaca.custom.general.server.jdbc.WS_AMA;
import com.csi_ti.itaca.custom.general.server.jdbc.util.ConexionFactoria;
import com.csi_ti.itaca.custom.general.server.service.GeneralBusinessServiceImpl;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@Theme("tests-valo-reindeer")
public class ProvDatosDetalleExpediente extends Panel implements ItacaView {


	public ArrayList lsCon = new ArrayList();
	public Button btCerrarExpediente = new Button("Cerrar Expediente");
	public CheckBox ckRevisar = new CheckBox("Revisar");
	public ProvVenCerrarExpediente provVenCerrarExpediente = new ProvVenCerrarExpediente(this);
	private ProvPantallaConsultaExpediente provPantallaConsultaExpedienteInicial;	
	private static GeneralBusinessServiceImpl service;
	    
    Property.ValueChangeListener listenerChgVip;
    
	Table tableTel = new Table();
	Label lCaption = new Label();
	Label dgenCausa =new Label("Causa:");
	Label dgenObservaciones =new Label("Observaciones:");
	
	/*Label dgenCPoblacion = new Label("Poblaci�n:");
	Label dgenDireccion = new Label("Direcci�n:");
	Label dgenPolizas =new Label();
	Label dgenPais = new Label();*/
	

	

	

	private static final long serialVersionUID = -304344441663815443L;

	// constructor inicial
	public ProvDatosDetalleExpediente( Map<String, Object> retorno, ProvPantallaConsultaExpediente provPantallaConsultaExpediente ) {

		provPantallaConsultaExpedienteInicial = provPantallaConsultaExpediente;
		service = (GeneralBusinessServiceImpl) UI.getCurrent().getSession().getAttribute("service");
		
		// TAB TITULAR
		GridLayout dtitLayout = new GridLayout(6,2);
		dtitLayout.setStyleName("layout-lineas-juntas");
		dtitLayout.addComponent(new Label("Causa:"),0,0);
		dgenCausa.setCaptionAsHtml(true);
		dgenCausa.setContentMode(ContentMode.HTML);
		dgenCausa.setValue("<h style='font-weight: normal;'>"+ retorno.get("CAUSA").toString()+" </h>");
		dtitLayout.addComponent(dgenCausa,1,0);
		

		dtitLayout.addComponent(new Label("Observaciones:"),0,1);
		dgenObservaciones.setCaptionAsHtml(true);
		dgenObservaciones.setContentMode(ContentMode.HTML);
		if ( retorno.get("OBSERVACIONES")==null )
			dgenObservaciones.setValue("<h style='font-weight: normal;'></h>");
		else
			dgenObservaciones.setValue("<h style='font-weight: normal;'>"+ retorno.get("OBSERVACIONES").toString()+" </h>");
		dtitLayout.addComponent(dgenObservaciones,1,1,5,1);
		
		
		dtitLayout.addComponent(btCerrarExpediente,3,0);
		btCerrarExpediente.setStyleName(ValoTheme.BUTTON_DANGER);
		dtitLayout.setComponentAlignment(btCerrarExpediente, Alignment.BOTTOM_CENTER);
		dtitLayout.addComponent(ckRevisar,5,0);
		dtitLayout.setComponentAlignment(ckRevisar, Alignment.MIDDLE_CENTER);
		
		if ( UI.getCurrent().getSession().getAttribute("revisar").toString().trim().equals("N")) {
			ckRevisar.setValue(false);	
		}
		else {
			ckRevisar.setValue(true);
		}
		
		dtitLayout.setWidth("100%");
		
		dtitLayout.setColumnExpandRatio(0,12);
		dtitLayout.setColumnExpandRatio(1,58);
		dtitLayout.setColumnExpandRatio(2,0);
		dtitLayout.setColumnExpandRatio(3,15);
		dtitLayout.setColumnExpandRatio(4,0);
		dtitLayout.setColumnExpandRatio(5,15);

				
		setCaption("Datos del Expediente");
		setContent(dtitLayout);
		
		// Validamos si tenemos que mostrar el bot�n de cerrar expediente
		
		if ( !ValidarComunicado.EsValido("CA") && !ValidarComunicado.EsValido("FT") ) {
			btCerrarExpediente.setVisible(false);
		}
		
		ckRevisar.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				//System.out.println("Hemos pulsado" + ckRevisar.getValue().toString());
				//ConexionFactoria.establecerConexion();
				WS_AMA llamada = null;
				try {
					llamada = new WS_AMA(service.plsqlDataSource.getConnection());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				HashMap respuesta = null;
				BigDecimal expbd = null;

				String  valor = null;
				if ( ckRevisar.getValue())
					valor = "S";
				else
					valor = "N";
			 
				try {
					respuesta = llamada.ejecutaWS_AMA__ACTUALIZAR_FLAG_REVISADO(
							UI.getCurrent().getSession().getAttribute("user").toString(),
							UI.getCurrent().getSession().getAttribute("origen").toString(),
							new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString()),
							valor
							);
					
					Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
					if ( retorno.get("CODIGOERROR").toString().equals("0"))
					
						new Notification("Proceso realizado",
								"Cambios de revisión realizado correctamente",
								Notification.Type.TRAY_NOTIFICATION, true)
								.show(Page.getCurrent());
					else
						new Notification("Se han producido un error",
								"Error al modificar el estado de Revisar \n" + retorno.get("TEXTOERROR"),
								Notification.Type.ERROR_MESSAGE, true)
								.show(Page.getCurrent());
					
				} catch (Exception e) {
					new Notification("Se han producido un error",
							"Error al modificar el estado de Revisar",
							Notification.Type.ERROR_MESSAGE, true)
							.show(Page.getCurrent());
					e.printStackTrace();
				}
			}
		});
		
		btCerrarExpediente.addClickListener( new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
					provVenCerrarExpediente.init();
					UI.getCurrent().addWindow(provVenCerrarExpediente);
				
			}
		});			
		
		


	}
	
	private class HorizontalRule extends Label {
		  public HorizontalRule() {
		    super("<hr style='color:gray;height:1px' />", ContentMode.HTML);
		  }
		}
	
	


	
	

	
	@Override
	public void enter(ViewChangeEvent event) {

	}
	
	public void recargarDatos( ) {

		// Recuperamos los comunicados
		
		//System.out.println(">>>>>>>>>> Recargamos comunicados con el expediente:" + new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString()));
		PAC_SHWEB_PROVEEDORES llamadaProv = null;
		try {
			llamadaProv = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuestaCom = null;

		try {
			respuestaCom = llamadaProv.ejecutaPAC_SHWEB_PROVEEDORES__F_COMUNICADOS_EXPEDIENTE(
					UI.getCurrent().getSession().getAttribute("user").toString(),
					UI.getCurrent().getSession().getAttribute("origen").toString(),
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString())
					);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Mostramos el estado del expediente
		PAC_SHWEB_PROVEEDORES llamada = null;
		try {
			llamada = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuesta = null;
		try {
			respuesta = llamada.ejecutaPAC_SHWEB_PROVEEDORES__F_ESTADO_EXPEDIENTE(
					UI.getCurrent().getSession().getAttribute("user").toString(),
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString())
					);
			
			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);

			UI.getCurrent().getSession().setAttribute("estadoExpediente",retorno.get("ESTADO").toString());
			
			provPantallaConsultaExpedienteInicial.setCaption("GESTIÓN DEL EXPEDIENTE Nº " + UI.getCurrent().getSession().getAttribute("expediente")
					+ " ( " + 
					UI.getCurrent().getSession().getAttribute("estadoExpediente") + " ) ");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}	
		
		// Maestro comunicados

		WS_AMA llamadaAMA = null;
		try {
			llamadaAMA = new WS_AMA(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuestaMaestro = null;
		
		//System.out.println("Llamammos maestro comunicados: " + UI.getCurrent().getSession().getAttribute("tipousuario").toString().substring(1,1));;
		try {
			// pPUSUARIO, pORIGEN, pTPCOMUNI, pTPUSUARIO, pESTADO)
			
			respuestaMaestro = llamadaAMA.ejecutaWS_AMA__MAESTRO_COMUNICADOS(
					UI.getCurrent().getSession().getAttribute("user").toString(),
					UI.getCurrent().getSession().getAttribute("origen").toString(),
					null,
					UI.getCurrent().getSession().getAttribute("tipousuario").toString().substring(1,1),
					UI.getCurrent().getSession().getAttribute("estadoExpediente").toString()
					);			

			
			Map<String, Object> retornoMaestro = new HashMap<String, Object>(respuestaMaestro);
			List<Map> valorMaestro = (List<Map>) retornoMaestro.get("COMUNICADOS");
			UI.getCurrent().getSession().setAttribute("comunicadosExpediente",valorMaestro);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UI.getCurrent().getSession().setAttribute("comunicadosExpediente",null);
		}
		
		// 	
		
		
		// Validamos si tenemos que mostrar el bot�n de cerrar expediente
		
		if ( !ValidarComunicado.EsValido("CA") && !ValidarComunicado.EsValido("FT") ) {
			provPantallaConsultaExpedienteInicial.provDatosDetalleExpediente.btCerrarExpediente.setVisible(false);
		}
		else {
			provPantallaConsultaExpedienteInicial.provDatosDetalleExpediente.btCerrarExpediente.setVisible(true);

			
			
		}
		// Mostramos botonera de cerrar expediente
		// Validamos si tenemos que mostrar el bot�n de cerrar expediente
		

	}



}