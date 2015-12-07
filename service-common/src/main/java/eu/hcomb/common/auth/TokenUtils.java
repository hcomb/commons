package eu.hcomb.common.auth;

import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;

import com.google.common.base.Optional;
import com.google.common.net.HttpHeaders;

public class TokenUtils {

	public static Optional<String> getTokenFromParam(MultivaluedMap<String, String> params, String paramName) {
		final String param = params.getFirst(paramName);
		return Optional.of(param);
	}


	public static Optional<String> getTokenFromHeader(String header, String prefix) {
        if (header != null) {
            int space = header.indexOf(' ');
            if (space > 0) {
                final String method = header.substring(0, space);
                if (prefix.equalsIgnoreCase(method)) {
                    final String rawToken = header.substring(space + 1);
                    return Optional.of(rawToken);
                }
            }
        }

        return Optional.absent();
	}
	
	public static Optional<String> getTokenFromHeader(MultivaluedMap<String, String> headers, String prefix) {
        final String header = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return getTokenFromHeader(header, prefix);
    }

    public static Optional<String> getTokenFromCookie(final Map<String, Cookie> cookies, String cookieName) {

        if (cookieName != null && cookies.containsKey(cookieName)) {
            final Cookie tokenCookie = cookies.get(cookieName);
            final String rawToken = tokenCookie.getValue();
            return Optional.of(rawToken);
        }

        return Optional.absent();
    }

}
