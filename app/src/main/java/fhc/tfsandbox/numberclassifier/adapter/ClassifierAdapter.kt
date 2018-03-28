package fhc.tfsandbox.numberclassifier.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fhc.tfsandbox.numberclassifier.R
import fhc.tfsandbox.numberclassifier.classifier.BitmapMnistClassifier
import kotlinx.android.synthetic.main.item_classifier.view.*

class ClassifierAdapter(listOfClassifiers: List<BitmapMnistClassifier>) : RecyclerView.Adapter<ClassifierViewHolder>() {

    private val classifiers: List<ClassifierViewModel> = listOfClassifiers.map { ClassifierViewModel(it.name, 0, it) }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ClassifierViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_classifier, parent, false)
        return ClassifierViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClassifierViewHolder?, position: Int) {
        val vm = classifiers[position]
        holder?.let {
            it.classifierName.text = vm.name
            it.classifierResult.text = vm.result.toString()
        }
    }

    override fun getItemCount(): Int = classifiers.size

    fun classify(bitmap: Bitmap) {
        classifiers.forEachIndexed { index, classifier ->
            classifier.classify(bitmap)
            notifyItemChanged(index)
        }
    }

}

class ClassifierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var classifierName: TextView
    var classifierResult: TextView

    init {
        classifierName = itemView.classifier_name
        classifierResult = itemView.classifier_result
    }
}

data class ClassifierViewModel(val name: String, var result: Int = 0, val bitmapMnistClassifier: BitmapMnistClassifier) {
    fun classify(bitmap: Bitmap) {
        result = bitmapMnistClassifier.classify(bitmap)
    }
}