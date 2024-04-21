package domein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import persistentie.OntwikkelingskaartMapper;

public class OntwikkelingskaartRepository {
	
	private List<Ontwikkelingskaart> ontwikkelingskaarten;
	private OntwikkelingskaartMapper om;
	
	public OntwikkelingskaartRepository(int niveau) {
		om = new OntwikkelingskaartMapper();
		setOntwikkelingskaarten(niveau);
		schud();
	}
	
	public List<Ontwikkelingskaart> getOntwikkelingskaarten() {
		return ontwikkelingskaarten;
	}

	private void setOntwikkelingskaarten(int niveau) {
		List<Ontwikkelingskaart> kaarten = om.geefOntwikkelingskaarten();
		this.ontwikkelingskaarten = new ArrayList<>();
		
		for (Ontwikkelingskaart kaart : kaarten) {
			if (kaart.getNiveau() == niveau) {
				ontwikkelingskaarten.add(kaart);
			}
		}
	}
	
	public void voegOntwikkelingskaartToe(Ontwikkelingskaart ontwikkelingskaart) {
		ontwikkelingskaarten.add(ontwikkelingskaart);
	}
	
	public void verwijderOntwikkelingskaart(Ontwikkelingskaart ontwikkelingskaart) {
		ontwikkelingskaarten.remove(ontwikkelingskaart);
	}
	
	public Ontwikkelingskaart geefOntwikkelingskaart(int index) {
		return ontwikkelingskaarten.get(index);
	}
	
	public void schud() {
		Collections.shuffle(ontwikkelingskaarten);
	}
	
}
