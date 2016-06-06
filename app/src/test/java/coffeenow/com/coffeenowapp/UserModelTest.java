package coffeenow.com.coffeenowapp;

import org.junit.Test;

import coffeenow.com.coffeenowapp.models.User;

import static org.junit.Assert.*;

public class UserModelTest {

    @Test
    public void userDisplayTest() {
        User user = new User("123", "Joe");

        assertEquals("Joe", user.toString());
    }
}
