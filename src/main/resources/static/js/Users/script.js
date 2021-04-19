document.addEventListener('DOMContentLoaded', function () {
    var router = new VueRouter({
        mode: 'history',
        routes: []
    });
    Vue.component('v-pagination', window['vue-plain-pagination'])
    new Vue({
        router,
        el: '#app',
        data: {
            dropdownMenu: false,
            dateNav: "",
            dateNavEn: "",
            dateNavText: "",
            recordsShownOnPage: 20,
            currentPage: 1,
            paginationCurrentPage: 1,
            currentSort: "id",
            currentSortDir: "asc",
            userInfo: [],
            email: "",
            username: "",
            name: "",
            nameEN: "",
            changePageUsers: false,
            users: [],
            usersPage: [],
            userListImport: [],
            usersConflictList: [],
            usernameList: [],
            usersCorrectList: [],
            usersCorrectUserIdList: [],
            message: "",
            groups: [],
            groupListCreate: "",
            groupListUpdate: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            editS: "display:none",
            addS: "display:none",
            showS: "",
            ShowSExtra: "display:none",
            total: 1,
            ImportedFile: '',
            bootstrapPaginationClasses: {
                ul: 'pagination',
                li: 'page-item',
                liActive: 'active',
                liDisable: 'disabled',
                button: 'page-link'  
            },
            paginationAnchorTexts: {
                first: '<<',
                prev: '<',
                next: '>',
                last: '>>'
            },
            margin1: "ml-1",
            margin3: "ml-3",
            margin5: "ml-5",
            col1Style: "padding-left: .5rem !important; padding-right: 0 !important; display: inline-flex;",
            col2Style: "padding-left: 0 !important; padding-right: .5rem !important; display: inline-flex;",
            padding0Left: "padding-left: 0rem;",
            padding0Right: "padding-right: 0rem;",
            userPicture: "images/PlaceholderUser.png",
            activeItem: "info",
            activeItemMain: "list",
            eye: "right: 1%;",
            font: "font-size: 0.74em; text-align: right;",
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
            userFound : false,
            showCreate: false,
            showRCreate: false,
            showCCreate: false,
            has_numberCreate: false,
            has_lowerUPPERcaseCreate: false,
            has_specialcharCreate: false,
            has_charCreate: false,
            passwordCreate: "",
            checkPasswordCreate: "",
			passwordVisibleCreate: true,
            submittedCreate: false,
            userStatus: "",
            resetPassEmailSent: false,
            usersImported: false,
            allIsSelected: false,
            userIdm2MFlag: false,
            userIdM2mFlag: false,
            displayNamem2MFlag: false,
            displayNameM2mFlag: false,
            searchUserId: "",
            searchDisplayName: "",
            searchGroup: "",
            searchStatus: "",
            promptImportButtons: false,
            loader: false,
            loader1: false,
            duplicatePasswords: false,
            showUnDeletableList: false,
            unDeletableList: "",
            isListEmpty: false,
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
            s23: "فایلی انتخاب نشده است.",
            s24: "از فرمت فایل انتخابی پشتیبانی نمی شود.",
            s25: "سایز فایل انتخابی بیش از 100M می باشد.",
            s26: "آیا از اعمال این تغییرات اطمینان دارید؟",
            s27: "آیا از افزودن این کاربر اطمینان دارید؟",
            s28: "آیا از حذف این کاربر اطمینان دارید؟",
            s29: "آیا از حذف تمامی کاربران اطمینان دارید؟",
            s30: "./privacy",
            s31: "پیکربندی",
            s32: "./configs",
            s33: "رفع تناقض وارد کردن کاربران جدید",
            s34: "کاربر قدیمی",
            s35: "کاربر جدید",
            s36: "اعمال تغییرات",
            s37: "ویرایش کاربر",
            s38: "ایجاد کاربر",
            s39: "./events",
            s40: "وضعیت کاربر",
            s41: "فعال",
            s42: "غیر فعال",
            s43: "قفل شده",
            s44: "اطلاعات کاربر",
            s45: "رمز عبور",
            s46: "رمز عبور جدید",
            s47: "تکرار رمز عبور جدید",
            s48: "رمز عبور شما باید شامل موارد زیر باشد:",
            s49: "رمز عبور های وارد شده یکسان نمی باشند",
            s50: "کاربری با این شناسه کاربری وجود دارد، شناسه کاربری دیگری انتخاب کنید.",
            s51: "زمان انقضا کاربر",
            s52: "ایمیل بازنشانی رمز عبور با موفقیت ارسال شد.",
            s53: "حذف کاربر",
            s54: "اعمال",
            s55: "آیا از حذف کاربران انتخاب شده اطمینان دارید؟",
            s56: "هیچ کاربری انتخاب نشده است.",
            s57: "بازنشانی رمزعبور همه",
            s58: "جدید",
            s59: "بازنشانی",
            s60: "فایل نمونه",
            s61: "نام فارسی",
            s62: "گروه",
            s63: "وضعیت",
            s64: "فعال",
            s65: "غیرفعال",
            s66: "قفل شده",
            s67: "اعمال فیلتر",
            s68: "حذف فیلتر",
            s69: "",
            s70: "",
            s71: "از مجموع کاربران، تعداد ",
            s72: " کاربر با موفقیت ثبت شده و تعداد ",
            s73: " کاربر در هنگام ثبت با مشکل مواجه شده و ثبت نشده اند.",
            s74: " (برای شناسه کاربری تنها حروف انگلیسی و اعداد مجاز می باشد)",
            s75: "تعداد رکورد ها: ",
            s76: "لیست کاربران",
            s77: "آیا اطلاعات جدید کاربران تکراری، جایگزین اطلاعات فعلی آنها شود؟",
            s78: "خیر",
            s79: "بله",
            s80: "ممیزی ها",
            s81: "/audits",
            s82: "رمز عبور جدید و رمز عبور قدیمی نباید یکسان باشند.",
            s83: "غیرقابل حذف",
            s84: "کاربران زیر غیرقابل حذف می باشند.",
            s85: "کاربری یافت نشد",
            rolesText: "نقش ها",
            rolesURLText: "./roles",
            reportsText: "گزارش ها",
            reportsURLText: "./reports",
            importUserListGroupError: false,
            importUserListGroupErrorList: "",
            importUserListGroupErrorText: "گروه های مشخص شده برای کاربران زیر، تعریف نشده اند.",
            importUserListRepetitiveError: false,
            importUserListRepetitiveErrorList: "",
            importUserListRepetitiveErrorText: "کاربران زیر، تکراری می باشند.",
            importUserListOverrideResault: false,
            importUserListOverrideSuccessful: "",
            importUserListOverrideUnsuccessful: "",
            importUserListOverride1Text: "از مجموع کاربران، اطلاعات ",
            importUserListOverride2Text: " کاربر با موفقیت جایگزین شده و اطلاعات ",
            importUserListOverride3Text: " کاربر با مشکل مواجه شده و جایگزین نشده است.",
            emailFormatErrorText: "فرمت آدرس ایمیل را به درستی وارد کنید",
            mobileFormatErrorText: "فرمت شماره تلفن را به درستی وارد کنید",
            U0: "رمز عبور",
            U1: "کاربران",
            U2: "شناسه کاربری",
            U3: "نام (به انگلیسی)",
            U4: "نام خانوادگی (به انگلیسی)",
            U5: "نام کامل (به فارسی)",
            U6: "شماره تلفن",
            U7: "ایمیل",
            U8: "کد ملی",
            U9: "توضیحات",
            U10: "به روزرسانی",
            U11: "حذف",
            U12: "کاربر جدید",
            U13: "ویرایش",
            U14: "گروه های عضو",
            U15: "تکرار رمز عبور",
            U16: "کاربر مورد نظر در گروههای زیر عضویت دارد. کاربر مورد نظر از چه گروههایی حذف شود؟",
            U17: "حذف همه",
            U18: "وارد کردن کاربران با فایل",
            U19: "ذخیره سازی داده ها در فایل",
            U20: "وارد کردن کاربران با فایل",
            U21: "بارگزاری",
            U22: "بازنشانی رمز عبور",
            U23: "فایل الگو",
            U24: "کد پرسنلی"
        },
        created: function () {
            this.setDateNav();
            this.getUserInfo();
            this.getUserPic();
            this.getUsers();
            this.getGroups();
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
            isActive (menuItem) {
                return this.activeItem === menuItem;
            },
            setActive (menuItem) {
                this.activeItem = menuItem;
            },
            isActiveMain (menuItem) {
                return this.activeItemMain === menuItem;
            },
            setActiveMain (menuItem) {
                this.activeItemMain = menuItem;
            },
            importUserList: function(override){
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.importUserListRepetitiveError = false;
                if(override){
                    this.usersImported = false;
                    this.importUserListGroupError = false;
                    this.loader = true;
                    axios({
                        method: 'put',
                        url: url + "/api/users/import/massUpdate",  //
                        headers: {'Content-Type': 'application/json'},
                        data: vm.userListImport
                    }).then((res) => {
                        vm.loader = false;
                        vm.importUserListOverrideSuccessful = res.data.nSuccessful;
                        vm.importUserListOverrideUnsuccessful = res.data.nUnSuccessful;
                        vm.importUserListOverrideResault = true;
                        vm.usersImported = true;
                        vm.filter();
                    }).catch((error) => {
                        vm.loader = false;
                    });
                }
            },
            selectedFile() {
                let re = /(\.xlsx)$/i;
                let fup = document.getElementById('file');
                let fileName = fup.value;
                if(!re.exec(fileName)){
                    alert(this.s24);
                }else{
                    var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                    var vm = this;
                    var bodyFormData = new FormData();
                    var file = document.querySelector('#file');
                    bodyFormData.append("file", file.files[0]);
                    this.userListImport = [];
                    this.usersImported = false;
                    this.importUserListGroupError = false;
                    this.importUserListRepetitiveError = false;
                    this.importUserListOverrideResault = false;
                    this.loader = true;
                    axios({
                        method: 'post',
                        url: url + "/api/users/import",  //
                        headers: {'Content-Type': 'multipart/form-data'},
                        data: bodyFormData,
                    })
                    .then((res) => {
                        vm.loader = false;
                        vm.s69 = res.data.nSuccessful;
                        vm.s70 = res.data.nUnSuccessful;
                        vm.usersImported = true;
                        setTimeout(function(){ vm.usersImported = false; }, 10000);
                        vm.filter();
                    }).catch((error) => {
                        vm.loader = false;
                        if(error.response.status === 302){
                            vm.s69 = error.response.data.nSuccessful;
                            vm.s70 = error.response.data.nUnSuccessful;
                            vm.usersImported = true;
                            if(typeof error.response.data.invalidGroups !== "undefined"){
                                if(error.response.data.invalidGroups.length != 0){
                                    vm.importUserListGroupErrorList = "";
                                    for(let i = 0; i < error.response.data.invalidGroups.length-1; ++i){
                                        vm.importUserListGroupErrorList = vm.importUserListGroupErrorList + error.response.data.invalidGroups[i].userId + ", ";
                                    }
                                    vm.importUserListGroupErrorList = vm.importUserListGroupErrorList + error.response.data.invalidGroups[error.response.data.invalidGroups.length-1].userId;
                                    vm.importUserListGroupError = true;
                                }
                            }
                            if(typeof error.response.data.repetitiveUsers !== "undefined"){
                                if(error.response.data.repetitiveUsers.length != 0){
                                    vm.importUserListRepetitiveErrorList = "";
                                    for(let i = 0; i < error.response.data.repetitiveUsers.length-1; ++i){
                                        vm.importUserListRepetitiveErrorList = vm.importUserListRepetitiveErrorList + error.response.data.repetitiveUsers[i].new.userId + ", ";
                                        vm.userListImport.push(error.response.data.repetitiveUsers[i].new);
                                    }
                                    vm.importUserListRepetitiveErrorList = vm.importUserListRepetitiveErrorList + error.response.data.repetitiveUsers[error.response.data.repetitiveUsers.length-1].new.userId;
                                    vm.userListImport.push(error.response.data.repetitiveUsers[error.response.data.repetitiveUsers.length-1].new);
                                    vm.importUserListRepetitiveError = true;
                                }
                            }
                            vm.filter();
                        }
                    });
                }
            },
            changeRecords: function(event) {
                this.recordsShownOnPage = event.target.value;
                this.filter();
            },
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user") //
                    .then((res) => {
                        vm.username = res.data.userId;
                        vm.name = res.data.displayName;
                        vm.nameEN = res.data.firstName + " " + res.data.lastName;
                        if(window.localStorage.getItem("lang") === null || window.localStorage.getItem("lang") === "FA"){
                            vm.s1 = vm.name;
                        }else if(window.localStorage.getItem("lang") === "EN") {
                            vm.s1 = vm.nameEN;
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
            getGroups: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/groups")
                .then((res) => {
                  vm.groups = res.data;
                });
            },
            addGroupCreate: function (n) {
                n = n.split("'").join("");
                if(this.groupListCreate.includes(n)){
                  if(this.groupListCreate.includes(n + ',')){
                    this.groupListCreate = this.groupListCreate.replace(n + ',', "");
                  }else if(this.groupListCreate === n){
                    this.groupListCreate = this.groupListCreate.replace(n, "");
                  }else{
                    this.groupListCreate = this.groupListCreate.replace(',' + n, "");
                  }
                }else{
                  if(this.groupListCreate === ""){
                    this.groupListCreate += n;
                  }else{
                    this.groupListCreate += ',' + n;
                  }
                }
            },
            addGroupUpdate: function (n) {
                n = n.split("'").join("");
                if(this.groupListUpdate.includes(n)){
                  if(this.groupListUpdate.includes(n + ',')){
                    this.groupListUpdate = this.groupListUpdate.replace(n + ',', "");
                  }else if(this.groupListUpdate === n){
                    this.groupListUpdate = this.groupListUpdate.replace(n, "");
                  }else{
                    this.groupListUpdate = this.groupListUpdate.replace(',' + n, "");
                  }
                }else{
                  if(this.groupListUpdate === ""){
                    this.groupListUpdate += n;
                  }else{
                    this.groupListUpdate += ',' + n;
                  }
                }
            },
            getUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let tempUsers = {};
                this.users = [];
                this.isListEmpty = false;
                this.loader1 = true;
                axios.get(url + "/api/users/" + vm.currentPage + "/" + vm.recordsShownOnPage) //
                    .then((res) => {
                        vm.total = res.data.pages;
                        res.data.userList.forEach(function (item) {
                            tempUsers = {};
                            tempUsers.userId = item.userId;
                            tempUsers.displayName = item.displayName;
                            tempUsers.memberOf = item.memberOf;
                            vm.users.push(tempUsers);
                        });
                        for(let i = 0; i < vm.recordsShownOnPage && i < res.data.size; ++i){
                            vm.users[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
                        }
                        vm.loader1 = false;
                        if(res.data.userList.length == 0){
                            vm.isListEmpty = true;
                        }
                    }).catch((error) => {
                        vm.loader1 = false;
                    });
            },
            filter: function (p) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let searchQuery = "?";
                searchQuery = searchQuery + "searchUid=";
                if(this.searchUserId != ""){
                    searchQuery = searchQuery + this.searchUserId + "&";
                }else{
                    searchQuery = searchQuery + "&";
                }
                searchQuery = searchQuery + "searchDisplayName=";
                if(this.searchDisplayName != ""){
                    searchQuery = searchQuery + this.searchDisplayName + "&";
                }else{
                    searchQuery = searchQuery + "&";
                }
                searchQuery = searchQuery + "groupFilter=";
                if(this.searchGroup != ""){
                    searchQuery = searchQuery + this.searchGroup + "&";
                }else{
                    searchQuery = searchQuery + "&";
                }
                searchQuery = searchQuery + "userStatus=";
                if(this.searchStatus != ""){
                    searchQuery = searchQuery + this.searchStatus + "&";
                }else{
                    searchQuery = searchQuery + "&";
                }
                searchQuery = searchQuery + "sortType=";
                if(this.userIdm2MFlag){
                    searchQuery = searchQuery + "uid_m2M";
                }else if(this.userIdM2mFlag){
                    searchQuery = searchQuery + "uid_M2m";
                }else if(this.displayNamem2MFlag){
                    searchQuery = searchQuery + "displayName_m2M";
                }else if(this.displayNameM2mFlag){
                    searchQuery = searchQuery + "displayName_M2m";
                }
                if(p != "paginationCurrentPage"){
                    this.currentPage = 1;
                    this.paginationCurrentPage = this.currentPage;
                }
                let tempUsers = {};
                this.users = [];
                this.isListEmpty = false;
                this.loader1 = true;
                axios.get(url + "/api/users/" + vm.currentPage + "/" + vm.recordsShownOnPage + searchQuery) //
                    .then((res) => {
                        vm.total = res.data.pages;
                        res.data.userList.forEach(function (item) {
                            tempUsers = {};
                            tempUsers.userId = item.userId;
                            tempUsers.displayName = item.displayName;
                            tempUsers.memberOf = item.memberOf;
                            vm.users.push(tempUsers);
                        });
                        for(let i = 0; i < vm.recordsShownOnPage && i < res.data.size; ++i){
                            vm.users[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
                        }
                        vm.loader1 = false;
                        if(res.data.userList.length == 0){
                            vm.isListEmpty = true;
                        }
                    }).catch((error) => {
                        vm.loader1 = false;
                    });
                this.changePageUsers = false;
            },
            changeSelected: function (action) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedUsers = [];
                if(action == "delete"){
                    selectedUsers = [];
                    for(let i = 0; i < vm.users.length; ++i){
                        if(document.getElementById("checkbox-" + vm.users[i].userId).checked){
                            selectedUsers.push(vm.users[i].userId.toString());
                        }
                    }
                    if(selectedUsers.length != 0){
                        var check = confirm(this.s55);
                        if (check == true) {
                            this.loader = true;
                            axios({
                                method: 'delete',
                                url: url + "/api/users", //
                                headers: {'Content-Type': 'application/json'},
                                data: JSON.stringify({
                                    names: selectedUsers
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                vm.loader = false;
                                if(res.status == 206) {
                                    vm.unDeletableList = "";
                                    for(let i = 0; i < res.data.length-1; ++i){
                                        vm.unDeletableList = vm.unDeletableList + res.data[i] + ", ";
                                    }
                                    vm.unDeletableList = vm.unDeletableList + res.data[res.data.length-1];
                                    vm.showUnDeletableList = true;
                                    setTimeout(function(){ vm.showUnDeletableList = false; }, 10000);
                                }
                                vm.filter();
                            }).catch((error) => {
                                vm.loader = false;
                            });
                        }
                    }else{
                        alert(this.s56);
                    }
                }else if(action == "resetEmail"){
                    selectedUsers = [];
                    for(let i = 0; i < vm.users.length; ++i){
                        if(document.getElementById("checkbox-" + vm.users[i].userId).checked){
                            selectedUsers.push(vm.users[i].userId.toString());
                        }
                    }
                    if(selectedUsers.length != 0){
                        this.loader = true;
                        axios({
                            method: 'post',
                            url: url + "/api/users/sendMail", //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                names: selectedUsers
                            }).replace(/\\\\/g, "\\")
                        }).then((res) => {
                            vm.loader = false;
                            vm.resetPassEmailSent = true;
                            setTimeout(function(){ vm.resetPassEmailSent = false; }, 3000);
                        }).catch((error) => {
                            vm.loader = false;
                        });
                    }else{
                        alert(this.s56);
                    }
                }
            },
            deleteFilter: function () {
                this.searchUserId = "";
                this.searchDisplayName = "";
                this.searchGroup = "";
                this.searchStatus = ""
                this.userIdm2MFlag = false;
                this.userIdM2mFlag = false;
                this.displayNamem2MFlag = false;
                this.displayNameM2mFlag = false;
                document.getElementById("userIdmin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("userIdMax2min").style = "border-top: solid 7px black;";
                document.getElementById("displayNamemin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("displayNameMax2min").style = "border-top: solid 7px black;";
                this.filter();
            },
            userIdm2M: function () {
                document.getElementById("userIdmin2Max").style = "border-bottom: solid 7px #007bff;";
                document.getElementById("userIdMax2min").style = "border-top: solid 7px black;";
                document.getElementById("displayNamemin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("displayNameMax2min").style = "border-top: solid 7px black;";
                this.userIdm2MFlag = true;
                this.userIdM2mFlag = false;
                this.displayNamem2MFlag = false;
                this.displayNameM2mFlag = false;
                this.filter();
            },
            userIdM2m: function () {
                document.getElementById("userIdmin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("userIdMax2min").style = "border-top: solid 7px #007bff;";
                document.getElementById("displayNamemin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("displayNameMax2min").style = "border-top: solid 7px black;";
                this.userIdm2MFlag = false;
                this.userIdM2mFlag = true;
                this.displayNamem2MFlag = false;
                this.displayNameM2mFlag = false;
                this.filter();
            },
            displayNamem2M: function () {
                document.getElementById("userIdmin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("userIdMax2min").style = "border-top: solid 7px black;";
                document.getElementById("displayNamemin2Max").style = "border-bottom: solid 7px #007bff;";
                document.getElementById("displayNameMax2min").style = "border-top: solid 7px black;";
                this.userIdm2MFlag = false;
                this.userIdM2mFlag = false;
                this.displayNamem2MFlag = true;
                this.displayNameM2mFlag = false;
                this.filter();
            },
            displayNameM2m: function () {
                document.getElementById("userIdmin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("userIdMax2min").style = "border-top: solid 7px black;";
                document.getElementById("displayNamemin2Max").style = "border-bottom: solid 7px black;";
                document.getElementById("displayNameMax2min").style = "border-top: solid 7px #007bff;";
                this.userIdm2MFlag = false;
                this.userIdM2mFlag = false;
                this.displayNamem2MFlag = false;
                this.displayNameM2mFlag = true;
                this.filter();
            },
            showUsers: function () {
                this.showS = ""
                this.ShowSExtra = "display:none"
                this.addS = "display:none"
                this.editS = "display:none"
                location.reload();
            },
            editUserS: function (id) {
                this.showS = "display:none"
                this.ShowSExtra = ""
                this.addS = "display:none"
                this.editS = ""
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.getGroups();
                axios.get(url + `/api/users/u/${id}`) //
                    .then((res) => {
                        vm.editInfo = res.data;
                        if(typeof res.data.status !== 'undefined'){
                            vm.userStatus = res.data.status;
                            if(res.data.status == "enable"){
                                document.getElementById("option1").selected = true;
                                document.getElementById("option2").selected = false;
                                document.getElementById("option3").selected = false;
                            }else if(res.data.status == "disable"){
                                document.getElementById("option1").selected = false;
                                document.getElementById("option2").selected = true;
                                document.getElementById("option3").selected = false;
                            }else if(res.data.status == "lock"){
                                document.getElementById("option1").selected = false;
                                document.getElementById("option2").selected = false;
                                document.getElementById("option3").selected = true;
                            }
                        }

                        if(typeof res.data.unDeletable !== 'undefined'){
                            if(res.data.unDeletable){
                                document.getElementsByName("unDeletableUpdate")[0].checked = true;
                            }else{
                                document.getElementsByName("unDeletableUpdate")[0].checked = false;
                            }
                        }

                        if(typeof res.data.endTime !== 'undefined'){
                            let seTime = res.data.endTime;
                            persianDate.toCalendar('gregorian');
                            let dayWrapper = new persianDate([seTime.substring(0,4), seTime.substring(4,6), seTime.substring(6,8),
                              seTime.substring(8,10), seTime.substring(10,12), seTime.substring(12,14), seTime.substring(15,18)]);
                            document.getElementById("endTime").value = dayWrapper.toCalendar('persian').format("dddd DD MMMM YYYY  HH:mm  a");
                        }

                        if(typeof res.data.memberOf !== 'undefined'){
                            for(let i = 0; i < res.data.memberOf.length; ++i){
                                document.getElementById("groupNameId" + res.data.memberOf[i]).checked = true;
                                if(vm.groupListUpdate === ""){
                                    vm.groupListUpdate += res.data.memberOf[i];
                                }else{
                                    vm.groupListUpdate += ',' + res.data.memberOf[i];
                                }
                            }
                        }
                    });
            },
            editUser: function (id) {
                const emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                const mobileRegex = /^09\d{9}$/;

                if(id == "" ||
                document.getElementById("editInfo.displayNameUpdate").value == "" ||
                document.getElementById("editInfo.mobileUpdate").value == "" ||
                document.getElementById("editInfo.mailUpdate").value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else if(!emailRegex.test(document.getElementById("editInfo.mailUpdate").value)){
                    alert(this.emailFormatErrorText);
                }else if(!mobileRegex.test(document.getElementById("editInfo.mobileUpdate").value)){
                    alert(this.mobileFormatErrorText);
                }else{
                    let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                    var check = confirm(this.s26);
                    var unDeletableVar = false;

                    var checkedGroups = [];
                    if(document.getElementById('groupsUpdate').value != ""){
                        checkedGroups = document.getElementById('groupsUpdate').value.split(',');
                    }

                    if(document.getElementsByName('unDeletableUpdate')[0].checked){
                        unDeletableVar = true;
                    }else{
                        unDeletableVar = false;
                    }

                    let endTimeFinal = null;
                    if(document.getElementById('endTime').value != ""){
                        let dateEndTemp = document.getElementById('endTime').value.split("  ");
                        let dateEnd = dateEndTemp[0].split(' ');
                        let dateEndFinal;
                        dateEnd[dateEnd.length-1] = this.FaNumToEnNum(dateEnd[dateEnd.length-1]);
                        dateEnd[dateEnd.length-3] = this.FaNumToEnNum(dateEnd[dateEnd.length-3]);

                        switch(dateEnd[dateEnd.length-2]) {
                        case "فروردین":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-01-" + dateEnd[dateEnd.length-3];
                            break;
                        case "اردیبهشت":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-02-" + dateEnd[dateEnd.length-3];
                            break;
                        case "خرداد":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-03-" + dateEnd[dateEnd.length-3];
                            break;
                        case "تیر":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-04-" + dateEnd[dateEnd.length-3];
                            break;
                        case "مرداد":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-05-" + dateEnd[dateEnd.length-3];
                            break;
                        case "شهریور":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-06-" + dateEnd[dateEnd.length-3];
                            break;
                        case "مهر":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-07-" + dateEnd[dateEnd.length-3];
                            break;
                        case "آبان":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-08-" + dateEnd[dateEnd.length-3];
                            break;
                        case "آذر":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-09-" + dateEnd[dateEnd.length-3];
                            break;
                        case "دی":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-10-" + dateEnd[dateEnd.length-3];
                            break;
                        case "بهمن":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-11-" + dateEnd[dateEnd.length-3];
                            break;
                        case "اسفند":
                            dateEndFinal = dateEnd[dateEnd.length-1] + "-12-" + dateEnd[dateEnd.length-3];
                            break;
                        default:
                            console.log("Wrong Input for Month");
                        }

                        let timeEnd = dateEndTemp[1].split(':');

                        timeEnd = this.FaNumToEnNum(timeEnd[0]) + ':' + this.FaNumToEnNum(timeEnd[1]);
                        
                        let dateE = dateEndFinal.split('-');

                        if(parseInt(dateE[1]) < 7){
                        timeEnd = timeEnd + ":00.000+4:30";
                        }else{
                        timeEnd = timeEnd + ":00.000+3:30";
                        }

                        let TempE = timeEnd.split(':');
                        if(TempE[0].length == 1){
                        TempE[0] = '0' + TempE[0];
                        timeEnd = "";
                        for(i = 0; i < TempE.length; ++i){
                            timeEnd = timeEnd + TempE[i] + ':';
                        }
                        timeEnd = timeEnd.substring(0,timeEnd.length-1);
                        }

                        TempE = dateEndFinal.split('-');
                        if(TempE[1].length == 1){
                        TempE[1] = '0' + TempE[1];
                        }
                        if(TempE[2].length == 1){
                        TempE[2] = '0' + TempE[2];
                        }
                        
                        dateEndFinal = TempE[0] + '-' + TempE[1] + '-' + TempE[2];

                        endTimeFinal = dateEndFinal + "T" + timeEnd;
                    }

                    if (check == true) {
                        let statusValue;
                        if(document.getElementById('status').value != "lock"){
                            if(document.getElementById('status').value == "enable"){
                                if(this.userStatus == "enable"){
                                    statusValue = "";
                                }else if(this.userStatus == "disable"){
                                    statusValue = "enable";
                                }else if(this.userStatus == "lock"){
                                    statusValue = "unlock";
                                }
                            }else if(document.getElementById('status').value == "disable"){
                                if(this.userStatus == "enable"){
                                    statusValue = "disable";
                                }else if(this.userStatus == "disable"){
                                    statusValue = "";
                                }else if(this.userStatus == "lock"){
                                    statusValue = "disable";
                                }
                            }
                        }else{
                            statusValue = "";
                        }

                        axios({
                            method: 'put',
                            url: url + '/api/users/u/' + id,  //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                userId: id,
                                firstName: document.getElementById('editInfo.firstNameUpdate').value,
                                lastName: document.getElementById('editInfo.lastNameUpdate').value,
                                displayName: document.getElementById('editInfo.displayNameUpdate').value,
                                mobile: document.getElementById('editInfo.mobileUpdate').value,
                                memberOf: checkedGroups,
                                mail: document.getElementById('editInfo.mailUpdate').value,
                                employeeNumber: document.getElementById('editInfo.employeeNumberUpdate').value,
                                description: document.getElementById('editInfo.descriptionUpdate').value,
                                cStatus: statusValue,
                                unDeletable: unDeletableVar,
                                endTime: endTimeFinal
                            }).replace(/\\\\/g, "\\")
                        })
                        .then((res) => {
                            location.reload();
                        });
                    }
                }
            },
            editPass: function (id) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var check = confirm(this.s26);
                var vm = this;
                if (check == true) {
                    axios({
                        method: 'put',
                        url: url + '/api/users/u/' + id,  //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            userPassword: document.getElementById('newPassword').value
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        location.reload();
                    }).catch((error) => {
                        if (error.response) {
                            if(error.response.status === 302){
                                vm.duplicatePasswords = true;
                                setTimeout(function(){ vm.duplicatePasswords = false; }, 5000);
                            }
                        }
                    });
                }

            },
            exportUsers: function(){
                let url_ = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.loader = true;
                axios({
                    url: url_ + "/api/users/export",
                    method: "GET",
                    responseType: "blob",
                }).then((response) => {
                    vm.loader = false;
                    const url = window.URL.createObjectURL(new Blob([response.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute("download", "users.xls");
                    document.body.appendChild(link);
                    link.click();
                }).catch((error) => {
                    vm.loader = false;
                });
            },
            addUserS: function () {
                this.showS = "display:none"
                this.ShowSExtra = ""
                this.addS = ""
                this.editS = "display:none"
                this.getGroups();
            },
            addUser: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var unDeletableVar = false;
                const emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                const mobileRegex = /^09\d{9}$/;

                if(document.getElementById("editInfo.userIdCreate").value == "" ||
                    document.getElementById("editInfo.displayNameCreate").value == "" ||
                    document.getElementById("editInfo.mailCreate").value == "" ||
                    document.getElementById("editInfo.mobileCreate").value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else if(!emailRegex.test(document.getElementById("editInfo.mailCreate").value)){
                    alert(this.emailFormatErrorText);
                }else if(!mobileRegex.test(document.getElementById("editInfo.mobileCreate").value)){
                    alert(this.mobileFormatErrorText);
                }else{

                    var check = confirm(this.s27);

                    var checkedGroups = [];
                    if(document.getElementById('groupsCreate').value != ""){
                        checkedGroups = document.getElementById('groupsCreate').value.split(',');
                    }

                    if(document.getElementsByName('unDeletableCreate')[0].checked){
                        unDeletableVar = true;
                    }else{
                        unDeletableVar = false;
                    }

                    let endTimeFinal = null;
                    if(document.getElementById('endTimeCreate').value != ""){
                        let dateEndTemp = document.getElementById('endTimeCreate').value.split("  ");
                        let dateEnd = dateEndTemp[0].split(' ');
                        let dateEndFinal;
                        dateEnd[dateEnd.length-1] = this.FaNumToEnNum(dateEnd[dateEnd.length-1]);
                        dateEnd[dateEnd.length-3] = this.FaNumToEnNum(dateEnd[dateEnd.length-3]);


                        switch(dateEnd[dateEnd.length-2]) {
                            case "فروردین":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-01-" + dateEnd[dateEnd.length-3];
                                break;
                            case "اردیبهشت":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-02-" + dateEnd[dateEnd.length-3];
                                break;
                            case "خرداد":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-03-" + dateEnd[dateEnd.length-3];
                                break;
                            case "تیر":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-04-" + dateEnd[dateEnd.length-3];
                                break;
                            case "مرداد":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-05-" + dateEnd[dateEnd.length-3];
                                break;
                            case "شهریور":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-06-" + dateEnd[dateEnd.length-3];
                                break;
                            case "مهر":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-07-" + dateEnd[dateEnd.length-3];
                                break;
                            case "آبان":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-08-" + dateEnd[dateEnd.length-3];
                                break;
                            case "آذر":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-09-" + dateEnd[dateEnd.length-3];
                                break;
                            case "دی":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-10-" + dateEnd[dateEnd.length-3];
                                break;
                            case "بهمن":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-11-" + dateEnd[dateEnd.length-3];
                                break;
                            case "اسفند":
                                dateEndFinal = dateEnd[dateEnd.length-1] + "-12-" + dateEnd[dateEnd.length-3];
                                break;
                            default:
                                console.log("Wrong Input for Month");
                        }

                        let timeEnd = dateEndTemp[1].split(':');

                        timeEnd = this.FaNumToEnNum(timeEnd[0]) + ':' + this.FaNumToEnNum(timeEnd[1]);

                        let dateE = dateEndFinal.split('-');

                        if(parseInt(dateE[1]) < 7){
                            timeEnd = timeEnd + ":00.000+4:30";
                        }else{
                            timeEnd = timeEnd + ":00.000+3:30";
                        }

                        let TempE = timeEnd.split(':');
                        if(TempE[0].length == 1){
                            TempE[0] = '0' + TempE[0];
                            timeEnd = "";
                            for(i = 0; i < TempE.length; ++i){
                                timeEnd = timeEnd + TempE[i] + ':';
                            }
                            timeEnd = timeEnd.substring(0,timeEnd.length-1);
                        }

                        TempE = dateEndFinal.split('-');
                        if(TempE[1].length == 1){
                            TempE[1] = '0' + TempE[1];
                        }
                        if(TempE[2].length == 1){
                            TempE[2] = '0' + TempE[2];
                        }

                        dateEndFinal = TempE[0] + '-' + TempE[1] + '-' + TempE[2];

                        endTimeFinal = dateEndFinal + "T" + timeEnd;
                    }

                    if(check==true) {
                        axios({
                            method: 'post',
                            url: url + "/api/users",  //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                    userId: document.getElementById('editInfo.userIdCreate').value,
                                    firstName: document.getElementById('editInfo.firstNameCreate').value,
                                    lastName: document.getElementById('editInfo.lastNameCreate').value,
                                    displayName: document.getElementById('editInfo.displayNameCreate').value,
                                    mobile: document.getElementById('editInfo.mobileCreate').value,
                                    memberOf: checkedGroups,
                                    mail: document.getElementById('editInfo.mailCreate').value,
                                    employeeNumber: document.getElementById('editInfo.employeeNumberCreate').value,
                                    userPassword: document.getElementById('newPasswordCreate').value,
                                    description: document.getElementById('editInfo.descriptionCreate').value,
                                    status: document.getElementById('statusCreate').value,
                                    unDeletable: unDeletableVar,
                                    endTime: endTimeFinal
                                }
                            ).replace(/\\\\/g, "\\")
                        })
                            .then(() => {
                                location.reload();
                            })
                            .catch((error) => {
                                if (error.response) {
                                    if(error.response.status === 302){
                                        vm.userFound = true;
                                    }
                                }
                            });
                    }
                }
            },
            sendResetEmail(userId) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedUsers = [];
                selectedUsers.push(userId.toString());
                this.loader = true;
                axios({
                    method: 'post',
                    url: url + "/api/users/sendMail", //
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify({
                        names: selectedUsers
                    }).replace(/\\\\/g, "\\")
                })
                .then((res) => {
                    vm.loader = false;
                    vm.resetPassEmailSent = true;
                    setTimeout(function(){ vm.resetPassEmailSent = false; }, 3000);
                }).catch((error) => {
                    vm.loader = false;
                });
            },
            sendAllResetEmail() {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios({
                    method: 'post',
                    url: url + "/api/users/sendMail", //
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify({
                    }).replace(/\\\\/g, "\\")
                })
                .then((res) => {
                    vm.resetPassEmailSent = true;
                    setTimeout(function(){ vm.resetPassEmailSent = false; }, 3000);
                });
            },
            removeError() {
                this.userFound = false;
            },
            deleteUser: function (userId) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedUsers = [];
                selectedUsers.push(userId.toString());
                var check = confirm(this.s28);
                if (check == true) {
                    this.loader = true;
                    axios({
                        method: 'delete',
                        url: url + "/api/users", //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            names: selectedUsers
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        vm.loader = false;
                        if(res.status == 206) {
                            vm.unDeletableList = "";
                            for(let i = 0; i < res.data.length-1; ++i){
                                vm.unDeletableList = vm.unDeletableList + res.data[i] + ", ";
                            }
                            vm.unDeletableList = vm.unDeletableList + res.data[res.data.length-1];
                            vm.showUnDeletableList = true;
                            setTimeout(function(){ vm.showUnDeletableList = false; }, 10000);
                        }
                        vm.filter();
                    }).catch((error) => {
                        vm.loader = false;
                    });
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
            passwordCheckCreate () {
                this.has_numberCreate    = /[0-9]+/.test(this.passwordCreate);
                this.has_lowerUPPERcaseCreate = /[a-zA-Z]/.test(this.passwordCreate);
                this.has_specialcharCreate = /[!@#\$%\^\&*\)\(+=\[\]._-]+/.test(this.passwordCreate) || this.persianTextCheck(this.passwordCreate);
                this.has_charCreate  = /.{8,}/.test(this.passwordCreate);
            },
            FaNumToEnNum: function (str) {
                let s = str.split("");
                let sEn = "";
                for(i = 0; i < s.length; ++i){
                  if(s[i] == '۰'){
                    sEn = sEn + '0';
                  }else if(s[i] == '۱'){
                    sEn = sEn + '1';
                  }else if(s[i] == '۲'){
                    sEn = sEn + '2';
                  }else if(s[i] == '۳'){
                    sEn = sEn + '3';
                  }else if(s[i] == '۴'){
                    sEn = sEn + '4';
                  }else if(s[i] == '۵'){
                    sEn = sEn + '5';
                  }else if(s[i] == '۶'){
                    sEn = sEn + '6';
                  }else if(s[i] == '۷'){
                    sEn = sEn + '7';
                  }else if(s[i] == '۸'){
                    sEn = sEn + '8';
                  }else if(s[i] == '۹'){
                    sEn = sEn + '9';
                  }
                }
                return sEn;
            },
            userIdValidate ($event) {
                let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
                if (keyCode < 48 || keyCode > 122) {
                   $event.preventDefault();
                }else if (keyCode == 58 || keyCode == 62) {
                  $event.preventDefault();
                }
            },
            rowSelected:function(id) {
                let row = document.getElementById("row-" + id);
                if(row.style.background == ""){
                    row.style.background = "#c2dbff";
                }else{
                    row.style.background = "";
                }
                this.allIsSelected = false;
                if(document.getElementById("selectAllCheckbox").checked == true){
                    document.getElementById("selectAllCheckbox").click();
                }
            },
            allSelected () {
                if(this.allIsSelected){
                    this.allIsSelected = false;
                    for(let i = 0; i < this.users.length; ++i){
                        if(document.getElementById("checkbox-" + this.users[i].userId).checked == true){
                            document.getElementById("checkbox-" + this.users[i].userId).click();
                        }
                        document.getElementById("row-" + this.users[i].userId).style.background = "";
                    }
                }else{
                    this.allIsSelected = true;
                    for(let i = 0; i < this.users.length; ++i){
                        if(document.getElementById("checkbox-" + this.users[i].userId).checked == false){
                            document.getElementById("checkbox-" + this.users[i].userId).click();
                        }
                        document.getElementById("row-" + this.users[i].userId).style.background = "#c2dbff";
                    }
                }
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    window.localStorage.setItem("lang", "EN");
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin1 = "mr-1";
                    this.margin3 = "mr-3";
                    this.margin5 = "mr-5";
                    this.col1Style = "padding-left: 0 !important; padding-right: .5rem !important; display: inline-flex;";
                    this.col2Style = "padding-left: .5rem !important; padding-right: 0 !important; display: inline-flex;";
                    this.padding0Left = "padding-right: 0rem;";
                    this.padding0Right = "padding-left: 0rem;";
                    this.eye = "left: 1%;";
                    this.font = "font-size: 0.9em; text-align: left;"
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
                    this.s23 = "No File Chosen.";
                    this.s24 = "File extension not supported!";
                    this.s25 = "File too big (> 100MB)";
                    this.s26 = "Are You Sure You Want To Edit?";
                    this.s27 = "Are You Sure You Want To Add This User?";
                    this.s28 = "Are You Sure You Want To Delete?";
                    this.s29 = "Are You Sure You Want To Delete All Users?";
                    this.s31 = "Configs";
                    this.s33 = "Resolve Conflicts While Importing Users";
                    this.s34 = "Old User";
                    this.s35 = "New User";
                    this.s36 = "Submit";
                    this.s37 = "Edit User";
                    this.s38 = "Add User";
                    this.s40 = "Status";
                    this.s41 = "Enabled";
                    this.s42 = "Disabled";
                    this.s43 = "Locked";
                    this.s44 = "Information";
                    this.s45 = "Password";
                    this.s46 = "New Password";
                    this.s47 = "Repeat New Password";
                    this.s48 = "Your Password Must Meet All Of The Following Criteria:";
                    this.s49 = "Passwords Don't Match";
                    this.s50 = "This UID Already Exists In Our Database, Please Choose Another.";
                    this.s51 = "User Expiration Date";
                    this.s52 = "Password Reset Email Sent Successfully.";
                    this.s53 = "Delete User";
                    this.s54 = "Action";
                    this.s55 = "Are You Sure You Want To Delete Selected Users?";
                    this.s56 = "No User is Selected.";
                    this.s57 = "Reset Password All";
                    this.s58 = "New";
                    this.s59 = "Reset";
                    this.s60 = "Sample";
                    this.s61 = "Full Name";
                    this.s62 = "Group";
                    this.s63 = "Status";
                    this.s64 = "Enabled";
                    this.s65 = "Disabled";
                    this.s66 = "Locked";
                    this.s67 = "Filter";
                    this.s68 = "No Filter";
                    this.s71 = "From Imported Users, ";
                    this.s72 = " Of Them Were Imported Successfully, And ";
                    this.s73 = " Of Them Faced Some Problem And Were Not Imported.";
                    this.s74 = " (Only English Letters And Numbers Are Allowed For ID)";
                    this.s75 = "Records a Page: ";
                    this.s76 = "Users List";
                    this.s77 = "Replace Duplicate Users' New Information With Their Current Information?";
                    this.s78 = "No";
                    this.s79 = "Yes";
                    this.s80 = "Audits";
                    this.s82 = "New Password Should Not be Same as Old Password.";
                    this.s83 = "Indelible";
                    this.s84 = "Users Listed Below Are Indelible.";
                    this.s85 = "No User Found";
                    this.rolesText = "Roles";
                    this.reportsText = "Reports";
                    this.importUserListGroupErrorText = "The Groups Specified For The Following Users Are Not Defined.";
                    this.importUserListRepetitiveErrorText = "The Following Users Are Duplicates.";
                    this.importUserListOverride1Text = "From Total Users, Information Of ";
                    this.importUserListOverride2Text = " Users Was Successfully Replaced And Information Of ";
                    this.importUserListOverride3Text = " Users Encountered a Problem And Was Not Replaced.";
                    this.emailFormatErrorText = "Enter Email Address Format Correctly";
                    this.mobileFormatErrorText = "Enter Phone Number Format Correctly";
                    this.U0 = "Password";
                    this.U1 = "Users";
                    this.U2 = "ID";
                    this.U3 = "First Name (In English)";
                    this.U4 = "Last Name (In English)";
                    this.U5 = "Full Name (In Persian)";
                    this.U6 = "Phone";
                    this.U7 = "Email";
                    this.U8 = "NID";
                    this.U9 = "Description";
                    this.U10 = "Update";
                    this.U11 = "Delete";
                    this.U12 = "New User";
                    this.U13 = "Edit";
                    this.U14 = "Groups";
                    this.U17 = "Remove All";
                    this.U18 = "Export Users to a file";
                    this.U19 = "Save in file";
                    this.U20 = "Import users using file";
                    this.U21 = "Upload";
                    this.U22 = "Reset Password";
                    this.U23 = "Template File";
                    this.U24 = "Employee Number";
                    this.rules[0].message = "- One Lowercase or Uppercase English Letter Required.";
                    this.rules[1].message = "- One special Character or Persian Letter Required.";
                    this.rules[2].message = "- 8 Characters Minimum.";
                    this.rules[3].message = "- One Number Required.";
                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin1 = "ml-1";
                    this.margin3 = "ml-3";
                    this.margin5 = "ml-5";
                    this.col1Style = "padding-left: .5rem !important; padding-right: 0 !important; display: inline-flex;";
                    this.col2Style = "padding-left: 0 !important; padding-right: .5rem !important; display: inline-flex;";
                    this.padding0Left = "padding-left: 0rem;";
                    this.padding0Right = "padding-right: 0rem;";
                    this.eye = "right: 1%;";
                    this.font = "font-size: 0.74em; text-align: right;"
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
                    this.s23 = "فایلی انتخاب نشده است.";
                    this.s24 = "از فرمت فایل انتخابی پشتیبانی نمی شود.";
                    this.s25 = "سایز فایل انتخابی بیش از 100M می باشد.";
                    this.s26 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s27 = "آیا از افزودن این کاربر اطمینان دارید؟";
                    this.s28 = "آیا از حذف این کاربر اطمینان دارید؟";
                    this.s29 = "آیا از حذف تمامی کاربران اطمینان دارید؟";
                    this.s31 = "پیکربندی";
                    this.s33 = "رفع تناقض وارد کردن کاربران جدید";
                    this.s34 = "کاربر قدیمی";
                    this.s35 = "کاربر جدید";
                    this.s36 = "اعمال تغییرات";
                    this.s37 = "ویرایش کاربر";
                    this.s38 = "ایجاد کاربر";
                    this.s40 = "وضعیت کاربر";
                    this.s41 = "فعال";
                    this.s42 = "غیر فعال";
                    this.s43 = "قفل شده";
                    this.s44 = "اطلاعات کاربر";
                    this.s45 = "رمز عبور";
                    this.s46 = "رمز عبور جدید";
                    this.s47 = "تکرار رمز عبور جدید";
                    this.s48 = "رمز عبور شما باید شامل موارد زیر باشد:";
                    this.s49 = "رمز عبور های وارد شده یکسان نمی باشند";
                    this.s50 = "کاربری با این شناسه کاربری وجود دارد، شناسه کاربری دیگری انتخاب کنید.";
                    this.s51 = "زمان انقضا کاربر";
                    this.s52 = "ایمیل بازنشانی رمز عبور با موفقیت ارسال شد.";
                    this.s53 = "حذف کاربر";
                    this.s54 = "اعمال";
                    this.s55 = "آیا از حذف کاربران انتخاب شده اطمینان دارید؟";
                    this.s56 = "هیچ کاربری انتخاب نشده است.";
                    this.s57 = "بازنشانی رمزعبور همه";
                    this.s58 = "جدید";
                    this.s59 = "بازنشانی";
                    this.s60 = "فایل نمونه";
                    this.s61 = "نام فارسی";
                    this.s62 = "گروه";
                    this.s63 = "وضعیت";
                    this.s64 = "فعال";
                    this.s65 = "غیرفعال";
                    this.s66 = "قفل شده";
                    this.s67 = "اعمال فیلتر";
                    this.s68 = "حذف فیلتر";
                    this.s71 = "از مجموع کاربران وارد شده تعداد ";
                    this.s72 = " کاربر با موفقیت ثبت شدند و تعداد ";
                    this.s73 = " کاربر در هنگام ثبت با مشکل مواجه شده و ثبت نشدند.";
                    this.s74 = " (برای شناسه کاربری تنها حروف انگلیسی و اعداد مجاز می باشد)";
                    this.s75 = "تعداد رکورد ها: ";
                    this.s76 = "لیست کاربران";
                    this.s77 = "آیا اطلاعات جدید کاربران تکراری، جایگزین اطلاعات فعلی آنها شود؟";
                    this.s78 = "خیر";
                    this.s79 = "بله";
                    this.s80 = "ممیزی ها";
                    this.s82 = "رمز عبور جدید و رمز عبور قدیمی نباید یکسان باشند.";
                    this.s83 = "غیرقابل حذف";
                    this.s84 = "کاربران زیر غیرقابل حذف می باشند.";
                    this.s85 = "کاربری یافت نشد";
                    this.rolesText = "نقش ها";
                    this.reportsText = "گزارش ها";
                    this.importUserListGroupErrorText = "گروه های مشخص شده برای کاربران زیر، تعریف نشده اند.";
                    this.importUserListRepetitiveErrorText = "کاربران زیر، تکراری می باشند.";
                    this.importUserListOverride1Text = "از مجموع کاربران، اطلاعات ";
                    this.importUserListOverride2Text = " کاربر با موفقیت جایگزین شده و اطلاعات ";
                    this.importUserListOverride3Text = " کاربر با مشکل مواجه شده و جایگزین نشده است.";
                    this.emailFormatErrorText = "فرمت آدرس ایمیل را به درستی وارد کنید";
                    this.mobileFormatErrorText = "فرمت شماره تلفن را به درستی وارد کنید";
                    this.U0 = "رمز";
                    this.U1 = "کاربران";
                    this.U2 = "شناسه کاربری";
                    this.U3 = "نام (به انگلیسی)";
                    this.U4 = "نام خانوادگی (به انگلیسی)";
                    this.U5 = "نام کامل (به فارسی)";
                    this.U6 = "شماره تلفن";
                    this.U7 = "ایمیل";
                    this.U8 = "کد ملی";
                    this.U9 = "توضیحات";
                    this.U10 = "به روز رسانی";
                    this.U11 = "حذف"
                    this.U12 = "کاربر جدید";
                    this.U13 = "ویرایش";
                    this.U14 = "گروه های عضو";
                    this.U17 = "حذف همه";
                    this.U18 = "وارد کردن کاربران با فایل";
                    this.U19 = "ذخیره سازی داده ها در فایل";
                    this.U20 = "وارد کردن کاربران با فایل";
                    this.U21 = "بارگزاری";
                    this.U22 = "بازنشانی رمز عبور";
                    this.U23 = "فایل الگو";
                    this.U24 = "کد پرسنلی";
                    this.rules[0].message = "حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ";
                    this.rules[2].message = "حداقل ۸ کاراکتر باشد. ";
                    this.rules[3].message = "حداقل شامل یک عدد باشد. ";
                }
            }
        },
        computed:{
            userCreateExists () {
                return this.userFound;
            },
            sortedUsers:function() {
                this.usersPage = this.users;
                return this.usersPage;
            },
            notSamePasswords () {
                if (this.passwordsFilled) {
                    return (this.password !== this.checkPassword)
                }else {
                    return false;
                }
            },
            duplicatePasswordsComputed () {
                return this.duplicatePasswords;
            },
            passwordsFilled () {
                return (this.password !== '' && this.checkPassword !== '')
            },
            isActiveUserPassUpdate () {
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
            },
            notSamePasswordsCreate () {
                if (this.passwordsFilledCreate) {
                    return (this.passwordCreate !== this.checkPasswordCreate)
                } else {
                    return false
                }
            },
            passwordsFilledCreate () {
                return (this.passwordCreate !== '' && this.checkPasswordCreate !== '')
            },
            isActiveUserPassCreate () {
                if(this.passwordCreate !== '' && this.checkPasswordCreate !== ''){
                    let errors = []
                    for (let condition of this.rules) {
                        if (!condition.regex.test(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }
                    if(errors.length === 0){
                        if(this.passwordCreate === this.checkPasswordCreate){
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
            passwordValidationCreate () {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.passwordCreate) && !this.persianTextCheck(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.passwordCreate)) {
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
            strengthLevelCreate() {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.passwordCreate) && !this.persianTextCheck(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.passwordCreate)) {
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
        },
        watch : {
            paginationCurrentPage : function () {
                if(this.paginationCurrentPage != this.currentPage){
                    this.currentPage = this.paginationCurrentPage;
                    this.filter("paginationCurrentPage");
                }
            }
        }
    });
})
