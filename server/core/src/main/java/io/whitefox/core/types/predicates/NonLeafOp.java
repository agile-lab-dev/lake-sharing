package io.whitefox.core.types.predicates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.stream.Collectors;

// Represents a non-leaf operation.
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "op"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EqualOp.class, name = "equal"),
        @JsonSubTypes.Type(value = NotOp.class, name = "not"),
        @JsonSubTypes.Type(value = OrOp.class, name = "or"),
        @JsonSubTypes.Type(value = IsNullOp.class, name = "null"),
        @JsonSubTypes.Type(value = AndOp.class, name = "and"),
        @JsonSubTypes.Type(value = LessThanOp.class, name = "lessThan"),
        @JsonSubTypes.Type(value = LessThanOrEqualOp.class, name = "lessThanOrEqual"),
        @JsonSubTypes.Type(value = GreaterThanOp.class, name = "greaterThan"),
        @JsonSubTypes.Type(value = GreaterThanOrEqualOp.class, name = "greaterThanOrEqualOp")
})
public abstract class NonLeafOp implements BaseOp {

    @JsonProperty("children")
    List<BaseOp> children;

    public List<BaseOp> getAllChildren() {
      // TODO flat map every child
        return List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList());
    }
}

class IsNullOp extends NonLeafOp implements UnaryOp {

  @JsonProperty("children")
  List<LeafOp> children;

  public IsNullOp(List<LeafOp> children) {
    this.children = children;
  }

  public IsNullOp() {
    super();
  }

  @Override
    public void validate() {
        validateChildren(List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList()));
    }

  @Override
  public Object eval(EvalContext ctx) {
    return ((LeafOp) children.get(0)).isNull(ctx);
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "equal")
class EqualOp extends NonLeafOp implements BinaryOp {
  @JsonProperty("children")
  List<LeafOp> children;

  public EqualOp() {
      super();
  }

  public EqualOp(List<LeafOp> children) {
    this.children = children;
  }

  @Override
  public void validate() {
    validateChildren(List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList()));
  }

  @Override
  public Object eval(EvalContext ctx) {
    return EvalHelper.equal(children, ctx);
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "lessThan")
class LessThanOp extends NonLeafOp implements BinaryOp {
  @JsonProperty("children")
  List<LeafOp> children;

  public LessThanOp(List<LeafOp> children) {
    this.children = children;
  }

    public LessThanOp() {
      super();
    }

    @Override
  public void validate() {
    validateChildren(List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList()));
  }

  @Override
  public Object eval(EvalContext ctx) {
    return EvalHelper.lessThan(children, ctx);
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "lessThanOrEqual")
class LessThanOrEqualOp extends NonLeafOp implements BinaryOp {

  @JsonProperty("children")
  List<LeafOp> children;

  public LessThanOrEqualOp(List<LeafOp> children) {
    this.children = children;
  }

    public LessThanOrEqualOp() {
      super();
    }

    @Override
  public void validate() {
    validateChildren(List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList()));
  }

  @Override
  public Object eval(EvalContext ctx) {
    return EvalHelper.lessThan(children, ctx) || EvalHelper.equal(children, ctx);
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "greaterThan")
class GreaterThanOp extends NonLeafOp implements BinaryOp {

  @JsonProperty("children")
  List<LeafOp> children;

    public GreaterThanOp() {
        super();
    }

    @Override
  public void validate() {
    validateChildren(List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList()));
  }

  @Override
  public Object eval(EvalContext ctx) {
    return !EvalHelper.lessThan(children, ctx) && !EvalHelper.equal(children, ctx);
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "greaterThanOrEqual")
class GreaterThanOrEqualOp extends NonLeafOp implements BinaryOp {

  @JsonProperty("children")
  List<LeafOp> children;

    public GreaterThanOrEqualOp() {
        super();
    }

    @Override
  public void validate() {
    validateChildren(List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList()));
  }

  @Override
  public Object eval(EvalContext ctx) {
    return !EvalHelper.lessThan(children, ctx);
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "and")
class AndOp extends NonLeafOp implements BinaryOp {

  public AndOp(List<BaseOp> children) {
    this.children = children;
  }

    public AndOp() {
        super();
    }

    @Override
  public void validate() {
    validateChildren(children);
  }

  @Override
  public Object eval(EvalContext ctx) {
    // short-circuits, so not all exceptions will be thrown
    return children.stream().allMatch(c -> c.evalExpectBoolean(ctx));
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "or")
class OrOp extends NonLeafOp implements BinaryOp {

  public OrOp(List<BaseOp> children) {
    this.children = children;
  }

    public OrOp() {
        super();
    }

    @Override
  public void validate() {
    validateChildren(children);
  }

  @Override
  public Object eval(EvalContext ctx) {
    return children.stream().anyMatch(c -> c.evalExpectBoolean(ctx));
  }
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "not")
class NotOp extends NonLeafOp implements UnaryOp {

  @JsonProperty("children")
  List<LeafOp> children;

  public NotOp(List<LeafOp> children) {
    this.children = children;
  }

    public NotOp() {
        super();
    }

    @Override
  public void validate() {
    validateChildren(List.copyOf(children).stream().map(c -> (BaseOp) c).collect(Collectors.toList()));
  }

  @Override
  public Object eval(EvalContext ctx) {
    return !children.get(0).evalExpectBoolean(ctx);
  }
}