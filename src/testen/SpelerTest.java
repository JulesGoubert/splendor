package testen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Ontwikkelingskaart;
import domein.Speler;
import persistentie.OntwikkelingskaartMapper;

class SpelerTest {
	
	private static final String GELDIGE_GEBRUIKERSNAAM = "Test_test 123";
	private static final int GELDIGE_GEBOORTEJAAR = 2003;
	private Speler speler;
	OntwikkelingskaartMapper okmapper = new OntwikkelingskaartMapper();
	List<Ontwikkelingskaart> kaarten = okmapper.geefOntwikkelingskaarten();
	Ontwikkelingskaart okNiveau1, okNiveau2, okNiveau3;
	
	@BeforeEach
	void before() {
		speler = new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIGE_GEBOORTEJAAR);
		okNiveau1 = kaarten.get(0);
		okNiveau2 = kaarten.get(41);
		okNiveau3 = kaarten.get(71);
	}

	@Test
	void maakSpeler_GeldigeParameters_MaaktSpeler() {
		assertEquals(GELDIGE_GEBRUIKERSNAAM, speler.getGebruikersnaam());
		assertEquals(GELDIGE_GEBOORTEJAAR, speler.getGeboortejaar());
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 2017, 2003 })
	void maakSpeler_GeldigeGebruikersnaam_RandwaardeGeboortejaar_MaaktSpeler(int geboortejaar) {
		Speler speler = new Speler(GELDIGE_GEBRUIKERSNAAM, geboortejaar);
		assertEquals(GELDIGE_GEBRUIKERSNAAM, speler.getGebruikersnaam());
		assertEquals(geboortejaar, speler.getGeboortejaar());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { " ",  " \t   \n\t \n   ", "_Test", "123Test", " test", "test-" })
	void maakSpeler_GeldigGeboortejaar_OngeldigeGebruikersnaam_WerptException(String gebruikersnaam) {
		assertThrows(IllegalArgumentException.class, () -> new Speler(gebruikersnaam, GELDIGE_GEBOORTEJAAR));
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 2018, 2020, 23000 })
	void maakSpeler_OngeldigGeboortejaar_GeldigeGebruikersnaam_WerptException(int geboortejaar) {
		assertThrows(IllegalArgumentException.class, () -> new Speler(GELDIGE_GEBRUIKERSNAAM, geboortejaar));
	}
	
	@Test
	void maakSpeler_VoegOntwikkelingskaartToeAanSpeler_SpelerMetOntwikkelingskaart() {
		speler.voegOntwikkelingskaartToe(okNiveau1);
		speler.voegOntwikkelingskaartToe(okNiveau2);
		speler.voegOntwikkelingskaartToe(okNiveau3);
		assertEquals(okNiveau1, speler.getOntwikkelingskaarten().get(0));
		assertEquals(okNiveau2, speler.getOntwikkelingskaarten().get(1));
		assertEquals(okNiveau3, speler.getOntwikkelingskaarten().get(2));
	}
	
	@Test
	void maakSpeler_VoegFichesToeAanSpeler_SpelerMetFiches() {
		assertEquals(0, speler.geefTotaalAantalFiches());
		speler.voegFichesToe(0, 1);
		speler.voegFichesToe(1, 2);
		speler.voegFichesToe(2, 3);
		speler.voegFichesToe(3, 4);
		speler.voegFichesToe(4, 5);
		assertEquals(1, speler.getFiches()[0]);
		assertEquals(2, speler.getFiches()[1]);
		assertEquals(3, speler.getFiches()[2]);
		assertEquals(4, speler.getFiches()[3]);
		assertEquals(5, speler.getFiches()[4]);
	}
	
	@Test
	void maakSpeler_VerwijderFichesVanSpeler_SpelerZonderFiches() {
		maakSpeler_VoegFichesToeAanSpeler_SpelerMetFiches();
		speler.neemFichesWeg(0, 1);
		speler.neemFichesWeg(1, 2);
		speler.neemFichesWeg(2, 3);
		speler.neemFichesWeg(3, 4);
		speler.neemFichesWeg(4, 5);
		assertEquals(0, speler.getFiches()[0]);
		assertEquals(0, speler.getFiches()[1]);
		assertEquals(0, speler.getFiches()[2]);
		assertEquals(0, speler.getFiches()[3]);
		assertEquals(0, speler.getFiches()[4]);	
	}
	
}
