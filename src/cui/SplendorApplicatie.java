package cui;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import domein.DomeinController;
import domein.FicheRepository;
import domein.Spel;
import dto.EdeleDTO;
import dto.OntwikkelingskaartDTO;
import dto.SpelerDTO;
import exceptions.TeVeelSpelersException;

public class SplendorApplicatie {

	private final DomeinController dc;
	private final Scanner invoer = new Scanner(System.in);

	// Menu
	private static final int SPELER_TOEVOEGEN = 1;
	private static final int SPELER_SELECTEREN = 2;
	private static final int OVERZICHT_SPELERS_DATABANK = 3;
	private static final int OVERZICHT_GESELECTEERDE_SPELERS = 4;
	private static final int TAAL_VERANDEREN = 5;
	private static final int SPEL_STARTEN = 6;

	// Aantallen
	private static final int AANTAL_TALEN = 2;
	private static final int AANTAL_KEUZES = 6;

	// Taalkeuze
	private static final int NEDERLANDS = 1;
	private static final int ENGELS = 2;
	private ResourceBundle rb = ResourceBundle.getBundle("res.bundle");

	public SplendorApplicatie(DomeinController dc) {
		this.dc = dc;
	}

	public void start() {
		selecteerTaal();

		System.out.printf("%n%s%n", rb.getString("welkom"));

		int keuze = geefKeuze();

		while (keuze != SPEL_STARTEN || dc.geefAantalSpelers() < Spel.MIN_AANTAL_SPELERS) {
			switch (keuze) {
			case SPELER_TOEVOEGEN -> voegSpelerToe();
			case SPELER_SELECTEREN -> selecteerSpeler();
			case OVERZICHT_SPELERS_DATABANK -> toonOverzichtSpelers(true);
			case OVERZICHT_GESELECTEERDE_SPELERS -> toonOverzichtSpelers(false);
			case TAAL_VERANDEREN -> selecteerTaal();
			case SPEL_STARTEN ->
				System.out.printf("%n%s%n", String.format(rb.getString("nietGenoegSpelers"), Spel.MIN_AANTAL_SPELERS));
			}

			keuze = geefKeuze();
		}

		try {
			dc.startSpel();
			System.out.printf("%n%s%n", rb.getString("aangemaakt"));
			geefSpelSituatie();
			while (!dc.isEindeSpel()) {
				//Spaghetti code Achiles
				List<OntwikkelingskaartDTO> niv1 = dc.geefNiveau1();
				List<OntwikkelingskaartDTO> niv2 = dc.geefNiveau2();
				List<OntwikkelingskaartDTO> niv3 = dc.geefNiveau3();
				switch(geefKeuzeBeurt()){
					case 1 -> {
						SpelerDTO spelerAanDeBeurt = dc.geefHuidigeSpeler();
						System.out.println(geefOverzichtSpelerFiches(spelerAanDeBeurt));
						System.out.println("\n=== Ontwikkelingskaarten ===");
						toonOntwikkelingskaarten(niv1, 1);
						toonOntwikkelingskaarten(niv2, 2);
						toonOntwikkelingskaarten(niv3, 3);
						System.out.printf("Welk niveau wil je van kiezen? ");
						int niveau = invoer.nextInt();
						switch(niveau) {
						case 1 -> {
							toonOntwikkelingskaarten(niv1, 1);
							System.out.println("Welke kaart kies je?");
							int gekozenKaart = invoer.nextInt();
							dc.koopOntwikkelingskaart(gekozenKaart, 1);
							geefOverzichtSpeler(spelerAanDeBeurt);
						}
						case 2 -> toonOntwikkelingskaarten(niv2, 2);
						}
					}
					default -> dc.slaBeurtOver();
				}
				//Spaghetti code eindigt hier
			}
		} catch (IllegalArgumentException iae) {
			System.out.println(iae.getMessage());
			System.out.printf("%n%s%n", rb.getString("spelFout"));
		}
	}

	private void selecteerSpeler() {
		boolean flag = true;

		do {
			try {
				toonOverzichtSpelers(true);

				System.out.print(rb.getString("selecteer"));
				int index = invoer.nextInt() - 1;

				dc.voegSpelerToe(index);

				flag = false;
			} catch (TeVeelSpelersException e) {
				System.out.println(e.getMessage());
				break;
			} catch (InputMismatchException ime) {
				System.out.println("U moet een geheel getal ingeven.");
			} catch (IllegalArgumentException iae) {
				System.out.println(iae.getMessage());
			}
		} while (flag);

	}

