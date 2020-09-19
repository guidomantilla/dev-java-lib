package dev.java.common.file;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileImporter<F> {

    private FileImportFunction fileImportFunction;
    private FileFormatDetectionFunction fileFormatDetectionFunction;
    private FileLineValidatorFunction fileLineValidatorFunction;

    private int batchSize;
    private int workersPoolSize;
    private int timeOutInMin;

    public FileImporter(
            FileFormatDetectionFunction fileFormatDetectionFunction,
            FileImportFunction fileImportFunction) {
        this(fileFormatDetectionFunction, null, fileImportFunction);
    }

    public FileImporter(FileFormatDetectionFunction fileFormatDetectionFunction,
                        FileLineValidatorFunction fileLineValidatorFunction,
                        FileImportFunction fileImportFunction) {
        this(1000, 10, 100, fileFormatDetectionFunction, fileLineValidatorFunction, fileImportFunction);
    }

    private FileImporter(int batchSize, int timeOutInMin, int workersPoolSize,
                         FileFormatDetectionFunction fileFormatDetectionFunction,
                         FileLineValidatorFunction fileLineValidatorFunction,
                         FileImportFunction fileImportFunction) {

        this.batchSize = batchSize;
        this.timeOutInMin = timeOutInMin;
        this.workersPoolSize = workersPoolSize;
        this.fileImportFunction = fileImportFunction;
        this.fileFormatDetectionFunction = fileFormatDetectionFunction;
        this.fileLineValidatorFunction = fileLineValidatorFunction;
    }

    public void importFile(byte[] fileBytes) throws Exception {
        importFile(new FileReaderAdapter(fileBytes));
    }

    public void importFile(String filePath) throws Exception {
        importFile(new FileReaderAdapter(new File(filePath)));
    }

    private void importFile(FileReaderAdapter fileReaderAdapter) throws Exception {

        fileReaderAdapter.open();

        Instant start = Instant.now();

        int lineCount = fileReaderAdapter.retrieveNumberOfLines();

        F format = detectFormat(fileReaderAdapter.iterator());
        importFile(format, fileReaderAdapter.iterator(), lineCount, batchSize);

        Instant end = Instant.now();

        long time = Duration.between(start, end).toMillis();

        fileReaderAdapter.close();
    }

    private F detectFormat(Iterator<String> iterator) throws Exception {
        String headerLine = "";
        while (iterator.hasNext()) {
            headerLine = iterator.next();
            break;
        }

        return (F) fileFormatDetectionFunction.detect(headerLine);
    }

    private void importFile(F format, Iterator<String> iterator, int lineCount, int batchSize) throws Exception {

        int batchLast = lineCount % batchSize;
        int batchCount = (lineCount / batchSize) + (batchLast == 0 ? 0 : 1);

        ExecutorService executor = Executors.newFixedThreadPool(workersPoolSize);

        Instant start = Instant.now();
        try {
            for (int i = 1; i <= batchCount; i++) {

                int batchNumber = i;
                List<String> lineBatchHolder = retrieveBatch(format, iterator, batchCount, batchNumber, batchSize, batchLast);
                executor.submit(() -> {
                    try {

                        fileImportFunction.execute(format, lineBatchHolder);

                    } catch (Exception e) {

                    }
                });
            }
        } catch (Exception e) {
            throw e;
        }

        executor.shutdown();
        executor.awaitTermination(timeOutInMin, TimeUnit.MINUTES);

        Instant end = Instant.now();

        long time = Duration.between(start, end).toMillis();

    }

    private List<String> retrieveBatch(F format, Iterator<String> iterator, int batchCount, int batchCounter,
                                       int batchSize, int batchLast) throws Exception {

        List<String> lineBatchHolder = null;
        if (batchCounter != batchCount) {
            lineBatchHolder = retrieveBatch(format, iterator, batchSize);
        } else {
            lineBatchHolder = retrieveBatch(format, iterator, batchLast);
        }
        return lineBatchHolder;
    }

    private List<String> retrieveBatch(F format, Iterator<String> iterator, int batchSize) throws Exception {

        List<String> lineBatchHolder = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize && iterator.hasNext(); i++) {

            String line = iterator.next();
            if (canAddLine(format, line)) {
                lineBatchHolder.add(line);
            }
        }

        return lineBatchHolder;
    }

    private boolean canAddLine(F format, String line) throws Exception {
        return Objects.isNull(fileLineValidatorFunction) || fileLineValidatorFunction.validateLine(format, line);
    }
}
