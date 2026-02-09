package com.example.search.repository;

import com.example.search.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    List<Product> findByNameContaining(String name);
    List<Product> findByDescriptionContaining(String description);
}