	private void selecteerTaal() {
		boolean flag = true;

		do {
			try {
				System.out.printf("1. %s%n", rb.getString("nederlands"));
				System.out.printf("2. %s%n", rb.getString("engels"));
				System.out.print(rb.getString("keuze"));
				int keuze = invoer.nextInt();

				if (keuze < 1 || keuze > AANTAL_TALEN) {
					throw new IllegalArgumentException();
				}

				switch (keuze) {
				case NEDERLANDS -> rb = ResourceBundle.getBundle("res.bundle");
				case ENGELS -> rb = ResourceBundle.getBundle("res.bundle", new Locale("en", "GB"));
				// hier kunnen nog andere talen komen
				}

				flag = false;
			} catch (InputMismatchException ime) {
				System.out.println("U moet een geheel getal ingeven");
				invoer.nextLine();
			} catch (IllegalArgumentException iae) {
				System.out.printf("Je keuze moet in het interval [1, %d] liggen.%nProbeer het alstublieft opnieuw.%n",
						AANTAL_TALEN);
			}
		} while (flag);
	}

	private void toonOverzichtSpelers(boolean databank) {
		System.out.printf("%n=== %s ===%n", rb.getString(String.format("overzicht%s", databank ? 'D' : 'S')));

		List<SpelerDTO> spelers = dc.geefSpelers(databank);
		int i = 1;

		if (spelers.size() == 0) {
			System.out.println(rb.getString("geenSpelers"));
		} else {
			for (SpelerDTO speler : spelers) {
				System.out.printf("%d. %s%n", i++,
						String.format(rb.getString("speler"), speler.gebruikersnaam(), speler.geboortejaar()));
			}
		}
	}

	private void voegSpelerToe() {
		boolean flag = true;

		do {
			try {
				invoer.nextLine();
				System.out.printf("%n%s", rb.getString("gebruikersnaam"));
				String gebruikersnaam = invoer.nextLine();

				System.out.printf("%s", rb.getString("geboortejaar"));
				int geboortejaar = invoer.nextInt();

				dc.meldAan(gebruikersnaam, geboortejaar);

				flag = false;
			} catch (TeVeelSpelersException e) {
				System.out.println(e.getMessage());
				break;
			} catch (InputMismatchException ime) {
				System.out.println("U moet een geheel getal ingeven.");
			} catch (IllegalArgumentException iae) {
				System.out.println(iae.getMessage());
			}
		} while (flag);
	}

	private int geefKeuze() {
		int keuze = 0;
		boolean flag = true;

		do {
			try {
				toonKeuzeMenu();
				System.out.printf("%n%s", rb.getString("keuze"));
				keuze = invoer.nextInt();

				if (keuze < 1 || keuze > AANTAL_KEUZES) {
					throw new IllegalArgumentException(String.format(
							"Je keuze moet in het interval [1, %d] liggen.\nProbeer het alstublieft opnieuw.",
							AANTAL_KEUZES));
				}

				flag = false;
			} catch (InputMismatchException ime) {
				System.out.println("U moet een geheel getal ingeven.");
				invoer.nextLine();
			} catch (IllegalArgumentException iae) {
				System.out.println(iae.getMessage());
			}
		} while (flag);

		return keuze;
	}

	private void toonKeuzeMenu() {
		System.out.println("\n=== Menu ===");
		System.out.printf("%d. %s%n", SPELER_TOEVOEGEN, rb.getString("toevoegen"));
		System.out.printf("%d. %s%n", SPELER_SELECTEREN, rb.getString("selecteren"));
		System.out.printf("%d. %s%n", OVERZICHT_SPELERS_DATABANK, rb.getString("overzichtD"));
		System.out.printf("%d. %s%n", OVERZICHT_GESELECTEERDE_SPELERS, rb.getString("overzichtS"));
		System.out.printf("%d. %s%n", TAAL_VERANDEREN, rb.getString("taalVeranderen"));
		System.out.printf("%d. %s%n", SPEL_STARTEN, rb.getString("start"));
	}

	public void geefSpelSituatie() {
		List<SpelerDTO> spelers = dc.geefSpelers(false);
		System.out.println("\n=== Overzicht Spelers ===\n");

		for (SpelerDTO speler : spelers) {
			geefOverzichtSpeler(speler);
		}
		geefOverzichtSpel();
	}

	private void geefOverzichtSpeler(SpelerDTO s) {
		// Speler gegevens
		System.out.printf("== %s[%s] ==%n", rb.getString("titelSpeler"), s.gebruikersnaam());
		System.out.printf("%s%d%n", rb.getString("totaalPrestige"), s.prestigePunten());
		//System.out.printf("%s%s%n", rb.getString("isBeurt"), String.valueOf(Spel.bepaalBeurt(s));
		System.out.printf("%s%s%n", rb.getString("isStart"), String.valueOf(s.startSpeler()));

		// Ontwikkelingskaarten
		System.out.printf("= %s =%n", rb.getString("titelKaarten"));
		if (s.ontwikkelingskaarten().isEmpty()) {
			System.out.printf("%s%n", rb.getString("geenKaarten"));
		} else {
			for (OntwikkelingskaartDTO ok : s.ontwikkelingskaarten()) {
				System.out.printf("%s%n", rb.getString("overzichtKaarten"), ok.bonus(), ok.prestigepunten(),
						ok.niveau());
			}
		}

		// Edelsteenfiches
		System.out.printf("= %s =%n", rb.getString("titelFiches"));
		System.out.println(geefOverzichtSpelerFiches(s));

		// Edelen
		System.out.printf("= %s =%n", rb.getString("titelEdelen"));
		System.out.println(geefOverzichtSpelerEdelen(s));
		System.out.println();
	}

