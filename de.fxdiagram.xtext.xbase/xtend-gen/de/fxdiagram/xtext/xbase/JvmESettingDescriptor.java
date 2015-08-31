package de.fxdiagram.xtext.xbase;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.XtextESettingDescriptor;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@ModelNode
@SuppressWarnings("all")
public class JvmESettingDescriptor<ECLASS extends EObject> extends XtextESettingDescriptor<ECLASS> {
  public JvmESettingDescriptor() {
  }
  
  public JvmESettingDescriptor(final XtextEObjectID sourceID, final XtextEObjectID targetID, final EReference reference, final int index, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(sourceID, targetID, reference, index, mappingConfigID, mappingID, provider);
  }
  
  @Override
  protected IResourceServiceProvider getResourceServiceProvider() {
    URI _createURI = URI.createURI("dummy.___xbase");
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
