package com.servihogar.custom.general.frontend;

import java.math.BigDecimal;
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
import com.servihogar.custom.general.api.service.GeneralBusinessServiceImpl;
import com.servihogar.custom.general.server.jdbc.PAC_SHWEB_CLIENTES;
import com.servihogar.custom.general.server.jdbc.PAC_SHWEB_PROVEEDORES;
import com.servihogar.custom.general.server.jdbc.WEB_AMA;
import com.servihogar.custom.general.server.jdbc.WS_AMA;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.sass.internal.parser.ParseException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
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
public class CliPantallaBusquedaExpedientes extends PantallaBaseConInputOutput<VerticalLayout, Void, Void> {

	private FieldGroup binder;
	private FormLayout flayout;
	private HorizontalLayout hlayout;
	private FormLayout flayout2;
	private FormLayout flayout3;
	private FormLayout flayout4;
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
	TextField expedientecia;
	TextField movimientoEconomico;
	
	ComboBox contrato;
	ComboBox causa;
	ComboBox garantia;
	ComboBox estado;
	ComboBox tipo;
	
	TextField numexpIni;
	TextField numexpFin;

	DateField cierreIni;
	DateField cierreFin;
	
	DateField fecexpIni;
	DateField fecexpFin;
	
	DateField dffecha;

	TextField telefono;
	TextField direccion;
	TextField apellidos;
	TextField nombre;

	Label lblInc;
	Label lblRev;
	ComboBox cias;
	ComboBox poblaciones;
	ComboBox provinexpediente;
	Label titulo;

	int validarCampos = 0;

	// ConexionFactoria service = new ConexionFactoria();

	@Autowired
	private GeneralBusinessServiceImpl service;

	@Override
	public void enter(ViewChangeEvent event) {

		System.out.println("Enter");
		provinexpediente.setFilteringMode(FilteringMode.CONTAINS);

		tableexp.setPageLength(0);
		tableexp.setVisible(false);

		PropertysetItem item = new PropertysetItem();
		
		item.addItemProperty("poliza", new ObjectProperty<String>(""));
		item.addItemProperty("nif", new ObjectProperty<String>(""));
		
		item.addItemProperty("expediente", new ObjectProperty<String>(""));
		item.addItemProperty("expedientecia", new ObjectProperty<String>(""));
		item.addItemProperty("movimientoEconomico", new ObjectProperty<String>(""));
		item.addItemProperty("nombre", new ObjectProperty<String>(""));
		item.addItemProperty("apellidos", new ObjectProperty<String>(""));
		item.addItemProperty("numexpIni", new ObjectProperty<String>(""));
		item.addItemProperty("numexpFin", new ObjectProperty<String>(""));
		item.addItemProperty("contrato", new ObjectProperty<String>(""));
		//item.addItemProperty("causa", new ObjectProperty<BigDecimal>(null));
		item.addItemProperty("cierreIni", new ObjectProperty<String>(""));
		item.addItemProperty("cierreFin", new ObjectProperty<String>(""));
		item.addItemProperty("garantia", new ObjectProperty<String>(""));
		item.addItemProperty("telefono", new ObjectProperty<String>(""));
		item.addItemProperty("fecexpIni", new ObjectProperty<String>(""));
		item.addItemProperty("fecexpFin", new ObjectProperty<String>(""));		

		item.addItemProperty("estado", new ObjectProperty<String>(""));
		item.addItemProperty("direccion", new ObjectProperty<String>(""));
		item.addItemProperty("tipo", new ObjectProperty<String>(""));
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);



		// VALIDADORES

