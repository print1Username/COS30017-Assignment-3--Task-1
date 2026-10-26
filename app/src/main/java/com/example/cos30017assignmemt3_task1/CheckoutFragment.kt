package com.example.cos30017assignmemt3_task1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.util.Locale

class CheckoutFragment : Fragment() {

	interface OnResetListener {
		fun onResetOrder()
	}

	data class MenuItem(val name: String, val price: Double)

	private val fillings = listOf(
		MenuItem("Ham", 2.50),
		MenuItem("Roasted Chicken", 2.00),
		MenuItem("Beef Steak", 4.50),
		MenuItem("Grilled Salmon", 3.70),
		MenuItem("Kebab", 4.00)
	)

	private val sides = listOf(
		MenuItem("Tomato", 1.00),
		MenuItem("Lettuce", 1.20),
		MenuItem("Onion", 0.50),
		MenuItem("Cheese", 1.50)
	)

	private val sizes = mapOf(
		"6 inch" to 7.00,
		"9 inch" to 9.50,
		"12 inch" to 13.00
	)

	private val addOns = listOf(
		MenuItem("Tea", 3.50),
		MenuItem("Coffee", 4.50),
		MenuItem("French fries", 5.00)
	)

	private lateinit var radioAddOn: RadioGroup
	private lateinit var totalPrice: TextView
	private lateinit var buttonPlaceOrder: ImageButton
	private lateinit var buttonReset: ImageButton

	private val selectedFillingNames: List<String>
		get() = arguments?.getStringArrayList(ARG_FILLINGS) ?: emptyList()

	private val selectedSideNames: List<String>
		get() = arguments?.getStringArrayList(ARG_SIDES) ?: emptyList()

	private val selectedSize: String
		get() = arguments?.getString(ARG_SIZE) ?: "6 inch"

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_checkout, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		radioAddOn = view.findViewById(R.id.radioAddOn)
		totalPrice = view.findViewById(R.id.totalPrice)
		buttonPlaceOrder = view.findViewById(R.id.buttonPlaceOrder)
		buttonReset = view.findViewById(R.id.buttonReset)

		radioAddOn.setOnCheckedChangeListener { _, _ -> updateTotal() }
		buttonPlaceOrder.setOnClickListener { placeOrder() }
		buttonReset.setOnClickListener {
			(requireActivity() as OnResetListener).onResetOrder()
		}

		updateTotal()
	}

	private fun getSelectedFillings(): List<MenuItem> {
		return fillings.filter { it.name in selectedFillingNames }
	}

	private fun getSelectedSides(): List<MenuItem> {
		return sides.filter { it.name in selectedSideNames }
	}

	private fun getSelectedAddOn(): MenuItem? {
		return when (radioAddOn.checkedRadioButtonId) {
			R.id.radioTea -> addOns[0]
			R.id.radioCoffee -> addOns[1]
			R.id.radioFries -> addOns[2]
			else -> null
		}
	}

	/**
	 * The cheapest selected item in each category is complimentary.
	 */
	private fun calculateCategoryPrice(selectedItems: List<MenuItem>): Double {
		if (selectedItems.isEmpty()) return 0.00
		return selectedItems.sumOf { it.price } - selectedItems.minOf { it.price }
	}

	private fun calculateAddOnPrice(): Double {
		val price = getSelectedAddOn()?.price ?: return 0.00
		return if (selectedFillingNames.size == 3) price * 0.80 else price
	}

	private fun calculateTotal(): Double {
		val sizePrice = sizes[selectedSize] ?: 0.00
		val fillingPrice = calculateCategoryPrice(getSelectedFillings())
		val sidePrice = calculateCategoryPrice(getSelectedSides())
		return sizePrice + fillingPrice + sidePrice + calculateAddOnPrice()
	}

	private fun updateTotal() {
		totalPrice.text = String.format(Locale.US, "RM %.2f", calculateTotal())
	}

	private fun placeOrder() {
		val selectedFillings = getSelectedFillings()
		if (selectedFillings.isEmpty()) {
			AlertDialog.Builder(requireContext())
				.setTitle("Error")
				.setMessage("Please select at least one filling.")
				.setPositiveButton("OK", null)
				.show()
			return
		}

		val selectedSides = getSelectedSides()
		val freeFilling = selectedFillings.minByOrNull { it.price }
		val freeSide = selectedSides.minByOrNull { it.price }
		val addOn = getSelectedAddOn()
		val addOnPrice = calculateAddOnPrice()

		val fillingLines = selectedFillings.joinToString("\n") {
			val price = if (it == freeFilling) 0.00 else it.price
			String.format(Locale.US, "%-20s RM %.2f", it.name, price)
		}

		val sideLines = if (selectedSides.isEmpty()) {
			"None"
		} else {
			selectedSides.joinToString("\n") {
				val price = if (it == freeSide) 0.00 else it.price
				String.format(Locale.US, "%-20s RM %.2f", it.name, price)
			}
		}

		val message = buildString {
			append(String.format(Locale.US, "Size\n%-20s RM %.2f\n\n", selectedSize, sizes[selectedSize] ?: 0.00))
			append("Filling\n")
			append(fillingLines)
			append("\n\nSide\n")
			append(sideLines)
			append("\n\nAdd-on\n")
			if (addOn == null) {
				append("None")
			} else {
				append(String.format(Locale.US, "%-20s RM %.2f", addOn.name, addOnPrice))
				if (selectedFillingNames.size == 3) {
					append(String.format(Locale.US, " (-RM %.2f)", addOn.price - addOnPrice))
				}
			}
			append(String.format(Locale.US, "\n\nTotal\n%-20s RM %.2f", "", calculateTotal()))
		}

		AlertDialog.Builder(requireContext())
			.setTitle("Your Order")
			.setMessage(message)
			.setPositiveButton("OK", null)
			.show()
	}

	companion object {
		private const val ARG_FILLINGS = "fillings"
		private const val ARG_SIDES = "sides"
		private const val ARG_SIZE = "size"

		fun newInstance(fillings: ArrayList<String>, sides: ArrayList<String>, size: String) =
			CheckoutFragment().apply {
				arguments = Bundle().apply {
					putStringArrayList(ARG_FILLINGS, fillings)
					putStringArrayList(ARG_SIDES, sides)
					putString(ARG_SIZE, size)
				}
			}
	}
}
