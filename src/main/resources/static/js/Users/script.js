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
    Vue.component('v-pagination', window['vue-plain-pagination'])
    new Vue({
        router,
        el: '#app',
        data: {
            recordsShownOnPage: 20,
            currentPage: 1,
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
            importConflictS: "display:none",
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
            searchGroup: "none",
            searchStatus: "none",
            promptImportButtons: false,
            loader: false,
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
            s71: "از مجموع کاربران وارد شده تعداد ",
            s72: " کاربر با موفقیت ثبت شدند و تعداد ",
            s73: " کاربر در هنگام ثبت با مشکل مواجه شده و ثبت نشدند.",
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
            this.getUserInfo();
            this.getUserPic();
            this.getUsers();
            this.getGroups();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
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
                if(override){
                    axios({
                        method: 'put',
                        url: url + "/api/users/import/massUpdate",  //
                        headers: {'Content-Type': 'application/json'},
                        data: vm.userListImport
                    });
                }
                this.usersImported = false;
                this.promptImportButtons = false;
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
                    vm.loader = true;
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
                            vm.promptImportButtons = true;
                            vm.userListImport = error.response.data.list;
                            for(let i = 0; i < error.response.data.list.length; ++i){
                                vm.userListImport.push(error.response.data.list[i].new);
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
            selectConflict: function(s1, s2) {
                document.getElementById(s1).className = "btn btn-success mb-2";
                document.getElementById(s2).className = "btn btn-notSelected mb-2";
                var res = s1.split("-");
                this.usersCorrectUserIdList.push(res[0]);
                for(var i = 0; i < this.usersCorrectList.length; ++i){
                    if(this.usersCorrectList[i].userId == res[0]){
                        if(res[1] == "firstName"){
                            this.usersCorrectList[i].firstName = res[2];
                        }else if(res[1] == "lastName"){
                            this.usersCorrectList[i].lastName = res[2];
                        }else if(res[1] == "displayName"){
                            this.usersCorrectList[i].displayName = res[2];
                        }else if(res[1] == "mobile"){
                            this.usersCorrectList[i].mobile = res[2];
                        }else if(res[1] == "mail"){
                            this.usersCorrectList[i].mail = res[2];
                        }else if(res[1] == "description"){
                            this.usersCorrectList[i].description = res[2];
                        }else if(res[1] == "memberOf"){
                            this.usersCorrectList[i].memberOf = res[2];
                        }
                    }
                }
            },
            unselectConflict: function(s) {
                document.getElementById(s).className = "btn btn-notSelected mb-2";
            },
            resolveConflicts: function() {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                for(var i = 0; i < this.usersCorrectList.length; ++i){
                    if(this.usersCorrectUserIdList.indexOf(this.usersCorrectList[i].userId) != -1){
                        var groupsConflictList = [];
                        var groupsList = this.usersCorrectList[i].memberOf.split(',');
                        for(var j = 0; j < groupsList.length; ++j){
                            groupsConflictList.push(groupsList[j]);
                        }
                        axios({
                            method: 'put',
                            url: url + '/api/users/u/' + vm.usersCorrectList[i].userId,  //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                userId: vm.usersCorrectList[i].userId,
                                firstName: vm.usersCorrectList[i].firstName,
                                lastName: vm.usersCorrectList[i].lastName,
                                displayName: vm.usersCorrectList[i].displayName,
                                mobile: vm.usersCorrectList[i].mobile,
                                memberOf: groupsConflictList,
                                mail: vm.usersCorrectList[i].mail,
                                description: vm.usersCorrectList[i].description,
                            }).replace(/\\\\/g, "\\")
                        });
                    }
                }
                location.reload();
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
                axios.get(url + "/api/users/" + vm.currentPage + "/" + vm.recordsShownOnPage) //
                    .then((res) => {
                        if(res.data.userList.length == 0){
                            vm.isListEmpty = true;
                        }
                        vm.total = Math.ceil(res.data.size / vm.recordsShownOnPage);
                        res.data.userList.forEach(function (item) {
                            tempUsers = {};
                            tempUsers.userId = item.userId;
                            tempUsers.displayName = item.displayName;
                            tempUsers.memberOf = item.memberOf;
                            vm.users.push(tempUsers);
                        });
                        for(let i = 0; i < vm.recordsShownOnPage; ++i){
                            if(i < res.data.size){
                                vm.users[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
                            }
                        }
                    });
            },
            filter: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let searchQuery = "?";
                if(!this.changePageUsers){
                    this.currentPage = 1;
                }
                this.users = [];
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
                if(this.searchGroup != "none"){
                    searchQuery = searchQuery + this.searchGroup + "&";
                }else{
                    searchQuery = searchQuery + "&";
                }
                searchQuery = searchQuery + "userStatus=";
                if(this.searchStatus != "none"){
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
                let tempUsers = {};
                this.users = [];
                this.isListEmpty = false;
                axios.get(url + "/api/users/" + vm.currentPage + "/" + vm.recordsShownOnPage + searchQuery) //
                    .then((res) => {
                        if(res.data.userList.length == 0){
                            vm.isListEmpty = true;
                        }
                        vm.total = Math.ceil(res.data.size / vm.recordsShownOnPage);
                        res.data.userList.forEach(function (item) {
                            tempUsers = {};
                            tempUsers.userId = item.userId;
                            tempUsers.displayName = item.displayName;
                            tempUsers.memberOf = item.memberOf;
                            vm.users.push(tempUsers);
                        });
                        for(let i = 0; i < vm.recordsShownOnPage; ++i){
                            if(i < res.data.size){
                                vm.users[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
                            }
                        }
                    });
                this.changePageUsers = false;
            },
            deleteFilter: function () {
                this.searchUserId = "";
                this.searchDisplayName = "";
                this.searchGroup = "none";
                this.searchStatus = "none"
                this.userIdm2MFlag = false;
                this.userIdM2mFlag = false;
                this.displayNamem2MFlag = false;
                this.displayNameM2mFlag = false;
                this.filter();
            },
            userIdm2M: function () {
                this.userIdm2MFlag = true,
                this.userIdM2mFlag = false,
                this.displayNamem2MFlag = false,
                this.displayNameM2mFlag = false,
                this.filter();
            },
            userIdM2m: function () {
                this.userIdm2MFlag = false,
                this.userIdM2mFlag = true,
                this.displayNamem2MFlag = false,
                this.displayNameM2mFlag = false,
                this.filter();
            },
            displayNamem2M: function () {
                this.userIdm2MFlag = false,
                this.userIdM2mFlag = false,
                this.displayNamem2MFlag = true,
                this.displayNameM2mFlag = false,
                this.filter();
            },
            displayNameM2m: function () {
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
                this.importConflictS = "display:none"
                location.reload();
            },
            showImportConflicts: function () {
                this.showS = "display:none"
                this.ShowSExtra = ""
                this.addS = "display:none"
                this.editS = "display:none"
                this.importConflictS = ""
            },
            editUserS: function (id) {
                this.showS = "display:none"
                this.ShowSExtra = ""
                this.addS = "display:none"
                this.editS = ""
                this.importConflictS = "display:none"
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.getGroups();
                axios.get(url + `/api/users/u/${id}`) //
                    .then((res) => {
                        vm.editInfo = res.data;
                        if(typeof res.data.status !== 'undefined'){
                            vm.userStatus = res.data.status;
                            if(res.data.status == "active"){
                                document.getElementById("option1").selected = true;
                                document.getElementById("option2").selected = false;
                                document.getElementById("option3").selected = false;
                            }else if(res.data.status == "disabled"){
                                document.getElementById("option1").selected = false;
                                document.getElementById("option2").selected = true;
                                document.getElementById("option3").selected = false;
                            }else if(res.data.status == "locked"){
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
                if(id == "" ||
                document.getElementById('editInfo.displayNameUpdate').value == "" ||
                document.getElementById('editInfo.mobileUpdate').value == "" ||
                document.getElementById('editInfo.mailUpdate').value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
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
                        if(document.getElementById('status').value != "locked"){
                            if(document.getElementById('status').value == "active"){
                                if(this.userStatus == "active"){
                                    statusValue = "";
                                }else if(this.userStatus == "disabled"){
                                    statusValue = "enable";
                                }else if(this.userStatus == "locked"){
                                    statusValue = "unlock";
                                }
                            }else if(document.getElementById('status').value == "disabled"){
                                if(this.userStatus == "active"){
                                    statusValue = "disable";
                                }else if(this.userStatus == "disabled"){
                                    statusValue = "";
                                }else if(this.userStatus == "locked"){
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
                url_ = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                vm.loader = true;
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
                this.importConflictS = "display:none"
                this.getGroups();
            },
            sendResetEmail(userId) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedUsers = [];
                selectedUsers.push(userId.toString());
                vm.loader = true;
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
            addUser: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var unDeletableVar = false;

                if(document.getElementById('editInfo.userIdCreate').value == "" ||
                document.getElementById('editInfo.displayNameCreate').value == "" ||
                document.getElementById('editInfo.mailCreate').value == "" ||
                document.getElementById('editInfo.mobileCreate').value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
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
                                    cStatus: document.getElementById('statusCreate').value,
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
            deleteUser: function (userId) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedUsers = [];
                selectedUsers.push(userId.toString());
                var check = confirm(this.s28);
                if (check == true) {
                    axios({
                        method: 'delete',
                        url: url + "/api/users", //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            names: selectedUsers
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
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
                    });
                }
            },
            deleteAllUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm(this.s29);
                if (check == true) {
                    axios({
                        method: 'delete',
                        url: url + "/api/users", //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        vm.filter();
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
                            axios({
                                method: 'delete',
                                url: url + "/api/users", //
                                headers: {'Content-Type': 'application/json'},
                                data: JSON.stringify({
                                    names: selectedUsers
                                }).replace(/\\\\/g, "\\")
                            })
                            .then((res) => {
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
                        vm.loader = true;
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
                    }else{
                        alert(this.s56);
                    }
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
            changeLang: function () {
                if(this.lang == "EN"){
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
                    this.s22 = "./profile?en";
                    this.s23 = "No File Chosen.";
                    this.s24 = "File extension not supported!";
                    this.s25 = "File too big (> 100MB)";
                    this.s26 = "Are You Sure You Want To Edit?";
                    this.s27 = "Are You Sure You Want To Add This User?";
                    this.s28 = "Are You Sure You Want To Delete?";
                    this.s29 = "Are You Sure You Want To Delete All Users?";
                    this.s30 = "./privacy?en";
                    this.s31 = "Configs";
                    this.s32 = "./configs?en";
                    this.s33 = "Resolve Conflicts While Importing Users";
                    this.s34 = "Old User";
                    this.s35 = "New User";
                    this.s36 = "Submit";
                    this.s37 = "Edit User";
                    this.s38 = "Add User";
                    this.s39 = "./events?en";
                    this.s40 = "Status";
                    this.s41 = "Active";
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
                    this.s64 = "Active";
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
                    this.s81 = "/audits?en";
                    this.s82 = "New Password Should Not be Same as Old Password.";
                    this.s83 = "Indelible";
                    this.s84 = "Users Listed Below Are Indelible.";
                    this.s85 = "No User Found";
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
                } else{
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
                    this.s22 = "./profile";
                    this.s23 = "فایلی انتخاب نشده است.";
                    this.s24 = "از فرمت فایل انتخابی پشتیبانی نمی شود.";
                    this.s25 = "سایز فایل انتخابی بیش از 100M می باشد.";
                    this.s26 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s27 = "آیا از افزودن این کاربر اطمینان دارید؟";
                    this.s28 = "آیا از حذف این کاربر اطمینان دارید؟";
                    this.s29 = "آیا از حذف تمامی کاربران اطمینان دارید؟";
                    this.s30 = "./privacy";
                    this.s31 = "پیکربندی";
                    this.s32 = "./configs";
                    this.s33 = "رفع تناقض وارد کردن کاربران جدید";
                    this.s34 = "کاربر قدیمی";
                    this.s35 = "کاربر جدید";
                    this.s36 = "اعمال تغییرات";
                    this.s37 = "ویرایش کاربر";
                    this.s38 = "ایجاد کاربر";
                    this.s39 = "./events";
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
                    this.s81 = "/audits";
                    this.s82 = "رمز عبور جدید و رمز عبور قدیمی نباید یکسان باشند.";
                    this.s83 = "غیرقابل حذف";
                    this.s84 = "کاربران زیر غیرقابل حذف می باشند.";
                    this.s85 = "کاربری یافت نشد";
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
            currentPage : function () {
              this.changePageUsers = true;
              this.filter();
            }
        }
    });
})
