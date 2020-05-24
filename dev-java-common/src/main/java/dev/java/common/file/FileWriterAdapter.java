/*
 *
 */
package dev.java.common.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * The Class FileWriterAdapter.
 */
public class FileWriterAdapter {

	/** The buffered writer. */
	private BufferedWriter bufferedWriter;

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new file writer adapter.
	 *
	 * @param file the file
	 * @throws Exception the exception
	 */
	public FileWriterAdapter(File file) throws Exception {

		this.file = file;

		if (!file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 * Open.
	 *
	 * @throws Exception the exception
	 */
	public void open() throws Exception {

		FileWriter fileWriter = new FileWriter(file);
		bufferedWriter = new BufferedWriter(fileWriter);
	}

	/**
	 * Close.
	 *
	 * @throws Exception the exception
	 */
	public void close() throws Exception {

		bufferedWriter.close();
	}

	/**
	 * Write.
	 *
	 * @param line the line
	 * @throws Exception the exception
	 */
	public void write(String line) throws Exception {

		bufferedWriter.write(line);

	}
}
