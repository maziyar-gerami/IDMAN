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
                { message:"حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ", regex:/[a-zA-Z]+/, fa:false},
                { message:"حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ",  regex:/[!@#\$%\^\&*\)\(+=\[\]._-]+/, fa:true},
				{ message:"حداقل ۸ کاراکتر باشد. ", regex:/.{8,}/, fa:false},
				{ message:"حداقل شامل یک عدد باشد. ", regex:/[0-9]+/, fa:false}
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
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            marg: "margin-left: auto;",
            font: "font-size: 0.74em; text-align: right;",
            lang: "EN",
            rtl: "direction: rtl;",
            eye: "right: 3%;",
            s0: "احراز هویت متمرکز شرکت نفت فلات قاره ایران",
            s1: "نام کاربری و رمز عبور خود را وارد کنید",
            s2: "نام کاربری",
            s3: "رمز عبور",
            s4: "ورود",
            s5: "اطلاعات کاربری نادرست است",
            s6: "رمز عبور جدید",
            s7: "تکرار رمز عبور جدید",
            s8: "تایید",
            s9: "رمز عبور های وارد شده یکسان نمی باشند",
            s10: ":آدرس ایمیلی که با آن ثبت نام کرده اید را وارد نمایید",
            s11: "ارسال ایمیل باز نشانی",
            s12: "بازنشانی رمز عبور",
            s13: ":رمز عبور شما باید شامل موارد زیر باشد",
            s14: ":جهت تکمیل فرآیند بازنشانی رمز عبور خود، با رعایت نکات زیر، رمز عبور جدید خود را وارد نمایید",
            s15: "شما اجازه دسترسی به این صفحه را ندارید",
            s16: "بازگشت",
            s17: "",
            s18: " عزیز",
            s19: "،",
            s20: "متاسفانه درخواست شما با مشکل مواجه شده است",
            s21: "رمز عبور جدید و رمز عبور قدیمی نباید یکسان باشند.",
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
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                const redirectedUrl = new URL(location.href);
                var vm = this;
                axios.get(url + "/api/public/getName/" + redirectedUrl.searchParams.get('uid') + "/" + redirectedUrl.searchParams.get('token')) //
                    .then((res) => {
                    vm.userInfo = res.data;
                    vm.s17 = vm.userInfo.displayName;
                });
            },
            passwordCheck () {
                this.has_number    = /[0-9]+/.test(this.password);
                this.has_lowerUPPERcase = /[a-zA-Z]/.test(this.password);
                this.has_specialchar = /[!@#\$%\^\&*\)\(+=\[\]._-]+/.test(this.password) || this.persianTextCheck(this.password);
                this.has_char   = /.{8,}/.test(this.password);
            },
            persianTextCheck (s) {
                for (let i = 0; i < s.length; ++i) {
                    if(persianRex.text.test(s.charAt(i))){
                        return true;
                    }
                }
                return false;
            },
            resetPasswords () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                const redirectedUrl = new URL(location.href); 
                const formData = new FormData();
                formData.append('newPassword', this.password);
                var vm = this;               
                axios({
                    method: 'put',
                    url: url + '/api/public/resetPass/' + redirectedUrl.searchParams.get('uid') + "/" + redirectedUrl.searchParams.get('token'),  //
                    headers: {'Content-Type': 'application/json'},
                    data: formData
                }).then((res) => {
                    location.replace(url);
                }).catch((error) => {
                    if (error.response) {
                        if(error.response.status === 302){
                            vm.duplicatePasswords = true;
                            setTimeout(function(){ vm.duplicatePasswords = false; }, 5000);
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
                    this.s21 = "New Password Should Not be Same as Old Password.";
                    this.rules[0].message = "- One Lowercase or Uppercase English Letter Required.";
                    this.rules[1].message = "- One special Character or Persian Letter Required.";
                    this.rules[2].message = "- 8 Characters Minimum.";
                    this.rules[3].message = "- One Number Required.";

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
                    this.s1 = "نام کاربری و رمز عبور خود را وارد کنید";
                    this.s2 = "نام کاربری";
                    this.s3 = "رمز عبور";
                    this.s4 = "ورود";
                    this.s5 = "اطلاعات کاربری نادرست است";
                    this.s6 = "رمز عبور جدید";
                    this.s7 = "تکرار رمز عبور جدید";
                    this.s8 = "تایید";
                    this.s9 = "رمز عبور های وارد شده یکسان نمی باشند";
                    this.s10 = ":آدرس ایمیلی که با آن ثبت نام کرده اید را وارد نمایید"
                    this.s11 = "ارسال ایمیل";
                    this.s12 = "بازنشانی رمز عبور";
                    this.s13 = ":رمز عبور شما باید شامل موارد زیر باشد";
                    this.s14 = ":جهت تکمیل فرآیند بازنشانی رمز عبور خود، با رعایت نکات زیر، رمز عبور جدید خود را وارد نمایید";
                    this.s15 = "شما اجازه دسترسی به این صفحه را ندارید";
                    this.s16 = "بازگشت";
                    this.s17 = this.userInfo.displayName;
                    this.s18 = " عزیز";
                    this.s19 = "،";
                    this.s20 = "متاسفانه درخواست شما با مشکل مواجه شده است";
                    this.s21 = "رمز عبور جدید و رمز عبور قدیمی نباید یکسان باشند.";
                    this.rules[0].message = "حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ";
                    this.rules[2].message = "حداقل ۸ کاراکتر باشد. ";
                    this.rules[3].message = "حداقل شامل یک عدد باشد. ";

                }
            } 
        },
        computed: {
            infoError () {
                if (typeof this.$route.query.error !== 'undefined') {
                    return true
                }
            },
            notSamePasswords () {
                if (this.passwordsFilled) {
                    return (this.password !== this.checkPassword)
                }else {
                    return false;
                }
            },
            passwordsFilled () {
                return (this.password !== '' && this.checkPassword !== '')
            },
            setActive () {
                if(this.password !== '' && this.checkPassword !== ''){
                    let errors = []
                    for (let condition of this.rules) {
                        if (!condition.regex.test(this.password)) {
                            errors.push(condition.message)
                        }
                    }
                    if(errors.length === 0){
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
            },
            passwordValidation () {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.password) && !this.persianTextCheck(this.password)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.password)) {
                            errors.push(condition.message)
                        }
                    }
                }
                if (errors.length === 0) {
                    return { valid:true, errors }
                } else {
                    return { valid:false, errors }
                }
            },
            strengthLevel() {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.password) && !this.persianTextCheck(this.password)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.password)) {
                            errors.push(condition.message)
                        }
                    }
                }
                if(errors.length === 0) return 4;
                if(errors.length === 1) return 3;
                if(errors.length === 2) return 2;
                if(errors.length === 3) return 1;
                if(errors.length === 4) return 0;
            }
        }
    });
})