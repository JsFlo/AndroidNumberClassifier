package fhc.tfsandbox.numberclassifier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fhc.tfsandbox.numberclassifier.DrawImageView.CanvasModel
import fhc.tfsandbox.numberclassifier.DrawImageView.DrawableImage
import fhc.tfsandbox.numberclassifier.classifier.NodeDef
import fhc.tfsandbox.numberclassifier.classifier.TFClassifier
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

class MainActivity : AppCompatActivity() {
    private val INPUT_SIZE = 28

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tfInf = TensorFlowInferenceInterface(assets, "model_graph_28_test.pb")
        val inputNodeDef = NodeDef("inputImage", intArrayOf(1, 28, 28, 1).toTypedArray())
        val outputNodeDef = NodeDef("output", FloatArray(10).toTypedArray())

        val classifier = TFClassifier(tfInf, inputNodeDef, outputNodeDef)

        val model = CanvasModel(INPUT_SIZE, INPUT_SIZE)
        val drawableImage = view_draw as DrawableImage
        drawableImage.init(model)

        button_clear.setOnClickListener { drawableImage.clear() }
        button_detect.setOnClickListener {
            println(classifier.classify(drawableImage.getBitmap()).toString())
        }

    }
}
