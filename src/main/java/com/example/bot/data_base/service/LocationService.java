package com.example.bot.data_base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bot.data_base.entity.DbLocation;
import com.example.bot.data_base.repository.LocationRepository;

@Service
public class LocationService{
    @Autowired
    private LocationRepository repo;

    public DbLocation save(DbLocation location) {
        return repo.save(location);
    }

    public long count() {
        return repo.count();
    }
    
}
