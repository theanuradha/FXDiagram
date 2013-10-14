package de.fxdiagram.examples.ecore;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddESuperTypeRapidButtonBehavior extends AbstractBehavior<EClassNode> {
  private List<XRapidButton> buttons;
  
  private Map<Object,EClass> key2availableSuperType = new Function0<Map<Object,EClass>>() {
    public Map<Object,EClass> apply() {
      HashMap<Object,EClass> _newHashMap = CollectionLiterals.<Object, EClass>newHashMap();
      return _newHashMap;
    }
  }.apply();
  
  public AddESuperTypeRapidButtonBehavior(final EClassNode host) {
    super(host);
  }
  
  protected void doActivate() {
    EClassNode _host = this.getHost();
    EClass _eClass = _host.getEClass();
    EList<EClass> _eSuperTypes = _eClass.getESuperTypes();
    final Procedure1<EClass> _function = new Procedure1<EClass>() {
      public void apply(final EClass it) {
        String _key = AddESuperTypeRapidButtonBehavior.this.getKey(it);
        AddESuperTypeRapidButtonBehavior.this.key2availableSuperType.put(_key, it);
      }
    };
    IterableExtensions.<EClass>forEach(_eSuperTypes, _function);
    boolean _isEmpty = this.key2availableSuperType.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          AddESuperTypeRapidButtonBehavior.this.createChooser(button);
        }
      };
      final Procedure1<XRapidButton> addSuperTypeAction = _function_1;
      List<XRapidButton> _createButtons = this.createButtons(addSuperTypeAction);
      this.buttons = _createButtons;
      EClassNode _host_1 = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      Iterables.<XRapidButton>addAll(_buttons, this.buttons);
      EClassNode _host_2 = this.getHost();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_2);
      ObservableList<XConnection> _connections = _diagram_1.getConnections();
      final ListChangeListener<XConnection> _function_2 = new ListChangeListener<XConnection>() {
        public void onChanged(final Change<? extends XConnection> change) {
          boolean _next = change.next();
          boolean _while = _next;
          while (_while) {
            boolean _wasAdded = change.wasAdded();
            if (_wasAdded) {
              List<? extends XConnection> _addedSubList = change.getAddedSubList();
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  Object _key = it.getKey();
                  AddESuperTypeRapidButtonBehavior.this.key2availableSuperType.remove(_key);
                }
              };
              IterableExtensions.forEach(_addedSubList, _function);
            }
            boolean _next_1 = change.next();
            _while = _next_1;
          }
          boolean _isEmpty = AddESuperTypeRapidButtonBehavior.this.key2availableSuperType.isEmpty();
          if (_isEmpty) {
            EClassNode _host = AddESuperTypeRapidButtonBehavior.this.getHost();
            XDiagram _diagram = CoreExtensions.getDiagram(_host);
            ObservableList<XRapidButton> _buttons = _diagram.getButtons();
            Iterables.removeAll(_buttons, AddESuperTypeRapidButtonBehavior.this.buttons);
          }
        }
      };
      _connections.addListener(_function_2);
    }
  }
  
  protected String getKey(final EClass superType) {
    EClassNode _host = this.getHost();
    EClass _eClass = ((EClassNode) _host).getEClass();
    String _name = _eClass.getName();
    String _plus = (_name + " extends ");
    String _name_1 = superType.getName();
    String _plus_1 = (_plus + _name_1);
    return _plus_1;
  }
  
  protected List<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addSuperTypeAction) {
    EClassNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, _triangleButton, addSuperTypeAction);
    EClassNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, _triangleButton_1, addSuperTypeAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
  
  protected void createChooser(final XRapidButton button) {
    EClassNode _host = this.getHost();
    Pos _chooserPosition = button.getChooserPosition();
    CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
    final CoverFlowChooser chooser = _coverFlowChooser;
    Collection<EClass> _values = this.key2availableSuperType.values();
    final Procedure1<EClass> _function = new Procedure1<EClass>() {
      public void apply(final EClass superType) {
        EClassNode _eClassNode = new EClassNode(superType);
        chooser.addChoice(_eClassNode);
      }
    };
    IterableExtensions.<EClass>forEach(_values, _function);
    final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
      public XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo) {
        EClass _eClass = ((EClassNode) choice).getEClass();
        String _key = AddESuperTypeRapidButtonBehavior.this.getKey(_eClass);
        XConnection _xConnection = new XConnection(host, choice, _key);
        final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
          public void apply(final XConnection it) {
            ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
            XDiagram _diagram = CoreExtensions.getDiagram(host);
            ObjectProperty<Paint> _backgroundPaintProperty = _diagram.backgroundPaintProperty();
            TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, _strokeProperty, _backgroundPaintProperty, false);
            it.setTargetArrowHead(_triangleArrowHead);
          }
        };
        XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
        return _doubleArrow;
      }
    };
    chooser.setConnectionProvider(_function_1);
    EClassNode _host_1 = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host_1);
    _root.setCurrentTool(chooser);
  }
}