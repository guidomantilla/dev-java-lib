package dev.java.common.file;

@FunctionalInterface
public interface FileLineValidatorFunction<F> {

    boolean validateLine(F format, String line) throws Exception;
}

