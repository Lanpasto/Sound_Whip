Ця програма реалізує функціональність обробки жести "трясіння" пристрою та відтворення звукових ефектів та анімацій при трясінні.
Основні компоненти програми:
ShakeHandler: Клас, що відповідає за обробку трясіння пристрою. Він реалізує інтерфейс ShakeDetector.Listener, який містить методи onShakeStart(), onShake(), onShakeStop().
Клас має методи для реєстрації та відміни реєстрації слухача трясіння (register(), unregister()). При трясінні пристрою викликаються відповідні методи, 
відтворюються звукові ефекти (playSoundShakeWhip(), playSoundShakePlash(), playSoundShakeLong()) та виконується обертання зображення (imageRotator.rotate()).
ImageRotator: Клас, який відповідає за обертання зображення. 
Він має метод rotate(), який запускає анімацію обертання зображення.
Анімація виконується за допомогою об'єкта Animation та AnimationListener для відслідковування початку, закінчення та повторення анімації.
ShakeDetector: Клас, що відповідає за виявлення трясіння пристрою на основі даних сенсора акселерометра. 
Він реалізує інтерфейс SensorEventListener та має методи onSensorChanged() та onAccuracyChanged(). 
Клас оцінює дані сенсора та визначає, чи відбувається трясіння, та сповіщає про це через методи інтерфейсу Listener. Клас також має методи register() та unregister() для реєстрації та відміни реєстрації слухача сенсора акселерометра.
MainActivity: Головна активність програми, яка ініціалізує компоненти і встановлює обробники подій. 
Вона має посилання на ShakeHandler та ImageRotator і викликає їх методи для реєстрації та відміни реєстрації. 
В активності також встановлено обробник події натискання на кнопку, який викликає методи playSoundButton() та rotate() у ShakeHandler та ImageRotator відповідно.






![XRecorder_01072023_151233 (online-video-cutter com) (1) (1)](https://github.com/Lanpasto/Sound_Whip/assets/77079137/19188f61-1c27-49dc-ae54-88480eb3b1e3)
