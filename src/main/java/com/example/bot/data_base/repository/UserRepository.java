package com.example.bot.data_base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bot.data_base.entity.DbUser;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Long> {
}
