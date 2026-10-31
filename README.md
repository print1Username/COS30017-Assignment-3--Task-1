# COS30017 Assignment 3: Task 1
COS 30017 Software Development for Mobile Devices - Assignment 3: Task 1

[简体中文](README.zh-CN.md)

## Introduction

This Android application is a two-page sandwich ordering app developed for COS30017 Assignment 3 Task 1. It demonstrates how two fragments communicate through `MainActivity` by using interfaces. Users can select sandwich fillings, sides and a size on the first page, then choose an add-on and review the total price on the second page.

## Clone the Project

Make sure Git is installed, then run:

```powershell
git clone https://github.com/print1Username/COS30017-Assignment-3--Task-1.git
cd COS30017-Assignment-3--Task-1
```

## Android Studio Setup

1. Install Android Studio and open it.
2. Select **Open**, then choose the cloned `COS30017-Assignment-3--Task-1` folder.
3. In **Tools > SDK Manager**, install Android SDK 37. The application supports Android 7.0 (API 24) and later.
4. Keep the project Gradle JDK set to Android Studio's bundled JDK under **Settings > Build, Execution, Deployment > Build Tools > Gradle**.
5. Wait for Android Studio to download the required dependencies and complete the Gradle sync.
6. In **Tools > Device Manager**, create and start an Android emulator, or connect a physical Android device with USB debugging enabled.
7. Select the `app` run configuration and click **Run**.

## Main Features

- Uses two fragments inside the main activity.
- Passes the first-page selections to the second fragment through an interface and a `Bundle`.
- Allows up to three fillings to be selected.
- Requires at least one filling and one sandwich size when the **Next** button is used.
- Applies the cheapest selected filling and the cheapest selected side as complimentary items.
- Applies a 20% discount to the selected add-on when three fillings are selected.
- Recalculates the total when the add-on selection changes.
- Displays an itemised bill in the **Your Order** dialog.
- Resets selections on both pages with the **Reset** button.
- Provides clickable page indicators for moving between the two pages.

## Menu and Prices

### Fillings

| Item | Price (RM) |
| --- | ---: |
| Ham | 2.50 |
| Roasted Chicken | 2.00 |
| Beef Steak | 4.50 |
| Grilled Salmon | 3.70 |
| Kebab | 4.00 |

### Sides

| Item | Price (RM) |
| --- | ---: |
| Tomato | 1.00 |
| Lettuce | 1.20 |
| Onion | 0.50 |
| Cheese | 1.50 |

### Sizes

| Size | Price (RM) |
| --- | ---: |
| 6 inch | 7.00 |
| 9 inch | 9.50 |
| 12 inch | 13.00 |

### Add-ons

| Item | Price (RM) |
| --- | ---: |
| Tea | 3.50 |
| Coffee | 4.50 |
| French fries | 5.00 |

## Application Flow

1. `MainActivity` loads `OrderOptionsFragment` into the fragment container.
2. The user selects fillings, sides and a sandwich size.
3. `OrderOptionsFragment` sends the selections to `MainActivity` through `OnOptionsSelectedListener`.
4. `MainActivity` places the selections in a `Bundle` and loads `CheckoutFragment`.
5. The user may choose an add-on, view the calculated total, place the order or reset the form.
6. `CheckoutFragment` sends add-on and reset events back to `MainActivity` through `OnCheckoutListener`.

## Pricing Rules

The total is calculated from the selected sandwich size, fillings, sides and add-on. In each filling and side category, the cheapest selected item is complimentary. When exactly three fillings are selected, the chosen add-on receives a 20% discount.

## Project Structure

```text
app/src/main/
|-- java/com/example/cos30017assignmemt3_task1/
|   |-- MainActivity.kt
|   |-- OrderOptionsFragment.kt
|   `-- CheckoutFragment.kt
|-- res/layout/
|   |-- activity_main.xml
|   |-- fragment_order_options.xml
|   `-- fragment_checkout.xml
|-- res/drawable/             # Sandwich, option and button images
|-- res/color/                # Radio button colour selector
`-- res/values/               # Strings, colours and themes
```

## Key Components

- **`MainActivity`**: Hosts both fragments, implements their interfaces, controls page navigation and stores the selected add-on ID.
- **`OrderOptionsFragment`**: Handles filling, side and size selections, limits fillings to three, validates the required selections and sends the order data to the activity.
- **`CheckoutFragment`**: Handles add-on selection, price calculation, the itemised order dialog and the reset action.

## Technologies Used

- Kotlin
- Android SDK
- Android Fragments
- Interfaces and `Bundle`
- XML layouts
- AndroidX AppCompat and Material Components

## Build from the Command Line

The debug APK can also be built from the project root on Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

The generated APK will be available under `app/build/outputs/apk/debug/`.
