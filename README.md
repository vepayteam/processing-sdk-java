# MoneyTransfer SDK

MoneyTransfer SDK позволяет интегрировать SDK в мобильные приложения для проведения оплаты в
платформе Android.

### Требования

Для работы MoneyTransfer SDK необходим Android версии 7.0 и выше (API level 24).

### Подключение

Для подключения SDK добавьте в [_build.gradle_][build-config] вашего проекта следующие зависимости:

```groovy
implementation 'io.finbridge.vepay.*:$latestVersion'
```

``` settings.gradle
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

### Подготовка к работе

Для начала работы с SDK вам понадобятся:

* UUID оплаты
* X-user id-пользователя в системе оплаты

### Пример работы

Для проведения оплаты необходимо создать registerForActivityResultContract используя Activity Result
API (https://developer.android.com/training/basics/intents/result).
Пример можно увидеть в модуле demo. Входными параметрами являются:

* UUID платежа формата String.
* X-user id-пользователя формата String

Выходным параметром является TransferStatus(Enum класс со следующим наполением:

~~~
WAITING,
DONE,
ERROR,
CANCEL,
NOT_EXEC,
WAITING_CHECK_STATUS,
REFUND_DONE
~~~
).

### Для новых клиентов(получение X-user)

Оставьте заявку на сайте https://vepay.online/

### Предполагаемый функционал будущих релизов

* Добавление и сохранение карт
* Распознавание карт с помощью камеры или NFC

Вся информация и исходный код предоставляются в исходном виде, без
явно выраженных или подразумеваемых гарантий. Использование исходного
кода или его части осуществляются исключительно по вашему усмотрению
и на ваш риск. Компания ООО “Мобильные платежи” принимает разумные меры для
обеспечения актуальности информации, размещенной в данном репозитории,
но она не принимает на себя ответственности за поддержку или актуализацию
данного репозитория или его частей вне рамок, устанавливаемых компанией
самостоятельно и без уведомления третьих лиц.