		btConsultar.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				UI.getCurrent().getNavigator().navigateTo("CliPantallaConsultaExpediente");
			}

		});

	}

	// **************************************** BOTON LIMPIAR CAMPOS
	// **************************************************

	public void limpiarButton() {

		tableexp.removeAllItems();
		tableexp.setVisible(false);
		gl.setVisible(false);
		poliza.clear();
		expediente.clear();
		expedientecia.clear();
		fecexpIni.clear();
		fecexpFin.clear();
		estado.clear();
		tipo.clear();
		movimientoEconomico.clear();
		numexpIni.clear();
		numexpFin.clear();
		telefono.clear();
		direccion.clear();
		nif.clear();
		apellidos.clear();
		nombre.clear();
		cierreIni.clear();
		cierreFin.clear();
		contrato.clear();
		causa.clear();
		garantia.clear();

	}

	// ****************************************************************************************************************
	// *************************+ BUSCAR EXPEDIENTES
	// *******************************************************
	// ****************************************************************************************************************
	@SuppressWarnings({ "unchecked", "serial" })
	public void buscarButton() {

		try {

			// System.out.println("Esntramos en buscarbutton");

			validarCampos = 1;
			binder.commit();

			// Validaciones
			String mensajes = null;

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
		} catch (CommitException e) {

			String mensajes = "";
            for (Field<?> field: binder.getFields()) {
                ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
                
                if (errMsg != null) {
                	////System.out.println("Error:"+errMsg.getFormattedHtmlMessage());
                	mensajes+=errMsg.getFormattedHtmlMessage();
                }
            }
			new Notification("Datos de búsqueda incorrectos",
					mensajes,
					Notification.Type.TRAY_NOTIFICATION, true)
					.show(Page.getCurrent());

		} catch (InvalidValueException e) {
			// TODO Auto-generated catch block
			// System.out.println("Error de validacion");
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

		System.out.println("Contenedor de componentes");

		HashMap respuesta = null;

		service = (GeneralBusinessServiceImpl) UI.getCurrent().getSession().getAttribute("service");

		/*
		 * System.out.println("------------Llamamos a obetener conecion");
		 * PAC_SHWEB_PROVEEDORES llamadaListas = null; try { llamadaListas = new
		 * PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection()); }
		 * catch (SQLException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } System.out.println(
		 * "------------- DESPUES Llamamos a obetener conecion"); respuesta =
		 * null; try { respuesta = llamadaListas.
		 * ejecutaPAC_SHWEB_PROVEEDORES__F_LISTA_ESTADOS_EXPEDIENTE("P");
		 * Map<String, Object> retListas= new HashMap<String,
		 * Object>(respuesta); List<Map> valorRespuesta = (List<Map>)
		 * retListas.get("REGISTROS");
		 * UI.getCurrent().getSession().setAttribute("estadosExpediente",
		 * valorRespuesta); } catch (Exception e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */

		VerticalLayout layout = new VerticalLayout();
		// layout.setSizeFull();
		layout.setMargin(false);

		tableexp = new Table() {
			@Override
			protected String formatPropertyValue(Object rowId, Object colId, Property property) {
				// Format by property type
				if (property.getType() == Date.class) {

					if (property.getValue() == null) {
						return null;
					}
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					return df.format((Date) property.getValue());
				}

				return super.formatPropertyValue(rowId, colId, property);
			}
		};

		expediente = new TextField("Exp. Servihogar:");
		expediente.setWidth("100px");

		expedientecia = new TextField("Expediente CIA:");
		expedientecia.addValidator(new RegexpValidator("[0-9]*", "Expediente Cia. solo permite números"));

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
		nif = new TextField("NIF:");
		poliza = new TextField("Póliza:");
		telefono = new TextField("Teléfono:");
		direccion = new TextField("Dirección:");
		nombre = new TextField("Nombre:");
		apellidos = new TextField("Apellidos:");
		movimientoEconomico = new TextField("Mov.Económico:");
		contrato = new ComboBox("Contrato:");
		
		contrato.removeAllItems();			
		HashMap mapContratos = (HashMap) UI.getCurrent().getSession().getAttribute("contratos");
		List<Map> valor = (List<Map>) mapContratos.get("RETURN");
		for (Map map : valor) {
			contrato.addItem(map.get("CDCONTRA"));
			contrato.setItemCaption(map.get("CDCONTRA"),(String)map.get("NBCONTRA"));
		}
		contrato.setFilteringMode(FilteringMode.CONTAINS);

		
		contrato.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
		
				causa.removeAllItems();
				if ( contrato.getValue()!=null) {
					WEB_AMA llamada = null;
					try {
						llamada = new WEB_AMA(service.plsqlDataSource.getConnection());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					HashMap respuesta = null;
					BigDecimal expbd = null;
					// Recuperamos los datos del Expediente;
					try {
						respuesta = llamada.ejecutaWEB_AMA__OBTENERCAUSAS(UI.getCurrent().getSession().getAttribute("user").toString(),contrato.getValue().toString(), new BigDecimal("0"));
						Map<String, Object> mapCausas = new HashMap<String, Object>(respuesta);
	
						List<Map> valor = (List<Map>) mapCausas.get("RETURN");
						for (Map map : valor) {
							causa.addItem(map.get("CDCAUSA"));
							causa.setItemCaption(map.get("CDCAUSA"),(String)map.get("NBCAUSA"));
						}
						causa.setFilteringMode(FilteringMode.CONTAINS);
						
							
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					llamada = null;
					WEB_AMA llamadaama = null;
					respuesta = null;
					expbd = null;
					try {
						llamadaama = new WEB_AMA(service.plsqlDataSource.getConnection());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						
						
						respuesta = llamadaama.ejecutaWEB_AMA__OBTENERGARANTIAS(UI.getCurrent().getSession().getAttribute("user").toString(), contrato.getValue().toString(), new BigDecimal("0"));
						Map<String, Object> mapGarantias = new HashMap<String, Object>(respuesta);
	
						List<Map> valor = (List<Map>) mapGarantias.get("RETURN");
						for (Map map : valor) {
							garantia.addItem(map.get("CDGARANT"));
							garantia.setItemCaption(map.get("CDGARANT"),(String)map.get("NBGARANT"));
						}
						garantia.setFilteringMode(FilteringMode.CONTAINS);					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					WEB_AMA llamadaama2 = null;
					respuesta = null;
					expbd = null;
					try {
						llamadaama2 = new WEB_AMA(service.plsqlDataSource.getConnection());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						respuesta = llamadaama2.ejecutaWEB_AMA__ESTADOSEXPEDIENTE();
						Map<String, Object> mapEstado = new HashMap<String, Object>(respuesta);
						//  tpsituac, dssituac
						List<Map> valor = (List<Map>) mapEstado.get("RETURN");
						for (Map map : valor) {
							estado.addItem(map.get("DSSITUAC"));
							estado.setItemCaption(map.get("DSSITUAC"),(String)map.get("TPSITUAC"));
						}
						estado.setFilteringMode(FilteringMode.CONTAINS);					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				}
			}
		});
		
		
		garantia = new ComboBox("Garantía:");
		estado = new ComboBox("Estado exp:");
		tipo = new ComboBox("Tipo:");
		
		tipo.removeAllItems();
		tipo.addItem("R");
		tipo.setItemCaption("R","Reparable");
		tipo.addItem("M");
		tipo.setItemCaption("M","Mixto");
		tipo.addItem("I");
		tipo.setItemCaption("I","Indemnizable");		
		
		
		causa = new ComboBox("Causa:");
		
		contrato.setWidth("130px");
		causa.setWidth("130px");
		garantia.setWidth("130px");
		
		numexpIni = new TextField("Num. Expediente Desde:");
		numexpFin = new TextField("Hasta:");

		cierreIni = new DateField("Fecha Cierre Desde:");
		cierreFin = new DateField("Hasta:");
		
		fecexpIni = new DateField("Fecha expediente Desde:");
		fecexpFin = new DateField("Hasta:");		
		
		
		cierreIni.setDateFormat("dd/MM/yyyy");
		cierreFin.setDateFormat("dd/MM/yyyy");
		fecexpIni.setDateFormat("dd/MM/yyyy");
		fecexpFin.setDateFormat("dd/MM/yyyy");
		

		

		/*
		 * dffecha = new DateField("Fecha Asignación:");
		 * dffecha.setWidth("100px"); dffecha.setDateFormat("dd/MM/yyyy");
		 * 
		 * estado = new ComboBox("Estado:"); estado.setWidth("260px");
		 * 
		 * 
		 * estado.removeAllItems(); List<Map> valor = (List<Map>)
		 * UI.getCurrent().getSession().getAttribute("estadosExpediente"); for
		 * (Map map : valor) { estado.addItem(map.get("CDESTADO"));
		 * estado.setItemCaption(map.get("CDESTADO"),map.get("DSESTADO").
		 * toString() + " - " + map.get("CDESTADO")); }
		 */

		hlayout = new HorizontalLayout();
		hlayout.setMargin(false);
		hlayout.setSpacing(false);

		flayout = new FormLayout();
		flayout.addComponent(poliza);
		flayout.addComponent(expediente);
		expediente.addValidator(new RegexpValidator("[0-9]*", "Expediente Sergihogar solo permite números"));
		flayout.addComponent(nombre);
		flayout.addComponent(contrato);
		flayout.addComponent(garantia);
		flayout.addComponent(estado);
		

		flayout2 = new FormLayout();
		flayout2.setMargin(true);
		flayout2.addComponent(nif);
		flayout2.addComponent(expedientecia);
		flayout2.addComponent(apellidos);
		flayout2.addComponent(causa);
		
		//poliza.setWidth("180px");

		

		flayout2.addComponent(telefono);

		telefono.addValidator(new RegexpValidator("[0-9]*", "Teléfono solo permite números"));
		telefono.setMaxLength(10);

		flayout2.addComponent(direccion);
		//direccion.setWidth("180px");

		flayout3 = new FormLayout();
		flayout3.setMargin(true);
		flayout3.addComponent(new Label(""));
		flayout3.addComponent(movimientoEconomico);
		movimientoEconomico.addValidator(new RegexpValidator("[0-9]*", "Movimiento económico solo permite números"));
		flayout3.addComponent(numexpIni);
		flayout3.addComponent(cierreIni);
		flayout3.addComponent(fecexpIni);
		flayout3.addComponent(tipo);
		

		flayout4 = new FormLayout();
		flayout4.setMargin(true);
		flayout4.addComponent(new Label(""));
		flayout4.addComponent(new Label(""));
		flayout4.addComponent(numexpFin);
		flayout4.addComponent(cierreFin);
		flayout4.addComponent(fecexpFin);



		// ckInc.setWidth("45px");
		// ckRev.setWidth("45px");
		// flayout4.setComponentAlignment(lblInc, Alignment.MIDDLE_LEFT);
		// flayout4.setComponentAlignment(lblRev, Alignment.MIDDLE_RIGHT);
		// flayout4.setComponentAlignment(ckInc, Alignment.MIDDLE_LEFT);
		// flayout4.setComponentAlignment(ckRev, Alignment.MIDDLE_RIGHT);

		hlayout.addComponent(flayout);
		hlayout.addComponent(flayout2);
		hlayout.addComponent(flayout3);
		hlayout.addComponent(flayout4);
		expediente.setImmediate(true);
		expediente.setValidationVisible(true);

		expediente.setImmediate(true);
		expediente.setValidationVisible(true);

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
		GridLayout hlbot = new GridLayout(4, 1);
		hlbot.setMargin(true);
		hlbot.setWidth("100%");
		hlbot.addComponent(limpiar, 2, 0);
		hlbot.addComponent(buscar, 3, 0);
		layout.addComponent(hlbot);
		
		
		//layout.setPrimaryStyleName("fondo_rojo");
		 
		Button btFocus = new Button();
		// layout.addComponent(btFocus);
		hlbot.setComponentAlignment(limpiar, Alignment.MIDDLE_CENTER);
		hlbot.setComponentAlignment(buscar, Alignment.MIDDLE_RIGHT);
		hlbot.setColumnExpandRatio(0, 90);
		hlbot.setColumnExpandRatio(1, 5);
		hlbot.setColumnExpandRatio(2, 5);
		// TABLAS
		layout.setMargin(false);
		String[] columnsexp = { "Consultar", "Expediente", "Póliza", "Contrato", "Causa", "Titular", "Fecha", "Estado",
				"SLA" };
		Object[] typesexp = { Button.class, String.class, String.class, String.class, String.class, String.class,
				String.class, String.class, String.class };
		Object[] visibleColumnsexp = new Object[] { "Consultar", "Expediente", "Póliza", "Contrato", "Causa", "Titular",
				"Fecha", "Estado", "SLA" };
		Util.defineTable(tableexp, columnsexp, typesexp, visibleColumnsexp, true);
		tableexp.setColumnHeaders(new String[] { "Consultar", "Expediente", "Póliza", "Contrato", "Causa", "Titular",
				"Fecha", "Estado", "SLA" });

		tableexp.setColumnExpandRatio("Consultar", 5);
		tableexp.setColumnExpandRatio("Expediente", 10);
		tableexp.setColumnExpandRatio("Póliza", 15);
		tableexp.setColumnExpandRatio("Contrato", 20);
		tableexp.setColumnExpandRatio("Causa", 15);
		tableexp.setColumnExpandRatio("Titular", 15);
		tableexp.setColumnExpandRatio("Fecha", 10);
		tableexp.setColumnExpandRatio("Estado", 10);
		tableexp.setColumnExpandRatio("SLA", 5);

		tableexp.setTabIndex(-1);

		tableexp.setColumnAlignment("Coste", Align.RIGHT);

		gl = new GridLayout(1, 1);
		gl.setVisible(false);
		gl.setStyleName("box_verde");
		gl.setMargin(true);
		gl.setSpacing(true);
		gl.setWidth("100%");
		gl.setHeight("45px");

		btConsultar = new Button("Alta");
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

		// new Responsive(table);
		// new Responsive(tableexp);

		vtable.setMargin(true);
		vtable.addComponent(tableexp);
		layout.setMargin(false);
		layout.setStyleName("expediente-panel-busqueda");
		layout.addComponent(vtable);
		layout.setComponentAlignment(vtable, Alignment.MIDDLE_CENTER);
		layout.setExpandRatio(vtable, 1);

		// NAVEGACION CAMPOS
		poliza.setTabIndex(1);
		expediente.setTabIndex(2);
		nombre.setTabIndex(3);
		contrato.setTabIndex(4);
		garantia.setTabIndex(5);
		estado.setTabIndex(6);
		nif.setTabIndex(7);
		expedientecia.setTabIndex(8);
		apellidos.setTabIndex(9);
		causa.setTabIndex(12);
		telefono.setTabIndex(13);
		direccion.setTabIndex(14);
		movimientoEconomico.setTabIndex(15);
		numexpIni.setTabIndex(16);
		numexpFin.setTabIndex(17);
		cierreIni.setTabIndex(18);
		cierreFin.setTabIndex(19);
		fecexpIni.setTabIndex(20);
		fecexpFin.setTabIndex(21);
		tipo.setTabIndex(22);
		btFocus.setTabIndex(23);
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

				/*
				 * Property itemProperty; btConsultar.setVisible(true);
				 * btConsultar.setCaption("Consultar expediente: " + (String)
				 * itemClickEvent.getItem().getItemProperty("Expediente").
				 * getValue());
				 */
				btConsultar.setVisible(false);
				UI.getCurrent().getSession().setAttribute("expediente",
						(String) itemClickEvent.getItem().getItemProperty("Expediente").getValue());
				UI.getCurrent().getSession().setAttribute("tit.nombretit",
						(String) itemClickEvent.getItem().getItemProperty("Titular").getValue());
				UI.getCurrent().getSession().setAttribute("tit.estadoexp",
						(String) itemClickEvent.getItem().getItemProperty("Estado").getValue());
				/*UI.getCurrent().getSession().setAttribute("tit.cia",
						(String) itemClickEvent.getItem().getItemProperty("Cia").getValue());
				UI.getCurrent().getSession().setAttribute("tit.direccion",
						(String) itemClickEvent.getItem().getItemProperty("Direccion").getValue());
				UI.getCurrent().getSession().setAttribute("tit.poblacion",
						(String) itemClickEvent.getItem().getItemProperty("Poblacion").getValue());
				UI.getCurrent().getSession().setAttribute("tit.provincia",
						(String) itemClickEvent.getItem().getItemProperty("Provincia").getValue());
				UI.getCurrent().getSession().setAttribute("tit.cp",
						(String) itemClickEvent.getItem().getItemProperty("Cp").getValue());

				// System.out.println("1.Este es el campo de revisar:
				// "+itemClickEvent.getItem().getItemProperty("Revisar").getValue());;
				UI.getCurrent().getSession().setAttribute("revisar",
						(String) itemClickEvent.getItem().getItemProperty("Revisar").getValue());
				UI.getCurrent().getSession().setAttribute("estadoExpediente", EstadoExpediente());
				telefonosExpediente(
						new BigDecimal(itemClickEvent.getItem().getItemProperty("Expediente").getValue().toString()));*/

			}
		});

		tableexp.setCellStyleGenerator(new Table.CellStyleGenerator() {
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {

				/*
				 * if(propertyId != null ) {
				 * 
				 * 
				 * 
				 * if ( propertyId.toString().equals("Incidencia") &&
				 * source.getItem(itemId).getItemProperty("Incidencia").getValue
				 * ().equals("S") ) {
				 * 
				 * return "bgred";
				 * 
				 * } if ( propertyId.toString().equals("Revisar") &&
				 * source.getItem(itemId).getItemProperty("Revisar").getValue().
				 * equals("S") ) {
				 * 
				 * return "bgred";
				 * 
				 * }
				 * 
				 * 
				 * 
				 * return "normal"; } else { return null; }
				 */
				return "normal";
			}
		});

		return new WrapperComponentContainer<VerticalLayout>(layout);
	}

	@Override
	protected void enlazarDatos() {
		// TODO Auto-generated method stub

	}

	private void buscarExpedientes() throws ParseException {
		// ***************************************************************************************************
		// TABLA DETALLE EXPEDIENTES
		// ***************************************************************************************************

		try {

			btConsultar.setVisible(false);
			msgAlta.setVisible(false);

			gl.setVisible(false);

			tableexp.removeAllItems();

			// System.out.println("llamamos a ejecutar Consulta");
			// service2.ejecutarConsulta();

			/*
			 * System.out.println("Buscamos los expedientes "); try {
			 * PAC_SHWEB_PROVEEDORES llamada = new
			 * PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection()); }
			 * catch (SQLException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } System.out.println("Después de buscar");
			 */
			// System.out.println("Buscamos2");
			HashMap respuesta = null;
			BigDecimal expbd = null;
			if (expediente.getValue() == null && expediente.getValue() == "") {
			} else {
				try {
					expbd = new BigDecimal(expediente.getValue().toString());
				} catch (Exception e) {
				}
			}

			try {
				// SimpleDateFormat formatoFecha = new
				// SimpleDateFormat("yyyyMMdd");
				// String pfecha = null;
				// String pestado = null;

				/*
				 * if ( dffecha.getValue()==null ) { } else { pfecha =
				 * formatoFecha.format(dffecha.getValue()); }
				 */
				/*
				 * if ( estado.getValue()==null) { } else { pestado =
				 * estado.getValue().toString(); }
				 */

				// sI ES LA PRIMERA ENTRADA EL ESTADO = 'CON'
				// UI.getCurrent().getSession().setAttribute("expedientebuscar",expediente.getValue());
				// UI.getCurrent().getSession().setAttribute("fechabuscar",dffecha.getValue());
				/*
				 * if ( primeraEntrada == 0 ) {
				 * UI.getCurrent().getSession().setAttribute("estadobuscar",
				 * "CON"); pestado = "CON"; } else {
				 * UI.getCurrent().getSession().setAttribute("estadobuscar",
				 * estado.getValue()); }
				 */
				primeraEntrada = 1;

				// System.out.println("La fecha es: " + pfecha + " el estado " +
				// pestado);

				String inc = null;
				String rev = null;

	

				System.out.println("Vamos a buscar " + UI.getCurrent().getSession().getAttribute("user").toString()
						+ UI.getCurrent().getSession().getAttribute("origen").toString());

			
				BigDecimal expcia = null;
				if (expedientecia.getValue() != null && expedientecia.getValue().length() > 0 ) {
					expcia = new BigDecimal(expedientecia.getValue().toString());
				}

				/*
				 *    FUNCTION obtener_query_expedientes (
					      p_in_usuario           IN   VARCHAR2,
					      p_in_exp_uah           IN   NUMBER,
					      p_in_poliza            IN   VARCHAR2,
					      p_in_exp_cia           IN   VARCHAR2,
					      p_in_mov_eco           IN   NUMBER,
					      p_in_nif               IN   VARCHAR2,
					      p_in_nombre            IN   VARCHAR2,
					      p_in_apellidos         IN   VARCHAR2,
					      p_in_contrato          IN   VARCHAR2,
					      p_in_causa             IN   NUMBER,
					      p_in_garantia          IN   VARCHAR2,
					      p_in_est_exp           IN   VARCHAR2,
					      p_in_fhexp_ini         IN   VARCHAR2,
					      p_in_fhexp_fin         IN   VARCHAR2,
					      p_in_fhexpcierre_ini   IN   VARCHAR2,
					      p_in_fhexpcierre_fin   IN   VARCHAR2,
					      p_in_numexp_ini        IN   NUMBER,
					      p_in_numexp_fin        IN   NUMBER,
					      p_in_tipo_sin          IN   VARCHAR2
					   )
					   
				 */
				BigDecimal pmovco = null;
				if ( movimientoEconomico.getValue()!=null && movimientoEconomico.getValue()!=""  ) {
					pmovco = new BigDecimal((String)movimientoEconomico.getValue());
				}
				BigDecimal pcausa = null;
				if ( causa.getValue()!=null && causa.getValue()!=""  ) {
					pcausa = new BigDecimal(String.valueOf(causa.getValue()));
				}
				BigDecimal pnumexpini = null;
				if ( numexpIni.getValue()!=null && numexpIni.getValue()!=""  ) {
					pnumexpini = new BigDecimal((String)numexpIni.getValue());
				}
				BigDecimal pnumexpfin = null;
				if ( numexpFin.getValue()!=null && numexpFin.getValue()!=""  ) {
					pnumexpfin = new BigDecimal((String)numexpFin.getValue());
				}
				String pfexpini = null;
				if ( fecexpIni.getValue()!=null  ) {
					pfexpini = String.valueOf(fecexpIni.getValue());
				}
				String pfexpfin = null;
				if ( fecexpFin.getValue()!=null  ) {
					pfexpfin = String.valueOf(fecexpFin.getValue());
				}
				String pcierreini = null;
				if ( cierreIni.getValue()!=null  ) {
					pcierreini = String.valueOf(cierreIni.getValue());
				}
				String pcierrefin = null;
				if ( cierreFin.getValue()!=null  ) {
					pcierrefin = String.valueOf(cierreFin.getValue());
				}	
				String ptipo = null;
				if ( tipo.getValue()!=null && tipo.getValue()!=""  ) {
					ptipo = String.valueOf(tipo.getValue());

				}				
				
				
				PAC_SHWEB_CLIENTES llamada2 = new PAC_SHWEB_CLIENTES(service.plsqlDataSource.getConnection());
				respuesta = llamada2.ejecutaPAC_SHWEB_CLIENTES__OBTENER_QUERY_EXPEDIENTES(
						UI.getCurrent().getSession().getAttribute("user").toString(),
						expbd, 
						poliza.getValue(), 
						expcia,
						null, 
						nif.getValue(), 
						nombre.getValue(), 
						apellidos.getValue(), 
						(String)contrato.getValue(), 
						pcausa, 
						(String)garantia.getValue(), 
						(String)estado.getValue(), 
						pfexpini,
						pfexpfin,
						pcierreini,
						pcierrefin,						
						pnumexpini,
						pnumexpfin,
						ptipo
				);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Después de buscar buscar");
			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);


			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fecha;
			Date date = null;
			List<Map> valor = (List<Map>) retorno.get("REGISTROS");
			// System.out.println("Registros hay" + valor.size());

			if (valor==null || valor.size() == 0) {
				// System.out.println("no hay registros");
				gl.setStyleName("box_rojo");
				msgAlta.setVisible(true);
				gl.setVisible(true);
				return;
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
						Object data = event.getButton().getData();
						tableexp.select(data);
						Item itemClickEvent = tableexp.getItem(data);
						btConsultar.setVisible(false);
						UI.getCurrent().getSession().setAttribute("expediente",
								(String) itemClickEvent.getItemProperty("Expediente").getValue());

						telefonosExpediente(
								new BigDecimal(itemClickEvent.getItemProperty("Expediente").getValue().toString()));

						UI.getCurrent().getSession().setAttribute("ordenColumna",
								tableexp.getSortContainerPropertyId());
						UI.getCurrent().getSession().setAttribute("orden", tableexp.isSortAscending());

						UI.getCurrent().getNavigator().navigateTo("CliPantallaConsultaExpediente");
					}
				});

				date = null;
				row1.getItemProperty("Consultar").setValue(btBuscar);

				// Fecha visita
				fecha = (String) map.get("FHVISITA");

				try {
					date = formatter.parse(fecha);
				} catch (Exception e) {

				}

				if (date != null)
					row1.getItemProperty("Fhvisita").setValue(date);

				// Fecha asignacion
				// Fecha visita
				date = null;
				fecha = (String) map.get("FHASIGNACION");

				try {
					date = formatter.parse(fecha);
				} catch (Exception e) {

				}

				if (date != null)
					row1.getItemProperty("Fhasignacion").setValue(date);

				// Fecha PC
				try {
					date = null;

					try {
						if (map.get("FHPC") != null) {
							// System.out.println(map.get("FHPC"));
							date = formatter.parse((String) map.get("FHPC"));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (date != null)
						row1.getItemProperty("Fhpc").setValue(date);
				} catch (ReadOnlyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				row1.getItemProperty("Expediente").setValue(map.get("EXPEDIENTE").toString());

				row1.getItemProperty("Póliza").setValue(map.get("POLIZA"));
				row1.getItemProperty("Contrato").setValue(map.get("CONTRATO"));
				row1.getItemProperty("Causa").setValue(map.get("CAUSA"));
				row1.getItemProperty("Titular").setValue(map.get("TITULAR"));
				row1.getItemProperty("Fecha").setValue(map.get("FECHA_EXP"));
				row1.getItemProperty("Estado").setValue(map.get("ESTADO"));
				row1.getItemProperty("SLA").setValue(map.get("SLA").toString());

			}

			tableexp.setFooterVisible(true);
			tableexp.setColumnFooter("Expediente", "Total: " + String.valueOf(tableexp.size()));

			tableexp.setVisible(true);
			tableexp.setSelectable(true);
			tableexp.setImmediate(true);
			tableexp.setPageLength((int) UI.getCurrent().getSession().getAttribute("resoluciony") / 40);
			// tableexp.setHeight("33%");

			// System.out.println("Recuperamos el orden ?" +
			// UI.getCurrent().getSession().getAttribute("orden"));
			// System.out.println("Recuperamos el ordenColumna ?" +
			// UI.getCurrent().getSession().getAttribute("ordenColumna"));
			tableexp.setSortContainerPropertyId(UI.getCurrent().getSession().getAttribute("ordenColumna"));
			if (UI.getCurrent().getSession().getAttribute("orden") != null) {
				tableexp.setSortAscending((boolean) UI.getCurrent().getSession().getAttribute("orden"));
				tableexp.sort();

			}

			// }
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			// System.out.println("Error");
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
					new BigDecimal(UI.getCurrent().getSession().getAttribute("expediente").toString()));

			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);

			ComunicadosExpediente(retorno.get("ESTADO").toString());

			return (String) retorno.get("ESTADO");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void ComunicadosExpediente(String estado) {

		WS_AMA llamada = null;
		try {
			llamada = new WS_AMA(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuesta = null;

		// System.out.println("Llamammos maestro comunicados: " +
		// UI.getCurrent().getSession().getAttribute("tipousuario").toString().substring(1,1));;
		try {
			// pPUSUARIO, pORIGEN, pTPCOMUNI, pTPUSUARIO, pESTADO)
			respuesta = llamada.ejecutaWS_AMA__MAESTRO_COMUNICADOS(
					UI.getCurrent().getSession().getAttribute("user").toString(),
					UI.getCurrent().getSession().getAttribute("origen").toString(), null,
					UI.getCurrent().getSession().getAttribute("tipousuario").toString().substring(1, 1), estado);

			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
			List<Map> valor = (List<Map>) retorno.get("COMUNICADOS");
			UI.getCurrent().getSession().setAttribute("comunicadosExpediente", valor);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UI.getCurrent().getSession().setAttribute("comunicadosExpediente", null);
		}

	}

	public void telefonosExpediente(java.math.BigDecimal pEXPEDIENTE) {

		PAC_SHWEB_PROVEEDORES llamada = null;
		try {
			llamada = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuesta = null;

		// System.out.println("TELEFONOS EXPEDIENTE: " ) ;
		try {
			// pPUSUARIO, pORIGEN, pTPCOMUNI, pTPUSUARIO, pESTADO)
			respuesta = llamada.ejecutaPAC_SHWEB_PROVEEDORES__F_LISTA_TELEFONOS_EXPEDIENTE(pEXPEDIENTE);

			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
			List<Map> valor = (List<Map>) retorno.get("REGISTROS");
			UI.getCurrent().getSession().setAttribute("telefonosExpediente", valor);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UI.getCurrent().getSession().setAttribute("telefonosExpediente", null);
		}

	}

}
