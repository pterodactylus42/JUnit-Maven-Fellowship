package com.vogella.unittest.services;

import static com.vogella.unittest.Race.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.fail;

import com.vogella.unittest.Movie;
import com.vogella.unittest.Ring;
import com.vogella.unittest.TolkienCharacter;
import com.vogella.unittest.services.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.vogella.unittest.services.DataService;

class DataServiceTest {

    // TODO initialize before each test
    // com.vogella.unittest.services.DataService dataService;
    DataService dataService;
    
    @BeforeEach
    void initDataService() {
    	dataService = new DataService();
    }

    @Test
    void ensureThatInitializationOfTolkienCharactersWorks() {
        TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);

        // TODO check that age is 33
        assertEquals(true, frodo.age == 33);
        // TODO check that name is "Frodo"
        assertEquals(true, frodo.getName().equals("Frodo"));
        // TODO check that name is not "Frodon"
        assertEquals(false, frodo.getName().equals("Frodon"));
    }

    @Test
    void ensureThatEqualsWorksForCharaters() {
        Object jake = new TolkienCharacter("Jake", 43, HOBBIT);
        Object sameJake = jake;
        Object jakeClone = new TolkienCharacter("Jake", 12, HOBBIT);
        assertEquals(true, jake.equals(sameJake));
        assertEquals(false, jake.equals(jakeClone));
    }

    @Test
    void checkInheritance() {
        TolkienCharacter tolkienCharacter = dataService.getFellowship().get(0);
        if(tolkienCharacter.getClass().equals(Movie.class)) {
            fail("tolkienCharacter should not be a Movie class.");        	
        }
    }

    @Test
    void ensureFellowShipCharacterAccessByNameReturnsNullForUnknownCharacter() {
        assertEquals(null, dataService.getFellowshipCharacter("Heinz"), "Ensure that dataService.getFellowshipCharacter returns null for unknown character.");
    }

    @Test
    void ensureFellowShipCharacterAccessByNameWorksGivenCorrectNameIsGiven() {
        assertNotNull(dataService.getFellowshipCharacter("Frodo"));
    }


    @Test
    void ensureThatFrodoAndGandalfArePartOfTheFellowsip() {

        List<TolkienCharacter> fellowship = dataService.getFellowship();

        // TODO check that Frodo and Gandalf are part of the fellowship
        assertNotNull(fellowship.stream().filter(s->s.getName() == "Frodo").findFirst().get(), "Ensure that Frodo is in the fellowship.");
        assertNotNull(fellowship.stream().filter(s->s.getName() == "Gandalf").findFirst().get(), "Ensure that Frodo is in the fellowship.");
    }

    @Test
    void ensureThatOneRingBearerIsPartOfTheFellowship() {

        List<TolkienCharacter> fellowship = dataService.getFellowship();
        Map<Ring, TolkienCharacter> ringBearers = dataService.getRingBearers();
        boolean contains = false;
        for(int i = 0; i < fellowship.size(); i++) {
        	if(ringBearers.containsValue(fellowship.get(i)))
        	{
        		contains = true;
        	}
        }
        assertEquals(true, contains, "Ensure that one ring bearer is in the fellowship.");
    }

    // TODO Use @RepeatedTest(int) to execute this test 1000 times
    @RepeatedTest(1000)
    @Tag("slow")
    @DisplayName("Minimal stress testing: run this test 1000 times to ")
    void ensureThatWeCanRetrieveFellowshipMultipleTimes() {
        dataService = new DataService();
        assertNotNull(dataService.getFellowship());
        //fail("this should run 1000 times");
    }

    @Test
    void ensureOrdering() {
        List<TolkienCharacter> fellowship = dataService.getFellowship();

        assertEquals("Frodo", fellowship.get(0).getName(), "Ensure Frodo is first.");
        assertEquals("Sam", fellowship.get(1).getName(), "Ensure Sam is second.");
        assertEquals("Merry", fellowship.get(2).getName(), "Ensure Merry is third.");
        assertEquals("Pippin", fellowship.get(3).getName(), "Ensure Pippin is fourth.");
        assertEquals("Gandalf", fellowship.get(4).getName(), "Ensure Gandalf is fifth.");
        assertEquals("Legolas", fellowship.get(5).getName(), "Ensure Legolas is sixth.");
        assertEquals("Gimli", fellowship.get(6).getName(), "Ensure Gimli is seventh.");
        assertEquals("Aragorn", fellowship.get(7).getName(), "Ensure Aragorn is eighth.");
        assertEquals("Boromir", fellowship.get(8).getName(), "Ensure Boromir is ninth.");
    }

    @Test
    void ensureAge() {
        List<TolkienCharacter> fellowship = dataService.getFellowship();

        // ensure that all hobbits and men are younger than 100 years
        assertEquals(Optional.empty(), fellowship.stream().filter(s->s.getRace() == HOBBIT).filter(s->s.age >= 100).findAny(), "Ensure all hobbits are younger than 100 years.");
        assertEquals(Optional.empty(), fellowship.stream().filter(s->s.getRace() == MAN).filter(s->s.age >= 100).findAny(), "Ensure all men are younger than 100 years.");

        // ensure that elves, dwarfs and maia are all older than 100 years
        assertEquals(Optional.empty(), fellowship.stream().filter(s->s.getRace() == ELF).filter(s->s.age <= 100).findAny(), "Ensure all elves are younger than 100 years.");
        assertEquals(Optional.empty(), fellowship.stream().filter(s->s.getRace() == DWARF).filter(s->s.age <= 100).findAny(), "Ensure all dwarfs are younger than 100 years.");
        assertEquals(Optional.empty(), fellowship.stream().filter(s->s.getRace() == MAIA).filter(s->s.age <= 100).findAny(), "Ensure all maia are younger than 100 years.");

    }

    @Test
    void ensureThatFellowsStayASmallGroup() {
        List<TolkienCharacter> fellowship = dataService.getFellowship();
        assertThrows(IndexOutOfBoundsException.class, () -> fellowship.get(20));   
    }
    
    @Test
    void ensureTimeout() {
    	assertTimeout(Duration.ofMillis(3000), () -> dataService.update());
    }
    
}