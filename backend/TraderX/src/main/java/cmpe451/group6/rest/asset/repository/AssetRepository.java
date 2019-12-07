package cmpe451.group6.rest.asset.repository;

import cmpe451.group6.rest.asset.model.Asset;
import cmpe451.group6.rest.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Integer> {

    @Query("SELECT a FROM Asset a WHERE a.user.username=?1 AND a.equipment.code=?2")
    Asset getAsset(String user, String equipment);

    @Query("SELECT a FROM Asset a WHERE a.user.username=?1")
    List<Asset> findByUser_username(String user);

}
