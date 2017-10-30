package com.servihogar.custom.general.frontend;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csi_ti.itaca.architecture.tools.webmodule.pantallas.ItacaView;
import com.servihogar.custom.general.api.service.GeneralBusinessServiceImpl;
import com.servihogar.custom.general.frontend.utiles.BotoneraDoble;
import com.servihogar.custom.general.frontend.ventanas.ProvVenComunicadoExpediente;
import com.servihogar.custom.general.server.jdbc.PAC_SHWEB_CLIENTES;
import com.servihogar.custom.general.server.jdbc.WS_AMA;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("tests-valo-reindeer")
public class ProvDatosComunicadosExpediente extends Panel implements ItacaView {
	
    //public TextArea texto = new TextArea();
    public Label contador = new Label("123");
    public Label contadorgrande = new Label("123");
    public int pendiente = 0;
    TextArea textoModal  = new TextArea();
    Window ventana = new Window();
    

    public Table tbCom = new Table();
    TextArea observaciones = new TextArea();
    //Button btComunicado = new Button("Nuevo Comunicado");
    
    ProvVenComunicadoExpediente provVenComunicadoExpediente;
    CliPantallaConsultaExpediente provPantallaconsultaExpedienteInicial;

    private static GeneralBusinessServiceImpl service;
	private static final long serialVersionUID = -304344441663815443L;

