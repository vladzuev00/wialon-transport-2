package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class RequestLoginPackageDeserializerTest {

    private final RequestLoginPackageDeserializer deserializer;

    public RequestLoginPackageDeserializerTest() {
        this.deserializer = new RequestLoginPackageDeserializer();
    }

    @Test
    public void requestLoginPackageShouldBeDeserialized() {
        final String givenSource = "#L#imei;password";

        final Package actual = this.deserializer.deserialize(givenSource);
        final RequestLoginPackage expected = new RequestLoginPackage("imei", "password");
        assertEquals(expected, actual);
    }
}
