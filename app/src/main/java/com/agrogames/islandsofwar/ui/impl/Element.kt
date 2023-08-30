package com.agrogames.islandsofwar.ui.impl

class Element(val type: ElementType, val x: Float, var y: Float,
              var width: Float, var height: Float, var texture: String,
              var onClick:  (Element.() -> Unit)? = null,
              var visible: Boolean = true, var renderInBorders: Boolean = true
)