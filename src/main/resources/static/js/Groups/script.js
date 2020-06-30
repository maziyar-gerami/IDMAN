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
            U0: "رمز عبور",
            U1: "گروه ها",
            U2: "شناسه",
            U3: "نام",
            U4: "نام خانوادگی (به انگلیسی)",
            U5: "نام کامل (به فارسی)",
            U6: "شماره تماس",
            U7: "ایمیل",
            U8: "کد ملی",
            U9: "توضیحات",
            U10: "به روزرسانی",
            U11: "حذف",
            U12: "اضافه کردن گروه جدید",
            U13: "ویرایش",
            U14: "گروههای عضو",
            U15: "تکرار رمز عبور",
            U16: "کاربر مورد نظر در گروههای زیر عضویت دارد. کاربر مورد نظر از چه گروههایی حذف شود؟",
            U17: "حذف تمامی گروه ها",
            h1: "ترکیبی از حروف و اعداد. مثال: ali123",
            p1: "خیلی ضعیف",
            p2: "متوسط",
            p3: "قوی"
        },
        created: function () {
            this.getUserInfo();
            this.getGroups();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/username") // /idman
                    .then((res) => {
                        vm.username = res.data;
                        axios.get(url + "/api/users/u/" + vm.username) // /idman
                            .then((res) => {
                                vm.userInfo = res.data;
                                vm.name = vm.userInfo.displayName;
                                vm.nameEN = vm.userInfo.firstName;
                                vm.s1 = vm.name;
                            });
                    });
            },
            getGroups: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/groups") // /idman
                    .then((res) => {
                        vm.groups = res.data;
                        vm.total = Math.ceil(vm.groups.length / 2);
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
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/groups/" + id) // /idman
                    .then((res) => {
                        vm.group = res.data;
                    });
            },
            editGroup: function (id) {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var check = confirm("Are you sure you want to edit?");
                if (check == true) {
                    axios({
                        method: 'put',
                        url: url + '/api/groups/' + id, // /idman
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            id: document.getElementById('group.idUpdate').value,
                            name: document.getElementById('group.nameUpdate').value,
                            description: document.getElementById('group.descriptionUpdate').value,
                        }),
                    },);
                }
            },
            addGroupS: function () {
                this.showS = "display:none"
                this.addS = ""
                this.editS = "display:none"
            },
            addGroup: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios({
                    method: 'post',
                    url: url + "/api/groups", // /idman
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify({
                        id: document.getElementById('group.idCreate').value,
                        name: document.getElementById('group.nameCreate').value,
                        description: document.getElementById('group.descriptionCreate').value,
                    }),
                },);
            },
            deleteGroup: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm("Are you sure you want to delete?");
                if (check == true) {
                    axios.delete(url + `/api/groups/${id}`) // /idman
                        .then(() => {
                            vm.getGroups();
                        });
                }
            },
            deleteAllGroups: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm("Are you sure you want to delete all groups?");
                if (check == true) {
                    axios.delete(url + `/api/groups`) // /idman
                        .then(() => {
                            vm.getGroups();
                        });
                }
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
                    this.U12 = "Add New Group";
                    this.U13 = "Edit";
                    this.U17 = "Remove All Groups";
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
                    this.U0= "رمز";
                    this.U1= "گروه ها";
                    this.U2= "شناسه";
                    this.U3= "نام";
                    this.U4= "نام خانوادگی (به انگلیسی)";
                    this.U5= "نام کامل (به فارسی)";
                    this.U6= "شماره تماس";
                    this.U7= "ایمیل";
                    this.U8= "کد ملی";
                    this.U9= "توضیحات";
                    this.U10 = "به روز رسانی";
                    this.U11 = "حذف"
                    this.U12 = "اضافه کردن گروه جدید";
                    this.U13 = "ویرایش";
                    this.U17 = "حذف تمامی گروه ها"
                }
            }
        },
        computed:{
            sortedGroups:function() {
                this.groupsPage = [];
                for(let i = 0; i < 2; ++i){
                    if(i + ((this.currentPage - 1) * 2) <= this.groups.length - 1){
                        this.groupsPage[i] = this.groups[i + ((this.currentPage - 1) * 2)];
                    }
                }
                return this.groupsPage;
            }
        }
    });
})