	// constructor inicial
	public ProvDatosComunicadosExpediente( Map<String, Object> retorno, CliPantallaConsultaExpediente provPantallaConsultaExpediente ) {

		service = (GeneralBusinessServiceImpl) UI.getCurrent().getSession().getAttribute("service");
		provPantallaconsultaExpedienteInicial = provPantallaConsultaExpediente;

		ProvVenComunicadoExpediente provVenComunicadoExpediente = new ProvVenComunicadoExpediente(this);
		
		// TAB TITULAR
		GridLayout dtitLayout = new GridLayout(1,2);
		dtitLayout.setStyleName("layout-lineas-juntas");

		// A�adimos los datos a dtitlayout
		

		String[] columnsexp ={"Fecha","Tipo","dstipcom","Gremio","Destino","Descripcion"};
		Object[] typesexp = {String.class,String.class,String.class,String.class,String.class,String.class,};
		Object[] visibleColumnsexp = new Object[]{"dstipcom","Gremio","Descripcion"};
		Util.defineTable(tbCom, columnsexp, typesexp, visibleColumnsexp,true);
		tbCom.setColumnHeaders(new String[] {"Tipo","Gremio","Observaciones" });

		//tbCom.setColumnExpandRatio("Fecha", 13);
		tbCom.setColumnExpandRatio("dstipcom", 25);
		tbCom.setColumnExpandRatio("Gremio", 10);
		//tbCom.setColumnExpandRatio("Destino", 13);
		tbCom.setColumnExpandRatio("Descripcion", 65 ) ;
		tbCom.setColumnReorderingAllowed(false);
		
		tbCom.setTabIndex(-1);

		
		
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fecha;
		Date date = null;
		/*List<Map> valor = (List<Map>) retorno.get("REGISTROS");
		//System.out.println("Registros hay" + valor.size());
		


		for (Map map : valor) {

			Object newItemId = tbCom.addItem();
			Item row1 = tbCom.getItem(newItemId);

			if ( !map.get("TIPO").toString().equals("PP")) {
			
				row1.getItemProperty("Fecha").setValue(map.get("FECHA"));
				row1.getItemProperty("Tipo").setValue(map.get("TIPO").toString());
				row1.getItemProperty("Origen").setValue(map.get("ORIGEN"));
				row1.getItemProperty("Destino").setValue(map.get("DESTINO"));
				row1.getItemProperty("Descripcion").setValue(map.get("TEXTO"));
			
			}

		}*/
		
		/*tbCom.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub

				observaciones.setReadOnly(false);
				observaciones.setValue("");
				if ( event.getItem().getItemProperty("Descripcion").getValue()!=null) {
					observaciones.setValue(event.getItem().getItemProperty("Descripcion").getValue().toString());
				}
				observaciones.setReadOnly(true);
			}
		});*/
		
		/*tbCom.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

				
				observaciones.setReadOnly(false);
				observaciones.setValue("");
			
				Item row1 = tbCom.getItem(tbCom.getValue());
				if ( row1 != null && row1.getItemProperty("Descripcion").getValue()!=null) {
					observaciones.setValue(row1.getItemProperty("Descripcion").getValue().toString());
				}
				observaciones.setReadOnly(true);
			}
		});*/
		

		tbCom.setCellStyleGenerator(new Table.CellStyleGenerator() {                
		        @Override
		        public String getStyle(Table source, Object itemId, Object propertyId) {

		            if(propertyId != null ) {
		                return "normal";
		            } else {
		                return null;
		            }
		        }
		      });	
		
		tbCom.setVisible(true);
		tbCom.setSelectable(true);
		tbCom.setImmediate(true);
		tbCom.setPageLength(10);
		dtitLayout.addComponent(tbCom);
		dtitLayout.setMargin(true);
		dtitLayout.setWidth("100%");
		observaciones.setReadOnly(true);
		observaciones.setVisible(false);
		/*observaciones.setWidth("100%");
		observaciones.setHeight("100%");
		observaciones.setRows((int)UI.getCurrent().getSession().getAttribute("resoluciony")/53);*/
		//observaciones.setRows(13);
		dtitLayout.addComponent(observaciones);
		/*dtitLayout.addComponent(btComunicado);
		btComunicado.setStyleName(ValoTheme.BUTTON_PRIMARY);
		dtitLayout.setComponentAlignment(btComunicado, Alignment.BOTTOM_CENTER);*/
		setContent(dtitLayout);
		// Validamos si tenemos que mostrar el bot�n de cerrar expediente
		/*if ( !ValidarComunicado.EsValido("PC") &&
			 !ValidarComunicado.EsValido("V1") &&
			 !ValidarComunicado.EsValido("RS") &&
			 !ValidarComunicado.EsValido("H")  &&
			 !ValidarComunicado.EsValido("IT") 
		   ) {
			
			btComunicado.setVisible(false);
		}*/		
		
		
		/*btComunicado.addClickListener( new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
					recargarComunicados();
					provVenComunicadoExpediente.init();
					UI.getCurrent().addWindow(provVenComunicadoExpediente);
				
			}
		});
		*/
		
		
		/*observaciones.addContextClickListener(new ContextClickListener() {
			
			@Override
			public void contextClick(ContextClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().addWindow(ventana);
				

                textoModal.focus();
				textoModal.setReadOnly(false);
				textoModal.setValue(observaciones.getValue());
				textoModal.setReadOnly(true);
			}
		});*/
		
		/*textoModal.addContextClickListener(new ContextClickListener() {
			
			@Override
			public void contextClick(ContextClickEvent event) {
				// TODO Auto-generated method stub
				ventana.close();
			}
		});	*/	
		
		
		
    	// VENTANA
    	
    	ventana.setWidth("800px");
    	ventana.setHeight("500px");
    	ventana.setStyleName("ventana_texto_ampliar");
    	ventana.center();
    	textoModal.setWidth("780px");
    	textoModal.setHeight("430px");
    	VerticalLayout vl = new VerticalLayout();

    	textoModal.setStyleName("texto_modal_ampliar");
    	vl.setMargin(true);
    	ventana.setContent(vl);
    	
   	

	    BotoneraDoble botonera = new BotoneraDoble();
	    vl.addComponent(textoModal);
    	vl.addComponent(botonera);
	    vl.setComponentAlignment(botonera, Alignment.BOTTOM_CENTER);
	    //vl.addComponent(contadorgrande);

		
		botonera.btCancelar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				

				ventana.close();
				
			}
		});
		
		botonera.btAceptar.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
				

				int len = pendiente - textoModal.getValue().length();
                contador.setValue(" "+String.valueOf(len));
                
                float ini = (float) textoModal.getValue().length();
                float fin = pendiente;
                float quedan = ( ini / fin )  * 100 ;
                
                
                if ( quedan  > 98 ) {
                	contador.setStyleName("contador_rojo");
                } else if ( quedan > 70 ) {
                	contador.setStyleName("contador_naranja");
                }
                else {
                	contador.setStyleName("contador_verde");
                } 
                //texto.focus();
				ventana.close();
				
			}
		});			

    	ventana.setContent(vl);
    	
    	botonera.btCancelar.setVisible(false);
    	contador.setVisible(false);
    	
    	// FIN VENTANA
    	
		setCaption("Seguimiento");

		
	}
	

	

	
	@Override
	public void enter(ViewChangeEvent event) {

	}
	
	public void recargarComunicados( ) {

		// Recuperamos los comunicados

		HashMap respuestaCom = null;
		PAC_SHWEB_CLIENTES llamadaComunicados = null;

		try {
			llamadaComunicados = new PAC_SHWEB_CLIENTES(service.plsqlDataSource.getConnection());
			respuestaCom = llamadaComunicados.ejecutaPAC_SHWEB_CLIENTES__F_RELACION_SEGUIMIENTOS(
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString())
					);
			System.out.println("respuestaComunicados: " + respuestaCom);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tbCom.removeAllItems();
		Map<String, Object> retornoComunicados = new HashMap<String, Object>(respuestaCom);
		List<Map> valor = (List<Map>) retornoComunicados.get("COMUNICADOS");

		for (Map map : valor) {

			Object newItemId = tbCom.addItem();
			Item row1 = tbCom.getItem(newItemId);

			row1.getItemProperty("dstipcom").setValue(map.get("TIPO"));
			row1.getItemProperty("Descripcion").setValue(map.get("OBSERVACIONES"));
			row1.getItemProperty("Gremio").setValue(map.get("SERVICIO"));
	
		}
		


		

	}


}