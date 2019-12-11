package cmpe451.group6.rest.search.service;

import cmpe451.group6.authorization.model.User;
import cmpe451.group6.helpers.CustomModelMapper;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.search.dto.EquipmentSearchDTO;
import cmpe451.group6.rest.search.dto.UserSearchDTO;
import cmpe451.group6.rest.search.repository.SearchEquipmentRepository;
import cmpe451.group6.rest.search.repository.SearchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchEquipmentRepository searchEquipmentRepository;

    @Autowired
    private SearchUserRepository searchUserRepository;

    @Autowired
    private CustomModelMapper modelMapper;

    public List<EquipmentSearchDTO> getEquipmentsByName(String name, String requesterName) {
        List<Equipment> equipments = searchEquipmentRepository.equipmentFindByNameContainingIgnoreCase(name);

        List<EquipmentSearchDTO> equipmentSearchDTOs = new ArrayList<EquipmentSearchDTO>();
        equipments
                .forEach(item -> equipmentSearchDTOs.add(modelMapper.map(item, EquipmentSearchDTO.class)));
        return equipmentSearchDTOs;

    }


    public List<EquipmentSearchDTO> getEquipmentsByCode(String code, String requesterName) {
        List<Equipment> equipments = searchEquipmentRepository.equipmentFindByCodeContainingIgnoreCase(code);

        List<EquipmentSearchDTO> equipmentSearchDTOs = new ArrayList<EquipmentSearchDTO>();
        equipments
                .forEach(item -> equipmentSearchDTOs.add(modelMapper.map(item, EquipmentSearchDTO.class)));
        return equipmentSearchDTOs;
    }

    public List<UserSearchDTO> getUsersByName(String name, String requesterName){
        List<User> users = searchUserRepository.userFindByNameContainingIgnoreCase(name);

        List<UserSearchDTO> userSearchDTOs = new ArrayList<UserSearchDTO>();
        users
                .forEach(item -> userSearchDTOs.add(modelMapper.map(item, UserSearchDTO.class)));
        return userSearchDTOs;
    }

}
