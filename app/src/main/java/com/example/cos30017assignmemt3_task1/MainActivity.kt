package com.example.cos30017assignmemt3_task1

import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

	// ==================== DATA CLASSES ====================

	/**
	 * Stores the name and price of each sandwich option.
	 */
	data class MenuItem(
		val name: String,
		val price: Double
	)

	// ==================== PRICE DATA ====================

	// Filling options and prices
	private val fillings = listOf(
		MenuItem("Ham", 2.50),
		MenuItem("Roasted Chicken", 2.00),
		MenuItem("Beef Steak", 4.50),
		MenuItem("Grilled Salmon", 3.70),
		MenuItem("Kebab", 4.00)
	)

	// Side options and prices
	private val sides = listOf(
		MenuItem("Tomato", 1.00),
		MenuItem("Lettuce", 1.20),
		MenuItem("Onion", 0.50),
		MenuItem("Cheese", 1.50)
	)

	// Size options and prices
	private val sizes = mapOf(
		"6 inch" to 7.00,
		"9 inch" to 9.50,
		"12 inch" to 13.00
	)

	// ==================== VIEWS ====================

	// Filling checkboxes
	private lateinit var checkHam: CheckBox
	private lateinit var checkChicken: CheckBox
	private lateinit var checkBeef: CheckBox
	private lateinit var checkSalmon: CheckBox
	private lateinit var checkKebab: CheckBox

	// Side checkboxes
	private lateinit var checkTomato: CheckBox
	private lateinit var checkLettuce: CheckBox
	private lateinit var checkOnion: CheckBox
	private lateinit var checkCheese: CheckBox

	// Size radio group
	private lateinit var radioSize: RadioGroup

	// Selection display
	private lateinit var fillingSelection: TextView
	private lateinit var sideSelection: TextView

	// Total price
	private lateinit var totalPrice: TextView

	// Buttons
	private lateinit var buttonPlaceOrder: ImageButton
	private lateinit var buttonReset: ImageButton

	// ==================== SELECTION STATE ====================

	/*
	 * Stores the most recently selected filling and side.
	 * If the latest selected item is unchecked, the display
	 * returns to "Filling |" or "Side |".
	 */
	private var lastSelectedFilling: String? = null
	private var lastSelectedSide: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initialiseViews()
		setupDefaultState()
		setupListeners()
		updateTotal()
	}

	// ==================== INITIALISATION ====================

	/**
	 * Connects Kotlin variables to the views in activity_main.xml.
	 */
	private fun initialiseViews() {

		// Filling
		checkHam = findViewById(R.id.checkHam)
		checkChicken = findViewById(R.id.checkChicken)
		checkBeef = findViewById(R.id.checkBeef)
		checkSalmon = findViewById(R.id.checkSalmon)
		checkKebab = findViewById(R.id.checkKebab)

		// Side
		checkTomato = findViewById(R.id.checkTomato)
		checkLettuce = findViewById(R.id.checkLettuce)
		checkOnion = findViewById(R.id.checkOnion)
		checkCheese = findViewById(R.id.checkCheese)

		// Size
		radioSize = findViewById(R.id.radioSize)

		// Selection display
		fillingSelection = findViewById(R.id.fillingSelection)
		sideSelection = findViewById(R.id.sideSelection)

		// Total
		totalPrice = findViewById(R.id.totalPrice)

		// Buttons
		buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder)
		buttonReset = findViewById(R.id.buttonReset)
	}

	// ==================== DEFAULT STATE ====================

	/**
	 * Sets the application to its default state.
	 *
	 * Default:
	 * - 6 inch
	 * - RM 7.00
	 * - No filling selected
	 * - No side selected
	 */
	private fun setupDefaultState() {

		// Default sandwich size
		radioSize.check(R.id.radio6Inch)

		// Default selection labels
		fillingSelection.text = "Filling |"
		sideSelection.text = "Side |"

		// Default latest selections
		lastSelectedFilling = null
		lastSelectedSide = null

		// Make sure all filling checkboxes are enabled
		enableAllFillings()
	}

	// ==================== LISTENERS ====================

	private fun setupListeners() {

		// Filling checkbox listeners
		checkHam.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection(checkHam, "Ham", isChecked)
		}

		checkChicken.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection(
				checkChicken,
				"Roasted Chicken",
				isChecked
			)
		}

		checkBeef.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection(
				checkBeef,
				"Beef Steak",
				isChecked
			)
		}

		checkSalmon.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection(
				checkSalmon,
				"Grilled Salmon",
				isChecked
			)
		}

		checkKebab.setOnCheckedChangeListener { _, isChecked ->
			handleFillingSelection(
				checkKebab,
				"Kebab",
				isChecked
			)
		}

		// Side checkbox listeners
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

		// Size listener
		radioSize.setOnCheckedChangeListener { _, _ ->
			updateTotal()
		}

		// Place Order
		buttonPlaceOrder.setOnClickListener {
			placeOrder()
		}

		// Reset
		buttonReset.setOnClickListener {
			resetOrder()
		}
	}

	// ==================== FILLING LOGIC ====================

	/**
	 * Handles a filling checkbox selection or removal.
	 */
	private fun handleFillingSelection(
		checkBox: CheckBox,
		fillingName: String,
		isChecked: Boolean
	) {

		if (isChecked) {
			// Store the latest selected filling
			lastSelectedFilling = fillingName

		} else if (lastSelectedFilling == fillingName) {
			/*
			 * If the latest selected filling is cancelled,
			 * return the display to its default state.
			 */
			lastSelectedFilling = null
		}

		updateFillingDisplay()
		updateFillingAvailability()
		updateTotal()
	}

	/**
	 * Updates the Filling title to show the latest selected item.
	 */
	private fun updateFillingDisplay() {

		fillingSelection.text =
			if (lastSelectedFilling == null) {
				"Filling |"
			} else {
				"Filling | $lastSelectedFilling"
			}
	}

	/**
	 * Disables unselected fillings when 3 fillings are selected.
	 */
	private fun updateFillingAvailability() {

		val fillingCheckBoxes = getFillingCheckBoxes()

		val selectedCount = fillingCheckBoxes.count {
			it.isChecked
		}

		fillingCheckBoxes.forEach { checkBox ->

			/*
			 * Selected checkboxes remain enabled so the user
			 * can remove a selected filling.
			 */
			checkBox.isEnabled =
				checkBox.isChecked || selectedCount < 3
		}
	}

	/**
	 * Returns all filling checkboxes as a list.
	 */
	private fun getFillingCheckBoxes(): List<CheckBox> {
		return listOf(
			checkHam,
			checkChicken,
			checkBeef,
			checkSalmon,
			checkKebab
		)
	}

	/**
	 * Enables all filling checkboxes.
	 */
	private fun enableAllFillings() {
		getFillingCheckBoxes().forEach {
			it.isEnabled = true
		}
	}

	// ==================== SIDE LOGIC ====================

	/**
	 * Handles a side checkbox selection or removal.
	 */
	private fun handleSideSelection(
		sideName: String,
		isChecked: Boolean
	) {

		if (isChecked) {
			// Store the latest selected side
			lastSelectedSide = sideName

		} else if (lastSelectedSide == sideName) {
			/*
			 * If the latest selected side is cancelled,
			 * return the display to its default state.
			 */
			lastSelectedSide = null
		}

		updateSideDisplay()
		updateTotal()
	}

	/**
	 * Updates the Side title to show the latest selected item.
	 */
	private fun updateSideDisplay() {

		sideSelection.text =
			if (lastSelectedSide == null) {
				"Side |"
			} else {
				"Side | $lastSelectedSide"
			}
	}

	// ==================== SELECTED ITEMS ====================

	/**
	 * Returns all currently selected fillings.
	 */
	private fun getSelectedFillings(): List<MenuItem> {

		val selectedFillings = mutableListOf<MenuItem>()

		if (checkHam.isChecked) {
			selectedFillings.add(fillings[0])
		}

		if (checkChicken.isChecked) {
			selectedFillings.add(fillings[1])
		}

		if (checkBeef.isChecked) {
			selectedFillings.add(fillings[2])
		}

		if (checkSalmon.isChecked) {
			selectedFillings.add(fillings[3])
		}

		if (checkKebab.isChecked) {
			selectedFillings.add(fillings[4])
		}

		return selectedFillings
	}

	/**
	 * Returns all currently selected sides.
	 */
	private fun getSelectedSides(): List<MenuItem> {

		val selectedSides = mutableListOf<MenuItem>()

		if (checkTomato.isChecked) {
			selectedSides.add(sides[0])
		}

		if (checkLettuce.isChecked) {
			selectedSides.add(sides[1])
		}

		if (checkOnion.isChecked) {
			selectedSides.add(sides[2])
		}

		if (checkCheese.isChecked) {
			selectedSides.add(sides[3])
		}

		return selectedSides
	}

	// ==================== SIZE ====================

	/**
	 * Returns the currently selected sandwich size.
	 */
	private fun getSelectedSize(): String {

		return when (radioSize.checkedRadioButtonId) {

			R.id.radio9Inch -> "9 inch"

			R.id.radio12Inch -> "12 inch"

			else -> "6 inch"
		}
	}

	// ==================== PRICE CALCULATION ====================

	/**
	 * Calculates the charge for a category.
	 *
	 * The cheapest selected item is complimentary.
	 * All other selected items are charged normally.
	 */
	private fun calculateCategoryPrice(
		selectedItems: List<MenuItem>
	): Double {

		if (selectedItems.isEmpty()) {
			return 0.00
		}

		val totalPrice = selectedItems.sumOf {
			it.price
		}

		val cheapestPrice = selectedItems.minOf {
			it.price
		}

		return totalPrice - cheapestPrice
	}

	/**
	 * Calculates the complete order total.
	 */
	private fun calculateTotal(): Double {

		// Size price
		val selectedSize = getSelectedSize()
		val sizePrice = sizes[selectedSize] ?: 0.00

		// Filling price
		val fillingPrice = calculateCategoryPrice(
			getSelectedFillings()
		)

		// Side price
		val sidePrice = calculateCategoryPrice(
			getSelectedSides()
		)

		return sizePrice + fillingPrice + sidePrice
	}

	/**
	 * Updates the Total TextView immediately whenever
	 * the user's selection changes.
	 */
	private fun updateTotal() {

		val total = calculateTotal()

		totalPrice.text = String.format(
			Locale.US,
			"RM %.2f",
			total
		)
	}

	// ==================== PLACE ORDER ====================

	/**
	 * Validates the order and displays the itemised billing.
	 */
	private fun placeOrder() {

		val selectedFillings = getSelectedFillings()

		// At least one filling is required
		if (selectedFillings.isEmpty()) {

			AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage("Please select at least one filling.")
				.setPositiveButton("OK", null)
				.show()

			return
		}

		showOrderSummary()
	}

	/**
	 * Displays the complete itemised billing in an AlertDialog.
	 */
	private fun showOrderSummary() {

		val selectedFillings = getSelectedFillings()
		val selectedSides = getSelectedSides()
		val selectedSize = getSelectedSize()

		val sizePrice = sizes[selectedSize] ?: 0.00

		/*
		 * Find the cheapest selected filling and side.
		 * These are the complimentary items.
		 */
		val freeFilling = selectedFillings.minByOrNull {
			it.price
		}

		val freeSide = selectedSides.minByOrNull {
			it.price
		}

		val fillingLines = selectedFillings.joinToString("\n") {
			val price = if (it == freeFilling) {
				0.00
			} else {
				it.price
			}

			String.format(
				Locale.US,
				"%-20s RM %.2f",
				it.name,
				price
			)
		}

		val sideLines = if (selectedSides.isEmpty()) {
			"None"
		} else {
			selectedSides.joinToString("\n") {
				val price = if (it == freeSide) {
					0.00
				} else {
					it.price
				}

				String.format(
					Locale.US,
					"%-20s RM %.2f",
					it.name,
					price
				)
			}
		}

		val total = calculateTotal()

		val message = buildString {

			append(
				String.format(
					Locale.US,
					"Size\n%-20s RM %.2f\n\n",
					selectedSize,
					sizePrice
				)
			)

			append("Filling\n")
			append(fillingLines)
			append("\n\n")

			append("Side\n")
			append(sideLines)
			append("\n\n")

			append(
				String.format(
					Locale.US,
					"Total\n%-20s RM %.2f",
					"",
					total
				)
			)
		}

		AlertDialog.Builder(this)
			.setTitle("Your Order")
			.setMessage(message)
			.setPositiveButton("OK", null)
			.show()
	}

	// ==================== RESET ====================

	/**
	 * Resets the entire order to the default state.
	 */
	private fun resetOrder() {

		// Clear all filling selections
		checkHam.isChecked = false
		checkChicken.isChecked = false
		checkBeef.isChecked = false
		checkSalmon.isChecked = false
		checkKebab.isChecked = false

		// Clear all side selections
		checkTomato.isChecked = false
		checkLettuce.isChecked = false
		checkOnion.isChecked = false
		checkCheese.isChecked = false

		// Restore default size
		radioSize.check(R.id.radio6Inch)

		// Clear latest selections
		lastSelectedFilling = null
		lastSelectedSide = null

		// Restore selection display
		fillingSelection.text = "Filling |"
		sideSelection.text = "Side |"

		// Re-enable all filling options
		enableAllFillings()

		// Restore default total
		updateTotal()
	}
}