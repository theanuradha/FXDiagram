package de.fxdiagram.examples.lcars;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectHandleImpl;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.examples.lcars.LcarsModelProvider;

@ModelNode({ "id", "key", "provider" })
@SuppressWarnings("all")
public class LcarsConnectionHandle extends DomainObjectHandleImpl {
  public LcarsConnectionHandle(final String fieldName, final LcarsModelProvider provider) {
    super(fieldName, fieldName, provider);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LcarsConnectionHandle() {
  }
  
  public void populate(final ModelElement modelElement) {
    modelElement.addProperty(idProperty(), String.class);
    modelElement.addProperty(keyProperty(), String.class);
    modelElement.addProperty(providerProperty(), DomainObjectProvider.class);
  }
}
