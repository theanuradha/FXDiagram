package de.fxdiagram.lib.shapes

import de.fxdiagram.core.XNode
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ImageNode extends XNode implements SvgExportable {

	static int instanceCount
	
	ImageView imageView

	new() {
		node = imageView = new ImageView => [
			preserveRatio = true
			fitWidthProperty.bind(widthProperty) 
			fitHeightProperty.bind(heightProperty)
		]
		key = 'ImageNode' + instanceCount
		instanceCount = instanceCount + 1
	}
	
	def setImage(Image image) {
		imageView.image = image
	}
	
	def getImage() {
		imageView.image
	}
	
	override toSvgElement(extension SvgExporter exporter) {
		this.toSvgImage(image)
	}
}