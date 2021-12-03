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
            rules: [
                { message:"حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. "},
                { message:"حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. "},
				{ message:"حداقل ۸ کاراکتر باشد. "},
				{ message:"حداقل شامل یک عدد باشد. "}
            ],
            userInfo: [],
            show: false,
            showR: false,
            has_number: false,
            has_lowerUPPERcase: false,
            has_specialchar: false,
            has_char: false,
			password: "",
			checkPassword: "",
            passwordVisible: true,
            isRtl: true,
            submitted: false,
            btnDisable: true,
            duplicatePasswords: false,
            passwordChangeSuccessful: false,
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            marg: "margin-left: auto;",
            font: "font-size: 0.74em; text-align: right;",
            lang: "EN",
            rtl: "direction: rtl;",
            eye: "right: 3%;",
            s0: "احراز هویت متمرکز شرکت نفت فلات قاره ایران",
            s1: "شماره پرسنلی و گذرواژه خود را وارد کنید",
            s2: "شماره پرسنلی",
            s3: "گذرواژه",
            s4: "ورود",
            s5: "اطلاعات کاربری نادرست است",
            s6: "گذرواژه جدید",
            s7: "تکرار گذرواژه جدید",
            s8: "تایید",
            s9: "گذرواژه های وارد شده یکسان نمی باشند",
            s10: ":آدرس ایمیلی که با آن ثبت نام کرده اید را وارد نمایید",
            s11: "ارسال ایمیل باز نشانی",
            s12: "بازنشانی گذرواژه",
            s13: ":گذرواژه شما باید شامل موارد زیر باشد",
            s14: ":جهت تکمیل فرآیند بازنشانی گذرواژه خود، با رعایت نکات زیر، گذرواژه جدید خود را وارد نمایید",
            s15: "شما اجازه دسترسی به این صفحه را ندارید",
            s16: "بازگشت",
            s17: "",
            s18: " عزیز",
            s19: "،",
            s20: "متاسفانه درخواست شما با مشکل مواجه شده است",
            duplicatePasswordsText: "گذرواژه جدید نباید با گذرواژه های قدیمی یکسان باشند، لطفا دوباره تلاش کنید.",
            expiredSMSCodeText: "کد پیامکی منقضی شده است، لطفا به صفحه قبل بازگشته و دوباره تلاش کنید.",
            changeUserPasswordText: "تغییر گذرواژه کاربر",
            passwordChangeSuccessfulText: "گذرواژه شما با موفقیت تغییر یافت، در حال انتقال به صفحه داشبورد",
        },
        created: function () {
            this.setDateNav();
            this.getName();
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
            getName: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                const redirectedUrl = new URL(location.href);
                let vm = this;
                if(typeof redirectedUrl.searchParams.get("uid") !== "undefined" && typeof redirectedUrl.searchParams.get("token") !== "undefined"){
                    if(redirectedUrl.searchParams.get("uid") !== null && redirectedUrl.searchParams.get("token") !== null){
                        axios.get(url + "/api/public/getName/" + redirectedUrl.searchParams.get("uid") + "/" + redirectedUrl.searchParams.get("token")) //
                            .then((res) => {
                                vm.userInfo = res.data;
                                vm.s17 = vm.userInfo.displayName;
                            });
                    }
                }
            },
            passwordCheck: function () {
                this.has_number    = /[0-9]+/.test(this.password);
                this.has_lowerUPPERcase = /[a-zA-Z]/.test(this.password);
                this.has_specialchar = /[!@#\$%\^\&*\)\(+=\[\]._-]+/.test(this.password) || this.persianTextCheck(this.password);
                this.has_char   = /.{8,}/.test(this.password);
            },
            persianTextCheck: function (s) {
                for (let i = 0; i < s.length; ++i) {
                    if(persianRex.text.test(s.charAt(i))){
                        return true;
                    }
                }
                return false;
            },
            resetPasswords: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                const redirectedUrl = new URL(location.href); 
                const formData = new FormData();
                formData.append("newPassword", this.password);
                axios({
                    method: "put",
                    url: url + "/api/public/resetPass/" + redirectedUrl.searchParams.get("uid") + "/" + redirectedUrl.searchParams.get("token"),  //
                    headers: {"Content-Type": "application/json"},
                    data: formData
                }).then((res0) => {
                    vm.passwordChangeSuccessful = true;
                    let index = 0;
                    let customTimer = window.setInterval(function() {
                        if(index == 1){
                            clearInterval(customTimer);
                        }
                        ++index;
                    }, 2000);
                    axios.get(url + "/cas/login?service=" + window.location.protocol + "//" + window.location.hostname + "/login/cas") //
                        .then((res1) => {
                            let loginPage = document.createElement("html");
                            loginPage.innerHTML = res1.data;
                            let execution = loginPage.getElementsByTagName("form")[0].getElementsByTagName("input")[2].value;
                            let bodyFormData = new FormData();
                            bodyFormData.append("username", redirectedUrl.searchParams.get("uid"));
                            bodyFormData.append("password", vm.password);
                            bodyFormData.append("execution", execution);
                            bodyFormData.append("geolocation", "");
                            bodyFormData.append("_eventId", "submit");
                            axios({
                                method: "post",
                                url: url + "/cas/login",
                                headers: {"Content-Type": "multipart/form-data"},
                                data: bodyFormData
                            }).then((res2) => {
                                location.replace(url);
                            }).catch((error) => {
                                console.log(error);
                            });
                        }).catch((error) => {
                            console.log(error);
                        });
                }).catch((error) => {
                    if (error.response) {
                        if(error.response.status === 302){
                            alert(vm.duplicatePasswordsText);
                        } else if(error.response.status === 403){
                            alert(vm.expiredSMSCodeText);
                        }
                    }
                });
            },
            changeLang: function () {
                if (this.lang == "EN") {
                    window.localStorage.setItem("lang", "EN");
                    this.placeholder = "text-align: left;"
                    this.isRtl = false;
                    this.margin = "margin-left: 30px;";
                    this.marg = "margin-right: auto;";
                    this.font = "font-size: 0.9em; text-align: left;"
                    this.lang = "فارسی";
                    this.rtl = "direction: ltr;";
                    this.eye = "left: 3%;";
                    this.dateNavText = this.dateNavEn;
                    this.s0 = "IOOC Centralized Authentication";
                    this.s1 = "Enter Your Username and Password";
                    this.s2 = "Username";
                    this.s3 = "Password";
                    this.s4 = "Sign in";
                    this.s5 = "Invalid Username and Password";
                    this.s6 = "New Password";
                    this.s7 = "Repeat New Password";
                    this.s8 = "Submit";
                    this.s9 = "Passwords Don't Match";
                    this.s10 = "Enter your email address that register with:";
                    this.s11 = "Send emial";
                    this.s12 = "Reset Password";
                    this.s13 = "Your Password Must Meet All Of The Following Criteria:";
                    this.s14 = "For Resetting Your Password, Enter Your New Password By Following The Tips Below:";
                    this.s15 = "You Don't Have Permission To Access This Page";
                    this.s16 = "Return";
                    this.s17 = this.userInfo.firstName + " " + this.userInfo.lastName;
                    this.s18 = ",";
                    this.s19 = "Dear ";
                    this.s20 = "Sorry, There Was a Problem With Your Request";
                    this.rules[0].message = "- One Lowercase or Uppercase English Letter Required.";
                    this.rules[1].message = "- One special Character or Persian Letter Required.";
                    this.rules[2].message = "- 8 Characters Minimum.";
                    this.rules[3].message = "- One Number Required.";
                    this.duplicatePasswordsText = "The new password should not be the same as the old ones, please try again.";
                    this.expiredSMSCodeText = "SMS code has expired, please go back to the previous page and try again.";
                    this.changeUserPasswordText = "Change User Password";
                    this.passwordChangeSuccessfulText = "Your password has been successfully changed, loading dashboard";
                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;";
                    this.isRtl = true;
                    this.margin = "margin-right: 30px;";
                    this.marg = "margin-left: auto;";
                    this.font = "font-size: 0.74em; text-align: right;";
                    this.lang = "EN";
                    this.rtl = "direction: rtl;";
                    this.eye = "right: 3%;";
                    this.dateNavText = this.dateNav;
                    this.s0 = "احراز هویت متمرکز شرکت نفت فلات قاره ایران";
                    this.s1 = "شماره پرسنلی و گذرواژه خود را وارد کنید";
                    this.s2 = "شماره پرسنلی";
                    this.s3 = "گذرواژه";
                    this.s4 = "ورود";
                    this.s5 = "اطلاعات کاربری نادرست است";
                    this.s6 = "گذرواژه جدید";
                    this.s7 = "تکرار گذرواژه جدید";
                    this.s8 = "تایید";
                    this.s9 = "گذرواژه های وارد شده یکسان نمی باشند";
                    this.s10 = ":آدرس ایمیلی که با آن ثبت نام کرده اید را وارد نمایید"
                    this.s11 = "ارسال ایمیل";
                    this.s12 = "بازنشانی گذرواژه";
                    this.s13 = ":گذرواژه شما باید شامل موارد زیر باشد";
                    this.s14 = ":جهت تکمیل فرآیند بازنشانی گذرواژه خود، با رعایت نکات زیر، گذرواژه جدید خود را وارد نمایید";
                    this.s15 = "شما اجازه دسترسی به این صفحه را ندارید";
                    this.s16 = "بازگشت";
                    this.s17 = this.userInfo.displayName;
                    this.s18 = " عزیز";
                    this.s19 = "،";
                    this.s20 = "متاسفانه درخواست شما با مشکل مواجه شده است";
                    this.rules[0].message = "حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ";
                    this.rules[2].message = "حداقل ۸ کاراکتر باشد. ";
                    this.rules[3].message = "حداقل شامل یک عدد باشد. ";
                    this.duplicatePasswordsText = "گذرواژه جدید نباید با گذرواژه های قدیمی یکسان باشند، لطفا دوباره تلاش کنید.";
                    this.expiredSMSCodeText = "کد پیامکی منقضی شده است، لطفا به صفحه قبل بازگشته و دوباره تلاش کنید.";
                    this.changeUserPasswordText = "تغییر گذرواژه کاربر";
                    this.passwordChangeSuccessfulText = "گذرواژه شما با موفقیت تغییر یافت، در حال انتقال به صفحه داشبورد";
                }
            } 
        },
        computed: {
            infoError () {
                if (typeof this.$route.query.error !== "undefined") {
                    return true
                }
            },

            notSamePasswords: function() {
                return this.password !== this.checkPassword;
            },
            isActiveUserPassUpdate: function() {
                if(this.password !== "" && this.checkPassword !== ""){
                    if(/[0-9]+/.test(this.password)){
                        if(/[a-zA-Z]/.test(this.password)){
                            if(/[!@#\$%\^\&*\)\(+=\[\]._-]+/.test(this.password) || this.persianTextCheck(this.password)){
                                if(/.{8,}/.test(this.password)){
                                    if(this.password === this.checkPassword){
                                        return false
                                    }else{
                                        return true
                                    }
                                }else{
                                    return true
                                }
                            }else{
                                return true
                            }
                        }else{
                            return true
                        }
                    }else{
                        return true
                    }
                }else{
                    return true
                }
            },
            strengthLevel: function() {
                let checks = 0;
                if(/[0-9]+/.test(this.password)){
                    checks += 1;
                }
                if(/[a-zA-Z]/.test(this.password)){
                    checks += 1;
                }
                if(/[!@#\$%\^\&*\)\(+=\[\]._-]+/.test(this.password) || this.persianTextCheck(this.password)){
                    checks += 1;
                }
                if(/.{8,}/.test(this.password)){
                    checks += 1;
                }
                return checks;
            }
        }
    });
})