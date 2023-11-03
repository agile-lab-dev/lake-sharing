package io.whitefox.api.server;

import io.quarkus.runtime.util.ExceptionUtil;
import io.whitefox.api.deltasharing.model.v1.generated.CommonErrorResponse;
import io.whitefox.core.Principal;
import io.whitefox.core.services.DeltaSharedTable;
import io.whitefox.core.services.exceptions.AlreadyExists;
import io.whitefox.core.services.exceptions.NotFound;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ApiUtils extends DeltaHeaders {

  Function<Throwable, Response> exceptionToResponse = t -> {
    if (t instanceof IllegalArgumentException) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(new CommonErrorResponse()
              .errorCode("BAD REQUEST")
              .message(ExceptionUtil.generateStackTrace(t)))
          .build();
    } else if (t instanceof AlreadyExists) {
      return Response.status(Response.Status.CONFLICT)
          .entity(new CommonErrorResponse()
              .errorCode("CONFLICT")
              .message(ExceptionUtil.generateStackTrace(t)))
          .build();
    } else if (t instanceof NotFound) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(new CommonErrorResponse()
              .errorCode("NOT FOUND")
              .message(ExceptionUtil.generateStackTrace(t)))
          .build();
    } else {
      return Response.status(Response.Status.BAD_GATEWAY)
          .entity(new CommonErrorResponse()
              .errorCode("BAD GATEWAY")
              .message(ExceptionUtil.generateStackTrace(t)))
          .build();
    }
  };

  default Response wrapExceptions(Supplier<Response> f, Function<Throwable, Response> mapper) {
    try {
      return f.get();
    } catch (Throwable t) {
      return mapper.apply(t);
    }
  }

  default Response notFoundResponse() {
    return Response.status(Response.Status.NOT_FOUND)
        .entity(new CommonErrorResponse().errorCode("1").message("NOT FOUND"))
        .build();
  }

  default <T> Response optionalToNotFound(Optional<T> opt, Function<T, Response> fn) {
    return opt.map(fn).orElse(notFoundResponse());
  }

  default String getResponseFormatHeader(Map<String, String> deltaSharingCapabilities) {
    return String.format(
        "%s=%s",
        DeltaHeaders.DELTA_SHARING_RESPONSE_FORMAT, getResponseFormat(deltaSharingCapabilities));
  }

  default String getResponseFormat(Map<String, String> deltaSharingCapabilities) {
    return deltaSharingCapabilities.getOrDefault(
        DeltaHeaders.DELTA_SHARING_RESPONSE_FORMAT,
        DeltaSharedTable.DeltaShareTableFormat.RESPONSE_FORMAT_PARQUET);
  }

  default Principal getRequestPrincipal() {
    return new Principal("Mr. Fox");
  }

  default Principal resolvePrincipal(String s) {
    return new Principal(s);
  }
}