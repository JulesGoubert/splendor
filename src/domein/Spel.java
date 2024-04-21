package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import utils.Kleur;

public class Spel {

	private FicheRepository ficheRepo;
	private OntwikkelingskaartRepository niveau1;
	private OntwikkelingskaartRepository niveau2;
	private OntwikkelingskaartRepository niveau3;
	private EdeleRepository edeleRepo;
	private List<Speler> spelers;
	private Speler huidigeSpeler;

	public static final int MIN_AANTAL_SPELERS = 2;
	private static final int AANTAL_PRESTIGEPUNTEN_WIN = 15;

	// Beurtteller
	private int beurt;

	public Spel(List<Speler> spelers) {
		int aantalSpelers = spelers.size();

		setSpelers(spelers);
		setFicheRepository(aantalSpelers);
		setEdeleRepository(aantalSpelers);
		huidigeSpeler = spelers.get(0);
		niveau1 = new OntwikkelingskaartRepository(1);
		niveau2 = new OntwikkelingskaartRepository(2);
		niveau3 = new OntwikkelingskaartRepository(3);
		this.beurt = 0;
	}

	public List<Speler> getSpelers() {
		return spelers;
	}

	private void setSpelers(List<Speler> spelers) {
		if (spelers == null) {
			throw new IllegalArgumentException("geenSpelers");
		}
		if (spelers.size() < MIN_AANTAL_SPELERS) {
			throw new IllegalArgumentException("minAantalSpelers");
		}

		this.spelers = spelers;
	}

	public int[] getFiches() {
		return ficheRepo.getFiches();
	}

	private void setFicheRepository(int aantalSpelers) {
		// aantal fiches voor elke edelsteen bepalen a.d.h.v. het aantal spelers
		int aantalFiches = switch (aantalSpelers) {
		case 2 -> 4;
		case 3 -> 5;
		case 4 -> 7;
		default -> throw new IllegalArgumentException("aantalSpelersError");
		};

		int lengte = Kleur.values().length;
		int[] edelstenen = new int[lengte];

		// het aantal fiches voor elke edelsteen gelijkstellen aan het eerder bepaalde
		// aantal
		for (int i = 0; i < lengte; i++) {
			edelstenen[i] = aantalFiches;
		}

		ficheRepo = new FicheRepository(edelstenen);
	}

	public List<Edele> getEdelen() {
		return edeleRepo.getEdelen();
	}

	public void setEdeleRepository(int aantalSpelers) {
		// aantal edelen van het spel bepalen a.d.h.v. het aantal spelers
		int aantalEdelen = switch (aantalSpelers) {
		case 2 -> 3;
		case 3 -> 4;
		case 4 -> 5;
		default -> 3;
		};

		this.edeleRepo = new EdeleRepository(aantalEdelen);
	}

	public List<Ontwikkelingskaart> getNiveau1() {
		return niveau1.getOntwikkelingskaarten();
	}

	public List<Ontwikkelingskaart> getNiveau2() {
		return niveau2.getOntwikkelingskaarten();
	}

	public List<Ontwikkelingskaart> getNiveau3() {
		return niveau3.getOntwikkelingskaarten();
	}

	public boolean isEindeSpel() {
		// als de huidige speler geen startspeler is wilt dat zeggen dat er nog een
		// ronde bezig is en kan er nog geen winnaar zijn
		if (!huidigeSpeler.isStartSpeler()) {
			return false;
		}

		// spelers overlopen en checken of één van de spelers 15 of meer prestigepunten
		// heeft
		for (Speler s : spelers) {
			if (s.getPrestigePunten() >= AANTAL_PRESTIGEPUNTEN_WIN) {
				return true;
			}
		}

		return false;
	}

	private int geefIndexSpelerAanDeBeurt() {
		return beurt % spelers.size();
	}

