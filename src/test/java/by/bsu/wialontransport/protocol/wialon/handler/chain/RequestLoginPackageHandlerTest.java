package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.core.service.authorization.AuthorizationTrackerService;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.RequestPingPackage;

import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class RequestLoginPackageHandlerTest {

    @Mock
    private RequestPingPackageHandler mockedNextHandler;

    @Mock
    private AuthorizationTrackerService mockedAuthorizationTrackerService;

    private RequestLoginPackageHandler handler;

    @Captor
    private ArgumentCaptor<RequestLoginPackage> requestLoginPackageArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Before
    public void initializeHandler() {
        this.handler = new RequestLoginPackageHandler(this.mockedNextHandler, this.mockedAuthorizationTrackerService);
    }

    @Test
    public void packageShouldBeHandledIndependently() {
        final Package givenPackage = new RequestLoginPackage("11111222223333344444", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenPackage, givenContext);

        verify(this.mockedAuthorizationTrackerService, times(1))
                .authorize(this.requestLoginPackageArgumentCaptor.capture(), this.contextArgumentCaptor.capture());

        assertSame(givenPackage, this.requestLoginPackageArgumentCaptor.getValue());
        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeHandledIndependentlyBecauseOfNotSuitableType() {
        final Package givenPackage = new RequestPingPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenPackage, givenContext);
    }
}
