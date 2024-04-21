package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domein.Ontwikkelingskaart;

public class OntwikkelingskaartMapper {

	public List<Ontwikkelingskaart> geefOntwikkelingskaarten() {
		List<Ontwikkelingskaart> kaarten = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL, "ID399434_G01", "08Groep01");
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM ID399434_G01.Ontwikkelingskaart");
				ResultSet rs = statement.executeQuery()) {

			while (rs.next()) {
				int id 					= rs.getInt(1);
				String bonus            = rs.getString(4);
				int prestigePunten      = rs.getInt(3);
				int niveau              = rs.getInt(2);
				int vereisteBonusGroen  = rs.getInt(5);
				int vereisteBonusWit    = rs.getInt(6);
				int vereisteBonusBlauw  = rs.getInt(7);
				int vereisteBonusZwart  = rs.getInt(8);
				int vereisteBonusRood   = rs.getInt(9);

				kaarten.add(new Ontwikkelingskaart(id, bonus, prestigePunten, niveau, new int[] { vereisteBonusGroen,
						vereisteBonusWit, vereisteBonusBlauw, vereisteBonusZwart, vereisteBonusRood }));
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return kaarten;
	}
	
}
