package de.fxdiagram.xtext.glue.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.tools.AbstractChooser
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.buttons.RapidButtonBehavior
import de.fxdiagram.lib.tools.CarusselChooser
import de.fxdiagram.lib.tools.CoverFlowChooser
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter
import java.util.List
import javafx.collections.ListChangeListener

import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class LazyConnectionMappingBehavior<ARG> extends RapidButtonBehavior<XNode> {
	
	List<LazyConnectionRapidButtonAction<?, ARG>> actions = newArrayList

	new(XNode host) {
		super(host)
	}

	def addConnectionMappingCall(AbstractConnectionMappingCall<?, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource) {
		actions += createAction(mappingCall, configInterpreter, hostIsSource)
	}
	
	protected def createAction(AbstractConnectionMappingCall<?, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource) {
		val action = new LazyConnectionRapidButtonAction(mappingCall, configInterpreter, hostIsSource)
		add(new RapidButton(host, LEFT, mappingCall.getImage(LEFT), action))
		add(new RapidButton(host, RIGHT, mappingCall.getImage(RIGHT), action))
		add(new RapidButton(host, TOP, mappingCall.getImage(TOP), action))
		add(new RapidButton(host, BOTTOM, mappingCall.getImage(BOTTOM), action)) 
		action
	}
	
	
	override protected doActivate() {
		super.doActivate()
		actions.forEach[updateEnablement(host)]
		host.diagram.connectionsProperty.addListener [ ListChangeListener.Change<? extends XConnection> it |
			actions.forEach[updateEnablement(host)]
		]
	}
}

class LazyConnectionRapidButtonAction<MODEL, ARG> extends RapidButtonAction {
	
	XDiagramConfigInterpreter configInterpreter

	AbstractConnectionMappingCall<MODEL, ARG> mappingCall

	boolean hostIsSource

	new(AbstractConnectionMappingCall<MODEL, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource) {
		this.mappingCall = mappingCall
		this.configInterpreter = configInterpreter
		this.hostIsSource = hostIsSource
	}
	
	def void updateEnablement(XNode host) {
		val hostDescriptor = host.domainObject as XtextDomainObjectDescriptor<ARG>
		val existingConnectionDescriptors = host.diagram.connections.map[domainObject].toSet
		hostDescriptor.withDomainObject[ 
			domainArgument |
			val connectionDomainObjects = configInterpreter.select(mappingCall, domainArgument)
			for(connectionDomainObject: connectionDomainObjects) {
				val connectionDescriptor = configInterpreter.getDescriptor(connectionDomainObject, mappingCall.connectionMapping)
				if(existingConnectionDescriptors.add(connectionDescriptor)) {
					val nodeMappingCall = (mappingCall.connectionMapping.source ?: mappingCall.connectionMapping.target)
					val nodeDomainObjects = configInterpreter.select(nodeMappingCall, connectionDomainObject)
					if(!nodeDomainObjects.empty) {
						enabled = true
						return null						
					}
				}
			}
			enabled = false
			null
		]
	}
	
	override perform(RapidButton button) {
		val chooser = createChooser(button)
		chooser.populateChooser(button.host)
		button.host.root.currentTool = chooser
	}
	
	def protected createChooser(RapidButton button) {
		val position = button.position
		val chooser = if(position.vertical) {
			new CarusselChooser(button.host, position)
		} else {
			new CoverFlowChooser(button.host, position)			
		}
		chooser
	}
	
	protected def populateChooser(AbstractChooser chooser, XNode host) {
		val hostDescriptor = host.domainObject as XtextDomainObjectDescriptor<ARG>
		val existingConnectionDescriptors = host.diagram.connections.map[domainObject].toSet
		hostDescriptor.withDomainObject[ 
			domainArgument |
			val connectionDomainObjects = configInterpreter.select(mappingCall, domainArgument)
			connectionDomainObjects.forEach [ connectionDomainObject |
				val connectionDescriptor = configInterpreter.getDescriptor(connectionDomainObject, mappingCall.connectionMapping)
				if(existingConnectionDescriptors.add(connectionDescriptor)) {
					val nodeMappingCall = (mappingCall.connectionMapping.source ?: mappingCall.connectionMapping.target)
					val nodeDomainObjects = configInterpreter.select(nodeMappingCall, connectionDomainObject)
					nodeDomainObjects.forEach [	
						chooser.addChoice(createNode(nodeMappingCall.nodeMapping), connectionDescriptor)
					]
				}
			]
			chooser.connectionProvider = [ thisNode, thatNode, connectionDesc |
				val descriptor = connectionDesc as XtextDomainObjectDescriptor<MODEL>
				mappingCall.connectionMapping.createConnection(descriptor) => [
					if(hostIsSource) {
						source = thisNode
						target = thatNode
					} else {
						source = thatNode
						target = thisNode
					}
				]
			]	
			null
		]
	}
	
	protected def <NODE> createNode(Object nodeDomainObject, NodeMapping<?> nodeMapping) {
		if (nodeMapping.isApplicable(nodeDomainObject)) {
			val nodeMappingCasted = nodeMapping as NodeMapping<NODE>
			val descriptor = configInterpreter.getDescriptor(nodeDomainObject as NODE, nodeMappingCasted)
			val node = nodeMappingCasted.createNode(descriptor) 
			node
		} else { 
			null
		}
	}
}