package domein;

import java.util.ArrayList;
import java.util.List;

import dto.EdeleDTO;
import dto.OntwikkelingskaartDTO;
import dto.SpelerDTO;

public class DomeinController {

	private SpelerRepository spelerRepo;
	private Spel spel;

	public DomeinController() {
		spelerRepo = new SpelerRepository();
	}

	public void startSpel() {
		spelerRepo.bepaalStartSpeler();
		spel = new Spel(spelerRepo.getSpelers());
	}

	public void voegSpelerToe(int index) {
		spelerRepo.voegSpelerToe(index);
	}
	
	public void meldAan(String gebruikersnaam, int geboortejaar) {
		spelerRepo.meldAan(new Speler(gebruikersnaam, geboortejaar));
	}
	
	public void registreer(String gebruikersnaam, int geboortejaar) {
		spelerRepo.registreer(new Speler(gebruikersnaam, geboortejaar));
	}
	
	public void meldAf(String gebruikersnaam, int geboortejaar) {
		spelerRepo.meldAf(gebruikersnaam, geboortejaar);
	}

	public List<SpelerDTO> geefSpelers(boolean databank) {
		List<SpelerDTO> dtos = new ArrayList<>();
		List<Speler> spelers;

		if (databank) {
			spelers = spelerRepo.geefSpelersUitDatabank();
		} else {
			spelers = spelerRepo.getSpelers();
		}

		for (Speler speler : spelers) {
			dtos.add(new SpelerDTO(speler.getGebruikersnaam(), speler.getGeboortejaar(), speler.isStartSpeler(),
					speler.getPrestigePunten(), speler.geefEdelen(), speler.geefOntwikkelingskaarten(),
					speler.getFiches()));
		}

		return dtos;
	}

	public int geefAantalSpelers() {
		return spelerRepo.aantalSpelers();
	}

	public boolean isEindeSpel() {
		return spel.isEindeSpel();
	}

	public List<SpelerDTO> geefWinnaars() {
		List<SpelerDTO> dtos = new ArrayList<>();
		List<Speler> lijst = spel.geefWinnaars();

		for (Speler s : lijst) {
			dtos.add(new SpelerDTO(s.getGebruikersnaam(), s.getGeboortejaar(), s.isStartSpeler(), s.getPrestigePunten(),
					s.geefEdelen(), s.geefOntwikkelingskaarten(), s.getFiches()));
		}

		return dtos;
	}
	
	public List<OntwikkelingskaartDTO> geefNiveau1() {
		List<Ontwikkelingskaart> kaarten = spel.getNiveau1();
		List<OntwikkelingskaartDTO> dtos = new ArrayList<>();
		
		for (Ontwikkelingskaart ok : kaarten) {
			dtos.add(new OntwikkelingskaartDTO(ok.getId(), ok.getBonus(), ok.getPrestigepunten(), ok.getNiveau(), ok.getVereisteFiches()));
		}
		
		return dtos;
	}
	
	public List<OntwikkelingskaartDTO> geefNiveau2() {
		List<Ontwikkelingskaart> kaarten = spel.getNiveau2();
		List<OntwikkelingskaartDTO> dtos = new ArrayList<>();
		
		for (Ontwikkelingskaart ok : kaarten) {
			dtos.add(new OntwikkelingskaartDTO(ok.getId(), ok.getBonus(), ok.getPrestigepunten(), ok.getNiveau(), ok.getVereisteFiches()));
		}
		
		return dtos;
	}
	
	public List<OntwikkelingskaartDTO> geefNiveau3() {
		List<Ontwikkelingskaart> kaarten = spel.getNiveau3();
		List<OntwikkelingskaartDTO> dtos = new ArrayList<>();
		
		for (Ontwikkelingskaart ok : kaarten) {
			dtos.add(new OntwikkelingskaartDTO(ok.getId(), ok.getBonus(), ok.getPrestigepunten(), ok.getNiveau(), ok.getVereisteFiches()));
		}
		
		return dtos;
	}

	public List<EdeleDTO> geefEdelen() {
		List<EdeleDTO> dtos = new ArrayList<>();
		List<Edele> edelen = spel.getEdelen();
		
		for (Edele e : edelen) {
			dtos.add(new EdeleDTO(e.getId(), e.getVereisteBonussen()));
		}
		
		return dtos;
	}

	public int[] geefFiches() {
		return spel.getFiches();
	}
	
	public void neemTweeDezelfdeFiches(int kleur) {
		spel.neemTweeDezelfdeFiches(kleur);
	}
	
	public void neemVerschillendeFiches(List<Integer> kleuren) {
		spel.neemVerschillendeFiches(kleuren);
	}
	
	public List<Integer> koopOntwikkelingskaart(int id, int niveau) {
		return spel.koopOntwikkelingskaart(id, niveau);
	}
	
	public void koopEdele(int id) {
		spel.koopEdele(id);
	}
	
	public void slaBeurtOver() {
		spel.verhoogBeurtTeller();
	}
	
	public SpelerDTO geefHuidigeSpeler() {
		Speler s = spel.getHuidigeSpeler();
		return new SpelerDTO(s.getGebruikersnaam(), s.getGeboortejaar(), s.isStartSpeler(), s.getPrestigePunten(),
				s.geefEdelen(), s.geefOntwikkelingskaarten(), s.getFiches());
	}
	
	public void bepaalHuidigeSpeler() {
		spel.setHuidigeSpeler();
	}
	
	public String[] geefEdelstenen() {
		return FicheRepository.EDELSTENEN;
	}
	
	public boolean isAandeBeurt(String gebruikersnaam, int geboortejaar) {
		return spel.isAanDeBeurt(gebruikersnaam, geboortejaar);
	}
	
	public void preload() {
		spel.preload();
	}
	
	public void geefGenoegKaartenOmEdelenTeKopen() {
		spel.geefGenoegKaartenOmEdelenTeKopen();
	}
	
	public void maakWinnaars() {
		spel.maakWinnaars();
	}
 	
}