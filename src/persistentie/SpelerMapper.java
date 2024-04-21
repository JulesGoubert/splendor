package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domein.Speler;

public class SpelerMapper {

	private static final String INSERT_SPELER = "INSERT INTO ID399434_G01.Speler (gebruikersnaam, geboortejaar)"
			+ "VALUES (?, ?)";

	public void voegSpelerToe(Speler speler) {

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL, "ID399434_G01", "08Groep01");
				PreparedStatement query = conn.prepareStatement(INSERT_SPELER)) {

			query.setString(1, speler.getGebruikersnaam());
			query.setInt(2, speler.getGeboortejaar());
			query.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<Speler> geefSpelers() {
		List<Speler> spelers = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL, "ID399434_G01", "08Groep01");
				PreparedStatement query = conn.prepareStatement("SELECT * FROM ID399434_G01.Speler");
				ResultSet rs = query.executeQuery()) {

			while (rs.next()) {
				String gebruikersnaam = rs.getString("gebruikersnaam");
				int geboortejaar = rs.getInt("geboortejaar");
				spelers.add(new Speler(gebruikersnaam, geboortejaar));
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return spelers;
	}

}
