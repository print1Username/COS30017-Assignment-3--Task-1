package com.example.cos30017assignmemt3_task1

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(),
	OrderOptionsFragment.OnOptionsSelectedListener,
	CheckoutFragment.OnCheckoutListener {

	private var selectedAddOnId = View.NO_ID

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		selectedAddOnId = savedInstanceState?.getInt("selected_add_on", View.NO_ID) ?: View.NO_ID

		findViewById<TextView>(R.id.pageOneDot).setOnClickListener {
			if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) is CheckoutFragment) {
				returnToOptions()
			}
		}
		findViewById<TextView>(R.id.pageTwoDot).setOnClickListener {
			(supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? OrderOptionsFragment)
				?.goToCheckout(requireSelection = false)
		}
		supportFragmentManager.addOnBackStackChangedListener { updatePageIndicators() }

		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction()
				.replace(R.id.fragmentContainer, OrderOptionsFragment())
				.commit()
		}
		updatePageIndicators()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putInt("selected_add_on", selectedAddOnId)
		super.onSaveInstanceState(outState)
	}

	override fun onOptionsSelected(
		fillings: ArrayList<String>,
		sides: ArrayList<String>,
		size: String
	) {
		supportFragmentManager.beginTransaction()
			.replace(
				R.id.fragmentContainer,
				CheckoutFragment.newInstance(fillings, sides, size, selectedAddOnId)
			)
			.addToBackStack(null)
			.commit()
		showPage(2)
	}

	private fun returnToOptions() {
		supportFragmentManager.popBackStackImmediate()
		showPage(1)
	}

	override fun onAddOnSelected(addOnId: Int) {
		selectedAddOnId = addOnId
	}

	override fun onResetOrder() {
		supportFragmentManager.popBackStackImmediate(
			null,
			FragmentManager.POP_BACK_STACK_INCLUSIVE
		)
		selectedAddOnId = View.NO_ID
		supportFragmentManager.beginTransaction()
			.replace(R.id.fragmentContainer, OrderOptionsFragment())
			.commit()
		showPage(1)
	}

	private fun updatePageIndicators() {
		val page = if (supportFragmentManager.findFragmentById(R.id.fragmentContainer)
			is CheckoutFragment) 2 else 1
		showPage(page)
	}

	private fun showPage(page: Int) {
		val active = ContextCompat.getColor(this, R.color.total_text)
		val inactive = ContextCompat.getColor(this, android.R.color.darker_gray)
		findViewById<TextView>(R.id.pageOneDot).setTextColor(if (page == 1) active else inactive)
		findViewById<TextView>(R.id.pageTwoDot).setTextColor(if (page == 2) active else inactive)
	}
}
