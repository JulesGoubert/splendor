package domein;

import utils.Kleur;

public class FicheRepository {
    
    private int[] fiches;
    public static final String[] EDELSTENEN = { "smaragd", "diamant", "saffier", "onyx", "robijn" };
    
    public FicheRepository() {
        fiches = new int[Kleur.values().length];
    }
    
    public FicheRepository(int[] fiches) {
        setFiches(fiches);
    }

    private void setFiches(int[] fiches) {
        this.fiches = fiches;
    }
    
    public int[] getFiches() {
        return fiches;
    }
    
    public void voegFichesToe(int kleur, int aantal) {
    	if (aantal < 0) {
    		throw new IllegalArgumentException("nietNegatief");
    	}
        fiches[kleur] += aantal;
    }
    
    public void neemFichesWeg(int kleur, int aantal) {
        if (fiches[kleur] < aantal) {
            throw new IllegalArgumentException("aantalFiches");
        }
        fiches[kleur] -= aantal;
    }
    
    public int geefAantal(int kleur) {
        return fiches[kleur];
    }
    
}