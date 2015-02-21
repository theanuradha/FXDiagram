package de.fxdiagram.eclipse;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.SelectAndRevealCommand;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.services.ClassLoaderProvider;
import de.fxdiagram.core.tools.actions.CenterAction;
import de.fxdiagram.core.tools.actions.DeleteAction;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.DiagramActionRegistry;
import de.fxdiagram.core.tools.actions.ExportSvgAction;
import de.fxdiagram.core.tools.actions.FullScreenAction;
import de.fxdiagram.core.tools.actions.LayoutAction;
import de.fxdiagram.core.tools.actions.LoadAction;
import de.fxdiagram.core.tools.actions.NavigateNextAction;
import de.fxdiagram.core.tools.actions.NavigatePreviousAction;
import de.fxdiagram.core.tools.actions.RedoAction;
import de.fxdiagram.core.tools.actions.RevealAction;
import de.fxdiagram.core.tools.actions.SaveAction;
import de.fxdiagram.core.tools.actions.SelectAllAction;
import de.fxdiagram.core.tools.actions.UndoAction;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
import de.fxdiagram.eclipse.EditorListener;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.DiagramMappingCall;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.eclipse.mapping.InterpreterContext;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.eclipse.mapping.NodeMappingCall;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import de.fxdiagram.eclipse.mapping.XDiagramConfigInterpreter;
import de.fxdiagram.lib.actions.UndoRedoPlayerAction;
import de.fxdiagram.swtfx.SwtToFXGestureConverter;
import java.util.Collections;
import java.util.Set;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swt.FXCanvas;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.util.Duration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.IXtextModelListener;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Embeds an {@link FXCanvas} with an {@link XRoot} in an eclipse {@link ViewPart}.
 * 
 * Uses {@link AbstractMapping} API to map domain objects to diagram elements.
 */
@SuppressWarnings("all")
public class FXDiagramView extends ViewPart {
  private FXCanvas canvas;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private XRoot root;
  
  private SwtToFXGestureConverter gestureConverter;
  
  private Set<IEditorPart> contributingEditors = CollectionLiterals.<IEditorPart>newHashSet();
  
  private Set<IEditorPart> changedEditors = CollectionLiterals.<IEditorPart>newHashSet();
  
  private IPartListener2 listener;
  
  private final XDiagramConfigInterpreter configInterpreter = new XDiagramConfigInterpreter();
  
  @Override
  public void createPartControl(final Composite parent) {
    FXCanvas _fXCanvas = new FXCanvas(parent, SWT.NONE);
    this.canvas = _fXCanvas;
    SwtToFXGestureConverter _swtToFXGestureConverter = new SwtToFXGestureConverter(this.canvas);
    this.gestureConverter = _swtToFXGestureConverter;
    Scene _createFxScene = this.createFxScene();
    this.canvas.setScene(_createFxScene);
  }
  
  protected Scene createFxScene() {
    XRoot _xRoot = new XRoot();
    final Procedure1<XRoot> _function = new Procedure1<XRoot>() {
      @Override
      public void apply(final XRoot it) {
        XDiagram _xDiagram = new XDiagram();
        it.setRootDiagram(_xDiagram);
        ObservableList<DomainObjectProvider> _domainObjectProviders = it.getDomainObjectProviders();
        ClassLoaderProvider _classLoaderProvider = new ClassLoaderProvider();
        _domainObjectProviders.add(_classLoaderProvider);
        ObservableList<DomainObjectProvider> _domainObjectProviders_1 = it.getDomainObjectProviders();
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
        final Function1<XDiagramConfig, IMappedElementDescriptorProvider> _function = new Function1<XDiagramConfig, IMappedElementDescriptorProvider>() {
          @Override
          public IMappedElementDescriptorProvider apply(final XDiagramConfig it) {
            return it.getDomainObjectProvider();
          }
        };
        Iterable<IMappedElementDescriptorProvider> _map = IterableExtensions.map(_configurations, _function);
        Set<IMappedElementDescriptorProvider> _set = IterableExtensions.<IMappedElementDescriptorProvider>toSet(_map);
        Iterables.<DomainObjectProvider>addAll(_domainObjectProviders_1, _set);
        DiagramActionRegistry _diagramActionRegistry = it.getDiagramActionRegistry();
        CenterAction _centerAction = new CenterAction();
        DeleteAction _deleteAction = new DeleteAction();
        LayoutAction _layoutAction = new LayoutAction(LayoutType.DOT);
        ExportSvgAction _exportSvgAction = new ExportSvgAction();
        UndoAction _undoAction = new UndoAction();
        RedoAction _redoAction = new RedoAction();
        RevealAction _revealAction = new RevealAction();
        LoadAction _loadAction = new LoadAction();
        SaveAction _saveAction = new SaveAction();
        SelectAllAction _selectAllAction = new SelectAllAction();
        ZoomToFitAction _zoomToFitAction = new ZoomToFitAction();
        NavigatePreviousAction _navigatePreviousAction = new NavigatePreviousAction();
        NavigateNextAction _navigateNextAction = new NavigateNextAction();
        FullScreenAction _fullScreenAction = new FullScreenAction();
        UndoRedoPlayerAction _undoRedoPlayerAction = new UndoRedoPlayerAction();
        _diagramActionRegistry.operator_add(Collections.<DiagramAction>unmodifiableList(CollectionLiterals.<DiagramAction>newArrayList(_centerAction, _deleteAction, _layoutAction, _exportSvgAction, _undoAction, _redoAction, _revealAction, _loadAction, _saveAction, _selectAllAction, _zoomToFitAction, _navigatePreviousAction, _navigateNextAction, _fullScreenAction, _undoRedoPlayerAction)));
      }
    };
    XRoot _doubleArrow = ObjectExtensions.<XRoot>operator_doubleArrow(_xRoot, _function);
    Scene _scene = new Scene(
      this.root = _doubleArrow);
    final Procedure1<Scene> _function_1 = new Procedure1<Scene>() {
      @Override
      public void apply(final Scene it) {
        PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
        it.setCamera(_perspectiveCamera);
        FXDiagramView.this.root.activate();
      }
    };
    return ObjectExtensions.<Scene>operator_doubleArrow(_scene, _function_1);
  }
  
