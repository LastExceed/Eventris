import com.kgl.glfw.*
import com.kgl.opengl.utils.*
import org.lwjgl.opengl.GL11.*

object Renderer {
	private lateinit var window: Window

	fun init() {
		Glfw.init()

		window = Window(1080, 1080, "Eventris", null) //Glfw.primaryMonitor
		Glfw.currentContext = window

		Glfw.setSwapInterval(0)//disable vsync

		Loader.load()

		window.setSizeCallback { _, width, height ->
			glViewport(0, 0, width, height)
			drawFrame()
		}
		window.setKeyCallback { _, keyboardKey, scancode, action, mods ->
			when (action) {
				Action.Press -> Game.onKeyDown(keyboardKey)
				Action.Repeat -> {}
				Action.Release -> Game.onKeyUp(keyboardKey)
			}
		}

		drawFrame()
	}

	fun runEventLoop() {
		while (!window.shouldClose) {
			Glfw.waitEvents()
			Game.Das.execute()
		}
	}

	fun toggleFullscreen() {
		with(Glfw.primaryMonitor!!) {
			val monitor = if (window.monitor == null) this else null
			window.setMonitor(monitor, 0, 0, videoMode.width, videoMode.height, videoMode.refreshRate)
			if (monitor == null) {
				window.position = 100 to 100
				drawFrame()
			}
		}

	}

	private const val squareSize = 0.04f

	fun drawFrame() {
		glClearColor(0f, 0f, 0f, 1f)
		glClear(GL_COLOR_BUFFER_BIT)

		Game.stage.take(stageHeight).forEachIndexed { y, row ->
			row.forEachIndexed { x, color ->
				if (color.isNotBlack()) {
					drawSquare(x, y, color)
				}
			}
		}

		with(Game.ghost) {
			blocks.forEach {
				drawSquare(x + it.first, y + it.second, type.color, true)
			}
		}
		with(Game.currentTetromino) {
			blocks.forEach {
				drawSquare(x + it.first, y + it.second, type.color)
			}
		}

		glBegin(GL_LINE_STRIP)

		glColor3f(1f, 1f, 1f)

		glVertex2f(0f, 0f)
		glVertex2f(squareSize * 10, 0f)
		glVertex2f(squareSize * 10, squareSize * 20)
		glVertex2f(0f, squareSize * 20)
		glVertex2f(0f, 0f)

		glEnd()

		window.swapBuffers()
	}

	private fun drawSquare(x: Int, y: Int, color: Color, isGhost: Boolean = false) {
		val xf = x * squareSize
		val yf = y * squareSize

		glBegin(GL_QUADS)

		if (isGhost) glColor3f(color.r * 0.5f, color.g * 0.5f, color.b * 0.5f)
		else glColor3f(color.r, color.g, color.b)

		glVertex2f(xf, yf)
		glVertex2f(xf + squareSize, yf)
		glVertex2f(xf + squareSize, yf + squareSize)
		glVertex2f(xf, yf + squareSize)

		glEnd()
	}
}