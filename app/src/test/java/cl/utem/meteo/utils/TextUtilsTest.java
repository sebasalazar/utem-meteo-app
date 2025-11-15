package cl.utem.meteo.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TextUtilsTest {

    public TextUtilsTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of normalize method, of class TextUtils.
     */
    @Test
    public void testNormalize() {
        System.out.println("normalize");
        
        // ARANGE
        String text = " Un   Ñandú         Chileno ";
        String expResult = "Un Ñandú Chileno";
        
        // ACT
        String result = TextUtils.normalize(text);
        
        // Assert
        Assertions.assertEquals(expResult, result);        
    }

    /**
     * Test of upper method, of class TextUtils.
     */
    @Test
    public void testUpper() {
        System.out.println("upper");
        // ARRANGE
        String text = " ñandú    2 ";
        String expResult = "ÑANDÚ 2";
        
        // ACT
        String result = TextUtils.upper(text);
        
        // ASSERT
        Assertions.assertEquals(expResult, result);        
    }

    /**
     * Test of lower method, of class TextUtils.
     */
    @Test
    public void testLower() {
        System.out.println("lower");
        // ARRANGE
        String text = null;
        String expResult = "";
        
        // ACT
        String result = TextUtils.lower(text);
        
        // ASSERT
        Assertions.assertEquals(expResult, result);        
    }
}
