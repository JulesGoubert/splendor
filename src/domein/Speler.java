package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dto.EdeleDTO;
import dto.OntwikkelingskaartDTO;
import utils.Kleur;

public class Speler implements Comparable<Speler> {

	private String gebruikersnaam;
	private int geboortejaar;
	private boolean startSpeler;
	private int prestigePunten;
	private List<Edele> edelen;
	private List<Ontwikkelingskaart> ontwikkelingskaarten;
	private int[] fiches;

	private static final int MIN_LEEFTIJD = 6;

	public Speler(String gebruikersnaam, int geboortejaar) {
		setGebruikersnaam(gebruikersnaam);
		setGeboortejaar(geboortejaar);
		prestigePunten = 0;
		edelen = new ArrayList<>();
		ontwikkelingskaarten = new ArrayList<>();
		fiches = new int[5];
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	private void setGebruikersnaam(String gebruikersnaam) {
		if (gebruikersnaam == null || gebruikersnaam.isBlank()) {
			throw new IllegalArgumentException("oningevuldeNaam");
		}
		if (!gebruikersnaam.matches("[a-zA-Z].*")) {
			throw new IllegalArgumentException("naamZonderLetter");
		}
		if (!gebruikersnaam.matches("[\\w ]*")) {
			throw new IllegalArgumentException("ongeldigeNaam");
		}

		this.gebruikersnaam = gebruikersnaam;
	}

	public int getGeboortejaar() {
		return geboortejaar;
	}

	private void setGeboortejaar(int geboortejaar) {
		int huidigJaar = LocalDate.now().getYear();

		if (huidigJaar - geboortejaar < MIN_LEEFTIJD) {
			throw new IllegalArgumentException(String.format("ongeldigeLeeftijd"));
		}

		this.geboortejaar = geboortejaar;
	}

	public boolean isStartSpeler() {
		return startSpeler;
	}

	public void setStartSpeler(boolean startspeler) {
		this.startSpeler = startspeler;
	}

	public int getPrestigePunten() {
		return prestigePunten;
	}

	public List<Edele> getEdelen() {
		return edelen;
	}

	public List<Ontwikkelingskaart> getOntwikkelingskaarten() {
		return ontwikkelingskaarten;
	}

	public int[] getFiches() {
		return fiches;
	}

	public void voegFichesToe(int kleur, int aantal) {
		if (aantal < 0) {
			throw new IllegalArgumentException("Aantal mag niet negatief zijn");
		}
		fiches[kleur] += aantal;
	}

	public void neemFichesWeg(int kleur, int aantal) {
		if (fiches[kleur] < aantal) {
			throw new IllegalArgumentException("aantalFiches");
		}

		fiches[kleur] -= aantal;
	}

	public void voegOntwikkelingskaartToe(Ontwikkelingskaart ow) {
		ontwikkelingskaarten.add(ow);
		prestigePunten += ow.getPrestigepunten();
	}

	public void voegEdeleToe(Edele e) {
		edelen.add(e);
		prestigePunten += Edele.PRESTIGEPUNTEN;
	}

	public int geefTotaalAantalFiches() {
		int totaal = 0;

		for (int aantal : fiches) {
			totaal += aantal;
		}

		return totaal;
	}

	public int berekenPrestigePunten() {
		int punten = edelen.size() * Edele.PRESTIGEPUNTEN;

		for (Ontwikkelingskaart o : ontwikkelingskaarten) {
			punten += o.getPrestigepunten();
		}

		return punten;
	}
	
	public int[] berekenBonussen() {
		int[] bonussen = new int[5];
		
		for (Ontwikkelingskaart o : ontwikkelingskaarten) {
			bonussen[Kleur.valueOf(o.getBonus().toUpperCase()).ordinal()]++;
		}
		
		return bonussen;
	}

	public List<OntwikkelingskaartDTO> geefOntwikkelingskaarten() {
		List<OntwikkelingskaartDTO> dtos = new ArrayList<>();

		for (Ontwikkelingskaart o : ontwikkelingskaarten) {
			dtos.add(new OntwikkelingskaartDTO(o.getId(), o.getBonus(), o.getPrestigepunten(), o.getNiveau(),
					o.getVereisteFiches()));
		}

		return dtos;
	}

	public List<EdeleDTO> geefEdelen() {
		List<EdeleDTO> dtos = new ArrayList<>();

		for (Edele e : edelen) {
			dtos.add(new EdeleDTO(e.getId(), e.getVereisteBonussen()));
		}

		return dtos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(geboortejaar, gebruikersnaam);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Speler other = (Speler) obj;
		return geboortejaar == other.geboortejaar && Objects.equals(gebruikersnaam, other.gebruikersnaam);
	}

	@Override
	public int compareTo(Speler s) {
		int sorterenOpGeboortejaar = Integer.compare(s.geboortejaar, geboortejaar);
		int sorterenOpLengteGebruikersnaam = Integer.compare(s.gebruikersnaam.length(), gebruikersnaam.length());
		int sorterenOpGebruikersnaamOmgekeerd = s.gebruikersnaam.compareTo(gebruikersnaam);

		return sorterenOpGeboortejaar != 0 ? sorterenOpGeboortejaar
				: sorterenOpLengteGebruikersnaam != 0 ? sorterenOpLengteGebruikersnaam
						: sorterenOpGebruikersnaamOmgekeerd;
	}

}