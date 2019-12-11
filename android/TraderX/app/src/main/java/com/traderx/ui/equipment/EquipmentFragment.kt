package com.traderx.ui.equipment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.CommentResponse
import com.traderx.api.response.EquipmentResponse
import com.traderx.ui.comment.CommentFragment
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.EquipmentViewModel
import io.reactivex.disposables.CompositeDisposable
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView

class EquipmentFragment : Fragment(), FragmentTitleEmitters {
    private var equipmentCode: String? = null
    private lateinit var equipmentViewModel: EquipmentViewModel

    private lateinit var code: TextView
    private lateinit var name: TextView
    private lateinit var value: TextView
    private lateinit var percentage: TextView
    private lateinit var prediction: TextView
    private lateinit var equipmentType: TextView
    private lateinit var stock: TextView
    private lateinit var chart: LineChartView
    private var isCommenting = false
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            equipmentCode = it.getString(ARG_EQUIPMENT_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, equipmentCode)

        val equipmentViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context as Context)

        equipmentViewModel = ViewModelProvider(this, equipmentViewModelFactory)
            .get(EquipmentViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_equipment, container, false)

        code = root.findViewById(R.id.code)
        name = root.findViewById(R.id.name)
        value = root.findViewById(R.id.value)
        percentage = root.findViewById(R.id.percentage)
        stock = root.findViewById(R.id.stock)
        prediction = root.findViewById(R.id.prediction)
        equipmentType = root.findViewById(R.id.equipment_type)

        root.findViewById<Button>(R.id.buy_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(
                    EquipmentFragmentDirections.actionNavigationEquipmentToNavigationTransaction(
                        equipmentCode ?: ""
                    )
                )
            }
        }

        root.findViewById<FloatingActionButton>(R.id.alert_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(
                    EquipmentFragmentDirections.actionNavigationEquipmentToNavigationAlert(
                        equipmentCode ?: ""
                    )
                )
            }
        }

        root.findViewById<FrameLayout>(R.id.comments)?.let {
            val commentFragment = CommentFragment(
                equipmentViewModel.getComments(equipmentCode ?: "")
                    .compose(Helper.applySingleSchedulers<ArrayList<CommentResponse>>()),
                { id, doOnSuccess ->
                    deleteComment(id, doOnSuccess)
                },
                { comment, doOnSuccess ->
                    createComment(comment, doOnSuccess)
                },
                {}
            )

            val fragmentTransaction = fragmentManager?.beginTransaction()

            fragmentTransaction?.add(it.id, commentFragment, "test")
            fragmentTransaction?.commit()
        }

        chart = root.findViewById(R.id.chart)
        chart.setInteractive(false)

        disposable.add(
            equipmentViewModel.getEquipment(equipmentCode ?: "")
                .compose(Helper.applySingleSchedulers<EquipmentResponse>())
                .subscribe({
                    updateView(it)
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )

        return root
    }

    private fun deleteComment(id: Int, doOnSuccess: () -> Unit) {
        if(isCommenting) {
            return
        }

        isCommenting = true

        disposable.add(
            equipmentViewModel.deleteComment(id)
                .compose(Helper.applyCompletableSchedulers())
                .doOnComplete {
                    doOnSuccess()
                    isCommenting = false
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        getString(R.string.comment_delete_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    private fun createComment(comment: String, doOnSuccess: () -> Unit) {
        if (isCommenting) {
            return
        }

        isCommenting = true

        disposable.add(
            equipmentViewModel.createComment(equipmentCode ?: "", comment)
                .compose(Helper.applyCompletableSchedulers())
                .doOnComplete {
                    doOnSuccess()
                    isCommenting = false
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        getString(R.string.comment_create_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    private fun updateView(equipment: EquipmentResponse) {
        code.text = equipment.equipment.code
        name.text = equipment.equipment.name
        value.text = String.format("%,.2f", equipment.equipment.currentValue)
        prediction.text = String.format("%,.2f", equipment.equipment.predictionRate)
        equipmentType.text = equipment.equipment.localizedType(context as Context)
        stock.text = String.format("%,.2f", equipment.equipment.currentStock)

        updateChart(chart, equipment.historicalValues)
    }

    private fun updateChart(chart: LineChartView, history: List<EquipmentResponse.History>) {
        val points = ArrayList<PointValue>()
        val lines = ArrayList<Line>()
        val data = LineChartData()

        val size = history.size
        var max = 0f
        var min = if (size > 0) history[0].open.toFloat() else 0f
        // Take last 30 points
        for (i in Math.max(0, size - 30) until size) {
            points.add(PointValue(i.toFloat(), history[i].open.toFloat()))
            max = Math.max(max, history[i].open.toFloat())
            min = Math.min(min, history[i].open.toFloat())
        }

        val line = Line(points).setColor(0xdd0000).setFilled(true)
        lines.add(line)

        data.setLines(lines).setBaseValue(0f).setAxisYLeft(Axis())

        chart.setLineChartData(data)

        val v = chart.maximumViewport
        v.set(v.left, max * (1.05f), v.right, min / (1.1f))
        chart.maximumViewport = v
        chart.currentViewport = v
    }

    companion object {
        private const val ARG_EQUIPMENT_NAME = "equipment_name"
    }
}
