import com.kgl.glfw.*
import com.kgl.glfw.Window
import com.kgl.opengl.utils.*
import org.lwjgl.opengl.*

object Renderer {
	lateinit var window: Window

	fun init() {
		Glfw.init()

		window = Window(1080, 1080, "Eventris", null) //Glfw.primaryMonitor
		Glfw.currentContext = window

		Glfw.setSwapInterval(0)//disable vsync

		Loader.load()

		window.setSizeCallback { _, width, height ->
			GL11.glViewport(0, 0, width, height)
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

	private const val squareSize = 0.04f

	fun drawFrame() {
		GL11.glClearColor(0f, 0f, 0f, 1f)
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

		Game.stage.take(stageHeight).forEachIndexed { y, row ->
			row.forEachIndexed { x, color ->
				if (color.isNotBlack()) {
					drawSquare(x, y, color)
				}
			}
		}

		with(Game.currentPiece) {
			blocks.forEach {
				drawSquare(x + it.first, y + it.second, type.color)
			}
		}

		GL11.glBegin(GL11.GL_LINE_STRIP)

		GL11.glColor3f(1f, 1f, 1f)

		GL11.glVertex2f(0f, 0f)
		GL11.glVertex2f(squareSize * 10, 0f)
		GL11.glVertex2f(squareSize * 10, squareSize * 20)
		GL11.glVertex2f(0f, squareSize * 20)
		GL11.glVertex2f(0f, 0f)

		GL11.glEnd()

		window.swapBuffers()
	}

	fun drawSquare(x: Int, y: Int, color: Color) {
		val xf = x * squareSize
		val yf = y * squareSize

		GL11.glBegin(GL11.GL_QUADS)

		GL11.glColor3f(color.r, color.g, color.b)

		GL11.glVertex2f(xf, yf)
		GL11.glVertex2f(xf + squareSize, yf)
		GL11.glVertex2f(xf + squareSize, yf + squareSize)
		GL11.glVertex2f(xf, yf + squareSize)

		GL11.glEnd()
	}
}