package fhc.tfsandbox.numberclassifier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import fhc.tfsandbox.numberclassifier.DrawImageView.CanvasModel
import fhc.tfsandbox.numberclassifier.DrawImageView.DrawableImage
import fhc.tfsandbox.numberclassifier.adapter.ClassifierAdapter
import fhc.tfsandbox.numberclassifier.adapter.ClassifierViewModel
import fhc.tfsandbox.numberclassifier.classifier.NodeDef
import fhc.tfsandbox.numberclassifier.classifier.TFClassifier
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

class MainActivity : AppCompatActivity() {
    private val INPUT_SIZE = 28

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup classifiers
        val outputNodeDef = NodeDef("output", FloatArray(10).toTypedArray())

        val listOfClassifiers = listOf(
                TFClassifier("v1",
                        TensorFlowInferenceInterface(assets, "model_graph_28_v1.pb"),
                        NodeDef("inputImage", intArrayOf(1, 28, 28, 1).toTypedArray()), outputNodeDef),

                TFClassifier("valid_graph",
                        TensorFlowInferenceInterface(assets, "valid_graph.pb"),
                        NodeDef("input", intArrayOf(1, 784).toTypedArray()), outputNodeDef),

                TFClassifier("test",
                        TensorFlowInferenceInterface(assets, "model_graph_28_test.pb"),
                        NodeDef("inputImage", intArrayOf(1, 28, 28, 1).toTypedArray()), outputNodeDef))

        // initialise draw view
        val model = CanvasModel(INPUT_SIZE, INPUT_SIZE)
        val drawableImage = view_draw as DrawableImage
        drawableImage.init(model)

        // setup the adapter
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = ClassifierAdapter(listOfClassifiers)
        recycler_view.adapter = adapter

        button_clear.setOnClickListener {
            drawableImage.clear()
        }
        button_detect.setOnClickListener {
            val bitmap = drawableImage.getBitmap()
            adapter.classify(bitmap)
        }

    }
}
