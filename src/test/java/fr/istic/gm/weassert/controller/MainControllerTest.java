package fr.istic.gm.weassert.controller;

import fr.istic.gm.weassert.test.application.WeAssertRunner;
import javafx.scene.layout.VBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainControllerTest {

    @Mock
    private VBox mainVBox;

    @Mock
    private WeAssertRunner mockWeAssertRunner;

    @InjectMocks
    private MainController mainController;

    @Test
    public void shouldRunTests() {

        mainController.testAction();

        verify(mockWeAssertRunner).runTests();
    }

    @Test
    public void shouldRunGeneration() {

        mainController.generateAction();

        verify(mockWeAssertRunner).generate();
    }
}