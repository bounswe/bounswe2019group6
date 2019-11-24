package com.traderx.ui.alert


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.request.AlertRequest
import com.traderx.enum.AlertType
import com.traderx.enum.TransactionType
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.EquipmentViewModel
import io.reactivex.disposables.CompositeDisposable
import java.lang.Double.parseDouble

/**
 * A simple [Fragment] subclass.
 */
class AlertFragment : Fragment(), FragmentTitleEmitters {

    private var equipmentCode: String? = null

    private lateinit var equipmentViewModel: EquipmentViewModel

    private lateinit var amount: EditText
    private var transactionType: TransactionType = TransactionType.BUY
    private var alertType: AlertType = AlertType.ABOVE
    private lateinit var limit: EditText
    private lateinit var warningLayout: LinearLayout
    private lateinit var warning: TextView

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            equipmentCode = it.getString(ARG_EQUIPMENT_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = context as Context

        setFragmentTitle(context, getString(R.string.alert) + " - " + equipmentCode)

        equipmentViewModel =
            ViewModelProvider(this, Injection.provideEquipmentViewModelFactory(context)).get(
                EquipmentViewModel::class.java
            )

        val root = inflater.inflate(R.layout.fragment_alert, container, false)

        root.findViewById<RadioButton>(R.id.sell)?.let { radio ->
            radio.setOnClickListener {
                onTransactionTypeChange(radio)
            }
        }
        root.findViewById<RadioButton>(R.id.buy)?.let { radio ->
            radio.setOnClickListener {
                onTransactionTypeChange(radio)
            }
        }
        root.findViewById<RadioButton>(R.id.above)?.let { radio ->
            radio.setOnClickListener {
                onAlertTypeChange(radio)
            }
        }
        root.findViewById<RadioButton>(R.id.below)?.let { radio ->
            radio.setOnClickListener {
                onAlertTypeChange(radio)
            }
        }

        amount = root.findViewById(R.id.amount)
        limit = root.findViewById(R.id.limit)
        warningLayout = root.findViewById(R.id.warning_layout)
        warning = root.findViewById(R.id.warning)

        root.findViewById<Button>(R.id.create_action)?.let { button ->
            button.setOnClickListener {
                createAlert(button)
            }
        }

        return root
    }

    fun onTransactionTypeChange(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.id) {
                R.id.sell -> if (checked) {
                    transactionType = TransactionType.SELL
                }
                R.id.buy -> if (checked) {
                    transactionType = TransactionType.BUY
                }
            }
        }
    }

    fun onAlertTypeChange(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.id) {
                R.id.above -> if (checked) {
                    alertType = AlertType.ABOVE
                }
                R.id.below -> if (checked) {
                    alertType = AlertType.BELOW
                }
            }
        }
    }

    private fun createAlert(button: Button) {
        if (button.isProgressActive()) {
            return
        }

        if(amount.text.isEmpty() || limit.text.isEmpty()) {
            showError(getString(R.string.amount_or_limit_empty))
            return
        }

        button.showProgress()

        disposable.add(
            equipmentViewModel.createAlert(
                AlertRequest(
                    code = equipmentCode ?: "",
                    amount = parseDouble(amount.text.toString()),
                    limit = parseDouble(limit.text.toString()),
                    transactionType = transactionType.value,
                    alertType = alertType.value
                )
            ).compose(Helper.applyCompletableSchedulers())
                .doOnComplete {
                    button.hideProgress(R.string.create)
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        getString(R.string.alert_create_success),
                        Snackbar.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(AlertFragmentDirections.actionNavigationAlertToNavigationAlerts())
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    private fun clearError() {
        warningLayout.visibility = View.GONE
    }

    private fun showError(text: String) {
        warningLayout.visibility = View.VISIBLE
        warning.text = text
    }

    companion object {
        private const val ARG_EQUIPMENT_CODE = "equipment_code"
    }
}
