package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.analyser.impl.CodeVisitorImpl;
import fr.istic.gm.weassert.test.model.VariableDefinition;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class CodeVisitorImplTest {

    private CodeVisitor codeVisitor;

    @Before
    public void setUp() {
        codeVisitor = new CodeVisitorImpl();
    }

    @Test
    public void shouldGetValue() {

        assertThat(codeVisitor.getVariableValues(), notNullValue());
    }

    @Test
    public void shouldResetValueWhenInit() {

        VariableDefinition key = new VariableDefinition();
        codeVisitor.getVariableValues().put(key, "a-string");

        codeVisitor.initVariableValues();

        assertThat(codeVisitor.getVariableValues().containsKey(key), equalTo(false));
    }

    @Test
    public void shouldVisit() {

        codeVisitor.visit(getClass(), "a-method", "a-def", "a-var", 10);

        VariableDefinition expectedVariableDefinition = VariableDefinition.builder()
                .clazz(getClass())
                .methodName("a-method")
                .methodDesc("a-def")
                .variableName("a-var")
                .build();

        assertThat(codeVisitor.getVariableValues(), notNullValue());
        assertThat(codeVisitor.getVariableValues().containsKey(expectedVariableDefinition), equalTo(true));
        assertThat(codeVisitor.getVariableValues().get(expectedVariableDefinition), equalTo(10));
    }
}
