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
            userInfo: [],
            email: "",
            username: "",
            name: "",
            nameEN: "",
            users: [],
            message: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            editS: "display:none",
            addS: "display:none",
            showS: "",
            s0: "پارسو",
            s1: "",
            s2: "خروج",
            s3: "بازنشانی رمز عبور",
            s4: "سرویس ها",
            s5: "گروه ها",
            s6: "رویداد ها",
            s7: "تنظیمات",
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
            U16: "کاربر مورد نظر در گروههای زیر عضویت دارد. کاربر مورد نظر از چه گروههایی حذف شود؟",
            U17: "حذف تمامی کاربران",
            h1: "ترکیبی از حروف و اعداد. مثال: ali123",
            p1: "خیلی ضعیف",
            p2: "متوسط",
            p3: "قوی"
        },
        created: function () {
            this.getUserInfo();
            this.refreshUsers();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/idman/username")
                    .then((res) => {
                        vm.username = res.data;
                        axios.get(url + "/idman/api/users/u/" + vm.username)
                            .then((res) => {
                                vm.userInfo = res.data;
                                vm.name = vm.userInfo.displayName;
                                vm.nameEN = vm.userInfo.firstName;
                                vm.s1 = vm.name;
                            });
                    });
            },
            refreshUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                console.log("in refresh users")
                axios.get(url + "/idman/api/users")
                    .then((res) => {
                        vm.users = res.data;


                    });
            },
            showUsers: function () {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
            },
            updateUser: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + `/idman/api/users/u/${id}`)
                    .then((res) => {
                        for(i = 0; i < vm.users.length; ++i){
                            if(vm.users[i].userId == id){
                                vm.users[i].firstName = res.data.firstName;
                                vm.users[i].lastName = res.data.lastName;
                                vm.users[i].displayName = res.data.displayName;
                                vm.users[i].telephoneNumber = res.data.telephoneNumber;
                                vm.users[i].mail = res.data.mail;
                                vm.users[i].memberOf = res.data.memberOf;
                                vm.users[i].userPassword = res.data.userPassword;
                                vm.users[i].description = res.data.description;
                            }
                        }
                    });
            },
            editUserS: function (id) {
                this.showS = "display:none"
                this.addS = "display:none"
                this.editS = ""
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var res;
                axios.get(url + `/idman/api/users/u/${id}`)
                    .then((res) => {
                        vm.editInfo = res.data;
                        vm.editInfo = res.data;
                        vm.editInfo.password = res.data.userPassword;
                        vm.editInfo.phone = res.data.telephoneNumber;
                        populate(res.data.memberOf);

                    });

                function populate(checkedGroups) {

                    axios.get(url + `/idman/api/groups`)
                        .then((res) => {
                                populateTwo(res.data, checkedGroups)
                            }
                        );
                    function populateTwo(allGroups, checkedGroups) {
                        for (var i = 0; i < allGroups.length; i++) {
                            let iDiv = document.createElement('div');
                            iDiv.id = 'block' + i;
                            iDiv.className = 'block';
                            document.getElementById('lsGroups').appendChild(iDiv);
                            var v = document.createElement('input');
                            v.setAttribute("id", "checkbox" + i);
                            v.type = "checkbox";
                            console.log(v.getAttribute("id", "checkbox" + i));
                            v.value = allGroups[i].name;
                            if (checkedGroups!=null) {
                                for (var j = 0; j < checkedGroups.length; j++) {
                                    if ((allGroups[i].name).localeCompare(checkedGroups[j]) == 0) {
                                        v.checked = true;
                                    }
                                }
                            }
                            let l = document.createElement('label');
                            l.setAttribute("for", v.value);
                            l.innerHTML = v.value;
                            document.getElementById('lsGroups').appendChild(v);
                            document.getElementById('lsGroups').appendChild(l);
                            let innerDiv = document.createElement('div');
                            innerDiv.className = 'block-2';
                            iDiv.appendChild(innerDiv);
                        }
                    };
                }


            },
            editUser: function (id) {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var check = confirm("Are you sure you want to edit?");
                if (check == true) {
                    axios({
                        method: 'put',
                        url: url + '/idman/api/users/u/' + id,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            userId: id,
                            firstName: document.getElementById('editInfo.firstNameUpdate').value,
                            lastName: document.getElementById('editInfo.lastNameUpdate').value,
                            displayName: document.getElementById('editInfo.displayNameUpdate').value,
                            telephoneNumber: document.getElementById('editInfo.phoneUpdate').value,
                            mail: document.getElementById('editInfo.mailUpdate').value,
                            userPassword: document.getElementById('editInfo.passwordRetypeUpdate').value,
                            description: document.getElementById('editInfo.descriptionUpdate').value,
                        }),
                    },);

                }


            }
            ,
            addUserS: function () {
                this.showS = "display:none"
                this.addS = ""
                this.editS = "display:none"
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;

                axios.get(url + `/idman/api/groups`)
                    .then((res) => {
                            populate(res.data);
                        }
                    );
                function populate(allGroups) {
                    console.log(allGroups)
                    for (var i = 0; i < allGroups.length; i++) {
                        let iDiv = document.createElement('div');
                        iDiv.id = 'block' + i;
                        iDiv.className = 'block';
                        document.getElementById('lsGroupsAdd').appendChild(iDiv);
                        var v = document.createElement('input');
                        v.setAttribute("id", "checkboxaddpart" + i);
                        v.type = "checkbox";
                        v.value = allGroups[i].name;
                        let l = document.createElement('label');
                        l.setAttribute("for", v.value);
                        l.innerHTML = v.value;
                        document.getElementById('lsGroupsAdd').appendChild(v);
                        document.getElementById('lsGroupsAdd').appendChild(l);
                        let innerDiv = document.createElement('div');
                        innerDiv.className = 'block-2';
                        iDiv.appendChild(innerDiv);
                    }
                };



            },
            addUser: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;

                axios({
                    method: 'post',
                    url: url + "/idman/api/users",
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify({
                        userId: document.getElementById('editInfo.userIdCreate').value,
                        firstName: document.getElementById('editInfo.firstNameCreate').value,
                        lastName: document.getElementById('editInfo.lastNameCreate').value,
                        displayName: document.getElementById('editInfo.displayNameCreate').value,
                        telephoneNumber: document.getElementById('editInfo.phoneCreate').value,
                        mail: document.getElementById('editInfo.mailCreate').value,
                        userPassword: document.getElementById('editInfo.passwordRetypeCreate').value,
                        description: document.getElementById('editInfo.descriptionCreate').value,
                    }),
                },);



            },
            deleteUser: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;

                var check = confirm("Are you sure you want to delete?");
                if (check == true) {
                    axios.delete(url + `/idman/api/users/u/${id}`)
                        .then(() => {
                            vm.refreshUsers();
                        });
                }




            },
            deleteAllUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;

                var check = confirm("Are you sure you want to delete all users?");
                if (check == true) {
                    axios.delete(url + `/idman/api/users`)
                        .then(() => {
                            vm.refreshUsers();
                        });
                }

            },
            changeLang: function () {
                if(this.lang == "EN"){
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.s0 = "Parsso";
                    this.s1 = this.nameEN;
                    this.s2 = "Exit";
                    this.s3 = "Reset Password";
                    this.s4 = "Services";
                    this.s5 = "Groups";
                    this.s6 = "Events";
                    this.s7 = "Settings";
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
                    this.U14 = "Member Of The Groups";
                    this.U15 = "Repeat Password";
                    this.U17 = "Remove all user";
                } else{
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.s0 = "پارسو";
                    this.s1 = this.name;
                    this.s2 = "خروج";
                    this.s3 = "بازنشانی رمز عبور";
                    this.s4 = "سرویس ها";
                    this.s5 = "گروه ها";
                    this.s6 = "رویداد ها";
                    this.s7 = "تنظیمات";
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
                    this.U14 = "گروههای عضو";
                    this.U15 = "تکرار رمز عبور";
                    this.U17 = "حذف تمامی کاربران";
                }
            }
        }
    })
})
