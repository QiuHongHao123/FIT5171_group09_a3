package rockets.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

public class RocketUnitTest {
	private Rocket target;

    @BeforeEach
    public void setUp() {
    	target=new Rocket("LM-9","China",new LaunchServiceProvider("CALT", 2000, "AES"));
    }

    @AfterEach
    public void tearDown() {
    }

    @DisplayName("should create rocket successfully when given right parameters to constructor")
    @Test
    public void shouldConstructRocketObject() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertNotNull(bfr);
    }

    @DisplayName("should throw exception when given null manufacturer to constructor")
    @Test
    public void shouldThrowExceptionWhenNoManufacturerGiven() {
        String name = "BFR";
        String country = "USA";
        assertThrows(NullPointerException.class, () -> new Rocket(name, country, null));
    }

    @DisplayName("should set rocket massToLEO value")
    @ValueSource(strings = {"10000", "15000"})
    public void shouldSetMassToLEOWhenGivenCorrectValue(String massToLEO) {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");

        Rocket bfr = new Rocket(name, country, manufacturer);

        bfr.setMassToLEO(massToLEO);
        assertEquals(massToLEO, bfr.getMassToLEO());
    }

    @DisplayName("should throw exception when set massToLEO to null")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertThrows(NullPointerException.class, () -> bfr.setMassToLEO(null));
    }
  
    @DisplayName("should return true when two rockets have the same infomation")
    @Test
    public void shouldReturnTrueWhenRocketsHaveSameINfo() {
        String name = "LM-9";
        String country="China";
        LaunchServiceProvider manufacturer=new LaunchServiceProvider("CALT", 2000, "AES");
        Rocket anotherRocket = new Rocket(name,country,manufacturer);
        assertTrue(target.equals(anotherRocket));
    }
    @DisplayName("should return true when two rockets have different infomation")
    @Test
    public void shouldReturnTrueWhenRocketsHaveDifferentINfo() {
        String name = "Saturn V";
        String country="USA";
        String manufacturer="NASA";
        Rocket anotherRocket = new Rocket(name,country,new LaunchServiceProvider("CALT", 2000, "China"));
        assertFalse(target.equals(anotherRocket));
    }
   
    static Stream<Arguments> LEOGTOGeneratorOne(){
        return Stream.of(Arguments.of("16500", "6500"), Arguments.of("34900","16300"), Arguments.of("140000", "66000"));
    }
    
    static Stream<Arguments> nameAndFamilyGenerator(){
        return Stream.of(Arguments.of("Delta V", "Delta"), Arguments.of("Falcon 9","Falcon"), Arguments.of("Ariane 5[8]", "Ariane"));
    }
}