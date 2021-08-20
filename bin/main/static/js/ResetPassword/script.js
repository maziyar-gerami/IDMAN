document.addEventListener('DOMContentLoaded', function () {
    var router = new VueRouter({
        mode: 'history',
        routes: []
    });
    new Vue({
        router,
        el: '#app',
        data: {
            dateNav: "",
            dateNavEn: "",
            dateNavText: "",
            userInfo: [],
            userId: "",
            userIdSMSCode: "",
            email: "",
            emailE: "",
            mobile: "",
            mobileS: "",
            codeSMS: "",
            username: "",
            usernameE: "",
            usernameS: "",
            usernameSMSCheck: "",
            name: "",
            nameEN: "",
            users: [],
            message: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            sendUserId: true,
            SuccessUserId: false,
            ErrorUserId: false,
            ErrorUserIdSMSCode: false,
            ErrorUserIdSMSCode403: false,
            ErrorUserIdSMSCode408: false,
            sendE: true,
            sendU: false,
            Success: false,
            Error: false,
            sendS: true,
            sendUS: false,
            checkSMSCode: false,
            SuccessS: false,
            ErrorS: false,
            ErrorSMSCode: false,
            ErrorSMSCode403: false,
            ErrorSMSCode408: false,
            editS: "display:none",
            addS: "display:none",
            showS: "",
            activeItem: "userId",
            captcha: "",
            captchaId: "",
            capAnswer: "",
            loader: false,
            s0: "پارسو",
            s1: "",
            s2: "خروج",
            s3: "بازنشانی رمز عبور",
            s4: "سرویس ها",
            s5: "گروه ها",
            s6: "رویداد ها",
            s7: "پروفایل",
            s9: "fa fa-arrow-right",
            s10: "قوانین",
            s11: "حریم خصوصی",
            s12: "راهنما",
            s13: "کاربران",
            s14: "./dashboard",
            s15: "./services",
            s16: "./users",
            s17: "بازگشت",
            s18: "تایید",
            s19: "ایمیل",
            s20: "داشبورد",
            s21: "./groups",
            s22: "./profile",
            s23: "لطفا جهت بازنشانی رمز عبور، آدرس ایمیل خود را وارد کنید:",
            s24: "ارسال موفقیت آمیز بود.",
            s25: "لطفا به mailbox خود مراجعه نموده و طبق راهنما، باقی مراحل را انجام دهید.",
            s26: "بازگشت به صفحه ورود",
            s27: "اطلاعات شما در پایگاه داده ما وجود ندارد.",
            s28: "لطفا با دقت بیشتری نسبت به ورود اطلاعات خود اقدام نمایید.",
            s29: "لطفا جهت تکمیل مراحل، شناسه کاربری خود را وارد نمایید:",
            s30: "لطفا جهت بازنشانی رمز عبور، شماره موبایل خود را وارد کنید:",
            s31: "لطفا به لیست پیامک های دریافتی خود مراجعه نموده و کد ارسال شده را وارد نمایید.",
            s32: "اطلاعات شما در پایگاه داده ما وجود ندارد.",
            s33: "لطفا با دقت بیشتری نسبت به ورود اطلاعات خود اقدام نمایید.",
            s34: "شماره موبایل",
            s35: "شما در حال انتقال به صفحه بازنشانی رمز عبور هستید...",
            s36: "شناسه کاربری",
            s37: "آدرس ایمیل خود را وارد کنید",
            s38: "فرمت آدرس ایمیل را به درستی وارد کنید",
            s39: "کد وارد شده منقضی شده است، لطفا دوباره تلاش کنید.",
            s40: "کد وارد شده اشتباه است.",
            s41: "شناسه کاربری خود را وارد کنید",
            s42: "شماره موبایل خود را وارد کنید",
            s43: "کد پیامک شده خود را وارد کنید",
            s44: "جواب کد امنیتی نمی تواند خالی باشد.",
            s45: "جواب کد امنیتی اشتباه است، دوباره تلاش کنید.",
            s46: "کد امنیتی",
            mobileFormatErrorText: "فرمت شماره تلفن را به درستی وارد کنید",
            tryAgainText: "تلاش دوباره",
            personnelNumberText: "شماره پرسنلی",
            enterPersonnelNumberText: "لطفا جهت بازنشانی رمز عبور، شماره پرسنلی خود را وارد کنید:",
            emptyPersonnelNumberText: "شماره پرسنلی خود را وارد کنید",
            codeNot6DigitsText: "کد وارد شده 6 رقم نمی باشد.",
        },
        created: function () {
            this.setDateNav();
            this.getCaptcha();
            if(window.localStorage.getItem("lang") === null){
                window.localStorage.setItem("lang", "FA");
            }else if(window.localStorage.getItem("lang") === "EN") {
                this.changeLang();
            }
        },
        methods: {
            setDateNav: function () {
                this.dateNav = new persianDate().format("dddd، DD MMMM YYYY");
                persianDate.toCalendar("gregorian");
                persianDate.toLocale("en");
                this.dateNavEn = new persianDate().format("dddd, DD MMMM YYYY");
                persianDate.toCalendar("persian");
                persianDate.toLocale("fa");
                this.dateNavText = this.dateNav;
            },
            getCaptcha: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                
                axios.get(url + "/api/captcha/request") //
                .then((res) => {
                    vm.captcha = "data:image/png;base64," + res.data.img;
                    vm.captchaId = res.data.id;
                    vm.capAnswer = "";
                })
            },
            isActive (menuItem) {
                return this.activeItem === menuItem;
            },
            setActive (menuItem) {
                this.activeItem = menuItem;
                this.getCaptcha();
            },
            sendEmail: function (email) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                const emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                if(email !== ""){
                    if(vm.capAnswer !== ""){
                        if(emailRegex.test(email)){
                            vm.loader = true;
                            axios.get(url + "/api/public/sendMail/" + email + "/" + vm.captchaId + "/" + vm.capAnswer) //
                                .then((res) => {
                                    vm.loader = false;
                                    vm.sendE = false;
                                    vm.sendU = false;
                                    vm.Success = true;
                                    vm.Error = false;
                                }).catch((error) => {
                                if (error.response) {
                                    vm.loader = false;
                                    if(error.response.status === 302){
                                        vm.usernameE = "";
                                        vm.sendE = false;
                                        vm.sendU = true;
                                        vm.Success = false;
                                        vm.Error = false;
                                    }else if(error.response.status === 403){
                                        alert(vm.s45);
                                        vm.getCaptcha();
                                    }else if(error.response.status === 404){
                                        vm.sendE = false;
                                        vm.sendU = false;
                                        vm.Success = false;
                                        vm.Error = true;
                                    }
                                }
                            });
                        }else{
                            alert(this.s38);
                        }
                    }else{
                        alert(this.s44);
                    }
                }else{
                    alert(this.s37);
                }
            },
            sendEmailUser: function (email, username) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                if(username !== ""){
                    this.loader = true;
                    axios.get(url + "/api/public/sendMail/" + email + "/" + username + "/" + vm.captchaId + "/" + vm.capAnswer) //
                        .then((res) => {
                            vm.loader = false;
                            vm.sendE = false;
                            vm.sendU = false;
                            vm.Success = true;
                            vm.Error = false;
                        }).catch((error) => {
                            if (error.response) {
                                vm.loader = false;
                                if(error.response.status === 403){
                                    vm.emailE = "";
                                    vm.usernameE = "";
                                    vm.sendE = true;
                                    vm.sendU = false;
                                    vm.Success = false;
                                    vm.Error = false;
                                    alert(vm.s45);
                                    vm.getCaptcha();
                                }else if(error.response.status === 404){
                                    vm.sendE = false;
                                    vm.sendU = false;
                                    vm.Success = false;
                                    vm.Error = true;
                                }
                            }
                        });
                }else{
                    alert(this.s41);
                }
            },
            sendSMS: function (mobile) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                const mobileRegex = /^09\d{9}$/;
                if(mobile !== ""){
                    if(vm.capAnswer !== ""){
                        if(mobileRegex.test(mobile)){
                            vm.loader = true;
                            axios.get(url + "/api/public/sendSMS/" + mobile + "/" + vm.captchaId + "/" + vm.capAnswer) //
                                .then((res) => {
                                    vm.usernameSMSCheck = res.data.userId;
                                    vm.loader = false;
                                    vm.sendS = false;
                                    vm.sendUS = false;
                                    vm.checkSMSCode = true;
                                    vm.SuccessS = false;
                                    vm.ErrorS = false;
                                    vm.ErrorSMSCode = false;
                                }).catch((error) => {
                                if (error.response) {
                                    vm.loader = false;
                                    if(error.response.status === 302){
                                        vm.usernameS = "";
                                        vm.sendS = false;
                                        vm.sendUS = true;
                                        vm.checkSMSCode = false;
                                        vm.SuccessS = false;
                                        vm.ErrorS = false;
                                        vm.ErrorSMSCode = false;
                                    }else if(error.response.status === 403){
                                        alert(vm.s45);
                                        vm.getCaptcha();
                                    }else if(error.response.status === 404){
                                        vm.sendS = false;
                                        vm.sendUS = false;
                                        vm.checkSMSCode = false;
                                        vm.SuccessS = false;
                                        vm.ErrorS = true;
                                        vm.ErrorSMSCode = false;
                                    }
                                }
                            });
                        }else{
                            alert(this.mobileFormatErrorText);
                        }
                    }else{
                        alert(this.s44);
                    }
                }else{
                    alert(this.s42);
                }
            },
            sendSMSUser: function (mobile, username) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                if(username !== ""){
                    vm.loader = true;
                    axios.get(url + "/api/public/sendSMS/" + mobile + "/" + username + "/" + vm.captchaId + "/" + vm.capAnswer) //
                        .then((res) => {
                            vm.usernameSMSCheck = res.data.userId;
                            vm.loader = false;
                            vm.codeSMS = "";
                            vm.sendS = false;
                            vm.sendUS = false;
                            vm.checkSMSCode = true;
                            vm.SuccessS = false;
                            vm.ErrorS = false;
                            vm.ErrorSMSCode = false;
                        }).catch((error) => {
                        if (error.response) {
                            vm.loader = false;
                            if(error.response.status === 403){
                                vm.mobile = "";
                                vm.usernameS = "";
                                vm.sendS = true;
                                vm.sendUS = false;
                                vm.checkSMSCode = false;
                                vm.SuccessS = false;
                                vm.ErrorS = false;
                                vm.ErrorSMSCode = false;
                                alert(vm.s45);
                                vm.getCaptcha();
                            }else if(error.response.status === 404){
                                vm.sendS = false;
                                vm.sendUS = false;
                                vm.checkSMSCode = false;
                                vm.SuccessS = false;
                                vm.ErrorS = true;
                                vm.ErrorSMSCode = false;
                            }
                        }
                    });
                }else{
                    alert(this.s41);
                }
            },
            checkSMS: function (code) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;

                code = document.getElementById("digit-1-1").value.trim() + document.getElementById("digit-2-1").value.trim() +
                    document.getElementById("digit-3-1").value.trim() + document.getElementById("digit-4-1").value.trim() +
                    document.getElementById("digit-5-1").value.trim() + document.getElementById("digit-6-1").value.trim();
                if(code !== ""){
                    if(code.length === 6) {
                        vm.loader = true;
                        axios.get(url + "/api/public/validateMessageToken/" + vm.usernameSMSCheck + "/" + code) //
                            .then((res) => {
                                vm.loader = false;
                                vm.sendS = false;
                                vm.sendUS = false;
                                vm.checkSMSCode = false;
                                vm.SuccessS = true;
                                vm.ErrorS = false;
                                vm.ErrorSMSCode = false;
                                window.location.replace(url + "/newpassword?uid=" + vm.usernameSMSCheck + "&token=" + code);
                            })
                            .catch((error) => {
                                if (error.response) {
                                    vm.loader = false;
                                    vm.sendS = false;
                                    vm.sendUS = false;
                                    vm.checkSMSCode = false;
                                    vm.SuccessS = false;
                                    vm.ErrorS = false;
                                    vm.ErrorSMSCode = true;
                                    if (error.response.status === 403) {
                                        vm.ErrorSMSCode403 = true;
                                        vm.ErrorSMSCode408 = false;
                                    } else if (error.response.status === 408) {
                                        vm.ErrorSMSCode403 = false;
                                        vm.ErrorSMSCode408 = true;
                                    }
                                }
                            });
                    }else{
                        alert(this.codeNot6DigitsText);
                    }
                }else{
                    alert(this.s43);
                }
            },
            sendUID: function (uid) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                if(uid !== ""){
                    if(vm.capAnswer !== ""){
                        vm.loader = true;
                        axios.get(url + "/api/public/sendTokenUser/" + uid + "/" + vm.captchaId + "/" + vm.capAnswer) //
                            .then((res) => {
                                vm.loader = false;
                                vm.sendUserId = false;
                                vm.SuccessUserId = true;
                                vm.ErrorUserId = false;
                                vm.ErrorUserIdSMSCode = false;
                            }).catch((error) => {
                                if (error.response) {
                                    vm.loader = false;
                                    if(error.response.status === 404){
                                        vm.userId = "";
                                        vm.sendUserId = false;
                                        vm.SuccessUserId = false;
                                        vm.ErrorUserId = true;
                                        vm.ErrorUserIdSMSCode = false;
                                    }
                                }
                            });
                    }else{
                        alert(this.s44);
                    }
                }else{
                    alert(this.emptyPersonnelNumberText);
                }
            },
            checkUIDSMS: function (code) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;

                code = document.getElementById("digit-1").value.trim() + document.getElementById("digit-2").value.trim() +
                    document.getElementById("digit-3").value.trim() + document.getElementById("digit-4").value.trim() +
                    document.getElementById("digit-5").value.trim() + document.getElementById("digit-6").value.trim();
                if(code !== ""){
                    if(code.length === 6) {
                        vm.loader = true;
                        axios.get(url + "/api/public/validateMessageToken/" + vm.userId + "/" + code) //
                            .then((res) => {
                                vm.loader = false;
                                window.location.replace(url + "/newpassword?uid=" + vm.userId + "&token=" + code);
                            })
                            .catch((error) => {
                                if (error.response) {
                                    vm.loader = false;
                                    vm.sendUserId = false;
                                    vm.SuccessUserId = false;
                                    vm.ErrorUserId = false;
                                    vm.ErrorUserIdSMSCode = true;
                                    if (error.response.status === 403) {
                                        vm.ErrorUserIdSMSCode403 = true;
                                        vm.ErrorUserIdSMSCode408 = false;
                                    } else if (error.response.status === 408) {
                                        vm.ErrorUserIdSMSCode403 = false;
                                        vm.ErrorUserIdSMSCode408 = true;
                                    }
                                }
                            });
                    }else{
                        alert(this.codeNot6DigitsText);
                    }
                }else{
                    alert(this.s43);
                }
            },
            tryAgain: function (option) {
                if(option === "email"){
                    this.emailE = "";
                    this.usernameE = "";
                    this.sendE = true;
                    this.sendU = false;
                    this.Success = false;
                    this.Error = false;
                }else if(option === "sms"){
                    this.mobile = "";
                    this.usernameS = "";
                    this.codeSMS = "";
                    this.sendS = true;
                    this.sendUS = false;
                    this.checkSMSCode = false;
                    this.SuccessS = false;
                    this.ErrorS = false;
                    this.ErrorSMSCode = false;
                }
                else if(option === "userId"){
                    this.userId = "";
                    this.sendUserId = true;
                    this.SuccessUserId = false;
                    this.ErrorUserId = false;
                    this.ErrorUserIdSMSCode = false;
                }
                this.getCaptcha();
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    window.localStorage.setItem("lang", "EN");
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.dateNavText = this.dateNavEn;
                    this.s0 = "Parsso";
                    this.s1 = this.nameEN;
                    this.s2 = "Exit";
                    this.s3 = "Reset Password";
                    this.s4 = "Services";
                    this.s5 = "Groups";
                    this.s6 = "Events";
                    this.s7 = "Profile";
                    this.s9 = "fa fa-arrow-left";
                    this.s10 = "Rules";
                    this.s11 = "Privacy";
                    this.s12 = "Guide";
                    this.s13 = "Users";
                    this.s17 = "Go Back";
                    this.s18 = "Submit";
                    this.s19 = "Email";
                    this.s20 = "Dashboard";
                    this.s23 = "Please Enter Your Email Address To Reset Your Password:";
                    this.s24 = "Submission Was Successful.";
                    this.s25 = "Please Go To Your mailbox And Follow The Instructions.";
                    this.s26 = "Return To The Login Page";
                    this.s27 = "There Is No Such Information In Our Database.";
                    this.s28 = "Please Enter Your Information More Carefully.";
                    this.s29 = "Please Enter Your ID To Complete The Process:";
                    this.s30 = "Please Enter Your Mobile Number To Reset Your Password:";
                    this.s31 = "Please Go To Your SMS Inbox And Enter The Code We Sent You.";
                    this.s32 = "There Is No Such Information In Our Database.";
                    this.s33 = "Please Enter Your Information More Carefully.";
                    this.s34 = "Mobile Number";
                    this.s35 = "We Are Redirecting You To Password Reset Page...";
                    this.s36= "ID";
                    this.s37 = "Enter Your Email Address";
                    this.s38 = "Enter Email Address Format Correctly";
                    this.s44 = "Security Code Answer Cannot Be Empty.";
                    this.s45 = "Security Code Answer Is Incorrect, Try Again.";
                    this.s46 = "Security Code";
                    this.mobileFormatErrorText = "Enter Phone Number Format Correctly";
                    this.tryAgainText = "Try Again";
                    this.personnelNumberText = "Personnel Number";
                    this.enterPersonnelNumberText = "Please Enter Your Personnel Number To Reset Your Password:";
                    this.emptyPersonnelNumberText = "Please Enter Your Personnel Number";
                    this.codeNot6DigitsText = "Entered Code is Not 6 Digits.";
                } else{
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.dateNavText = this.dateNav;
                    this.s0 = "پارسو";
                    this.s1 = this.name;
                    this.s2 = "خروج";
                    this.s3 = "بازنشانی رمز عبور";
                    this.s4 = "سرویس ها";
                    this.s5 = "گروه ها";
                    this.s6 = "رویداد ها";
                    this.s7 = "پروفایل";
                    this.s9 = "fa fa-arrow-right";
                    this.s10 = "قوانین";
                    this.s11 = "حریم خصوصی";
                    this.s12 = "راهنما";
                    this.s13 = "کاربران";
                    this.s17 = "بازگشت";
                    this.s18 = "تایید";
                    this.s19 = "ایمیل";
                    this.s20 = "داشبورد";
                    this.s23 = "لطفا جهت بازنشانی رمز عبور، آدرس ایمیل خود را وارد کنید:";
                    this.s24 = "ارسال موفقیت آمیز بود.";
                    this.s25 = "لطفا به mailbox خود مراجعه نموده و طبق راهنما، باقی مراحل را انجام دهید.";
                    this.s26 = "بازگشت به صفحه ورود";
                    this.s27 = "اطلاعات شما در پایگاه داده ما وجود ندارد.";
                    this.s28 = "لطفا با دقت بیشتری نسبت به ورود اطلاعات خود اقدام نمایید.";
                    this.s29 = "لطفا جهت تکمیل مراحل، شناسه کاربری خود را وارد نمایید:";
                    this.s30 = "لطفا جهت بازنشانی رمز عبور، شماره موبایل خود را وارد کنید:";
                    this.s31 = "لطفا به لیست پیامک های دریافتی خود مراجعه نموده و کد ارسال شده را وارد نمایید.";
                    this.s32 = "اطلاعات شما در پایگاه داده ما وجود ندارد.";
                    this.s33 = "لطفا با دقت بیشتری نسبت به ورود اطلاعات خود اقدام نمایید.";
                    this.s34 = "شماره موبایل";
                    this.s35 = "شما در حال انتقال به صفحه بازنشانی رمز عبور هستید...";
                    this.s36 = "شناسه کاربری";
                    this.s37 = "آدرس ایمیل خود را وارد کنید";
                    this.s38 = "فرمت آدرس ایمیل را به درستی وارد کنید";
                    this.s44 = "جواب کد امنیتی نمی تواند خالی باشد.";
                    this.s45 = "جواب کد امنیتی اشتباه است، دوباره تلاش کنید.";
                    this.s46 = "کد امنیتی";
                    this.mobileFormatErrorText = "فرمت شماره تلفن را به درستی وارد کنید";
                    this.tryAgainText = "تلاش دوباره";
                    this.personnelNumberText = "شماره پرسنلی";
                    this.enterPersonnelNumberText = "لطفا جهت بازنشانی رمز عبور، شماره پرسنلی خود را وارد کنید:";
                    this.emptyPersonnelNumberText = "شماره پرسنلی خود را وارد کنید";
                    this.codeNot6DigitsText = "کد وارد شده 6 رقم نمی باشد.";
                }
            }
        }
    })
})