  @Override
  public void setFocus() {
    this.canvas.setFocus();
  }
  
  public void clear() {
    this.contributingEditors.clear();
    this.changedEditors.clear();
    XDiagram _xDiagram = new XDiagram();
    this.root.setDiagram(_xDiagram);
    CommandStack _commandStack = this.root.getCommandStack();
    _commandStack.clear();
  }
  
  public <T extends Object> void revealElement(final T element, final MappingCall<?, ? super T> mappingCall, final IEditorPart editor) {
    Scene _scene = this.canvas.getScene();
    double _width = _scene.getWidth();
    boolean _equals = (_width == 0);
    if (_equals) {
      Scene _scene_1 = this.canvas.getScene();
      ReadOnlyDoubleProperty _widthProperty = _scene_1.widthProperty();
      final ChangeListener<Number> _function = new ChangeListener<Number>() {
        @Override
        public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
          Scene _scene = FXDiagramView.this.canvas.getScene();
          ReadOnlyDoubleProperty _widthProperty = _scene.widthProperty();
          _widthProperty.removeListener(this);
          FXDiagramView.this.<T>revealElement(element, mappingCall, editor);
        }
      };
      _widthProperty.addListener(_function);
    } else {
      Scene _scene_2 = this.canvas.getScene();
      double _height = _scene_2.getHeight();
      boolean _equals_1 = (_height == 0);
      if (_equals_1) {
        Scene _scene_3 = this.canvas.getScene();
        ReadOnlyDoubleProperty _heightProperty = _scene_3.heightProperty();
        final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
          @Override
          public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
            Scene _scene = FXDiagramView.this.canvas.getScene();
            ReadOnlyDoubleProperty _heightProperty = _scene.heightProperty();
            _heightProperty.removeListener(this);
            FXDiagramView.this.<T>revealElement(element, mappingCall, editor);
          }
        };
        _heightProperty.addListener(_function_1);
      } else {
        this.<T>doRevealElement(element, mappingCall, editor);
      }
    }
  }
  
  protected <T extends Object> void doRevealElement(final T element, final MappingCall<?, ? super T> mappingCall, final IEditorPart editor) {
    final InterpreterContext interpreterContext = new InterpreterContext();
    if ((mappingCall instanceof DiagramMappingCall<?, ?>)) {
      if (editor!=null) {
        this.register(editor);
      }
      boolean _or = false;
      boolean _equals = Objects.equal(editor, null);
      if (_equals) {
        _or = true;
      } else {
        boolean _remove = this.changedEditors.remove(editor);
        _or = _remove;
      }
      if (_or) {
        interpreterContext.setIsNewDiagram(true);
        XDiagram _execute = this.configInterpreter.execute(((DiagramMappingCall<?, T>) mappingCall), element, interpreterContext);
        this.root.setDiagram(_execute);
      }
    } else {
      if ((mappingCall instanceof NodeMappingCall<?, ?>)) {
        if (editor!=null) {
          this.register(editor);
        }
        XDiagram _diagram = this.root.getDiagram();
        interpreterContext.setDiagram(_diagram);
        this.configInterpreter.execute(((NodeMappingCall<?, T>) mappingCall), element, interpreterContext, true);
      }
    }
    CommandStack _commandStack = this.root.getCommandStack();
    AddRemoveCommand _command = interpreterContext.getCommand();
    _commandStack.execute(_command);
    boolean _needsLayout = interpreterContext.needsLayout();
    if (_needsLayout) {
      CommandStack _commandStack_1 = this.root.getCommandStack();
      Layouter _layouter = new Layouter();
      XDiagram _diagram_1 = this.root.getDiagram();
      Duration _millis = DurationExtensions.millis(500);
      LazyCommand _createLayoutCommand = _layouter.createLayoutCommand(LayoutType.DOT, _diagram_1, _millis);
      _commandStack_1.execute(_createLayoutCommand);
    }
    final IMappedElementDescriptor<T> descriptor = this.<T, Object>createMappedDescriptor(element);
    CommandStack _commandStack_2 = this.root.getCommandStack();
    final Function1<XShape, Boolean> _function = new Function1<XShape, Boolean>() {
      @Override
      public Boolean apply(final XShape it) {
        boolean _switchResult = false;
        boolean _matched = false;
        if (!_matched) {
          if (it instanceof XNode) {
            _matched=true;
            DomainObjectDescriptor _domainObject = ((XNode)it).getDomainObject();
            _switchResult = Objects.equal(_domainObject, descriptor);
          }
        }
        if (!_matched) {
          if (it instanceof XConnection) {
            _matched=true;
            DomainObjectDescriptor _domainObject = ((XConnection)it).getDomainObject();
            _switchResult = Objects.equal(_domainObject, descriptor);
          }
        }
        if (!_matched) {
          _switchResult = false;
        }
        return Boolean.valueOf(_switchResult);
      }
    };
    SelectAndRevealCommand _selectAndRevealCommand = new SelectAndRevealCommand(this.root, _function);
    _commandStack_2.execute(_selectAndRevealCommand);
  }
  
  protected <T extends Object, U extends Object> IMappedElementDescriptor<T> createMappedDescriptor(final T domainObject) {
    IMappedElementDescriptor<T> _xblockexpression = null;
    {
      XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
      Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
      final Function1<XDiagramConfig, Iterable<? extends AbstractMapping<T>>> _function = new Function1<XDiagramConfig, Iterable<? extends AbstractMapping<T>>>() {
        @Override
        public Iterable<? extends AbstractMapping<T>> apply(final XDiagramConfig it) {
          return it.<T>getMappings(domainObject);
        }
      };
      Iterable<Iterable<? extends AbstractMapping<T>>> _map = IterableExtensions.map(_configurations, _function);
      Iterable<AbstractMapping<T>> _flatten = Iterables.<AbstractMapping<T>>concat(_map);
      final AbstractMapping<T> mapping = IterableExtensions.<AbstractMapping<T>>head(_flatten);
      XDiagramConfig _config = mapping.getConfig();
      IMappedElementDescriptorProvider _domainObjectProvider = _config.getDomainObjectProvider();
      _xblockexpression = _domainObjectProvider.<T>createMappedElementDescriptor(domainObject, mapping);
    }
    return _xblockexpression;
  }
  
  public void register(final IEditorPart editor) {
    boolean _add = this.contributingEditors.add(editor);
    if (_add) {
      this.changedEditors.add(editor);
      if ((editor instanceof XtextEditor)) {
        IXtextDocument _document = ((XtextEditor)editor).getDocument();
        final IXtextModelListener _function = new IXtextModelListener() {
          @Override
          public void modelChanged(final XtextResource it) {
            FXDiagramView.this.changedEditors.add(((XtextEditor)editor));
          }
        };
        _document.addModelListener(_function);
      }
    }
    boolean _equals = Objects.equal(this.listener, null);
    if (_equals) {
      EditorListener _editorListener = new EditorListener(this);
      this.listener = _editorListener;
      IWorkbenchPartSite _site = editor.getSite();
      IWorkbenchPage _page = _site.getPage();
      _page.addPartListener(this.listener);
    }
  }
  
  public boolean deregister(final IWorkbenchPartReference reference) {
    boolean _xblockexpression = false;
    {
      final IWorkbenchPart part = reference.getPart(false);
      boolean _xifexpression = false;
      boolean _notEquals = (!Objects.equal(part, null));
      if (_notEquals) {
        boolean _xblockexpression_1 = false;
        {
          this.changedEditors.remove(part);
          _xblockexpression_1 = this.contributingEditors.remove(part);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public void dispose() {
    this.gestureConverter.dispose();
    super.dispose();
  }
  
  @Pure
  public XRoot getRoot() {
    return this.root;
  }
}