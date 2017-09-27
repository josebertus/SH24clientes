package com.csi_ti.itaca.custom.general.frontend;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.csi_ti.itaca.architecture.tools.webmodule.componentes.ContenedorDeComponentes;
import com.csi_ti.itaca.architecture.tools.webmodule.componentes.WrapperComponentContainer;
import com.csi_ti.itaca.architecture.tools.webmodule.pantallas.PantallaBaseConInputOutput;
import com.csi_ti.itaca.custom.general.server.jdbc.PAC_SHWEB_PROVEEDORES;
import com.csi_ti.itaca.custom.general.server.jdbc.WS_AMA;
import com.csi_ti.itaca.custom.general.server.service.GeneralBusinessServiceImpl;
import com.google.gwt.thirdparty.common.css.compiler.ast.ParseException;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;
import com.vaadin.ui.themes.ValoTheme;


@Theme("tests-valo-reindeer")
@SpringUI
public class ProvPantallaBusquedaExpedientes extends PantallaBaseConInputOutput<VerticalLayout, Void, Void>{
	
	private FieldGroup binder;
	private FormLayout flayout;
	private HorizontalLayout hlayout;
	private FormLayout flayout2;
	private FormLayout flayout3;
	private HorizontalLayout flayout4;
	Label msgAlta;
	Button buscar;
	Button btConsultar;
	Button limpiar;
	String inTipoAlta;
	String inFiltro;
	GridLayout gl;
	int primeraEntrada = 0;
	
	HorizontalLayout panelAlta = new HorizontalLayout();
	HorizontalLayout hBotonera = new HorizontalLayout();


	private static final long serialVersionUID = 4440733679587692241L;
	
	Table tableexp;
	TextField nif;
	TextField poliza;
	TextField expediente;
	DateField dffecha;
	ComboBox estado;
	
	TextField telefono;
	TextField direccion;
	TextField apellidos;
	TextField nombre;
	ComboBox ckInc;
	ComboBox ckRev;
	Label lblInc;
	Label lblRev;
	ComboBox cias;
	ComboBox poblaciones;
	ComboBox provinexpediente;
	Label titulo;
	
	int validarCampos = 0;

	//ConexionFactoria service = new ConexionFactoria();
	
