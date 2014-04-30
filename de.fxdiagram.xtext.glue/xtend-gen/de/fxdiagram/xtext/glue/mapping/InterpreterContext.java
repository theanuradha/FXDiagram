package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import java.util.List;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class InterpreterContext {
  private XDiagram diagram;
  
  private List<XNode> addedNodes = CollectionLiterals.<XNode>newArrayList();
  
  private List<XConnection> addedConnections = CollectionLiterals.<XConnection>newArrayList();
  
  public XDiagram setDiagram(final XDiagram diagram) {
    return this.diagram = diagram;
  }
  
  public boolean addNode(final XNode node) {
    return this.addedNodes.add(node);
  }
  
  public boolean addConnection(final XConnection connection) {
    return this.addedConnections.add(connection);
  }
  
  public <T extends Object> XConnection getConnection(final DomainObjectDescriptor descriptor) {
    ObservableList<XConnection> _connections = this.diagram.getConnections();
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(this.addedConnections, _connections);
    final Function1<XConnection,Boolean> _function = new Function1<XConnection,Boolean>() {
      public Boolean apply(final XConnection it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
      }
    };
    return IterableExtensions.<XConnection>findFirst(_plus, _function);
  }
  
  public <T extends Object> XNode getNode(final DomainObjectDescriptor descriptor) {
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    Iterable<XNode> _plus = Iterables.<XNode>concat(this.addedNodes, _nodes);
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
      public Boolean apply(final XNode it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
      }
    };
    return IterableExtensions.<XNode>findFirst(_plus, _function);
  }
  
  public boolean needsLayout() {
    int _size = this.addedNodes.size();
    int _size_1 = this.addedConnections.size();
    int _plus = (_size + _size_1);
    return (_plus > 1);
  }
  
  public AddRemoveCommand getCommand() {
    Iterable<XShape> _plus = Iterables.<XShape>concat(this.addedNodes, this.addedConnections);
    return AddRemoveCommand.newAddCommand(this.diagram, ((XShape[])Conversions.unwrapArray(_plus, XShape.class)));
  }
}
