package com.fabiankaraben.xmlapi;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlResponseApiTest {

    @Test
    void testXmlSerialization() throws Exception {
        User user = new User("1", "John Doe", "john.doe@example.com");
        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(user);

        // Validate by deserializing back to object
        User deserializedUser = xmlMapper.readValue(xml, User.class);
        
        assertEquals("1", deserializedUser.getId());
        assertEquals("John Doe", deserializedUser.getName());
        assertEquals("john.doe@example.com", deserializedUser.getEmail());
        
        // Also verify root element name implies structure
        assertTrue(xml.contains("<user>"));
    }
}
