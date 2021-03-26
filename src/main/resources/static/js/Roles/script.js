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
            userListSearch: "",
            userList: [],
            editList: [],
            editUserIdList: [],
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
            getUsers: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                this.loader = true;
                axios({
                    method: 'get',
                    url: url + "/api/roles", //
                })
                .then((res) => {
                    vm.userList = res.data;
                    vm.loader = false;
                });
            },
            editRole: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let role = document.getElementById("role").value;
                if(role == ""){
                    alert(this.noRoleSelectedAlertText);
                }else if(this.editUserIdList.length == 0){
                    alert(this.noUserAddedAlertText);
                }else{
                    let check = confirm(this.confirmationText);
                    if(check){
                        axios({
                            method: 'put',
                            url: url + "/api/roles/" + role, //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                names: vm.editUserIdList
                            }).replace(/\\\\/g, "\\")
                        })
                        .then((res) => {
                            vm.userListSearch = "";
                            vm.userList = [];
                            vm.editList = [];
                            vm.editUserIdList = [];
                            document.getElementById("role").value = "";
                            vm.getUsers();
                        });
                    }
                }
            },
            addUserToList: function (user) {
                let index = this.userList.indexOf(user);
                if (index > -1) {
                    this.editList.unshift(user);
                    this.editUserIdList.unshift(user.userId);
                    this.userList.splice(index, 1);
                }
            },
            removeUserFromList: function (user) {
                let index = this.editList.indexOf(user);
                if (index > -1) {
                    this.editList.splice(index, 1);
                    this.editUserIdList.splice(index, 1);
                    this.userList.unshift(user);
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
