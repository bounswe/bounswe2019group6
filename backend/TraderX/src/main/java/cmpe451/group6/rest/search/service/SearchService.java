package cmpe451.group6.rest.search.service;

import cmpe451.group6.authorization.model.User;
import cmpe451.group6.helpers.CustomModelMapper;
import cmpe451.group6.rest.article.model.Article;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.follow.service.FollowService;
import cmpe451.group6.rest.search.dto.ArticleSearchDTO;
import cmpe451.group6.rest.search.dto.EquipmentSearchDTO;
import cmpe451.group6.rest.search.dto.UserSearchDTO;
import cmpe451.group6.rest.search.repository.SearchArticleRepository;
import cmpe451.group6.rest.search.repository.SearchEquipmentRepository;
import cmpe451.group6.rest.search.repository.SearchUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Service
public class SearchService {

    @Autowired
    private SearchEquipmentRepository searchEquipmentRepository;

    @Autowired
    private SearchUserRepository searchUserRepository;

    @Autowired
    private SearchArticleRepository searchArticleRepository;

    @Autowired
    private FollowService followService;

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

    public List<ArticleSearchDTO> getArticlesByHeader(String header, String requesterName){
        List<Article> articles =  searchArticleRepository.articleFindByHeaderContainingIgnoreCase(header);

        articles.removeIf(a -> !followService.isPermitted(a.getUser().getUsername(), requesterName));

        List<ArticleSearchDTO> articleSearchDTOs = new ArrayList<ArticleSearchDTO>();
        articles
                .forEach(item -> articleSearchDTOs.add(modelMapper.map(item, ArticleSearchDTO.class)));
        return articleSearchDTOs;
    }

    public List<ArticleSearchDTO> getArticlesByTag(String tag, String requesterName){
        List<Article> articles =  searchArticleRepository.findAll();

        articles.removeIf(a -> !tagContains(a.getTags(), tag) || !followService.isPermitted(a.getUser().getUsername(), requesterName));

        List<ArticleSearchDTO> articleSearchDTOs = new ArrayList<ArticleSearchDTO>();
        articles
                .forEach(item -> articleSearchDTOs.add(modelMapper.map(item, ArticleSearchDTO.class)));
        return articleSearchDTOs;
    }

    private boolean tagContains(List<String> arr, String tag){
        for(String s : arr){
            if(s.toLowerCase().contains(tag.toLowerCase())){
                return true;
            }
        }
        return false;
    }

}
