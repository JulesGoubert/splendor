package domein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exceptions.TeVeelSpelersException;
import persistentie.SpelerMapper;

public class SpelerRepository {

	private List<Speler> spelers;
	private final SpelerMapper sm;
	public static final int MAX_AANTAL_SPELERS = 4;

	public SpelerRepository() {
		sm = new SpelerMapper();
		spelers = new ArrayList<>();
	}
	
	public void meldAan(Speler speler) {
		List<Speler> spelersInDatabank = sm.geefSpelers();
		
		controleerAantalSpelers();
		
		if (spelers.contains(speler)) {
			throw new IllegalArgumentException("alAangemeld");
		}
		
		if (!spelersInDatabank.contains(speler)) {
			throw new IllegalArgumentException("nietGeregistreerd");
		}
		
		spelers.add(speler);
	}
	
	public void registreer(Speler speler) {
		List<Speler> spelersInDatabank = sm.geefSpelers();
		
		controleerAantalSpelers();
		
		if (spelersInDatabank.contains(speler)) {
			throw new IllegalArgumentException("alGeregistreerd");
		}
		
		sm.voegSpelerToe(speler);
//		spelers.add(speler);
	}
	
	public void meldAf(String gebruikersnaam, int geboortedatum) {
		Speler speler = geefSpeler(new Speler(gebruikersnaam, geboortedatum));
		spelers.remove(speler);
	}

	private Speler geefSpeler(Speler speler) {
		for (Speler s : spelers) {
			if (s.equals(speler)) {
				return s;
			}
		}
		
		return null;
	}

	// gebruikt in CUI
	public void voegSpelerToe(int index) {
		List<Speler> spelersInDatabank = sm.geefSpelers();
		
		controleerAantalSpelers();

		if (index < 0 || index > spelersInDatabank.size() - 1) {
			throw new IllegalArgumentException(
					String.format("Het nummer van de opgegeven speler moet in het interval [0, %d] liggen",
							spelersInDatabank.size() - 1));
		}
		
		spelers.add(spelersInDatabank.get(index));
	}

	private void controleerAantalSpelers() {
		if (spelers.size() >= MAX_AANTAL_SPELERS) {
			throw new TeVeelSpelersException();
		}
	}

	public List<Speler> getSpelers() {
		return spelers;
	}

	public void bepaalStartSpeler() {
		if (spelers.size() == 0) {
			return;
		}
		
		List<Speler> copy = new ArrayList<>(spelers);
		Collections.sort(copy);
		Speler startSpeler = copy.get(0);
		startSpeler.setStartSpeler(true);
		spelers.remove(startSpeler);
		spelers.add(0, startSpeler);
	}

	public int aantalSpelers() {
		return spelers.size();
	}

	public List<Speler> geefSpelersUitDatabank() {
		return sm.geefSpelers();
	}

}