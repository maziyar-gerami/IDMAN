function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; ++i) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}
document.addEventListener('DOMContentLoaded', function () {
    var router = new VueRouter({
        mode: 'history',
        routes: []
    });
    new Vue({
        router,
        el: '#app',
        data: {
            userInfo: [],
            email: "",
            emailE: "",
            emailEE: "",
            mobile: "",
            mobileS: "",
            codeSMS: "",
            username: "",
            usernameE: "",
            usernameS: "",
            name: "",
            nameEN: "",
            users: [],
            emails: [],
            message: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
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
            activeItem: "email",
            captcha: "",
            captchaId: "",
            capAnswer: "",
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
            s22: "./settings",
            s23: "لطفا جهت بازنشانی رمز عبور، آدرس ایمیل خود را وارد کنید:",
            s24: "ارسال موفقیت آمیز بود.",
            s25: "لطفا به mailbox خود مراجعه نموده و طبق راهنما، باقی مراحل را انجام دهید.",
            s26: "بازگشت به صفحه ورود",
            s27: "ایمیل وارد شده در پایگاه داده ما وجود ندارد.",
            s28: "لطفا با دقت بیشتری نسبت به ورود آدرس ایمیل اقدام نمایید:",
            s29: "لطفا جهت تکمیل مراحل، شناسه خود را وارد نمایید:",
            s30: "لطفا جهت بازنشانی رمز عبور، شماره موبایل خود را وارد کنید:",
            s31: "لطفا به لیست پیامک های دریافتی خود مراجعه نموده و کد ارسال شده را وارد نمایید.",
            s32: "شماره موبایل وارد شده در پایگاه داده ما وجود ندارد.",
            s33: "لطفا با دقت بیشتری نسبت به ورود شماره موبایل اقدام نمایید:",
            s34: "شماره موبایل",
            s35: "شما در حال انتقال به صفحه بازنشانی رمز عبور هستید...",
            s36: "شناسه",
            s37: "آدرس ایمیل خود را وارد کنید",
            s38: "فرمت آدرس ایمیل را به درستی وارد کنید",
            s39: "کد وارد شده منقضی شده است، لطفا دوباره تلاش کنید.",
            s40: "کد وارد شده اشتباه است.",
            s41: "شناسه خود را وارد کنید",
            s42: "شماره موبایل خود را وارد کنید",
            s43: "کد پیامک شده خود را وارد کنید",
            s44: "جواب CAPTCHA نمی تواند خالی باشد.",
            s45: "جواب CAPTCHA اشتباه است، دوباره تلاش کنید."
        },
        created: function () {
            this.getCaptcha();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
            getCaptcha: function  () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                
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
            sendEmail: function  (email) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                const emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                if(email != ""){
                    if(emailRegex.test(email)){
                        if(vm.capAnswer != ""){
                            axios.get(url + "/api/public/checkMail/" + email) //
                                .then((res) => {
                                    vm.emails = res.data;
                                    if(vm.emails.length == 0){
                                        vm.emailEE = "";
                                        vm.sendE = false;
                                        vm.sendU = false;
                                        vm.Success = false;
                                        vm.Error = true;
                                        vm.getCaptcha();
                                    } else if(vm.emails.length == 1){
                                         axios.get(url + "/api/public/sendMail/" + email + "/" + vm.captchaId + "/" + vm.capAnswer) //
                                            .then((res) => {
                                                vm.sendE = false;
                                                vm.sendU = false;
                                                vm.Success = true;
                                                vm.Error = false;
                                            })
                                            .catch((error) => {
                                                if (error.response) {
                                                    if(error.response.status === 403){
                                                        alert(vm.s45);
                                                        vm.getCaptcha();
                                                    }
                                                }
                                            });
                                    } else if(vm.emails.length > 1){
                                        vm.usernameE = "";
                                        vm.sendE = false;
                                        vm.sendU = true;
                                        vm.Success = false;
                                        vm.Error = false;
                                    }
                                });
                            }else{
                                alert(this.s44);
                            }
                    }else{
                        alert(this.s38);
                    }
                }else{
                    alert(this.s37);
                }
            },
            sendEmailUser: function (email, username) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var flag = false;
                if(username != ""){
                    axios.get(url + "/api/public/checkMail/" + email) //
                        .then((res) => {
                            vm.emails = res.data;
                            if(vm.emails.length > 1){
                                for(let i = 0; i < vm.emails.length; ++i){
                                    if(vm.emails[i].userId == username){
                                        flag = true;
                                        
                                        axios.get(url + "/api/public/sendMail/" + email + "/" + username + "/" + vm.captchaId + "/" + vm.capAnswer) //
                                            .then((res) => {
                                                vm.sendE = false;
                                                vm.sendU = false;
                                                vm.Success = true;
                                                vm.Error = false;
                                            }).catch((error) => {
                                                if (error.response) {
                                                    if(error.response.status === 403){
                                                        vm.emailE = "";
                                                        vm.emailEE = "";
                                                        vm.sendE = true;
                                                        vm.sendU = false;
                                                        vm.Success = false;
                                                        vm.Error = false;
                                                        alert(vm.s45);
                                                        vm.getCaptcha();
                                                    }
                                                }
                                            });

                                        break;
                                    }
                                }
                                if(!flag){
                                    vm.emailEE = "";
                                    vm.sendE = false;
                                    vm.sendU = false;
                                    vm.Success = false;
                                    vm.Error = true;
                                    vm.getCaptcha();
                                }
                            }
                        });
                }else{
                    alert(this.s41);
                }
            },
            checkSMS: function  (code) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var usernameCheck;

                if(this.mobiles.length == 1){
                    usernameCheck = this.mobiles[0].userId;
                }else if(this.mobiles.length > 1){
                    usernameCheck = this.usernameS;
                }

                if(code != ""){
                    axios.get(url + "/api/public/validateMessageToken/" + usernameCheck + "/" + code) //
                    .then((res) => {
                        vm.sendS = false;
                        vm.sendUS = false;
                        vm.checkSMSCode = false;
                        vm.SuccessS = true;
                        vm.ErrorS = false;
                        vm.ErrorSMSCode = false;
                        window.location.replace(url + "/resetPassword?uid=" + usernameCheck + "&token=" + code);
                    })
                    .catch((error) => {
                        if (error.response) {
                            if(error.response.status === 408){
                                console.log("Code is Expired");
                                vm.ErrorSMSCode408 = true;
                                vm.ErrorSMSCode403 = false;
                                vm.sendS = false;
                                vm.sendUS = false;
                                vm.checkSMSCode = false;
                                vm.SuccessS = false;
                                vm.ErrorS = false;
                                vm.ErrorSMSCode = true;
                            }else if(error.response.status === 403){
                                vm.ErrorSMSCode408 = false;
                                vm.ErrorSMSCode403 = true;
                                vm.sendS = false;
                                vm.sendUS = false;
                                vm.checkSMSCode = false;
                                vm.SuccessS = false;
                                vm.ErrorS = false;
                                vm.ErrorSMSCode = true;
                            }
                        }
                    });
                }else{
                    alert(this.s43);
                }
            },
            sendSMS: function  (mobile) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                if(mobile != ""){
                    if(vm.capAnswer != ""){
                        axios.get(url + "/api/public/checkMobile/" + mobile) //
                            .then((res) => {
                                vm.mobiles = res.data;
                                if(vm.mobiles.length == 0){
                                    vm.mobileS = "";
                                    vm.sendS = false;
                                    vm.sendUS = false;
                                    vm.checkSMSCode = false;
                                    vm.SuccessS = false;
                                    vm.ErrorS = true;
                                    vm.ErrorSMSCode = false;
                                    vm.getCaptcha();
                                } else if(vm.mobiles.length == 1){
                                    axios.get(url + "/api/public/sendSMS/" + mobile + "/" + vm.captchaId + "/" + vm.capAnswer) //
                                        .then((res) => {
                                            vm.codeSMS = "";
                                            vm.sendS = false;
                                            vm.sendUS = false;
                                            vm.checkSMSCode = true;
                                            vm.SuccessS = false;
                                            vm.ErrorS = false;
                                            vm.ErrorSMSCode = false;
                                        }).catch((error) => {
                                            if (error.response) {
                                                if(error.response.status === 403){
                                                    alert(vm.s45);
                                                    vm.getCaptcha();
                                                }
                                            }
                                        });
                                } else if(vm.mobiles.length > 1){
                                    vm.usernameS = "";
                                    vm.sendS = false;
                                    vm.sendUS = true;
                                    vm.checkSMSCode = false;
                                    vm.SuccessS = false;
                                    vm.ErrorS = false;
                                    vm.ErrorSMSCode = false;
                                }
                            });
                        }else{
                            alert(this.s44);
                        }
                }else{
                    alert(this.s42);
                }
            },
            sendSMSUser: function (mobile, username) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var flag = false;
                if(username != ""){
                    axios.get(url + "/api/public/checkMobile/" + mobile) //
                        .then((res) => {
                            vm.mobiles = res.data;
                            if(vm.mobiles.length > 1){
                                for(let i = 0; i < vm.mobiles.length; ++i){
                                    if(vm.mobiles[i].userId == username){
                                        flag = true;
                                        axios.get(url + "/api/public/sendSMS/" + mobile + "/" + username + "/" + vm.captchaId + "/" + vm.capAnswer) //
                                            .then((res) => {
                                                vm.codeSMS = "";
                                                vm.sendS = false;
                                                vm.sendUS = false;
                                                vm.checkSMSCode = true;
                                                vm.SuccessS = false;
                                                vm.ErrorS = false;
                                                vm.ErrorSMSCode = false;
                                            }).catch((error) => {
                                                if (error.response) {
                                                    if(error.response.status === 403){
                                                        vm.mobile = "";
                                                        vm.mobileS = "";
                                                        vm.sendS = true;
                                                        vm.sendUS = false;
                                                        vm.checkSMSCode = false;
                                                        vm.SuccessS = false;
                                                        vm.ErrorS = false;
                                                        vm.ErrorSMSCode = false;
                                                        alert(vm.s45);
                                                        vm.getCaptcha();
                                                    }
                                                }
                                            });

                                        break;
                                    }
                                }
                                if(!flag){
                                    vm.mobileS = "";
                                    vm.sendS = false;
                                    vm.sendUS = false;
                                    vm.checkSMSCode = false;
                                    vm.SuccessS = false;
                                    vm.ErrorS = true;
                                    vm.ErrorSMSCode = false;
                                    vm.getCaptcha();
                                }
                            }
                        });
                }else{
                    alert(this.s41);
                }
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
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
                    this.s14 = "./dashboard?en";
                    this.s15 = "./services?en";
                    this.s16 = "./users?en";
                    this.s17 = "Go Back";
                    this.s18 = "Submit";
                    this.s19 = "Email";
                    this.s20 = "Dashboard";
                    this.s21 = "./groups?en";
                    this.s22 = "./settings?en";
                    this.s23 = "Please Enter Your Email Address To Reset Your Password:";
                    this.s24 = "Submission Was Successful.";
                    this.s25 = "Please Go To Your mailbox And Follow The Instructions.";
                    this.s26 = "Return To The Login Page";
                    this.s27 = "There Is No Such Email Address In Our Database.";
                    this.s28 = "Please Enter Your Email Address More Carefully:";
                    this.s29 = "Please Enter Your ID To Complete The Process:";
                    this.s30 = "Please Enter Your Mobile Number To Reset Your Password:";
                    this.s31 = "Please Go To Your SMS Inbox And Enter The Code We Sent You.";
                    this.s32 = "There Is No Such Mobile Number In Our Database.";
                    this.s33 = "Please Enter Your Mobile Number More Carefully:";
                    this.s34 = "Mobile Number";
                    this.s35 = "We Are Redirecting You To Password Reset Page...";
                    this.s36= "ID";
                    this.s37 = "Enter Your Email Address";
                    this.s38 = "Enter Email Address Format Correctly";
                    this.s44 = "The Answer To CAPTCHA Cannot Be Empty.";
                    this.s45 = "The Answer To CAPTCHA Is Incorrect, Try Again.";
                } else{
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
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
                    this.s14 = "./dashboard";
                    this.s15 = "./services";
                    this.s16 = "./users";
                    this.s17 = "بازگشت";
                    this.s18 = "تایید";
                    this.s19 = "ایمیل";
                    this.s20 = "داشبورد";
                    this.s21 = "./groups";
                    this.s22 = "./settings";
                    this.s23 = "لطفا جهت بازنشانی رمز عبور، آدرس ایمیل خود را وارد کنید:";
                    this.s24 = "ارسال موفقیت آمیز بود.";
                    this.s25 = "لطفا به mailbox خود مراجعه نموده و طبق راهنما، باقی مراحل را انجام دهید.";
                    this.s26 = "بازگشت به صفحه ورود";
                    this.s27 = "ایمیل وارد شده در پایگاه داده ما وجود ندارد.";
                    this.s28 = "لطفا با دقت بیشتری نسبت به ورود آدرس ایمیل اقدام نمایید:";
                    this.s29 = "لطفا جهت تکمیل مراحل، شناسه خود را وارد نمایید:";
                    this.s30 = "لطفا جهت بازنشانی رمز عبور، شماره موبایل خود را وارد کنید:";
                    this.s31 = "لطفا به لیست پیامک های دریافتی خود مراجعه نموده و کد ارسال شده را وارد نمایید.";
                    this.s32 = "شماره موبایل وارد شده در پایگاه داده ما وجود ندارد.";
                    this.s33 = "لطفا با دقت بیشتری نسبت به ورود شماره موبایل اقدام نمایید:";
                    this.s34 = "شماره موبایل";
                    this.s35 = "شما در حال انتقال به صفحه بازنشانی رمز عبور هستید...";
                    this.s36 = "شناسه";
                    this.s37 = "آدرس ایمیل خود را وارد کنید";
                    this.s38 = "فرمت آدرس ایمیل را به درستی وارد کنید";
                    this.s44 = "جواب CAPTCHA نمی تواند خالی باشد.";
                    this.s45 = "جواب CAPTCHA اشتباه است، دوباره تلاش کنید.";
                }
            }
        }
    })
})
