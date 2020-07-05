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
            username: "",
            usernameE: "",
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
            editS: "display:none",
            addS: "display:none",
            showS: "",
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
            U0: "رمز عبور",
            U1: "کاربران",
            U2: "شناسه",
            U3: "نام (به انگلیسی)",
            U4: "نام خانوادگی (به انگلیسی)",
            U5: "نام کامل (به فارسی)",
            U6: "شماره تماس",
            U7: "ایمیل",
            U8: "کد ملی",
            U9: "توضیحات",
            U10: "به روزرسانی",
            U11: "حذف",
            U12: "اضافه کردن کاربر جدید",
            U13: "ویرایش",
            U14: "گروههای عضو",
            U15: "تکرار رمز عبور",
            U16: "کاربر مورد نظر در گروههای زیر عضویت دارد. کاربر مورد نظر از چه گروههایی حذف شود؟",
            U17: "حذف تمامی کاربران",
            h1: "ترکیبی از حروف و اعداد. مثال: ali123",
            p1: "خیلی ضعیف",
            p2: "متوسط",
            p3: "قوی"
        },
        created: function () {
            this.getUserInfo();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
            sendEmail: function  (email) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/idman/api/public/checkMail/" + email) // 
                    .then((res) => {
                        vm.emails = res.data;
                        if(vm.emails.length == 0){
                            vm.sendE = false;
                            vm.sendU = false;
                            vm.Success = false;
                            vm.Error = true;
                        } else if(vm.emails.length == 1){
                            vm.sendE = false;
                            vm.sendU = false;
                            vm.Success = true;
                            vm.Error = false;
                            axios.get(url + "/idman/api/public/sendMail/" + email) // 
                                .then((res) => {
                                    
                                });
                        } else if(vm.emails.length > 1){
                            vm.sendE = false;
                            vm.sendU = true;
                            vm.Success = false;
                            vm.Error = false;
                        }
                    });
            },
            sendEmailUser: function (email, username) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var flag = false;
                axios.get(url + "/idman/api/public/checkMail/" + email) // 
                    .then((res) => {
                        vm.emails = res.data;
                        if(vm.emails.length > 1){
                            for(let i = 0; i < vm.emails.length; ++i){
                                if(vm.emails[i].userId == username){
                                    flag = true;
                                    vm.sendE = false;
                                    vm.sendU = false;
                                    vm.Success = true;
                                    vm.Error = false;
                                    axios.get(url + "/idman/api/public/sendMail/" + email + "/" + username) // 
                                        .then((res) => {
                                            
                                        });
                                    break;
                                }
                            }
                            if(!flag){
                                vm.sendE = false;
                                vm.sendU = false;
                                vm.Success = false;
                                vm.Error = true;
                            }
                        }
                    });
            },
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/idman/api/user") // 
                .then((res) => {
                    vm.userInfo = res.data;
                    vm.username = vm.userInfo.userId;
                    vm.name = vm.userInfo.displayName;
                    vm.nameEN = vm.userInfo.firstName + vm.userInfo.lastName;
                    vm.s1 = vm.name;
                });
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
                    this.U0= "Password";
                    this.U1= "Users";
                    this.U2= "ID";
                    this.U3= "First Name (In English)";
                    this.U4= "Last Name (In English)";
                    this.U5= "FullName (In Persian)";
                    this.U6= "Phone";
                    this.U7= "Email";
                    this.U8= "NID";
                    this.U9 = "Description";
                    this.U10 = "Update";
                    this.U11 = "Delete"
                    this.U12 = "Add New User";
                    this.U13 = "Edit";
                    this.U14 = "Member Of The Groups";
                    this.U15 = "Repeat Password";
                    this.U17 = "Remove all user";
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
                    this.U0= "رمز";
                    this.U1= "کاربران";
                    this.U2= "شناسه";
                    this.U3= "نام (به انگلیسی)";
                    this.U4= "نام خانوادگی (به انگلیسی)";
                    this.U5= "نام کامل (به فارسی)";
                    this.U6= "شماره تماس";
                    this.U7= "ایمیل";
                    this.U8= "کد ملی";
                    this.U9= "توضیحات";
                    this.U10 = "به روز رسانی";
                    this.U11 = "حذف"
                    this.U12 = "اضافه کردن کاربر جدید";
                    this.U13 = "ویرایش";
                    this.U14 = "گروههای عضو";
                    this.U15 = "تکرار رمز عبور";
                    this.U17 = "حذف تمامی کاربران";
                }
            }
        }
    })
})
