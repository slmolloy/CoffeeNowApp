package coffeenow.com.coffeenowapp;

import org.junit.Test;

import coffeenow.com.coffeenowapp.models.CoffeeMaker;

import static org.junit.Assert.*;

public class CoffeeMakerModelTest {

    @Test
    public void coffeeMakerDisplay() {
        CoffeeMaker cm = new CoffeeMaker("test");

        cm.setVolume(1);
        assertEquals("test (1 cups)", cm.toString());

        cm.setLatitude(0.1);
        cm.setLongitude(0.1);
        assertEquals("test (1 cups) -- Lat: 0.100 Long: 0.100", cm.toString());
    }
}