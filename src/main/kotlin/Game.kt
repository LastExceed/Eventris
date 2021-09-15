import com.kgl.glfw.*
import kotlinx.coroutines.*

const val stageWidth = 10
const val stageHeight = 20

val virtualHeight
	get() = stageHeight + 4

object Game {
	private val randomizer = BagRandomizer(1)
	private val rotationSystem = SRSjstris
	val stage = MutableList(virtualHeight) { Array(stageWidth) { Color.Black } }
	var currentTetromino = randomizer.next()
	val ghost
		get() = currentTetromino.copy().apply { tryMove(MoveDirection.Down, true) }

	fun onKeyDown(key: KeyboardKey) {
		when (key) {
			KeyboardKey.F11 -> Renderer.toggleFullscreen()

			KeyboardKey.LEFT -> {
				Das.begin(DasDirection.Left)
				val moved = currentTetromino.tryMove(MoveDirection.Left, false)
				if (moved) Renderer.drawFrame()
			}
			KeyboardKey.RIGHT -> {
				Das.begin(DasDirection.Right)
				val moved = currentTetromino.tryMove(MoveDirection.Right, false)
				if (moved) Renderer.drawFrame()
			}
			KeyboardKey.DOWN -> {
				val moved = currentTetromino.tryMove(MoveDirection.Down, true)
				if (moved) Renderer.drawFrame()
			}
			KeyboardKey.SPACE -> {
				val moved = currentTetromino.tryMove(MoveDirection.Down, true)
				if (moved) Renderer.drawFrame()
				lockPiece()
			}
			KeyboardKey.A -> {
				val rotated = currentTetromino.tryRotate(Rotation.CounterClockwise)
				if (rotated) Renderer.drawFrame()
			}
			KeyboardKey.D -> {
				val rotated = currentTetromino.tryRotate(Rotation.Clockwise)
				if (rotated) Renderer.drawFrame()
			}
			KeyboardKey.LEFT_SHIFT -> {
				val rotated = currentTetromino.tryRotate(Rotation.OneEighty)
				if (rotated) Renderer.drawFrame()
			}
			else -> {
			}
		}
	}

	fun onKeyUp(key: KeyboardKey) {
		val directionToCancel = when (key) {
			KeyboardKey.LEFT -> DasDirection.Left
			KeyboardKey.RIGHT -> DasDirection.Right
			else -> return
		}
		if (Das.direction == directionToCancel) Das.cancel()
	}

	private fun Tetromino.tryMove(direction: MoveDirection, repeat: Boolean): Boolean {
		val new = copy()
		when (direction) {
			MoveDirection.Left -> new.x--
			MoveDirection.Right -> new.x++
			MoveDirection.Down -> new.y--
		}
		val success = new.fits
		if (success) {
			x = new.x
			y = new.y
			if (repeat) tryMove(direction, true)
		}
		return success
	}

	private fun Tetromino.tryRotate(rotation: Rotation): Boolean {
		val newOrientation = Tetromino.Orientation.values().run {
			val index = indexOf(currentTetromino.orientation)
			val offset = when (rotation) {
				Rotation.Clockwise -> 1
				Rotation.CounterClockwise -> 3
				Rotation.OneEighty -> 2
			}
			get((index + offset) % 4)
		}

		val kickTable = rotationSystem.getKicktable(type, orientation, rotation)
		val kick = kickTable.find { copy(newOrientation, x + it.first, y + it.second).fits }

		if (kick != null) {
			orientation = newOrientation
			x += kick.first
			y += kick.second
		}
		return kick != null
	}

	private val Tetromino.fits
		get() = blocks.all {
			val blockX = x + it.first
			val blockY = y + it.second
			blockX in stage.first().indices && blockY >= 0 && !(blockY in stage.indices && stage[blockY][blockX].isNotBlack())
		}

	private fun lockPiece() {
		with(currentTetromino) {
			blocks.forEach {
				stage[y + it.second][x + it.first] = type.color
			}
		}
		currentTetromino = randomizer.next()

		stage.removeAll { it.all { it.isNotBlack() } }
		repeat(virtualHeight - stage.size) {
			stage.add(Array(stageWidth) { Color.Black })
		}

		Renderer.drawFrame()
	}

	object Das {
		private const val delay = 80L

		private var nextTime: Long? = null
		var direction: DasDirection? = null
		private var job: Job? = null

		fun begin(direction: DasDirection) {
			nextTime = System.currentTimeMillis() + delay
			this.direction = direction
			job = GlobalScope.launch {
				delay(delay)
				Glfw.postEmptyEvent()
			}
		}

		fun cancel() {
			nextTime = null
			direction = null
			job?.cancel()
		}

		fun execute() {
			nextTime.let { if (it == null || System.currentTimeMillis() < it) return }
			when (direction) {
				DasDirection.Left -> if (currentTetromino.tryMove(MoveDirection.Left, true)) Renderer.drawFrame()
				DasDirection.Right -> if (currentTetromino.tryMove(MoveDirection.Right, true)) Renderer.drawFrame()
				null -> return
			}
		}
	}
}

enum class DasDirection {
	Left,
	Right
}

enum class MoveDirection {
	Left,
	Right,
	Down
}

enum class Rotation {
	Clockwise,
	CounterClockwise,
	OneEighty
}