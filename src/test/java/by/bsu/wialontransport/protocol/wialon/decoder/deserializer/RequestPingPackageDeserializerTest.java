package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class RequestPingPackageDeserializerTest {
    private final RequestPingPackageDeserializer deserializer;

    public RequestPingPackageDeserializerTest() {
        this.deserializer = new RequestPingPackageDeserializer();
    }

    @Test
    public void requestPingPackageShouldBeDeserialized() {
        final String givenSource = "#P#";

        final Package actual = this.deserializer.deserialize(givenSource);
        assertNotNull(actual);
    }
}
