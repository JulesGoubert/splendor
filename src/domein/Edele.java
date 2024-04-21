package domein;

import java.util.Objects;

public class Edele {
	
	public static final int PRESTIGEPUNTEN = 3;
	private int id;
	private int[] vereisteBonussen;
	
	public Edele(int id, int[] vereisteBonussen) {
		setId(id);
		setVereisteBonussen(vereisteBonussen);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[] getVereisteBonussen() {
		return vereisteBonussen;
	}

	public void setVereisteBonussen(int[] vereisteBonussen) {
		this.vereisteBonussen = vereisteBonussen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edele other = (Edele) obj;
		return id == other.id;
	}

}