	public Speler geefSpeler(Speler speler) {
		for (Speler s : spelers) {
			if (s.equals(speler)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * @return een lijst met de winnaars van het spel
	 * 		   dit kan er maar 1 zijn maar het kunnen ook meerdere spelers zijn
	 */
	public List<Speler> geefWinnaars() {
		// als het spel nog niet gedaan is kunnen er nog geen winnaars zijn
		if (!isEindeSpel()) {
			return null;
		}

		// copy maken van de spelers list
		List<Speler> spelersCopy = new ArrayList<>(spelers);
		// copy sorteren aan de hand van WinnaarComparator, deze sorteert op
		// prestigepunten van groot naar klein en aantal ontwikkelingskaarten van klein
		// naar groot
		Collections.sort(spelersCopy, new WinnaarComparator());
		// eerste speler in de gesorteerde copu eruit halen en gelijkstellen aan de
		// mogelijkeWinaar variabele
		Speler winnaar = spelersCopy.remove(0);
		List<Speler> winnaars = new ArrayList<>();
		winnaars.add(winnaar);

		int aantalOntwikkelingskaartenWinnaar = winnaar.getOntwikkelingskaarten().size();
		int index = 0;
		Speler volgendeSpeler = spelersCopy.get(index);

		// kijken of er nog winnaars zijn door de overige speler af te lopen en te
		// kijken of ze even veel prestigepunten en even weinig ontwikkelingskaarten
		// hebben
		while (volgendeSpeler.getPrestigePunten() == winnaar.getPrestigePunten()
				&& volgendeSpeler.getOntwikkelingskaarten().size() == aantalOntwikkelingskaartenWinnaar) {
			winnaars.add(volgendeSpeler);
			if (index + 1 < spelersCopy.size()) {				
				volgendeSpeler = spelersCopy.get(++index);
			} else {
				volgendeSpeler = new Speler("test", 2002);
			}
		}

		return winnaars;
	}
	
	/**
	 * Voegt 2 fiches van dezelfde kleur toe aan de fiches van de speler
	 * en neem 2 fiches van die kleur weg bij het spel.
	 * @param kleur geeft aan welke kleur de 2 fiches zijn die de speler moet krijgen en weggenomen moeten worden bij het spel
	 */
	

	public void neemTweeDezelfdeFiches(int kleur) {
		// als er minder dan 4 fiches van de opgegeven kleur in het spel zitten dan
		// mogen er geen 2 dezelfde fiches van die kleur genomen worden
		if (ficheRepo.geefAantal(kleur) < 4) {
			throw new IllegalArgumentException("teweinigEdfiches");
		}

		// contoleren of de speler niet meer dan 10 fiches heeft als hij er 2 bijneemt
		controleerAantalFichesSpeler(huidigeSpeler.geefTotaalAantalFiches() + 2);
		// fiches wegnemen van het spel
		ficheRepo.neemFichesWeg(kleur, 2);
		// fiches toevoegen bij de speler
		huidigeSpeler.voegFichesToe(kleur, 2);
		beurt++;
	}

	public void neemVerschillendeFiches(List<Integer> kleuren) {
		// het aantal genomen fiches mag maximaal 3 zijn
		if (kleuren.size() > 3) {
			throw new IllegalArgumentException("teveelEdfiches");
		}

		// er mogen geen duplicate fiches bijzitten
		if (kleuren.size() > new HashSet<>(kleuren).size()) {
			throw new IllegalArgumentException("dubbeleFiches");
		}

		// controleren of de speler niet meer dan 10 fiches heeft als hij het aantal
		// geselecteerde fiches neemt
		controleerAantalFichesSpeler(huidigeSpeler.geefTotaalAantalFiches() + kleuren.size());

		// elk geselecteerd fiche toevoegen bij de speler en wegnemen bij het spel
		for (int kleur : kleuren) {
			ficheRepo.neemFichesWeg(kleur, 1);
			huidigeSpeler.voegFichesToe(kleur, 1);
		}

		beurt++;
	}

	private void controleerAantalFichesSpeler(int aantal) {
		// een speler mag niet meer dan 10 fiches in bezit hebben
		if (aantal > 10) {
			throw new IllegalArgumentException("maxEdelfiches");
		}
	}

	private Ontwikkelingskaart geefOntwikkelingskaart(int id, int niveau) {
		List<Ontwikkelingskaart> kaarten = geefNiveau(niveau);

		for (Ontwikkelingskaart o : kaarten) {
			if (o.getId() == id) {
				return o;
			}
		}

		return null;
	}

	public List<Integer> koopOntwikkelingskaart(int id, int niveau) {
		Ontwikkelingskaart o = geefOntwikkelingskaart(id, niveau);
		List<Ontwikkelingskaart> ontwikkelingskaartenSpeler = huidigeSpeler.getOntwikkelingskaarten();
		int[] fichesSpeler = huidigeSpeler.getFiches();
		int[] fichesEnBonussenSpeler = Arrays.copyOf(fichesSpeler, fichesSpeler.length);
		int[] vereisteFiches = o.getVereisteFiches();
		int[] wegTeHalen = Arrays.copyOf(vereisteFiches, vereisteFiches.length);

		// bonussen toevoegen aan fiches en aftrekken van de weg te halen fiches
		for (Ontwikkelingskaart kaart : ontwikkelingskaartenSpeler) {
			int bonus = Kleur.valueOf(kaart.getBonus().toUpperCase()).ordinal();
			fichesEnBonussenSpeler[bonus]++;
			if (wegTeHalen[bonus] > 0) {
				wegTeHalen[bonus]--;
			}
		}

		// kijken of speler genoeg fiches heeft om kaart te kopen
		for (int i = 0; i < fichesEnBonussenSpeler.length; i++) {
			if (fichesEnBonussenSpeler[i] < vereisteFiches[i]) {
				throw new IllegalArgumentException("teWeinigFichesError");
			}
		}

		// fiches weghalen bij speler en toevoegen aan spel
		for (int i = 0; i < wegTeHalen.length; i++) {
			int aantal = wegTeHalen[i];
			huidigeSpeler.neemFichesWeg(i, aantal);
			ficheRepo.voegFichesToe(i, aantal);
		}

		geefNiveau(niveau).remove(o);
		huidigeSpeler.voegOntwikkelingskaartToe(o);

		// bepaal of er koopbare edelen zijn na een ontwikkelingskaart kopen
		return geefIDsKoopbareEdelen(); // TODO Opgelet: zorg ervoor dat de beurtteller na deze methode verhoogt.
	}

	private List<Integer> geefIDsKoopbareEdelen() {
		List<Integer> idsKoopbareEdelen = new ArrayList<>();

		for (Edele e : edeleRepo.getEdelen()) {
			if (controleerEdeleIsKoopbaar(e)) {
				idsKoopbareEdelen.add(e.getId());
			}
		}

		return idsKoopbareEdelen;
	}

	public void koopEdele(int id) {
		Edele e = geefEdele(id);

		// controlleren of de speler genoeg bonussen heeft om de edele te kopen
		if (!controleerEdeleIsKoopbaar(e)) {
			throw new IllegalArgumentException("teWeinigBonussenError");
		}
		
		// edele verwijderen bij spel en toevoegen bij de huidige speler
		edeleRepo.verwijderEdele(e);
		huidigeSpeler.voegEdeleToe(e);
	}

	private Edele geefEdele(int id) {
		for (Edele e : edeleRepo.getEdelen()) {
			if (e.getId() == id) {
				return e;
			}
		}

		return null;
	}

	private boolean controleerEdeleIsKoopbaar(Edele e) {
		int[] bonussenSpeler = huidigeSpeler.berekenBonussen();
		int[] vereisteBonussenEdele = e.getVereisteBonussen();

		// kijken of de huidige speler de edele kan kopen
		for (int i = 0; i < bonussenSpeler.length; i++) {
			if (bonussenSpeler[i] < vereisteBonussenEdele[i]) {
				return false;
			}
		}

		return true;
	}

	private List<Ontwikkelingskaart> geefNiveau(int niveau) {
		return switch (niveau) {
		case 1 -> niveau1.getOntwikkelingskaarten();
		case 2 -> niveau2.getOntwikkelingskaarten();
		case 3 -> niveau3.getOntwikkelingskaarten();
		default -> null;
		};
	}

	public void verhoogBeurtTeller() {
		beurt++;
	}

	public void setHuidigeSpeler() {
		huidigeSpeler = spelers.get(geefIndexSpelerAanDeBeurt());
	}

	public Speler getHuidigeSpeler() {
		return huidigeSpeler;
	}

	public boolean isAanDeBeurt(String gebruikersnaam, int geboortedatum) {
		Speler s = geefSpeler(new Speler(gebruikersnaam, geboortedatum));
		return huidigeSpeler.equals(s);
	}

	// methode om de spelers aan het begin van het spel al ontwikkelingskaarten te
	// geven
	public void preload() {
		for (Speler s : spelers) {
			neemKaarten(s, niveau1, 5);
			neemKaarten(s, niveau2, 3);
//			neemKaarten(s, niveau3, 1);
		}
	}

	private void neemKaarten(Speler s, OntwikkelingskaartRepository ontwikklingskaartRepo, int aantal) {
		for (int i = 0; i < aantal; i++) {
			Ontwikkelingskaart kaart = ontwikklingskaartRepo.geefOntwikkelingskaart(i);
			ontwikklingskaartRepo.verwijderOntwikkelingskaart(kaart);
			s.voegOntwikkelingskaartToe(kaart);
		}
	}

	// methode die alle ontwikkelingskaarten uit het spel retourneert
	public List<Ontwikkelingskaart> geefAlleOntwikkelingskaarten() {
		List<Ontwikkelingskaart> alleOntwikkelingskaarten = geefNiveau2En3();
		
		for (Ontwikkelingskaart o : niveau1.getOntwikkelingskaarten()) {
			alleOntwikkelingskaarten.add(o);
		}

		return alleOntwikkelingskaarten;
	}
	
	public List<Ontwikkelingskaart> geefNiveau2En3() {
		List<Ontwikkelingskaart> kaarten = new ArrayList<>();
		
		for (Ontwikkelingskaart o : niveau2.getOntwikkelingskaarten()) {
			kaarten.add(o);
		}
		for (Ontwikkelingskaart o : niveau3.getOntwikkelingskaarten()) {
			kaarten.add(o);
		}
		
		return kaarten;
	}

	// methode om het nemen van een Edele te testen
	public void geefGenoegKaartenOmEdelenTeKopen() {
		Speler s = spelers.get(0);
		int[] aantallen = new int[5];
		List<Ontwikkelingskaart> kaarten = geefAlleOntwikkelingskaarten();
		Collections.shuffle(kaarten);
		int i = 0;

		while (s.getOntwikkelingskaarten().size() < 20) {
			Ontwikkelingskaart o = kaarten.get(i++);
			int bonus = Kleur.valueOf(o.getBonus().toUpperCase()).ordinal();
			if (aantallen[bonus] < 4) {
				s.voegOntwikkelingskaartToe(o);
			}
			aantallen[bonus]++;
		}
	}
	
	private void geef15Prestigepunten(int index) {
		Speler s = spelers.get(index);
		int i = 0;
		List<Ontwikkelingskaart> kaarten = geefNiveau2En3();
		
		while (s.getPrestigePunten() != 15) {
			Ontwikkelingskaart o = kaarten.get(i++);
			if (s.getPrestigePunten() + o.getPrestigepunten() <= 15) {				
				s.voegOntwikkelingskaartToe(o);
			}
		}
	}
	
	public void maakWinnaars() {
		geef15Prestigepunten(0);
		geef15Prestigepunten(1);
	}

}
