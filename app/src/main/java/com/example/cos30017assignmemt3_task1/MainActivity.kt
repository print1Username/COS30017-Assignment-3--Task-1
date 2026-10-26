package com.example.cos30017assignmemt3_task1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(),
	OrderOptionsFragment.OnOptionsSelectedListener,
	CheckoutFragment.OnResetListener {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction()
				.replace(R.id.fragmentContainer, OrderOptionsFragment())
				.commit()
		}
	}

	override fun onOptionsSelected(
		fillings: ArrayList<String>,
		sides: ArrayList<String>,
		size: String
	) {
		supportFragmentManager.beginTransaction()
			.replace(
				R.id.fragmentContainer,
				CheckoutFragment.newInstance(fillings, sides, size)
			)
			.addToBackStack(null)
			.commit()
	}

	override fun onResetOrder() {
		supportFragmentManager.popBackStackImmediate(
			null,
			FragmentManager.POP_BACK_STACK_INCLUSIVE
		)
		supportFragmentManager.beginTransaction()
			.replace(R.id.fragmentContainer, OrderOptionsFragment())
			.commit()
	}
}
