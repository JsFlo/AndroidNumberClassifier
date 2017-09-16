package fhc.tfsandbox.numberclassifier.classifier

import fhc.tfsandbox.numberclassifier.DrawView
import fhc.tfsandbox.numberclassifier.model.NodeDef
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

class TFClassifier(val tfInference: TensorFlowInferenceInterface,
                   val inputNodeDef: NodeDef<Int>,
                   val outputNodeDef: NodeDef<Float>)
    : SimpleClassifier<DrawView?, Int, FloatArray, FloatArray>() {

    override fun transformClassifyInputToModelInput(data: DrawView?): FloatArray {
        val floatArray = data?.pixelData?.map { it.toFloat() }
                ?.toFloatArray()?.toTypedArray()

        // TODO: A little akward to pass around null or an empty list, need to bring in optionals
        return floatArray?.toFloatArray()!!
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