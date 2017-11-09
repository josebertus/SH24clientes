package com.sh24;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.csi_ti.itaca.architecture.tools.configurator.Configurator;
import com.csi_ti.itaca.architecture.tools.i18n.ItacaMessageResolver;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;

@UIScope
@SpringComponent
public class CliCustomPrincipal extends CustomComponent {
	
	private static final long serialVersionUID = -641725931242567777L;

	private VerticalLayout layout;
	private HorizontalLayout topLayout;
	
	//private LogTablas logUsuario;
	private ProvMenuPrincipal menuPrincipal;

	private HorizontalLayout menuLayout;
	
	private Button logout;

	private VerticalLayout main;
	private Label lblProveedores = new Label("WEB Proveedores");

	@Getter
	private Panel viewContainer;

	@Autowired
	private ItacaMessageResolver messageResolver;

	@Autowired
	private Configurator configurator;

	public void init(  ) {
		
		// TODO Auto-generated method stub
		
		// LAYOUT PRINCIPAL
		
		System.out.println("Entramos al init");
		layout = new VerticalLayout();
		layout.setSizeFull();
		
		layout.setMargin(false);
		layout.setSpacing(false);

		Resource res = new ThemeResource("img/logosh24.jpg");
		Image image = new Image(null, res);
		image.setHeight("30px");
		
		Resource resMenu;
		Image imageMenu = null;

		
	
		
		// LAYOUT LA BARRA DE MENU
		HorizontalLayout menuLayout = new HorizontalLayout();
		menuLayout.setMargin(false);
		menuLayout.addComponent(image);
		menuLayout.setComponentAlignment(image, Alignment.TOP_LEFT);
		menuLayout.setStyleName("menu-panel");
		menuLayout.setWidth("100%");
		
		


		Label lbluser = new Label();
		String usuario = (String) UI.getCurrent().getSession().getAttribute("user");
		

		System.out.println("exp: >>>>>>>> " + UI.getCurrent().getSession().getAttribute("exp"));
		if ( UI.getCurrent().getSession().getAttribute("exp")==null) {
			String idsesion = "&nbsp;[&nbsp;"+(String) UI.getCurrent().getSession().getSession().getId()+"&nbsp;]&nbsp;&nbsp;";
			String usunombre = "&nbsp;[&nbsp;"+(String) UI.getCurrent().getSession().getAttribute("username")+"&nbsp;]&nbsp;&nbsp;";
			String tipousuario = "<b>&nbsp;[&nbsp;"+(String) UI.getCurrent().getSession().getAttribute("tipousuario")+"&nbsp;]&nbsp;&nbsp;</b>";
			StringBuffer strb = new StringBuffer();   
			
			if ( UI.getCurrent().getSession().getAttribute("entorno").equals("TEST" )) {
					strb.append("<p style='background-color:orange;height:15px;font-size:10px'><b>Usuario:&nbsp;&nbsp;</b>")
					.append(usuario)
					.append(usunombre)
					.append(tipousuario)
					
					//.append(idsesion)
					.append("</p>");
			} else {
				strb.append("<p style='height:25px;font-size:10px'><b>Usuario:&nbsp;&nbsp;</b>")
				.append(usuario)
				.append(usunombre)
				.append(tipousuario)
				
				.append("</p>");
				
			}
			
			
		
			lbluser.setCaption(strb.toString());   
			lbluser.setCaptionAsHtml(true);
		}
		

		logout = new Button("Desconectar");
		logout.addClickListener((e) -> doLogout());
		logout.setStyleName(ValoTheme.BUTTON_DANGER);
		logout.setWidth("90px");
		


		HorizontalLayout barraH = new HorizontalLayout();
		barraH.setWidth("100%");
		barraH.setHeight("32px");
		
		
		HorizontalLayout menuCliente = new HorizontalLayout();
		
		//menuCliente.addComponent(lbluser);
		//menuCliente.setComponentAlignment(lbluser, Alignment.TOP_LEFT);

		// LAYOUT LA BARRA DEL CLIENTE
		HashMap mapCSS = (HashMap) UI.getCurrent().getSession().getAttribute("cabecera");
		if ( mapCSS==null) {
		}
		else {
				List<Map> valorCSS = (List<Map>) mapCSS.get("RETURN");
				for (Map map : valorCSS) {
					
					resMenu = new ThemeResource("img/" + map.get("IMGLOGO"));
					imageMenu = new Image(null, resMenu);
					imageMenu.setHeight("34px");
					
					menuCliente.setStyleName("menu-panel-cliente");
					menuCliente.setPrimaryStyleName("estilo_" + map.get("AGRUPACION").toString().toUpperCase() );
					//layout.setPrimaryStyleName("fondo_rojo");
					
					Label lblCabecera = new Label(map.get("TEXTOCABECERA").toString());
					menuCliente.addComponent(lblCabecera);
					lblCabecera.setPrimaryStyleName("estilo_" + map.get("AGRUPACION").toString().toUpperCase() );
					menuCliente.setComponentAlignment(lblCabecera, Alignment.MIDDLE_LEFT);
					menuCliente.setHeight("38px");
					/*contrato.addItem(map.get("CDCONTRA"));
					contrato.setItemCaption(map.get("CDCONTRA"),(String)map.get("NBCONTRA"));*/
					
				}
				menuCliente.setMargin(false);
				if ( imageMenu!=null) {
					menuCliente.addComponent(imageMenu);
					menuCliente.setComponentAlignment(imageMenu, Alignment.MIDDLE_RIGHT);
					menuCliente.setWidth("100%");
				}
		}
		

		
		//barraH.addComponent(lbluser);
		barraH.addComponent(logout);
		barraH.setMargin(false);
		
		
		barraH.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);
		

		
		barraH.setCaptionAsHtml(true);
		
