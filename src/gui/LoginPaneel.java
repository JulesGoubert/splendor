package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import domein.DomeinController;
import dto.SpelerDTO;
import exceptions.TeVeelSpelersException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginPaneel extends GridPane {

	private DomeinController dc;
	private HoofdPaneel hoofdPaneel;
	private List<SpelerDTO> aangemeldeSpelers;
	private ResourceBundle rb;

	public LoginPaneel(DomeinController dc, HoofdPaneel hoofdPaneel, ResourceBundle rb) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;
		this.rb = rb;
		this.aangemeldeSpelers = dc.geefSpelers(true);
		
		configureerGrid();
		voegComponentenToe();
	}

	private void configureerGrid() {
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
		
		col1.setPercentWidth(40);
		col2.setPercentWidth(20);
		col3.setPercentWidth(40);

		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(20));
		
		getColumnConstraints().addAll(col1, col2, col3);
	}
	
	private VBox left = new VBox();
	private VBox middle = new VBox();
	private VBox right = new VBox();
	private Button toevoegen;
	private Button verwijderen;
	private List<Label> lblTekstLijst = new ArrayList<>();
	private List<Button> btnTekstLijst = new ArrayList<>();

	private void voegComponentenToe() {
//		left.setStyle("-fx-background-color: lightgrey");
//		right.setStyle("-fx-background-color: lightgrey");
		left.setSpacing(5);
		middle.setSpacing(5);
		right.setSpacing(5);
		left.setPadding(new Insets(10));
		middle.setPadding(new Insets(10));
		right.setPadding(new Insets(10));
		middle.setAlignment(Pos.CENTER);
		
		Label lblSpelers = new Label(rb.getString("overzicht"));
		lblSpelers.setUserData("overzicht");
		lblSpelers.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		lblSpelers.setAlignment(Pos.CENTER);
		lblTekstLijst.add(lblSpelers);
		Label lblGeselecteerd = new Label(rb.getString("geselecteerd"));
		lblGeselecteerd.setUserData("geselecteerd");
		lblGeselecteerd.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		lblGeselecteerd.setAlignment(Pos.CENTER);
		lblTekstLijst.add(lblGeselecteerd);
		Button btnRegistreer = new Button(rb.getString("nieuweSpeler"));
		btnRegistreer.setUserData("nieuweSpeler");
		btnRegistreer.setOnAction(this::registreren);
		btnTekstLijst.add(btnRegistreer);
		Button btnStart = new Button(rb.getString("start"));
		btnStart.setUserData("start");
		btnStart.setOnAction(this::startSpel);
		btnTekstLijst.add(btnStart);
		
				
		for (SpelerDTO s : aangemeldeSpelers) {
			Button btn = new Button(String.format("%s", s.gebruikersnaam()));
			btn.setUserData(s);
			btn.setOnAction((ActionEvent event) -> {
				toevoegen = btn;
			});

			left.getChildren().add(btn);
		}
		
		// buttons in midden
		Button btnVoegToe = new Button(rb.getString("voegToe"));
		btnVoegToe.setUserData("voegToe");
		btnTekstLijst.add(btnVoegToe);
		Button btnVerwijder = new Button(rb.getString("verwijder"));
		btnVerwijder.setUserData("verwijder");
		btnTekstLijst.add(btnVerwijder);
		
		// buttons toevoegen op vbox
		middle.getChildren().addAll(btnVoegToe, btnVerwijder);

		// event koppelen aan klik op button
		btnVoegToe.setOnAction(this::voegSpelerToe);
		btnVerwijder.setOnAction(this::verwijderSpeler);

		// componenten op scherm zetten
		add(lblSpelers, 0, 0);
		add(lblGeselecteerd, 2, 0);
		ScrollPane sp1 = new ScrollPane();
		sp1.setContent(left);
		sp1.setPrefViewportHeight(180);
		sp1.setMaxHeight(180);
		add(sp1, 0, 2);
		add(middle, 1, 2);
		ScrollPane sp2 = new ScrollPane();
		sp2.setContent(right);
		sp2.setPrefViewportHeight(180);
		sp2.setMaxHeight(180);
		add(sp2, 2, 2);
		add(btnRegistreer, 0, 3);
		add(btnStart, 2, 3);
	}

	private void verwijderSpeler(ActionEvent event) {
		if (verwijderen == null) {
			return;
		}
		
		right.getChildren().remove(verwijderen);
		left.getChildren().add(verwijderen);
		
		for (Node n : left.getChildren()) {
			Button btn = (Button) n;
			btn.setOnAction((ActionEvent e) -> {
				toevoegen = btn;
			});
		}
		
		SpelerDTO speler = (SpelerDTO) verwijderen.getUserData();
		dc.meldAf(speler.gebruikersnaam(), speler.geboortejaar());
	}

	private void voegSpelerToe(ActionEvent event) {
		if (toevoegen == null) {
			return;
		}
		
		try {
			SpelerDTO speler = (SpelerDTO) toevoegen.getUserData();
			dc.meldAan(speler.gebruikersnaam(), speler.geboortejaar());
			
			left.getChildren().remove(toevoegen);
			right.getChildren().add(toevoegen);
			
			for (Node n : right.getChildren()) {
				Button btn = (Button) n;
				btn.setOnAction((ActionEvent e) -> {
					verwijderen = btn;
				});
			}
		} catch (TeVeelSpelersException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("foutmelding"));
			alert.setHeaderText(rb.getString("fout"));
			alert.setContentText(rb.getString(e.getMessage()));
			alert.showAndWait();
		} catch (IllegalArgumentException iae) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("foutmelding"));
			alert.setHeaderText(rb.getString("fout"));
			alert.setContentText(rb.getString(iae.getMessage()));
			alert.showAndWait();
		}
	}
	
	private void startSpel(ActionEvent event) {		
		try {			
			hoofdPaneel.startSpel();
			hoofdPaneel.toonSpelerPaneel();
		} catch (IllegalArgumentException iae) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("foutmelding"));
			alert.setHeaderText(rb.getString("fout"));
			alert.setContentText(rb.getString(iae.getMessage()));
			alert.showAndWait();
		}
	}
	
	private void registreren(ActionEvent event) {
		hoofdPaneel.toonRegistratie();
	}

	public void voegSpelerToeAanOnaangemeld(String gebruikersnaam) {
		Button btn = new Button(gebruikersnaam);
		btn.setUserData(geefSpeler(gebruikersnaam));
		btn.setOnAction((ActionEvent e) -> {
			toevoegen = btn;
		});
		left.getChildren().add(btn);
	}
	
	private SpelerDTO geefSpeler(String gebruikersnaam) {
		aangemeldeSpelers = dc.geefSpelers(true);
		
		for (SpelerDTO s : aangemeldeSpelers) {
			if (s.gebruikersnaam().equals(gebruikersnaam)) {
				return s;
			}
		}
		
		return null;
	}
	
	public void veranderTaal(ResourceBundle rb) {
		this.rb = rb;
		for (Label lbl : lblTekstLijst) {
			lbl.setText(rb.getString(lbl.getUserData().toString()));
		}
		
		for (Button btn : btnTekstLijst) {
			btn.setText(rb.getString(btn.getUserData().toString()));
		}
	}

}
