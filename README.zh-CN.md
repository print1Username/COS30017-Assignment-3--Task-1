# COS30017 作业 3：任务 1
COS30017 移动设备软件开发 - 作业 3：任务 1

[English](README.md)

## 项目简介

本项目是一个使用 Kotlin 开发的两页式三明治点餐 Android 应用，对应 COS30017 作业 3 的任务 1。项目展示了两个 Fragment 如何通过接口与 `MainActivity` 通信。用户可以在第一页选择三明治馅料、配菜和尺寸，然后在第二页选择附加餐点并查看总价。

## 克隆项目

请先确保电脑已安装 Git，然后运行：

```powershell
git clone https://github.com/print1Username/COS30017-Assignment-3--Task-1.git
cd COS30017-Assignment-3--Task-1
```

## Android Studio 配置

1. 安装并打开 Android Studio。
2. 选择 **Open**，然后选择克隆下来的 `COS30017-Assignment-3--Task-1` 文件夹。
3. 前往 **Tools > SDK Manager** 安装 Android SDK 37。本应用支持 Android 7.0（API 24）及以上版本。
4. 前往 **Settings > Build, Execution, Deployment > Build Tools > Gradle**，将项目的 Gradle JDK 保持为 Android Studio 内置的 JDK。
5. 等待 Android Studio 下载所需依赖并完成 Gradle 同步。
6. 前往 **Tools > Device Manager** 创建并启动 Android 模拟器，或连接已启用 USB 调试的 Android 实体设备。
7. 选择 `app` 运行配置，然后点击 **Run**。

## 主要功能

- 在 Main Activity 中使用两个 Fragment。
- 通过接口和 `Bundle` 将第一页的选择传递到第二个 Fragment。
- 最多可以选择三种馅料。
- 使用 **Next** 按钮时，必须至少选择一种馅料和一个三明治尺寸。
- 每个类别中价格最低的已选馅料和配菜免费。
- 选择三种馅料时，附加餐点可享受 20% 折扣。
- 更改附加餐点后自动重新计算总价。
- 在 **Your Order** 对话框中显示分项订单明细。
- 使用 **Reset** 按钮重置两个页面的选择。
- 可通过底部的页面圆点在两个页面之间切换。

## 菜单与价格

### 馅料

| 项目 | 价格（RM） |
| --- | ---: |
| 火腿（Ham） | 2.50 |
| 烤鸡肉（Roasted Chicken） | 2.00 |
| 牛排（Beef Steak） | 4.50 |
| 烤三文鱼（Grilled Salmon） | 3.70 |
| 烤肉串（Kebab） | 4.00 |

### 配菜

| 项目 | 价格（RM） |
| --- | ---: |
| 番茄（Tomato） | 1.00 |
| 生菜（Lettuce） | 1.20 |
| 洋葱（Onion） | 0.50 |
| 芝士（Cheese） | 1.50 |

### 尺寸

| 尺寸 | 价格（RM） |
| --- | ---: |
| 6 英寸 | 7.00 |
| 9 英寸 | 9.50 |
| 12 英寸 | 13.00 |

### 附加餐点

| 项目 | 价格（RM） |
| --- | ---: |
| 茶（Tea） | 3.50 |
| 咖啡（Coffee） | 4.50 |
| 炸薯条（French fries） | 5.00 |

## 应用流程

1. `MainActivity` 将 `OrderOptionsFragment` 加载到 Fragment 容器中。
2. 用户选择馅料、配菜和三明治尺寸。
3. `OrderOptionsFragment` 通过 `OnOptionsSelectedListener` 将选择传递给 `MainActivity`。
4. `MainActivity` 将选择放入 `Bundle`，然后加载 `CheckoutFragment`。
5. 用户可以选择附加餐点、查看总价、提交订单或重置表单。
6. `CheckoutFragment` 通过 `OnCheckoutListener` 将附加餐点和重置事件传回 `MainActivity`。

## 价格计算规则

总价由所选三明治尺寸、馅料、配菜和附加餐点共同计算。在馅料和配菜两个类别中，价格最低的已选项目免费。当用户正好选择三种馅料时，所选附加餐点可享受 20% 折扣。

## 项目结构

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
|-- res/drawable/             # 三明治、选项和按钮图片
|-- res/color/                # RadioButton 颜色选择器
`-- res/values/               # 字符串、颜色和主题
```

## 主要组件

- **`MainActivity`**：承载两个 Fragment、实现它们的接口、控制页面导航，并保存所选附加餐点的 ID。
- **`OrderOptionsFragment`**：处理馅料、配菜和尺寸选择，将馅料限制为最多三种，验证必选内容并将订单数据传递给 Activity。
- **`CheckoutFragment`**：处理附加餐点选择、价格计算、分项订单对话框和重置操作。

## 使用的技术

- Kotlin
- Android SDK
- Android Fragment
- 接口和 `Bundle`
- XML 布局
- AndroidX AppCompat 和 Material Components

## 使用命令行构建

也可以在 Windows 的项目根目录中使用以下命令构建 Debug APK：

```powershell
.\gradlew.bat :app:assembleDebug
```

生成的 APK 位于 `app/build/outputs/apk/debug/` 目录中。
