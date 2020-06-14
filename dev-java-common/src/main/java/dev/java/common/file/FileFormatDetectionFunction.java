package dev.java.common.file;

@FunctionalInterface
public interface FileFormatDetectionFunction<F> {

    F detect(String line) throws Exception;
}

