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
            lang: "EN",
            isRtl: true,
            username: "",
            name: "",
            nameEN: "",
            userPicture: "images/PlaceholderUser.png",
            margin: "margin-right: 30px;",
            loader: false,
            isListEmpty: false,
            userListSearch: "",
            userList: [],
            parssoTitleText: "پارسو",
            usernameText: "",
            exitText: "خروج",
            dashboardText: "داشبورد",
            servicesText: "سرویس ها",
            usersText: "کاربران",
            groupsText: "گروه ها",
            rolesText: "نقش ها",
            eventsText: "رویداد ها",
            auditsText: "ممیزی ها",
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
            eventsURLText: "./events",
            auditsURLText: "./audits",
            profileURLText: "./profile",
            settingsURLText: "./settings",
            configsURLText: "./configs",
            rulesURLText: "./rules",
            privacyURLText: "./privacy",
            guideURLText: "./guide",
            s23: "آیا از اعمال این تغییرات اطمینان دارید؟",
            userListText: "لیست کاربران",
            searchText: "جستجو...",
            addToListText: "افزودن به لیست",
            userNotFoundText: "کاربری یافت نشد",
            s43: "لیست اعضای گروه",
            s44: "حذف از لیست",
        },
        created: function () {
            this.getUserInfo();
            this.getUserPic();
            this.getUsers();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user") //
                .then((res) => {
                    vm.username = res.data.userId;
                    vm.name = res.data.displayName;
                    vm.nameEN = res.data.firstName + res.data.lastName;
                    vm.usernameText = vm.name;
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
            getUsers: function (){
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.loader = true;
                axios.get(url + "/api/roles") //
                    .then((res) => {
                        vm.userList = res.data;
                        vm.loader = false;
                    });
            },
            addUserToList: function (user){
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
            removeUserFromList: function (user){
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
            changeLang: function () {
                if(this.lang == "EN"){
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin = "margin-left: 30px;";
                    this.parssoTitleText = "Parsso";
                    this.usernameText = this.nameEN;
                    this.exitText = "Exit";
                    this.dashboardText = "Dashboard";
                    this.servicesText = "Services";
                    this.usersText = "Users";
                    this.groupsText = "Groups";
                    this.rolesText = "Roles";
                    this.eventsText = "Events";
                    this.auditsText = "Audits";
                    this.profileText = "Profile";
                    this.settingsText = "Settings";
                    this.configsText = "Configs";
                    this.rulesText = "Rules";
                    this.privacyText = "Privacy";
                    this.guideText = "Guide";
                    this.dashboardURLText = "./dashboard?en";
                    this.servicesURLText = "./services?en";
                    this.usersURLText = "./users?en";
                    this.groupsURLText = "./groups?en";
                    this.rolesURLText = "./roles?en";
                    this.eventsURLText = "./events?en";
                    this.auditsURLText = "./audits?en";
                    this.profileURLText = "./profile?en";
                    this.settingsURLText = "./settings?en";
                    this.configsURLText = "./configs?en";
                    this.rulesURLText = "./rules?en";
                    this.privacyURLText = "./privacy?en";
                    this.guideURLText = "./guide?en";
                    this.s23 = "Are You Sure You Want To Edit?";
                    this.s39 = "Users List";
                    this.s40 = "Search...";
                    this.s41 = "Add to Group";
                    this.s42 = "No User Found";
                    this.s43 = "List of Group Members";
                    this.s44 = "Remove from Group";
                } else{
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin = "margin-right: 30px;";
                    this.parssoTitleText = "پارسو";
                    this.usernameText = this.name;
                    this.exitText = "خروج";
                    this.dashboardText = "داشبورد";
                    this.servicesText = "سرویس ها";
                    this.usersText = "کاربران";
                    this.groupsText = "گروه ها";
                    this.rolesText = "نقش ها";
                    this.eventsText = "رویداد ها";
                    this.auditsText = "ممیزی ها";
                    this.profileText = "پروفایل";
                    this.settingsText = "تنظیمات";
                    this.configsText = "پیکربندی";
                    this.rulesText = "قوانین";
                    this.privacyText = "حریم خصوصی";
                    this.guideText = "راهنما";
                    this.dashboardURLText = "./dashboard";
                    this.servicesURLText = "./services";
                    this.usersURLText = "./users";
                    this.groupsURLText = "./groups";
                    this.rolesURLText = "./roles";
                    this.eventsURLText = "./events";
                    this.auditsURLText = "./audits";
                    this.profileURLText = "./profile";
                    this.settingsURLText = "./settings";
                    this.configsURLText = "./configs";
                    this.rulesURLText = "./rules";
                    this.privacyURLText = "./privacy";
                    this.guideURLText = "./guide";
                    this.s23 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s39 = "لیست کاربران";
                    this.s40 = "جستجو...";
                    this.s41 = "افزودن به گروه";
                    this.s42 = "کاربری یافت نشد";
                    this.s43 = "لیست اعضای گروه";
                    this.s44 = "حذف از گروه";
                }
            }
        },
        computed:{
            usersListResult(){
                if(this.userListSearch){
                  let buffer = [];
                  buffer = buffer.concat(this.userList.filter((item)=>{
                    return this.userListSearch.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
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
