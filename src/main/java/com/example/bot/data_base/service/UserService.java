package com.example.bot.data_base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bot.data_base.entity.DbUser;
import com.example.bot.data_base.repository.UserRepository;

@Service
public class UserService{
    @Autowired
    private UserRepository repo;

    public DbUser save(DbUser user) {
        return repo.save(user);
    }

    public boolean existsById(long id) {
        return repo.existsById(id);
    }

    public DbUser findById(long id) {
        return repo.findById(id).get();
    }

    public long count() {
        return repo.count();
    }
}
