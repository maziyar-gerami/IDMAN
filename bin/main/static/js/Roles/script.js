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
            lang: "EN",
            isRtl: true,
            dropdownMenu: false,
            dateNav: "",
            dateNavEn: "",
            dateNavText: "",
            username: "",
            name: "",
            nameEN: "",
            userPicture: "images/PlaceholderUser.png",
            margin: "margin-right: 30px;",
            loader: false,
            overlayLoader: false,
            userListSearch: "",
            userList: [],
            editList: [],
            parssoTitleText: "احراز هویت متمرکز شرکت فلات قاره",
            usernameText: "",
            exitText: "خروج",
            dashboardText: "داشبورد",
            servicesText: "سرویس ها",
            usersText: "کاربران",
            groupsText: "گروه ها",
            rolesText: "نقش ها",
            publicmessagesText: "اعلان های عمومی",
            ticketingText: "پشتیبانی",
            eventsText: "رویداد ها",
            auditsText: "ممیزی ها",
            reportsText: "گزارش ها",
            profileText: "پروفایل",
            settingsText: "تنظیمات",
            configsText: "پیکربندی",
            rulesText: "قوانین",
            privacyText: "حریم خصوصی",
            guideText: "راهنما",
            dashboardURLText: "./dashboard",
            servicesURLText: "./services",
            usersURLText: "./users",
            groupsURLText: "./groups",
            rolesURLText: "./roles",
            publicmessagesURLText: "./publicmessages",
            ticketingURLText: "./ticketing",
            eventsURLText: "./events",
            auditsURLText: "./audits",
            reportsURLText: "./reports",
            profileURLText: "./profile",
            settingsURLText: "./settings",
            configsURLText: "./configs",
            rulesURLText: "./rules",
            privacyURLText: "./privacy",
            guideURLText: "./guide",
            userListText: "لیست کاربران",
            searchText: "جستجو...",
            addToListText: "افزودن به لیست",
            userNotFoundText: "کاربری یافت نشد",
            chooseRoleText: "انتخاب نقش",
            submitText: "تایید",
            removeFromListText: "حذف از لیست",
            noUserAddedText: "کاربری در لیست وجود ندارد",
            confirmationText: "آیا از اعمال این تغییرات اطمینان دارید؟",
            noUserAddedAlertText: "کاربری انتخاب نشده است.",
            noRoleSelectedAlertText: "نقشی انتخاب نشده است.",
            userIdText: "شناسه",
            roleText: "نقش",
            superAdminText: "مدیر کل",
            supporterText: "پشتیبانی",
            adminText: "مدیر",
            presenterText: "ارائه دهنده",
            userText: "کاربر",
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
        },
        created: function () {
            this.setDateNav();
            this.getUserInfo();
            this.getUserPic();
            this.getUsers();
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
            getUserInfo: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                axios.get(url + "/api/user") //
                .then((res) => {
                    vm.username = res.data.userId;
                    vm.name = res.data.displayName;
                    vm.nameEN = res.data.firstName + " " + res.data.lastName;
                    if(window.localStorage.getItem("lang") === null || window.localStorage.getItem("lang") === "FA"){
                        vm.usernameText = vm.name;
                    }else if(window.localStorage.getItem("lang") === "EN") {
                        vm.usernameText = vm.nameEN;
                    }
                    if(res.data.skyroomAccess){
                        vm.showMeeting = true;
                    }
                });
            },
            getUserPic: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
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
            getUsers: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let superAdminTempList = [];
                let supporterTempList = [];
                let adminTempList = [];
                let presenterTempList = [];
                let userTempList = [];
                this.loader = true;
                axios({
                    method: "get",
                    url: url + "/api/roles", //
                })
                .then((res) => {
                    for(let i = 0; i < res.data.length; ++i){
                        res.data[i].checked = false;
                        if(res.data[i].role == "SUPERADMIN"){
                            res.data[i].roleFa = "مدیر کل";
                            res.data[i].icon = "color: #dc3545;";
                            superAdminTempList.push(res.data[i]);
                        }else if(res.data[i].role == "SUPPORTER"){
                            res.data[i].roleFa = "پشتیبانی";
                            res.data[i].icon = "color: #28a745;";
                            supporterTempList.push(res.data[i]);
                        }else if(res.data[i].role == "ADMIN"){
                            res.data[i].roleFa = "مدیر";
                            res.data[i].icon = "color: #007bff;";
                            adminTempList.push(res.data[i]);
                        }else if(res.data[i].role == "PRESENTER"){
                            res.data[i].roleFa = "ارائه دهنده";
                            res.data[i].icon = "color: #00f7f7;";
                            presenterTempList.push(res.data[i]);
                        }else if(res.data[i].role == "USER"){
                            res.data[i].roleFa = "کاربر";
                            res.data[i].icon = "color: #ffc107;";
                            userTempList.push(res.data[i]);
                        }
                    }
                    vm.userList = superAdminTempList.concat(supporterTempList, adminTempList, presenterTempList, userTempList);
                    vm.loader = false;
                });
            },
            editRole: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let role = document.getElementById("role").value;
                if(role == ""){
                    alert(this.noRoleSelectedAlertText);
                }else if(this.editList.length == 0){
                    alert(this.noUserAddedAlertText);
                }else{
                    let check = confirm(this.confirmationText);
                    if(check){
                        axios({
                            method: "put",
                            url: url + "/api/roles/" + role, //
                            headers: {"Content-Type": "application/json"},
                            data: JSON.stringify({
                                names: vm.editList
                            }).replace(/\\\\/g, "\\")
                        })
                        .then((res) => {
                            vm.userListSearch = "";
                            vm.userList = [];
                            vm.editList = [];
                            document.getElementById("role").value = "";
                            vm.getUsers();
                        });
                    }
                }
            },
            selectUser: function (user) {
                let editListIndex = this.editList.indexOf(user.userId);
                let userListIndex = this.userList.indexOf(user);

                if (editListIndex > -1) {
                    this.editList.splice(editListIndex, 1);
                    this.userList[userListIndex].checked = false;
                }else{
                    this.editList.push(user.userId);
                    this.userList[userListIndex].checked = true;
                }
            },
            preventCheckbox(e){
                e.preventDefault();
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    window.localStorage.setItem("lang", "EN");
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin = "margin-left: 30px;";
                    this.dateNavText = this.dateNavEn;
                    this.parssoTitleText = "IOOC Centralized Authentication";
                    this.usernameText = this.nameEN;
                    this.exitText = "Exit";
                    this.dashboardText = "Dashboard";
                    this.servicesText = "Services";
                    this.usersText = "Users";
                    this.groupsText = "Groups";
                    this.rolesText = "Roles";
                    this.publicmessagesText = "Public Messages";
                    this.ticketingText = "Ticketing";
                    this.eventsText = "Events";
                    this.auditsText = "Audits";
                    this.reportsText = "Reports";
                    this.profileText = "Profile";
                    this.settingsText = "Settings";
                    this.configsText = "Configs";
                    this.rulesText = "Rules";
                    this.privacyText = "Privacy";
                    this.guideText = "Guide";
                    this.userListText = "Users List";
                    this.searchText = "Search...";
                    this.addToListText = "Add To List";
                    this.userNotFoundText = "No User Found";
                    this.chooseRoleText = "Select Role";
                    this.submitText = "Submit";
                    this.removeFromListText = "Remove From List";
                    this.noUserAddedText = "No User In The List";
                    this.confirmationText = "Are You Sure You Want To Edit?";
                    this.noUserAddedAlertText = "No User Selected.";
                    this.noRoleSelectedAlertText = "No Role Selected.";
                    this.userIdText = "UserId";
                    this.roleText = "Role";
                    this.superAdminText = "SUPERADMIN";
                    this.supporterText = "SUPPORTER";
                    this.adminText = "ADMIN";
                    this.presenterText = "PRESENTER";
                    this.userText = "USER";
                    this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingText = "Meeting";
                    this.enterMeetingText = "Enter Meeting";
                    this.inviteToMeetingText = "Invite To Meeting";
                    this.copyText = "Copy";
                    this.returnText = "Return";
                } else{
                    window.localStorage.setItem("lang", "FA");
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin = "margin-right: 30px;";
                    this.dateNavText = this.dateNav;
                    this.parssoTitleText = "احراز هویت متمرکز شرکت فلات قاره";
                    this.usernameText = this.name;
                    this.exitText = "خروج";
                    this.dashboardText = "داشبورد";
                    this.servicesText = "سرویس ها";
                    this.usersText = "کاربران";
                    this.groupsText = "گروه ها";
                    this.rolesText = "نقش ها";
                    this.publicmessagesText = "اعلان های عمومی";
                    this.ticketingText = "پشتیبانی";
                    this.eventsText = "رویداد ها";
                    this.auditsText = "ممیزی ها";
                    this.reportsText = "گزارش ها";
                    this.profileText = "پروفایل";
                    this.settingsText = "تنظیمات";
                    this.configsText = "پیکربندی";
                    this.rulesText = "قوانین";
                    this.privacyText = "حریم خصوصی";
                    this.guideText = "راهنما";
                    this.userListText = "لیست کاربران";
                    this.searchText = "جستجو...";
                    this.addToListText = "افزودن به لیست";
                    this.userNotFoundText = "کاربری یافت نشد";
                    this.chooseRoleText = "انتخاب نقش";
                    this.submitText = "تایید";
                    this.removeFromListText = "حذف از لیست";
                    this.noUserAddedText = "کاربری در لیست وجود ندارد";
                    this.confirmationText = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.noUserAddedAlertText = "کاربری انتخاب نشده است.";
                    this.noRoleSelectedAlertText = "نقشی انتخاب نشده است.";
                    this.userIdText = "شناسه";
                    this.roleText = "نقش";
                    this.superAdminText = "مدیر کل";
                    this.supporterText = "پشتیبانی";
                    this.adminText = "مدیر";
                    this.presenterText = "ارائه دهنده";
                    this.userText = "کاربر";
                    this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingText = "جلسه مجازی";
                    this.enterMeetingText = "ورود به جلسه";
                    this.inviteToMeetingText = "دعوت به جلسه";
                    this.copyText = "کپی";
                    this.returnText = "بازگشت";
                }
            }
        },
        computed:{
            usersListResult(){
                if(this.userListSearch){
                  let buffer = [];
                  buffer = buffer.concat(this.userList.filter((item)=>{
                    return this.userListSearch.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v))
                  }));
                  let uniqueBuffer = [...new Set(buffer)];
                  return uniqueBuffer;
                }else{
                  return [];
                }
            }
        }
    });
})