/*
 *
 */
package dev.java.common.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * The Class JAXBMarshaller.
 */
public class JAXBMarshaller {

	/**
	 * Marshal.
	 *
	 * @param <T>    the generic type
	 * @param object the object
	 * @return the string
	 * @throws JAXBException the JAXB exception
	 */
	public <T> String marshal(T object) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(object.getClass());

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(object, stringWriter);

		return stringWriter.toString();
	}

	/**
	 * Unmarshal.
	 *
	 * @param <T>    the generic type
	 * @param clazz  the clazz
	 * @param string the string
	 * @return the t
	 * @throws JAXBException the JAXB exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(Class<T> clazz, String string) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(clazz);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		T object = (T) unmarshaller.unmarshal(new StringReader(string));

		return object;
	}
}