	@Autowired
	private GeneralBusinessServiceImpl service;
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		
		provinexpediente.setFilteringMode(FilteringMode.CONTAINS);

				
		tableexp.setPageLength(0);
		tableexp.setVisible(false);
		
		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("expediente", new ObjectProperty<String>(""));


		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);
		

		
		try {
			//System.out.println("Maquina:"+Inet4Address.getLocalHost().getHostName().toString());
			if (Inet4Address.getLocalHost().getHostName().toString().equals("port-116")
					&& UI.getCurrent().getSession().getAttribute("entorno").toString().equals("TEST") ) {
				poliza.setValue("A15372430SR"); // B2C

			
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// VALIDADORES
		
		estado.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

				try {
					ckInc.setVisible(false);
					ckRev.setVisible(false);
					lblInc.setVisible(false);
					lblRev.setVisible(false);
					if ( estado.getValue().equals(null)) {
						
						return;
					}
					if ( estado!=null && estado.getValue().equals("CUR")) {
						ckInc.setVisible(true);
						ckRev.setVisible(true);
						lblInc.setVisible(true);
						lblRev.setVisible(true);
					}
					else {
						ckInc.setVisible(false);
						ckRev.setVisible(false);
						lblInc.setVisible(false);
						lblRev.setVisible(false);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				}
			}
		});
		
		btConsultar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
		

				UI.getCurrent().getNavigator().navigateTo("ProvPantallaConsultaExpediente");
			}
			
		});
		
		/*
		 * 			UI.getCurrent().getSession().setAttribute("expedientebuscar",expediente.getValue());
			UI.getCurrent().getSession().setAttribute("fechabuscar",dffecha.getValue());
			UI.getCurrent().getSession().setAttribute("estadobuscar",estado.getValue());
		 */
		if ( UI.getCurrent().getSession().getAttribute("expedientebuscar") != null
			||UI.getCurrent().getSession().getAttribute("fechabuscar") != null
			|| UI.getCurrent().getSession().getAttribute("estadobuscar") != null) {
			expediente.setValue((String) UI.getCurrent().getSession().getAttribute("expedientebuscar"));
			dffecha.setValue((Date) UI.getCurrent().getSession().getAttribute("fechabuscar"));
			estado.setValue((String) UI.getCurrent().getSession().getAttribute("estadobuscar"));
			primeraEntrada = 1;

			ckRev.setValue(UI.getCurrent().getSession().getAttribute("ckRev"));
			ckInc.setValue(UI.getCurrent().getSession().getAttribute("ckInc"));

			
		}
		else {
			UI.getCurrent().getSession().setAttribute("estadobuscar","CON");
			estado.setValue("CON");
		}
		
		
		
		buscar.click();
	
		
		
		
	    

	}	
	
	
	//**************************************** BOTON LIMPIAR CAMPOS **************************************************

	public void limpiarButton(){
		
		tableexp.removeAllItems();
		tableexp.setVisible(false);
		gl.setVisible(false);
		poliza.clear();
		expediente.clear();
		dffecha.clear();
		estado.clear();
		ckInc.removeAllItems();
		ckRev.removeAllItems();
		ckInc.addItem("S");
		ckInc.setItemCaption("S","Si");
		ckInc.addItem("N");
		ckInc.setItemCaption("N","No");		
		
		ckRev.addItem("S");
		ckRev.setItemCaption("S","Si");
		ckRev.addItem("N");
		ckRev.setItemCaption("N","No");		
		telefono.clear();
		direccion.clear();
		nif.clear();
		apellidos.clear();
		nombre.clear();
		provinexpediente.clear();
		
		
	}
	
	//****************************************************************************************************************
	//*************************+              BUSCAR EXPEDIENTES *******************************************************
	//****************************************************************************************************************
	@SuppressWarnings({ "unchecked", "serial" })
	public void buscarButton(){		
		

		try {
			
			//System.out.println("Esntramos en buscarbutton");
			

			validarCampos = 1;
			//binder.commit();
			
			// Validaciones
			String mensajes = null;
			
			if ( !expediente.isValid() )  {
				mensajes = "Campo expediente incorrecto. Solo se permiten números \n";
			}
			else if ( ( dffecha.getValue()==null || dffecha.getValue().equals("") ) 
				&& ( expediente.getValue()==null || expediente.getValue().equals("") )
				&& ( estado.getValue()==null || estado.getValue().equals("") )
				
				&& primeraEntrada==1
				&& estado.getValue()!="CON") {
				mensajes = "Es obligatorio introducir el expediente, fecha o estado. \n";
			}
			else if ( ( expediente.getValue()==null || expediente.getValue().equals("") )
					&& ( estado.getValue().equals("CUR") )
					&& ( ckInc.getValue()==null  && ckRev.getValue()==null )
					) {

				mensajes = "Es situación CUR es obligatorio introducir expediente o Inc/Rev\n";				
			}			
			else if ( ( expediente.getValue()==null || expediente.getValue().equals("") )
					&& ( estado.getValue().equals("FIN") )) {
				mensajes = "Es situación FIN es obligatorio poner un expediente. \n";				
			}
			if ( mensajes!=null && primeraEntrada==1 ) {
				new Notification("Se han detectado errores",
						mensajes,
						Notification.Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());
			}
			else {
		
					tableexp.removeAllItems();
					gl.setVisible(false);
					tableexp.setVisible(false);			
					
					String inFiltros;
					int inVertodos = 0;
					String outMsgerror = null;
					
					
					
					try {

						buscarExpedientes();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			

	

					
		} catch (InvalidValueException e) {
			// TODO Auto-generated catch block
			//System.out.println("Error de validacion");
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	
	
	@Override
	protected ContenedorDeComponentes<VerticalLayout> crearCuerpo() {
		
		//System.out.println("Contenedor de componentes");
		
		HashMap respuesta = null;
		
		service = (GeneralBusinessServiceImpl) UI.getCurrent().getSession().getAttribute("service");
		
		System.out.println("------------Llamamos a obetener conecion");
		PAC_SHWEB_PROVEEDORES llamadaListas = null;
		try {
			llamadaListas = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("------------- DESPUES Llamamos a obetener conecion");
		respuesta = null;
		try {
			respuesta = llamadaListas.ejecutaPAC_SHWEB_PROVEEDORES__F_LISTA_ESTADOS_EXPEDIENTE("P");
			Map<String, Object> retListas= new HashMap<String, Object>(respuesta);
			List<Map> valorRespuesta = (List<Map>) retListas.get("REGISTROS");
			UI.getCurrent().getSession().setAttribute("estadosExpediente", valorRespuesta);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		VerticalLayout layout = new VerticalLayout();
		//layout.setSizeFull();
		layout.setMargin(false);
		

		
		tableexp = new Table()  {
		    @Override
		    protected String formatPropertyValue(Object rowId,
		            Object colId, Property property) {
		        // Format by property type
		        if (property.getType() == Date.class) {
		        	
		        	if (property.getValue() == null) {
		        		return null;
		        	}
		            SimpleDateFormat df =
		                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		            return df.format((Date)property.getValue());
		        }

		        return super.formatPropertyValue(rowId, colId, property);
		    }
		};
		
		expediente = new TextField("Expediente:");
		expediente.setWidth("100px");
		expediente.addValidator(new RegexpValidator("[0-9]*", "Expediente solo permite números"));
		

		poblaciones = new ComboBox("Poblacion:");
		poblaciones.setWidth("300px");
		poblaciones.setVisible(false);
		provinexpediente = new ComboBox("Provincia:");
		provinexpediente.setWidth("300px");
		provinexpediente.setVisible(false);

		Property.ValueChangeListener listener = new Property.ValueChangeListener() {
			@Override
		    public void valueChange(ValueChangeEvent event) {
				
				
				poblaciones.removeAllItems();
				poblaciones.setEnabled(false);
				
		    }


		};
		provinexpediente.addValueChangeListener(listener);
		
		titulo = new Label();
		nif = new TextField("NIf/Cif:");
		poliza = new TextField("N� P�liza:");
		telefono = new TextField("Tel�fono:");
		direccion = new TextField("Direcci�n:");
		apellidos = new TextField("Apellidos:");

		nombre = new TextField("Nombre:");
		
		dffecha = new DateField("Fecha Asignación:");
		dffecha.setWidth("100px");
		dffecha.setDateFormat("dd/MM/yyyy");

		estado = new ComboBox("Estado:");
		estado.setWidth("260px");
		

		estado.removeAllItems();
		List<Map> valor =  (List<Map>) UI.getCurrent().getSession().getAttribute("estadosExpediente");
		for (Map map : valor) {
			estado.addItem(map.get("CDESTADO"));
			estado.setItemCaption(map.get("CDESTADO"),map.get("DSESTADO").toString() + " - " + map.get("CDESTADO"));
		}
					

		
		
		hlayout = new HorizontalLayout();
		hlayout.setMargin(false);
		hlayout.setSpacing(false);
		
		
		flayout = new FormLayout();
		flayout.addComponent(expediente);

		/*flayout.addComponent(provinexpediente);		
		flayout.addComponent(poblaciones);*/

		flayout2 = new FormLayout();
		flayout2.setMargin(true);
		flayout2.addComponent(dffecha);
		poliza.setWidth("180px");
		
		poliza.setVisible(false);
		direccion.setVisible(false);
		telefono.setVisible(false);
		nombre.setVisible(false);
		apellidos.setVisible(false);
		nif.setVisible(false);
		
		
		flayout2.addComponent(telefono);
		
		telefono.addValidator(new RegexpValidator("[0-9]*", "Teléfono solo permite números"));
		telefono.setMaxLength(10);
		
		flayout2.addComponent(direccion);
		direccion.setWidth("180px");

		flayout3 = new FormLayout();
		flayout3.setMargin(true);
		flayout3.addComponent(estado);
		

		flayout4 = new HorizontalLayout();

		
		ckInc = new ComboBox();
		ckRev = new ComboBox();
		
		lblInc = new Label(" Inc:");
		lblRev = new Label(" Rev:");
		
		flayout4.setMargin(true);
		flayout4.addComponent(lblInc);
		flayout4.addComponent(ckInc);
		flayout4.addComponent(new Label("&nbsp;", ContentMode.HTML));
		flayout4.addComponent(lblRev);
		flayout4.addComponent(ckRev);
		
		ckInc.setWidth("45px");
		ckRev.setWidth("45px");
		flayout4.setComponentAlignment(lblInc, Alignment.MIDDLE_LEFT);
		flayout4.setComponentAlignment(lblRev, Alignment.MIDDLE_RIGHT);		
		flayout4.setComponentAlignment(ckInc, Alignment.MIDDLE_LEFT);
		flayout4.setComponentAlignment(ckRev, Alignment.MIDDLE_RIGHT);

	
		hlayout.addComponent(flayout);
		hlayout.addComponent(flayout2);
		hlayout.addComponent(flayout3);
		hlayout.addComponent(flayout4);
	    expediente.setImmediate(true);
	    expediente.setValidationVisible(true);

	    
	    expediente.setImmediate(true);
	    expediente.setValidationVisible(true);
	    expediente.addValidator(new Validator() {
			
		
		private static final long serialVersionUID = 1L;

		@Override
		public void validate(Object value) throws InvalidValueException {
				
				
				if (( expediente.getValue()==null || expediente.getValue().equals(""))
				&& ( nif.getValue()==null || nif.getValue().equals(""))
				&& ( poliza.getValue()==null || poliza.getValue().equals(""))
				&& validarCampos== 1) {
					
				 
						if ( telefono.getValue()!=null && !telefono.getValue().equals("") ) {
							
							expediente.setImmediate(true);
							expediente.setValidationVisible(true);
			                throw new InvalidValueException("Si informa el teléfono es obligatorio informar la Compañia");
						}
						if ( nombre.getValue()!=null && !nombre.getValue().equals("")) {

							expediente.setImmediate(true);
							expediente.setValidationVisible(true);
			                throw new InvalidValueException("Si informa el teléfono es obligatorio informar la Compañia");
						}
						if ( apellidos.getValue()!=null && !apellidos.getValue().equals("")) {
							
							expediente.setImmediate(true);
							expediente.setValidationVisible(true);
			                throw new InvalidValueException("Si informa el teléfono es obligatorio informar la Compañia");
						}
						if ( direccion.getValue()!=null && !direccion.getValue().equals("")) {
							
							expediente.setImmediate(true);
							expediente.setValidationVisible(true);
			                throw new InvalidValueException("Si informa el teléfono es obligatorio informar la Compañia");
						}
						
				}

				
			}
		});    
	    
		Util.addComponentsToLayoutInHorizontal(layout, Alignment.TOP_LEFT, "cuadrosinbarras", hlayout);
		expediente.focus();
		// BOTONERA 
		buscar = new Button("Buscar");
		buscar.setClickShortcut(KeyCode.ENTER);
		buscar.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buscar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				buscarButton();
			}
		});
		limpiar = new Button("Limpiar");
		limpiar.setStyleName(ValoTheme.BUTTON_DANGER);
		limpiar.addClickListener((e) -> limpiarButton());
		GridLayout hlbot = new GridLayout(4,1);
		hlbot.setMargin(true);
		hlbot.setWidth("100%");
		hlbot.addComponent(limpiar,2,0);
		hlbot.addComponent(buscar,3,0);
		layout.addComponent(hlbot);
		Button btFocus = new Button();
		//layout.addComponent(btFocus);
		hlbot.setComponentAlignment(limpiar, Alignment.MIDDLE_CENTER);
		hlbot.setComponentAlignment(buscar, Alignment.MIDDLE_RIGHT);
		hlbot.setColumnExpandRatio(0, 90);
		hlbot.setColumnExpandRatio(1, 5);
		hlbot.setColumnExpandRatio(2, 5);
		// TABLAS
		layout.setMargin(false);
		String[] columnsexp ={"Consultar","Fhvisita","Fhpc","Fhasignacion","Agrupacion","Expediente","Nombre","Direccion","Cp","Poblacion","Provincia","Estado","Incidencia","Revisar","Cia","Anular"};
		Object[] typesexp = {Button.class, Date.class, Date.class, Date.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,Button.class};
		Object[] visibleColumnsexp = 
				new Object[]{"Consultar","Expediente","Fhvisita","Fhpc","Fhasignacion","Nombre","Direccion","Cp","Poblacion","Provincia","Estado","Incidencia","Revisar"};
		Util.defineTable(tableexp, columnsexp, typesexp, visibleColumnsexp,true);
		tableexp.setColumnHeaders(new String[] {"Con.","Expediente","Fecha Visita","Fecha PC","Asignación","Nombre","Dirección","C.P.","Población","Provincia","Estado","Inc.","Rev."});

		tableexp.setColumnExpandRatio("Expediente", 9);
		tableexp.setColumnExpandRatio("Fhvisita", 8);
		tableexp.setColumnExpandRatio("Fhpc",8 );
		tableexp.setColumnExpandRatio("Fhasignacion",8 );
		tableexp.setColumnExpandRatio("Nombre", 20);
		tableexp.setColumnExpandRatio("Direccion", 12);
		tableexp.setColumnExpandRatio("Cp", 5);
		tableexp.setColumnExpandRatio("Poblacion", 16);
		tableexp.setColumnExpandRatio("Provincia", 9);
		tableexp.setColumnExpandRatio("Incidencia", 4);
		tableexp.setColumnExpandRatio("Revisar", 4);
		tableexp.setColumnExpandRatio("Anular", 5);
		
	
		tableexp.setTabIndex(-1);

		tableexp.setColumnAlignment("Coste", Align.RIGHT);		

		gl = new GridLayout(1,1);
		gl.setVisible(false);
		gl.setStyleName("box_verde");
		gl.setMargin(true);
		gl.setSpacing(true);
		gl.setWidth("100%");
		gl.setHeight("45px");

		btConsultar= new Button("Alta");
		btConsultar.setVisible(false);
		btConsultar.setStyleName(ValoTheme.BUTTON_PRIMARY);
		gl.addComponent(btConsultar);
		gl.setComponentAlignment(btConsultar, Alignment.MIDDLE_CENTER);
		msgAlta = new Label("No se ha encontrado ningún registro");
		gl.addComponent(msgAlta);
		gl.setComponentAlignment(msgAlta, Alignment.MIDDLE_CENTER);
		msgAlta.setVisible(false);

		layout.addComponent(gl);
		
		VerticalLayout vtable = new VerticalLayout();


		//new Responsive(table);
		//new Responsive(tableexp);
		
		ckInc.addItem("S");
		ckInc.setItemCaption("S","Si");
		ckInc.addItem("N");
		ckInc.setItemCaption("N","No");		
		
		ckRev.addItem("S");
		ckRev.setItemCaption("S","Si");
		ckRev.addItem("N");
		ckRev.setItemCaption("N","No");
		
		
		vtable.setMargin(true);
		vtable.addComponent(tableexp);
		layout.setMargin(false);
		layout.setStyleName("expediente-panel-busqueda");
		layout.addComponent(vtable);
		layout.setComponentAlignment(vtable, Alignment.MIDDLE_CENTER);		
                

		layout.setExpandRatio(vtable, 1);
		

		// NAVEGACION CAMPOS
		expediente.setTabIndex(1);
		provinexpediente.setTabIndex(2);
		poblaciones.setTabIndex(3);
		poliza.setTabIndex(4);
		telefono.setTabIndex(5);
		direccion.setTabIndex(6);
		nif.setTabIndex(7);
		apellidos.setTabIndex(8);
		nombre.setTabIndex(9);
		limpiar.setTabIndex(12);
		buscar.setTabIndex(13);
		btFocus.setTabIndex(14);
		btFocus.setStyleName("botoninvisible");
		
		btFocus.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				// TODO Auto-generated method stub
				expediente.focus();
				
			}
		});
		
		
		tableexp.setTabIndex(-1);
		tableexp.addItemClickListener(new ItemClickEvent.ItemClickListener() {
		    @SuppressWarnings("unchecked")
			@Override
		    public void itemClick(ItemClickEvent itemClickEvent) {
				// TODO Auto-generated method stub
				
				/*Property itemProperty;
				btConsultar.setVisible(true);
				btConsultar.setCaption("Consultar expediente: " + (String) itemClickEvent.getItem().getItemProperty("Expediente").getValue());
				*/
		    	btConsultar.setVisible(false);
				UI.getCurrent().getSession().setAttribute("expediente",(String) itemClickEvent.getItem().getItemProperty("Expediente").getValue());
				UI.getCurrent().getSession().setAttribute("tit.nombretit",(String) itemClickEvent.getItem().getItemProperty("Nombre").getValue());
				UI.getCurrent().getSession().setAttribute("tit.estadoexp",(String) itemClickEvent.getItem().getItemProperty("Estado").getValue());
				UI.getCurrent().getSession().setAttribute("tit.cia",(String) itemClickEvent.getItem().getItemProperty("Cia").getValue());
				UI.getCurrent().getSession().setAttribute("tit.direccion",(String) itemClickEvent.getItem().getItemProperty("Direccion").getValue());
				UI.getCurrent().getSession().setAttribute("tit.poblacion",(String) itemClickEvent.getItem().getItemProperty("Poblacion").getValue());
				UI.getCurrent().getSession().setAttribute("tit.provincia",(String) itemClickEvent.getItem().getItemProperty("Provincia").getValue());
				UI.getCurrent().getSession().setAttribute("tit.cp",(String) itemClickEvent.getItem().getItemProperty("Cp").getValue());
				
				//System.out.println("1.Este es el campo de revisar: "+itemClickEvent.getItem().getItemProperty("Revisar").getValue());;
				UI.getCurrent().getSession().setAttribute("revisar",(String) itemClickEvent.getItem().getItemProperty("Revisar").getValue());
				UI.getCurrent().getSession().setAttribute("estadoExpediente",EstadoExpediente());
				telefonosExpediente(new BigDecimal(itemClickEvent.getItem().getItemProperty("Expediente").getValue().toString()));
				
			}
		});
		
		tableexp.setCellStyleGenerator(new Table.CellStyleGenerator() {                
	        @Override
	        public String getStyle(Table source, Object itemId, Object propertyId) {

	        	
	        	
	            if(propertyId != null ) {
	            	
	                
	                
	                if ( propertyId.toString().equals("Incidencia")
	                		&& source.getItem(itemId).getItemProperty("Incidencia").getValue().equals("S")
	                	)  {
				
	                	return "bgred"; 
	                	
		 			}
	                if ( propertyId.toString().equals("Revisar")
	                		&& source.getItem(itemId).getItemProperty("Revisar").getValue().equals("S")
	                	)  {
				
	                	return "bgred"; 
	                	
		 			}
								                
	            	
	               
	                return "normal";
	            } else {
	                return null;
	            }
	        }
	      });		

		return new WrapperComponentContainer<VerticalLayout>(layout);
	}

	
	


	@Override
	protected void enlazarDatos() {
		// TODO Auto-generated method stub
		
	}
	
	private void buscarExpedientes() throws ParseException {
		//***************************************************************************************************
    	// TABLA DETALLE EXPEDIENTES
    	//***************************************************************************************************

    		 
		try {

			
			btConsultar.setVisible(false);
			msgAlta.setVisible(false);

			gl.setVisible(false);
			

			tableexp.removeAllItems();
			
			
			//System.out.println("llamamos a ejecutar Consulta");
			//service2.ejecutarConsulta();
			
			
			
			/*System.out.println("Buscamos los expedientes ");
			try {
				PAC_SHWEB_PROVEEDORES llamada = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Después de buscar");*/
			//System.out.println("Buscamos2");
			HashMap respuesta = null;
			BigDecimal expbd = null;
			if ( expediente.getValue()==null && expediente.getValue()=="" ) {
			}
			else {
				try {
				expbd = new BigDecimal(expediente.getValue().toString());
				}
				catch ( Exception e ) {}
			}
				
			try {
				SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
				String pfecha = null;
				String pestado = null;
				
				if  ( dffecha.getValue()==null  )  {
				}
				else {
					pfecha = formatoFecha.format(dffecha.getValue());
				}
				if  ( estado.getValue()==null)   {
				}
				else {
					pestado = estado.getValue().toString();
				}
				
				// sI ES LA PRIMERA ENTRADA EL ESTADO = 'CON'
				UI.getCurrent().getSession().setAttribute("expedientebuscar",expediente.getValue());
				UI.getCurrent().getSession().setAttribute("fechabuscar",dffecha.getValue());
				if ( primeraEntrada == 0 ) {
					UI.getCurrent().getSession().setAttribute("estadobuscar","CON");
					pestado = "CON";
				}
				else {
					UI.getCurrent().getSession().setAttribute("estadobuscar",estado.getValue());
				}	
				primeraEntrada = 1;
				
				//System.out.println("La fecha es: " + pfecha + " el estado " + pestado);
				
				String inc = null;
				String  rev=null;
				
				if ( estado.getValue().equals("CUR") )  {
					
					if ( ckInc.getValue()==null) {
						inc = null;
					} else {
						inc = ckInc.getValue().toString();
					}
					if ( ckRev.getValue()==null) {
						rev = null;
					} else {
						rev = ckRev.getValue().toString();
					}					
					
					
				}
				
				if ( estado.getValue().equals("CON")) {
				
					tableexp.setVisibleColumns(
							new Object[]{"Consultar","Expediente","Fhvisita","Fhpc","Fhasignacion","Nombre","Direccion","Cp","Poblacion","Provincia","Estado","Incidencia","Revisar"}
							);
				} else  {
	
						tableexp.setVisibleColumns(
								new Object[]{"Consultar","Expediente","Fhvisita","Fhasignacion","Nombre","Direccion","Cp","Poblacion","Provincia","Estado","Incidencia","Revisar"}
								);
				}
				
				System.out.println("Vamos a buscar " + UI.getCurrent().getSession().getAttribute("user").toString() +
						UI.getCurrent().getSession().getAttribute("origen").toString());
				
				PAC_SHWEB_PROVEEDORES llamada2 = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
				respuesta = llamada2.ejecutaPAC_SHWEB_PROVEEDORES__F_LISTA_EXPEDIENTE(
						expbd,
						UI.getCurrent().getSession().getAttribute("user").toString(),
						UI.getCurrent().getSession().getAttribute("origen").toString(),
						"semana",
						new BigDecimal("0"),
						pfecha,
						pestado,
						inc,
						rev
						);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.out.println("Después de buscar buscar");
			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
			
			if ( !retorno.get("CODIGOERROR").toString().equals("0")) {
				
				System.out.println("Error");
				new Notification("Error",
						retorno.get("TEXTOERROR").toString(),
						Notification.Type.ERROR_MESSAGE, true)
						.show(Page.getCurrent());
			}
			else if(retorno!=null) {

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					String fecha;
					Date date = null;
					List<Map> valor = (List<Map>) retorno.get("REGISTROS");
					//System.out.println("Registros hay" + valor.size());
					
					if ( valor.size()==0) {
						//System.out.println("no hay registros");
						gl.setStyleName("box_rojo");
						msgAlta.setVisible(true);
						gl.setVisible(true);
					} else {
						gl.setStyleName("box_verde");
						gl.setVisible(false);
						btConsultar.setVisible(true);
						msgAlta.setVisible(false);
					}
					btConsultar.setVisible(false);
					for (Map map : valor) {
						
						Object newItemId = tableexp.addItem();
						Item row1 = tableexp.getItem(newItemId);
						
						//row1.getItemProperty("Poliza").setValue(map.get("NUMPOL"));
						//row1.getItemProperty("Expediente").setValue(String.valueOf(map.get("NUMEXP")));
						
						Button btBuscar = new Button();
						btBuscar.setId(newItemId.toString());
						btBuscar.setData(newItemId);
						btBuscar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
						btBuscar.addStyleName(ChameleonTheme.BUTTON_DOWN);
						btBuscar.setIcon(FontAwesome.SEARCH);
						btBuscar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableexp.select(data);
								Item itemClickEvent = tableexp.getItem(data);
								btConsultar.setVisible(false);
								UI.getCurrent().getSession().setAttribute("expediente",(String) itemClickEvent.getItemProperty("Expediente").getValue());
								UI.getCurrent().getSession().setAttribute("tit.nombretit",(String) itemClickEvent.getItemProperty("Nombre").getValue());
								UI.getCurrent().getSession().setAttribute("tit.estadoexp",(String) itemClickEvent.getItemProperty("Estado").getValue());
								UI.getCurrent().getSession().setAttribute("tit.cia",(String) itemClickEvent.getItemProperty("Cia").getValue());
								UI.getCurrent().getSession().setAttribute("tit.direccion",(String) itemClickEvent.getItemProperty("Direccion").getValue());
								UI.getCurrent().getSession().setAttribute("tit.poblacion",(String) itemClickEvent.getItemProperty("Poblacion").getValue());
								UI.getCurrent().getSession().setAttribute("tit.provincia",(String) itemClickEvent.getItemProperty("Provincia").getValue());
								UI.getCurrent().getSession().setAttribute("tit.cp",(String) itemClickEvent.getItemProperty("Cp").getValue());
								UI.getCurrent().getSession().setAttribute("revisar",(String) itemClickEvent.getItemProperty("Revisar").getValue());
								UI.getCurrent().getSession().setAttribute("estadoExpediente",EstadoExpediente());
								telefonosExpediente(new BigDecimal(itemClickEvent.getItemProperty("Expediente").getValue().toString()));								
								
								UI.getCurrent().getSession().setAttribute("ordenColumna",tableexp.getSortContainerPropertyId());
								UI.getCurrent().getSession().setAttribute("orden",tableexp.isSortAscending());

								UI.getCurrent().getSession().setAttribute("ckRev",ckRev.getValue());
								UI.getCurrent().getSession().setAttribute("ckInc",ckInc.getValue());
								
								UI.getCurrent().getNavigator().navigateTo("ProvPantallaConsultaExpediente");		
							}
						});
						
						// Botón anular / Rechazar Expediente
						
						Button btAnular = new Button();
						btAnular.setId(newItemId.toString());
						btAnular.setData(newItemId);
						btAnular.addStyleName(ValoTheme.BUTTON_DANGER);
						btAnular.addStyleName(ChameleonTheme.BUTTON_DOWN);
						btAnular.setIcon(FontAwesome.GEAR);
						btAnular.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableexp.select(data);
								Item itemClickEvent = tableexp.getItem(data);
								btConsultar.setVisible(false);
		
							}
						});						
						
						
						
						date = null;
						row1.getItemProperty("Consultar").setValue(btBuscar);
						row1.getItemProperty("Anular").setValue(btAnular);
						
						// Fecha visita
						fecha = (String) map.get("FHVISITA");

							try {
								date = formatter.parse(fecha);
							} catch ( Exception e ) {
								
							}
						
						if (date!=null) row1.getItemProperty("Fhvisita").setValue(date);
						
						// Fecha asignacion
						// Fecha visita
						date = null;
						fecha = (String) map.get("FHASIGNACION");

							try {
								date = formatter.parse(fecha);
							} catch ( Exception e ) {
								
							}
						
						if (date!=null) row1.getItemProperty("Fhasignacion").setValue(date);
						
						// Fecha PC
						try {
							date = null;
	
								try {
									if ( map.get("FHPC")!=null ) {
										//System.out.println(map.get("FHPC"));
										date = formatter.parse((String) map.get("FHPC"));
									}
								} catch ( Exception e ) {
									e.printStackTrace();
								}
							
							if (date!=null) row1.getItemProperty("Fhpc").setValue(date);
						} catch (ReadOnlyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}							
						

						
	
						row1.getItemProperty("Agrupacion").setValue(map.get("AGRUPACION"));
						row1.getItemProperty("Expediente").setValue(map.get("IDEXPEDIENTE").toString());
						row1.getItemProperty("Nombre").setValue(map.get("NOMBRE"));
						row1.getItemProperty("Direccion").setValue(map.get("DIRECCION"));
						row1.getItemProperty("Cp").setValue(map.get("CP"));
						row1.getItemProperty("Poblacion").setValue(map.get("POBLACION"));
						row1.getItemProperty("Provincia").setValue(map.get("PROVINCIA"));
						row1.getItemProperty("Estado").setValue(map.get("ESTADOEXP"));
						row1.getItemProperty("Incidencia").setValue(map.get("INCIDENCIA"));
						row1.getItemProperty("Revisar").setValue(map.get("REVISAR"));
						row1.getItemProperty("Cia").setValue(map.get("CIA"));
						

					}
					
					tableexp.setFooterVisible(true);
					tableexp.setColumnFooter("Expediente", "Total: "+String.valueOf(tableexp.size()));
			
					
					tableexp.setVisible(true);
					tableexp.setSelectable(true);
					tableexp.setImmediate(true);
					tableexp.setPageLength((int)UI.getCurrent().getSession().getAttribute("resoluciony")/30);
					//tableexp.setHeight("33%");
					
					//System.out.println("Recuperamos el orden ?" + UI.getCurrent().getSession().getAttribute("orden"));
					//System.out.println("Recuperamos el ordenColumna ?" + UI.getCurrent().getSession().getAttribute("ordenColumna"));
					tableexp.setSortContainerPropertyId(UI.getCurrent().getSession().getAttribute("ordenColumna"));
					if ( UI.getCurrent().getSession().getAttribute("orden")!=null) {
						tableexp.setSortAscending((boolean) UI.getCurrent().getSession().getAttribute("orden"));
						tableexp.sort();
						

						
					}
					
					

		

			}
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			//System.out.println("Error");
			e.printStackTrace();
			

		}		    
		
	}
	
	public String EstadoExpediente() {
		
		
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
		
			ComunicadosExpediente(retorno.get("ESTADO").toString());
			
			return (String) retorno.get("ESTADO");			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public void ComunicadosExpediente( String estado) {
		
		
		
		WS_AMA llamada = null;
		try {
			llamada = new WS_AMA(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuesta = null;
		
		//System.out.println("Llamammos maestro comunicados: " + UI.getCurrent().getSession().getAttribute("tipousuario").toString().substring(1,1));;
		try {
			// pPUSUARIO, pORIGEN, pTPCOMUNI, pTPUSUARIO, pESTADO)
			respuesta = llamada.ejecutaWS_AMA__MAESTRO_COMUNICADOS(
					UI.getCurrent().getSession().getAttribute("user").toString(),
					UI.getCurrent().getSession().getAttribute("origen").toString(),
					null,
					UI.getCurrent().getSession().getAttribute("tipousuario").toString().substring(1,1),
					estado
					);
			
			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
			List<Map> valor = (List<Map>) retorno.get("COMUNICADOS");
			UI.getCurrent().getSession().setAttribute("comunicadosExpediente",valor);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UI.getCurrent().getSession().setAttribute("comunicadosExpediente",null);
		}

		

		
	}
		
	
	public void telefonosExpediente( java.math.BigDecimal pEXPEDIENTE ) {
		
		
		PAC_SHWEB_PROVEEDORES llamada = null;
		try {
			llamada = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuesta = null;
		
		//System.out.println("TELEFONOS EXPEDIENTE: " ) ;
		try {
			// pPUSUARIO, pORIGEN, pTPCOMUNI, pTPUSUARIO, pESTADO)
			respuesta = llamada.ejecutaPAC_SHWEB_PROVEEDORES__F_LISTA_TELEFONOS_EXPEDIENTE(pEXPEDIENTE);
			
			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
			List<Map> valor = (List<Map>) retorno.get("REGISTROS");
			UI.getCurrent().getSession().setAttribute("telefonosExpediente",valor);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UI.getCurrent().getSession().setAttribute("telefonosExpediente",null);
		}
		
	}
	
}
