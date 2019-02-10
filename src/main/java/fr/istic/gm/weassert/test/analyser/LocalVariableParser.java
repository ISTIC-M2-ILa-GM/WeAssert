package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.model.LocalVariableParsed;

import java.util.List;

public interface LocalVariableParser {

    Class getClazz();

    List<LocalVariableParsed> parse();

}
