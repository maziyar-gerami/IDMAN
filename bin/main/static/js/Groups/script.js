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
            userInfo: [],
            email: "",
            username: "",
            name: "",
            nameEN: "",
            menuSA: false,
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
            s34: "آیا از حذف گروه های انتخاب شده اطمینان دارید؟",
            s35: "هیچ گروهی انتخاب نشده است.",
            s36: "تعداد رکورد ها: ",
            s37: "ممیزی ها",
            s38: "/audits",
            s39: "لیست کاربران",
            s40: "جستجو...",
            s41: "افزودن به گروه",
            s42: "کاربری یافت نشد",
            s43: "لیست اعضای گروه",
            s44: "حذف از گروه",
            U0: "رمز عبور",
            U1: "گروه ها",
            U2: "نام",
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
            this.getUserInfo();
            this.isAdmin();
            this.getUserPic();
            this.getGroups();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
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
                    vm.userInfo = res.data;
                    vm.username = vm.userInfo.userId;
                    vm.name = vm.userInfo.displayName;
                    vm.nameEN = vm.userInfo.firstName + vm.userInfo.lastName;
                    vm.s1 = vm.name;
                });
            },
            isAdmin: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user/isAdmin") //
                  .then((res) => {
                    if(res.data == "0"){
                      vm.menuSA = true;
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
                axios.get(url + "/api/groups") //
                    .then((res) => {
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
                this.editingGroupID = id;
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
            loadAllUsersList: function (flag){
                /*var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                if(flag){
                    this.numberOfLoadAllUsers = this.numberOfLoadAllUsers + 1;
                }
                let i = 0;
                while(i < 10){
                    this.allUsersListPage = this.allUsersListPage + 1;
                    axios.get(url + "/api/users/" + vm.allUsersListPage + "/" + vm.allUsersShownOnPage) //
                        .then((res) => {
                            if(res.data.userList.length == 0){
                                document.getElementById("loadAllUsersListButton").disabled = true;
                                vm.endOfAllUsersList = false;
                            }else{
                                vm.helperArray = res.data.userList.filter(a => !vm.groupUsersSearch.map(b=>b.userId).includes(a.userId));
                                console.log(vm.helperArray);
                                vm.allUsersSearch = vm.allUsersSearch.concat(vm.helperArray);
                            }
                            if(vm.allUsersSearch.length >= (vm.allUsersShownOnPage * vm.numberOfLoadAllUsers)){
                                vm.endOfAllUsersList = false;
                            }
                        });
                    i = i + 1;
                }*/         
            },
            loadGroupUsersList: function (){
                /*var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.groupUsersListPage = this.groupUsersListPage + 1;
                axios.get(url + "/api/users/group/" + vm.editingGroupID, {
                    params: { page: vm.groupUsersListPage,  nRec: vm.groupUsersShownOnPage},
                    })
                    .then((res) => {
                        vm.groupUsersSearch = vm.groupUsersSearch.concat(res.data.userList);
                        if(vm.groupUsersSearch.length >= vm.groupUsersListSize){
                            document.getElementById("loadGroupUsersListButton").disabled = true;
                        }
                    });*/
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin1 = "mr-1";
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
                    this.s23 = "Are You Sure You Want To Edit?";
                    this.s24 = "Are You Sure You Want To Delete?";
                    this.s25 = "Are You Sure You Want To Delete All Groups?";
                    this.s26 = "./privacy?en";
                    this.s27 = "Configs";
                    this.s28 = "./configs?en";
                    this.s29 = "Add Group";
                    this.s30 = "Edit Group";
                    this.s31 = "./events?en";
                    this.s32 = "Delete Group";
                    this.s33 = "Action";
                    this.s34 = "Are You Sure You Want To Delete Selected Groups?";
                    this.s35 = "No Group is Selected.";
                    this.s36 = "Records a Page: ";
                    this.s37 = "Audits";
                    this.s38 = "/audits?en";
                    this.s39 = "Users List";
                    this.s40 = "Search...";
                    this.s41 = "Add to Group";
                    this.s42 = "No User Found";
                    this.s43 = "List of Group Members";
                    this.s44 = "Remove from Group";
                    this.U0= "Password";
                    this.U1= "Groups";
                    this.U2= "ID";
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
                } else{
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin1 = "ml-1";
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
                    this.s23 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s24 = "آیا از حذف این گروه اطمینان دارید؟";
                    this.s25 = "آیا از حذف تمامی گروه ها اطمینان دارید؟";
                    this.s26 = "./privacy";
                    this.s27 = "پیکربندی";
                    this.s28 = "./configs";
                    this.s29 = "افزودن گروه";
                    this.s30 = "ویرایش گروه";
                    this.s31 = "./events";
                    this.s32 = "حذف گروه";
                    this.s33 = "اعمال";
                    this.s34 = "آیا از حذف گروه های انتخاب شده اطمینان دارید؟";
                    this.s35 = "هیچ گروهی انتخاب نشده است.";
                    this.s36 = "تعداد رکورد ها: ";
                    this.s37 = "ممیزی ها";
                    this.s38 = "/audits";
                    this.s39 = "لیست کاربران";
                    this.s40 = "جستجو...";
                    this.s41 = "افزودن به گروه";
                    this.s42 = "کاربری یافت نشد";
                    this.s43 = "لیست اعضای گروه";
                    this.s44 = "حذف از گروه";
                    this.U0 = "رمز";
                    this.U1 = "گروه ها";
                    this.U2 = "شناسه";
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
