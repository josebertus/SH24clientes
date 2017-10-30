package com.servihogar.custom.general.frontend;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csi_ti.itaca.architecture.tools.webmodule.pantallas.ItacaView;
import com.servihogar.custom.general.api.service.GeneralBusinessServiceImpl;
import com.servihogar.custom.general.server.jdbc.PAC_SHWEB_PROVEEDORES;
import com.servihogar.custom.general.server.jdbc.WS_AMA;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@Theme("tests-valo-reindeer")
public class ProvDatosDetalleExpediente extends Panel implements ItacaView {


	public ArrayList lsCon = new ArrayList();
	
	//public CheckBox ckRevisar = new CheckBox("Revisar");
	
	private CliPantallaConsultaExpediente provPantallaConsultaExpedienteInicial;	
	private static GeneralBusinessServiceImpl service;
	    
    Property.ValueChangeListener listenerChgVip;
    
	Table tableTel = new Table();
	Label lCaption = new Label();
	Label dgenEstado =new Label();
	Label dgenSolicitante =new Label();
	Label dgenFechaOcurrencia = new Label();
	Label dgenFechaExpediente = new Label();
	Label dgenFechaCierre = new Label();
	Label dgenNombre = new Label();
	Label dgenCausa = new Label();
	Label dgenLugarSiniestro = new Label();
	Label dgenExpCia = new Label();
	Label dgenExpSh = new Label();
	

	private static final long serialVersionUID = -304344441663815443L;

