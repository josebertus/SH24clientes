
package com.sh24;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.servihogar.custom.general.api.service.GeneralBusinessServiceImpl;
import com.servihogar.custom.general.frontend.ProvPantallaBusquedaExpedientes;
import com.servihogar.custom.general.frontend.ProvPantallaConsultaExpediente;
import com.servihogar.custom.general.server.jdbc.PAC_SHWEB_CLIENTES;
import com.servihogar.custom.general.server.jdbc.PAC_SHWEB_LOGIN;
import com.servihogar.custom.general.server.jdbc.WS_AMA;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import oracle.jdbc.driver.OracleConnection;




@Theme("tests-valo-reindeer")
//@Widgetset("com.vaadin.addon.touchkit.gwt.TouchKitWidgetSet")
@Title("Web de Clientes" )
@SpringUI
public class Sh24clientesUI extends UI {
	
	
	
	@Autowired
	private GeneralBusinessServiceImpl service2;
	
	String token;
	
	String plataforma = "PWP";
	
	private ProvCustomPrincipal principal = new ProvCustomPrincipal();
	
	@Value("${conexion.usuario}")
	private String usuAdmin;
	
	@Value("${conexion.entorno}")
	private String usuEntorno;
	
	@Value("${conexion.password}")
	private String pwdAdmin;
	
	@Value("${conexion.url}")
	private String url;
	
	Connection conn = null;

	private static final long serialVersionUID = -4339363261243543395L;
	
	
	private VerticalLayout layout;

	Label lblProveedores = new Label("WEB de Clientes");
	private Button login;
	private Button btFocus = new Button();
	private TextField usuario;
	private TextField dniNif;
	private PasswordField password;
	private String msgErrorConexion;
	
	HorizontalLayout vEspera = new HorizontalLayout();
	Label lEspera = new Label("Conectando...");

	private FieldGroup binder;
	

	String entrada;
	Navigator navigator;
	
