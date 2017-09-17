package fhc.tfsandbox.numberclassifier.classifier

import android.graphics.Bitmap
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

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
}