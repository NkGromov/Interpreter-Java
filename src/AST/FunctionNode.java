package AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Parser.FunctionDefiniton;

public class FunctionNode extends ExpressionNode  {

  private PrototypeNode proto;
  private List<ExpressionNode> body = new ArrayList<>();

  public FunctionNode(PrototypeNode proto, List<ExpressionNode> body) {
    this.proto = proto;
    this.body = body;
  }

  @Override
  public Integer applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions) {
    fnDefinitions.put(proto.getName(), new FunctionDefiniton(body, proto));
    return 0;
  }

  public List<ExpressionNode> getBody() {
    return body;
  }

  public PrototypeNode getProto() {
    return proto;
  }
}
