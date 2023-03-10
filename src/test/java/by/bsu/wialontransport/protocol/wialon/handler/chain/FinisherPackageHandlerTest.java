package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public final class FinisherPackageHandlerTest {
    private final FinisherPackageHandler handler;

    public FinisherPackageHandlerTest() {
        this.handler = new FinisherPackageHandler();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void packageShouldNotBeHandledIndependently() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenPackage, givenContext);
    }
}
