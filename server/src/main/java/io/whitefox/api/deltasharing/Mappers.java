package io.whitefox.api.deltasharing;

import io.whitefox.core.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mappers {
  public static io.whitefox.api.deltasharing.model.generated.Share share2api(Share p) {
    return new io.whitefox.api.deltasharing.model.generated.Share().id(p.id()).name(p.name());
  }

  public static io.whitefox.api.deltasharing.model.generated.Schema schema2api(Schema schema) {
    return new io.whitefox.api.deltasharing.model.generated.Schema()
        .name(schema.name())
        .share(schema.share());
  }

  public static io.whitefox.api.deltasharing.model.generated.Table table2api(Table table) {
    return new io.whitefox.api.deltasharing.model.generated.Table()
        .name(table.name())
        .share(table.share())
        .schema(table.schema());
  }

  public static io.whitefox.api.model.Metastore metastore2api(Metastore metastore) {
    return new io.whitefox.api.model.Metastore()
        .name(metastore.name())
        .comment(metastore.comment().orElse(null))
        .owner(metastore.owner().name())
        .type(metastore.type().value)
        .properties(metastoreProperties2api(metastore.properties()))
        .validatedAt(metastore.validatedAt().orElse(null))
        .createdAt(metastore.createdAt())
        .createdBy(metastore.createdBy().name())
        .updatedAt(metastore.updatedAt())
        .updatedBy(metastore.updatedBy().name());
  }

  private static io.whitefox.api.model.MetastoreProperties metastoreProperties2api(
      MetastoreProperties properties) {
    if (properties instanceof MetastoreProperties.GlueMetastoreProperties) {
      var glueMetastoreProperties = (MetastoreProperties.GlueMetastoreProperties) properties;
      return new io.whitefox.api.model.MetastoreProperties()
          .catalogId(glueMetastoreProperties.catalogId())
          .credentials(simpleAwsCredentials2api(glueMetastoreProperties.credentials()));
    }
    throw new IllegalArgumentException("Unknown type of metastore properties: " + properties);
  }

  private static io.whitefox.api.model.SimpleAwsCredentials simpleAwsCredentials2api(
      AwsCredentials credentials) {
    if (credentials instanceof AwsCredentials.SimpleAwsCredentials) {
      var simpleAwsCredentials = (AwsCredentials.SimpleAwsCredentials) credentials;
      return new io.whitefox.api.model.SimpleAwsCredentials()
          .awsAccessKeyId(simpleAwsCredentials.awsAccessKeyId())
          .awsSecretAccessKey(simpleAwsCredentials.awsSecretAccessKey())
          .region(simpleAwsCredentials.region());
    }
    throw new IllegalArgumentException("Unknown type of aws credentials: " + credentials);
  }

  public static CreateMetastore api2createMetastore(
      io.whitefox.api.model.CreateMetastore createMetastore, Principal principal) {

    var res = new CreateMetastore(
        createMetastore.getName(),
        Optional.ofNullable(createMetastore.getComment()),
        apit2MetastoreType(createMetastore.getType()),
        api2CreateMetastoreProperties(createMetastore.getProperties(), createMetastore.getType()),
        principal,
        createMetastore.getSkipValidation());
    return res;
  }

  public static MetastoreProperties api2CreateMetastoreProperties(
      io.whitefox.api.model.MetastoreProperties createMetastore,
      io.whitefox.api.model.CreateMetastore.TypeEnum type) {
    switch (type) {
      case GLUE:
        return new MetastoreProperties.GlueMetastoreProperties(
            createMetastore.getCatalogId(), api2awsCredentials(createMetastore.getCredentials()));
      default:
        throw new IllegalArgumentException("Unknown metastore type " + type.value());
    }
  }

  public static AwsCredentials api2awsCredentials(
      io.whitefox.api.model.SimpleAwsCredentials credentials) {
    return new AwsCredentials.SimpleAwsCredentials(
        credentials.getAwsAccessKeyId(),
        credentials.getAwsSecretAccessKey(),
        credentials.getRegion());
  }

  public static MetastoreType apit2MetastoreType(
      io.whitefox.api.model.CreateMetastore.TypeEnum type) {
    switch (type) {
      case GLUE:
        return MetastoreType.GLUE;
      default:
        throw new IllegalArgumentException("Unknown metastore type " + type.value());
    }
  }

  public static <A, B> List<B> mapList(List<A> list, Function<A, B> f) {
    return list.stream().map(f).collect(Collectors.toList());
  }
}