package domein;

import java.util.Comparator;

public class WinnaarComparator implements Comparator<Speler> {

	@Override
	public int compare(Speler s1, Speler s2) {
		// sorteren op basis van prestigepunten van groot naar klein
		int res = Integer.compare(s2.getPrestigePunten(), s1.getPrestigePunten());
		// sorteren op basis van aantal ontwikkelingskaarten van klein naar groot
		return res != 0 ? res : Integer.compare(s1.getOntwikkelingskaarten().size(), s2.getOntwikkelingskaarten().size());
	}

}
