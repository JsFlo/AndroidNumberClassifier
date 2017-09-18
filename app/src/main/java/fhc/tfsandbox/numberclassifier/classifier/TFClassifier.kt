package fhc.tfsandbox.numberclassifier.classifier

import android.graphics.*
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth


data class NodeDef<T>(val name: String, val shape: Array<T>)


class TFClassifier(val tfInference: TensorFlowInferenceInterface,
                   val inputNodeDef: NodeDef<Int>,
                   val outputNodeDef: NodeDef<Float>)
    : SimpleClassifier<Bitmap, Int, FloatArray, FloatArray>() {

    override fun transformClassifyInputToModelInput(bitmap: Bitmap): FloatArray {
        val width = bitmap.width
        val height = bitmap.height

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        //bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels.map {
            // Set 0 for white and 255 for black pixel
            0xff - (it and 0xff)
        }.map {
            it.toFloat()
        }.toFloatArray()
    }

    override fun classifyModelInput(modelInput: FloatArray): FloatArray {
        // feed
        tfInference.feed(inputNodeDef.name, modelInput, *reshape(inputNodeDef.shape.toIntArray()))

        // fetch
        val floatOutputs = outputNodeDef.shape.toFloatArray()
        tfInference.run(arrayOf(outputNodeDef.name))
        tfInference.fetch("output", floatOutputs)
        return floatOutputs
    }

    override fun transformModelOutputToClassifyOutput(modelOutput: FloatArray): Int {
        return modelOutput.indexOfFirst { it > 0.0 }
    }

    private fun reshape(intArray: IntArray) = intArray.map { it.toLong() }.toLongArray()

    fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
        val width: Int
        val height: Int
        height = bmpOriginal.height
        width = bmpOriginal.width

        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.setColorFilter(f)
        c.drawBitmap(bmpOriginal, 0f, 0f, paint)
        return bmpGrayscale
    }
}