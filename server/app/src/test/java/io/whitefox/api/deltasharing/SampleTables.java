package io.whitefox.api.deltasharing;

import static io.whitefox.DeltaTestUtils.*;

import io.whitefox.S3TestConfig;
import io.whitefox.api.deltasharing.model.FileObjectFileWithoutPresignedUrl;
import io.whitefox.api.deltasharing.model.FileObjectWithoutPresignedUrl;
import io.whitefox.api.deltasharing.model.v1.generated.*;
import io.whitefox.core.InternalTable;
import io.whitefox.core.Principal;
import io.whitefox.core.SharedTable;
import io.whitefox.persistence.StorageManager;
import io.whitefox.persistence.memory.InMemoryStorageManager;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SampleTables {

  private static final Principal testPrincipal = new Principal("Mr. Fox");

  public static InternalTable s3DeltaTable1(S3TestConfig s3TestConfig) {
    return s3DeltaTable("delta-table", s3TestConfig);
  }

  public static InternalTable s3DeltaTableWithHistory1(S3TestConfig s3TestConfig) {
    return s3DeltaTable("delta-table-with-history", s3TestConfig);
  }

  public static final InternalTable deltaTable1 = deltaTable("delta-table");

  public static final String deltaTable1Path = deltaTableUri("delta-table");

  public static final String deltaTableWithHistory1Path = deltaTableUri("delta-table-with-history");

  public static final InternalTable deltaTableWithHistory1 = deltaTable("delta-table-with-history");

  public static StorageManager createStorageManager() {
    return new InMemoryStorageManager(List.of(new io.whitefox.core.Share(
        "name",
        "key",
        Map.of(
            "default",
            new io.whitefox.core.Schema(
                "default",
                List.of(
                    new SharedTable("table1", "default", "name", deltaTable1),
                    new SharedTable(
                        "table-with-history", "default", "name", deltaTableWithHistory1)),
                "name")),
        testPrincipal,
        0L)));
  }

  public static final MetadataObject deltaTable1Metadata = new MetadataObject()
      .metadata(new MetadataObjectMetadata()
          .id("56d48189-cdbc-44f2-9b0e-2bded4c79ed7")
          .name("table1")
          .format(new FormatObject().provider("parquet"))
          .schemaString(
              "{\"type\":\"struct\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"nullable\":true,\"metadata\":{}}]}")
          .partitionColumns(List.of())
          .version(0L)
          ._configuration(Map.of()));

  public static final MetadataObject s3DeltaTable1Metadata = new MetadataObject()
      .metadata(new MetadataObjectMetadata()
          .id("ed2297c4-8bb8-4c74-963d-8fed6bebfd8b")
          .name("s3Table1")
          .format(new FormatObject().provider("parquet"))
          .schemaString(
              "{\"type\":\"struct\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"nullable\":true,\"metadata\":{}}]}")
          .partitionColumns(List.of())
          .version(0L)
          ._configuration(Map.of()));
  public static final MetadataObject deltaTableWithHistory1Metadata = new MetadataObject()
      .metadata(new MetadataObjectMetadata()
          .id("56d48189-cdbc-44f2-9b0e-2bded4c79ed7")
          .name("table-with-history")
          .format(new FormatObject().provider("parquet"))
          .schemaString(
              "{\"type\":\"struct\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"nullable\":true,\"metadata\":{}}]}")
          .partitionColumns(List.of())
          .version(0L)
          ._configuration(Map.of()));
  public static final ProtocolObject deltaTable1Protocol =
      new ProtocolObject().protocol(new ProtocolObjectProtocol().minReaderVersion(1));

  public static final ProtocolObject s3DeltaTable1Protocol =
      new ProtocolObject().protocol(new ProtocolObjectProtocol().minReaderVersion(1));

  public static final Set<FileObject> deltaTable1Files = Set.of(
      new FileObject()
          ._file(new FileObjectFile()
              .url(deltaTable1Path
                  + "part-00003-049d1c60-7ad6-45a3-af3f-65ffcabcc974-c000.snappy.parquet")
              .id(deltaTable1Path
                  + "part-00003-049d1c60-7ad6-45a3-af3f-65ffcabcc974-c000.snappy.parquet")
              .partitionValues(Map.of())
              .size(478L)
              .stats(
                  "{\"numRecords\":1,\"minValues\":{\"id\":1},\"maxValues\":{\"id\":1},\"nullCount\":{\"id\":0}}")
              .version(0L)
              .timestamp(1695976443161L)
              .expirationTimestamp(9223372036854775807L)),
      new FileObject()
          ._file(new FileObjectFile()
              .url(deltaTable1Path
                  + "part-00001-a67388a6-e20e-426e-a872-351c390779a5-c000.snappy.parquet")
              .id(deltaTable1Path
                  + "part-00001-a67388a6-e20e-426e-a872-351c390779a5-c000.snappy.parquet")
              .partitionValues(Map.of())
              .size(478L)
              .stats(
                  "{\"numRecords\":1,\"minValues\":{\"id\":0},\"maxValues\":{\"id\":0},\"nullCount\":{\"id\":0}}")
              .version(0L)
              .timestamp(1695976443161L)
              .expirationTimestamp(9223372036854775807L)),
      new FileObject()
          ._file(new FileObjectFile()
              .url(deltaTable1Path
                  + "part-00007-3e861bbf-fe8b-44d0-bac7-712b8cf4608c-c000.snappy.parquet")
              .id(deltaTable1Path
                  + "part-00007-3e861bbf-fe8b-44d0-bac7-712b8cf4608c-c000.snappy.parquet")
              .partitionValues(Map.of())
              .size(478L)
              .stats(
                  "{\"numRecords\":1,\"minValues\":{\"id\":3},\"maxValues\":{\"id\":3},\"nullCount\":{\"id\":0}}")
              .version(0L)
              .timestamp(1695976443161L)
              .expirationTimestamp(9223372036854775807L)),
      new FileObject()
          ._file(new FileObjectFile()
              .url(deltaTable1Path
                  + "part-00005-e7b9aad4-adf6-42ad-a17c-fbc93689b721-c000.snappy.parquet")
              .id(deltaTable1Path
                  + "part-00005-e7b9aad4-adf6-42ad-a17c-fbc93689b721-c000.snappy.parquet")
              .partitionValues(Map.of())
              .size(478L)
              .stats(
                  "{\"numRecords\":1,\"minValues\":{\"id\":2},\"maxValues\":{\"id\":2},\"nullCount\":{\"id\":0}}")
              .version(0L)
              .timestamp(1695976443161L)
              .expirationTimestamp(9223372036854775807L)),
      new FileObject()
          ._file(new FileObjectFile()
              .url(deltaTable1Path
                  + "part-00009-90280af8-7b24-4519-9e49-82db78a1651b-c000.snappy.parquet")
              .id(deltaTable1Path
                  + "part-00009-90280af8-7b24-4519-9e49-82db78a1651b-c000.snappy.parquet")
              .partitionValues(Map.of())
              .size(478L)
              .stats(
                  "{\"numRecords\":1,\"minValues\":{\"id\":4},\"maxValues\":{\"id\":4},\"nullCount\":{\"id\":0}}")
              .version(0L)
              .timestamp(1695976443161L)
              .expirationTimestamp(9223372036854775807L)));

  public static final Set<FileObjectWithoutPresignedUrl> s3DeltaTable1FilesWithoutPresignedUrl =
      Set.of(
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":3},\"maxValues\":{\"id\":3},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1697796692836L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":0},\"maxValues\":{\"id\":0},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1697796692836L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":1},\"maxValues\":{\"id\":1},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1697796692836L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":4},\"maxValues\":{\"id\":4},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1697796692836L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":2},\"maxValues\":{\"id\":2},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1697796692836L)));

  public static final Set<FileObjectWithoutPresignedUrl> deltaTable1FilesWithoutPresignedUrl =
      Set.of(
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":3},\"maxValues\":{\"id\":3},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1695976443161L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":0},\"maxValues\":{\"id\":0},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1695976443161L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":1},\"maxValues\":{\"id\":1},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1695976443161L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":4},\"maxValues\":{\"id\":4},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1695976443161L)),
          new FileObjectWithoutPresignedUrl()
              ._file(new FileObjectFileWithoutPresignedUrl()
                  .partitionValues(Map.of())
                  .size(478L)
                  .stats(
                      "{\"numRecords\":1,\"minValues\":{\"id\":2},\"maxValues\":{\"id\":2},\"nullCount\":{\"id\":0}}")
                  .version(0L)
                  .timestamp(1695976443161L)));
}