package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import domein.DomeinController;
import dto.EdeleDTO;
import dto.OntwikkelingskaartDTO;
import dto.SpelerDTO;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class SpelPaneel extends GridPane {

	private HoofdPaneel hoofdPaneel;
	private DomeinController dc;
	private ResourceBundle rb;
	private SpelerDTO speler;
	private List<Label> lblTekstLijst;
	private List<Button> btnTekstLijst;

	public SpelPaneel(HoofdPaneel hoofdpaneel, DomeinController dc, ResourceBundle rb) {
		this.dc = dc;
		this.rb = rb;
		this.hoofdPaneel = hoofdpaneel;
		this.speler = dc.geefHuidigeSpeler();
		this.lblTekstLijst = new ArrayList<>();
		this.btnTekstLijst = new ArrayList<>();

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
		col2.setHalignment(HPos.RIGHT);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setHalignment(HPos.RIGHT);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setHgrow(Priority.ALWAYS);

		getColumnConstraints().addAll(col1, col2, col3);
	}

	private List<Button> ontwikkelingskaartenSpel = new ArrayList<>();
	private List<Integer> fiches = new ArrayList<>();
	private List<Label> ficheAantallenSpel = new ArrayList<>();
	private List<Label> ficheAantallenSpeler = new ArrayList<>();
	private VBox spelerBox = new VBox();
	private HBox niveau1HBox = new HBox();
	private HBox niveau2HBox = new HBox();
	private HBox niveau3HBox = new HBox();
	private HBox edelenHBox = new HBox();
	private List<EdeleDTO> edelen;
	private List<Integer> idEdelenPlaats;
	private int[] fichesSpel;
	private HBox geselecteerdeFiches = new HBox();

	private void voegComponentenToe() {
		// EDELSTEENFICHES
		fichesSpel = dc.geefFiches();
		int plaats = 0;
		String[] edelstenen = dc.geefEdelstenen();
		VBox fichesVBox = new VBox();
		fichesVBox.setSpacing(10);

		for (String edelsteen : edelstenen) {
			maakFiche(fichesVBox, edelsteen, plaats++, 70, 70);
		}

		add(fichesVBox, 0, 0, 1, 5);

		// EDELEN
		edelen = dc.geefEdelen();
		edelenHBox.setSpacing(10);
		toonEdelen(edelenHBox);


		// ONTWIKKELINGSKAARTEN
		VBox ontwikkelingskaartenVBox = new VBox();

		for (int i = 1; i < 4; i++) {
			toonRijOntwikkelingskaarten(i, geefNiveau(i));
			ontwikkelingskaartenVBox.getChildren().add(geefNiveauHBox(i));
		}

		VBox edelenEnKaarten = new VBox(edelenHBox, ontwikkelingskaartenVBox);
		edelenEnKaarten.setSpacing(10);
		add(edelenEnKaarten, 1, 0, 1, 1);

		// HUIDIGE SPELER
		spelerBox.setMinWidth(270);
		spelerBox.setMaxWidth(350);
		add(geefHuidigeSpeler(), 2, 0, 1, 3);

		// KNOPPEN
		VBox knoppen = new VBox();
		knoppen.setSpacing(10);
		HBox rij1 = new HBox();
		rij1.setSpacing(10);
		HBox rij2 = new HBox();
		rij2.setSpacing(10);

		// GESELECTEERDE FICHES
		Label ficheKeuze = new Label(rb.getString("ficheKeuze"));
		ficheKeuze.setUserData("ficheKeuze");
		lblTekstLijst.add(ficheKeuze);

		// TERUG NAAR SPELEROVERZICHT
		Button btnSpelerOverzicht = new Button(rb.getString("spelerOverzicht"));
		btnSpelerOverzicht.setUserData("spelerOverzicht");
		btnTekstLijst.add(btnSpelerOverzicht);
		btnSpelerOverzicht.setOnAction((ActionEvent e) -> hoofdPaneel.toonSpelerPaneel());
		btnSpelerOverzicht.setAlignment(Pos.CENTER);
		btnSpelerOverzicht.setMaxWidth(Double.MAX_VALUE);

		// SKIP BEURT
		Button btnSkipBeurt = new Button(rb.getString("skipBeurt"));
		btnSkipBeurt.setUserData("skipBeurt");
		btnTekstLijst.add(btnSkipBeurt);
		btnSkipBeurt.setOnAction((ActionEvent e) -> skipBeurt());
		btnSkipBeurt.setAlignment(Pos.CENTER);
		btnSkipBeurt.setMaxWidth(Double.MAX_VALUE);

		// BEVESTIG FICHE KEUZE
		Button btnBevestigFicheKeuze = new Button(rb.getString("bevestigFicheKeuze"));
		btnBevestigFicheKeuze.setUserData("bevestigFicheKeuze");
		btnTekstLijst.add(btnBevestigFicheKeuze);
		btnBevestigFicheKeuze.setOnAction((ActionEvent e) -> bevestigFicheKeuze());
		btnBevestigFicheKeuze.setAlignment(Pos.CENTER);
		btnBevestigFicheKeuze.setMaxWidth(Double.MAX_VALUE);

		// RESET FICHE KEUZE
		Button btnClearFicheKeuze = new Button(rb.getString("annuleerFicheKeuze"));
		btnClearFicheKeuze.setUserData("annuleerFicheKeuze");
		btnTekstLijst.add(btnClearFicheKeuze);
		btnClearFicheKeuze.setOnAction((ActionEvent e) -> annuleerFichekeuze());
		btnClearFicheKeuze.setAlignment(Pos.CENTER);
		btnClearFicheKeuze.setMaxWidth(Double.MAX_VALUE);
		
		VBox geselecteerdeFichesVBox = new VBox(ficheKeuze, geselecteerdeFiches);
		geselecteerdeFichesVBox.setSpacing(10);
		knoppen.getChildren().addAll(btnSpelerOverzicht, btnSkipBeurt, btnBevestigFicheKeuze, btnClearFicheKeuze, geselecteerdeFichesVBox);
		// knoppen op gelijke breedte zetten
		HBox.setHgrow(btnSpelerOverzicht, Priority.ALWAYS);
		HBox.setHgrow(btnSkipBeurt, Priority.ALWAYS);
		HBox.setHgrow(btnBevestigFicheKeuze, Priority.ALWAYS);
		HBox.setHgrow(btnClearFicheKeuze, Priority.ALWAYS);
		knoppen.setMaxWidth(140);
		add(knoppen, 3, 0, 1, 4);
	}

	private void maakEdele(int id, HBox box, int nummer) {
		Label edeleTekst = new Label(rb.getString("edele"));
		edeleTekst.setUserData("edele");
		lblTekstLijst.add(edeleTekst);
		Label edeleNummer = new Label(String.format(" %d", nummer));
		ImageView img = new ImageView(
				getClass().getResource(String.format("/gui/images/edelen/edele%d.png", id)).toExternalForm());
		img.setFitHeight(85);
		img.setFitWidth(85);
		box.getChildren().add(new VBox(img, new HBox(edeleTekst, edeleNummer)));
	}

	private void maakOntwikkelingskaart(int niveau, int id, HBox box) {
		ImageView img = new ImageView(
				getClass().getResource(String.format("/gui/images/niveau%d/kaart%d.png", niveau, id)).toExternalForm());
		img.setFitHeight(120);
		img.setFitWidth(80);
		Button btn = new Button();
		btn.setGraphic(img);
		btn.setOnAction((ActionEvent event) -> koopOntwikkelingskaart(id, niveau));
		ontwikkelingskaartenSpel.add(btn);
		box.getChildren().add(btn);
	}

	private void maakFiche(VBox box, String edelsteen, int plaats, int hoogte, int breedte) {
		ImageView img = new ImageView(
				getClass().getResource(String.format("/gui/images/edelstenen/%s.png", edelsteen)).toExternalForm());
		img.setFitHeight(hoogte);
		img.setFitWidth(breedte);
		Label lblAantalTekst = new Label(rb.getString("aantal"));
		lblAantalTekst.setUserData("aantal");
		lblTekstLijst.add(lblAantalTekst);
		Label lblAantal = new Label(Integer.toString(fichesSpel[plaats]));
		ficheAantallenSpel.add(lblAantal);
		Button btn = new Button();
		btn.setGraphic(img);
		btn.setOnAction((ActionEvent event) -> neemFiche(plaats));
		box.getChildren().add(new VBox(btn, new HBox(lblAantalTekst, lblAantal)));
	}

	private void toonRijOntwikkelingskaarten(int niveau, List<OntwikkelingskaartDTO> kaarten) {
		HBox niveauHBox = geefNiveauHBox(niveau);
		niveauHBox.getChildren().clear();
		niveauHBox.setSpacing(10);
		Label lblNiveau = new Label(rb.getString("niveau"));
		lblNiveau.setUserData("niveau");
		lblTekstLijst.add(lblNiveau);
		niveauHBox.getChildren().add(new HBox(lblNiveau, new Label(Integer.toString(niveau))));

		for (int i = 0; i < 4; i++) {
			maakOntwikkelingskaart(niveau, kaarten.get(i).id(), niveauHBox);
		}
	}

	private void toonEdelen(HBox box) {
		box.getChildren().clear();

		idEdelenPlaats = new ArrayList<>();
		edelen = dc.geefEdelen();

		for (int i = 0; i < edelen.size(); i++) {
			idEdelenPlaats.add(edelen.get(i).id());
			maakEdele(edelen.get(i).id(), box, i+1);
		}
	}

	private HBox geefNiveauHBox(int niveau) {
		return switch (niveau) {
		case 1 -> niveau1HBox;
		case 2 -> niveau2HBox;
		case 3 -> niveau3HBox;
		default -> null;
		};
	}

	private VBox geefHuidigeSpeler() {
		spelerBox.getChildren().clear();
		ficheAantallenSpeler.clear();

		spelerBox.setSpacing(10);
		spelerBox.setPadding(new Insets(10));

		HBox icoonEnNaam = new HBox();
		icoonEnNaam.setSpacing(10);
		ImageView imgSpelerIcon = new ImageView(getClass().getResource("/gui/images/speler.png").toExternalForm());
		imgSpelerIcon.setFitHeight(40);
		imgSpelerIcon.setFitWidth(40);
		icoonEnNaam.setAlignment(Pos.CENTER_LEFT);
		icoonEnNaam.getChildren().addAll(imgSpelerIcon, new Label(speler.gebruikersnaam()));

		Label lblPrestigepunten = new Label(rb.getString("prestigepunten"));
		lblPrestigepunten.setUserData("prestigepunten");
		lblTekstLijst.add(lblPrestigepunten);
		HBox prestigepuntenHBox = new HBox(lblPrestigepunten, new Label(Integer.toString(speler.prestigePunten())));

		Label lblFiches = new Label(rb.getString("fiches"));
		lblFiches.setUserData("fiches");
		lblTekstLijst.add(lblFiches);

		Label lblOntwikkelingskaarten = new Label(rb.getString("ontwikkelingskaarten"));
		lblOntwikkelingskaarten.setUserData("ontwikkelingskaarten");
		lblTekstLijst.add(lblOntwikkelingskaarten);

		Label lblEdelen = new Label(rb.getString("edelen"));
		lblEdelen.setUserData("edelen");
		lblTekstLijst.add(lblEdelen);

		// ONTWIKKELINGSKAARTEN
		TilePane ontwikkelingskaartenPane = new TilePane();
		ontwikkelingskaartenPane.setPrefColumns(7);
		List<OntwikkelingskaartDTO> ontwikkelingskaartenSpeler = speler.ontwikkelingskaarten();

		for (OntwikkelingskaartDTO o : ontwikkelingskaartenSpeler) {
			ImageView img = new ImageView(
					getClass().getResource(String.format("/gui/images/niveau%d/kaart%d.png", o.niveau(), o.id()))
							.toExternalForm());
			img.setFitHeight(75);
			img.setFitWidth(45);
			ontwikkelingskaartenPane.getChildren().add(img);
		}

		// EDELEN
		TilePane edelenSpeler = new TilePane();
		List<EdeleDTO> edelenS = speler.edelen();

		for (EdeleDTO e : edelenS) {
			ImageView img = new ImageView(
					getClass().getResource(String.format("/gui/images/edelen/edele%d.png", e.id())).toExternalForm());
			img.setFitHeight(50);
			img.setFitWidth(45);
			edelenSpeler.getChildren().add(img);
		}

		// EDELSTEENFICHES
		int[] fichesS = speler.fiches();
		int index = 0;
		HBox fichesSpeler = new HBox();
		fichesSpeler.setSpacing(5);
		String[] edelstenen = dc.geefEdelstenen();

		for (String edelsteen : edelstenen) {
			ImageView img = new ImageView(
					getClass().getResource(String.format("/gui/images/edelstenen/%s.png", edelsteen)).toExternalForm());
			img.setFitHeight(30);
			img.setFitWidth(30);
			Label lblAantal = new Label(rb.getString("aantal"));
			lblAantal.setUserData("aantal");
			lblTekstLijst.add(lblAantal);
			ficheAantallenSpeler.add(lblAantal);
			fichesSpeler.getChildren()
					.add(new VBox(img, new HBox(lblAantal, new Label(Integer.toString(fichesS[index++])))));
		}

		spelerBox.getChildren().addAll(icoonEnNaam, prestigepuntenHBox, lblFiches, fichesSpeler,
				lblOntwikkelingskaarten, ontwikkelingskaartenPane, lblEdelen, edelenSpeler);
		return spelerBox;
	}

	private void neemFiche(int plaats) {
		if (fiches.size() == 0) {
			disableOntwikkelingskaarten();
		}

		try {
			if (fiches.size() < 3) {
				fiches.add(plaats);
				voegFicheToeAanOverzicht(plaats);
			} else {
				fiches.clear();
				geselecteerdeFiches.getChildren().clear();
				throw new IllegalArgumentException("teVeelFiches");
			}
		} catch (IllegalArgumentException iae) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("foutmelding"));
			alert.setHeaderText(rb.getString("fout"));
			alert.setContentText(rb.getString(iae.getMessage()));
			alert.showAndWait();
		}
	}

	private void bevestigFicheKeuze() {
		try {
			if (fiches.size() <= 0) {
				throw new IllegalArgumentException(
						"nietsGeselecteerd");
			} else if (fiches.size() == 2 && (fiches.get(0) == fiches.get(1))) {
				dc.neemTweeDezelfdeFiches(fiches.get(0));
			} else {
				dc.neemVerschillendeFiches(fiches);
			}
			annuleerFichekeuze();
			eindeBeurt();
		} catch (IllegalArgumentException iae) {
			annuleerFichekeuze();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("foutmelding"));
			alert.setHeaderText(rb.getString("fout"));
			alert.setContentText(iae.getMessage());
			alert.setContentText(rb.getString(iae.getMessage()));
			alert.showAndWait();
		}
	}

	private void annuleerFichekeuze() {
		fiches.clear();
		geselecteerdeFiches.getChildren().clear();
		enableOntwikkelingskaarten();
	}

	private void eindeBeurt() {
		reloadFicheAantallen(dc.geefFiches(), ficheAantallenSpel);
		reloadFicheAantallen(speler.fiches(), ficheAantallenSpeler);
		bepaalVolgendeSpeler();
	}

	private void skipBeurt() {
		annuleerFichekeuze();
		dc.slaBeurtOver();
		eindeBeurt();
	}

	private void voegFicheToeAanOverzicht(int plaats) {
		String geselecteerdeEdelsteen = dc.geefEdelstenen()[plaats];
		ImageView img = new ImageView(getClass()
				.getResource(String.format("/gui/images/edelstenen/%s.png", geselecteerdeEdelsteen)).toExternalForm());
		img.setFitHeight(30);
		img.setFitWidth(30);
		geselecteerdeFiches.getChildren().add(img);
	}

	private void koopOntwikkelingskaart(int id, int niveau) {
		try {
			List<Integer> idsKoopbareEdelen = dc.koopOntwikkelingskaart(id, niveau);
			if (idsKoopbareEdelen.size() > 0) {
				if (idsKoopbareEdelen.size() == 1) {
					dc.koopEdele(idsKoopbareEdelen.get(0));
				} else {
					List<String> edelenAlsOpties = new ArrayList<>();
					int gekozenEdele;

					for (int i = 1; i <= idsKoopbareEdelen.size(); i++) {
						edelenAlsOpties.add(String.format("%s %d", rb.getString("edele"), i));
					}

					do {
						gekozenEdele = JOptionPane.showOptionDialog(null, rb.getString("selecteerEdeleTekst"),
								rb.getString("selecteerEdeleTitel"), JOptionPane.DEFAULT_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, edelenAlsOpties.toArray(),
								edelenAlsOpties.toArray()[0]);
					} while (gekozenEdele == -1);

					dc.koopEdele(idEdelenPlaats.get(gekozenEdele));
				}
				toonEdelen(edelenHBox); // Reload de edele knoppen.
			}
			toonRijOntwikkelingskaarten(niveau, geefNiveau(niveau));
			reloadFicheAantallen(dc.geefFiches(), ficheAantallenSpel);
			dc.slaBeurtOver(); 	// Teller verhogen aangezien koopOntwikkelingskaart() de beurtteller niet meer
										// gaat verhogen.
			bepaalVolgendeSpeler();
		} catch (IllegalArgumentException iae) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("foutmelding"));
			alert.setHeaderText(rb.getString("fout"));
			alert.setContentText(rb.getString(iae.getMessage()));
			alert.showAndWait();
		}
	}

	private void disableOntwikkelingskaarten() {
		for (Button o : ontwikkelingskaartenSpel) {
			o.setDisable(true);
		}
	}

	private void enableOntwikkelingskaarten() {
		for (Button o : ontwikkelingskaartenSpel) {
			o.setDisable(false);
		}
	}

	private void reloadFicheAantallen(int[] fiches, List<Label> ficheAantallen) {
		int index = 0;

		for (Label lbl : ficheAantallen) {
			lbl.setText(Integer.toString(fiches[index++]));
		}
	}

	private void bepaalVolgendeSpeler() {
		dc.bepaalHuidigeSpeler();
		hoofdPaneel.vernieuwStatus();
		speler = dc.geefHuidigeSpeler();
		geefHuidigeSpeler();
		if (dc.isEindeSpel()) {
			toonWinnaar();
		}
	}

	private void toonWinnaar() {
		List<SpelerDTO> winnaars = dc.geefWinnaars();
		String winnaarsString = "";

		for (SpelerDTO winnaar : winnaars) {
			winnaarsString += String.format("- %s%n", winnaar.gebruikersnaam());
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(rb.getString(winnaars.size() > 1 ? "winnaarsTitel" : "winnaarTitel"));
		alert.setHeaderText(rb.getString("winnaarBekend"));
		alert.setContentText(String.format("%s%n%s%n%s", rb.getString(winnaars.size() > 1 ? "winnaars" : "winnaar"),
				winnaarsString, rb.getString("proficiat")));
		alert.showAndWait();
		hoofdPaneel.reset();
	}

	private List<OntwikkelingskaartDTO> geefNiveau(int niveau) {
		return switch (niveau) {
		case 1 -> dc.geefNiveau1();
		case 2 -> dc.geefNiveau2();
		case 3 -> dc.geefNiveau3();
		default -> null;
		};
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
