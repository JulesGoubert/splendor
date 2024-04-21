package domein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import persistentie.EdeleMapper;

public class EdeleRepository {
	
	private List<Edele> edelen;
	private EdeleMapper em;
	
	public EdeleRepository(int aantal) {
		em = new EdeleMapper();
		setEdelen(aantal);
	}
	
	public List<Edele> getEdelen() {
		return edelen;
	}
	
	private void setEdelen(int aantal) {
		this.edelen = new ArrayList<>();
		List<Edele> alleEdelen = em.geefEdelen();
		schud();
		
		// voegt <aantal> edelen toe aan het attribuut this.edelen
		for (int i = 0; i < aantal; i++) {
			voegEdeleToe(alleEdelen.get(i));
		}
	}
	
	public void voegEdeleToe(Edele edele) {
		edelen.add(edele);
	}
	
	private void schud() {
		Collections.shuffle(edelen);
	}
	
	public void verwijderEdele(Edele e) {
		edelen.remove(e);
	}
	
}
