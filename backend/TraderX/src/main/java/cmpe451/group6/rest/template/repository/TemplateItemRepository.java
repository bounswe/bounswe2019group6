package cmpe451.group6.rest.template.repository;

import cmpe451.group6.rest.template.model.TemplateItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** This interface will help us to retrieve data from database using
 *  object fields directly.
 *
 *  INTERFACE SIGNATURE:
 *  public interface Entity_NameRepository extends JpaRepository<Entity_Name, Type of @Id on Entity_Name class>
 *
 *  [DO NOT CHANGE OR IMPLEMENT THOSE CLASSES / INTERFACES. FOR TEMPLATING PURPOSE ONLY.]
 *
 */

public interface TemplateItemRepository extends JpaRepository<TemplateItem, Integer> {

    // To see the available functions, write Entity_Name (TemplateItem for this case)
    // and write "get" or "find". Then suggestions will be shown to you (talking for IntelliJ IDE.)
    // Various get and find methods by the fields of Entity_Name (TemplateItem) can be found.
    // Include only the ones you need. Do not implement any of them. Remember: this is an interface
    // and going to be implemented automatically.
    // Save to database operations will be performed through this interface. Even if they're not defined here, they
    // are included from JpaRepository interface itself.
    // Read about JpaRepository blog post or etc. for more information.


    // Some examples below:

    TemplateItem findFirstByBoolField(boolean value);

    List<TemplateItem> findByBoolField(boolean value);

    boolean existsByItemId(int id);
}


