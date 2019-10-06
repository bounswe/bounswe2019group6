package cmpe451.group6.rest.template.service;


import cmpe451.group6.rest.template.dto.TemplateItemResponseDTO;
import cmpe451.group6.rest.template.model.TemplateItem;
import cmpe451.group6.rest.template.repository.TemplateItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  This class is for the purpose of implementing the functions operating on
 *  the Entities. Remember there was a warn about not to implement and other
 *  methods other than getter and setters on TemplateItem.java.
 *  Now, here is the place to write al the functions you need.
 *  Feel free to implement any of them. Of course, keep them short and clean!
 *
 *  NOTE : Try to call those functions from controller class.
 *
 *  [DO NOT CHANGE OR IMPLEMENT THOSE CLASSES / INTERFACES. FOR TEMPLATING PURPOSE ONLY.]
 */
@Service
public class TemplateItemService {

    // Wire the repository you created for the Entity.
    // You can think that as a singleton that you can use wherever it's "autowired"
    // Don't let the annotation blow your mind!
    @Autowired
    private TemplateItemRepository templateItemRepository;

    // Example:

    public void incrementLongValue(TemplateItem item){
        item.setLongField(item.getLongField()+1);
        templateItemRepository.save(item);
    }

    public boolean deleteItembyId(TemplateItem item){
        if(templateItemRepository.existsByItemId(item.getItemId())){
            templateItemRepository.delete(item.getItemId());
            return true;
        } else {
            return false;
        }
    }
}
