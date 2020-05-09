<?php

return [
    \App\Events\Authentication\Login::TAG => 'ورود به حساب کاربری',
    \App\Events\Authentication\Register::TAG => 'ثبت نام در سیستم',
    \App\Events\Authentication\Verify::TAG => 'تایید دو مرحله ای',
    \App\Events\Service\Transfer::TAG => 'ورود به سرویس :service',
    \App\Events\Authentication\ResetPassword::TAG => 'بازیابی کلمه عبور',
    \App\Events\Settings\ChangePassword::TAG => 'تغییر کلمه عبور',
    \App\Events\Settings\RegisterMobile::TAG => 'ثبت تلفن همراه',
    \App\Events\Settings\RegisterEmail::TAG => 'ثبت ایمیل',

    'time_format' => 'j F Y ساعت H:i'
];