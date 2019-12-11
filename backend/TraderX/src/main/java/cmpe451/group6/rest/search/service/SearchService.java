package cmpe451.group6.rest.search.service;

import cmpe451.group6.helpers.CustomModelMapper;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.search.dto.EquipmentSearchDTO;
import cmpe451.group6.rest.search.repository.SearchEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchEquipmentRepository searchRepository;

    @Autowired
    private CustomModelMapper modelMapper;

    public List<EquipmentSearchDTO> getEquipmentsByName(String name, String requesterName) {
        List<Equipment> equipments = searchRepository.equipmentFindByNameContainingIgnoreCase(name);

        List<EquipmentSearchDTO> equipmentSearchDTOs = new ArrayList<EquipmentSearchDTO>();
        equipments
                .forEach(item -> equipmentSearchDTOs.add(modelMapper.map(item, EquipmentSearchDTO.class)));
        return equipmentSearchDTOs;

    }


    public List<EquipmentSearchDTO> getEquipmentsByCode(String code, String requesterName) {
        List<Equipment> equipments = searchRepository.equipmentFindByCodeContainingIgnoreCase(code);

        List<EquipmentSearchDTO> equipmentSearchDTOs = new ArrayList<EquipmentSearchDTO>();
        equipments
                .forEach(item -> equipmentSearchDTOs.add(modelMapper.map(item, EquipmentSearchDTO.class)));
        return equipmentSearchDTOs;
    }
}
