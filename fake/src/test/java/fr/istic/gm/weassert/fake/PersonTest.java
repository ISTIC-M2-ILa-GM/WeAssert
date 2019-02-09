package fr.istic.gm.weassert.fake;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * A fake Test class to try to generate assert
 */
public class PersonTest {

    @Test
    public void testAge() {

        Person p = new Person();
        p.setAge(13);
        p.setName("name");

        assertFalse(p.isAdult());
    org.junit.Assert.assertEquals(p.getName(),"name"); // GENERATED ASSERT
org.junit.Assert.assertEquals(p.getAge(),13); // GENERATED ASSERT
}
}