package com.example.cos30017assignmemt3_task1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class OrderOptionsFragment : Fragment() {

	interface OnOptionsSelectedListener {
		fun onOptionsSelected(
			fillings: ArrayList<String>,
			sides: ArrayList<String>,
			size: String
		)
	}

	private lateinit var checkHam: CheckBox
	private lateinit var checkChicken: CheckBox
	private lateinit var checkBeef: CheckBox
	private lateinit var checkSalmon: CheckBox
	private lateinit var checkKebab: CheckBox

	private lateinit var checkTomato: CheckBox
	private lateinit var checkLettuce: CheckBox
	private lateinit var checkOnion: CheckBox
	private lateinit var checkCheese: CheckBox

	private lateinit var radioSize: RadioGroup
	private lateinit var fillingSelection: TextView
	private lateinit var sideSelection: TextView
	private lateinit var buttonNext: Button

	private var lastSelectedFilling: String? = null
	private var lastSelectedSide: String? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_order_options, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initialiseViews(view)
		setupDefaultState()
		setupListeners()
	}

	private fun initialiseViews(view: View) {
		checkHam = view.findViewById(R.id.checkHam)
		checkChicken = view.findViewById(R.id.checkChicken)
		checkBeef = view.findViewById(R.id.checkBeef)
		checkSalmon = view.findViewById(R.id.checkSalmon)
		checkKebab = view.findViewById(R.id.checkKebab)

		checkTomato = view.findViewById(R.id.checkTomato)
		checkLettuce = view.findViewById(R.id.checkLettuce)
		checkOnion = view.findViewById(R.id.checkOnion)
		checkCheese = view.findViewById(R.id.checkCheese)

		radioSize = view.findViewById(R.id.radioSize)
		fillingSelection = view.findViewById(R.id.fillingSelection)
		sideSelection = view.findViewById(R.id.sideSelection)
		buttonNext = view.findViewById(R.id.buttonNext)
	}

	private fun setupDefaultState() {
		radioSize.clearCheck()
		fillingSelection.text = "Filling |"
		sideSelection.text = "Side |"
		lastSelectedFilling = null
		lastSelectedSide = null
		updateFillingAvailability()
	}

	private fun setupListeners() {
		checkHam.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection("Ham", isChecked)
		}
		checkChicken.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection("Roasted Chicken", isChecked)
		}
		checkBeef.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection("Beef Steak", isChecked)
		}
		checkSalmon.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection("Grilled Salmon", isChecked)
		}
		checkKebab.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection("Kebab", isChecked)
		}

		checkTomato.setOnCheckedChangeListener { _, isChecked ->
			handleSideSelection("Tomato", isChecked)
		}
		checkLettuce.setOnCheckedChangeListener { _, isChecked ->
			handleSideSelection("Lettuce", isChecked)
		}
		checkOnion.setOnCheckedChangeListener { _, isChecked ->
			handleSideSelection("Onion", isChecked)
		}
		checkCheese.setOnCheckedChangeListener { _, isChecked ->
			handleSideSelection("Cheese", isChecked)
		}

		buttonNext.setOnClickListener { goToCheckout() }
	}

	fun goToCheckout(requireSelection: Boolean = true) {
		val selectedFillings = getSelectedFillings()
		if (requireSelection && selectedFillings.isEmpty()) {
			AlertDialog.Builder(requireContext())
				.setTitle("Error")
				.setMessage("Please select at least one filling.")
				.setPositiveButton("OK", null)
				.show()
			return
		}

		if (requireSelection && radioSize.checkedRadioButtonId == View.NO_ID) {
			AlertDialog.Builder(requireContext())
				.setTitle("Error")
				.setMessage("Please select a size.")
				.setPositiveButton("OK", null)
				.show()
			return
		}

		(requireActivity() as OnOptionsSelectedListener).onOptionsSelected(
			selectedFillings,
			getSelectedSides(),
			getSelectedSize()
		)
	}

	private fun handleFillingSelection(fillingName: String, isChecked: Boolean) {
		if (isChecked) {
			lastSelectedFilling = fillingName
		} else if (lastSelectedFilling == fillingName) {
			lastSelectedFilling = null
		}

		updateFillingDisplay()
		updateFillingAvailability()
	}

	private fun updateFillingDisplay() {
		fillingSelection.text =
			if (lastSelectedFilling == null) {
				"Filling |"
			} else {
				"Filling | $lastSelectedFilling"
			}
	}

	private fun updateFillingAvailability() {
		val fillingCheckBoxes = getFillingCheckBoxes()
		val selectedCount = fillingCheckBoxes.count { it.isChecked }

		fillingCheckBoxes.forEach { checkBox ->
			checkBox.isEnabled = checkBox.isChecked || selectedCount < 3
		}
	}

	private fun getFillingCheckBoxes(): List<CheckBox> {
		return listOf(checkHam, checkChicken, checkBeef, checkSalmon, checkKebab)
	}

	private fun handleSideSelection(sideName: String, isChecked: Boolean) {
		if (isChecked) {
			lastSelectedSide = sideName
		} else if (lastSelectedSide == sideName) {
			lastSelectedSide = null
		}

		updateSideDisplay()
	}

	private fun updateSideDisplay() {
		sideSelection.text =
			if (lastSelectedSide == null) {
				"Side |"
			} else {
				"Side | $lastSelectedSide"
			}
	}

	private fun getSelectedFillings(): ArrayList<String> {
		val selectedFillings = arrayListOf<String>()
		if (checkHam.isChecked) selectedFillings.add("Ham")
		if (checkChicken.isChecked) selectedFillings.add("Roasted Chicken")
		if (checkBeef.isChecked) selectedFillings.add("Beef Steak")
		if (checkSalmon.isChecked) selectedFillings.add("Grilled Salmon")
		if (checkKebab.isChecked) selectedFillings.add("Kebab")
		return selectedFillings
	}

	private fun getSelectedSides(): ArrayList<String> {
		val selectedSides = arrayListOf<String>()
		if (checkTomato.isChecked) selectedSides.add("Tomato")
		if (checkLettuce.isChecked) selectedSides.add("Lettuce")
		if (checkOnion.isChecked) selectedSides.add("Onion")
		if (checkCheese.isChecked) selectedSides.add("Cheese")
		return selectedSides
	}

	private fun getSelectedSize(): String {
		return when (radioSize.checkedRadioButtonId) {
			R.id.radio6Inch -> "6 inch"
			R.id.radio9Inch -> "9 inch"
			R.id.radio12Inch -> "12 inch"
			else -> ""
		}
	}
}
