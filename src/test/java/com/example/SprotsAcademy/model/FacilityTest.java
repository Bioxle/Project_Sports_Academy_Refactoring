
package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FacilityTest {

    private Facility facility;

    @BeforeEach
    void setUp() {
        facility = new Facility("Football Field", 30);
    }

    @Test
    void testFacilityCreation() {
        assertNotNull(facility);

        assertAll(
                () -> assertEquals(
                        "Football Field",
                        facility.getNameInstallation()
                ),
                () -> assertEquals(
                        30,
                        facility.getMaxCapacity()
                )
        );
    }

    @Test
    void testSetNameInstallation() {
        facility.setNameInstallation("Basketball Court");

        assertEquals(
                "Basketball Court",
                facility.getNameInstallation()
        );
    }

    @Test
    void testSetMaxCapacity() {
        facility.setMaxCapacity(50);

        assertEquals(50, facility.getMaxCapacity());
    }

    @Test
    void testToString() {
        assertEquals(
                "Football Field",
                facility.toString()
        );
    }

    @Test
    void testToStringAfterNameChange() {
        facility.setNameInstallation("Swimming Pool");

        assertEquals(
                "Swimming Pool",
                facility.toString()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, 1000})
    void testDifferentCapacities(int capacity) {
        facility.setMaxCapacity(capacity);

        assertEquals(
                capacity,
                facility.getMaxCapacity()
        );
    }

    @Test
    void testNullFacilityName() {
        facility.setNameInstallation(null);

        assertNull(facility.getNameInstallation());
        assertNull(facility.toString());
    }

}