	/*@Override
	protected void init(VaadinRequest request) {
		
	    // Define a view
        class MyView extends NavigationView {
            public MyView() {
                super("Planet Details");

                CssLayout content = new CssLayout();
                setContent(content);

                VerticalComponentGroup group =
                        new VerticalComponentGroup();
                content.addComponent(group);

                group.addComponent(new TextField("Planet"));
                group.addComponent(new NumberField("Found"));
                group.addComponent(new Switch("Probed"));

                setRightComponent(new Button("OK"));
            }
        }

        // Use it as the content root
        setContent(new MyView());
    }*/
		
	
	@Override
	protected void init(VaadinRequest request) {
		
		System.out.println("El usuadmin es : " + usuAdmin);
		System.out.println("Service2: " + service2.toString());
		System.out.println(">>>>>>>>> " + entrada + " --> Sh24clientes" + request.getParameter("exp"));
		
		if ( request.getParameter("exp")==null || request.getParameter("exp")=="") {
			new Notification("Validación de entrada incorrecta",
					"No se ha podido realizar la validacion",
					Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			
			entrada = "login";
			return;
		}
		else {
			
			// Como viene el expediente encriptado informado
			// buscamos si existe en la tabla de mensajes
			
			PAC_SHWEB_CLIENTES llamada = null;
			try {
				llamada = new PAC_SHWEB_CLIENTES(service2.plsqlDataSource.getConnection());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HashMap respuesta = null;
			BigDecimal expbd = null;


			// Recuperamos los datos del Expediente;
			try {
				token = request.getParameter("exp");
				respuesta = llamada.ejecutaPAC_SHWEB_CLIENTES__F_VALIDAR_TOKEN_SMS(
						token , "-1"
						);

				
				Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
				
				// La cadena es correcta si devuleve un -3
				System.out.println("Código: " + retorno.get("CODIGOERROR"));				
				
				if ( !retorno.get("CODIGOERROR").toString().equals("-3")) {
					new Notification("Acceso incorrecto",
							"Datos de entrada inválidos",
							Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					return;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			
			entrada = "expediente";
		}
		
		System.out.println(">>> Entrada: " + entrada ); 
		
		
		
		//service2.ejecutarConsulta();
			
		VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1800);
		VerticalLayout main = new VerticalLayout();
		main.setHeight("100%");
		main.setWidth("100%");
		main.setMargin(true);
		main.setSpacing(false);
		main.setStyleName("mainlogo-panel");
		main.setSizeFull();
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setSizeUndefined();		
		
		Resource res = new ThemeResource("img/logosh24.jpg");
		Image image = new Image(null, res);
		image.setWidth("300px");
		layout.addComponent(image);
		layout.setComponentAlignment(image, Alignment.TOP_CENTER);

		System.out.println( "***** SH24CLIENTES E N T O R N O :>>>>" + usuEntorno );
		UI.getCurrent().getSession().setAttribute("url", url);		
		System.out.println( "***** URL: " + url );
		

		if ( entrada.equals("expediente" )) {
			dniNif = new TextField("Introduzca el Dni/Nif del titular:");
			dniNif.setWidth("300px");
			dniNif.setRequired(true);
			dniNif.setRequiredError("Dni/Nif Obligatorio");
			layout.addComponent(dniNif);
		}
		
		else { // login
			
			usuario = new TextField("Usuario:");
			usuario.setId("usuario");
			usuario.setWidth("300px");
			usuario.setRequired(true);
			usuario.setRequiredError("Usuario obligatorio");
			//usuario.setInputPrompt(messageResolver.getMessage("pantalla.login.usuario.inputExample"));
			usuario.setInvalidAllowed(true);
			usuario.setIcon(FontAwesome.USER);
			usuario.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
			usuario.setValidationVisible(false);
			//System.out.println("Pinemos el usuaruio:"+System.getProperty("user.name").toUpperCase());
			usuario.setValue(System.getProperty("user.name").toUpperCase());
	
			
			layout.addComponent(usuario);
	
			password = new PasswordField("Contraseña:");
			password.setId("contrasena");
			password.setWidth("300px");
			password.setRequired(true);
			password.setRequiredError("Contraseña obligatoria");
			password.setValue("");
			password.setIcon(FontAwesome.LOCK);
			password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
			password.setNullRepresentation("");
			password.setValidationVisible(false);
			layout.addComponent(password);
		
		}
		
		// Añadimos el mapa de google en función de la dirección del titular
		

		if ( usuEntorno.equals("TEST")) {
			login = new Button("ENTORNO DE PRUEBAS ¡¡¡¡ - Entrar");	
		} else {
			login = new Button("Entrar");
		}
		
		login.setId("login");
		login.setClickShortcut(KeyCode.ENTER);
		login.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				if ( entrada.equals("expediente" )) {

					   UI.getCurrent().getSession().setAttribute("expediente",(String) request.getParameter("exp"));
					   System.out.println("El expediente es: " + UI.getCurrent().getSession().getAttribute("expediente"));
					   UI.getCurrent().getSession().setAttribute("entorno",usuEntorno);
					   doLoginExpediente();
				}
				else
					   doLogin();
			}
		});
		
		layout.addComponent(login);
		layout.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);



		layout.addComponent(btFocus);
		btFocus.setStyleName("botoninvisible");

		
		vEspera.setMargin(false);
		vEspera.setSpacing(false);
		lEspera.setValue("");
		vEspera.setWidth("300px");
		vEspera.addComponent(lEspera);
		lEspera.setStyleName("blink");
		lEspera.setWidth("300px");
		vEspera.setHeight("20px");
		vEspera.setComponentAlignment(lEspera, Alignment.MIDDLE_CENTER);
		layout.addComponent(vEspera);
		
		layout.setComponentAlignment(vEspera, Alignment.MIDDLE_CENTER);
		

		layout.setStyleName("login-panel");



		//super.setCompositionRoot(layout);
		layout.setStyleName("loginfondo-panel");
		main.addComponent(layout);
		main.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
		main.setSizeFull();
		
		setContent(main);
		PropertysetItem item = new PropertysetItem();
		
		if ( entrada.equals("expediente" )) {
			
			item.addItemProperty("dniNif", new ObjectProperty<String>(""));
			
			
		}
		else {
			usuario.setTabIndex(1);
			password.setTabIndex(2);
			login.setTabIndex(3);
			btFocus.setTabIndex(4);
			usuario.focus();
			item.addItemProperty("usuario", new ObjectProperty<String>(""));
			item.addItemProperty("password", new ObjectProperty<String>(""));
			
						
		}
				
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);
		
		
		btFocus.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				// TODO Auto-generated method stub
				if ( entrada.equals("expediente" )) {
					dniNif.focus();
				}
				else
					usuario.focus();
				
			}
		});
		
		
		setErrorHandler(new ErrorHandler() {
			
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				// TODO Auto-generated method stub
				event.getThrowable().printStackTrace();
				System.out.println(event.getThrowable().getCause());
				new Notification("Se ha producido un error inesperado",
						event.getThrowable().toString(),
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				
			}
		});;
	}

