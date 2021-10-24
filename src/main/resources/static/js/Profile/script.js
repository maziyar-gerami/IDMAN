document.addEventListener('DOMContentLoaded', function () {
    var router = new VueRouter({
        mode: 'history',
        routes: []
    });
    new Vue({
        router,
        el: '#app',
        data: {
            dropdownMenu: false,
            dateNav: "",
            dateNavEn: "",
            dateNavText: "",
            rules: [
                { message:"حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ", regex:/[a-zA-Z]+/, fa:false},
                { message:"حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ",  regex:/[!@#\$%\^\&*\)\(+=\[\]._-]+/, fa:true},
				{ message:"حداقل ۸ کاراکتر باشد. ", regex:/.{8,}/, fa:false},
				{ message:"حداقل شامل یک عدد باشد. ", regex:/[0-9]+/, fa:false}
            ],
            show: false,
            showR: false,
            showC: false,
            has_number: false,
            has_lowerUPPERcase: false,
            has_specialchar: false,
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
            activeItem: "info",
            eye: "right: 1%;",
            userPicture: "images/PlaceholderUser.png",
            overlayLoader: false,
            QR: "/api/mobile/qrcode",
            token: "",
            requestSentSuccess: false,
            requestSentFail: false,
            duplicatePasswords: false,
            incorrectPassword: false,
            incorrectToken: false,
            pwdInHistory: "5",
            s0: "احراز هویت متمرکز شرکت نفت فلات قاره ایران",
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
            s23: "./profile",
            s24: "آیا از اعمال این تغییرات اطمینان دارید؟",
            s25: "./privacy",
            s26: "رمز عبور فعلی",
            s27: "پیکربندی",
            s28: "./configs",
            s29: "./events",
            s30: "برنامه موبایل",
            s31: "لطفا کد QR را جهت فعال سازی برنامه موبایل، اسکن کنید.",
            s32: "کد تایید با موفقیت ارسال شد. لطفا به لیست پیامک های دریافتی خود مراجعه نموده و کد ارسال شده را وارد نمایید.",
            s33: "لطفا جهت تغییر رمز عبور خود، پس از درخواست کد تایید، کد ارسال شده از طریق پیامک را وارد نمایید.",
            s34: "درخواست کد تایید",
            s35: "تایید",
            s36: "کد تایید را وارد نمایید",
            s37: "خطایی رخ داده است. لطفا از صحت شماره موبایل خود اطمینان حاصل فرمایید.",
            s38: "لطفا برای دریافت برنامه موبایل بر روی لینک زیر کلیک کنید.",
            s39: "بازگشت",
            s40: "./dashboard",
            s41: "ممیزی ها",
            s42: "/audits",
            s43: "رمز عبور جدید و " + "5" + " رمز عبور آخر نباید یکسان باشند.",
            s44: "رمز عبور فعلی اشتباه است.",
            s45: "کد تایید اشتباه است.",
            rolesText: "نقش ها",
            rolesURLText: "./roles",
            reportsText: "گزارش ها",
            reportsURLText: "./reports",
            publicmessagesText: "اعلان های عمومی",
            publicmessagesURLText: "./publicmessages",
            ticketingText: "پشتیبانی",
            ticketingURLText: "./ticketing",
            transcriptsText: "گزارش های دسترسی",
            transcriptsURLText: "./transcripts",
            emailFormatErrorText: "فرمت آدرس ایمیل را به درستی وارد کنید",
            mobileFormatErrorText: "فرمت شماره تلفن را به درستی وارد کنید",
            inputEnglishFilterText: " (تنها حروف انگلیسی و اعداد مجاز می باشند)",
            inputPersianFilterText: " (تنها حروف فارسی و اعداد مجاز می باشند)",
            showMeeting: false,
            meetingInviteLinkStyle: "border-top-left-radius: 0;border-bottom-left-radius: 0;",
            meetingInviteLinkCopyStyle: "border-top-right-radius: 0;border-bottom-right-radius: 0;",
            meetingAdminLink: "",
            meetingGuestLink: "",
            meetingText: "جلسه مجازی",
            enterMeetingText: "ورود به جلسه",
            inviteToMeetingText: "دعوت به جلسه",
            copyText: "کپی",
            returnText: "بازگشت",
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
            U17: "تغییر اطلاعات کاربری",
            U18: "کد پرسنلی"
        },
        created: function () {
            this.setDateNav();
            this.getUserInfo();
            this.getUserPic();
            this.getQR();
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
            dropdownNavbar: function () {
                if(this.dropdownMenu){
                    let dropdowns = document.getElementsByClassName("dropdown-content");
                    for (let i = 0; i < dropdowns.length; ++i) {
                        let openDropdown = dropdowns[i];
                        if(openDropdown.classList.contains("show")) {
                            openDropdown.classList.remove("show");
                        }
                    }
                    this.dropdownMenu = false;
                }else{
                    document.getElementById("dropdownMenu").classList.toggle("show");
                    this.dropdownMenu = true;
                }
            },
            openMeeting: function () {
                window.open(this.meetingAdminLink, "_blank").focus();
            },
            openOverlay: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                document.getElementById("overlay").style.display = "block";
                this.overlayLoader = true;
                axios.get(url + "/api/skyroom") //
                    .then((res) => {
                        vm.overlayLoader = false;
                        document.getElementById("overlayBody").style.display = "block";
                        vm.meetingAdminLink = res.data.presenter;
                        vm.meetingGuestLink = res.data.students;
                    });
            },
            closeOverlay: function () {
                document.getElementById("overlay").style.display = "none";
                document.getElementById("overlayBody").style.display = "none";
            },
            copyMeetingLink: function () {
                let copyText = document.getElementById("copyMeetingLink");
                copyText.select();
                document.execCommand("copy");
                document.getElementById("copyMeetingLinkBtn").disabled = true;
                setTimeout(function(){ document.getElementById("copyMeetingLinkBtn").disabled = false; }, 3000);
            },
            isActive (menuItem) {
                return this.activeItem === menuItem
            },
            setActive (menuItem) {
                this.activeItem = menuItem
            },
            backDashboard: function () {
                window.location.href = this.s40;
            },
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user") //
                    .then((res) => {
                        vm.userInfo = res.data;
                        vm.username = res.data.userId;
                        vm.name = res.data.displayName;
                        vm.nameEN = res.data.firstName + " " + res.data.lastName;
                        if(window.localStorage.getItem("lang") === null || window.localStorage.getItem("lang") === "FA"){
                            vm.s1 = vm.name;
                        }else if(window.localStorage.getItem("lang") === "EN") {
                            vm.s1 = vm.nameEN;
                        }
                        if(res.data.profileInaccessibility){
                            document.getElementById("userInfo.firstNameUpdate").readOnly = true;
                            document.getElementById("userInfo.lastNameUpdate").readOnly = true;
                            document.getElementById("userInfo.displayNameUpdate").readOnly = true;
                            document.getElementById("userInfo.mobileUpdate").readOnly = true;
                            document.getElementById("userInfo.mailUpdate").readOnly = true;
                            document.getElementById("userInfo.employeeNumberUpdate").readOnly = true;
                            document.getElementById("userInfo.descriptionUpdate").readOnly = true;
                            document.getElementById("editButton").style = "display: none;";
                            vm.U17 = "اطلاعات کاربری";
                        }
                        if(res.data.skyroomAccess){
                            vm.showMeeting = true;
                        }
                    });
            },
            getUserPic: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user/photo") //
                    .then((res) => {
                        if(res.data == "Problem" || res.data == "NotExist"){
                            vm.userPicture = "images/PlaceholderUser.png";
                        }else{
                            vm.userPicture = "/api/user/photo";
                        }
                    })
                    .catch((error) => {
                        if (error.response) {
                            if (error.response.status == 400 || error.response.status == 500 || error.response.status == 403) {
                                vm.userPicture = "images/PlaceholderUser.png";
                            }else{
                                vm.userPicture = "/api/user/photo";
                            }
                        }
                    });
            },
            getQR: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                this.QR = url + this.QR;
            },
            editUser: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                const emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                const mobileRegex = /^09\d{9}$/;

                if(document.getElementById("userInfo.displayNameUpdate").value == "" ||
                document.getElementById("userInfo.mobileUpdate").value == "" ||
                document.getElementById("userInfo.mailUpdate").value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else if(!emailRegex.test(document.getElementById("userInfo.mailUpdate").value)){
                    alert(this.emailFormatErrorText);
                }else if(!mobileRegex.test(document.getElementById("userInfo.mobileUpdate").value)){
                    alert(this.mobileFormatErrorText);
                }else{
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
                                mobile: document.getElementById('userInfo.mobileUpdate').value,
                                mail: document.getElementById('userInfo.mailUpdate').value,
                                employeeNumber: document.getElementById('userInfo.employeeNumberUpdate').value,
                                description: document.getElementById('userInfo.descriptionUpdate').value
                            }).replace(/\\\\/g, "\\")
                        }).then((res) => {
                            location.replace(url + "/profile"); //
                        });
                    }
                }
            },
            editPass: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                if(document.getElementById('token').value != ""){
                    let check = confirm(this.s24);
                    if (check == true) {
                        axios({
                            method: "put",
                            url: url + "/api/user/password",   //
                            headers: {"Content-Type": "application/json"},
                            data: JSON.stringify({
                                newPassword: document.getElementById("newPassword").value,
                                currentPassword: "dummyPassword",
                                token: document.getElementById("token").value
                            }).replace(/\\\\/g, "\\")
                        }).then((res) => {
                            location.replace(url + "/profile"); //
                        }).catch((error) => {
                            if(error.response) {
                                if(error.response.status === 302) {
                                    vm.pwdInHistory = String(error.data);
                                    vm.duplicatePasswords = true;
                                    setTimeout(function(){ vm.duplicatePasswords = false; }, 5000);
                                }else if(error.response.status === 403) {
                                    vm.incorrectPassword = true;
                                    setTimeout(function(){ vm.incorrectPassword = false; }, 5000);
                                }else if(error.response.status === 405) {
                                    vm.incorrectToken = true;
                                    setTimeout(function(){ vm.incorrectToken = false; }, 5000);
                                }
                            }
                        });
                    }
                }else{
                    alert(this.s36);
                }
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
            sendToken: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                if(this.userInfo.mobile == "" || this.userInfo.mobile == null){
                    this.requestSentFail = true;
                    this.requestSentSuccess = false;
                }else{
                    this.requestSentFail = false;
                    this.requestSentSuccess = true;
                    axios.get(url + "/api/user/password/request") //
                        .then((res) => {
                            vm.setCountdown(res.data * 60);
                            document.getElementById("countdownBtn").disabled = true;
                            setTimeout(function(){ vm.requestSentSuccess = false; }, 5000);
                        });
                }
                
            },
            setCountdown: function (duration) {
                var timer = duration, minutes, seconds;
                var cd = setInterval(function () {
                    minutes = parseInt(timer / 60, 10)
                    seconds = parseInt(timer % 60, 10);
            
                    minutes = minutes < 10 ? "0" + minutes : minutes;
                    seconds = seconds < 10 ? "0" + seconds : seconds;
            
                    document.querySelector("#countdown").textContent = " (" + minutes + ":" + seconds + ") ";
            
                    if (--timer < 0) {
                        clearInterval(cd);
                        document.querySelector("#countdown").textContent = "";
                        document.getElementById("countdownBtn").disabled = false;
                    }
                }, 1000);
            },
            persianInputCharCheck ($event) {
                if($event.type == "keypress"){
                    let key = ($event.key ? $event.key : $event.which);
                    let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
                    if(keyCode < 48 || 57 < keyCode){
                        if(32 < keyCode && keyCode < 65){
                            $event.preventDefault();
                        }else if(90 < keyCode && keyCode < 97){
                            $event.preventDefault();
                        }else if(122 < keyCode && keyCode < 127){
                            $event.preventDefault();
                        }else if(!persianRex.text.test(key)){
                            $event.preventDefault();
                        }
                    }
                }else if ($event.type == "paste"){
                    let text = $event.clipboardData.getData("text");
                    for(let i = 0; i < text.length; ++i){
                        if(text[i].charCodeAt(0) < 48 || 57 < text[i].charCodeAt(0)){
                            if(32 < text[i].charCodeAt(0) && text[i].charCodeAt(0) < 65){
                                $event.preventDefault();
                                break;
                            }else if(90 < text[i].charCodeAt(0) && text[i].charCodeAt(0) < 97){
                                $event.preventDefault();
                                break;
                            }else if(122 < text[i].charCodeAt(0) && text[i].charCodeAt(0) < 127){
                                $event.preventDefault();
                                break;
                            }else if(!persianRex.text.test(text[i])){
                                $event.preventDefault();
                                break;
                            }
                        }
                    }
                }
            },
            englishInputCharCheck ($event) {
                if($event.type == "keypress"){
                    let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
                    if (keyCode < 48 || 122 < keyCode) {
                        $event.preventDefault();
                    }else if (57 < keyCode  && keyCode < 65) {
                        $event.preventDefault();
                    }else if (90 < keyCode  && keyCode < 97) {
                        $event.preventDefault();
                    }
                }else if ($event.type == "paste"){
                    let text = $event.clipboardData.getData("text");
                    for(let i = 0; i < text.length; ++i){
                        if (text[i].charCodeAt(0) < 48 || 122 < text[i].charCodeAt(0)) {
                            $event.preventDefault();
                            break;
                        }else if (57 < text[i].charCodeAt(0)  && text[i].charCodeAt(0) < 65) {
                            $event.preventDefault();
                            break;
                        }else if (90 < text[i].charCodeAt(0)  && text[i].charCodeAt(0) < 97) {
                            $event.preventDefault();
                            break;
                        }
                    }
                }
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    window.localStorage.setItem("lang", "EN");
                    this.placeholder = "text-align: left;";
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.font = "font-size: 0.9em; text-align: left;";
                    this.isRtl = false;
                    this.eye = "left: 1%;";
                    this.dateNavText = this.dateNavEn;
                    this.s0 = "IOOC Centralized Authentication";
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
                    this.s17 = "New Password";
                    this.s18 = "Repeat New Password";
                    this.s19 = "Your Password Must Meet All Of The Following Criteria:";
                    this.s20 = "Dashboard";
                    this.s21 = "Passwords Don't Match";
                    this.s24 = "Are You Sure You Want To Edit?";
                    this.s26 = "Current Password";
                    this.s27 = "Configs";
                    this.s30 = "Mobile Application";
                    this.s31 = "Please Scan The QR Code To Activate The App.";
                    this.s32 = "Verification Code Sent Successfully. Please Go To Your SMS Inbox And Enter The Code We Sent You.";
                    this.s33 = "To Change Your Password, Please Request a Verification Code And Then Enter the Sent Code.";
                    this.s34 = "Request Code";
                    this.s35 = "Submit";
                    this.s36 = "Enter The Verification Code";
                    this.s37 = "An Error Has Occurred. Please Make Sure Your Mobile Number Is Valid.";
                    this.s38 = "Please Click On The Link Below To Download The App.";
                    this.s39 = "Go Back";
                    this.s41 = "Audits";
                    this.s43 = "New Password Should Not be Same as the Last " + this.pwdInHistory + " Passwords.";
                    this.s44 = "Current Password is Incorrect.";
                    this.s45 = "Verification Code is Incorrect.";
                    this.rolesText = "Roles";
                    this.reportsText = "Reports";
                    this.publicmessagesText = "Public Messages";
                    this.ticketingText = "Ticketing";
                    this.transcriptsText = "Access Reports";
                    this.emailFormatErrorText = "Enter Email Address Format Correctly";
                    this.mobileFormatErrorText = "Enter Phone Number Format Correctly";
                    this.inputEnglishFilterText = " (Only English Letters And Numbers Are Allowed)";
                    this.inputPersianFilterText = " (Only Persian Letters And Numbers Are Allowed)";
                    this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingText = "Meeting";
                    this.enterMeetingText = "Enter Meeting";
                    this.inviteToMeetingText = "Invite To Meeting";
                    this.copyText = "Copy";
                    this.returnText = "Return";
                    this.U0 = "Password";
                    this.U1 = "Users";
                    this.U2 = "ID";
                    this.U3 = "First Name (In English)";
                    this.U4 = "Last Name (In English)";
                    this.U5 = "FullName (In Persian)";
                    this.U6 = "Phone";
                    this.U7 = "Email";
                    this.U8 = "NID";
                    this.U9 = "Description";
                    this.U10 = "Update";
                    this.U11 = "Delete"
                    this.U12 = "Add New User";
                    this.U13 = "Edit";
                    this.U16 = "Edit Password";
                    if(this.userInfo.profileInaccessibility){
                        this.U17 = "User Information";
                    }else {
                        this.U17 = "Edit User Information";
                    }
                    this.U18 = "Employee Number";
                    this.rules[0].message = "- One Lowercase or Uppercase English Letter Required.";
                    this.rules[1].message = "- One special Character or Persian Letter Required.";
                    this.rules[2].message = "- 8 Characters Minimum.";
                    this.rules[3].message = "- One Number Required.";
                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;";
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.font = "font-size: 0.74em; text-align: right;";
                    this.isRtl = true;
                    this.eye = "right: 1%;";
                    this.dateNavText = this.dateNav;
                    this.s0 = "احراز هویت متمرکز شرکت نفت فلات قاره ایران";
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
                    this.s17 = "رمز عبور جدید";
                    this.s18 = "تکرار رمز عبور جدید";
                    this.s19 = "رمز عبور شما باید شامل موارد زیر باشد:";
                    this.s20 = "داشبورد";
                    this.s21 = "رمز عبور های وارد شده یکسان نمی باشند";
                    this.s24 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s26 = "رمز عبور فعلی";
                    this.s27 = "پیکربندی";
                    this.s30 = "برنامه موبایل";
                    this.s31 = "لطفا کد QR را جهت فعال سازی برنامه موبایل، اسکن کنید.";
                    this.s32 = "کد تایید با موفقیت ارسال شد. لطفا به لیست پیامک های دریافتی خود مراجعه نموده و کد ارسال شده را وارد نمایید.";
                    this.s33 = "لطفا جهت تغییر رمز عبور خود، پس از درخواست کد تایید، کد ارسال شده از طریق پیامک را وارد نمایید.";
                    this.s34 = "درخواست کد تایید";
                    this.s35 = "تایید";
                    this.s36 = "کد تایید را وارد نمایید";
                    this.s37 = "خطایی رخ داده است. لطفا از صحت شماره موبایل خود اطمینان حاصل فرمایید.";
                    this.s38 = "لطفا برای دریافت برنامه موبایل بر روی لینک زیر کلیک کنید.";
                    this.s39 = "بازگشت";
                    this.s41 = "ممیزی ها";
                    this.s43 = "رمز عبور جدید و " + this.pwdInHistory + " رمز عبور آخر نباید یکسان باشند.";
                    this.s44 = "رمز عبور فعلی اشتباه است.";
                    this.s45 = "کد تایید اشتباه است.";
                    this.rolesText = "نقش ها";
                    this.reportsText = "گزارش ها";
                    this.publicmessagesText = "اعلان های عمومی";
                    this.ticketingText = "پشتیبانی";
                    this.transcriptsText = "گزارش های دسترسی";
                    this.emailFormatErrorText = "فرمت آدرس ایمیل را به درستی وارد کنید";
                    this.mobileFormatErrorText = "فرمت شماره تلفن را به درستی وارد کنید";
                    this.inputEnglishFilterText = " (تنها حروف انگلیسی و اعداد مجاز می باشند)";
                    this.inputPersianFilterText = " (تنها حروف فارسی و اعداد مجاز می باشند)";
                    this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingText = "جلسه مجازی";
                    this.enterMeetingText = "ورود به جلسه";
                    this.inviteToMeetingText = "دعوت به جلسه";
                    this.copyText = "کپی";
                    this.returnText = "بازگشت";
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
                    if(this.userInfo.profileInaccessibility){
                        this.U17 = "اطلاعات کاربری";
                    }else {
                        this.U17 = "تغییر اطلاعات کاربری";
                    }
                    this.U18 = "کد پرسنلی";
                    this.rules[0].message = "حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ";
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
    })
})
