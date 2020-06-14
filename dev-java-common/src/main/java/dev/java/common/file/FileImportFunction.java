package dev.java.common.file;

import java.util.List;

@FunctionalInterface
public interface FileImportFunction<F> {

    long execute(F format, List<String> lineBatchHolder) throws Exception;
}
