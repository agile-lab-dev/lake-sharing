package io.whitefox.core.services;

import static io.whitefox.DeltaTestUtils.deltaTable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.wildfly.common.Assert.assertTrue;

import io.whitefox.core.Protocol;
import io.whitefox.core.ReadTableRequest;
import io.whitefox.core.SharedTable;

import java.sql.Timestamp;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
public class DeltaSharedTableTest {

  @Test
  void getTableVersion() throws ExecutionException, InterruptedException {
    var PTable = new SharedTable("delta-table", "default", "share1", deltaTable("delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var version = DTable.getTableVersion(Optional.empty());
    assertEquals(Optional.of(0L), version);
  }

  @Test
  void getTableMetadata() {
    var PTable = new SharedTable("delta-table", "default", "share1", deltaTable("delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var metadata = DTable.getMetadata(Optional.empty());
    assertTrue(metadata.isPresent());
    assertEquals("56d48189-cdbc-44f2-9b0e-2bded4c79ed7", metadata.get().id());
  }

  @Test
  void getUnknownTableMetadata() {
    var unknownPTable = new SharedTable("notFound", "default", "share1", deltaTable("location1"));
    assertThrows(IllegalArgumentException.class, () -> DeltaSharedTable.of(unknownPTable));
  }

  @Test
  void getTableVersionNonExistingTable() throws ExecutionException, InterruptedException {
    var PTable =
        new SharedTable("delta-table", "default", "share1", deltaTable("delta-table-not-exists"));
    var exception = assertThrows(IllegalArgumentException.class, () -> DeltaSharedTable.of(PTable));
    assertTrue(exception.getMessage().startsWith("Cannot find a delta table at file"));
  }

  @Test
  void getTableVersionWithTimestamp() throws ExecutionException, InterruptedException {
    var PTable = new SharedTable("delta-table", "default", "share1", deltaTable("delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var version = DTable.getTableVersion(TestDateUtils.parseTimestamp("2023-09-30T10:15:30+01:00"));
    assertEquals(Optional.empty(), version);
  }

  @Test
  void getTableVersionWithFutureTimestamp() throws ExecutionException, InterruptedException {
    var PTable = new SharedTable("delta-table", "default", "share1", deltaTable("delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var version = DTable.getTableVersion(TestDateUtils.parseTimestamp("2024-10-20T10:15:30+01:00"));
    assertEquals(Optional.empty(), version);
  }

  @Test
  void getTableVersionWithMalformedTimestamp() throws ExecutionException, InterruptedException {
    var PTable = new SharedTable("delta-table", "default", "share1", deltaTable("delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    assertThrows(
        DateTimeParseException.class,
        () -> DTable.getTableVersion(Optional.of(Timestamp.valueOf("221rfewdsad10:15:30+01:00"))));
  }

  @Test
  void queryTableWithoutPredicate() {
    var PTable = new SharedTable(
        "partitioned-delta-table", "default", "share1", deltaTable("partitioned-delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var request =
        new ReadTableRequest.ReadTableCurrentVersion(List.of(), Optional.empty(), Optional.empty());
    var response = DTable.queryTable(request);
    assertEquals(response.protocol(), new Protocol(Optional.of(1)));
    assertEquals(response.other().size(), 9);
  }

  @Test
  void queryTableWithJsonPredicate() {
    var predicate = "{"
        + "      \"op\":\"equal\",\n"
        + "      \"children\":[\n"
        + "        {\"op\":\"column\",\"name\":\"date\",\"valueType\":\"date\"},\n"
        + "        {\"op\":\"literal\",\"value\":\"2021-08-15\",\"valueType\":\"date\"}\n"
        + "      ]\n"
        + "}";

    var PTable = new SharedTable(
        "partitioned-delta-table", "default", "share1", deltaTable("partitioned-delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var request = new ReadTableRequest.ReadTableCurrentVersion(
        List.of(), Optional.of(predicate), Optional.empty());
    var response = DTable.queryTable(request);
    assertEquals(response.other().size(), 4);
  }

  @Test
  void queryTableWithSqlPredicate() {
    var predicate = "date = '2021-08-15'";

    var PTable = new SharedTable(
        "partitioned-delta-table", "default", "share1", deltaTable("partitioned-delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var request = new ReadTableRequest.ReadTableCurrentVersion(
        List.of(predicate), Optional.empty(), Optional.empty());
    var response = DTable.queryTable(request);
    assertEquals(4, response.other().size());
  }

  @Test
  void queryTableWithNonPartitionSqlPredicate() {
    var predicate = "id < 5";
    var tableName = "partitioned-delta-table-with-multiple-columns";

    var PTable = new SharedTable(tableName, "default", "share1", deltaTable(tableName));
    var DTable = DeltaSharedTable.of(PTable);
    var request = new ReadTableRequest.ReadTableCurrentVersion(
        List.of(predicate), Optional.empty(), Optional.empty());
    var response = DTable.queryTable(request);
    assertEquals(1, response.other().size());
  }

  @Test
  void queryTableWithInvalidJsonPredicate() {
    var predicate = "{"
        + "      \"op\":\"equal\",\n"
        + "      \"children\":[\n"
        + "        {\"op\":\"column\",\"name\":\"dating\",\"valueType\":\"date\"},\n"
        + "        {\"op\":\"literal\",\"value\":\"2021-08-15\",\"valueType\":\"date\"}\n"
        + "      ]\n"
        + "}";

    var PTable = new SharedTable(
        "partitioned-delta-table", "default", "share1", deltaTable("partitioned-delta-table"));
    var DTable = DeltaSharedTable.of(PTable);
    var request = new ReadTableRequest.ReadTableCurrentVersion(
        List.of(), Optional.of(predicate), Optional.empty());
    var response = DTable.queryTable(request);
    assertEquals(response.other().size(), 9);
  }

  @Test
  void queryTableWithColumnRangePredicate() {
    var tableName = "partitioned-delta-table-with-multiple-columns";
    var predicate = "{"
        + "      \"op\":\"lessThan\",\n"
        + "      \"children\":[\n"
        + "        {\"op\":\"column\",\"name\":\"id\",\"valueType\":\"int\"},\n"
        + "        {\"op\":\"literal\",\"value\":\"4\",\"valueType\":\"int\"}\n"
        + "      ]\n"
        + "}";

    var PTable = new SharedTable(tableName, "default", "share1", deltaTable(tableName));
    var DTable = DeltaSharedTable.of(PTable);
    var request = new ReadTableRequest.ReadTableCurrentVersion(
        List.of(), Optional.of(predicate), Optional.empty());
    var response = DTable.queryTable(request);
    assertEquals(response.other().size(), 1);
  }
}
