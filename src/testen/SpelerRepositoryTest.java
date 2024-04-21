package testen;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.Speler;
import domein.SpelerRepository;
import exceptions.TeVeelSpelersException;

import static domein.SpelerRepository.*;

class SpelerRepositoryTest {

	private SpelerRepository spelerRepo;
	private static final Speler GELDIGE_SPELER = new Speler("Test", 2003);
	private static final Speler JULES = new Speler("Jules", 2003);
	private static final Speler ACHILES = new Speler("Achiles", 2002);
	private static final Speler MAROUANE = new Speler("Marouane", 2003);
	private static final Speler EDDY = new Speler("Eddy", 2003);
	
	@BeforeEach
	void before() {
		spelerRepo = new SpelerRepository();
	}
	
	@Test
	void voegSpelerToe_GeldigeSpeler_VoegtSpelerToe() {
		spelerRepo.meldAan(GELDIGE_SPELER);
		assertTrue(spelerRepo.getSpelers().contains(GELDIGE_SPELER));
	}
	
	@Test
	void voegSpelerToe_AlVierSpelers_WerptException() {
		meldSpelersAan();
		assertEquals(MAX_AANTAL_SPELERS, spelerRepo.aantalSpelers());
		assertThrows(TeVeelSpelersException.class, () -> spelerRepo.meldAan(GELDIGE_SPELER));
	}
	
	void meldSpelersAan() {
		spelerRepo.meldAan(ACHILES);
		spelerRepo.meldAan(JULES);
		spelerRepo.meldAan(EDDY);
		spelerRepo.meldAan(MAROUANE);
	}
	
	@Test
	void bespaalStartSpeler_VerschillendeGeboortejaren_bepaaltStartSpeler() {
		spelerRepo.meldAan(GELDIGE_SPELER);
		spelerRepo.meldAan(JULES);
		spelerRepo.bepaalStartSpeler();
		assertTrue(JULES.isStartSpeler());
		assertEquals(2, spelerRepo.aantalSpelers());
	}
	
	@Test
	void bespaalStartSpeler_GelijkeGeboortejaren_bepaaltStartSpeler() {
		spelerRepo.meldAan(MAROUANE);
		spelerRepo.meldAan(JULES);
		spelerRepo.bepaalStartSpeler();
		assertTrue(MAROUANE.isStartSpeler());
		assertEquals(2, spelerRepo.aantalSpelers());
	}
	
	@Test
	void bespaalStartSpeler_GelijkeGeboortejarenGelijkeLengteGebruikersnaam_bepaaltStartSpeler() {
		spelerRepo.meldAan(GELDIGE_SPELER);
		spelerRepo.meldAan(EDDY);
		spelerRepo.bepaalStartSpeler();
		assertTrue(GELDIGE_SPELER.isStartSpeler());
		assertEquals(2, spelerRepo.aantalSpelers());
	}
	
	@Test
	void bespaalStartSpeler_MeerdereSpelers_bepaaltStartSpeler() {
		meldSpelersAan();
		spelerRepo.bepaalStartSpeler();
		assertTrue(MAROUANE.isStartSpeler());
		assertEquals(4, spelerRepo.aantalSpelers());
		assertEquals(MAROUANE, spelerRepo.getSpelers().get(0));
		assertEquals(ACHILES, spelerRepo.getSpelers().get(1));
		assertEquals(JULES, spelerRepo.getSpelers().get(2));
		assertEquals(EDDY, spelerRepo.getSpelers().get(3));
	}

}