	private void geefOverzichtSpel() {
		System.out.printf("=== %s ===%n", rb.getString("titelSpel"));
		List<EdeleDTO> edele = dc.geefEdelen();
		int[] fiches = dc.geefFiches();
		List<OntwikkelingskaartDTO> niv1 = dc.geefNiveau1();
		List<OntwikkelingskaartDTO> niv2 = dc.geefNiveau2();
		List<OntwikkelingskaartDTO> niv3 = dc.geefNiveau3();
		int edeleIndex = 1;

		System.out.println("\n=== Edelsteenfiches ===");
		System.out.printf(
				"Aantal fiches van elke kleur: groen: %d, wit: %d, blauw: %d, zwart: %d, rood: %d",
				fiches[0], fiches[1], fiches[2], fiches[3], fiches[4]);

		System.out.println("\n=== Edelen ===");
		for (EdeleDTO e : edele) {
			System.out.printf(
					"Edele %d: vereiste groene fiches: %d, vereiste witte fiches: %d, vereiste blauwe fiches: %d vereiste zwarte fiches: %d, vereiste rode fiches %d%n",
					edeleIndex++, e.vereisteBonussen()[0], e.vereisteBonussen()[1], e.vereisteBonussen()[2], e.vereisteBonussen()[3], e.vereisteBonussen()[4]);
		}
		
		System.out.println("\n=== Ontwikkelingskaarten ===");
		
		toonOntwikkelingskaarten(niv1, 1);
		toonOntwikkelingskaarten(niv2, 2);
		toonOntwikkelingskaarten(niv3, 3);
	}
	
	private void toonOntwikkelingskaarten(List<OntwikkelingskaartDTO> kaarten, int niveau) {
		System.out.printf("=== Stapel niveau %d ===%n", niveau);
		
		for (int i = 0; i < 4; i++) {
			OntwikkelingskaartDTO ok = kaarten.get(i);
			System.out.printf("Ontwikkelingskaart %d:%n", i + 1);
			System.out.printf("- Niveau: %d%n- Bonus: %s%n- Prestigepunten %d%n", ok.niveau(), ok.bonus(), ok.prestigepunten());
			System.out.printf("- Vereiste fiches: groen: %d, wit: %d, blauw: %d, zwart: %d, rood: %d%n",
							   ok.vereisteFiches()[0], ok.vereisteFiches()[1], ok.vereisteFiches()[2], ok.vereisteFiches()[3], ok.vereisteFiches()[4]);
		}
		
	}
	
	private String geefOverzichtSpelerFiches(SpelerDTO s) {
		// Controleren of speler fiches heeft en de indexen van die opslaan
		List<Integer> bestaandeFichesIndexen = new ArrayList<>();
		for (int i = 0; i < s.fiches().length; i++) {
			if (s.fiches()[i] > 0) {
				bestaandeFichesIndexen.add(i);
			}
		}
		// Geen fiches? -> stoppen
		if (bestaandeFichesIndexen.isEmpty()) {
			return rb.getString("geenFiches");
		}
		// Wel fiches? -> Nieuwe string opbouwen en die als resultaat geven
		String spelerFiches = "";
		for (Integer i : bestaandeFichesIndexen) {
			spelerFiches += String.format("%s%n", String.format(rb.getString("overzichtFiche"), FicheRepository.EDELSTENEN[i], s.fiches()[i]));
		}
		return spelerFiches;
	}
	
	private String geefOverzichtSpelerEdelen(SpelerDTO s) {
		// Controleren of speler wel edelen heeft of niet
		if (s.edelen().size() == 0) {
			return rb.getString("geenEdelen");
		}
		// Maak nieuwe string om als resultaat door te geven
		String vereisteFiches = rb.getString("overzichtEdelen");
		// For-loop om over elke edele in bezit de benodige bonussen/fiches aan de resultaat toe te voegen
		for (EdeleDTO e : s.edelen()) {
			for (int i = 0; i < e.vereisteBonussen().length; i++) {
				if (e.vereisteBonussen()[i] > 0) {
					vereisteFiches.concat(String.format("%d %10s, ", e.vereisteBonussen()[i], FicheRepository.EDELSTENEN[i]));
				}
			}
			System.out.println();
			vereisteFiches.concat(rb.getString("overzichtEdelen"));
		}
		return vereisteFiches;
	}
	
	// Spaghetti code Achiles
	private int geefKeuzeBeurt() {
		int keuze;
		do {
			System.out.println("Geef keuze tussen volgende opties:");
			System.out.println("1. Neem 2 fiches van hetzelfde type");
			System.out.println("2. Neem 3 fiches van verschillende type");
			System.out.println("3. Koop ontwikkelingskaart");
			System.out.printf("4. Sla beurt over%nJe keuze: ");
			keuze = invoer.nextInt();
		} while (keuze < 1 || keuze > 4);
		return keuze;
	}
	// Spaghetti code eindigt hier
	
}