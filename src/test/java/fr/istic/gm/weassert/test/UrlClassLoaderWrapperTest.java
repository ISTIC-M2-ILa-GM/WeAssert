package fr.istic.gm.weassert.test;

import fr.istic.gm.weassert.TestRunnerAppTest;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl.LOAD_CLASS_ERROR;
import static fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl.PARSED_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(MockitoJUnitRunner.class)
public class UrlClassLoaderWrapperTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldReturnTheClass() {

        List<String> paths = Arrays.asList("file://target/classes", "file:///target/test-classes");
        List<String> classNames = Collections.singletonList("fr.istic.gm.weassert.TestRunnerAppTest");

        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(paths, classNames);

        assertThat(urlClassLoaderWrapper.getClassList(), notNullValue());
        assertThat(urlClassLoaderWrapper.getClassList(), hasSize(1));
        assertThat(urlClassLoaderWrapper.getClassList().get(0), equalTo(TestRunnerAppTest.class));
    }

    @Test
    public void shouldReturnAllClass() {

        List<String> paths = Arrays.asList("file://target/classes", "file:///target/test-classes");
        List<String> classNames = Arrays.asList("fr.istic.gm.weassert.TestRunnerAppTest", "fr.istic.gm.weassert.test.UrlClassLoaderWrapperTest");

        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(paths, classNames);

        assertThat(urlClassLoaderWrapper.getClassList(), notNullValue());
        assertThat(urlClassLoaderWrapper.getClassList(), hasSize(2));
        assertThat(urlClassLoaderWrapper.getClassList().get(0), equalTo(TestRunnerAppTest.class));
        assertThat(urlClassLoaderWrapper.getClassList().get(1), equalTo(UrlClassLoaderWrapperTest.class));
    }

    @Test
    public void shouldThrowExceptionWhenWrongPathIsSent() {

        List<String> paths = Collections.singletonList("target/classes");
        List<String> classNames = Arrays.asList("fr.istic.gm.weassert.TestRunnerAppTest", "fr.istic.gm.weassert.test.UrlClassLoaderWrapperTest");

        thrown.expect(WeAssertException.class);
        thrown.expectMessage(PARSED_ERROR);

        new UrlClassLoaderWrapperImpl(paths, classNames);
    }

    @Test
    public void shouldThrowExceptionWhenClassNotFound() {

        List<String> paths = Arrays.asList("file://target/classes", "file:///target/test-classes");
        List<String> classNames = Collections.singletonList("fr.istic.gm.weassert.NotAClass");

        thrown.expect(WeAssertException.class);
        thrown.expectMessage(LOAD_CLASS_ERROR);

        new UrlClassLoaderWrapperImpl(paths, classNames);
    }
}
