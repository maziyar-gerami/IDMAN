<?php

return [
    \App\Events\Authentication\Login::TAG => 'User Login',
    \App\Events\Authentication\Verify::TAG => 'Two Step Verification',
    \App\Events\Service\Transfer::TAG => 'Access Service :service',
    \App\Events\Authentication\ResetPassword::TAG => 'Reset Password',

    'time_format' => 'F j Y H:i'
];