package com.example.restapisqlite.repository;

import com.example.restapisqlite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @NonNull
    <S extends User> S save(@NonNull S entity);
}