private void doLoginExpediente( ) {
	

	try {
		binder.commit();
		
		// Una vez introducido el DNI validamos que es correcto junto al token de entrada
		PAC_SHWEB_CLIENTES llamada = null;
		try {
			llamada = new PAC_SHWEB_CLIENTES(service2.plsqlDataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap respuesta = null;
		BigDecimal expbd = null;


		// Recuperamos los datos del Expediente;
		try {
			respuesta = llamada.ejecutaPAC_SHWEB_CLIENTES__F_VALIDAR_TOKEN_SMS(
					token, dniNif.getValue()
					);

			
			Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
			
			// La cadena es correcta si devuleve un -3
			System.out.println("Validamos token con el dni: " + retorno.get("CODIGOERROR"));				
			
			if ( !retorno.get("CODIGOERROR").toString().equals("0")) {
				new Notification("Acceso incorrecto",
						"Código Dni/Nif del titular incorrecto",
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				return;
			}
			UI.getCurrent().getSession().setAttribute("expediente", retorno.get("EXPEDIENTE"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		System.out.println("*¨*************************************************");
		UI.getCurrent().getSession().setAttribute("service", service2);
	    principal.init();
		UI.getCurrent().setContent(principal);
		navigator = new Navigator(this, principal.getViewContainer());
		UI.getCurrent().setNavigator(navigator);
		
		UI.getCurrent().getSession().setAttribute("resoluciony", UI.getCurrent().getPage().getBrowserWindowHeight());
		//UI.getCurrent().getSession().setAttribute("user", usuario.getValue());
		UI.getCurrent().getSession().setAttribute("user", "AMA_ADMON");
		//UI.getCurrent().getSession().setAttribute("tipousuario", retLogin.get("TIPOUSUARIO"));
		//UI.getCurrent().getSession().setAttribute("posaltaexp", retLogin.get("POSALTAEXP"));
		//UI.getCurrent().getSession().setAttribute("username", nomUsu + " " + apeUsu);
		UI.getCurrent().getSession().setAttribute("entorno", usuEntorno);
		//UI.getCurrent().getSession().setAttribute("origen", plataforma );
		UI.getCurrent().getSession().setAttribute("origen", "BPM" );

		UI.getCurrent().getNavigator().addView("ProvPantallaConsultaExpediente", ProvPantallaConsultaExpediente.class);
		//UI.getCurrent().getNavigator().addView("ProvPantallaBusquedaExpedientes", ProvPantallaBusquedaExpedientes.class);
		UI.getCurrent().getNavigator().navigateTo("ProvPantallaConsultaExpediente");
		System.out.println("Navegamos");
	} catch (CommitException e) {
		new Notification("Campos obligatorios",
				"Faltan campos obligatorios por entrar",
				Notification.Type.TRAY_NOTIFICATION, true)
				.show(Page.getCurrent());

	}		
	
	
	

}


private void doLogin() {

		
	    boolean conexionOK;
		//Usuario0 user = null;

		usuario.setValidationVisible(true);
		password.setValidationVisible(true);
		try {
			binder.commit();

	        String URL = url;
	        String USER = usuario.getValue();
	        String PASS = password.getValue();
	        
	        java.util.Properties props = new java.util.Properties();
	        
	        props.setProperty("password",PASS);
	        props.setProperty("user",USER);
	        props.setProperty("program","<<<< PWP >>>>");
	        try {
				props.setProperty("machine", InetAddress.getLocalHost().getCanonicalHostName());
			} catch (UnknownHostException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
	        props.setProperty(OracleConnection.CONNECTION_PROPERTY_INSTANCE_NAME, "instancia**");
	        props.setProperty(
	        	       OracleConnection.CONNECTION_PROPERTY_THIN_VSESSION_PROGRAM,
	        	       "<<<< PWP >>>>" );
	        try {
				DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	        	        
	        
	        try {
				System.out.println("IP:"+Inet4Address.getLocalHost().getHostAddress());
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	        
	        
			//TODO
			try {
				msgErrorConexion ="";
				System.out.println("User:"+ USER + "PAS: "+ PASS + "URL:"+ URL);
				System.out.println("Primer intento de conexion");
				conn = DriverManager.getConnection(URL, props);
				conexionOK = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error conexion primer intento");
				msgErrorConexion = e.getMessage();
				try {
					System.out.println("Cerramos la conexion");
					if (conn==null) {
						System.out.println("No la cerramos ya que ya esta cerrada");
					}
					else {
						System.out.println("Cerramos la conexion 4");
						conn.close();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				conexionOK = false;
			
			}
			
			
			System.out.println("La conexión es: "+conexionOK);
			// USUARIO Y PWD CORRECTOS
			if (conexionOK)  {
				
					USER = "AMA_ADMON";
					PASS = "inicio";
	
					props.setProperty("password",PASS);
				    props.setProperty("user",USER);
				    
				    try {
						conn = DriverManager.getConnection(URL, props);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					System.out.println("Hacemos la conexion");
					PAC_SHWEB_LOGIN llamadaUsuario = new PAC_SHWEB_LOGIN(conn);
					HashMap respuesta = null;
					try {
						respuesta = llamadaUsuario.ejecutaPAC_SHWEB_LOGIN__F_LOGIN(usuario.getValue());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					String nomUsu = "";
					if ( respuesta.get("NOMBRE")==null ) {
						nomUsu = ""; 
					} else {
						nomUsu = (String)respuesta.get("NOMBRE");
					}
					String apeUsu = (String)respuesta.get("APELLIDOS");
					
					
					WS_AMA llamada = new WS_AMA(conn);
					respuesta = null;
					try {
						//respuesta = llamada.ejecutaPAC_SHWEB_LOGIN__F_LOGIN(usuario.getValue());
						respuesta = llamada.ejecutaWS_AMA__F_LOGIN(usuario.getValue(), plataforma);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("respuesta:"+respuesta.toString());
					Map<String, Object> retLogin = new HashMap<String, Object>(respuesta);

					String posAltaExp = (String)retLogin.get("POSALTAEXP");
					
					if ( !retLogin.get("CODIGOERROR").toString().equals("0")) {
						//System.out.println("Ha fallado la conexion");
						new Notification("Error de usuario/password",
								"Conexión denegada.<br>"
								+ "" + retLogin.get("TEXTOERROR"),
								Notification.Type.TRAY_NOTIFICATION, true)
						.show(Page.getCurrent());
							//log.info(messageResolver.getMessage("logging.badlocale", new Object[]{user.getIdioma(), localeResolver.getLocale().toLanguageTag()}));
						
						
					}
					else {
		
						System.out.println("]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]] La longitud es: " + UI.getCurrent().getPage().getBrowserWindowHeight() ) ;
						UI.getCurrent().getSession().setAttribute("resoluciony", UI.getCurrent().getPage().getBrowserWindowHeight());
						//UI.getCurrent().getSession().setAttribute("user", usuario.getValue());
						UI.getCurrent().getSession().setAttribute("user", "AMA_ADMON");
						UI.getCurrent().getSession().setAttribute("tipousuario", retLogin.get("TIPOUSUARIO"));
						UI.getCurrent().getSession().setAttribute("posaltaexp", retLogin.get("POSALTAEXP"));
						UI.getCurrent().getSession().setAttribute("username", nomUsu + " " + apeUsu);
						UI.getCurrent().getSession().setAttribute("entorno", usuEntorno);
						//UI.getCurrent().getSession().setAttribute("origen", plataforma );
						UI.getCurrent().getSession().setAttribute("origen", "BPM" );
						
						// Cargamos listas
						/*PAC_SHWEB_PROVEEDORES llamadaListas = new PAC_SHWEB_PROVEEDORES(conn);
						respuesta = null;
						try {
							//respuesta = llamada.ejecutaPAC_SHWEB_LOGIN__F_LOGIN(usuario.getValue());
							respuesta = llamadaListas.ejecutaPAC_SHWEB_PROVEEDORES__F_LISTA_ESTADOS_EXPEDIENTE("P");
							Map<String, Object> retListas= new HashMap<String, Object>(respuesta);
							List<Map> valorRespuesta = (List<Map>) retListas.get("REGISTROS");
							UI.getCurrent().getSession().setAttribute("estadosExpediente", valorRespuesta);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	*/					
						
						if ( retLogin.get("TIPOUSUARIO").toString().equals("S") )  {
							UI.getCurrent().getSession().setAttribute("tipousuario", "Supervisor");
						}
						else if ( retLogin.get("TIPOUSUARIO").toString().equals("P")  ) {
							UI.getCurrent().getSession().setAttribute("tipousuario", "Proveedor");
						}
						
						
						if (conn==null) {
							System.out.println("No la cerramos ya que ya esta cerrada 1");
						}
						else {
							try {
								System.out.println("Cerramos conexion 1.");
								conn.close();
								conn = null;
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
		
						System.out.println("*¨*************************************************");
						UI.getCurrent().getSession().setAttribute("service", service2);
					    principal.init();
						UI.getCurrent().setContent(principal);
						navigator = new Navigator(this, principal.getViewContainer());
						UI.getCurrent().setNavigator(navigator);
						
						UI.getCurrent().getNavigator().addView("ProvPantallaConsultaExpediente", ProvPantallaConsultaExpediente.class);
						UI.getCurrent().getNavigator().addView("ProvPantallaBusquedaExpedientes", ProvPantallaBusquedaExpedientes.class);
						UI.getCurrent().getNavigator().navigateTo("ProvPantallaBusquedaExpedientes");
						System.out.println("Navegamos");
					}
			
					
					
			}
			else {
				
				
				if (conn==null) {
					System.out.println("No la cerramos ya que ya esta cerrada");
				}
				else {
					try {
						System.out.println("cerramos conexion 3");
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//System.out.println("Ha fallado la conexion");
				new Notification("Error de usuario/password",
						"Conexión denegada.<br>"
						+ "" + msgErrorConexion,
						Notification.Type.TRAY_NOTIFICATION, true)
				.show(Page.getCurrent());
					//log.info(messageResolver.getMessage("logging.badlocale", new Object[]{user.getIdioma(), localeResolver.getLocale().toLanguageTag()}));
				
				
			}



		} catch (CommitException e) {
			new Notification("Campos obligatorios",
					"Campos obligatorios",
					Notification.Type.TRAY_NOTIFICATION, true)
					.show(Page.getCurrent());

		} 


}
	

}