package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.istic.gm.weassert.test.utils.ClassResolverUtil.mapClassToClassPath;

@Getter
public class SourceCodeWriter implements CodeWriter {
    public static final String CLASSNAME_REGEX = "^(private |public |protected |)(static |)class (\\w+)( \\{|)$";

    public static final String METHODNAME_REGEX = "^.*(@Test)(\\r|\\n)*\\s*(private |public |protected |)(static |)(void |)(\\w+)\\s*\\(\\)\\s+\\{$";

    private File sourceFile;

    private String sourceCode;

    private String className;

    public SourceCodeWriter(String classPath) {
        this.sourceFile = new File(classPath);

        if (!sourceFile.exists()) {
            throw new WeAssertException(String.format("SourceCodeWriter: no such source file at: '%s'", classPath));
        }

        try {
            this.sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));
        } catch (IOException e) {
            throw new WeAssertException("SourceCodeWriter: could not read source code !");
        }

        findClassName();
    }

    private void findClassName() {
        Pattern pattern = Pattern.compile(CLASSNAME_REGEX, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(this.sourceCode);

        if (matcher.find()) {
            if (matcher.group(3) != null) {
                this.className = matcher.group(3);
            } else {
                throw new WeAssertException(getClassName() + ": could not find class name!");
            }
        } else {
            throw new WeAssertException(getClassName() + ": given file is not a Java class source file!");
        }
    }

    public void insertOne(String methodName, String desc, String code) {
        Pattern pattern = Pattern.compile(METHODNAME_REGEX, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(this.sourceCode);

        int firstCurlyBraceIndex = 0;
        while (matcher.find()) {
            if (matcher.group(6).equals(methodName)) {
                firstCurlyBraceIndex = matcher.end();
                break;
            }
        }

        if (firstCurlyBraceIndex == 0) {
            throw new WeAssertException(String.format("%s: could not find method named \"%s\" !", getClassName(), methodName));
        }

        int lastCurlyBraceIndex = findLastCurlyBraceIndex(firstCurlyBraceIndex);

        StringBuilder str = new StringBuilder(this.sourceCode);
        str.insert(lastCurlyBraceIndex, code + "\n");

        // CODE INSERTION HERE
        this.sourceCode = str.toString();
    }

    private int findLastCurlyBraceIndex(int firstCurlyIndex) {
        int curlyBracesCounter = 1;
        int lastCurlyBrace = firstCurlyIndex;

        for (;lastCurlyBrace < this.sourceCode.length(); lastCurlyBrace++) {
            if (this.sourceCode.charAt(lastCurlyBrace) == '{') {
                curlyBracesCounter++;
            } else if (this.sourceCode.charAt(lastCurlyBrace) == '}') {
                curlyBracesCounter--;
            }

            if (curlyBracesCounter == 0) {
                break;
            }
        }

        if(lastCurlyBrace == 0) {
            throw new WeAssertException(getClassName() + ": parse error, could not find end of method!");
        }

        return lastCurlyBrace;
    }

    @Override
    public void insertMany(String methodName, String desc, List<String> codes) {
        codes.forEach(src -> this.insertOne(methodName, desc, src));
    }

    @Override
    public void writeAndCloseFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(this.sourceFile);
            writer.write(this.sourceCode);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
