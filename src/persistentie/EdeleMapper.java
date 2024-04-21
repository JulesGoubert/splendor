package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domein.Edele;

public class EdeleMapper {

	public List<Edele> geefEdelen() {
		List<Edele> edelen = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL, "ID399434_G01", "08Groep01");
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM ID399434_G01.Edele");
				ResultSet rs = statement.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt(1);
				int vereisteBonusGroen = rs.getInt(2);
				int vereisteBonusWit = rs.getInt(3);
				int vereisteBonusBlauw = rs.getInt(4);
				int vereisteBonusZwart = rs.getInt(5);
				int vereisteBonusRood = rs.getInt(6);

				edelen.add(new Edele(id, new int[] { vereisteBonusGroen, vereisteBonusWit, vereisteBonusBlauw,
						vereisteBonusZwart, vereisteBonusRood }));
			}

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return edelen;
	}

}