		logout.setVisible(true);
		logout.setTabIndex(-1);
		menuLayout.addComponent(barraH);
		menuLayout.setComponentAlignment(barraH, Alignment.TOP_LEFT);
		viewContainer = new Panel();
		viewContainer.setStyleName("views-panel");
		viewContainer.setSizeFull();
		layout.addComponent(menuLayout);
		layout.addComponent(menuCliente);
		layout.addComponent(viewContainer);
		  
		layout.setExpandRatio(viewContainer,1);
		
        super.setCompositionRoot(layout);
		super.setSizeFull();
		

		
		
	}



	
	
	/**
	 * MÃ©todo que realiza el logout de la aplicaciÃ³n.
	 */
	private void doLogout() {
		
		ConfirmDialog.Factory df = new DefaultConfirmDialogFactory() {

			
		    @Override
			public ConfirmDialog create(String caption, String message, String okCaption,
					String cancelCaption, String notOkCaption) {

		        ConfirmDialog  d = super.create(caption,message,okCaption, cancelCaption, notOkCaption
		               );

		        // Change the order of buttons
		        Button ok = d.getOkButton();
		        HorizontalLayout buttons = (HorizontalLayout) ok.getParent();
		        buttons.removeComponent(ok);
		        buttons.addComponent(ok,1);
		        buttons.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);

		        return d;
		    }

		};
		ConfirmDialog.setFactory(df);		
    	ConfirmDialog.show(UI.getCurrent(), "LOGOUT", "Seguro que desea desconectarse de la sesión ?",
    	        "Si", "No", new ConfirmDialog.Listener() {

    	            public void onClose(ConfirmDialog dialog) {
    	                if (dialog.isConfirmed()) {
    	                    // Confirmed to continue
    	                	//VaadinSession.getCurrent().getSession().invalidate();
    	                	
    	                	// Vaciamos las variables de session
    	                	UI.getCurrent().getSession().close();
    	                	getUI().close();
    	            		getUI().getPage().reload();
    	            		
    	                } else {
    	                    // User did not confirm
    	                    
    	                }
    	            }
    	        });
	
	}
	
   

}
