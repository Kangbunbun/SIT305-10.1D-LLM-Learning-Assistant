package com.example.llm_enhancedlearningassistantapp.ui.upgrade

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.llm_enhancedlearningassistantapp.R
import com.example.llm_enhancedlearningassistantapp.data.UserPrefs
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentUpgradeBinding
import com.example.llm_enhancedlearningassistantapp.model.PaymentIntentRequest
import com.example.llm_enhancedlearningassistantapp.network.RetrofitClient
import com.example.llm_enhancedlearningassistantapp.ui.BaseFragment
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch

class UpgradeFragment : BaseFragment<FragmentUpgradeBinding>(FragmentUpgradeBinding::inflate) {

    private lateinit var userPrefs: UserPrefs
    private lateinit var paymentSheet: PaymentSheet

    private val paymentService = RetrofitClient.paymentService
    private var selectedPaidPlan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PaymentConfiguration.init(requireContext(), STRIPE_PUBLISHABLE_KEY)

        paymentSheet = PaymentSheet(this) { paymentSheetResult ->
            handlePaymentResult(paymentSheetResult)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPrefs(requireContext())

        loadCurrentPlan()
        setupButtons()
        updatePlanButtons()
    }

    private fun loadCurrentPlan() {
        binding.tvCurrentPlan.text = "Current Plan: ${userPrefs.getCurrentPlan()}"
    }

    private fun setupButtons() {
        binding.btnStarter.setOnClickListener {
            if (userPrefs.getCurrentPlan() == "Starter") {
                Toast.makeText(requireContext(), "Starter is already your current plan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userPrefs.saveCurrentPlan("Starter")
            Toast.makeText(requireContext(), "Changed to Starter plan.", Toast.LENGTH_SHORT).show()

            loadCurrentPlan()
            updatePlanButtons()

            findNavController().navigate(R.id.action_upgradeFragment_to_profileFragment)
        }

        binding.btnIntermediate.setOnClickListener {
            if (userPrefs.getCurrentPlan() == "Intermediate") {
                Toast.makeText(requireContext(), "Intermediate is already your current plan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createPaymentIntentForPlan("Intermediate")
        }

        binding.btnAdvanced.setOnClickListener {
            if (userPrefs.getCurrentPlan() == "Advanced") {
                Toast.makeText(requireContext(), "Advanced is already your current plan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createPaymentIntentForPlan("Advanced")
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updatePlanButtons() {
        val currentPlan = userPrefs.getCurrentPlan()

        binding.btnStarter.isEnabled = currentPlan != "Starter"
        binding.btnIntermediate.isEnabled = currentPlan != "Intermediate"
        binding.btnAdvanced.isEnabled = currentPlan != "Advanced"

        binding.btnStarter.text =
            if (currentPlan == "Starter") "Current Plan" else "Use Starter"

        binding.btnIntermediate.text =
            if (currentPlan == "Intermediate") "Current Plan" else "Purchase with Stripe"

        binding.btnAdvanced.text =
            if (currentPlan == "Advanced") "Current Plan" else "Purchase with Stripe"
    }

    private fun createPaymentIntentForPlan(plan: String) {
        selectedPaidPlan = plan

        Toast.makeText(
            requireContext(),
            "Creating Stripe test payment for $plan...",
            Toast.LENGTH_SHORT
        ).show()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = paymentService.createPaymentIntent(
                    PaymentIntentRequest(plan = plan)
                )

                presentPaymentSheet(response.clientSecret)

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Payment setup failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun presentPaymentSheet(clientSecret: String) {
        val configuration = PaymentSheet.Configuration(
            merchantDisplayName = "LLM Learning Assistant"
        )

        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret = clientSecret,
            configuration = configuration
        )
    }

    private fun handlePaymentResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                userPrefs.saveCurrentPlan(selectedPaidPlan)

                Toast.makeText(
                    requireContext(),
                    "Payment successful. Upgraded to $selectedPaidPlan.",
                    Toast.LENGTH_LONG
                ).show()

                loadCurrentPlan()
                updatePlanButtons()

                findNavController().navigate(R.id.action_upgradeFragment_to_profileFragment)
            }

            is PaymentSheetResult.Canceled -> {
                Toast.makeText(
                    requireContext(),
                    "Payment cancelled. Your plan was not changed.",
                    Toast.LENGTH_LONG
                ).show()
            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(
                    requireContext(),
                    "Payment failed: ${paymentSheetResult.error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        private const val STRIPE_PUBLISHABLE_KEY =
            "pk_test_51TYJz78R0C7faaXBWs6UDzv3oDJBOycwjL9lHJt4zC8hQmUZUAQi9vR31MALQBCCyCcQ9bZaC1kSx4orWoS9ODe300x1cjCAjh"
    }
}