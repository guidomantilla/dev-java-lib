/*
 *
 */
package dev.java.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;

/**
 * The Class FileReaderAdapter.
 */
public class FileReaderAdapter implements Iterable<String> {

	/** The buffered reader. */
	private BufferedReader bufferedReader;

	/** The file iterator. */
	private final FileIterator fileIterator;

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new file reader adapter.
	 *
	 * @param file the file
	 */
	public FileReaderAdapter(File file) {

		this.file = file;
		this.fileIterator = new FileIterator();
	}

	/**
	 * Open.
	 *
	 * @throws Exception the exception
	 */
	public void open() throws Exception {

		FileReader fileReader = new FileReader(file);
		bufferedReader = new BufferedReader(fileReader);
	}

	/**
	 * Close.
	 *
	 * @throws Exception the exception
	 */
	public void close() throws Exception {

		bufferedReader.close();
	}

	/**
	 * Retrieve number of lines.
	 *
	 * @return the int
	 * @throws Exception the exception
	 */
	public int retrieveNumberOfLines() throws Exception {

		int cont = 0;
		long init = System.currentTimeMillis();

		boolean flag = false;
		do {
			try {

				FileReader fileReader = new FileReader(file);
				LineNumberReader reader = new LineNumberReader(fileReader);

				while ((reader.readLine()) != null) {
				}

				cont = reader.getLineNumber();
				reader.close();
				flag = true;

			} catch (Exception e) {

				long end = System.currentTimeMillis();
				double time = ((end - init) / 1000D);

				if (time > 30) {
					throw e;
				}
			}

		} while (!flag);

		return cont;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {

		return fileIterator;
	}

	/**
	 * The Class FileIterator.
	 */
	private class FileIterator implements Iterator<String> {

		/** The current line. */
		private String currentLine;

		/**
		 * Checks for next.
		 *
		 * @return true, if successful
		 */
		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			try {
				currentLine = bufferedReader.readLine();

			} catch (IOException e) {

				currentLine = null;
			}

			return currentLine != null;
		}

		/**
		 * Next.
		 *
		 * @return the string
		 */
		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Iterator#next()
		 */
		@Override
		public String next() {

			return currentLine;
		}

		/**
		 * Removes the.
		 */
		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {

		}
	}
}
