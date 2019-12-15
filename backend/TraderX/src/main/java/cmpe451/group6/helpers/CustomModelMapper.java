package cmpe451.group6.helpers;

import cmpe451.group6.rest.article.dto.ArticleDTO;
import cmpe451.group6.rest.article.model.Article;
import cmpe451.group6.rest.asset.dto.AssetDTO;
import cmpe451.group6.rest.asset.model.Asset;
import cmpe451.group6.rest.search.dto.ArticleSearchDTO;
import cmpe451.group6.rest.transaction.dto.TransactionDTO;
import cmpe451.group6.rest.transaction.dto.TransactionWithUserDTO;
import cmpe451.group6.rest.transaction.model.Transaction;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Configuration
public class CustomModelMapper extends ModelMapper {

    //custom mapper for TransactionDTO
    PropertyMap<Transaction, TransactionDTO> customMapTransactionDTO = new PropertyMap<Transaction, TransactionDTO>() {
        @Override
        protected void configure() {
            map().setCreatedAt(source.getDate().toString());
        }
    };

    //custom mapper for TransactionWithUserDTO
    PropertyMap<Transaction, TransactionWithUserDTO> customMapTransactionWithUserDTO = new PropertyMap<Transaction, TransactionWithUserDTO>() {
        @Override
        protected void configure() {
            map().setCreatedAt(source.getDate().toString());
            map().setUser(source.getUser().getUsername());
        }
    };

    //custom mapper for AssetDTO
    PropertyMap<Asset, AssetDTO> customMapAssetDTO = new PropertyMap<Asset, AssetDTO>() {
        @Override
        protected void configure() {
            map().setCode(source.getEquipment().getCode());
            map().setUser(source.getUser().getUsername());
        }
    };

    //custom mapper for ArticleDTO
    PropertyMap<Article, ArticleDTO> customMapArticleDTO = new PropertyMap<Article, ArticleDTO>() {
        @Override
        protected void configure() {
            map().setCreatedAt(source.getDate().toString());
            map().setUsername(source.getUser().getUsername());

        }
    };

    //custom mapper for ArticleSearchDTO
    PropertyMap<Article, ArticleSearchDTO> customMapArticleSearchDTO = new PropertyMap<Article, ArticleSearchDTO>() {
        @Override
        protected void configure() {
            map().setUser(source.getUser().getUsername());

        }
    };

    public CustomModelMapper(){
        super();
        this.addMappings(customMapTransactionDTO);
        this.addMappings(customMapTransactionWithUserDTO);
        this.addMappings(customMapAssetDTO);
        this.addMappings(customMapArticleDTO);
        this.addMappings(customMapArticleSearchDTO);
    }
}
