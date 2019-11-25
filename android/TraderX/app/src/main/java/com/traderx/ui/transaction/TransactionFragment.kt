package com.traderx.ui.transaction


import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.AssetResponse
import com.traderx.api.response.EquipmentResponse
import com.traderx.enum.EquipmentType
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import com.traderx.viewmodel.EquipmentViewModel
import com.traderx.viewmodel.TransactionViewModel
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.lang.Double.parseDouble

class TransactionFragment : Fragment(), FragmentTitleEmitters {

    private var equipmentCode: String? = null
    private lateinit var equipmentViewModel: EquipmentViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var authUserViewModel: AuthUserViewModel

    private lateinit var deposit: TextView
    private lateinit var name: TextView
    private lateinit var value: TextView
    private lateinit var code: TextView
    private lateinit var amount: EditText
    private lateinit var stock: TextView
    private lateinit var amountInUsd: EditText
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

        setFragmentTitle(context, getString(R.string.title_transaction))

        val equipmentViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context)
        equipmentViewModel = ViewModelProvider(this, equipmentViewModelFactory)
            .get(EquipmentViewModel::class.java)

        val transactionViewModelFactory = Injection.provideTransactionViewModelFactory(context)
        transactionViewModel = ViewModelProvider(
            this,
            transactionViewModelFactory
        ).get(TransactionViewModel::class.java)

        val authUserViewModelFactory = Injection.provideAuthUserViewModelFactory(context)
        authUserViewModel =
            ViewModelProvider(this, authUserViewModelFactory).get(AuthUserViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        stock = root.findViewById(R.id.stock)
        amount = root.findViewById(R.id.amount)
        deposit = root.findViewById(R.id.deposit)
        name = root.findViewById(R.id.name)
        value = root.findViewById(R.id.value)
        code = root.findViewById(R.id.code)
        amountInUsd = root.findViewById(R.id.amount_in_usd)
        warningLayout = root.findViewById(R.id.warning_layout)
        warning = root.findViewById(R.id.warning)

        root.findViewById<Button>(R.id.buy_action)?.let { button ->
            button.setOnClickListener {
                buyEquipment(button)
            }
        }

        disposable.add(
            equipmentViewModel.getEquipment(equipmentCode ?: "")
                .compose(Helper.applySingleSchedulers<EquipmentResponse>())
                .subscribe({
                    stock.text = it.equipment.currentStock.toString()
                    value.text = it.equipment.currentValue.toString()
                    name.text = it.equipment.name
                    code.text = it.equipment.code

                    var toUsd = it.equipment.currentValue

                    if (it.equipment.equipmentType == EquipmentType.CURRENCY) {
                        toUsd = 1 / toUsd
                    }

                    amountInUsd.setOnKeyListener { _, _, event ->
                        if (event.action == KeyEvent.ACTION_UP) {
                            applyRatio(amountInUsd, amount, 1 / toUsd)
                        }

                        false
                    }

                    amount.setOnKeyListener { _, _, event ->
                        if (event.action == KeyEvent.ACTION_UP) {
                            applyRatio(amount, amountInUsd, toUsd)
                        }

                        false
                    }
                }, {
                    ErrorHandler.handleError(it, context)
                })
        )

        disposable.add(
            authUserViewModel.getAssetAmount("USD")
                .compose(Helper.applySingleSchedulers<AssetResponse>())
                .subscribe({
                    deposit.text = it.amount.toString()
                }, {
                    if (it is HttpException && it.code() == 412) {
                        deposit.text = "0"
                    }
                    ErrorHandler.handleError(it, context)
                })
        )

        return root
    }

    override fun setFragmentTitle(context: Context?, title: String?) {
        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }

    private fun getAmount(): Double {
        val text = amount.text.toString()
        return if (text.isEmpty()) -1.0 else parseDouble(text)
    }

    private fun getAmountInUsd(): Double {
        val text = amountInUsd.text.toString()
        return if (text.isEmpty()) -1.0 else parseDouble(text)
    }

    private fun getDeposit(): Double {
        val text = deposit.text.toString()
        return if (text.isEmpty()) -1.0 else parseDouble(text)
    }

    private fun buyEquipment(button: Button) {
        if (getAmount() <= 0) {
            showError(getString(R.string.min_transaction_error))
            return
        } else if (getAmountInUsd() > getDeposit()) {
            showError(getString(R.string.not_enough_deposit))
            return
        }

        if (button.isProgressActive()) {
            return
        }

        button.showProgress()

        disposable.add(
            transactionViewModel.makeTransaction(
                equipmentCode ?: "", getAmount()

            )
                .compose(Helper.applyCompletableSchedulers())
                .doOnComplete {
                    button.hideProgress(R.string.buy)
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        getString(R.string.transaction_succes),
                        Snackbar.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(TransactionFragmentDirections.actionNavigationTransactionToNavigationTransactions())
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    private fun applyRatio(input: EditText, output: EditText, ratio: Double) {
        val amount = if(input.text.isEmpty()) 0.0 else parseDouble(input.text.toString())

        output.setText((amount * ratio).toString())
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
