Читанка за Kindle
=================

[![Build Status](https://travis-ci.org/kaloyan-raev/chitanka4kindle.svg?branch=master)](https://travis-ci.org/kaloyan-raev/chitanka4kindle)

Приложение за електронната книга Kindle, което позволява да се разглежда каталога на [Читанка](http://chitanka.info/) и да се свалят книги директно на устройството без помощта на компютър и кабел.

Връзката между Kindle и сайта на Читанка се осъществява през Wi-Fi.

[![Читанка за Kindle](http://img.youtube.com/vi/Ym1GvY1qjwg/0.jpg)](http://www.youtube.com/watch?v=Ym1GvY1qjwg)

Приложението работи само на следните устройства:
- Kindle Keyboard (познат още като Kindle 3)
- Kindle 4
- Kindle 5 (познат още като черния Kindle 4)

Не работи на устройства със сензорен екран като Kindle Touch, Kindle Paperwhite и по-нови.

Функционалност
==============

Към момента приложението предоставя следните възможности:
* Стартиране на приложението от главния екран на Kindle
* Разглеждане на каталога на Читанка: по автори, категории, поредици и т.н.
* Сваляне на избраната книга директно на Kindle в MOBI формат, т.е. не се налага допълнително конвертиране

В следващите версии може да се очаква:
* Търсене на книги по ключови думи
* По-изчерпателна информация за разглежданите книги
* Подобрена навигация в каталога

Инсталация
==========

За разлика от други устройства, Kindle е от затворен тип и е предвидено софтуерът му да се обновява и надгражда единствено от Amazon. За щастие е открит начин да се заобиколи тази забрана. За целта е необходимо устройството да се подготви чрез изпълнение на няколко модификации. Необходимо да разполагате с компютър и USB кабел, с който да го свържете към Kindle. 

Към настоящия момент следващите инструкции са тествани успешно от автора на проекта на устройствата Kindle Keyboard и Kindle 4. Приложението **не работи** на моделите Kindle Paperwhite. Тези модели не разполагат с хардуерни бутони, навигацията е изцяло с докосвания, програмният интерфейс също е значително променен и се налага създаване на напълно ново приложение.

В случай на затруднения, моля [отворете проблем](https://github.com/kaloyan-raev/chitanka4kindle/issues/new) към проекта.

Регистрация в Amazon
--------------------

Преди да пристъпите към следващите стъпки, уверете се, че устройството е регистрирано в Amazon. Това може да направите от меню Settings (Настройки).

Регистрацията е необходима за правилното функциониране на приложенията, които не са разработени от Amazon (като приложението Читанка). Тези приложения са подписани с цифров сертификат, който се инсталира на устройството като част от инсталационната процедура. Нерегистираните устройства автоматично изтриват всички такива сертификати по време на стартиране и приложенията не могат да работят.

Приложението Читанка няма шанс да работи на устройства, които не може да се регистрира в Amazon.

Отключване (jailbreak)
----------------------

Тази стъпка позволява инсталирането на софтуер, който не е разработен от производителя на устройството. Отключването е абсолютно необходимо за успешното изпълнение на инсталационния пакет на приложението.

- [Инструкции за Kindle Keyboard (Kindle 3)](https://hitrini.blogspot.bg/2017/03/jailbreak-kindle-3.html)
- [Инструкции за Kindle 4 и 5](https://hitrini.blogspot.bg/2017/03/jailbreak-kindle-4-5.html)

Инсталиране на приложението
---------------------------

1. Използвайте компютъра, за да изтеглите файла с инсталационния пакет от [последната версия](https://github.com/kaloyan-raev/chitanka4kindle/releases/latest) на приложението. Трябва да изберете правилния инсталационен пакет според модела на Вашия Kindle:
  - _update_chitanka_k3w_install.bin_ за Kindle Keyboard Wi-Fi (сериен номер, започващ с **B008**);
  - _update_chitanka_k3g_install.bin_ за Kindle Keyboard Wi-Fi + 3G (сериен номер, започващ с **B006**)
  - _update_chitanka_k3gb_install.bin_ за Kindle Keyboard Wi-Fi + 3G (сериен номер, започващ с **B00A**)
  - _update_chitanka_k4x_install.bin_ за Kindle 4 и 5.
2. Свържете Kindle към компютъра чрез USB кабела. Изчакайте Kindle да се разпознае като USB диск на компютъра.
3. Копирайте файла с инсталационния пакет в главната папка на Kindle.
4. Извадете Kindle като USB диск от компютъра, т.нар. "safe remove".
5. Извадете USB кабела от Kindle.
6. Обновете устройството като изберете _Menu -> Settings -> Menu -> Update Your Kindle_. Ако вече сте инсталирали [локализацията на български език](http://hitrini.blogspot.bg/2017/03/bg-l10n-kindle-4-5.html), трябва да изберете съответно _Меню -> Настройки -> Меню -> Обновяване на Kindle_.
7. Изчакайте обновяването да завърши и устройството да се рестартира.
8. Ще намерите приложението "Читанка" най-горе на главния екран на Kindle.

Обновяване на приложението
--------------------------

Има два начина да обновите приложението до по-нова версия:
1. Да изтеглите инсталационен пакет с по-нова версия и да обновите устройството както е описано по-горе.
2. Да изтеглите само файла _chitanka.azw2_ oт [последната версия](https://github.com/kaloyan-raev/chitanka4kindle/releases/latest) на приложението и да го копирате в папката documents на Kindle като го запишете върху старата версия на файла.

Лиценз
======

Читанка за Kindle е безплатен и свободен софтуер. Разпространява се под лиценза GNU General Public License, версия 3. Пълният текст на лиценза може да видите във файла [LICENSE](https://github.com/kaloyan-raev/chitanka4kindle/blob/master/LICENSE).
