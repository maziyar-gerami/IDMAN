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
            userInfo: [],
            email: "",
            username: "",
            name: "",
            nameEN: "",
            group: [],
            groups: [],
            groupsPage: [],
            message: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            editS: "display:none",
            addS: "display:none",
            showS: "",
            currentPage: 1,
            total: 1,
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
            userPicture: "images/PlaceholderUser.png",
            allIsSelected: false,
            allUsersListSearchQuery: "",
            groupUsersListSearchQuery: "",
            allUsersListSearchQueryC: "",
            groupUsersListSearchQueryC: "",
            allUsersSearch:[],
            groupUsersSearch:[],
            allUsersSearchC:[],
            groupUsersSearchC:[],
            groupsAddedUsersList: [],
            groupsRemovedUsersList: [],
            groupsAddedUsersListC: [],
            groupsRemovedUsersListC: [],
            allUsersListPage: 1,
            groupUsersListPage: 1,
            allUsersShownOnPage: 50,
            groupUsersShownOnPage: 50,
            allUsersListSize: 0,
            groupUsersListSize: 0,
            editingGroupID: "",
            numberOfLoadAllUsers: 1,
            helperArray: [],
            endOfAllUsersList: true,
            loader: false,
            loader1: false,
            loader2: false,
            isListEmpty: false,
            activeItem: "listTab",
            duplicateUsersList: "",
            usersAddedSuccess: false,
            usersAddedError: false,
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
            s23: "آیا از اعمال این تغییرات اطمینان دارید؟",
            s24: "آیا از حذف این گروه اطمینان دارید؟",
            s25: "آیا از حذف تمامی گروه ها اطمینان دارید؟",
            s26: "./privacy",
            s27: "پیکربندی",
            s28: "./configs",
            s29: "افزودن گروه",
            s30: "ویرایش گروه",
            s31: "./events",
            s32: "حذف گروه",
            s33: "اعمال",
            s34: "آیا از اعمال این تغییرات اطمینان دارید؟",
            s35: "هیچ گروهی انتخاب نشده است.",
            s36: "تعداد رکورد ها: ",
            s37: "ممیزی ها",
            s38: "/audits",
            s39: "لیست کاربران خارج از گروه ",
            s40: "جستجو...",
            s41: "افزودن به گروه",
            s42: "کاربری یافت نشد",
            s43: "لیست اعضای گروه",
            s44: "حذف از گروه",
            s45: "گروهی یافت نشد",
            s46: "لیست گروه ها",
            s47: "افزودن کاربران با فایل",
            s48: "فایل نمونه csv",
            s49: "فایل نمونه xls",
            s50: "فایل نمونه xlsx",
            s51: "انتخاب گروه",
            s52: "انتخاب فایل",
            s53: "از لیست ارسال شده کاربران زیر در پایگاه داده وجود نداشتند، و باقی کاربران با موفقیت به گروه افزوده شدند.",
            s54: "کاربران با موفقیت به گروه افزوده شدند.",
            s55: " (برای نام انگلیسی گروه تنها حروف انگلیسی و اعداد مجاز می باشد)",
            rolesText: "نقش ها",
            rolesURLText: "./roles",
            reportsText: "گزارش ها",
            reportsURLText: "./reports",
            publicmessagesText: "اعلان های عمومی",
            publicmessagesURLText: "./publicmessages",
            ticketingText: "پشتیبانی",
            ticketingURLText: "./ticketing",
            groupIdDuplicate: false,
            groupIdDuplicateText: "گروهی با این نام وجود دارد، نام دیگری انتخاب کنید.",
            fileUploadGroupNotSelectedText: "لطفا گروه مورد نظر خود را انتخاب کنید",
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
            expirePasswordText: "انقضای رمزعبور",
            usersCountText: "تعداد اعضا",
            membersText: "نفر",
            U0: "رمز عبور",
            U1: "گروه ها",
            U2: "نام انگلیسی",
            U3: "نام فارسی",
            U4: "نام خانوادگی (به انگلیسی)",
            U5: "نام کامل (به فارسی)",
            U6: "شماره تماس",
            U7: "ایمیل",
            U8: "کد ملی",
            U9: "توضیحات",
            U10: "به روزرسانی",
            U11: "حذف",
            U12: "جدید",
            U13: "ویرایش",
            U14: "گروههای عضو",
            U15: "تکرار رمز عبور",
            U16: "کاربر مورد نظر در گروههای زیر عضویت دارد. کاربر مورد نظر از چه گروههایی حذف شود؟",
            U17: "حذف",
            h1: "ترکیبی از حروف و اعداد. مثال: ali123",
            p1: "خیلی ضعیف",
            p2: "متوسط",
            p3: "قوی"
        },
        created: function () {
            this.setDateNav();
            this.getUserInfo();
            this.getUserPic();
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
            openMeeting: function () {
                window.open(this.meetingAdminLink, "_blank").focus();
            },
            openOverlay: function () {
                document.getElementById("overlay").style.display = "block";
            },
            closeOverlay: function () {
                document.getElementById("overlay").style.display = "none";
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
            allSelected () {
                if(this.allIsSelected){
                    this.allIsSelected = false;
                    for(let i = 0; i < this.groups.length; ++i){
                        if(document.getElementById("checkbox-" + this.groups[i].id).checked == true){
                            document.getElementById("checkbox-" + this.groups[i].id).click();
                        }
                        document.getElementById("row-" + this.groups[i].id).style.background = "";
                    }
                }else{
                    this.allIsSelected = true;
                    for(let i = 0; i < this.groups.length; ++i){
                        if(document.getElementById("checkbox-" + this.groups[i].id).checked == false){
                            document.getElementById("checkbox-" + this.groups[i].id).click();
                        }
                        document.getElementById("row-" + this.groups[i].id).style.background = "#c2dbff";
                    }
                }
            },
            changeRecords: function(event) {
                this.recordsShownOnPage = event.target.value;
                this.getGroups();
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
                    if(typeof res.data.skyRoom !== "undefined"){
                        if(res.data.skyRoom.enable){
                            vm.showMeeting = true;
                            vm.meetingAdminLink = res.data.skyRoom.presenter;
                            vm.meetingGuestLink = res.data.skyRoom.students;
                        }
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
                this.isListEmpty = false;
                axios.get(url + "/api/groups") //
                    .then((res) => {
                        if(res.data.length == 0){
                            vm.isListEmpty = true;
                        }
                        vm.groups = res.data;
                        for(let i = 0; i < vm.groups.length; ++i){
                            vm.groups[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
                        }
                        vm.total = Math.ceil(vm.groups.length / vm.recordsShownOnPage);
                    });
            },
            showGroups: function () {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
            },
            editGroupS: function (id) {
                this.showS = "display:none"
                this.addS = "display:none"
                this.editS = ""
                //this.editingGroupID = id;
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/groups/" + id) //
                    .then((res) => {
                        vm.group = res.data;
                        vm.getAllUsersList(id);
                    });
            },
            editGroup: function (id) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                if(document.getElementById('group.nameUpdate').value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{
                    var check = confirm(this.s23);
                    if (check == true) {
                        axios({
                            method: 'put',
                            url: url + '/api/groups/' + id, //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                id: document.getElementById('group.idUpdate').value,
                                name: document.getElementById('group.nameUpdate').value,
                                description: document.getElementById('group.descriptionUpdate').value,
                            }).replace(/\\\\/g, "\\")
                        })
                        .then((res) => {
                            axios({
                                method: 'put',
                                url: url + '/api/users/group/' + id, //
                                headers: {'Content-Type': 'application/json'},
                                data: JSON.stringify({
                                    add: vm.groupsAddedUsersList,
                                    remove: vm.groupsRemovedUsersList,
                                }).replace(/\\\\/g, "\\")
                            })
                            .then((resp) => {
                                location.reload();
                            });
                        });
                    }
                }
            },
            addGroupS: function () {
                this.showS = "display:none"
                this.addS = ""
                this.editS = "display:none"
                this.getAllUsersListC();
            },
            addGroup: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                if(document.getElementById('group.nameCreate').value == "" || document.getElementById('group.idCreate').value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{
                    axios({
                        method: 'post',
                        url: url + "/api/groups", //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            id: document.getElementById('group.idCreate').value,
                            name: document.getElementById('group.nameCreate').value,
                            description: document.getElementById('group.descriptionCreate').value,
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        axios({
                            method: 'put',
                            url: url + '/api/users/group/' + document.getElementById('group.idCreate').value, //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                add: vm.groupsAddedUsersListC,
                                remove: vm.groupsRemovedUsersListC,
                            }).replace(/\\\\/g, "\\")
                        })
                        .then((resp) => {
                            location.reload();
                        });
                    }).catch((error) => {
                        if (error.response) {
                            if(error.response.status === 302){
                                vm.groupIdDuplicate = true;
                            }
                        }
                    });
                }
            },
            deleteGroup: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedGroups = [];
                selectedGroups.push(id.toString());
                var check = confirm(this.s24);
                if (check == true) {
                    axios({
                        method: 'delete',
                        url: url + "/api/groups", //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            names: selectedGroups
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        vm.getGroups();
                    });
                }
            },
            deleteAllGroups: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm(this.s25);
                if (check == true) {
                    axios({
                        method: 'delete',
                        url: url + "/api/groups", //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        vm.getGroups();
                    });
                }
            },
            changeSelected: function (action) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedGroups = [];
                if(action == "delete"){
                    selectedGroups = [];
                    for(let i = 0; i < vm.groups.length; ++i){
                        if(document.getElementById("checkbox-" + vm.groups[i].id).checked){
                            selectedGroups.push(vm.groups[i].id.toString());
                        }
                    }
                    if(selectedGroups.length != 0){
                        var check = confirm(this.s34);
                        if (check == true) {
                            axios({
                                method: 'delete',
                                url: url + "/api/groups", //
                                headers: {'Content-Type': 'application/json'},
                                data: JSON.stringify({
                                    names: selectedGroups
                                }).replace(/\\\\/g, "\\")
                            })
                            .then((res) => {
                                vm.getGroups();
                            });
                        }
                    }else{
                        alert(this.s35);
                    }
                }else if(action == "expirePassword"){
                    selectedGroups = [];
                    for(let i = 0; i < vm.groups.length; ++i){
                        if(document.getElementById("checkbox-" + vm.groups[i].id).checked){
                            selectedGroups.push(vm.groups[i].id.toString());
                        }
                    }
                    if(selectedGroups.length != 0){
                        var check = confirm(this.s34);
                        if (check == true) {
                            axios({
                                method: "put",
                                url: url + "/api/groups/password/expire", //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    names: selectedGroups
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                vm.getGroups();
                            });
                        }
                    }else{
                        alert(this.s35);
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
            getAllUsersList: function (id){
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.loader = true;
                axios.get(url + "/api/users/") // + vm.allUsersListPage + "/" + vm.allUsersShownOnPage
                    .then((res) => {
                        vm.allUsersListSize = res.data.size;
                        vm.allUsersSearch = res.data;
                        vm.getGroupUsersList(id);
                    });
            },
            getGroupUsersList: function (id){
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/users/group/" + id)//, {params: { page: vm.groupUsersListPage,  nRec: vm.groupUsersShownOnPage},})
                    .then((res) => {
                        vm.groupUsersListSize = res.data.size;
                        vm.groupUsersSearch = res.data.userList;
                        if(vm.allUsersSearch.length != 0 || vm.groupUsersSearch.length != 0){
                            vm.allUsersSearch = vm.allUsersSearch.filter(a => !vm.groupUsersSearch.map(b=>b.userId).includes(a.userId));
                        }
                        vm.loader = false;
                        /*if(vm.groupUsersSearch.length >= vm.groupUsersListSize){
                            document.getElementById("loadGroupUsersListButton").disabled = true;
                            document.getElementById("loadAllUsersListButton").disabled = true;
                        }*/
                    });
            },
            addUserToGroup: function (user){
                let index = this.groupsRemovedUsersList.indexOf(user.userId);
                if (index > -1) {
                    index = this.groupsRemovedUsersList.indexOf(user.userId);
                    if (index > -1) {
                        this.groupsRemovedUsersList.splice(index, 1);
                    }
                    index = this.allUsersSearch.indexOf(user);
                    if (index > -1) {
                        this.allUsersSearch.splice(index, 1);
                    }
                    this.groupUsersSearch.push(user);
                }else{
                    this.groupsAddedUsersList.push(user.userId);
                    this.groupUsersSearch.push(user);
                    index = this.allUsersSearch.indexOf(user);
                    if (index > -1) {
                        this.allUsersSearch.splice(index, 1);
                    }
                }
            },
            removeUserFromGroup: function (user){
                let index = this.groupsAddedUsersList.indexOf(user.userId);
                if (index > -1) {
                    index = this.groupsAddedUsersList.indexOf(user.userId);
                    if (index > -1) {
                        this.groupsAddedUsersList.splice(index, 1);
                    }
                    index = this.groupUsersSearch.indexOf(user);
                    if (index > -1) {
                        this.groupUsersSearch.splice(index, 1);
                    }
                    this.allUsersSearch.push(user);
                }else{
                    this.groupsRemovedUsersList.push(user.userId);
                    this.allUsersSearch.push(user);
                    index = this.groupUsersSearch.indexOf(user);
                    if (index > -1) {
                        this.groupUsersSearch.splice(index, 1);
                    }
                }
            },
            getAllUsersListC: function (){
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.loader1 = true;
                axios.get(url + "/api/users/") // + vm.allUsersListPage + "/" + vm.allUsersShownOnPage
                    .then((res) => {
                        vm.allUsersSearchC = res.data;
                        vm.loader1 = false;
                    });
            },
            addUserToGroupC: function (user){
                let index = this.groupsRemovedUsersListC.indexOf(user.userId);
                if (index > -1) {
                    index = this.groupsRemovedUsersListC.indexOf(user.userId);
                    if (index > -1) {
                        this.groupsRemovedUsersListC.splice(index, 1);
                    }
                    index = this.allUsersSearchC.indexOf(user);
                    if (index > -1) {
                        this.allUsersSearchC.splice(index, 1);
                    }
                    this.groupUsersSearchC.push(user);
                }else{
                    this.groupsAddedUsersListC.push(user.userId);
                    this.groupUsersSearchC.push(user);
                    index = this.allUsersSearchC.indexOf(user);
                    if (index > -1) {
                        this.allUsersSearchC.splice(index, 1);
                    }
                }
            },
            removeUserFromGroupC: function (user){
                let index = this.groupsAddedUsersListC.indexOf(user.userId);
                if (index > -1) {
                    index = this.groupsAddedUsersListC.indexOf(user.userId);
                    if (index > -1) {
                        this.groupsAddedUsersListC.splice(index, 1);
                    }
                    index = this.groupUsersSearchC.indexOf(user);
                    if (index > -1) {
                        this.groupUsersSearchC.splice(index, 1);
                    }
                    this.allUsersSearchC.push(user);
                }else{
                    this.groupsRemovedUsersListC.push(user.userId);
                    this.allUsersSearchC.push(user);
                    index = this.groupUsersSearchC.indexOf(user);
                    if (index > -1) {
                        this.groupUsersSearchC.splice(index, 1);
                    }
                }
            },
            fileUploadCheck: function () {
                if(document.getElementById("editingGroupID").value != ""){
                    document.getElementById("file").click();
                }else{
                    alert(this.fileUploadGroupNotSelectedText);
                }
            },
            selectedFile() {
                let re = /(\.csv|\.xls|\.xlsx)$/i;
                let fup = document.getElementById('file');
                let fileName = fup.value;
                if(!re.exec(fileName)){
                    alert("از فرمت فایل انتخابی پشتیبانی نمی شود.");
                    return;
                }else{
                    var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                    var vm = this;
                    var bodyFormData = new FormData();
                    var file = document.querySelector('#file');
                    bodyFormData.append("file", file.files[0]);
                    this.duplicateUsersList = "";
                    this.loader2 = true;
                    axios({
                        method: 'put',
                        url: url + "/api/users/ou/" + document.getElementById("editingGroupID").value,  //
                        headers: {'Content-Type': 'multipart/form-data'},
                        data: bodyFormData,
                    }).then((res) => {
                        console.log(res);
                        if(res.status == 200){
                            vm.loader2 = false;
                            vm.usersAddedSuccess = true;
                            setTimeout(function(){ vm.usersAddedSuccess = false; }, 5000);
                        }else if(res.status == 206){
                            for(let i = 0; i < res.data.length - 1; ++i){
                                vm.duplicateUsersList = vm.duplicateUsersList + res.data[i] + ", ";
                            }
                            vm.duplicateUsersList = vm.duplicateUsersList + res.data[res.data.length - 1];
                            vm.loader2 = false;
                            vm.usersAddedError = true;
                            setTimeout(function(){ vm.usersAddedError = false; }, 15000);
                        }
                    }).catch((error) => {
                        vm.loader2 = false;
                        alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                    });
                }
            },
            removeGroupIdDuplicateError() {
                this.groupIdDuplicate = false;
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
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin1 = "mr-1";
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
                    this.s23 = "Are You Sure You Want To Edit?";
                    this.s24 = "Are You Sure You Want To Delete?";
                    this.s25 = "Are You Sure You Want To Delete All Groups?";
                    this.s27 = "Configs";
                    this.s29 = "Add Group";
                    this.s30 = "Edit Group";
                    this.s32 = "Delete Group";
                    this.s33 = "Action";
                    this.s34 = "Are You Sure You Want To Delete Selected Groups?";
                    this.s35 = "No Group is Selected.";
                    this.s36 = "Records a Page: ";
                    this.s37 = "Audits";
                    this.s39 = "Non-member Users List ";
                    this.s40 = "Search...";
                    this.s41 = "Add to Group";
                    this.s42 = "No User Found";
                    this.s43 = "List of Group Members ";
                    this.s44 = "Remove from Group";
                    this.s45 = "No Group Found";
                    this.s46 = "Groups List";
                    this.s47 = "Adding Users With File";
                    this.s48 = "CSV Sample";
                    this.s49 = "XLS Sample";
                    this.s50 = "XLSX Sample";
                    this.s51 = "Select Group";
                    this.s52 = "Select File";
                    this.s53 = "The Following Users From The Submitted List Were Not in The Database, The Rest of The Users Were Successfully Added to The Group.";
                    this.s54 = "The Users Were Successfully Added to The Group.";
                    this.s55 = " (Only English Letters And Numbers Are Allowed For Group Name)";
                    this.rolesText = "Roles";
                    this.reportsText = "Reports";
                    this.publicmessagesText = "Public Messages";
                    this.ticketingText = "Ticketing";
                    this.groupIdDuplicateText = "A Group With This Name Already Exists, Please Choose Another.";
                    this.fileUploadGroupNotSelectedText = "Please Select The Intended Group";
                    this.inputEnglishFilterText = " (Only English Letters And Numbers Are Allowed)";
                    this.inputPersianFilterText = " (Only Persian Letters And Numbers Are Allowed)";
                    this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingText = "Meeting";
                    this.enterMeetingText = "Enter Meeting";
                    this.inviteToMeetingText = "Invite To Meeting";
                    this.copyText = "Copy";
                    this.returnText = "Return";
                    this.expirePasswordText = "Expire Password";
                    this.usersCountText = "Users Count";
                    this.membersText = "Members";
                    this.U0= "Password";
                    this.U1= "Groups";
                    this.U2= "English Name";
                    this.U3= "Name";
                    this.U4= "Last Name (In English)";
                    this.U5= "FullName (In Persian)";
                    this.U6= "Phone";
                    this.U7= "Email";
                    this.U8= "NID";
                    this.U9 = "Description";
                    this.U10 = "Update";
                    this.U11 = "Delete"
                    this.U12 = "New";
                    this.U13 = "Edit";
                    this.U17 = "Delete";
                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin1 = "ml-1";
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
                    this.s23 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s24 = "آیا از حذف این گروه اطمینان دارید؟";
                    this.s25 = "آیا از حذف تمامی گروه ها اطمینان دارید؟";
                    this.s26 = "./privacy";
                    this.s27 = "پیکربندی";
                    this.s29 = "افزودن گروه";
                    this.s30 = "ویرایش گروه";
                    this.s32 = "حذف گروه";
                    this.s33 = "اعمال";
                    this.s34 = "آیا از حذف گروه های انتخاب شده اطمینان دارید؟";
                    this.s35 = "هیچ گروهی انتخاب نشده است.";
                    this.s36 = "تعداد رکورد ها: ";
                    this.s37 = "ممیزی ها";
                    this.s39 = "لیست کاربران خارج از گروه";
                    this.s40 = "جستجو...";
                    this.s41 = "افزودن به گروه";
                    this.s42 = "کاربری یافت نشد";
                    this.s43 = "لیست اعضای گروه ";
                    this.s44 = "حذف از گروه";
                    this.s45 = "گروهی یافت نشد";
                    this.s46 = "لیست گروه ها";
                    this.s47 = "افزودن کاربران با فایل";
                    this.s48 = "فایل نمونه csv";
                    this.s49 = "فایل نمونه xls";
                    this.s50 = "فایل نمونه xlsx";
                    this.s51 = "انتخاب گروه";
                    this.s52 = "انتخاب فایل";
                    this.s53 = "از لیست ارسال شده کاربران زیر در پایگاه داده وجود نداشتند، و باقی کاربران با موفقیت به گروه افزوده شدند.";
                    this.s54 = "کاربران با موفقیت به گروه افزوده شدند.";
                    this.s55 = " (برای نام انگلیسی گروه تنها حروف انگلیسی و اعداد مجاز می باشد)";
                    this.rolesText = "نقش ها";
                    this.reportsText = "گزارش ها";
                    this.publicmessagesText = "اعلان های عمومی";
                    this.ticketingText = "پشتیبانی";
                    this.groupIdDuplicateText = "گروهی با این نام وجود دارد، نام دیگری انتخاب کنید.";
                    this.fileUploadGroupNotSelectedText = "لطفا گروه مورد نظر خود را انتخاب کنید";
                    this.inputEnglishFilterText = " (تنها حروف انگلیسی و اعداد مجاز می باشند)";
                    this.inputPersianFilterText = " (تنها حروف فارسی و اعداد مجاز می باشند)";
                    this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingText = "جلسه مجازی";
                    this.enterMeetingText = "ورود به جلسه";
                    this.inviteToMeetingText = "دعوت به جلسه";
                    this.copyText = "کپی";
                    this.returnText = "بازگشت";
                    this.expirePasswordText = "انقضای رمزعبور";
                    this.usersCountText = "تعداد اعضا";
                    this.membersText = "نفر";
                    this.U0 = "رمز";
                    this.U1 = "گروه ها";
                    this.U2 = "نام انگلیسی";
                    this.U3 = "نام";
                    this.U4 = "نام خانوادگی (به انگلیسی)";
                    this.U5 = "نام کامل (به فارسی)";
                    this.U6 = "شماره تماس";
                    this.U7 = "ایمیل";
                    this.U8 = "کد ملی";
                    this.U9 = "توضیحات";
                    this.U10 = "به روز رسانی";
                    this.U11 = "حذف"
                    this.U12 = "جدید";
                    this.U13 = "ویرایش";
                    this.U17 = "حذف"
                }
            }
        },
        computed:{
            sortedGroups:function() {
                this.groupsPage = [];
                for(let i = 0; i < this.recordsShownOnPage; ++i){
                    if(i + ((this.currentPage - 1) * this.recordsShownOnPage) <= this.groups.length - 1){
                        this.groupsPage[i] = this.groups[i + ((this.currentPage - 1) * this.recordsShownOnPage)];
                    }
                }
                return this.groupsPage;
            },
            allUsersListResultQuery(){
                if(this.allUsersListSearchQuery){
                  let buffer = [];
                  buffer = buffer.concat(this.allUsersSearch.filter((item)=>{
                    return this.allUsersListSearchQuery.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
                  }));
                  buffer = buffer.concat(this.allUsersSearch.filter((item)=>{
                    return this.allUsersListSearchQuery.split(' ').every(v => item.displayName.includes(v))
                  }));
                  let uniqueBuffer = [...new Set(buffer)];
                  return uniqueBuffer;
                }else{
                  return [];
                }
            },
            groupUsersListResultQuery(){
                if(this.groupUsersListSearchQuery){
                  let buffer = [];
                  buffer = buffer.concat(this.groupUsersSearch.filter((item)=>{
                    return this.groupUsersListSearchQuery.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
                  }));
                  buffer = buffer.concat(this.groupUsersSearch.filter((item)=>{
                    return this.groupUsersListSearchQuery.split(' ').every(v => item.displayName.includes(v))
                  }));
                  let uniqueBuffer = [...new Set(buffer)];
                  return uniqueBuffer;
                }else{
                  return [];
                }
            },
            allUsersListResultQueryC(){
                if(this.allUsersListSearchQueryC){
                  let buffer = [];
                  buffer = buffer.concat(this.allUsersSearchC.filter((item)=>{
                    return this.allUsersListSearchQueryC.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
                  }));
                  buffer = buffer.concat(this.allUsersSearchC.filter((item)=>{
                    return this.allUsersListSearchQueryC.split(' ').every(v => item.displayName.includes(v))
                  }));
                  let uniqueBuffer = [...new Set(buffer)];
                  return uniqueBuffer;
                }else{
                  return [];
                }
            },
            groupUsersListResultQueryC(){
                if(this.groupUsersListSearchQueryC){
                  let buffer = [];
                  buffer = buffer.concat(this.groupUsersSearchC.filter((item)=>{
                    return this.groupUsersListSearchQueryC.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
                  }));
                  buffer = buffer.concat(this.groupUsersSearchC.filter((item)=>{
                    return this.groupUsersListSearchQueryC.split(' ').every(v => item.displayName.includes(v))
                  }));
                  let uniqueBuffer = [...new Set(buffer)];
                  return uniqueBuffer;
                }else{
                  return [];
                }
            },
        }
    });
})
