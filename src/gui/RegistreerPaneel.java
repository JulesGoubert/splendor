package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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

public class RegistreerPaneel extends GridPane {

	private ResourceBundle rb;
	private HoofdPaneel hoofdPaneel;
	private DomeinController dc;
	private LoginPaneel loginPaneel;

	public RegistreerPaneel(HoofdPaneel hoofdpaneel, DomeinController dc, ResourceBundle rb, LoginPaneel loginPaneel) {
		this.dc = dc;
		this.rb = rb;
		this.hoofdPaneel = hoofdpaneel;
		this.loginPaneel = loginPaneel;

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

	private final Text header = new Text();
	private final Label lblGebruikersnaam = new Label();
	private final Label lblGeboortejaar = new Label();
	private final TextField txfGebruikersnaam = new TextField();
	private final TextField txfGeboortejaar = new TextField();
	private final Label foutbericht = new Label();
	private final Button btnRegistreren = new Button();
	private final Hyperlink terug = new Hyperlink();

	private void voegComponentenToe() {
		header.setText(rb.getString("registreren"));
		header.getStyleClass().add("hoofding");
		GridPane.setHalignment(header, HPos.LEFT);
		add(header, 0, 0, 1, 1);

		lblGebruikersnaam.setText(rb.getString("gebruikersnaam"));
		add(lblGebruikersnaam, 0, 1, 1, 1);
		add(txfGebruikersnaam, 1, 1, 1, 1);

		lblGeboortejaar.setText(rb.getString("geboortejaar"));
		add(lblGeboortejaar, 0, 2, 1, 1);
		add(txfGeboortejaar, 1, 2, 1, 1);

		btnRegistreren.setText(rb.getString("registreren"));
		btnRegistreren.setDefaultButton(true);
		btnRegistreren.setOnAction(this::registreren);
		foutbericht.getStyleClass().add("foutbericht");
		HBox controls = new HBox(btnRegistreren, foutbericht);
		controls.setSpacing(10);
		add(controls, 0, 3, 2, 1);

		Tooltip tooltip = new Tooltip();
		tooltip.setText(rb.getString("gebruikersnaamTip"));
		txfGebruikersnaam.setTooltip(tooltip);

		Tooltip tooltipGeboortjaar = new Tooltip();
		tooltipGeboortjaar.setText(rb.getString("geboortejaarTip"));
		txfGeboortejaar.setTooltip(tooltipGeboortjaar);

		terug.setText(rb.getString("terug"));
		GridPane.setHalignment(terug, HPos.LEFT);
		terug.setOnAction((ActionEvent event) -> hoofdPaneel.toonInloggen());
		add(terug, 0, 4, 2, 1);
	}

	private void registreren(ActionEvent event) {
		foutbericht.setText("");

		try {
			if (txfGebruikersnaam.getText().trim().isEmpty()) {
				foutbericht.setText(rb.getString("gebruikersnaamLeeg"));
				return;
			}
			if (txfGeboortejaar.getText().trim().isEmpty()) {
				foutbericht.setText(rb.getString("geboortejaarLeeg"));
				return;
			}

			dc.registreer(txfGebruikersnaam.getText(), Integer.parseInt(txfGeboortejaar.getText()));
//			aanmeldPaneel.setLblAantalSpelers(String.format("%s %d", rb.getString("aSpelers"), dc.geefAantalSpelers()));
			loginPaneel.voegSpelerToeAanOnaangemeld(txfGebruikersnaam.getText());
			hoofdPaneel.toonInloggen();
		} catch (NumberFormatException nfe) {
			txfGeboortejaar.clear();
			foutbericht.setText(rb.getString("geboortejaarFout"));
		} catch (IllegalArgumentException iae) {
			txfGebruikersnaam.clear();
			txfGeboortejaar.clear();
			foutbericht.setText(rb.getString(iae.getMessage()));
		}

		txfGebruikersnaam.setText("");
		txfGeboortejaar.setText("");
	}

	public void veranderTaal(ResourceBundle rb) {
		header.setText(rb.getString("registreren"));
		lblGebruikersnaam.setText(rb.getString("gebruikersnaam"));
		lblGeboortejaar.setText(rb.getString("geboortejaar"));
		btnRegistreren.setText(rb.getString("registreren"));
		terug.setText(rb.getString("terug"));
	}

}
