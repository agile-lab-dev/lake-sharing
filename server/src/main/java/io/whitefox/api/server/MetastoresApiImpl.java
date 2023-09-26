package io.whitefox.api.server;

import io.whitefox.api.model.CreateMetastore;
import io.whitefox.api.model.UpdateMetastore;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MetastoresApiImpl implements MetastoresApi {
  @Override
  public CompletionStage<Response> createMetastore(CreateMetastore createMetastore) {
    Response res = Response.ok().build();
    return CompletableFuture.completedFuture(res);
  }

  @Override
  public CompletionStage<Response> deleteMetastore(String name, String force) {
    Response res = Response.ok().build();
    return CompletableFuture.completedFuture(res);
  }

  @Override
  public CompletionStage<Response> describeMetastore(String name) {
    Response res = Response.ok().build();
    return CompletableFuture.completedFuture(res);
  }

  @Override
  public CompletionStage<Response> listMetastores() {
    Response res = Response.ok().build();
    return CompletableFuture.completedFuture(res);
  }

  @Override
  public CompletionStage<Response> updateMetastore(String name, UpdateMetastore updateMetastore) {
    Response res = Response.ok().build();
    return CompletableFuture.completedFuture(res);
  }

  @Override
  public CompletionStage<Response> validateMetastore(String name) {
    Response res = Response.ok().build();
    return CompletableFuture.completedFuture(res);
  }
}