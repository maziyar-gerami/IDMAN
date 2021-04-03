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
            btnDisable : true,
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            marg: "margin-left: auto;",
            font: "font-size: 0.74em; text-align: right;",
            lang: "EN",
            rtl: "direction: rtl;",
            eye: "right: 3%;",
            s0: "پارسو",
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
            s21: "سامانه در حال پیکربندی است."
        },
        created: function () {
            this.setDateNav();
            if(window.localStorage.getItem("lang") === null){
                window.localStorage.setItem("lang", "FA");
            }else if(window.localStorage.getItem("lang") === "EN") {
                this.changeLang();
            }
        },
        methods: {
            setDateNav: function () {
                this.dateNav = new persianDate().format("dddd, DD MMMM YYYY");
                persianDate.toCalendar("gregorian");
                persianDate.toLocale("en");
                this.dateNavEn = new persianDate().format("dddd, DD MMMM YYYY");
                persianDate.toCalendar("persian");
                persianDate.toLocale("fa");
                this.dateNavText = this.dateNav;
            },
            changeLang: function () {
                if (this.lang == "EN") {
                    window.localStorage.setItem("lang", "EN");
                    this.placeholder = "text-align: left;"
                    this.isRtl = false;
                    this.dateNavText = this.dateNavEn;
                    this.margin = "margin-left: 30px;";
                    this.marg = "margin-right: auto;";
                    this.font = "font-size: 0.9em; text-align: left;"
                    this.lang = "فارسی";
                    this.rtl = "direction: ltr;";
                    this.eye = "left: 3%;";
                    this.s0 = "Parsso";
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
                    this.s21 = "System is Being Configured.";
                    this.rules[0].message = "- One Lowercase or Uppercase English Letter Required.";
                    this.rules[1].message = "- One special Character or Persian Letter Required.";
                    this.rules[2].message = "- 8 Characters Minimum.";
                    this.rules[3].message = "- One Number Required.";

                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;";
                    this.isRtl = true;
                    this.dateNavText = this.dateNav;
                    this.margin = "margin-right: 30px;";
                    this.marg = "margin-left: auto;";
                    this.font = "font-size: 0.74em; text-align: right;";
                    this.lang = "EN";
                    this.rtl = "direction: rtl;";
                    this.eye = "right: 3%;";
                    this.s0 = "پارسو";
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
                    this.s21 = "سامانه در حال پیکربندی است.";
                    this.rules[0].message = "حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ";
                    this.rules[2].message = "حداقل ۸ کاراکتر باشد. ";
                    this.rules[3].message = "حداقل شامل یک عدد باشد. ";

                }
            }
        }
    });
})