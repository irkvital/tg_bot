package com.example.bot.data_base;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.example.bot.data_base.entity.DbLocation;
import com.example.bot.data_base.entity.DbUser;
import com.example.bot.data_base.service.LocationService;
import com.example.bot.data_base.service.UserService;

@DataJpaTest
@ComponentScan
public class RelationTests {
    @Autowired
    UserService userService;
    @Autowired
    LocationService locationService;

    @Test
    public void FirstRelationTest() {
        DbUser user = new DbUser(0, "testUser", "First", "Last");
        DbLocation loc0 = new DbLocation(user, 0.3, 1.6);
        DbLocation loc1 = new DbLocation(user, 0.34, 1.2);
        locationService.saveAll(Arrays.asList(loc0, loc1));

        assertAll(
            () -> assertEquals(2, locationService.count()),
            () -> assertEquals(1, userService.count())
        );
    }
}
