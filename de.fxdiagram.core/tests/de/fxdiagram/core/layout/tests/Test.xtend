package de.fxdiagram.core.layout.tests

import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Group
import javafx.scene.shape.Rectangle
import javafx.application.Platform
import javafx.scene.Scene
import de.fxdiagram.core.XRoot
import javafx.scene.Node

class Test extends Application{

	def static void main(String[] args) {
		launch(args)
	}
	XRoot root 
	Node r
	override start(Stage primaryStage) throws Exception {
		primaryStage.scene = new Scene(
			root = new XRoot => [ 
				headsUpDisplay.children += r = new Rectangle(10,10,10,10) 
			], 320, 240)
		primaryStage.show
		new Thread() {
			override run() {
				Thread.sleep(1000)
				Platform.runLater [
					root.headsUpDisplay.children.remove(r)
					println('click')
				]		
			}
		}.start
		
	}
	
	
}