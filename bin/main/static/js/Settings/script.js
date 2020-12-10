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
            rules: [
                { message:"حداقل شامل یک کاراکتر کوچک باشد. ", regex:/[a-z]+/ },
				{ message:"حداقل شامل یک کاراکتر بزرگ باشد. ",  regex:/[A-Z]+/ },
				{ message:"حداقل ۸ کاراکتر باشد. ", regex:/.{8,}/ },
				{ message:"حداقل شامل یک عدد باشد. ", regex:/[0-9]+/ }
            ],
            show: false,
            showR: false,
            showC: false,
            has_number: false,
            has_lowercase: false,
            has_uppercase: false,
            has_char: false,
            password: "",
            checkPassword: "",
			passwordVisible: true,
            submitted: false,
            btnDisable : true,
            userInfo: [],
            email: "",
            username: "",
            name: "",
            nameEN: "",
            users: [],
            message: "",
            editInfo: {},
            placeholder: "text-align: right;",
            font: "font-size: 0.74em; text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            menuS: false,
            activeItem: "info",
            eye: "right: 1%;",
            userPicture: "images/PlaceholderUser.png",
            QR: "/api/mobile/qrcode",
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
            s17: "رمز عبور جدید",
            s18: "تکرار رمز عبور جدید",
            s19: "رمز عبور شما باید شامل موارد زیر باشد:",
            s20: "داشبورد",
            s21: "رمز عبور های وارد شده یکسان نمی باشند",
            s22: "./groups",
            s23: "./settings",
            s24: "آیا از اعمال این تغییرات اطمینان دارید؟",
            s25: "./privacy",
            s26: "رمز عبور فعلی",
            s27: "پیکربندی",
            s28: "./configs",
            s29: "./events",
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
            U16: "تغییر رمز عبور",
            U17: "تغییر اطلاعات کاربری"
        },
        created: function () {
            this.getUserInfo();
            this.getUserPic();
            this.isAdmin();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
            this.getQR();
        },
        methods: {
            isActive (menuItem) {
                return this.activeItem === menuItem
            },
            setActive (menuItem) {
                this.activeItem = menuItem
            },
            isAdmin: function () {
            var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
            var vm = this;
            axios.get(url + "/api/user/isAdmin") //
                .then((res) => {
                    if(res.data){
                        vm.menuS = true;
                    }
                });
            },
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user") //
                    .then((res) => {
                        vm.userInfo = res.data;
                        vm.username = vm.userInfo.userId;
                        vm.name = vm.userInfo.displayName;
                        vm.nameEN = vm.userInfo.firstName + vm.userInfo.lastName;
                        vm.s1 = vm.name;
                    });
            },
            getUserPic: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user/photo") //
                    .then((res) => {
                      vm.userPicture = "/api/user/photo";
                    })
                    .catch((error) => {
                        if (error.response) {
                          if (error.response.status == 400 || error.response.status == 500) {
                            vm.userPicture = "images/PlaceholderUser.png";
                          }else{
                            vm.userPicture = "/api/user/photo";
                          }
                        }else{
                          console.log("error.response is False")
                        }
                    });
            },
            getQR: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                this.QR = url + this.QR;
            },
            editUser: function (id) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm(this.s24);
                if (check == true) {
                    axios({
                        method: 'put',
                        url: url + '/api/user',   //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            firstName: document.getElementById('userInfo.firstNameUpdate').value,
                            lastName: document.getElementById('userInfo.lastNameUpdate').value,
                            displayName: document.getElementById('userInfo.displayNameUpdate').value,
                            telephoneNumber: document.getElementById('userInfo.telephoneNumberUpdate').value,
                            mail: document.getElementById('userInfo.mailUpdate').value,
                            description: document.getElementById('userInfo.descriptionUpdate').value
                        }),
                    }).then((res) => {
                        location.replace(url + "/settings"); //
                    });
                }
            },
            editPass: function (id) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var check = confirm(this.s24);
                if (check == true) {
                    axios({
                        method: 'put',
                        url: url + '/api/user/password',   //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            newPassword: document.getElementById('newPassword').value,
                            currentPassword: document.getElementById('currentPassword').value
                        }),
                    }).then((res) => {
                        location.replace(url + "/settings"); //
                    });
                }
            },
            passwordCheck () {
                this.has_number    = /\d/.test(this.password);
                this.has_lowercase = /[a-z]/.test(this.password);
                this.has_uppercase = /[A-Z]/.test(this.password);
                this.has_char   = /.{8,}/.test(this.password);
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.font = "font-size: 0.9em; text-align: left;"
                    this.isRtl = false;
                    this.eye = "left: 1%;";
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
                    this.s17 = "New Password";
                    this.s18 = "Repeat New Password";
                    this.s19 = "Your Password Must Meet All Of The Following Criteria:";
                    this.s20 = "Dashboard";
                    this.s21 = "Passwords Don't Match";
                    this.s22 = "./groups?en";
                    this.s23 = "./settings?en";
                    this.s24 = "Are You Sure You Want To Edit?";
                    this.s25 = "./privacy?en";
                    this.s26 = "Current Password";
                    this.s27 = "Configs";
                    this.s28 = "./configs?en";
                    this.s29 = "./events?en";
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
                    this.U16 = "Edit Password";
                    this.U17 = "Edit User Information";
                    this.rules[0].message = "- One Lowercase Letter Required.";
                    this.rules[1].message = "- One Uppercase Letter Required.";
                    this.rules[2].message = "- 8 Characters Minimum.";
                    this.rules[3].message = "- One Number Required.";
                } else{
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.font = "font-size: 0.74em; text-align: right;"
                    this.isRtl = true;
                    this.eye = "right: 1%;";
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
                    this.s17 = "رمز عبور جدید";
                    this.s18 = "تکرار رمز عبور جدید";
                    this.s19 = "رمز عبور شما باید شامل موارد زیر باشد:";
                    this.s20 = "داشبورد";
                    this.s21 = "رمز عبور های وارد شده یکسان نمی باشند";
                    this.s22 = "./groups";
                    this.s23 = "./settings";
                    this.s24 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s25 = "./privacy";
                    this.s26 = "رمز عبور فعلی";
                    this.s27 = "پیکربندی";
                    this.s28 = "./configs";
                    this.s29 = "./events";
                    this.U0= "رمز عبور";
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
                    this.U16 = "تغییر رمز عبور";
                    this.U17 = "تغییر اطلاعات کاربری";
                    this.rules[0].message = "حداقل شامل یک کاراکتر کوچک باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر بزرگ باشد. ";
                    this.rules[2].message = "حداقل ۸ کاراکتر باشد. ";
                    this.rules[3].message = "حداقل شامل یک عدد باشد. ";
                }
            }
        },
        computed: {
            notSamePasswords () {
                if (this.passwordsFilled) {
                    return (this.password !== this.checkPassword)
                } else {
                    return false
                }
            },
            passwordsFilled () {
                return (this.password !== '' && this.checkPassword !== '')
            },
            setActivee () {
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
                    if (!condition.regex.test(this.password)) {
                        errors.push(condition.message)
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
                    if (!condition.regex.test(this.password)) {
                        errors.push(condition.message)
                    }
                }
                if(errors.length === 0) return 4;
                if(errors.length === 1) return 3;
                if(errors.length === 2) return 2;
                if(errors.length === 3) return 1;
                if(errors.length === 4) return 0;
            }
        }
    })
})
