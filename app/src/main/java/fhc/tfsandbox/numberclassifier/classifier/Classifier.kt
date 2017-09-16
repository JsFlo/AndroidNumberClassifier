package fhc.tfsandbox.numberclassifier.classifier


interface Classifier<CLASSIFY_INPUT, CLASSIFY_OUTPUT> {
    fun classify(input: CLASSIFY_INPUT): CLASSIFY_OUTPUT
}

// assumes one input and one output
abstract class SimpleClassifier<CLASSIFY_INPUT, CLASSIFY_OUTPUT, MODEL_INPUT, MODEL_OUTPUT> : Classifier<CLASSIFY_INPUT, CLASSIFY_OUTPUT> {

    protected abstract fun transformClassifyInputToModelInput(data: CLASSIFY_INPUT): MODEL_INPUT

    protected abstract fun classifyModelInput(modelInput: MODEL_INPUT): MODEL_OUTPUT

    protected abstract fun transformModelOutputToClassifyOutput(modelOutput: MODEL_OUTPUT): CLASSIFY_OUTPUT

    override fun classify(input: CLASSIFY_INPUT): CLASSIFY_OUTPUT {
        val modelInput = transformClassifyInputToModelInput(input)
        val modelOutput = classifyModelInput(modelInput)
        return transformModelOutputToClassifyOutput(modelOutput)
    }

}