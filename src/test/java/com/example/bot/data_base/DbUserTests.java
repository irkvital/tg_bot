package com.example.bot.data_base;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import com.example.bot.data_base.entity.DbUser;
import com.example.bot.data_base.service.UserService;

@DataJpaTest
@ComponentScan
public class DbUserTests {
    @Autowired
    UserService userService;

    @Test
    public void UserCountTest() {
        DbUser user = new DbUser(0, "testUser", "First", "Last");
        userService.save(user);
        assertEquals(1, userService.count());
    }

    public void UserSaveTest() {
        DbUser user = new DbUser(0, "testUser", "First", "Last");
        userService.save(user);
        assertEquals(user, userService.findById(0));
    }

    @Test
    public void UserSetDateTest() {
        DbUser user = new DbUser(0, "testUser", "First", "Last");
        user.SetDate();
        assertEquals(LocalDate.now(), user.getDate());
    }

    @Test
    public void UserExistByIdTrueTest() {
        DbUser user0 = new DbUser(0, "testUser0", "First0", "Last0");
        DbUser user1 = new DbUser(1, "testUser1", "First1", "Last1");
        DbUser user2 = new DbUser(2, "testUser2", "First2", "Last2");
        userService.save(user0);
        userService.save(user1);
        userService.save(user2);
        
        assertAll(
            () -> assertTrue(userService.existsById(0)),
            () -> assertTrue(userService.existsById(1)),
            () -> assertTrue(userService.existsById(2))
        );
    }

    @Test
    public void UserExistByIdFalseTest() {
        DbUser user0 = new DbUser(0, "testUser0", "First0", "Last0");
        DbUser user1 = new DbUser(1, "testUser1", "First1", "Last1");
        DbUser user2 = new DbUser(2, "testUser2", "First2", "Last2");
        userService.save(user0);
        userService.save(user1);
        userService.save(user2);

        assertAll(
            () -> assertFalse(userService.existsById(5)),
            () -> assertFalse(userService.existsById(6)),
            () -> assertFalse(userService.existsById(7))
        );
    }

    @Test
    public void UserFindByIdTest() {
        DbUser user0 = new DbUser(0, "testUser0", "First0", "Last0");
        DbUser user1 = new DbUser(1, "testUser1", "First1", "Last1");
        DbUser user2 = new DbUser(2, "testUser2", "First2", "Last2");
        userService.save(user0);
        userService.save(user1);
        userService.save(user2);

        assertAll(
            () -> assertEquals(user0, userService.findById(0)),
            () -> assertEquals(user1, userService.findById(1)),
            () -> assertEquals(user2, userService.findById(2))
        );
    }

    @Test
    public void UserFindByIdExeptionTest() {
        DbUser user0 = new DbUser(0, "testUser0", "First0", "Last0");
        userService.save(user0);

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.findById(10));
    }

    @Test
    public void UserReferenceByIdTest() {
        long user_id = 0;
        DbUser user = new DbUser(user_id, "testUser", "First", "Last");
        userService.save(user);

        DbUser user1 = userService.getReference(user_id);
        System.out.println(user1);
        assertEquals(user, user1);
    }
}
