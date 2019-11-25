package cmpe451.group6.rest.portfolio.dto;

import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.portfolio.model.Portfolio;

import java.util.List;

public class PortfolioResponseDTO {

    private List<PortfolioEquipmentDTO> equipmentsInPortfolio;

    public PortfolioResponseDTO(List<PortfolioEquipmentDTO> equipmentsInPortfolio) {
        this.equipmentsInPortfolio = equipmentsInPortfolio;
    }

    public List<PortfolioEquipmentDTO> getEquipmentsInPortfolio() {
        return equipmentsInPortfolio;
    }

    public void setEquipmentsInPortfolio(List<PortfolioEquipmentDTO> equipmentsInPortfolio) {
        this.equipmentsInPortfolio = equipmentsInPortfolio;
    }
}
