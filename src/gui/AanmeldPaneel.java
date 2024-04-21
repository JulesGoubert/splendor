package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import exceptions.TeVeelSpelersException;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

// WORDT NIET GEBRUIKT
public class AanmeldPaneel extends GridPane {
	
	private HoofdPaneel hoofdPaneel;
	private DomeinController dc;
	private ResourceBundle rb;
	
	public AanmeldPaneel(HoofdPaneel hoofdpaneel, DomeinController dc, ResourceBundle rb) {
		this.dc = dc;
		this.rb = rb;
		this.hoofdPaneel = hoofdpaneel;
		
		configureerGrid();
		voegComponentenToe();
	}

	private void configureerGrid() {
		setPadding(new Insets(10));
		setHgap(10);
		setVgap(10);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHalignment(HPos.RIGHT);

		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);

		getColumnConstraints().addAll(col1, col2);
	}
	
	private final Text aanmelden = new Text();
	private final Label lblAantalSpelers = new Label();
	private final Label lblGebruikersnaam = new Label();
	private final Label lblGeboortejaar = new Label();
	private final TextField txfGebruikersnaam = new TextField();
	private final TextField txfGeboortejaar = new TextField();
	private final Label foutberichtAanmelden = new Label();
	private final Label foutberichtStarten = new Label();
	private final Button btnAanmelden = new Button();
	private final Hyperlink registratie = new Hyperlink();
	private final Button start = new Button();

	private void voegComponentenToe() {
		aanmelden.setText(rb.getString("aanmelden"));
		aanmelden.getStyleClass().add("hoofding");
		add(aanmelden, 0, 0, 1, 1);
		
		lblAantalSpelers.setText(String.format("%s %d", rb.getString("aSpelers"), dc.geefAantalSpelers()));
		GridPane.setHalignment(lblAantalSpelers, HPos.RIGHT);
		GridPane.setValignment(lblAantalSpelers, VPos.CENTER);
		add(lblAantalSpelers, 1, 0, 1, 1);

		lblGebruikersnaam.setText(rb.getString("gebruikersnaam"));
		add(lblGebruikersnaam, 0, 1, 1, 1);
		add(txfGebruikersnaam, 1, 1, 1, 1);

		lblGeboortejaar.setText(rb.getString("geboortejaar"));
		add(lblGeboortejaar, 0, 2, 1, 1);
		add(txfGeboortejaar, 1, 2, 1, 1);

		btnAanmelden.setText(rb.getString("aanmelden"));
		btnAanmelden.setOnAction(this::aanmelden);
		btnAanmelden.setDefaultButton(true);
		foutberichtAanmelden.getStyleClass().add("foutbericht");
		HBox controls = new HBox(btnAanmelden, foutberichtAanmelden);
		controls.setSpacing(10);
		add(controls, 0, 3, 2, 1);
		
		registratie.setText(rb.getString("nSpeler"));
		registratie.setOnAction(this::registreren);
		GridPane.setHalignment(registratie, HPos.LEFT);
		add(registratie, 0, 4, 2, 1);
		
		start.setText(rb.getString("start"));
		start.setOnAction(this::startSpel);
		foutberichtStarten.getStyleClass().add("foutbericht");
		HBox controls2 = new HBox(start, foutberichtStarten);
		controls2.setSpacing(10);
		add(controls2, 0, 5, 2, 1);
		
		Tooltip tooltip = new Tooltip();
		tooltip.setText("- De gebruikersnaam moet met een hoofdletter beginnen,\n- Gebruikersnaam mag spaties of _ bevatten maar geen andere speciale tekens. ");
		txfGebruikersnaam.setTooltip(tooltip);	
		
		Tooltip tooltipGeboortjaar = new Tooltip();
		tooltipGeboortjaar.setText("- Geboortejaar moet geldig zijn \n- Elke gebruiker is minstens 6 jaar oud ");
		txfGeboortejaar.setTooltip(tooltipGeboortjaar);	
	}
	
	private void aanmelden(ActionEvent event) {
		foutberichtAanmelden.setText("");
		
		try {
			if (txfGebruikersnaam.getText().trim().isEmpty()) {
				foutberichtAanmelden.setText("Gelieve uw gebruikersnaam in te vullen");
				return;
			}
			if (txfGeboortejaar.getText().trim().isEmpty()) {
				foutberichtAanmelden.setText("Gelieve uw geboortejaar in te vullen");
				return;
			}
			
			dc.meldAan(txfGebruikersnaam.getText(), Integer.parseInt(txfGeboortejaar.getText()));
			lblAantalSpelers.setText(String.format("%s %d", rb.getString("aSpelers"), dc.geefAantalSpelers()));
		} catch (TeVeelSpelersException e) {
			foutberichtAanmelden.setText(e.getMessage());
			System.out.println(e.getMessage());
		} catch (NumberFormatException nfe) {
			txfGeboortejaar.clear();
			foutberichtAanmelden.setText("Gelieve een nummer in te geven als geboortejaar");
		} catch (IllegalArgumentException iae) {
			txfGebruikersnaam.clear();
			txfGeboortejaar.clear();
			foutberichtAanmelden.setText(iae.getMessage());
			System.out.println(iae.getMessage());
		}
		
		txfGebruikersnaam.setText("");
		txfGeboortejaar.setText("");
	}
	
	private void registreren(ActionEvent event) {
		hoofdPaneel.toonRegistratie();
	}
	
	private void startSpel(ActionEvent event) {
		foutberichtStarten.setText("");
		
		try {			
			hoofdPaneel.startSpel();
			hoofdPaneel.toonSpelerPaneel();
		} catch (IllegalArgumentException iae) {
			foutberichtStarten.setText(iae.getMessage());
		}
	}

	public void veranderTaal(ResourceBundle rb) {
		aanmelden.setText(rb.getString("aanmelden"));
		lblAantalSpelers.setText(String.format("%s %d", rb.getString("aSpelers"), dc.geefAantalSpelers()));
		lblGebruikersnaam.setText(rb.getString("gebruikersnaam"));
		lblGeboortejaar.setText(rb.getString("geboortejaar"));
		btnAanmelden.setText(rb.getString("aanmelden"));
		registratie.setText(rb.getString("nSpeler"));
		start.setText(rb.getString("start"));
	}
	
	public void setLblAantalSpelers(String tekst) {
		lblAantalSpelers.setText(tekst);
	}
	
}