	// constructor inicial
	public ProvDatosDetalleExpediente( CliPantallaConsultaExpediente provPantallaConsultaExpediente, List<Map> retorno, List<Map> detalle ) {

		provPantallaConsultaExpedienteInicial = provPantallaConsultaExpediente;
		service = (GeneralBusinessServiceImpl) UI.getCurrent().getSession().getAttribute("service");
		
		// TAB TITULAR
		GridLayout dtitLayout = new GridLayout(10,5);
		dtitLayout.setStyleName("layout-lineas-juntas");
		
		dtitLayout.addComponent(new Label("Expediente Cia:"),0,0);
		dgenExpCia.setCaptionAsHtml(true);
		dgenExpCia.setContentMode(ContentMode.HTML);

		if ( retorno.get(0).get("EXPEDIENTE_CIA")==null ) {
			dgenExpCia.setValue("<h style='font-weight: normal;'>"+" </h>"	);
		} else {
			dgenExpCia.setValue("<h style='font-weight: normal;'>"+retorno.get(0).get("EXPEDIENTE_CIA")+" </h>"	);
		}
				
		dtitLayout.addComponent(dgenExpCia,1,0);
		
		//titLayout.addComponent(new Label("Expediente Asistencia:"),2,0,3,0);
		dgenExpSh.setCaptionAsHtml(true);
		dgenExpSh.setContentMode(ContentMode.HTML);
		dgenExpSh.setValue("<h style='font-weight: bold;'>Expediente: </h>"+"<h style='font-weight: normal;'>"+retorno.get(0).get("EXPEDIENTE_UAH").toString()+" </h>"	);		
		dtitLayout.addComponent(dgenExpSh,2,0,4,0);			
		
		
		dtitLayout.addComponent(new Label("Estado:"),0,1);
		dgenEstado.setCaptionAsHtml(true);
		dgenEstado.setContentMode(ContentMode.HTML);
		dgenEstado.setValue("<h style='font-weight: normal;'>"+ detalle.get(0).get("ESTADO").toString()+" </h>");
		dtitLayout.addComponent(dgenEstado,1,1);
		
		dtitLayout.addComponent(new Label("Fecha Ocurrencia:"),2,1);
		dgenFechaOcurrencia.setCaptionAsHtml(true);
		dgenFechaOcurrencia.setContentMode(ContentMode.HTML);
		dgenFechaOcurrencia.setValue("<h style='font-weight: normal;'>"+retorno.get(0).get("FHOCURRE").toString()+" </h>"	);		
		dtitLayout.addComponent(dgenFechaOcurrencia,3,1);
		
		dtitLayout.addComponent(new Label("Fecha Expediente:"),4,1);
		dgenFechaExpediente.setCaptionAsHtml(true);
		dgenFechaExpediente.setContentMode(ContentMode.HTML);
		dgenFechaExpediente.setValue("<h style='font-weight: normal;'>"+retorno.get(0).get("FHALTA").toString()+" </h>"	);		
		dtitLayout.addComponent(dgenFechaExpediente,5,1);
		
		dtitLayout.addComponent(new Label("Fecha Cierre:"),6,1);
		dgenFechaCierre.setCaptionAsHtml(true);
		dgenFechaCierre.setContentMode(ContentMode.HTML);
		dgenFechaCierre.setValue("<h style='font-weight: normal;'>"+retorno.get(0).get("FHCIERRE").toString()+" </h>"	);		
		dtitLayout.addComponent(dgenFechaCierre,7,1);
		

		dtitLayout.addComponent(new Label("Solicitante:"),0,2);
		dgenSolicitante.setCaptionAsHtml(true);
		dgenSolicitante.setContentMode(ContentMode.HTML);
		dgenSolicitante.setValue("<h style='font-weight: normal;'>"+detalle.get(0).get("SOLICITANTE").toString()+" </h>"	);
		dtitLayout.addComponent(dgenSolicitante,1,2);
		
		dtitLayout.addComponent(new Label("Nombre:"),2,2);
		dgenNombre.setCaptionAsHtml(true);
		dgenNombre.setContentMode(ContentMode.HTML);
		if ( detalle.get(0).get("NOMBRE")==null) {
			dgenNombre.setValue("<h style='font-weight: normal;'>"+" </h>"	);
		}
		else {
			dgenNombre.setValue("<h style='font-weight: normal;'>"+detalle.get(0).get("NOMBRE").toString()+" </h>"	);	
		}
		
		dtitLayout.addComponent(dgenNombre,3,2,8,2);
		
		dtitLayout.addComponent(new Label("Causa:"),0,3);
		dgenCausa.setCaptionAsHtml(true);
		dgenCausa.setContentMode(ContentMode.HTML);
		dgenCausa.setValue("<h style='font-weight: normal;'>"+detalle.get(0).get("CAUSA").toString()+" </h>"	);
		dtitLayout.addComponent(dgenCausa,1,3,8,3);
		
		dtitLayout.addComponent(new Label("Lugar del Siniestro:"),0,4);
		dgenLugarSiniestro.setCaptionAsHtml(true);
		dgenLugarSiniestro.setContentMode(ContentMode.HTML);
		dgenLugarSiniestro.setValue("<h style='font-weight: normal;'>"+detalle.get(0).get("LUGAR_SINIESTRO").toString()+" </h>"	);

		dtitLayout.addComponent(dgenLugarSiniestro,1,4,8,4);		
		
		

		dtitLayout.setWidth("100%");
		
		dtitLayout.setColumnExpandRatio(0,15);
		dtitLayout.setColumnExpandRatio(1,10);
		dtitLayout.setColumnExpandRatio(2,15);
		dtitLayout.setColumnExpandRatio(3,10);
		dtitLayout.setColumnExpandRatio(4,15);
		dtitLayout.setColumnExpandRatio(5,10);
		dtitLayout.setColumnExpandRatio(6,15);
		dtitLayout.setColumnExpandRatio(7,10);
		dtitLayout.setColumnExpandRatio(8,15);

				
		setCaption("Datos Expediente");
		setContent(dtitLayout);
		
		// Validamos si tenemos que mostrar el bot�n de cerrar expediente
		
	
		
		/*ckRevisar.addValueChangeListener(new ValueChangeListener() {
			
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
		});*/
		
		
		
		


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
		
		/*if ( !ValidarComunicado.EsValido("CA") && !ValidarComunicado.EsValido("FT") ) {
			provPantallaConsultaExpedienteInicial.provDatosDetalleExpediente.btCerrarExpediente.setVisible(false);
		}
		else {
			provPantallaConsultaExpedienteInicial.provDatosDetalleExpediente.btCerrarExpediente.setVisible(true);

			
			
		}*/
		// Mostramos botonera de cerrar expediente
		// Validamos si tenemos que mostrar el bot�n de cerrar expediente
		

	}



}