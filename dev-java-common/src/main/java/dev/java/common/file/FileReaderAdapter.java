/*
 *
 */
package dev.java.common.file;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;

/**
 * The Class FileReaderAdapter.
 */
public class FileReaderAdapter implements Iterable<String> {

	/**
	 * The buffered reader.
	 */
	private BufferedReader bufferedReader;

	/**
	 * The file iterator.
	 */
	private final FileIterator fileIterator;

	/**
	 * The file.
	 */
	private final File file;

	private final byte[] content;

	/**
	 * Instantiates a new file reader adapter.
	 *
	 * @param file the file
	 */
	public FileReaderAdapter(File file) {

		this.content = null;
		this.file = file;
		this.fileIterator = new FileIterator();
	}

	public FileReaderAdapter(byte[] content) {
		this.file = null;
		this.fileIterator = new FileIterator();
		this.content = content;
	}

	/**
	 * Open.
	 *
	 * @throws Exception the exception
	 */
	public void open() throws Exception {

		if (!Objects.isNull(file)) {
			bufferedReader = new BufferedReader(new FileReader(file));
		}

		if (!Objects.isNull(content)) {
			bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content)));
		}
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
				Reader reader = null;
				if (!Objects.isNull(file)) {
					reader = new FileReader(file);
				}

				if (!Objects.isNull(content)) {
					reader = new InputStreamReader(new ByteArrayInputStream(content));
				}

				LineNumberReader lineNumberReader = new LineNumberReader(reader);

				while ((lineNumberReader.readLine()) != null) {
				}

				cont = lineNumberReader.getLineNumber();
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

		/**
		 * The current line.
		 */
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
