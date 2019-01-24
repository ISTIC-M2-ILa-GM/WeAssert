package fr.istic.gm.weassert.test.utils;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.impl.ClassReaderFactoryImpl;
import jdk.internal.org.objectweb.asm.ClassReader;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class ClassReaderFactoryTest {

    private ClassReaderFactory factory;

    @Before
    public void setUp() {
        factory = new ClassReaderFactoryImpl();
    }

    @Test
    public void shouldCreateClassReader() {

        ClassReader result = factory.create(getClass());

        assertThat(result, notNullValue());
    }

    @Test(expected = WeAssertException.class)
    public void shouldThrowExceptionWhenCreateClassReaderWithAWrongPath() {
        factory.create(getClass());
    }
}
