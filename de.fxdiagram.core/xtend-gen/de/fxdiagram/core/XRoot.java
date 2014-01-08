package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.css.JavaToCss;
import de.fxdiagram.core.extensions.AccumulativeTransform2D;
import de.fxdiagram.core.tools.CompositeTool;
import de.fxdiagram.core.tools.DiagramGestureTool;
import de.fxdiagram.core.tools.MenuTool;
import de.fxdiagram.core.tools.SelectionTool;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class XRoot extends Parent implements XActivatable {
  private HeadsUpDisplay headsUpDisplay = new Function0<HeadsUpDisplay>() {
    public HeadsUpDisplay apply() {
      HeadsUpDisplay _headsUpDisplay = new HeadsUpDisplay();
      return _headsUpDisplay;
    }
  }.apply();
  
  private Pane diagramCanvas = new Function0<Pane>() {
    public Pane apply() {
      Pane _pane = new Pane();
      return _pane;
    }
  }.apply();
  
  private List<XDiagramTool> tools = CollectionLiterals.<XDiagramTool>newArrayList();
  
  private CompositeTool defaultTool;
  
  private XDiagramTool currentTool;
  
  public XRoot() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.diagramCanvas);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.headsUpDisplay);
    CompositeTool _compositeTool = new CompositeTool();
    this.defaultTool = _compositeTool;
    SelectionTool _selectionTool = new SelectionTool(this);
    this.defaultTool.operator_add(_selectionTool);
    DiagramGestureTool _diagramGestureTool = new DiagramGestureTool(this);
    this.defaultTool.operator_add(_diagramGestureTool);
    MenuTool _menuTool = new MenuTool(this);
    this.defaultTool.operator_add(_menuTool);
    this.tools.add(this.defaultTool);
  }
  
  public void setDiagram(final XDiagram newDiagram) {
    XDiagram _diagram = this.getDiagram();
    boolean _notEquals = (!Objects.equal(_diagram, null));
    if (_notEquals) {
      ObservableList<Node> _children = this.diagramCanvas.getChildren();
      XDiagram _diagram_1 = this.getDiagram();
      _children.remove(_diagram_1);
    }
    this.diagramProperty.set(newDiagram);
    ObservableList<Node> _children_1 = this.diagramCanvas.getChildren();
    _children_1.add(newDiagram);
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      newDiagram.activate();
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("-fx-background-color: ");
    Paint _backgroundPaint = newDiagram.getBackgroundPaint();
    CharSequence _css = JavaToCss.toCss(_backgroundPaint);
    _builder.append(_css, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("-fx-text-fill: ");
    Paint _foregroundPaint = newDiagram.getForegroundPaint();
    CharSequence _css_1 = JavaToCss.toCss(_foregroundPaint);
    _builder.append(_css_1, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    this.diagramCanvas.setStyle(_builder.toString());
    ObservableList<Node> _children_2 = this.headsUpDisplay.getChildren();
    _children_2.clear();
    XDiagram _diagram_2 = this.getDiagram();
    ObservableMap<Node,Pos> _fixedButtons = _diagram_2.getFixedButtons();
    Set<Map.Entry<Node,Pos>> _entrySet = _fixedButtons.entrySet();
    final Procedure1<Map.Entry<Node,Pos>> _function = new Procedure1<Map.Entry<Node,Pos>>() {
      public void apply(final Map.Entry<Node,Pos> it) {
        Node _key = it.getKey();
        Pos _value = it.getValue();
        XRoot.this.headsUpDisplay.add(_key, _value);
      }
    };
    IterableExtensions.<Map.Entry<Node,Pos>>forEach(_entrySet, _function);
    newDiagram.centerDiagram(false);
  }
  
  public HeadsUpDisplay getHeadsUpDisplay() {
    return this.headsUpDisplay;
  }
  
  public Pane getDiagramCanvas() {
    return this.diagramCanvas;
  }
  
  public AccumulativeTransform2D getDiagramTransform() {
    XDiagram _diagram = this.getDiagram();
    AccumulativeTransform2D _canvasTransform = _diagram.getCanvasTransform();
    return _canvasTransform;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
  }
  
  public void doActivate() {
    XDiagram _diagram = this.getDiagram();
    if (_diagram!=null) {
      _diagram.activate();
    }
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
      public void apply(final Pane it) {
        DoubleProperty _prefWidthProperty = it.prefWidthProperty();
        Scene _scene = it.getScene();
        ReadOnlyDoubleProperty _widthProperty = _scene.widthProperty();
        _prefWidthProperty.bind(_widthProperty);
        DoubleProperty _prefHeightProperty = it.prefHeightProperty();
        Scene _scene_1 = it.getScene();
        ReadOnlyDoubleProperty _heightProperty = _scene_1.heightProperty();
        _prefHeightProperty.bind(_heightProperty);
      }
    };
    ObjectExtensions.<Pane>operator_doubleArrow(
      this.diagramCanvas, _function);
    this.setCurrentTool(this.defaultTool);
  }
  
  public void setCurrentTool(final XDiagramTool tool) {
    XDiagramTool previousTool = this.currentTool;
    boolean _notEquals = (!Objects.equal(previousTool, null));
    if (_notEquals) {
      boolean _deactivate = previousTool.deactivate();
      boolean _not = (!_deactivate);
      if (_not) {
        XRoot.LOG.severe("Could not deactivate active tool");
      }
    }
    this.currentTool = tool;
    boolean _notEquals_1 = (!Objects.equal(tool, null));
    if (_notEquals_1) {
      boolean _activate = tool.activate();
      boolean _not_1 = (!_activate);
      if (_not_1) {
        this.currentTool = previousTool;
        boolean _activate_1 = false;
        if (previousTool!=null) {
          _activate_1=previousTool.activate();
        }
        boolean _not_2 = (!_activate_1);
        if (_not_2) {
          XRoot.LOG.severe("Could not reactivate tool");
        }
      }
    }
  }
  
  public void restoreDefaultTool() {
    this.setCurrentTool(this.defaultTool);
  }
  
  public Iterable<XShape> getCurrentSelection() {
    XDiagram _diagram = this.getDiagram();
    Iterable<XShape> _allShapes = _diagram.getAllShapes();
    final Function1<XShape,Boolean> _function = new Function1<XShape,Boolean>() {
      public Boolean apply(final XShape it) {
        boolean _and = false;
        boolean _isSelectable = it.isSelectable();
        if (!_isSelectable) {
          _and = false;
        } else {
          boolean _selected = it.getSelected();
          _and = (_isSelectable && _selected);
        }
        return Boolean.valueOf(_and);
      }
    };
    Iterable<XShape> _filter = IterableExtensions.<XShape>filter(_allShapes, _function);
    return _filter;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XRoot");
    ;
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyObjectWrapper<XDiagram> diagramProperty = new ReadOnlyObjectWrapper<XDiagram>(this, "diagram");
  
  public XDiagram getDiagram() {
    return this.diagramProperty.get();
  }
  
  public ReadOnlyObjectProperty<XDiagram> diagramProperty() {
    return this.diagramProperty.getReadOnlyProperty();
  }
}
