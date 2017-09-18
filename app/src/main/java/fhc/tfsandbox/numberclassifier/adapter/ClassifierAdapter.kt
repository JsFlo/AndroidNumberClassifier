package fhc.tfsandbox.numberclassifier.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fhc.tfsandbox.numberclassifier.R
import kotlinx.android.synthetic.main.item_classifier.view.*

class ClassifierAdapter(vararg val listOfClassifiers: ClassifierViewModel) : RecyclerView.Adapter<ClassifierViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ClassifierViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_classifier, parent, false)
        return ClassifierViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClassifierViewHolder?, position: Int) {
        val vm = listOfClassifiers[position]
        holder?.let {
            it.classifierName.text = vm.name
            it.classifierResult.text = vm.result.toString()
        }
    }

    override fun getItemCount(): Int = listOfClassifiers.size

    fun setResult(name: String, result: Int) {
        val index = listOfClassifiers
                .indexOfFirst { it.name == name }
        if (index != -1) {
            listOfClassifiers[index].result = result
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

data class ClassifierViewModel(val name: String, var result: Int = 0)