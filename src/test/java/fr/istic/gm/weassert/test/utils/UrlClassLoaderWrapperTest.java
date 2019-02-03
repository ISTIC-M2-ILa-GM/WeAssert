package fr.istic.gm.weassert.test.utils;

import fr.istic.gm.weassert.test.utils.impl.UrlClassLoaderWrapperImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static fr.istic.gm.weassert.TestUtils.getAbsolutePath;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(MockitoJUnitRunner.class)
public class UrlClassLoaderWrapperTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldReturnAllClassOfAnExternalProject() {
        List<String> paths = asList(getAbsolutePath("fake/target/classes"), getAbsolutePath("fake/target/test-classes"));

        UrlClassLoaderWrapper urlClassLoaderWrapper = new UrlClassLoaderWrapperImpl(paths);

        assertThat(urlClassLoaderWrapper.getClassList(), notNullValue());
        assertThat(urlClassLoaderWrapper.getClassList(), hasSize(2));
        assertThat(urlClassLoaderWrapper.getClassList().get(0).getSimpleName(), equalTo("Person"));
        assertThat(urlClassLoaderWrapper.getClassList().get(1).getSimpleName(), equalTo("PersonTest"));
    }
}
