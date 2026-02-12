package com.example.dam.repository;

import com.example.dam.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByFilename(String filename);

    @Query("SELECT a FROM Asset a JOIN a.metadata m WHERE KEY(m) = :key AND VALUE(m) LIKE %:value%")
    List<Asset> findByMetadata(@Param("key") String key, @Param("value") String value);
    
    Optional<Asset> findTopByFilenameOrderByVersionDesc(String filename);
}
