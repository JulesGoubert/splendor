package dto;

import java.util.List;

public record SpelerDTO(String gebruikersnaam, int geboortejaar, boolean startSpeler, int prestigePunten, List<EdeleDTO> edelen, List<OntwikkelingskaartDTO> ontwikkelingskaarten, int[] fiches) {

}
