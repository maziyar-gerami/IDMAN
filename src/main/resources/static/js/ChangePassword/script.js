document.addEventListener("DOMContentLoaded", function () {
    new Vue({
        el: "#app",
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
            userId: "",
            currentPassword: "",
			password: "",
			checkPassword: "",
            passwordVisible: true,
            isRtl: true,
            submitted: false,
            btnDisable: true,
            notAllowed: false,
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            marg: "margin-left: auto;",
            font: "font-size: 0.74em; text-align: right;",
            lang: "EN",
            rtl: "direction: rtl;",
            eye: "right: 3%;",
            s0: "احراز هویت متمرکز شرکت نفت فلات قاره ایران",
            s1: "شناسه کاربری",
            s2: "گذرواژه فعلی",
            s6: "گذرواژه جدید",
            s7: "تکرار گذرواژه جدید",
            s8: "تایید",
            s9: "گذرواژه های وارد شده یکسان نمی باشند.",
            s13: "گذرواژه شما باید شامل موارد زیر باشد:",
            s14: "کاربر عزیز، جهت تغییر گذرواژه خود، با رعایت نکات زیر، گذرواژه جدید خود را وارد نمایید:",
            s16: "بازگشت",
            s21: "شما مجاز به انجام این عمل نیستید.",
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
                axios({
                    method: "put",
                    url: url + "/api/public/changePassword",
                    headers: {"Content-Type": "application/json"},
                    data: JSON.stringify({
                        userId: vm.userId,
                        currentPassword: vm.currentPassword,
                        newPassword: vm.password
                        }
                    ).replace(/\\\\/g, "\\")
                }).then((res) => {
                    location.replace(url);
                }).catch((error) => {
                    if (error.response) {
                        if(error.response.status === 401 || error.response.status === 403 || error.response.status === 404){
                            vm.notAllowed = true;
                            setTimeout(function(){ vm.notAllowed = false; }, 5000);
                        }
                    }
                });
            },
            changeLang: function () {
                if (this.lang === "EN") {
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
                    this.s1 = "UserId";
                    this.s2 = "Current Password";
                    this.s6 = "New Password";
                    this.s7 = "Repeat New Password";
                    this.s8 = "Submit";
                    this.s9 = "Passwords Don't Match.";
                    this.s13 = "Your Password Must Meet All Of The Following Criteria:";
                    this.s14 = "Dear User, To Reset Your Password, Enter Your New Password By Following The Tips Below:";
                    this.s16 = "Return";
                    this.s21 = "You Are Not Allowed to Do This Process.";
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
                    this.s1 = "شناسه کاربری";
                    this.s2 = "گذرواژه فعلی";
                    this.s6 = "گذرواژه جدید";
                    this.s7 = "تکرار گذرواژه جدید";
                    this.s8 = "تایید";
                    this.s9 = "گذرواژه های وارد شده یکسان نمی باشند";
                    this.s13 = "گذرواژه شما باید شامل موارد زیر باشد:";
                    this.s14 = "کاربر عزیز، جهت تغییر گذرواژه خود، با رعایت نکات زیر، گذرواژه جدید خود را وارد نمایید:";
                    this.s16 = "بازگشت";
                    this.s21 = "شما مجاز به انجام این عمل نیستید.";
                    this.rules[0].message = "حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ";
                    this.rules[2].message = "حداقل ۸ کاراکتر باشد. ";
                    this.rules[3].message = "حداقل شامل یک عدد باشد. ";

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