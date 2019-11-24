package cmpe451.group6.rest.transaction.dto;

import cmpe451.group6.rest.transaction.model.Transaction;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomModelMapper extends ModelMapper {

    //custom mapper
    PropertyMap<Transaction, TransactionDTO> customMap = new PropertyMap<Transaction, TransactionDTO>() {
        @Override
        protected void configure() {
            map().setCreatedAt(source.getDate().toString());
        }
    };

    public CustomModelMapper(){
        super();
        this.addMappings(customMap);
    }
}
