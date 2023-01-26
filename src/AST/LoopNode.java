package AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Parser.FunctionDefiniton;


public class LoopNode extends ExpressionNode {
  private BinOperationNode varDeclaration;
  private CompareNode compare;
  private BinOperationNode increment;
  private List<ExpressionNode> body = new ArrayList<>();
  public LoopNode(BinOperationNode varDeclaration, CompareNode compare, BinOperationNode increment,List<ExpressionNode> body){
    this.varDeclaration = varDeclaration;
    this.compare = compare;
    this.increment = increment;
    this.body = body;
  }

  @Override
  public Integer applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions) {
    varDeclaration.applyNode(scope, fnDefinitions);
    while(compare.applyNode(scope, fnDefinitions) == 1){
      VariableNode variable = (VariableNode) varDeclaration.getLeftNode();
      for (ExpressionNode node : body) node.applyNode(scope, fnDefinitions);
      scope.put(variable.getVariable().getText(), increment.applyNode(scope, fnDefinitions));
    }
    return 0;
  };
}
