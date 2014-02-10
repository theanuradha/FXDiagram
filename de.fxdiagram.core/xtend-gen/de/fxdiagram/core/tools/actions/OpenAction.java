package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.OpenBehavior;
import de.fxdiagram.core.tools.actions.DiagramAction;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class OpenAction implements DiagramAction {
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    ObservableList<XNode> _nodes = _diagram.getNodes();
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
      public Boolean apply(final XNode it) {
        return Boolean.valueOf(it.getSelected());
      }
    };
    final Iterable<XNode> selectedNodes = IterableExtensions.<XNode>filter(_nodes, _function);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      XNode _head = IterableExtensions.<XNode>head(selectedNodes);
      OpenBehavior _behavior = _head.<OpenBehavior>getBehavior(OpenBehavior.class);
      if (_behavior!=null) {
        _behavior.open();
      }
    }
  }
}
