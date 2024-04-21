package domein;

import java.util.Objects;

public class Ontwikkelingskaart {
	
	private int id;
	private String bonus;
	private int prestigepunten;
	private int niveau;
	private int[] vereisteFiches;

	public Ontwikkelingskaart(int id, String bonus, int prestigepunten, int niveau, int[] vereisteFiches) {
		setId(id);
		setBonus(bonus);
		setPrestigepunten(prestigepunten);
		setNiveau(niveau);
		setVereisteFiches(vereisteFiches);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBonus() {
		return bonus;
	}

	public int getPrestigepunten() {
		return prestigepunten;
	}

	public int getNiveau() {
		return niveau;
	}

	public int[] getVereisteFiches() {
		return vereisteFiches;
	}

	private void setBonus(String bonus) {
		this.bonus = bonus;
	}

	private void setPrestigepunten(int prestigepunten) {
		this.prestigepunten = prestigepunten;
	}

	private void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	private void setVereisteFiches(int[] vereisteFiches) {
		this.vereisteFiches = vereisteFiches;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, niveau);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ontwikkelingskaart other = (Ontwikkelingskaart) obj;
		return id == other.id && niveau == other.niveau;
	}

}
