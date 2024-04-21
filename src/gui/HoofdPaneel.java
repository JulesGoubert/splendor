package gui;

import java.util.Locale;
import java.util.ResourceBundle;

import domein.DomeinController;
import dto.SpelerDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HoofdPaneel extends BorderPane {
	
	private DomeinController dc;
	private final AanmeldPaneel aanmelden;
	private final RegistreerPaneel registreren;
	private LoginPaneel inloggen;
	private final Stage stage;
	private SpelerPaneel spelerPaneel;
	private SpelPaneel spel;
	private ResourceBundle rb = ResourceBundle.getBundle("res.bundle");
	
	public HoofdPaneel(Stage primaryStage, DomeinController dc) {
		this.stage = primaryStage;
		this.dc = dc;
		this.aanmelden = new AanmeldPaneel(this, dc, rb);
		this.inloggen = new LoginPaneel(dc, this, rb);
		this.registreren = new RegistreerPaneel(this, dc, rb, inloggen);
		this.spelerPaneel = null;
		this.spel = null;
		
		voegComponentenToe();
	}
	
	private final Label status = new Label();

	private void voegComponentenToe() {
		ImageView splendorLogo = new ImageView(getClass().getResource("/gui/images/logotransparant.png").toExternalForm());
		splendorLogo.setFitHeight(70);
		splendorLogo.setFitWidth(180);
		
		HBox titelBox = new HBox(splendorLogo);
		titelBox.setSpacing(10);
		titelBox.setPadding(new Insets(10));
		
		status.setText(rb.getString("welkom"));
		status.setUserData("welkom");
		status.setId("status");
		status.setMaxWidth(Double.MAX_VALUE);
		
		MenuBar menuBar = new MenuBar();
		ImageView taalIcoon = new ImageView(getClass().getResource("/gui/images/taal.png").toExternalForm());
		taalIcoon.setFitHeight(20);
		taalIcoon.setFitWidth(20);
		Menu taalMenu = new Menu("", taalIcoon);
		
		ImageView nlIcoon = new ImageView(getClass().getResource("/gui/images/nederlands.png").toExternalForm());;
		nlIcoon.setFitHeight(20);
		nlIcoon.setFitWidth(30);
		ImageView enIcoon = new ImageView(getClass().getResource("/gui/images/engels.png").toExternalForm());;
		enIcoon.setFitHeight(20);
		enIcoon.setFitWidth(30);
		MenuItem nlMenuItem = new MenuItem("NL", nlIcoon);
		MenuItem enMenuItem = new MenuItem("EN", enIcoon);
		
		nlMenuItem.setOnAction(e -> veranderTaal(1));
		enMenuItem.setOnAction(e -> veranderTaal(2));
		taalMenu.getItems().addAll(nlMenuItem, new SeparatorMenuItem(), enMenuItem);
		menuBar.getMenus().add(taalMenu);
		VBox top = new VBox(menuBar, titelBox, status);
		setTop(top);
		
		toonInloggen();
	}
	
	private void veranderTaal(int taal) {
		Locale locale = switch (taal) {
		case 1 -> new Locale("nl");
		case 2 -> new Locale("en", "GB");
		default -> Locale.getDefault();
		};
		
		rb = ResourceBundle.getBundle("res.bundle", locale);
		aanmelden.veranderTaal(rb);
		registreren.veranderTaal(rb);
		inloggen.veranderTaal(rb);
		
		if (status.getUserData().toString().equals("welkom")) {
			status.setText(rb.getString("welkom"));
		} else {
			vernieuwStatus();
		}
		
		if (spelerPaneel != null) {
			spelerPaneel.veranderTaal(rb);
		}
		
		if (spel != null) {
			spel.veranderTaal(rb);
		}
 	}

	public void toonRegistratie() {
		setCenter(registreren);
	}

	public void toonAanmelden() {
		setCenter(aanmelden);
	}
	
	public void toonInloggen() {
		setCenter(inloggen);
	}
	
	public void toonSpel() {
		setCenter(spel);
		veranderGrootte(720, 1100);
	}
	
	public void toonSpelerPaneel() {
		this.spelerPaneel = new SpelerPaneel(dc, this, rb);
		setCenter(spelerPaneel);
		veranderGrootte(650, 920);
	}
	
	public void startSpel() {
		dc.startSpel();
//		dc.maakWinnaars();
//		dc.preload();
//		dc.geefGenoegKaartenOmEdelenTeKopen();
		this.spel = new SpelPaneel(this, dc, rb);
		vernieuwStatus();
	}
	
	public void vernieuwStatus() {
		SpelerDTO spelerAanDeBeurt = dc.geefHuidigeSpeler();
		status.setText(String.format("%s%s", rb.getString("beurt"), spelerAanDeBeurt.gebruikersnaam()));
		status.setUserData("beurt");
	}
	
	public void reset() {
		this.dc = new DomeinController();
		this.inloggen = new LoginPaneel(dc, this, rb);
		veranderGrootte(450, 620);
		status.setText(rb.getString("welkom"));
		status.setUserData("welkom");
		toonInloggen();
	}
	
	private void veranderGrootte(int hoogte, int breedte) {
		stage.setHeight(hoogte);
		stage.setWidth(breedte);
		stage.centerOnScreen();
	}
 	
}
