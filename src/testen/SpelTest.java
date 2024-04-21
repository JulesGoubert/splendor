package testen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.Ontwikkelingskaart;
import domein.Spel;
import domein.Speler;
import persistentie.OntwikkelingskaartMapper;

import static domein.Spel.MIN_AANTAL_SPELERS;

class SpelTest {

	Spel spelMetSpelers;
	private List<Speler> geldigSpelers;
	private List<Integer> drieVerschillendeFiches;

	OntwikkelingskaartMapper okmapper = new OntwikkelingskaartMapper();
	List<Ontwikkelingskaart> kaarten = okmapper.geefOntwikkelingskaarten();
	Ontwikkelingskaart okNiveau1, okNiveau2, okNiveau3;

	private static final Speler GELDIGE_SPELER_1 = new Speler("test1", 2001);
	private static final Speler GELDIGE_SPELER_2 = new Speler("test2", 2002);
	private static final Speler GELDIGE_SPELER_3 = new Speler("test3", 2003);
	private static final Speler GELDIGE_SPELER_4 = new Speler("test4", 2004);

	@BeforeEach
	void before() {
		geldigSpelers = new ArrayList<>();
		geldigSpelers.add(GELDIGE_SPELER_1);
		geldigSpelers.add(GELDIGE_SPELER_2);
		geldigSpelers.add(GELDIGE_SPELER_3);
		geldigSpelers.add(GELDIGE_SPELER_4);
		spelMetSpelers = new Spel(geldigSpelers);
		drieVerschillendeFiches = new ArrayList<>();

		okNiveau1 = kaarten.get(0);
		okNiveau2 = kaarten.get(41);
		okNiveau3 = kaarten.get(71);
	}

	@Test
	void maakSpel_OngeldigAantalSpelers_WerptException() {
		// arrange
		List<Speler> ongeldigAantalSpelers = new ArrayList<>();
		// act
		for (int i = 0; i < MIN_AANTAL_SPELERS - 1; i++) {
			ongeldigAantalSpelers.add(GELDIGE_SPELER_1);
		}
		// assert
		assertThrows(IllegalArgumentException.class, () -> new Spel(ongeldigAantalSpelers));
	}

	@Test
	void maakSpel_GeldigAantalSpelers_MaaktSpelMetSpelers() {
		assertEquals(geldigSpelers, spelMetSpelers.getSpelers());
	}

	@Test
	void maakSpel_GeldigSpelEnSpeler1NeemtTweeDezelfdeFiches_Speler1MetTweeDezelfdeFiches() {
		spelMetSpelers.neemTweeDezelfdeFiches(0);
		assertEquals(2, spelMetSpelers.geefSpeler(GELDIGE_SPELER_1).getFiches()[0]);
	}

	@Test
	void maakSpel_GeldigSpelEnSpeler4Neemt3VerschillendeFiches_Speler4Met3VerschillendeFiches() {
		drieVerschillendeFiches.add(0);
		drieVerschillendeFiches.add(1);
		drieVerschillendeFiches.add(2);
		spelMetSpelers.neemVerschillendeFiches(drieVerschillendeFiches);
		assertEquals(3, spelMetSpelers.geefSpeler(GELDIGE_SPELER_4).geefTotaalAantalFiches());
	}

	@Test
	void maakSpel_Speler1GenoegFichesKooptOntwikkelingskaartNiveau1_Speler1MetOntwikkelingskaartNiveau1() {
		// ARRANGE
		// geef 4 blauwe fiches aan speler 1
		spelMetSpelers.geefSpeler(GELDIGE_SPELER_1).voegFichesToe(2, 4);
		// controle of fiches zijn toegevoegd
		assertEquals(4, GELDIGE_SPELER_1.getFiches()[2]);
		// controle voor aantal ontwikkelingskaarten voor kopen (er zijn 40 niveau 1
		// ontwikkelingskaarten in totaal)
		assertEquals(40, spelMetSpelers.getNiveau1().size());
		// controle voor aantal ontwikkelingskaarten bij speler voor kopen
		assertEquals(0, GELDIGE_SPELER_1.getOntwikkelingskaarten().size());
		// ACT
		// koop ontwikkelingskaart met id 1
		spelMetSpelers.koopOntwikkelingskaart(okNiveau1.getId(), okNiveau1.getNiveau());
		// ASSERT
		// controle of gekochte ontwikkelingskaart overeen komt met die van de speler
		assertEquals(okNiveau1.getId(),
				spelMetSpelers.geefSpeler(GELDIGE_SPELER_1).getOntwikkelingskaarten().get(0).getId());
		// controle voor aantal ontwikkelingskaarten na kopen (40 - 1)
		assertEquals(39, spelMetSpelers.getNiveau1().size());
		// controle voor aantal ontwikkelingskaarten bij speler na kopen
		assertEquals(1, GELDIGE_SPELER_1.getOntwikkelingskaarten().size());
	}

	@Test
	void maakSpel_Speler1NietGenoegFichesKooptOntwikkelingskaartNiveau1_WerptException() {
		assertThrows(IllegalArgumentException.class,
				() -> spelMetSpelers.koopOntwikkelingskaart(okNiveau1.getId(), okNiveau1.getNiveau()));
	}

	@Test
	void maakSpel_Speler2NetNietGenoegFichesKooptOntiwikkelingskaartNiveau1_WerptException() {
		GELDIGE_SPELER_2.voegFichesToe(2, 3);
		assertThrows(IllegalArgumentException.class,
				() -> spelMetSpelers.koopOntwikkelingskaart(okNiveau1.getId(), okNiveau1.getNiveau()));
	}

	@Test
	void maakSpel_Speler3MeerDanGenoegFichesKooptOntiwikkelingskaartNiveau1_Speler1MetOntwikkelingskaartNiveau1() {
		// idem met
		// maakSpel_Speler1GenoegFichesKooptOntwikkelingskaartNiveau1_Speler1MetOntwikkelingskaartNiveau1()
		GELDIGE_SPELER_3.voegFichesToe(2, 5);
		spelMetSpelers.koopOntwikkelingskaart(okNiveau1.getId(), okNiveau1.getNiveau());
		assertEquals(okNiveau1.getId(),
				spelMetSpelers.geefSpeler(GELDIGE_SPELER_3).getOntwikkelingskaarten().get(0).getId());
		// controle speler heeft nog 1 fiche over
		assertEquals(1, GELDIGE_SPELER_3.geefTotaalAantalFiches());
		assertEquals(1, GELDIGE_SPELER_3.getFiches()[2]);
	}

}
