package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import domein.DomeinController;
import dto.EdeleDTO;
import dto.OntwikkelingskaartDTO;
import dto.SpelerDTO;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class SpelerPaneel extends GridPane {
	private DomeinController dc;
	private HoofdPaneel hoofdPaneel;
	private ResourceBundle rb;
	private List<Label> lblTekstLijst;
	
	public SpelerPaneel(DomeinController dc, HoofdPaneel hoofdPaneel, ResourceBundle rb) {
		this.dc = dc;
		this.hoofdPaneel = hoofdPaneel;
		this.rb = rb;
		this.lblTekstLijst = new ArrayList<>();
		
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

	private Button btnSpelOverzicht;

	private void voegComponentenToe() {
		List<SpelerDTO> spelers = dc.geefSpelers(false);
		ScrollPane scrollPane = new ScrollPane();
		TilePane spelersPane = new TilePane();
		spelersPane.setPrefColumns(2);
		spelersPane.setHgap(10);
		spelersPane.setVgap(10);
		scrollPane.setContent(spelersPane);
		scrollPane.setMaxWidth(REMAINING);

		for (SpelerDTO s : spelers) {
			VBox spelerBox = new VBox();

			spelerBox.setSpacing(10);
			spelerBox.setPadding(new Insets(10));

			HBox icoonEnNaam = new HBox();
			icoonEnNaam.setSpacing(10);
			ImageView imgSpelerIcon = new ImageView(getClass().getResource("/gui/images/speler.png").toExternalForm());
			imgSpelerIcon.setFitHeight(40);
			imgSpelerIcon.setFitWidth(40);
			icoonEnNaam.setAlignment(Pos.CENTER_LEFT);
			icoonEnNaam.getChildren().addAll(imgSpelerIcon, new Label(s.gebruikersnaam()));

			HBox prestigePuntenHBOX = new HBox();
			Label lblPrestigePunten = new Label(rb.getString("prestigepunten"));
			lblPrestigePunten.setUserData("prestigepunten");
			lblTekstLijst.add(lblPrestigePunten);
			prestigePuntenHBOX.getChildren().addAll(lblPrestigePunten, new Label(Integer.toString(s.prestigePunten())));

			HBox startSpelerHBox = new HBox();
			Label lblStartspelerTekst = new Label(rb.getString("startspeler"));
			lblStartspelerTekst.setUserData("startspeler");
			lblTekstLijst.add(lblStartspelerTekst);
			Label lblStartSpeler = new Label(
					String.format("%s", s.startSpeler() ? rb.getString("ja") : rb.getString("nee")));
			lblStartSpeler.setUserData(String.format("%s", s.startSpeler() ? "ja" : "nee"));
			lblTekstLijst.add(lblStartSpeler);
			startSpelerHBox.getChildren().addAll(lblStartspelerTekst, lblStartSpeler);

			HBox aanDeBeurtHBox = new HBox();
			Label lblAanDeBeurtTekst = new Label(rb.getString("isBeurt"));
			lblAanDeBeurtTekst.setUserData("isBeurt");
			lblTekstLijst.add(lblAanDeBeurtTekst);
			Label lblAanDeBeurt = new Label(String.format("%s",
					dc.isAandeBeurt(s.gebruikersnaam(), s.geboortejaar()) ? rb.getString("ja") : rb.getString("nee")));
			lblAanDeBeurt.setUserData(dc.isAandeBeurt(s.gebruikersnaam(), s.geboortejaar()) ? "ja" : "nee");
			lblTekstLijst.add(lblAanDeBeurt);
			aanDeBeurtHBox.getChildren().addAll(lblAanDeBeurtTekst, lblAanDeBeurt);

			TilePane ontwikkelingskaartenPane = new TilePane();
			ontwikkelingskaartenPane.setPrefColumns(10);
			List<OntwikkelingskaartDTO> ontwikkelingskaartenSpeler = s.ontwikkelingskaarten();

			for (OntwikkelingskaartDTO o : ontwikkelingskaartenSpeler) {
				ImageView img = new ImageView(
						getClass().getResource(String.format("/gui/images/niveau%d/kaart%d.png", o.niveau(), o.id()))
								.toExternalForm());
				img.setFitHeight(60);
				img.setFitWidth(40);
				ontwikkelingskaartenPane.getChildren().add(img);
			}

			TilePane edelenPane = new TilePane();
			List<EdeleDTO> edelenS = s.edelen();

			for (EdeleDTO e : edelenS) {
				ImageView img = new ImageView(
						getClass().getResource(String.format("/gui/images/edelen/edele%d.png", e.id())).toExternalForm());
				img.setFitHeight(55);
				img.setFitWidth(50);
				edelenPane.getChildren().add(img);
			}

			List<Label> ficheAantallenSpeler = new ArrayList<>();
			int[] fichesS = s.fiches();
			int index = 0;
			HBox fichesSpeler = new HBox();
			fichesSpeler.setSpacing(5);
			String[] edelstenen = dc.geefEdelstenen();

			for (String edelsteen : edelstenen) {
				ImageView img = new ImageView(getClass()
						.getResource(String.format("/gui/images/edelstenen/%s.png", edelsteen)).toExternalForm());
				img.setFitHeight(30);
				img.setFitWidth(30);
				HBox aantalHBox = new HBox();
				Label lblAantal = new Label(rb.getString("aantal"));
				lblAantal.setUserData("aantal");
				lblTekstLijst.add(lblAantal);
				aantalHBox.getChildren().addAll(lblAantal, new Label(Integer.toString(fichesS[index++])));
				ficheAantallenSpeler.add(lblAantal);
				fichesSpeler.getChildren().add(new VBox(img, aantalHBox));
			}

			Label lblFiches = new Label(rb.getString("fiches"));
			lblFiches.setUserData("fiches");
			lblTekstLijst.add(lblFiches);
			Label lblOntwikkelingskaarten = new Label(rb.getString("ontwikkelingskaarten"));
			lblOntwikkelingskaarten.setUserData("ontwikkelingskaarten");
			lblTekstLijst.add(lblOntwikkelingskaarten);
			Label lblEdelen = new Label(rb.getString("edelen"));
			lblEdelen.setUserData("edelen");
			lblTekstLijst.add(lblEdelen);

			spelerBox.getChildren().addAll(icoonEnNaam, prestigePuntenHBOX, startSpelerHBox, aanDeBeurtHBox, lblFiches,
					fichesSpeler, lblOntwikkelingskaarten, ontwikkelingskaartenPane, lblEdelen, edelenPane);
			spelersPane.getChildren().add(spelerBox);
		}

		add(scrollPane, 0, 0,2,1);
		btnSpelOverzicht = new Button(rb.getString("spelOverzicht"));
		btnSpelOverzicht.setOnAction((ActionEvent event) -> hoofdPaneel.toonSpel());
		add(btnSpelOverzicht, 0, 1, 1, 1);
	}

	public void veranderTaal(ResourceBundle rb) {
		this.rb = rb;

		for (Label lbl : lblTekstLijst) {
			lbl.setText(rb.getString(lbl.getUserData().toString()));
		}

		btnSpelOverzicht.setText(rb.getString("spelOverzicht"));
	}

}