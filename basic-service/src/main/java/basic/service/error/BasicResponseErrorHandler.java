package basic.service.error;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.List;

/**
 * Don't handle expected business error.
 */
public class BasicResponseErrorHandler extends DefaultResponseErrorHandler {

    private static final List<HttpStatus> unhandledStatus = ImmutableList.of(
            HttpStatus.UNAUTHORIZED
            , HttpStatus.FORBIDDEN
            , HttpStatus.NOT_FOUND);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = getHttpStatusCode(response);
        if (!unhandledStatus.contains(statusCode)) {
            super.handleError(response);
        }
    }
}
